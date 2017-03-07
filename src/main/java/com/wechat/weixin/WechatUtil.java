package com.wechat.weixin;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
/**
 * Created by Administrator on 2016/8/22.
 */
public class WechatUtil {

        private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
        // 凭证获取（GET）
      
        /**
         * 发送https请求
         *
         * @param requestUrl 请求地址
         * @param requestMethod 请求方式（GET、POST）
         * @param outputStr 提交的数据
         * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
         */
        public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
            JSONObject jsonObject = null;
            try {
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                TrustManager[] tm = { new MyX509TrustManager() };
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, tm, new java.security.SecureRandom());
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                URL url = new URL(requestUrl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(ssf);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                // 设置请求方式（GET/POST）
                conn.setRequestMethod(requestMethod);
                // 当outputStr不为null时向输出流写数据
                if (null != outputStr) {
                    OutputStream outputStream = conn.getOutputStream();
                    // 注意编码格式
                    outputStream.write(outputStr.getBytes("UTF-8"));
                    outputStream.close();
                }
                // 从输入流读取返回内容
                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                // 释放资源
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                inputStream = null;
                conn.disconnect();
                jsonObject = JSONObject.parseObject(buffer.toString());
            } catch (ConnectException ce) {
                log.error("连接超时：{}", ce);
            } catch (Exception e) {
                log.error("https请求异常：{}", e);
            }
            return jsonObject;
        }
        /**
         * 获取接口访问凭证
         *
         * @param appid 凭证
         * @param appsecret 密钥
         * @return
         */
        private static Map<String,Object> sessionMap = null;
        public static Token getToken(String appid, String appsecret) {
        	if(sessionMap==null){
        		sessionMap = new HashMap<String,Object>();
        	}
        	//系统当前时间  秒
        	long nowTime = System.currentTimeMillis()/1000;
        	//获取sessionMap 里的token
        	Object access_token = sessionMap.get("access_token");
        	//获取过期时间
        	Object time = sessionMap.get("expireTime");
        	long remainTime = 0;
        	if(time!=null){
        		long expireTime = Long.parseLong(time.toString());
        		remainTime = expireTime-nowTime;
        	}
        	if(access_token==null||remainTime<=0){
	            Token token = null;
	            String requestUrl = URLPATH.token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
	            // 发起GET请求获取凭证
	            JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
	            if (null != jsonObject) {
	                try {
	                    token = new Token();
	                    token.setAccessToken(jsonObject.getString("access_token"));
	                    token.setExpiresIn(jsonObject.getInteger("expires_in"));
	                    sessionMap.put("access_token", token);
	    	        	String ticket_url_new = URLPATH.ticket_url.replace("ACCESS_TOKEN", token.getAccessToken());
	    	        	JSONObject json = httpsRequest(ticket_url_new, "GET", null);
	    	        	String ticket = json.getString("ticket");
	    	        	sessionMap.put("ticket", ticket);
	    	        	//当前时间+7000s = token过期时间
	    	        	sessionMap.put("expireTime", nowTime+7000);
	                } catch (JSONException e) {
	                    token = null;
	                    // 获取token失败
	                    log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
	                }
	            }
	            log.info("获取token并放入session");
	            return token;
        	}else{
        		log.info("获取缓存token");
        		return (Token)access_token;
        	}
        }
        
        public static OAuthVO getOAuthVO( String appsecret,String appid,String code,HttpSession session) {
        	
        	OAuthVO token =  (OAuthVO)session.getAttribute("userToken");
	        if(token!=null&&!token.getAccess_token().equals("")){
	        	log.info("返回session中OAuthVO");
	        	return token;
	        }
	        
	        String requestUrl = URLPATH.user_token_url.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
	        // 发起GET请求获取凭证
	        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
         
            token = setUserToken(jsonObject,session);
            session.setAttribute("userToken", token);
            session.setMaxInactiveInterval(3600*2);
           
			return token;
        }
        
        public static OAuthVO getRefreshToken(OAuthVO token,String appid,HttpSession session){
	        // 发起GET请求获取凭证
        	JSONObject jsonObject = null;
            String refreshUrl = URLPATH.refresh_token_url.replace("APPID", appid).replace("REFRESH_TOKEN", token.getRefresh_token());
            jsonObject = httpsRequest(refreshUrl, "GET", null);
            token = setUserToken(jsonObject,session);
            session.setAttribute("userToken", token);
        	session.setMaxInactiveInterval(3600*2);
            
			return token;
        }
        
        private static OAuthVO setUserToken(JSONObject jsonObject,HttpSession session){
        	 OAuthVO token = new OAuthVO();
        	 try{
				 token.setAccess_token(jsonObject.getString("access_token"));
				 token.setExpires_in(jsonObject.getInteger("expires_in"));
				 token.setOpenid(jsonObject.getString("openid"));
				 token.setRefresh_token(jsonObject.getString("refresh_token"));
				 token.setScope(jsonObject.getString("scope"));
        	 }catch(Exception e){
        		 log.error("获取OAuthVO失败  errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
        		 token = (OAuthVO)session.getAttribute("userToken");
        		 log.info("获取OAuthVO失败，尝试获取session的OAuthVO");
        	 }
			 return token;
        }
        
        public static Ticket getTicket(Wechat wechat,String url){
        	WechatUtil.getToken(wechat.getAppid(), wechat.getSecret());
        	String ticket = sessionMap.get("ticket").toString();
        	log.info("获取ticket ->"+ticket);
        	Ticket access_ticket = sign(ticket,url,wechat.getAppid());
        	log.info("生成access_ticket ->"+ticket);
	        return access_ticket;
        }
        
        public static String getUserOpenid(String secret, String appid,String code){
        	JSONObject jsonObject = null;
        	String requestUrl = "";
        	String userOpenid = "";
        	try{
    	    	requestUrl = URLPATH.USER_OPENID_URL.replace("SECRET", secret).replace("APPID", appid).replace("CODE", code);
    	    	jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
    	    	userOpenid = jsonObject.getString("openid");
        	}catch(Exception e){
        		int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
        		log.error("获取useropenid失败 errcode:{} errmsg:{}", errorCode, errorMsg);
        	}
        	return userOpenid;
        }

        public static boolean isAccessToken(String accessToken, String openId){
        	String url = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
        	String requestUrl = url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        	JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
        	return jsonObject.getInteger("errcode")==0;
        }
        public static UserVO getUserInfo(String accessToken, String openId) {
            UserVO weixinUserInfo = null;
            // 拼接请求地址
            String requestUrl = "";
            requestUrl = URLPATH.USER_INFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
            // 获取用户信息
            JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
            if (null != jsonObject) {
                try {
                    weixinUserInfo = new UserVO();
                    // 用户的标识
                    weixinUserInfo.setOpenId(jsonObject.getString("openid"));
                    // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                    //weixinUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
                    // 用户关注时间
                    //weixinUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
                    // 昵称
                    weixinUserInfo.setNickname(jsonObject.getString("nickname"));
                    // 用户的性别（1是男性，2是女性，0是未知）
                    weixinUserInfo.setSex(jsonObject.getInteger("sex"));
                    // 用户所在国家
                    weixinUserInfo.setCountry(jsonObject.getString("country"));
                    // 用户所在省份
                    weixinUserInfo.setProvince(jsonObject.getString("province"));
                    // 用户所在城市
                    weixinUserInfo.setCity(jsonObject.getString("city"));
                    // 用户的语言，简体中文为zh_CN
                    //weixinUserInfo.setLanguage(jsonObject.getString("language"));
                    // 用户头像
                    weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                } catch (Exception e) {
                	int errorCode = jsonObject.getInteger("errcode");
                    String errorMsg = jsonObject.getString("errmsg");
                    log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
                }
            }
            return weixinUserInfo;
        }
        
        
        
        private static Ticket sign(String jsapi_ticket, String url,String appId) {
            String nonce_str = create_nonce_str();
            String timestamp = create_timestamp();
            String string1;
            String signature = "";

            //注意这里参数名必须全部小写，且必须有序
            string1 = "jsapi_ticket=" + jsapi_ticket +
                      "&noncestr=" + nonce_str +
                      "&timestamp=" + timestamp +
                      "&url=" + url;
            System.out.println(string1);

            try
            {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(string1.getBytes("UTF-8"));
                signature = byteToHex(crypt.digest());
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            Ticket access_ticket = new Ticket(appId, timestamp, nonce_str, signature);

            return access_ticket;
        }

        private static String byteToHex(final byte[] hash) {
            Formatter formatter = new Formatter();
            for (byte b : hash)
            {
                formatter.format("%02x", b);
            }
            String result = formatter.toString();
            formatter.close();
            return result;
        }

        private static String create_nonce_str() {
            return UUID.randomUUID().toString();
        }

        private static String create_timestamp() {
            return Long.toString(System.currentTimeMillis() / 1000);
        }
        public static void main(String[] args) {
			System.out.println(System.currentTimeMillis()/1000);
		}
        
        /**
         * URL编码（utf-8）
         *
         * @param source
         * @return
         */
        public static String urlEncodeUTF8(String source) {
            String result = source;
            try {
                result = java.net.URLEncoder.encode(source, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }
        /**
         * 根据内容类型判断文件扩展名
         *
         * @param contentType 内容类型
         * @return
         */
        public static String getFileExt(String contentType) {
            String fileExt = "";
            if ("image/jpeg".equals(contentType))
                fileExt = ".jpg";
            else if ("audio/mpeg".equals(contentType))
                fileExt = ".mp3";
            else if ("audio/amr".equals(contentType))
                fileExt = ".amr";
            else if ("video/mp4".equals(contentType))
                fileExt = ".mp4";
            else if ("video/mpeg4".equals(contentType))
                fileExt = ".mp4";
            return fileExt;
        }
    }

