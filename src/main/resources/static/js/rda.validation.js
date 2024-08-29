function validation(el, dom = null){
	let container = null;
   	let elementText = message.validation.validation_js_form;
   	let elementId;
	let success = true;
	if(dom == null){
	   	container = document.querySelector(`${el}`);
	} else {
		container = el;
	}
   
   	const elements = container.querySelectorAll('.validation-tag');
   	for(const element of elements){
    	let elementValue = element.value;
        let dataValidation = element.dataset.validation;
        if(isNull(elementValue)){
        	if(!isNull(element.placeholder)){
	        	elementText = element.placeholder;
        	} else {
        		elementText = message.authRqst.authSetting_emptyValidation;
        	}
        	
        	elementId = element.getAttribute('id');
        	if(dataValidation !== 'select' && dataValidation !== 'selectDate' && dataValidation !== 'locationSelect' && dataValidation !== 'oneDateSelect'){
	        	new MsgModalBuilder().init().alertBody(elementText).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) {
					    validationFocus(elementId);
					} else {
					    element.focus();
					}
					modal.close();
					
				}).open();
	            success = false;
	            break;
			}
		}
        if(dataValidation === 'email') {
            if(!checkEmail(elementValue)) {
        		elementId = element.getAttribute('id');
				new MsgModalBuilder().init().alertBody(message.validation.validation_js_email).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
                success = false;
                break;
            }
        }
        if(dataValidation === 'tel') {
            if(!checkTel(elementValue)) {
				elementId = element.getAttribute('id');
				new MsgModalBuilder().init().alertBody(message.validation.validation_js_contact).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
                success = false;
                break;
            }
        }
        if(dataValidation === 'name') {
			if(!checkName(elementValue)) {
				elementId = element.getAttribute('id');
				new MsgModalBuilder().init().alertBody(message.validation.validation_js_name).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
				success = false;
				break;
			}
		}
        if(dataValidation === 'id') {
			if(!checkId(elementValue)) {
				elementId = element.getAttribute('id');
				new MsgModalBuilder().init().alertBody(message.validation.validation_js_id).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
				success = false;
				break;
			}
		}
		if(dataValidation === 'pw') {
			if(!checkPw(elementValue)) {
				elementId = element.getAttribute('id');
				new MsgModalBuilder().init().alertBody(message.mypage.mypage_password_invalid).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
				success = false;
				break;				
			}
		}
		if(dataValidation === 'pwChecked') {
			const userPswd = document.getElementById('userPswd').value;
			const userPswdChk = document.getElementById('userPswdChk').value;
			if(userPswd != userPswdChk) {
				new MsgModalBuilder().init().alertBody(message.login.loginPwChange_mismatch_password).footer(4,message.common.button_confirm,function(button, modal){
					if(elementId) validationFocus(elementId);
					modal.close();
				}).open();
				success = false;
				break;				
			}
		}
		//년도 검색시 endDate가 startDate 보다 작을때
		if(dataValidation === 'selectDate') {
			const startDate = document.getElementById('startDt').value;
			const endDt = document.getElementById('endDt').value;
			if(isNull(elementValue) || startDate > endDt){
				new MsgModalBuilder().init().alertBody(message.validation.validation_js_startDate_or_endDate).footer(4,message.common.button_confirm,function(button, modal){
				    element.focus();
					modal.close();
				}).open();				
				success = false;
				break;
			}
		}
		if(dataValidation === 'select') {
			if(isNull(elementValue)){
				new MsgModalBuilder().init().alertBody(message.authRqst.authSetting_emptyValidation).footer(4,message.common.button_confirm,function(button, modal){
					element.focus();
					modal.close();
				}).open();				
				success = false;
				break;
			}
		}
		//조사 시작시간이 종료시간보다 늦은 시간일때
		if(dataValidation === 'surveyTime'){
			const startHour = document.querySelector('.startHour').value;
			const startMinute = document.querySelector('.startMinute').value;
			const invstStrTime = getInvstTime(startHour,startMinute);
			const endHourElement = document.querySelector('#endHour');
			const endHour = endHourElement.value;
			const endMinute = document.querySelector('.endMinute').value;
			const invstEndTime = getInvstTime(endHour,endMinute);;
			  
			const surveyTimeWrap = document.querySelector('.select-date-box.survey-TM');
			if(!surveyTimeWrap.classList.contains('none')){
			   if(invstStrTime >= invstEndTime){
			      new MsgModalBuilder().init().alertBody(message.survey.survey_js_survey_time_check).footer(4,message.common.button_confirm,function(button, modal){
			         preventDuplicate.disabled = false;
			         endHourElement.focus();
			         modal.close();
			      }).open();
			      success = false;
				  break;
			   }
			}			
		}
		//주소검색시 DSD 까지 필수
		if(dataValidation === 'locationSelect') {
			const updateLocationBox = document.getElementById('updateExmnLc');
			if(updateLocationBox) {
				if(!updateLocationBox.classList.contains('none')){
					if(isNull(elementValue)){
						new MsgModalBuilder().init().alertBody(message.validation.validation_js_time).footer(4,message.common.button_confirm,function(button, modal){
							element.focus();
							modal.close();
						}).open();				
						success = false;
						break;
					}
				}
			} else {
				if(isNull(elementValue)){
					new MsgModalBuilder().init().alertBody(message.validation.validation_js_time).footer(4,message.common.button_confirm,function(button, modal){
						element.focus();
						modal.close();
					}).open();				
					success = false;
					break;
				}				
			}
		}
		//조사년도 한개만
		if(dataValidation === 'oneDateSelect'){
			if(isNull(elementValue)){
				new MsgModalBuilder().init().alertBody(message.survey.survey_js_survey_search_year_validation).footer(4,message.common.button_confirm,function(button, modal){
					element.focus();
					modal.close();
				}).open();
				success = false;
				break;
			}
		}
    }
    return success;
}

function statisticValidation(el, dom = null){
	let container = null;
   	let elementText = message.validation.validation_js_form;
   	let elementId;
	let success = true;
	if(dom == null){
	   	container = document.querySelector(`${el}`);
	} else {
		container = el;
	}
   
   	const elements = container.querySelectorAll('.validation-tag');
   	for(const element of elements){
    	let elementValue = element.value;
        let dataValidation = element.dataset.validation;
        
		if(dataValidation === 'select') {
			if(isNull(elementValue)){
				success = false;
				break;
			}
		}
		//주소검색시 DSD 까지 필수
		if(dataValidation === 'locationSelect') {
			const updateLocationBox = document.getElementById('updateExmnLc');
			if(updateLocationBox) {
				if(!updateLocationBox.classList.contains('none')){
					if(isNull(elementValue)){
						success = false;
						break;
					}
				}
			} else {
				if(isNull(elementValue)){
					success = false;
					break;
				}				
			}
		}
    }
    return success;
}

let checkEmail = (email) => {
    const regExp = /^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regExp.test(email);
}
let checkTel = (tel) => {
    tel = tel.replace(/-/gi, "");
    const regExp = /^0[0-9]{9}$/;
    return regExp.test(tel);
}
let checkName = (name) => {
	const regExp = /^[a-zA-Z.\s]+$/;
	return regExp.test(name);
}
let checkPw = (pw) => {
	/*const regExp = /^(?=.*[a-z])(?=.*\W).{8,32}$/;*/
	const regExp = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,20}$/;
	return regExp.test(pw)
}
let checkId = (id) => {
	const regExp = /^[A-Za-z0-9]+$/;
	return regExp.test(id)
}

function validationFocus(element){
	const elementId = document.getElementById(element);
	const questValid = document.querySelector('.quest-validation');
	if(!questValid) elementId.classList.add('tag-invalid');
	elementId.focus();
}

function valid(_this){
	_this.classList.remove('tag-invalid');
}