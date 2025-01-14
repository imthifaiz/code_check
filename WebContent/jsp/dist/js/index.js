$('.fo-login input[type="password"],input[type="text"]').on("focus", function() {
    $(this).parent("div").children("label").removeClass("slideup")
}), $(".fo-login input").on("blur", function() {
    (null == $(this).val() || "" == $(this).val()) && $(this).parent("div").children("label").addClass("slideup")
}), $(".fo-login div.effect").each(function(t) {
    var e = $(this).children("input, textarea").val();
    null == e || "" == e ? $(this).children("label").addClass("slideup") : $(this).children("label").removeClass("slideup")
}), $("document").ready(function() {
	$(".fo-login input.form-control").each(function(){
		if (null != $(this).val() && "" != $(this).val()){
			$(this).parent("div").children("label").removeClass("slideup")
		}
	}
)
});