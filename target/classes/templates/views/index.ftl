<!doctype html>
<html lang="zh-CN">
	 <head>
        <meta name="viewport" content="maximum-scale=1,user-scalable=no">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"></meta>
        <title>微信分享</title>
        <link rel="stylesheet" type="text/css" href="${basePath}/static/css/mobile.css"></link>
        <link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"></link>
        <script src="${basePath}/static/js/jquery-1.js"></script>
		<script src="${basePath}/static/js/ort.js"></script><meta name="viewport" id="viewport" content="target-densitydpi=53.33333333333333, width=640, user-scalable=no">
		<script src="${basePath}/static/js/jquery.js"></script>
		<script src="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"></script>
        <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    </head>
	
	<body>
		<base id="basePath" href="basePath"></base>
	 	hello
	</body>
	<script>
	 /*
	  * 注意：
	  * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
	  * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
	  * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	  *
	  * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
	  * 邮箱地址：weixin-open@qq.com
	  * 邮件主题：【微信JS-SDK反馈】具体问题
	  * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
	  */
	 var basePath = '${basePath}';
	 wx.config({
	     debug: false,
	     appId: '${access_ticket.appId}',
	     timestamp: '${access_ticket.timestamp}',
	     nonceStr: '${access_ticket.nonceStr}',
	     signature: '${access_ticket.signature}',
	     jsApiList: [
	         // 所有要调用的 API 都要加到这个列表中
	         'checkJsApi',
	         'onMenuShareTimeline',
	         'onMenuShareAppMessage',
	         'onMenuShareQQ',
	         'onMenuShareWeibo'
	     ]
	 });
	 wx.ready(function () {
	     wx.checkJsApi({
	         jsApiList: [
	             'onMenuShareTimeline',
	         ]
	     });
	     wx.onMenuShareTimeline({
	         title: '分享',
	         link: url,
	         imgUrl: '/static/images/a.jpg',
	         desc : '打造数字银行，实现智能营销',
	         trigger : function(res) {
	
	         },
	         success : function(res) {
	   	
	             //alert('已分享');
	         },
	         cancel : function(res) {
	            // alert('已取消');
	         },
	         fail : function(res) {
	            // alert('wx.onMenuShareTimeline:fail: '+ JSON.stringify(res));
	         }
	     });
	     wx.onMenuShareAppMessage({
	         title: '策略营销系统',
	         link: url,
	         imgUrl: basePath+'/static/images/a.jpg',
	         desc : '打造数字银行，实现智能营销',
	         type : '', //
	         dataUrl : '', //
	         success : function() {
	        	 //alert('已分享');
	         },
	         cancel : function() {
	        	 //alert('已取消');
	             //
	         }
	     });
	
	 });
	 wx.error(function(res) {
	     //alert('wx.error: ' + JSON.stringify(res));
	 });
</script>
</html>