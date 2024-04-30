function datePickerSet(){
	$('.startPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:'다음 달',
		prevText:'이전 달',
		dayNames:['월요일','화요일','수요일','목요일','금요일','토요일','일요일'],
		dayNamesMin:['일','월','화','수','목','금','토'],
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		showMonthAfterYear:true,
		yearSuffix:'년',
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'',
	})
	
	$('.endPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:'다음 달',
		prevText:'이전 달',
		dayNames:['월요일','화요일','수요일','목요일','금요일','토요일','일요일'],
		dayNamesMin:['일','월','화','수','목','금','토'],
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		showMonthAfterYear:true,
		yearSuffix:'년',
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'',
	})
	
	$('.startPicker').datepicker();
    $('.startPicker').datepicker("option", "onClose", function ( selectedDate ) {
		$('.startPicker').attr("data-start-date",selectedDate + " 00:00:00");
        $(".endPicker").datepicker( "option", "minDate", selectedDate );
    });

    $('.endPicker').datepicker();
    $('.endPicker').datepicker("option", "minDate", $(".startPicker").val());
    $('.endPicker').datepicker("option", "onClose", function ( selectedDate ) {
		$('.endPicker').attr("data-end-date",selectedDate + " 00:00:00");
        $(".startPicker").datepicker( "option", "maxDate", selectedDate );
    });
    
    $('.startPicker, .endPicker').on('change', function(){
		$('.startMinute').find('option').prop("disabled", false);
	})	
	
}

$(function(){
	datePickerSet();
})
