package com.wechat.weixin;

/**
 * Created by Administrator on 2016/8/22.
 */
public class Token {
    private String accessToken;
    // 凭证有效期，单位：秒
    private int expiresIn;
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public int getExpiresIn() {
        return expiresIn;
    }
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}