<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="maximum-scale=1,user-scalable=no">
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"></meta>
        <title>防伪查询</title>
        <link rel="stylesheet" type="text/css" href="${basePath}/static/css/mobile.css"></link>
        <#--<link rel="stylesheet" type="text/css" href="http://cdn.bootcss.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"></link>-->
        <script src="${basePath}/static/js/jquery-1.js"></script>
		<script src="${basePath}/static/js/ort.js"></script><meta name="viewport" id="viewport" content="target-densitydpi=53.33333333333333, width=640, user-scalable=no">
		<script src="${basePath}/static/js/jquery.js"></script>
        <script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
        <script type="text/javascript" src="${basePath}/static/js/index.js"></script>
    </head>
    <body>
    <div id="Div1">
    	<base id="basePath" href="${basePath}"></base>
		<div class="sh">
			<div>
				<img class="logo" src="${basePath}/static/images/logo.png">
			</div>
			<div class="fw">
				<p style="float:left">防伪码：</p> <#--<button class="btn" style="float:right;vertical-align:middle;" type="button">扫一扫</button>-->
			</div>
			<div><input class="input-fwm" id="codeValue" type="text" value="${code }"></div>
			<p>验证码：</p>
			<div class="fw">
				<input class="input-code" id="kaptchaValue" type="text" value="">
				<img id="kaptcha" class="kaptcha" src="${basePath}/static/images/kaptcha.jpg">
			</div>
			<div align="center" ><img style="padding: 1.5em 0;" id="btnQuery" src="${basePath}/static/images/btn-query.jpg"></div>
			<div id="productInfo" class="output" style="display:none;"></div>
			<input type="hidden" id="userOpenID" value="${userOpenID }">
		</div>
	</div>
    </body>
</html>
