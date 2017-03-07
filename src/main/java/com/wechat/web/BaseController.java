package com.wechat.web;

import com.wechat.weixin.Wechat;

public class BaseController {
	
	public Wechat getWechat(){
		Wechat wechatVO= new Wechat();
		wechatVO.setAppid("wx13853ab8ad83ec89");
		wechatVO.setSecret("dc26e6dd51eca9465ecd4d267073f9fb");
		return wechatVO;
	}
}
