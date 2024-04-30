let deleteAnsArray = new Array();
let deleteDrctArray = new Array();

// optionAdd
optionAdd = function(_this) {
	const eleParent = _this.closest('.list-answer');
	const optionParent = eleParent.querySelector('.all-answer');
	let optionList = eleParent.querySelectorAll('.option-list');
	const listLength = optionList.length+1;
	
	const optionEtc = eleParent.querySelector('.optionEtc');
		
	let option = document.createElement('li');
	    option.className = 'option-list';
	    option.innerHTML = `<input type="text" class="answ-content" placeholder="새 옵션" data-answ-sort="${listLength}">
                           <input type="button" onclick="optionRemove(this)" value="삭제">`;		
		
	if(optionEtc) {
		optionParent.insertBefore(option, optionEtc);
	} else {
		optionParent.appendChild(option)
	}
	
	optionList = eleParent.querySelectorAll('.option-list');
	optionList.forEach((item, index) => {
		item.querySelectorAll('input[type="text"]').forEach(x => {
			x.setAttribute('data-answ-sort', index+1);
		})
	})
	
}
// optionEtc
optionEtcAdd = function(_this) {
	const eleParent = _this.closest('.list-answer');
	const listLength = eleParent.querySelectorAll('.option-list').length+1;
	const optionEtc = eleParent.querySelectorAll('.optionEtc');
	
	let option = '<li class="option-list optionEtc">';
		option +=	'<input type="text" class="answ-content" data-answ-sort="'+listLength+'" value="기타" readonly>'
		option +=	'<input type="button" onclick="optionRemove(this)" value="삭제"/>'
		option +='</li>';
		
	if(optionEtc.length < 1) eleParent.querySelector('.all-answer').insertAdjacentHTML('beforeend', option);
}

// optionRemove
function optionRemove(_this,answId = '') {
	const eleParent = _this.closest('.list-answer');
	const parentElement = _this.parentElement;
	parentElement.remove();
	
	const optionList = eleParent.querySelectorAll('.option-list');
	optionList.forEach((item, index) => {
		item.querySelectorAll('input[type="text"]').forEach(test => {
			test.setAttribute('data-answ-sort', index+1);
		})
	})	
	
	if(!isNull(answId)){
		deleteAnsArray.push(answId);
	}
}

//조사 담당자
function surveyOfficer(){
	const mngrInfo = document.querySelectorAll('.mngrInfo');
	mngrInfo.forEach(info => {
		info.value = '';
	})
	new ModalBuilder().init('조사 담당자 등록').ajaxBody("/datamng/invst/mngr").footer(1,'등록',function(button, modal){
		let mngrId, mngrNmValue;
		const mngrChecked 	= document.querySelector('.mngrCheckBox:checked');
		const modalUsermngId 	= mngrChecked.value;
		const modalUserId 		= mngrChecked.closest('tr').querySelector('.userId').textContent;

		const exmnpicIdElement = document.getElementById('exmnpicId');
		exmnpicIdElement.value = modalUserId;
		const usermngIdElement = document.getElementById('usermngId');
		usermngIdElement.value = modalUsermngId;
		
		modal.close();
	}).open();
}

//교통조사등록
let inputValidation = null;
function saveInvstMngSurvey(){
	preventDuplicate.disabled = true;
	let tmExmnMngSaveDTO = new Object();
	let invstSaveForm = new FormData($("#invstSaveForm")[0]);
	
	const invstKndCd = document.getElementById('exmnType');
	const type = invstKndCd.options[invstKndCd.selectedIndex].dataset.invstType;
	
	const colorType = document.querySelector('input[name="colorType"]:checked').value;

	//시작 기간 / 종료 기간
	const invstStrDt = document.getElementById('invstStrDt').dataset.startDate;
	const invstEndDt = document.getElementById('invstEndDt').dataset.endDate;
	//시작 시간 / 종료 시간		
	const startHour = document.querySelector('.startHour').value;
	const startMinute = document.querySelector('.startMinute').value;
	const invstStrTime = getInvstTime(startHour,startMinute);
	const endHour = document.querySelector('.endHour').value;
	const endMinute = document.querySelector('.endMinute').value;
	const invstEndTime = getInvstTime(endHour,endMinute);;
	
	//날짜 + 시간	
	const startDt = new Date(invstStrDt);
	const addStartDt = dateFormatForYYYYMMDD(startDt) +"T"+ invstStrTime;
	const endDt = new Date(invstEndDt);
	const addEndDt = dateFormatForYYYYMMDD(endDt) +"T"+ invstEndTime;
	
	invstSaveForm.append('startDt',addStartDt);
	invstSaveForm.append('endDt',addEndDt);
	invstSaveForm.append('colrCd',colorType);
	
	
	var tmExmnMng = serializeObjectByFormData(invstSaveForm);
	tmExmnMngSaveDTO.tmExmnMng = tmExmnMng;
	
	//조사 방향(TC(MCC,TM)조사인경우)
	if(type === 'traffic'){
		let tmExmnDrctList = new Array();
		const drctWrap = document.getElementsByClassName('drct-wrap');
		for(let drctElelment of drctWrap){
			let tmExmnDrct 	= new Object();
			let drctSqno 	= drctElelment.dataset.drctSqno;
			let startlcNm 	= drctElelment.querySelector('.startlcNm');
			let endlcNm 	= drctElelment.querySelector('.endlcNm');

			tmExmnDrct.drctSqno 	= drctSqno;
			tmExmnDrct.startlcNm 	= startlcNm.value;
			tmExmnDrct.endlcNm 		= endlcNm.value;
			tmExmnDrctList.push(tmExmnDrct);
		}
		tmExmnMngSaveDTO.tmExmnDrctList = tmExmnDrctList;
		
	}

	//조사 반경
	const exmnRangeElemenet = document.getElementById('exmnRange');
	const exmnRangeValue = Number(exmnRangeElemenet.value);
	exmnRange.value = exmnRangeValue;
	
	
	//validation
	const directionBox = document.getElementById('surveyDirectionBox');
	if(!directionBox.classList.contains('none')){
		const validationItem = document.querySelectorAll('.drct-wrap .input-table-text');
		validationItem.forEach(item => item.classList.add('surveyValue'));
	}
	
	const inputValidationItem  = document.querySelectorAll('.surveyValue');
	let inputValidation = [];
	inputValidationItem.forEach(input => inputValidation.push(input.value));
	const inputChecked = inputValidation.some(item => item === '');
	
	if(!inputChecked){
		//default traffic uurl
		let returnUrl = "/datamng/invst";
		
		fetch(__contextPath__+"/datamng/invst",{
			method: "POST",
			headers: {
				'Content-Type': 'application/json;charset=utf-8'
			},
			body: JSON.stringify(tmExmnMngSaveDTO)
		})
		.then(response => response.json())
		.then(result => 
			{
				if(result.code == '200'){
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
						if(type === 'survey'){
							const exmnmngId = result.data.exmnmngId;
							returnUrl = "/datamng/invst/survey/question/"+exmnmngId+"/save";
						}
						window.location.href = __contextPath__+returnUrl;
					}).open();
				}else{
					preventDuplicate.disabled = false;
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
				}
		})
	} else {
		preventDuplicate.disabled = false;
		new ModalBuilder().init().alertBody('조사 문항을 모두 입력해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();
	}
}

function updateInvstMngSurvey(exmnmngId){
	preventDuplicate.disabled = true;
	let tmExmnMngUpdateDTO = new Object();
	let invstSaveForm = new FormData($("#invstSaveForm")[0]);
	
	const invstKndCd = document.getElementById('exmnType');
	const type = invstKndCd.options[invstKndCd.selectedIndex].dataset.invstType;
	
	const colorType = document.querySelector('input[name="colorType"]:checked').value;

	//시작 기간 / 종료 기간
	const invstStrDt = document.getElementById('invstStrDt').dataset.startDate;
	const invstEndDt = document.getElementById('invstEndDt').dataset.endDate;
	//시작 시간 / 종료 시간		
	const startHour = document.querySelector('.startHour').value;
	const startMinute = document.querySelector('.startMinute').value;
	const invstStrTime = getInvstTime(startHour,startMinute);
	const endHour = document.querySelector('.endHour').value;
	const endMinute = document.querySelector('.endMinute').value;
	const invstEndTime = getInvstTime(endHour,endMinute);;
	
	//날짜 + 시간	
	const startDt = new Date(invstStrDt);
	const addStartDt = dateFormatForYYYYMMDD(startDt) +"T"+ invstStrTime;
	const endDt = new Date(invstEndDt);
	const addEndDt = dateFormatForYYYYMMDD(endDt) +"T"+ invstEndTime;
	
	invstSaveForm.append('startDt',addStartDt);
	invstSaveForm.append('endDt',addEndDt);
	invstSaveForm.append('exmnmngId',exmnmngId);
	invstSaveForm.append('colrCd',colorType);
	
	
	var tmExmnMng = serializeObjectByFormData(invstSaveForm);
	tmExmnMngUpdateDTO.tmExmnMng = tmExmnMng;
	
	//조사 방향(TC(MCC,TM)조사인경우)
	if(type === 'traffic'){
		let tmExmnDrctList = new Array();
		const drctWrap = document.getElementsByClassName('drct-wrap');
		for(let drctElelment of drctWrap){
			let tmExmnDrct 	= new Object();
			let drctSqno 	= drctElelment.dataset.drctSqno;
			let exmndrctId 	= drctElelment.dataset.exmndrctId;
			let startlcNm 	= drctElelment.querySelector('.startlcNm');
			let endlcNm 	= drctElelment.querySelector('.endlcNm');

			tmExmnDrct.exmndrctId 	= exmndrctId;
			tmExmnDrct.drctSqno 	= drctSqno;
			tmExmnDrct.exmnmngId 	= exmnmngId;
			tmExmnDrct.startlcNm 	= startlcNm.value;
			tmExmnDrct.endlcNm 		= endlcNm.value;
			tmExmnDrctList.push(tmExmnDrct);
		}
		tmExmnMngUpdateDTO.tmExmnDrctList = tmExmnDrctList;
		
		//수정 시 삭제된 방향 정보 DB 삭제
		tmExmnMngUpdateDTO.deleteDrctArray = deleteDrctArray; 
		
		
	}

	//조사 반경
	const exmnRangeElemenet = document.getElementById('exmnRange');
	const exmnRangeValue = Number(exmnRangeElemenet.value);
	exmnRange.value = exmnRangeValue;
	
	//validation
	const directionBox = document.getElementById('surveyDirectionBox');
	if(!directionBox.classList.contains('none')){
		const validationItem = document.querySelectorAll('.drct-wrap .input-table-text');
		validationItem.forEach(item => item.classList.add('surveyValue'));
	}	
	
	const inputValidationItem  = document.querySelectorAll('.surveyValue');
	let inputValidation = [];
	inputValidationItem.forEach(input => inputValidation.push(input.value));
	const inputChecked = inputValidation.some(item => item === '');
	
	if(!inputChecked) {
		fetch(__contextPath__+"/datamng/invst/"+exmnmngId,{
			method: "PUT",
			headers: {
				'Content-Type': 'application/json;charset=utf-8'
			},
			body: JSON.stringify(tmExmnMngUpdateDTO)
			//body: invstSaveForm
		})
		.then(response => response.json())
		.then(result => 
			{
				if(result.code == '200'){
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
						const exmnmngId = result.data.exmnmngId;
						let returnUrl = "/datamng/invst/"+exmnmngId;
						/*if(type === 'survey'){
							returnUrl = "/datamng/invst/survey/question/"+exmnmngId+"/update";
						}*/
						window.location.href = __contextPath__+returnUrl;
					}).open();
				}else{
					preventDuplicate.disabled = false;
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
				}
			})
	} else {
		preventDuplicate.disabled = false;
		new ModalBuilder().init().alertBody('조사 문항을 모두 입력해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();
	}
}

//설문지 정보 저장
function saveInvstMngSurveySctn(){
	//중복클릭방지
	preventDuplicate.disabled = true;
	
	let tmExmnMngSaveDTO = new Object();	//설문 부문 테이블 
	let tmSrvySectList = new Array();	//설문 부문 테이블 
	let exmnmngId = document.getElementById("exmnmngId").value;
	let questContainerList = document.getElementsByClassName("quest-container");
	
	for(const questContainerElement of questContainerList){
		let tmSrvySectInfo = new Object();
		//부제
		let sectTitle 	= questContainerElement.getElementsByClassName("quest-title");
		let sectSubtitle 	= questContainerElement.getElementsByClassName("quest-sub-title");
		//설문 타입/순서
		let sectTypeCd 	= questContainerElement.dataset.sctnType;
		let sectSqno 	= questContainerElement.dataset.sctnOrdr;
		
		tmSrvySectInfo.exmnmngId = exmnmngId;
		tmSrvySectInfo.sectTitle = sectTitle[0].innerText;
		tmSrvySectInfo.sectSubtitle = sectSubtitle[0].innerText;
		tmSrvySectInfo.sectTypeCd = sectTypeCd;
		tmSrvySectInfo.sectSqno = Number(sectSqno);
		
		
		let questBoxList = questContainerElement.getElementsByClassName("quest-box");
		let tmSrvyQstnList = new Array(); 	//질문 테이블 
		for(const questBoxElement of questBoxList){
			let tmSrvyQstnInfo 		= new Object();
			let tmSrvyAnsList 		= new Array();
			let qstnTitle 			= questBoxElement.getElementsByClassName("quest-name");
			let qstnType 			= qstnTitle[0].dataset.qstnType;
			let qstnSqno 			= Number(qstnTitle[0].dataset.qstnOrdr);
			
			if(qstnType === 'CHECKBOX' || qstnType === 'RADIO'){
				let optionList = questBoxElement.getElementsByClassName("answ-content");
				for(const optionElement of optionList){
					let tmSrvyAnsInfo 	= new Object();
					let ansCnts 	= optionElement.value;
					if(qstnType === 'CHECKBOX') ansCnts = optionElement.innerText;
					
					if(!isNull(ansCnts)){
						let ansSqno		= optionElement.dataset.answSort;
						
						tmSrvyAnsInfo.ansCnts = ansCnts;
						tmSrvyAnsInfo.ansSqno = ansSqno;
						tmSrvyAnsList.push(tmSrvyAnsInfo);
					}	
				}
			}
			
			tmSrvyQstnInfo.qstnTitle 	= qstnTitle[0].innerText;
			tmSrvyQstnInfo.qstnTypeCd = qstnType;
			tmSrvyQstnInfo.qstnSqno 	= qstnSqno;
			tmSrvyQstnInfo.tmSrvyAnsList = tmSrvyAnsList;				
			tmSrvyQstnList.push(tmSrvyQstnInfo);
		}
		tmSrvySectInfo.tmSrvyQstnList = tmSrvyQstnList;
		tmSrvySectList.push(tmSrvySectInfo);
	}
	tmExmnMngSaveDTO.tmSrvySectList = tmSrvySectList;
	tmExmnMngSaveDTO.exmnmngId		= exmnmngId;
	console.log(tmExmnMngSaveDTO)
//	fetch(__contextPath__+"/datamng/invst/survey/question",{
//		method: "POST",
//		headers: {
//			'Content-Type': 'application/json;charset=utf-8'
//		},
//		body: JSON.stringify(tmExmnMngSaveDTO)
//	})
//	.then(response => response.json())
//	.then(result => {
//		if(result.code == '200'){
//			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
//				modal.close();
//				window.location.href = __contextPath__+"/datamng/invst";
//			}).open();
//		}else{
//			//중복클릭해제
//			preventDuplicate.disabled = false;
//			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
//		}
//	})
}

//설문지 정보 수정
function updateInvstMngSurveySctn(){
	//중복클릭방지
	preventDuplicate.disabled = true;
	let tmExmnMngUpdateDTO = new Object();	//설문 부문 테이블 
	let tmSrvySectList = new Array();	//설문 부문 테이블 
	let exmnmngId = document.getElementById("exmnmngId").value;
	let questContainerList = document.getElementsByClassName("quest-container");
	
	for(const questContainerElement of questContainerList){
		let tmSrvySectInfo = new Object();
		//부제
		let sectTitle 	= questContainerElement.getElementsByClassName("quest-title");
		let sectSubtitle 	= questContainerElement.getElementsByClassName("quest-sub-title");
		//console.log(sectSubTitle[0].innerText);
		//설문 타입/순서
		let sectTypeCd 	= questContainerElement.dataset.sctnType;
		let sectId 		= questContainerElement.dataset.sctnId;
		let sectSqno 	= questContainerElement.dataset.sctnOrdr;
		
		tmSrvySectInfo.exmnmngId = exmnmngId;
		tmSrvySectInfo.sectId = sectId;
		tmSrvySectInfo.sectTitle = sectTitle[0].innerText;
		tmSrvySectInfo.sectSubtitle = sectSubtitle[0].innerText;
		tmSrvySectInfo.sectTypeCd = sectTypeCd;
		tmSrvySectInfo.sectSqno = Number(sectSqno);
		
		
		let questBoxList = questContainerElement.getElementsByClassName("quest-box");
		let tmSrvyQstnList = new Array(); 	//질문 테이블 
		for(const questBoxElement of questBoxList){
			let tmSrvyQstnInfo 		= new Object();
			let tmSrvyAnsList 		= new Array();
			let qstnTitle 			= questBoxElement.getElementsByClassName("quest-name");
			let qstnType 			= qstnTitle[0].dataset.qstnType;
			let qstnId	 			= qstnTitle[0].dataset.qstnId;
			let qstnSqno 			= Number(qstnTitle[0].dataset.qstnOrdr);
			
			if(qstnType === 'CHECKBOX' || qstnType === 'RADIO'){
				let optionList = questBoxElement.getElementsByClassName("answ-content");
				for(const optionElement of optionList){
					let tmSrvyAnsInfo 	= new Object();
					let ansCnts 	= optionElement.value;
					if(qstnType === 'CHECKBOX') ansCnts = optionElement.innerText;
					if(!isNull(ansCnts)){
						let ansSqno		= optionElement.dataset.answSort;
						let ansId		= optionElement.dataset.answId;
						
						tmSrvyAnsInfo.ansId 	= ansId;
						tmSrvyAnsInfo.qstnId 	= qstnId;
						tmSrvyAnsInfo.ansCnts 	= ansCnts;
						tmSrvyAnsInfo.ansSqno 	= ansSqno;
						tmSrvyAnsList.push(tmSrvyAnsInfo);
					}
				}
			}
			
			tmSrvyQstnInfo.qstnTitle 	= qstnTitle[0].innerText;
			tmSrvyQstnInfo.sectId		= sectId;
			tmSrvyQstnInfo.qstnId		= qstnId;
			tmSrvyQstnInfo.qstnTypeCd 	= qstnType;
			tmSrvyQstnInfo.qstnSqno 	= qstnSqno;
			tmSrvyQstnInfo.tmSrvyAnsList= tmSrvyAnsList;				
			tmSrvyQstnList.push(tmSrvyQstnInfo);
		}
		tmSrvySectInfo.tmSrvyQstnList = tmSrvyQstnList;
		tmSrvySectList.push(tmSrvySectInfo);
	}
	tmExmnMngUpdateDTO.tmSrvySectList = tmSrvySectList;
	tmExmnMngUpdateDTO.deleteAnsArray = deleteAnsArray;
	
	fetch(__contextPath__+"/datamng/invst/survey/question/"+exmnmngId,{
		method: "PUT",
		headers: {
			'Content-Type': 'application/json;charset=utf-8'
		},
		body: JSON.stringify(tmExmnMngUpdateDTO)
	})
	.then(response => response.json())
	.then(result => {
		deleteAnsArray = new Array();
		if(result.code == '200'){
			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
				modal.close();
				window.location.href = __contextPath__+"/datamng/invst/"+exmnmngId;
			}).open();
		}else{
			preventDuplicate.disabled = false;
			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
		}
	})
}

//조사 정보 삭제
function deleteInvstInfo(exmnmngId){
	preventDuplicate.disabled = true;
	new ModalBuilder().init().alertBody('조사정보를 삭제 하시겠습니까?').footer(3,'확인',function(button, modal){
		fetch(__contextPath__ + "/datamng/invst/" + exmnmngId,{
			method: "DELETE",
		})
		.then(response => response.json())
		.then(result => {
				if(result.code == '200'){
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
						window.location.href= __contextPath__ + "/datamng/invst";
					}).open();
				}else{
					preventDuplicate.disabled = false;
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
					}).open();
				}
			})
		modal.close();
	}, '취소', function(button, modal){
		modal.close();
	}).open();

}

//조사위치등록 04.05 위치 선택 바뀌어서 주석
/*function searchLocation(){
	alert('조사 위치 등록은 삭제되었습니다. 해당 부분은 수정해주세요.');
}*/

//조사위치반경
function layerCircle(){
	const coordInateValue = document.getElementById('coordinate').value;
	const locationCircleValue = document.getElementById('exmnRange').value;
	if(coordInateValue !== ''){		
		if(locationCircleValue !== '') {
			mapRemove();
		 	mapboxGl(locationLng, locationLat, locationCircleValue);
		} else {
			new ModalBuilder().init().alertBody('좌표 반경을 입력해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();
		}
	} else {
		new ModalBuilder().init().alertBody('조사 위치를 등록해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();				
	}
}

//조사 시간 세팅
function getInvstTime(invstTime,invstMinute){
	let resultTime = "00:00:00";
	if(!isNull(invstTime) && !isNull(invstMinute)){
		resultTime = invstTime+":"+invstMinute+":00";
	}
	return resultTime;
}

//조사 타입별 노출 옵션
function typeOption(){
	const surveyType = document.getElementById('exmnType');
	const typeValue = surveyType.value;
	const objective = document.querySelectorAll('.survey-objectives');
	const directionBox = document.getElementById('surveyDirectionBox');
	
	if(typeValue == 'OD' || typeValue == 'AXLELOAD' || typeValue == 'LABORSIDE') {
		objective.forEach(item => item.classList.remove('none'));
		directionBox.classList.add('none');
	} else {
		objective.forEach(item => item.classList.add('none'));
		directionBox.classList.remove('none');
	}
}

function updateExmnRange(exmnmngId){
	preventDuplicate.disabled = true;
	const exmnRange = document.getElementById('exmnRange').value;
	
	fetch(__contextPath__+"/datamng/invst/"+exmnmngId+"/exmnRange",{
			method: "PUT",
			body : JSON.stringify(parseInt(exmnRange))
		})
		.then(response => response.json())
		.then(result => {
					if(result.code == '200'){
						new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
						window.location.reload();
					}).open();
					}else{
						preventDuplicate.disabled = false;
						new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
					}
				}
		)	
}

// questSectionNewAdd
/*function questSecition(_this,invstNm,qstnList) {
	let container = $('#infoTableWrap');
	let dataContainer = container.children('.quest-container').length+1;
	let questTitleNumber = `Quset ${dataContainer}`;
	let questBox = questListBox(1,qstnList);
	let qstnJsonList = JSON.stringify(qstnList);
	qstnJsonList = qstnJsonList.replace(/"/g, '&quot;');
	let questContainer = '<div class="quest-container" data-container="'+dataContainer+'">'
		questContainer +=	'<div class="quest-label">'
		questContainer +=		'<h3 class="quest-title-number">'+questTitleNumber+'</h3>'
		questContainer +=		'<input type="button" class="is-key-button" onclick="sectionRemove(this)" value="삭제"/>'
		questContainer +=	'</div>'
		questContainer += 	'<div class="quest-wrap">'
		questContainer += 		'<div class="quest-box">'
		questContainer += 			'<div>'
		questContainer += 				'<input type="text" class="quest-title" value="'+invstNm+'" readonly/>'
		questContainer += 			'</div>'
		questContainer += 			'<div>'
		questContainer += 				'<input type="text" class="quest-sub-title" name="srvySubTitle" placeholder="(선택사항) 조사 부제를 입력하세요."/>'
		questContainer += 			'</div>'
		questContainer += 		'</div>'
		questContainer += 		'<div class="sortable">'
		questContainer +=			questBox
	 	questContainer += 		'</div>'
		questContainer += 	'</div>'
		questContainer += 	'<div class="quest-box-add">'
		questContainer += 		'<input type="button" class="is-key-button" onclick="questBoxAdd(this,'+qstnJsonList+')" value="설문항목 추가하기"/>'
		questContainer += 	'</div>'
		questContainer += '</div>'
		
	container.append(questContainer);
}*/

// questList
function questBoxAdd() {
	const html =`
				<div class="quest-box">
					<div class="sortable-handle none"><span></span></div>
					<div class="quest-title-box">
						<span class="quest-number">Q1.</span>
						<span class="quest-name" data-qstn-ordr="">
							<input type="text" placeholder="질문 제목을 입력해주세요."/>
						</span>
						<select class="select-list-box" onchange="optionSelected(this);">
							<option value="shortAnswer">주관식</option>
							<option value="select">드롭다운</option>
							<option value="radio">라디오</option>
							<option value="checkbox">체크박스</option>
							<option value="date">날짜</option>
							<option value="time">시간</option>
							<option value="location">위치</option>
						</select>
						<input type="button" class="quest-close"/>
					</div>
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img th:src="@{/images/quest_text.png}" alt="short"></span>
							<span class="quest-view-text">주관식 답변란</span>
						</div>
					</div>
				</div>	
				`
}

// selected layout show/hide
function optionSelected(_this) {
	const selectedItem = _this.value;
	const multipleValue = ['radio', 'checkbox', 'select'];
	const appendParent = _this.closest('.quest-box');
	const questitem = _this.closest('.quest-box').querySelector('.list-answer');
	
	//select, radio, checkbox
	let multipleItem = '<div class="list-answer">'
		multipleItem +=	'<ul class="all-answer">'					
		multipleItem +=		'<li class="option-list">'
		multipleItem +=			'<input type="text" name="qstnAnswContent" class="answ-content" placeholder="새 옵션" data-answ-sort="1">'
		multipleItem +=			'<input type="button" onclick="optionRemove(this)" value="삭제"/>'
		multipleItem +=		'</li>'
		multipleItem +=	'</ul>'
		multipleItem +=	'<div class="list-append">'
		multipleItem +=		'<input type="button" class="mr8" onclick="optionAdd(this)" value="+ 옵션 추가"/>'
		multipleItem += 		'<input type="button" onclick="optionEtcAdd(this)" value="+ 기타 옵션 추가"/>'
		multipleItem +=	'</div>'
		multipleItem +='</div>'
		
	//short answer
	const shortAnswerItem = `
							<div class="list-answer">
								<div class="quest-view-item">
									<span class="quest-view-img"><img src="/images/quest_text.png" alt="short"></span>
									<span class="quest-view-text">short answer</span>
								</div>
							</div>
							`;		
	
	//date
	const dateItem =`
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_date.png" alt="short"></span>
							<span class="quest-view-text">YYYY-MM-DD</span>
						</div>
					</div>
					`
	//time
	const timeItem =`
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_time.png" alt="short"></span>
							<span class="quest-view-text">Time</span>
						</div>
					</div>					
					`
	//location
	const locationItem =`
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_location.png" alt="short"></span>
							<span class="quest-view-text">Location</span>
						</div>
					</div>					
					`
	
	if(questitem) questitem.remove();
	if(multipleValue.includes(selectedItem)){
		appendParent.insertAdjacentHTML('beforeend', multipleItem);
	} else if(selectedItem === 'time') {
		appendParent.insertAdjacentHTML('beforeend', timeItem);
	} else if(selectedItem === 'date') {
		appendParent.insertAdjacentHTML('beforeend', dateItem);
	} else if(selectedItem === 'location') {
		appendParent.insertAdjacentHTML('beforeend', locationItem);
	} else if(selectedItem === 'shortAnswer'){
		appendParent.insertAdjacentHTML('beforeend', shortAnswerItem);
	}
	
}
// questboxSortable
window.addEventListener('load', () => {
	document.querySelectorAll('.quest-box').forEach(mouse => {
		mouse.addEventListener('mouseover', (e) => {
			const _thisClosest =  e.target.closest('.quest-box');
			_thisClosest.querySelector('.sortable-handle').classList.remove('none');
			
		    $(".sortable").sortable({
				handle: '.sortable-handle',
				stop: function(e, ui){
					/*const sortableBox = $(".sortable-box");
					const sortableNumber = $('.quest-number');
					sortableBox.each(function(idx,item){
						$(item).attr('data-index', idx+1);
					});
					sortableNumber.each(function(idx,item){
						$(item).text('Q'+(idx+1)+'.');
					})*/		
				}
			});
			$(".sortable").disableSelection();			
		})
		
		mouse.addEventListener('mouseleave', (e) => {
			const _thisClosest = e.target.closest('.quest-box');
			_thisClosest.querySelector('.sortable-handle').classList.add('none');
		})
	})
})
