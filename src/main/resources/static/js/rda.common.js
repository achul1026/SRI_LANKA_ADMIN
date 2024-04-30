//중복클릭 전역변수
let preventDuplicate;

$(function(){
	//뒤로가기 페이지 리로드
	window.onpageshow = function(event) {
		if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
		    window.location.reload()
		}		
	}
	
	//중복클릭 전역변수
	preventDuplicate = document.querySelector('.overLapping');


	selectMinuteSet();
	
	//autoCompleteOff
	document.querySelectorAll('input').forEach(auto => auto.setAttribute('autocomplete', 'off'))
})


isNull = function(value) {
    return (value === undefined || value === null || value === '') ? true : false;
}

// selectTime
function selectMinuteSet(){
	let hourOption = "";
	let minuteOption = "";
	let minuteByMinute = "";
	const minutes = [0, 15, 30, 45];
	
	for(i = 0; i < 24; i++){
		if(i < 10){
			hourOption += "<option value=0"+i+">0"+i+"시</option>";
		}else{
			hourOption += "<option value="+i+">"+i+"시</option>";
		}
	}
/*	for(i = 0; i <= 5; i++){
		minuteOption += "<option value="+i+"0>"+i+"0분</option>";
	}*/
	for(var i = 0; i < minutes.length; i++){
	    const value = minutes[i] < 10 ? "0" + minutes[i] : minutes[i];
	    minuteOption += "<option value='"+value+"'>"+value+"분</option>";
	}	
	for(i = 0; i <= 59; i++){
		if(i < 10){
			minuteByMinute += "<option value=0"+i+">0"+i+"분</option>";
		} else {
			minuteByMinute += "<option value="+i+">"+i+"분</option>";
		}
	}
	$('.select-hour').append(hourOption);
	$('.select-minute').append(minuteOption);
	$('.select-minute-unit').append(minuteByMinute);
	
	const startHour 	= $('.startHour').data('start-hour');
	const startMinute 	= $('.startMinute').data('start-minute');
	const endHour	 	= $('.endHour').data('end-hour');
	const endMinute 	= $('.endMinute').data('end-minute');
	
	if(!isNull(startHour)) 		$('.startHour').val(startHour) 
	if(!isNull(startMinute)) 	$('.startMinute').val(startMinute) 
	if(!isNull(endHour)) 		$('.endHour').val(endHour) 
	if(!isNull(endMinute))	 	$('.endMinute').val(endMinute) 
	
} 

// paging
function pageMove(pageNo){
	$("input[name='pageNo']").val(pageNo);
	document.searchForm.submit();
}
//비동기 paing
function pageMoveForAsynchronous(pageNo){
	$("#asynchronousPageNo").val(pageNo);
	asynchronousSearchList();
}

// toggle active
function toggleActive(idName){
	$(idName).toggleClass('active');
}

//click => checked
function checkedSet(_this, type = 'radio') {
	const checkedThis = _this.querySelector(`input[type="${type}"]`);
	const checkedValue = checkedThis.checked;
	if(type === 'checkbox') {
		if(checkedValue === false){
			checkedThis.checked = true;
		} else {
			checkedThis.checked = false;
		}
	} else if(type === 'radio'){
		checkedThis.checked = true;
	}
}

//date format 
function dateFormatForYYYYMMDD(paramDate, delimiter = '-') {
    const year = paramDate.getFullYear();
    const month = leftPad(paramDate.getMonth() + 1);
    const day = leftPad(paramDate.getDate());

    return [year, month, day].join(delimiter);
}


//월,일 10이상 값 처리
function leftPad(value) {
    if (value >= 10) {
        return value;
    }

    return `0${value}`;
}

//중복값 체크
//ex) values => 배열 값 / targetValue => 포함여부 확인하고 싶은 값
function hasDuplicates(values, targetValue) {
	
    let counts = {};
    for (let value of values) {
        counts[value] = (counts[value] || 0) + 1;
    }
    return counts[targetValue] > 1;
}

//form
function serializeObjectByFormId(form) {
    var obj = null;
    try {
        if (form.tagName && form.tagName.toUpperCase() == "FORM") {
            var arr = Array.from(new FormData(form).entries());
            if (arr) {
                obj = {};
                arr.forEach(function(item) {
                    obj[item[0]] = item[1];
                });
            }
        }
    } catch (e) {
        console.log(e.message);
    } finally {
        // You can add any finalization code here if needed
    }
    return obj;
}

function serializeObjectByFormData(formData) {
    "use strict";
    var obj = {};
    
    formData.forEach(function(value, key){
        obj[key] = value;
    });
    
    return obj;
}

//inputOnlyNumber
function inputOnlyNumber(_this, location = '') {
	const element = _this; 
	if(location === '') {
		element.value = element.value.replace(/[^0-9]/gi, "");
	} else if(location == location) {
		element.value = element.value.replace(/[^0-9.]+/g, "");
	}
};

function getRownum(paging,rowIdx){
	const rownum = paging.totalCount - (paging.pageSize * (paging.pageNo-1)) - rowIdx;
	return rownum;
}

function stringSplit(stringVal,delimiter = '-'){
	let result = "";
	if(!isNull(stringVal)){
		result = stringVal.split(delimiter);
	}
	return result;
}

function writePaging(paging) {

    let pagingHtml = '<ul id="pagination">';
    pagingHtml += '<li class="page-item"><a href="javascript:pageMoveForAsynchronous(' + paging.prevPageNo + ')" class="first"><i id="pageLeftArrow"></i></a></li>';

    for (var i = paging.startPageNo; i <= paging.endPageNo; i++) {
        pagingHtml += '<li class="page-item"><a href="javascript:pageMoveForAsynchronous(' + i + ')" class="' + (paging.pageNo === i ? 'active ' : '') + 'page-number">' + i + '</a></li>';
    }

    pagingHtml += '<li class="page-item"><a href="javascript:pageMoveForAsynchronous(' + paging.nextPageNo + ')" class="last"><i id="pageRightArrow"></i></a></li>';
    pagingHtml += '</ul>';
    return pagingHtml;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    let month = '' + (date.getMonth() + 1),
        day = '' + date.getDate(),
        year = date.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}