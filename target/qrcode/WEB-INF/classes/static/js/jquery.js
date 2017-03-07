function openPOP(obj){
	if($('#bg').size() <= 0){
		$("body").append("<div id='bg'></div>");
	}
	$("#bg").fadeIn(100);
	$(obj).show();
	setTimeout(function(){
		$(obj).css("opacity","1");
		$(obj).css("-webkit-transform","scale(1)").css("-moz-transform","scale(1)").css("-o-transform","scale(1)").css("-ms-transform","scale(1)").css("transform","scale(1)");
	},10);
}
function closePOP(){
	$("#bg").fadeOut(100);
	$(".pop").css("opacity","0");
	$(".pop").css("-webkit-transform","scale(0)").css("-moz-transform","scale(0)").css("-o-transform","scale(0)").css("-ms-transform","scale(0)").css("transform","scale(0)");
	setTimeout(function(){
		$(".pop").fadeOut(100);
	},600);
}
