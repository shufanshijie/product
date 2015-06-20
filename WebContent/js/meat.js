function action(url, type, dataType, success, error, before, data) {
	$.ajax({
		async : true,
		url : url, // 跨域URL
		type : type,
		dataType : dataType,
		data : data,
		timeout : 5000,
		crossdomain : true,
		success : success,
		error : error,
		beforeSend : before
	});
}
$(document).click(function(event){
	if($(event.target).hasClass('page_back')){
		window.history.go(-1);
	}
});

function getGotoPayOffUrl(mealId,page){
	var GotoPayOff="mealDetail/"+mealId+"/"+page;
	return GotoPayOff;
}