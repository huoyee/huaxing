/**
 * 
 */

$(document).ready(function(){
	$("#kaptcha").on("click",function(){
		$("#kaptcha").attr('src',"/static/images/kaptcha.jpg?"+Math.floor(Math.random()*100)); 
	});

	$("#btnQuery").on("click",function(){
		var basePath = document.getElementById("basePath").href;
		var code = $("#codeValue").val();
		var kaptcha = $("#kaptchaValue").val();
		var userOpenID = $("#userOpenID").val();
		
		$.post(basePath+"/qrcode/query",{openId:userOpenID,code:code,kachep:kaptcha},function(result){
			var data = eval("("+result+")");
			if(data.state==null){
				var product = data.productVO;
				var count = data.inquireVO.count;
				var html = "<span>您所查询的防伪码（为第<span>"+count+"</span>次查询）</span><br>";
				
				html+="<span class='c'>"+code+"</span>存在<br><hr>";
				html+="<span>产品型号："+product.model+"</span><br>";
				html+="<span>生产日期："+product.produceDate+"</span><br>";
				html+="<span>查询日期："+data.queryDate+"</span><br>";
				$("#productInfo").html(html);
				$("#productInfo").css('display','block'); 
				$("#kaptcha").attr('src',rootPath+"/kaptcha?"+Math.floor(Math.random()*100)); 
			}else{
				alert(data.message);
				$("#kaptcha").attr('src',rootPath+"/kaptcha?"+Math.floor(Math.random()*100)); 
				$("#productInfo").html();
				$("#productInfo").css('display','none'); 
			}
		});
	});
});