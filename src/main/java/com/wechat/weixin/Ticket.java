package com.wechat.weixin;

public class Ticket {
	
	private String appId;//: '<?php echo $signPackage["appId"];?>',
    private String timestamp;//: <?php echo $signPackage["timestamp"];?>,
    private String nonceStr;//: '<?php echo $signPackage["nonceStr"];?>',
    private String signature;//: '<?php echo $signPackage["signature"];?>',
    
    public Ticket(){
    	
    }
    
	public Ticket(String appId, String timestamp, String nonceStr,
			String signature) {
		super();
		this.appId = appId;
		this.timestamp = timestamp;
		this.nonceStr = nonceStr;
		this.signature = signature;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "Ticket [appId=" + appId + ", timestamp=" + timestamp
				+ ", nonceStr=" + nonceStr + ", signature=" + signature + "]";
	}
    
    
}
