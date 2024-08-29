//중복클릭 전역변수
/**
 	node.empty();
 */
Node.prototype.empty = function(){
    this.innerHTML = "";
}

let preventDuplicate;

let historyBack = () => {
	window.history.back();
}

//Login cookie start
let setLangCookie = () => {
    let selectLang = document.getElementById('selectLang');
    const langValue = document.getElementsByName('lang');
    let checkedValue;
    for (const lang of langValue) {
    	if (lang.checked){
    		checkedValue = lang.value;
    		break;
    	}
    }
    localStorage.setItem("lang",checkedValue);
    window.location.href="/changeLocale?lang="+checkedValue;
}

function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}
	
function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}

function getCookie(cookieName) {
    cookieName = cookieName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cookieName);
    var cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}
//Login cookie end

$(function(){
   //뒤로가기 페이지 리로드
   window.onpageshow = function(event) {
      if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
          window.location.reload()
      }      
   }
   
   preventDuplicate = document.querySelector('.overLapping');

   selectMinuteSet();
   
   //autoCompleteOff
   document.querySelectorAll('input').forEach(auto => auto.setAttribute('autocomplete', 'off'))
})

// selectTime
function selectMinuteSet(){
   let hourOption = "";
   let minuteOption = "";
   let minuteByMinute = "";
   const minutes = [0, 15, 30, 45];
   
   for(i = 0; i < 24; i++){
      if(i < 10){
         hourOption += "<option value=0"+i+">0"+i+message.common.hour+"</option>";
      }else{
         hourOption += "<option value="+i+">"+i+message.common.hour+"</option>";
      }
   }
/*   for(i = 0; i <= 5; i++){
      minuteOption += "<option value="+i+"0>"+i+"0"+message.common.minute+"분</option>";
   }*/
   for(var i = 0; i < minutes.length; i++){
       const value = minutes[i] < 10 ? "0" + minutes[i] : minutes[i];
       minuteOption += "<option value='"+value+"'>"+value+message.common.minute+"</option>";
   }   
   for(i = 0; i <= 59; i++){
      if(i < 10){
         minuteByMinute += "<option value=0"+i+">0"+i+message.common.minute+"</option>";
      } else {
         minuteByMinute += "<option value="+i+">"+i+message.common.minute+"</option>";
      }
   }
   $('.select-hour').append(hourOption);
   $('.select-minute').append(minuteOption);
   $('.select-minute-unit').append(minuteByMinute);
   
   const startHour    = $('.startHour').data('start-hour');
   const startMinute    = $('.startMinute').data('start-minute');
   const endHour       = $('.endHour').data('end-hour');
   const endMinute    = $('.endMinute').data('end-minute');
   
   if(!isNull(startHour))       $('.startHour').val(startHour) 
   if(!isNull(startMinute))    $('.startMinute').val(startMinute) 
   if(!isNull(endHour))       $('.endHour').val(endHour) 
   if(!isNull(endMinute))       $('.endMinute').val(endMinute) 
   
}

let hourSelect = () => {
	let hourOptionEl = "<option value=''>선택</option>";
	const selectStartEl = document.querySelector('.searchStartDate');
	const selectEndEl = document.querySelector('.searchEndDate');
	for(i = 0; i <= 23; i++) {
		hourOptionEl += `<option value="${i}~${i+1}">${i}시~${i+1}시</option>`
	}
	selectStartEl.innerHTML = hourOptionEl;
	selectEndEl.innerHTML = hourOptionEl;
} 

let hourTimeSelect = (parent) => {
    const selectStartEl = document.querySelector(`${parent} .searchStartHour`);
    const selectEndEl = document.querySelector(`${parent} .searchEndHour`);
    
    let generateOptions = (maxHour) => {
        let optionEl = "<option value=''>"+message.common.select+"</option>";
        for(let i = 0; i <= maxHour; i++) {
            if(i < 10) {
                optionEl += `<option value="0${i}">0${i}${message.common.hour}</option>`;
            } else {
                optionEl += `<option value="${i}">${i}${message.common.hour}</option>`;
            }
        }
        return optionEl;
    };

    selectStartEl.innerHTML = generateOptions(23);
    selectEndEl.innerHTML = generateOptions(23);

    selectEndEl.addEventListener('change', function() {
        let endHour = this.value;
        let startHour = selectStartEl.value;
        if(endHour) {
			endHour = parseInt(endHour, 10);
			selectStartEl.innerHTML = generateOptions(endHour - 1);
            if (parseInt(startHour, 10) >= endHour) {
			   	new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_different_year).footer(1,message.common.button_confirm,function(button, modal){
					selectStartEl.value = '';
			      	modal.close();
			   	}).open();    
            } else {
                selectStartEl.value = startHour;
            }
        } else {
            selectStartEl.innerHTML = generateOptions(24);
        }
    });
};


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

//trClick => checked
function checkedSet(_this, type = 'radio', funCt = 'not use', regionType = '') {
   const checkedThis = _this.querySelector(`input[type="${type}"]`);
   const checkedValue = checkedThis.checked;
   if(type === 'checkbox') {
      if(checkedValue === false){
         checkedThis.checked = true;
         if(funCt == 'use') {
	         if(regionType == 'TAZ'){
				tazSelectAddTr(checkedThis, regionType);
			 } else {
				tazSelectAddTr(checkedThis);
			 }
		 } 
      } else {
         checkedThis.checked = false;
         if(funCt == 'use') {
	        tazSelectRemoveTr(checkedThis);
		 }
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

function setNumberDigit(value, digit) {
    const strValue = value.toString();
    if (strValue.length >= digit) {
        return strValue;
    } else {
        const zeroPadding = '0'.repeat(digit - strValue.length);
        return zeroPadding + strValue;
    }
}

function isNull(value) {
    return (value === undefined || value === null || value === '' || value.length === 0 ) ? true : false;
}

/*loading*/
class setLoading {
    constructor() {
        let loading_cover = null;
    }
    
    start = function () {
        const loading_html = `<div id="loadingContainer">
                                <div id="loadingWrap">
                                   <div id="loadingCircle">
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   		<div></div>
                                   	</div>
                                </div>
                              </div>`;
        this.loading_cover = document.createElement('div');
        this.loading_cover.innerHTML = loading_html;
        document.body.appendChild(this.loading_cover);
        
        return this;
    }
    
    end = function () {
        this.loading_cover.remove();
        this.loading_cover = null;
        return this;
    }
}

//다중 로딩
class setEachLoading {
    constructor() {
        let loading_cover = null;
    }
    
    start = function (element) {
        const el = document.querySelector(`${element}`)
        const loading_html = `<div class="each-loader">
                                <div class="loader"></div>
                              </div>`;
      	const background = `<div class="loader-bg"></div>`
        this.loading_cover = document.createElement('div');
        this.loading_cover.innerHTML = loading_html
        el.appendChild(this.loading_cover);
        el.style.position = 'relative';
        el.insertAdjacentHTML('beforeend', background);
        return this;
    }
    
    end = function () {
        this.loading_cover.remove();
        this.loading_cover = null;
        return this;
    }	
}


// 숫자인지 확인하는 함수
function isNumeric(value) {
    return !isNaN(value) && isFinite(value);
}

//3자리수 콤마
let numberComma = (value) => {
	const number = value;
	return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 마우스 클릭 제어
function disableContextAndDrag() {
    var isProd = document.body.getAttribute('data-prod') === 'true';
    if (isProd) {
        document.body.oncontextmenu = function() { return false; };
        document.body.ondragstart = function() { return false; };
        document.body.onselectstart = function() { return false; };
    }
}