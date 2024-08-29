function datePickerSet(){
	//const siteIdWrap = $('#select-siteId-wrap');
    
    function handlePickerSelect(dateText, inst) {
        if ($(this).hasClass('tag-invalid')) {
            $(this).removeClass('tag-invalid');
        }
        
        // monthStartPicker와 monthEndPicker의 값을 확인
        const startPickerValue = $(".startPullPicker").datepicker("getDate");
        const endPickerValue = $(".endPullPicker").datepicker("getDate");
        const endMinPickerValue = $(".endMinPicker").datepicker("getDate");
        
        if (startPickerValue && endPickerValue) {
            if ($(this).data('type') === 'statistics') {
               // siteIdWrap.removeClass('none');
               getSiteIdList();
            }
        }
    }

	$('.startPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'0D',
	})
	
	$('.startPullPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'0D',
		 onSelect: function(dateText, inst) {
            updateEndPickerMinDate(dateText);
            handlePickerSelect.call(this, dateText, inst);
        }
	})
	
	$('.surveyStartPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		minDate:'0D',
	})
	
	$('.endPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'',
	})
	
		$('.endMinPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'',
		minDate: 0,
		onSelect: function(dateText, inst) {
            updateEndPickerMinDate(dateText);
            handlePickerSelect.call(this, dateText, inst);
        }
	})
	
	$('.endPullPicker').datepicker({
		dateFormat:'yy-mm-dd',
		changMonth:true,
		changYear:true,
		nextText:message.date.nextText,
		prevText:message.date.prevText,
		dayNames:message.date.dayNames,
		dayNamesMin:message.date.dayNamesMin,
		monthNamesShort:['1','2','3','4','5','6','7','8','9','10','11','12'],
		monthNames:message.date.monthNames,
		showMonthAfterYear:true,
		yearSuffix:message.date.yearSuffix,
	    format: 'YYYY',
		minViewMode: 'years',
	    viewMode: "years",
		maxDate:'',
		 onSelect: function(dateText, inst) {
            updateEndPickerMinDate(dateText);
            handlePickerSelect.call(this, dateText, inst);
        }
	})
	
	$('.startPicker, .startPullPicker, .surveyStartPicker').datepicker();
    $('.startPicker, .startPullPicker, .surveyStartPicker').datepicker("option", "onClose", function ( selectedDate ) {
		$('.startPicker, .startPullPicker, .surveyStartPicker').attr("data-start-date",selectedDate + " 00:00:00");
        $(".endPicker, .endPullPicker").datepicker( "option", "minDate", selectedDate );
    });

    $('.endPicker, endMinPicker, .endPullPicker').datepicker();
    $('.endPicker, endMinPicker, .endPullPicker').datepicker("option", "minDate", $(".startPicker, .surveyStartPicker").val());
    $('.endPicker, endMinPicker, .endPullPicker').datepicker("option", "onClose", function ( selectedDate ) {
		$('.endPicker, endMinPicker, .endPullPicker').attr("data-end-date",selectedDate + " 00:00:00");
        $(".startPicker, .startPullPicker, .surveyStartPicker").datepicker( "option", "maxDate", selectedDate );
    });
    
    $('.startPicker, .startPullPicker, .endPicker, endMinPicker, .endPullPicker, .surveyStartPicker').on('change', function(){
		$('.startMinute').find('option').prop("disabled", false);
	});
	
}

let monthPicker = () => {
	//const siteIdWrap = $('#select-siteId-wrap');
    
    function handlePickerSelect(dateText, inst) {
        if ($(this).hasClass('tag-invalid')) {
            $(this).removeClass('tag-invalid');
        }
        
        // monthStartPicker와 monthEndPicker의 값을 확인
        const startPickerValue = $(".monthStartPicker").monthpicker("getDate");
        const endPickerValue = $(".monthEndPicker").monthpicker("getDate");
        
        if (startPickerValue && endPickerValue) {
            if ($(this).data('type') === 'statistics') {
                //siteIdWrap.removeClass('none');
                 getSiteIdList();
            }
        }
    }

    $(".monthStartPicker").monthpicker({
        monthNames: message.date.monthNamesWithAbbrev,
        monthNamesShort: message.date.monthNames,
        changeYear: false,
        yearRange: 'c-2:c+2',
        dateFormat: 'yy-mm',
        onSelect: function(dateText, inst) {
            updateEndPickerMinDate(dateText);
            handlePickerSelect.call(this, dateText, inst);
        }
    });

    $(".monthEndPicker").monthpicker({
        monthNames: message.date.monthNamesWithAbbrev,
        monthNamesShort: message.date.monthNames,
        changeYear: false,
        yearRange: 'c-2:c+2',
        dateFormat: 'yy-mm',
        onSelect: function(dateText, inst) {
            updateStartPickerMaxDate(dateText);
            handlePickerSelect.call(this, dateText, inst);
        }
    });
    
    $(".monthStartPicker, .monthEndPicker").monthpicker('widget').addClass('ui-datepicker-month-year');
    $(".monthStartPicker").monthpicker('setDate', null);
    $(".monthEndPicker").monthpicker('setDate', null);
}

function updateEndPickerMinDate(startDate) {
    if(startDate) {
        let [year, month] = startDate.split('-');
        month = parseInt(month, 10);

        if (month === 12) {
            year = parseInt(year, 10) + 1;
            month = 1;
        } else {
            month += 1;
        }

        let minDate = `${year}-${month < 10 ? '0' + month : month}`;
        $(".monthEndPicker").monthpicker('option', 'minDate', minDate);
    }
}
function updateStartPickerMaxDate(endDate) {
    if(endDate) {
        let [year, month] = endDate.split('-');
        month = parseInt(month, 10);

        if (month === 1) {
            year = parseInt(year, 10) - 1;
            month = 12;
        } else {
            month -= 1;
        }

        let maxDate = `${year}-${month < 10 ? '0' + month : month}`;
        $(".monthStartPicker").monthpicker('option', 'maxDate', maxDate);
    }    
}

$(function(){
	datePickerSet();
})



