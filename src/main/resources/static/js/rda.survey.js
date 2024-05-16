let deleteSectArray = new Array();
let deleteAnsArray = new Array();
let deleteQstnArray = new Array();
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
	    option.innerHTML = `<input type="text" class="answ-content surveyValue" placeholder="새 옵션" data-answ-sort="${listLength}">
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
	new ModalBuilder().init('조사 담당자 등록').ajaxBody("/datamng/invst/modal/mngr").footer(1,'등록',function(button, modal){
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

//설문 양식 조회
function surveyForm(){
	const srvyInfo = document.querySelectorAll('.srvyInfo');
	const exmn = document.querySelector('#exmnType');
	const selectedOption = exmn.options[exmn.selectedIndex];
	const exmnType = selectedOption.dataset.invstTypecd;
	
	srvyInfo.forEach(info => {
		info.value = '';
	})
	new ModalBuilder().init('설문 양식 등록').ajaxBody("/datamng/invst/modal/survey?searchType="+exmnType).footer(1,'등록',function(button, modal){
		let mngrId, mngrNmValue;
		const surveyChecked 	= document.querySelector('.surveyCheckBox:checked');
		const modalSrvyId 		= surveyChecked.value;
		const modalSrvyTitle	= surveyChecked.closest('tr').querySelector('.srvyTitle').textContent;

		const srvyIdElement = document.getElementById('srvyId');
		srvyIdElement.value = modalSrvyId;
		const srvyTitleElement = document.getElementById('srvyTitle');
		srvyTitleElement.value = modalSrvyTitle;
	    
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
	const type = invstKndCd.options[invstKndCd.selectedIndex].dataset.hasDrct;
	
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
	if(type === 'true'){
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
						window.location.href = __contextPath__+returnUrl;
					}).open();
				}else{
					preventDuplicate.disabled = false;
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
					}).open();
				}
		})
	} else {
		preventDuplicate.disabled = false;
		console.log('test');
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
	let srvyTitle = document.getElementById("srvyTitle").value;
	let srvyType = document.getElementById("srvyType").value;
	let startDt = document.getElementById("startDt").value;
	let endDt = document.getElementById("endDt").value;
	let questContainerList = document.getElementsByClassName("quest-container");

	//validation
	const inputValidationItem  = document.querySelectorAll('.surveyValue');
	let inputValidation = [];
	inputValidationItem.forEach(input => inputValidation.push(input.value));
	const inputChecked = inputValidation.some(item => item === '');
	
	if(inputChecked) {
		new ModalBuilder().init().alertBody('비어있는 설문 목록이 있습니다. 모두 입력해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();
	}
	
	for(const questContainerElement of questContainerList){
		let tmSrvySectInfo = new Object();
		//부제
		let sectTitle 	= questContainerElement.querySelector(".quest-title > input");
		let sectSubtitle 	= questContainerElement.querySelector(".quest-sub-title > input");
		//설문 타입/순서
		let sectTypeCd 	= questContainerElement.dataset.sctnType;
		let sectSqno 	= questContainerElement.dataset.sctnOrdr;
		
		tmSrvySectInfo.sectTitle = sectTitle.value;
		tmSrvySectInfo.sectSubtitle = sectSubtitle.value;
		tmSrvySectInfo.sectTypeCd = sectTypeCd;
		tmSrvySectInfo.sectSqno = Number(sectSqno);
		
		
		let questBoxList = questContainerElement.getElementsByClassName("quest-box");
		let tmSrvyQstnList = new Array(); 	//질문 테이블 
		for(const questBoxElement of questBoxList){
			const questName = questBoxElement.querySelector('.quest-name');
			let tmSrvyQstnInfo 		= new Object();
			let tmSrvyAnsList 		= new Array();
			let qstnTitle 			= questBoxElement.querySelector(".quest-name > input");
			let qstnType 			= questName.dataset.qstnType;
			let qstnSqno 			= Number(questName.dataset.qstnOrdr);
			
			if(qstnType === 'CHECKBOX' || qstnType === 'RADIO' || qstnType === 'SELECTBOX'){
				let optionList = questBoxElement.getElementsByClassName("answ-content");
				for(const optionElement of optionList){
					let tmSrvyAnsInfo 	= new Object();
					let ansCnts 	= optionElement.value;
					
					if(!isNull(ansCnts)){
						let ansSqno		= optionElement.dataset.answSort;
						
						tmSrvyAnsInfo.ansCnts = ansCnts;
						tmSrvyAnsInfo.ansSqno = ansSqno;
						tmSrvyAnsList.push(tmSrvyAnsInfo);
					}	
				}
			}
			
			tmSrvyQstnInfo.qstnTitle 	= qstnTitle.value;
			tmSrvyQstnInfo.qstnTypeCd 	= qstnType;
			tmSrvyQstnInfo.qstnSqno 	= qstnSqno;
			tmSrvyQstnInfo.tmSrvyAnsList = tmSrvyAnsList;				
			tmSrvyQstnList.push(tmSrvyQstnInfo);
		}
		tmSrvySectInfo.tmSrvyQstnList = tmSrvyQstnList;
		tmSrvySectList.push(tmSrvySectInfo);
	}
	
	tmExmnMngSaveDTO.tmSrvySectList = tmSrvySectList;
	tmExmnMngSaveDTO.srvyTitle		= srvyTitle;
	tmExmnMngSaveDTO.srvyType		= srvyType;
	tmExmnMngSaveDTO.startDt		= startDt+"-01-01T00:00:00";
	tmExmnMngSaveDTO.endDt			= endDt+"-12-31T00:00:00";
	
	fetch(__contextPath__+"/datamng/survey",{
		method: "POST",
		headers: {
			'Content-Type': 'application/json;charset=utf-8'
		},
		body: JSON.stringify(tmExmnMngSaveDTO)
	})
	.then(response => response.json())
	.then(result => {
		if(result.code == '200'){
			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
				modal.close();
				window.location.href = __contextPath__+"/datamng/survey";
			}).open();
		}else{
			//중복클릭해제
			preventDuplicate.disabled = false;
			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){modal.close();}).open();
		}
	})
}

//설문지 정보 수정
function updateInvstMngSurveySctn(){
	//중복클릭방지
	preventDuplicate.disabled = true;
	
	let tmExmnMngUpdateDTO = new Object();	//설문 부문 테이블 
	let tmSrvySectList = new Array();	//설문 부문 테이블 
	let srvyId = document.getElementById("srvyId").value;
	let srvyTitle = document.getElementById("srvyTitle").value;
	let srvyType = document.getElementById("srvyType").value;
	let startDt = document.getElementById("startDt").value;
	let endDt = document.getElementById("endDt").value;
	let questContainerList = document.getElementsByClassName("quest-container");
	
	//validation
	const inputValidationItem  = document.querySelectorAll('.surveyValue');
	let inputValidation = [];
	inputValidationItem.forEach(input => inputValidation.push(input.value));
	const inputChecked = inputValidation.some(item => item === '');
	
	if(inputChecked) {
		new ModalBuilder().init().alertBody('비어있는 설문 목록이 있습니다. 모두 입력해주세요.').footer(4,'확인',function(button, modal){modal.close();}).open();
		return false;
	}
	
	for(const questContainerElement of questContainerList){
		let tmSrvySectInfo = new Object();
		//부제
		let sectTitle 	= questContainerElement.querySelector(".quest-title > input");
		let sectSubtitle 	= questContainerElement.querySelector(".quest-sub-title > input");
		//설문 타입/순서
		let sectTypeCd 	= questContainerElement.dataset.sctnType;
		let sectId 		= questContainerElement.dataset.sctnId;
		let sectSqno 	= questContainerElement.dataset.sctnOrdr;
		
		tmSrvySectInfo.srvyId = srvyId;
		tmSrvySectInfo.sectId = sectId;
		tmSrvySectInfo.sectTitle = sectTitle.value;
		tmSrvySectInfo.sectSubtitle = sectSubtitle.value;
		tmSrvySectInfo.sectTypeCd = sectTypeCd;
		tmSrvySectInfo.sectSqno = Number(sectSqno);
		
		
		let questBoxList = questContainerElement.getElementsByClassName("quest-box");
		let tmSrvyQstnList = new Array(); 	//질문 테이블 
		for(const questBoxElement of questBoxList){
			const questName = questBoxElement.querySelector('.quest-name');
			let tmSrvyQstnInfo 		= new Object();
			let tmSrvyAnsList 		= new Array();
			let qstnTitle 			= questBoxElement.querySelector(".quest-name > input");
			let qstnType 			= questName.dataset.qstnType;
			let qstnSqno 			= Number(questName.dataset.qstnOrdr);
			let qstnId	 			= questName.dataset.qstnId;
			
			if(qstnType === 'CHECKBOX' || qstnType === 'RADIO' || qstnType === 'SELECTBOX'){
				let optionList = questBoxElement.getElementsByClassName("answ-content");
				for(const optionElement of optionList){
					let tmSrvyAnsInfo 	= new Object();
					let ansCnts 	= optionElement.value;
					
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
			
			tmSrvyQstnInfo.qstnTitle 	= qstnTitle.value;
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
	tmExmnMngUpdateDTO.tmSrvySectList 	= tmSrvySectList;
	tmExmnMngUpdateDTO.deleteSectArray 	= deleteSectArray;
	tmExmnMngUpdateDTO.deleteQstnArray 	= deleteQstnArray;
	tmExmnMngUpdateDTO.deleteAnsArray 	= deleteAnsArray;
	tmExmnMngUpdateDTO.srvyId			= srvyId;
	tmExmnMngUpdateDTO.srvyTitle		= srvyTitle;
	tmExmnMngUpdateDTO.srvyType			= srvyType;
	tmExmnMngUpdateDTO.startDt			= startDt+"T00:00:00";
	tmExmnMngUpdateDTO.endDt			= endDt+"T00:00:00";

	fetch(__contextPath__+"/datamng/survey/"+srvyId,{
		method: "PUT",
		headers: {
			'Content-Type': 'application/json;charset=utf-8'
		},
		body: JSON.stringify(tmExmnMngUpdateDTO)
	})
	.then(response => response.json())
	.then(result => {
		deleteQstnArray = new Array();
		deleteAnsArray = new Array();
		if(result.code == '200'){
			new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
				modal.close();
				window.location.href = __contextPath__+"/datamng/survey/"+srvyId;
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
	const directionBox = document.querySelectorAll('.surveyDirectionBox');
	const locationSelect = document.querySelector('.survey-loaction-box');
	
	if(typeValue == 'OD' || typeValue == 'AXLELOAD' || typeValue == 'LABORSIDE') {
		objective.forEach(item => { 
			item.classList.remove('none')
			item.querySelectorAll('input').forEach(input => input.classList.add('surveyValue'));
		});
		directionBox.forEach(item => {
			item.classList.add('none')
			item.querySelectorAll('input').forEach(input => input.classList.remove('surveyValue'));
			if(typeValue == 'AXLELOAD') {
				document.getElementById('surveyDirectionBox').classList.remove('none');
				locationSelect.classList.remove('none');
			}
		});
		document.querySelector('.survey-loaction-box').classList.remove('none');
		document.getElementById('tableLocationColSpan').setAttribute('colSpan', '2');
	} else {
		objective.forEach(item => { 
			item.classList.add('none')
			item.querySelectorAll('input').forEach(input => input.classList.remove('surveyValue'));
		});
		directionBox.forEach(item => {
			item.classList.remove('none')
			item.querySelectorAll('input').forEach(input => input.classList.add('surveyValue'));
		});
		document.querySelector('.survey-loaction-box').classList.add('none');
		document.getElementById('tableLocationColSpan').setAttribute('colSpan', '');
	}
	
	document.getElementById('srvyId').value = "";
	document.getElementById('srvyTitle').value = "";
	locationSelect.querySelector('.startLocation').value = "선택";
	const twoLocation = locationSelect.querySelector('.twoLocation');
	const threeLocation = locationSelect.querySelector('.threeLocation');
	if(twoLocation) {
		locationSelect.querySelector('.twoLocation').remove();
		if(threeLocation) {
			locationSelect.querySelector('.threeLocation').remove();
		}
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

// questSectionNewAddModal
function questSectionAdd(){
	const defaultButtonBox = document.getElementById('questDefaultBox'); 
	new ModalBuilder().init('설문 섹션 추가').ajaxBody("/datamng/survey/section/save").footer(3,'추가',function(button, modal){
			modal.close();
			const sectType = document.querySelector('input[name="modalSectType"]:checked').value;
			surveySectionHTML(sectType);
			defaultButtonBox?.classList.add('none');
		}, '취소', function(button, modal){
			modal.close();
		}).open();
}

//questSectionHTML
function surveySectionHTML(sectType = ""){
	const containerAddBox = document.getElementById('questSurveyList');
	const containerIndex = document.querySelectorAll('.quest-container').length+1;
	const html =`
				<div class="quest-container" data-sctn-ordr="${containerIndex}" data-sctn-type="${sectType}">
					<div class="quest-wrap">
						<div class="quest-title-wrap">
							<h4 class="quest-title">
								<input type="text" class="input-reset surveyValue" placeholder="조사명을 입력하세요."/>
								<input type="button" class="quest-close" onclick="questContainerRemove(this);"/>
							</h4>
							<div class="quest-sub-title">
								<input type="text" class="input-reset surveyValue" placeholder="조사 부제를 입력하세요."/>
							</div>
						</div>
						<div class="quest-grid-box sortable">
							<div class="quest-box sortable-box" onmouseover="mouseOverSortable(this);">
								<div class="sortable-handle"><span></span></div>
								<div class="quest-title-box">
									<span class="quest-number">Q1.</span>
									<span class="quest-name" data-qstn-ordr="1" data-qstn-type="SUBJECTIVE">
										<input type="text" class="surveyValue" placeholder="질문 제목을 입력해주세요."/>
									</span>
									<select class="select-list-box" onchange="optionSelected(this);">
										<option value="SUBJECTIVE">주관식</option>
										<option value="SELECTBOX">드롭다운</option>
										<option value="RADIO">라디오</option>
										<option value="CHECKBOX">체크박스</option>
										<option value="DATE">날짜</option>
										<option value="TIME">시간</option>
										<option value="LOCATION">위치</option>
									</select>
									<input type="button" class="quest-close" onclick="questBoxRemove(this);"/>
								</div>
								<div class="list-answer">
									<div class="quest-view-item">
										<span class="quest-view-img"><img src="/images/quest_text.png" alt="short"></span>
										<span class="quest-view-text">주관식 답변란</span>
									</div>
								</div>
							</div>
						</div>
						<div class="quest-add-box">
							<input type="button" class="is-key-button" onclick="questBoxAdd(this);" value="설문항목 추가"/>
							<input type="button" class="is-key-button" onclick="questSectionAdd();" value="섹션 추가"/>
						</div>
					</div>
				</div>	
				`
	containerAddBox.insertAdjacentHTML('beforeend', html);	
}


// questList
function questBoxAdd(_this) {
	const questContainer = _this.closest('.quest-container');
	const questAddBox = questContainer.querySelector('.quest-grid-box');
	const questBoxIndex = questAddBox.querySelectorAll('.quest-box').length+1;
	const html =`
				<div class="quest-box sortable-box" onmouseover="mouseOverSortable(this);">
					<div class="sortable-handle"><span></span></div>
					<div class="quest-title-box">
						<span class="quest-number">Q${questBoxIndex}.</span>
						<span class="quest-name" data-qstn-ordr="${questBoxIndex}" data-qstn-type="SUBJECTIVE">
							<input type="text" class="surveyValue" placeholder="질문 제목을 입력해주세요."/>
						</span>
						<select class="select-list-box" onchange="optionSelected(this);">
							<option value="SUBJECTIVE">주관식</option>
							<option value="SELECTBOX">드롭다운</option>
							<option value="RADIO">라디오</option>
							<option value="CHECKBOX">체크박스</option>
							<option value="DATE">날짜</option>
							<option value="TIME">시간</option>
							<option value="LOCATION">위치</option>
						</select>
						<input type="button" class="quest-close" onclick="questBoxRemove(this);"/>
					</div>
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_text.png" alt="short"></span>
							<span class="quest-view-text">주관식 답변란</span>
						</div>
					</div>
				</div>	
				`
				
	questAddBox.insertAdjacentHTML('beforeend', html);
	
}

// selected layout show/hide
function optionSelected(_this) {
	const selectedItem = _this.value;
	const appendParent = _this.closest('.quest-box');
	const questitem = _this.closest('.quest-box').querySelector('.list-answer');
	const questType = appendParent.querySelector('.quest-name');
	
	//select, radio, checkbox
	let multipleItem = '<div class="list-answer">'
		multipleItem +=	'<ul class="all-answer">'					
		multipleItem +=		'<li class="option-list">'
		multipleItem +=			'<input type="text" name="qstnAnswContent" class="answ-content surveyValue" placeholder="새 옵션" data-answ-sort="1">'
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
									<span class="quest-view-text">주관식 답변란</span>
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
					`;
	//time
	const timeItem =`
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_time.png" alt="short"></span>
							<span class="quest-view-text">Time</span>
						</div>
					</div>					
					`;
	//location
	const locationItem =`
					<div class="list-answer">
						<div class="quest-view-item">
							<span class="quest-view-img"><img src="/images/quest_location.png" alt="short"></span>
							<span class="quest-view-text">Location</span>
						</div>
					</div>					
					`;
	
	if(questitem) questitem.remove();
	if(selectedItem === 'RADIO'){
		appendParent.insertAdjacentHTML('beforeend', multipleItem);
		questType.setAttribute('data-qstn-type', 'RADIO');
	} else if(selectedItem === 'TIME') {
		appendParent.insertAdjacentHTML('beforeend', timeItem);
		questType.setAttribute('data-qstn-type', 'TIME');
	} else if(selectedItem === 'DATE') {
		appendParent.insertAdjacentHTML('beforeend', dateItem);
		questType.setAttribute('data-qstn-type', 'DATE');
	} else if(selectedItem === 'LOCATION') {
		appendParent.insertAdjacentHTML('beforeend', locationItem);
		questType.setAttribute('data-qstn-type', 'LOCATION');
	} else if(selectedItem === 'SUBJECTIVE'){
		appendParent.insertAdjacentHTML('beforeend', shortAnswerItem);
		questType.setAttribute('data-qstn-type', 'SUBJECTIVE');
	} else if(selectedItem === 'CHECKBOX') {
		appendParent.insertAdjacentHTML('beforeend', multipleItem);
		questType.setAttribute('data-qstn-type', 'CHECKBOX');
	} else if(selectedItem === 'SELECTBOX') {
		appendParent.insertAdjacentHTML('beforeend', multipleItem);
		questType.setAttribute('data-qstn-type', 'SELECTBOX');
	}
	
}

// questboxSortable
function mouseOverSortable(_this){
	/*_this.querySelector('.sortable-handle').classList.remove('none');*/
	
    $(".sortable").sortable({
		handle: '.sortable-handle',
		stop: function(e, ui){
			const sortableBox = e.target.querySelectorAll('.sortable-box');
			sortableBox.forEach(function(item,idx){
				item.querySelector('.quest-name').setAttribute('data-qstn-ordr', idx+1);
				item.querySelector('.quest-number').textContent ='Q'+(idx+1)+'.';
			})
		}
	});
	$(".sortable").disableSelection();		
}

function mouseLeaveSortable(_this){
	_this.querySelector('.sortable-handle').classList.add('none');
}

//설문삭제
function questBoxRemove(_this,qstnId = ''){
	const questContainer = _this.closest('.quest-grid-box');
	const questBox = _this.closest('.quest-box');
	
	questBox.remove();
	
	const questNumber = questContainer.querySelectorAll('.quest-number');
	const questName = questContainer.querySelectorAll('.quest-name');
	questNumber.forEach((number,index) => number.textContent = `Q${index+1}.`)
	questName.forEach((attr,index) => attr.setAttribute('data-qstn-ordr', index+1))
	
	if(!isNull(qstnId)){
		deleteQstnArray.push(qstnId);
	}
}

//세션삭제
function questContainerRemove(_this,sectId = ''){
	const closest = _this.closest('.quest-container');
	const defaultButtonBox = document.getElementById('questDefaultBox');
	
	new ModalBuilder().init().alertBody('삭제하면 복구가 불가능합니다. 삭제하시겠습니까?').footer(3,'확인',function(button, modal){
		closest.remove();
		const container = document.querySelectorAll('.quest-container').length;
		if(container == 0) defaultButtonBox.classList.remove('none');
		
		if(!isNull(sectId)){
			deleteSectArray.push(sectId);
		}
		
		modal.close();
	}, '취소', function(button, modal){
		modal.close();
	}).open();
}

// 설문 삭제
function deleteInvstMngSurveySctn(srvyId){
	new ModalBuilder().init().alertBody('설문을 삭제 하시겠습니까?').footer(3,'확인',function(button, modal){
		fetch(__contextPath__ + "/datamng/survey/" + srvyId,{
			method: "DELETE",
		})
		.then(response => response.json())
		.then(result => {
				if(result.code == '200'){
					new ModalBuilder().init().alertBody(result.message).footer(4,'확인',function(button, modal){
						modal.close();
						window.location.href= __contextPath__ + "/datamng/survey";
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

//select year set
dateYearSet = () => {
	let yearOption;
	const getYear = new Date().getFullYear();
	const startYear = getYear - 30;
	const endYear = getYear + 30;
	const yearHTML = document.querySelectorAll('.dateYear');
	
	for(let y = startYear; y <= endYear; y++) {
		yearOption += `<option value="${y}">${y}</option>`;
	}
	
	yearHTML.forEach(select => {
		select.insertAdjacentHTML('beforeend', yearOption);
		select.querySelector(`option[value='${getYear}']`).selected = true;
	});
}


//survey detail chart
function chartCustom(elementsId, chartColor, dataValue, dataMax){
    const elements = document.getElementById(elementsId)
    let elementsWidth = elements.offsetWidth;
    let options = {
        value:  dataValue,
        size: elementsWidth,
        lineWidth: 10,
        rotate: 0,
        max: dataMax,
        units: dataValue,
        totalUnits:'/'+ dataMax
    };

    const html =	'<div class="chart-box">'+
    				'	<canvas></canvas>'+
    				'	<div class="percent"></div>'+
    				'</div>'+
    				'<div class="units-box">'+
    					'<span class="chart-units"></span>'+
                        '<span class="total-units"></span>'+
    				'</div>'
    				
	elements.innerHTML = html;
	
    const canvas = elements.querySelector('canvas');
    const percent = elements.querySelector('.percent');
    const units = elements.querySelector('.chart-units');
    const totalUnits = elements.querySelector('.total-units');
    
    units.textContent = options.units;
    totalUnits.textContent = options.totalUnits;
    
    const ctx = canvas.getContext('2d');
    canvas.width = canvas.height = options.size;
    elements.querySelector('.chart-box').style.height = options.size + 'px';
    percent.textContent = (dataValue / dataMax * 100).toFixed(0) + '%';
	elements.querySelector('.chart-units').style.color = chartColor;
	elements.querySelector('.percent').style.color = chartColor;
    
    ctx.translate(options.size / 2, options.size / 2);
    ctx.rotate((-1 / 2 + options.rotate / 180) * Math.PI);
    
    const radius = (options.size - options.lineWidth) / 2;
    
    function drawCircle(color, lineWidth, value) {
        value = Math.min(Math.max(0, value || 1), 1);
        ctx.beginPath();
        ctx.arc(0, 0, radius, 0, Math.PI * 2 * value, false);
        ctx.strokeStyle = color;
        ctx.lineCap = 'round';
        ctx.lineWidth = lineWidth;
        ctx.stroke();
    };
    
    drawCircle('#f1f1f1', options.lineWidth, 100 / 100);
    drawCircle(chartColor, options.lineWidth, options.value / options.max);   
}

