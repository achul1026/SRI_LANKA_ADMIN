let deleteSectArray = new Array();
let deleteAnsArray = new Array();
let deleteQstnArray = new Array();
let deleteDrctArray = new Array();
let sectTypeCdArray = new Array();
let metadataCdArray = new Array();

// optionAdd
let optionAdd = function(_this, isPreset, value) {
   const eleParent = isPreset ? _this : _this.closest('.list-answer');
   const optionParent = eleParent.querySelector('.all-answer');
   let optionList = eleParent.querySelectorAll('.option-list');
   const listLength = optionList.length+1;
   
   const optionEtc = eleParent.querySelector('.optionEtc');
      
   let option = document.createElement('li');
       option.className = 'option-list';
       option.innerHTML = `<span class="option-number"></span><input type="text" class="answ-content validation-tag" placeholder="${message.survey.survey_js_placeholder_new_option}" data-answ-sort="${listLength}" data-others-yn="N" ${isPreset ? `value="${value}"` : ''}  ${isPreset ? 'readonly' : ''}>
                           ${!isPreset ? `<input type="button" onclick="optionRemove(this)" value="${message.common.button_delete}">` : ''}`;
      
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
   eleParent.querySelectorAll('.option-number').forEach((item, index) => item.innerText = `${index+1}.`);
   
}
// optionEtc
let optionEtcAdd = function(_this, isPreset, value) {
   const eleParent = isPreset ? _this : _this.closest('.list-answer');
   const listLength = eleParent.querySelectorAll('.option-list').length+1;
   const optionEtc = eleParent.querySelectorAll('.optionEtc');
   const optionEtcNumber = eleParent.querySelectorAll('.option-number').length;
   
   let option = '<li class="option-list optionEtc">';
   	  option +=   `<span class="option-number">${optionEtcNumber+1}.</span>`
      option +=   '<input type="text" class="answ-content" data-answ-sort="'+listLength+'" value="'+(value ? value : 'Other')+'" data-others-yn="Y" readonly>'
      if(!isPreset) option += '<input type="button" onclick="optionRemove(this)" value="'+message.common.button_delete+'"/>'
      option +='</li>';
   
   if(optionEtc.length < 1) {
      eleParent.querySelector('.all-answer').insertAdjacentHTML('beforeend', option);
   }
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
      
   eleParent.querySelectorAll('.option-number').forEach((item, index) => item.innerText = `${index+1}.`);
   
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
   new ModalBuilder().init(message.invest.invstMngrList_regist_manager).ajaxBody("/datamng/invst/modal/mngr").footer(1,message.common.button_regist,function(button, modal){
      let mngrId, mngrNmValue;
      const mngrChecked    	  = document.querySelector('.mngrCheckBox:checked');
      const modalUsermngId    = mngrChecked.value;
      const modalUserId       = mngrChecked.closest('tr').querySelector('.userId').textContent;
      const modalUserNm       = mngrChecked.closest('tr').querySelector('.userNm').textContent;

      const exmnpicIdElement = document.getElementById('exmnpicId');
      exmnpicIdElement.value = modalUserId;
      const usermngIdElement = document.getElementById('usermngId');
      usermngIdElement.value = modalUsermngId;
      const userNmElement    = document.getElementById('userNm');
      userNmElement.value    = modalUserNm;

      if(userNmElement.classList.contains('tag-invalid')){
         userNmElement.classList.remove('tag-invalid');
      }
      
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
   new ModalBuilder().init(message.survey.survey_js_survey_form_regist).ajaxBody("/datamng/invst/modal/survey?searchType="+exmnType).footer(1,message.common.button_regist,function(button, modal){
      let mngrId, mngrNmValue;
      const surveyChecked    = document.querySelector('.surveyCheckBox:checked');
      const modalSrvyId      = surveyChecked.value;
      const modalCstmYn      = surveyChecked.dataset.cstmYn;
      const modalSrvyTitle   = surveyChecked.closest('tr').querySelector('.srvyTitle').textContent;

      const srvyIdElement = document.getElementById('srvyId');
      srvyIdElement.value = modalSrvyId;
      const cstmYnElement = document.getElementById('cstmYn');
      cstmYnElement.value = modalCstmYn;
      const srvyTitleElement = document.getElementById('srvyTitle');
      srvyTitleElement.value = modalSrvyTitle;

      //validation      
      if(srvyTitleElement.classList.contains('tag-invalid')){
         srvyTitleElement.classList.remove('tag-invalid');
      }
       
      modal.close();
   }).open();
}

//교통조사등록
let inputValidation = null;
function saveInvstMngSurvey(){
   if(validation('#invstSaveForm')){
      preventDuplicate.disabled = true;
      let tmExmnMngSaveDTO = new Object();
      let invstSaveForm = new FormData($("#invstSaveForm")[0]);
      
      const invstKndCd = document.getElementById('exmnType');
      const type = invstKndCd.options[invstKndCd.selectedIndex].dataset.hasDrct;
      
      const colorType = document.querySelector('input[name="colorType"]:checked').value;
   
      //시작 기간 / 종료 기간
      const invstStrDt = document.getElementById('invstStrDt').dataset.startDate;
      const invstEndDt = document.getElementById('invstEndDt').dataset.endDate;
      const startHour = document.querySelector('.startHour').value;
      const startMinute = document.querySelector('.startMinute').value;
      const invstStrTime = getInvstTime(startHour,startMinute);
      const endHourElement = document.querySelector('#endHour');
      const endHour = endHourElement.value;
      const endMinute = document.querySelector('.endMinute').value;
      let invstEndTime = getInvstTime(endHour,endMinute);
     
      //날짜 + 시간

      const startDt = new Date(invstStrDt);
      const addStartDt = dateFormatForYYYYMMDD(startDt) +"T"+ invstStrTime;
      const endDt = new Date(invstEndDt);

      const exmnType = invstKndCd.options[invstKndCd.selectedIndex].dataset.invstType;
      if(exmnType === 'survey'){
         invstEndTime = '23:59:59';
      }

      const addEndDt = dateFormatForYYYYMMDD(endDt) +"T"+ invstEndTime;
      const locationSelectList = document.querySelectorAll(".location-select");

      let exmnLc = '';
      let code = '';
      locationSelectList.forEach((locationSelectItem,idx) => {
          const dataType = locationSelectItem.dataset.type;
          const selectedOption = locationSelectItem.querySelector('option:checked');
          
          if(!isNull(selectedOption.value)) {
            if(idx === 0){
               exmnLc = `${selectedOption.value}`;
            }else{
               exmnLc += `, ${selectedOption.value}`;
            }
            
            //dsd/gn
            code += `${selectedOption.dataset.cd}`;
            if(dataType === 'dsdNm'){
               invstSaveForm.append('dsdCd',code);
            }else if(dataType === 'gnNm'){
               invstSaveForm.append('gnCd',code);
            }
            
         }
      });
      
      
      invstSaveForm.append('exmnLc',exmnLc);
      invstSaveForm.append('startDt',addStartDt);
      invstSaveForm.append('endDt',addEndDt);
      invstSaveForm.append('colrCd',colorType);
      
      var tmExmnMng = serializeObjectByFormData(invstSaveForm);
      console.log(tmExmnMng)
      tmExmnMngSaveDTO.tmExmnMng = tmExmnMng;
      
      //조사 방향(TC(MCC,TM)조사인경우)
      if(type === 'true'){
         let tmExmnDrctList = new Array();
         const drctWrap = document.getElementsByClassName('drct-wrap');
         for(let drctElelment of drctWrap){
            let tmExmnDrct    = new Object();
            let drctSqno    = drctElelment.dataset.drctSqno;
            let startlcNm    = drctElelment.querySelector('.startlcNm');
            let endlcNm    = drctElelment.querySelector('.endlcNm');
            if(!isNull(startlcNm.value) && !isNull(endlcNm.value)){
               tmExmnDrct.drctSqno    = drctSqno;
               tmExmnDrct.startlcNm    = startlcNm.value;
               tmExmnDrct.endlcNm       = endlcNm.value;
               tmExmnDrctList.push(tmExmnDrct);
            }
         }
         tmExmnMngSaveDTO.tmExmnDrctList = tmExmnDrctList;
         
      }
   
      //조사 반경
      const exmnRangeElemenet = document.getElementById('exmnRange');
      const exmnRangeValue = Number(exmnRangeElemenet.value);
      exmnRange.value = exmnRangeValue;
      
      console.log(tmExmnMngSaveDTO)
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
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
                  window.location.href = __contextPath__+returnUrl;
               }).open();
            }else{
               preventDuplicate.disabled = false;
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
               }).open();
            }
      })
   } else {
      preventDuplicate.disabled = false;
      return false;
   }
}

function updateInvstMngSurvey(exmnmngId){
   if(validation('#invstSaveForm')){
      preventDuplicate.disabled = true;
      let tmExmnMngUpdateDTO = new Object();
      let invstSaveForm = new FormData($("#invstSaveForm")[0]);
      
      const invstKndCd = document.getElementById('exmnType');
      const type = invstKndCd.options[invstKndCd.selectedIndex].dataset.invstType;
      const hasDrct = invstKndCd.options[invstKndCd.selectedIndex].dataset.hasDrct;
      const invstTypecd = invstKndCd.options[invstKndCd.selectedIndex].dataset.invstTypecd;
      const colorType = document.querySelector('input[name="colorType"]:checked').value;
   
      //시작 기간 / 종료 기간
      const invstStrDt = document.getElementById('invstStrDt').dataset.startDate;
      const invstEndDt = document.getElementById('invstEndDt').dataset.endDate;
      const startHour = document.querySelector('.startHour').value;
      const startMinute = document.querySelector('.startMinute').value;
      const invstStrTime = getInvstTime(startHour,startMinute);
      const endHourElement = document.querySelector('.endHour');
      const endHour = endHourElement.value;
      const endMinute = document.querySelector('.endMinute').value;
      const invstEndTime = getInvstTime(endHour,endMinute);;

      
      //날짜 + 시간   
      const startDt = new Date(invstStrDt);
      const addStartDt = dateFormatForYYYYMMDD(startDt) +"T"+ invstStrTime;
      const endDt = new Date(invstEndDt);
      const addEndDt = dateFormatForYYYYMMDD(endDt) +"T"+ invstEndTime;
      
      
      //조사 위치 (DSD/GN CODE)
      const locationSelectList = document.querySelectorAll(".location-select");
      let dsdCd = document.getElementById('dsdCd').value;
      let gnCd = document.getElementById('gnCd').value;
      let exmnLc = document.getElementById('exmnLcValue').value;
      
      let code = '';
      locationSelectList.forEach((locationSelectItem,idx) => {
	  	const dataType = locationSelectItem.dataset.type;
        const selectedOption = locationSelectItem.querySelector('option:checked');
         
        if(!isNull(selectedOption.value)) {
        	if(idx === 0){
            	exmnLc = `${selectedOption.value}`;
            }else{
                exmnLc += `, ${selectedOption.value}`;
            }
           
            //dsd/gn
            code += `${selectedOption.dataset.cd}`;
            if(dataType === 'dsdNm'){
            	dsdCd = code;
            	gnCd = '';
            }else if(dataType === 'gnNm'){
                gnCd = code;
           }
           
        }
     });
     
      
      invstSaveForm.append('exmnLc',exmnLc);
      invstSaveForm.append('dsdCd',dsdCd);
      invstSaveForm.append('gnCd',gnCd);
      invstSaveForm.append('startDt',addStartDt);
      invstSaveForm.append('endDt',addEndDt);
      invstSaveForm.append('exmnmngId',exmnmngId);
      invstSaveForm.append('colrCd',colorType);
      
      var tmExmnMng = serializeObjectByFormData(invstSaveForm);
      tmExmnMngUpdateDTO.tmExmnMng = tmExmnMng;
      
      //조사 방향(TC(MCC,TM)조사인경우)
      if(hasDrct === 'true'){
         let tmExmnDrctList = new Array();
         const drctWrap = document.getElementsByClassName('drct-wrap');
         for(let drctElelment of drctWrap){
            let tmExmnDrct    = new Object();
            let drctSqno    = drctElelment.dataset.drctSqno;
            let exmndrctId    = drctElelment.dataset.exmndrctId;
            let startlcNm    = drctElelment.querySelector('.startlcNm');
            let endlcNm    = drctElelment.querySelector('.endlcNm');
            if(!isNull(startlcNm.value) && !isNull(endlcNm.value)){
               tmExmnDrct.exmndrctId    = exmndrctId;
               tmExmnDrct.drctSqno    = drctSqno;
               tmExmnDrct.exmnmngId    = exmnmngId;
               tmExmnDrct.startlcNm    = startlcNm.value;
               tmExmnDrct.endlcNm       = endlcNm.value;
               tmExmnDrctList.push(tmExmnDrct);
            }
         }
         tmExmnMngUpdateDTO.tmExmnDrctList = tmExmnDrctList;
         
         //수정 시 삭제된 방향 정보 DB 삭제
         tmExmnMngUpdateDTO.deleteDrctArray = deleteDrctArray; 
      }
   
      //조사 반경
      const exmnRangeElemenet = document.getElementById('exmnRange');
      const exmnRangeValue = Number(exmnRangeElemenet.value);
      exmnRange.value = exmnRangeValue;
      
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
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
                  const exmnmngId = result.data.exmnmngId;
                  let returnUrl = "/datamng/invst/"+exmnmngId;
                  window.location.href = __contextPath__+returnUrl;
               }).open();
            }else{
               preventDuplicate.disabled = false;
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
            }
         })
   } else {
      preventDuplicate.disabled = false;
   }
}

//설문지 정보 저장
function saveInvstMngSurveySctn(cstmYn = "N"){
   //중복클릭방지
   preventDuplicate.disabled = true;
   
   let tmExmnMngSaveDTO = {};   //설문 부문 테이블
   let tmSrvySectList = [];   //설문 부문 테이블
   let srvyTitle = document.getElementById("srvyTitle").value;
   let srvyType = document.getElementById("srvyType").value;
   let questContainerList = document.getElementsByClassName("quest-container");
   
   if(validation('#infoTableWrap')){
      const sectionValidationItem  = document.querySelectorAll('.quest-container');
      if(sectionValidationItem.length <= 0) {
         preventDuplicate.disabled = false;
         new MsgModalBuilder().init().alertBody(message.survey.survey_js_section_notExist).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
         return false;
      } else {
         let isQuestBox = true;
         sectionValidationItem.forEach(container => {
            const questBox = container.querySelectorAll('.quest-box');
            if(questBox.length <= 0){
               isQuestBox = false;
            }
         });
         if (!isQuestBox) {
            preventDuplicate.disabled = false;
            new MsgModalBuilder().init().alertBody(message.survey.survey_js_question_notExist).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
            return false;
         }
      }
      
      for(const questContainerElement of questContainerList){
         let tmSrvySectInfo = {};
         //부제
         let sectTitle    = questContainerElement.querySelector(".quest-title > input");
         let sectSubtitle    = questContainerElement.querySelector(".quest-sub-title > input");
         //설문 타입/순서
         let sectTypeCd    = questContainerElement.querySelector(".sectTypeCd > option:checked").value;
         let sectSqno    = questContainerElement.dataset.sctnOrdr;
         
         tmSrvySectInfo.sectTitle = sectTitle.value;
         tmSrvySectInfo.sectSubtitle = sectSubtitle.value;
         tmSrvySectInfo.sectTypeCd = sectTypeCd;
         tmSrvySectInfo.sectSqno = Number(sectSqno);
         
         let questBoxList = questContainerElement.getElementsByClassName("quest-box");
         let tmSrvyQstnList = [];    //질문 테이블
         for(const questBoxElement of questBoxList){
            const questName = questBoxElement.querySelector('.quest-name');
            let tmSrvyQstnInfo       = {};
            let tmSrvyAnsList       = [];
            let qstnTitle          = questBoxElement.querySelector(".quest-name > input");
            let qstnType          = questName.dataset.qstnType;
            let metadata          = questName.dataset.metadata;
            let qstnSqno          = Number(questName.dataset.qstnOrdr);
            
            if(qstnType === 'CHECKBOX' || qstnType === 'RADIO' || qstnType === 'SELECTBOX'){
               let optionList = questBoxElement.getElementsByClassName("answ-content");
               
               //객관식 밸리데이션
               if(optionList.length < 2) {
                  new MsgModalBuilder().init().alertBody(message.survey.survey_js_multiple_choice_answers_must).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
                  return false
               }
               
               for(const optionElement of optionList){
                  let tmSrvyAnsInfo    = new Object();
                  let ansCnts    = optionElement.value;
                  let etcYn       = optionElement.dataset.othersYn;
                  
                  if(!isNull(ansCnts)){
                     let ansSqno      = optionElement.dataset.answSort;
                     
                     tmSrvyAnsInfo.etcYn   = etcYn;
                     tmSrvyAnsInfo.ansCnts = ansCnts;
                     tmSrvyAnsInfo.ansSqno = ansSqno;
                     tmSrvyAnsList.push(tmSrvyAnsInfo);
                  }   
               }
            }
            
            tmSrvyQstnInfo.qstnTitle    = qstnTitle.value;
            tmSrvyQstnInfo.qstnTypeCd    = qstnType;
            tmSrvyQstnInfo.metadataCd   = metadata;
            tmSrvyQstnInfo.qstnSqno    = qstnSqno;
            tmSrvyQstnInfo.tmSrvyAnsList = tmSrvyAnsList;            
            tmSrvyQstnList.push(tmSrvyQstnInfo);
         }
         tmSrvySectInfo.tmSrvyQstnList = tmSrvyQstnList;
         tmSrvySectList.push(tmSrvySectInfo);
      }
      
      tmExmnMngSaveDTO.tmSrvySectList = tmSrvySectList;
      tmExmnMngSaveDTO.srvyTitle      = srvyTitle;
      tmExmnMngSaveDTO.srvyType       = srvyType;
      tmExmnMngSaveDTO.cstmYn         = cstmYn;
      
      const loading = new setLoading().start();
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
            new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
               modal.close();
               window.location.href = __contextPath__+"/datamng/survey";
            }).open();
         }else{
            //중복클릭해제
            preventDuplicate.disabled = false;
            new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
				loading.end();
				modal.close();
			}).open();
         }
      })
      .catch((error) => {
		console.error('Error:', error);
	  }).finally(() => {
		loading.end();
	  });      
   } else {
      preventDuplicate.disabled = false;
   }
}

//설문지 정보 수정
function updateInvstMngSurveySctn(){
   //중복클릭방지
   preventDuplicate.disabled = true;
   
   let tmExmnMngUpdateDTO = {};   //설문 부문 테이블
   let tmSrvySectList = [];   //설문 부문 테이블
   let srvyId = document.getElementById("srvyId").value;
   let srvyTitle = document.getElementById("srvyTitle").value;
   let cstmYn = document.getElementById("cstmYn").value;
   let srvyType = document.getElementById("srvyType").value;
//   let startDt = document.getElementById("startDt").value;
//   let endDt = document.getElementById("endDt").value;
   let questContainerList = document.getElementsByClassName("quest-container");
   
   if(validation('#infoTableWrap')){
      const sectionValidationItem  = document.querySelectorAll('.quest-container');
      if(sectionValidationItem.length <= 0) {
         preventDuplicate.disabled = false;
         new MsgModalBuilder().init().alertBody(message.survey.survey_js_section_notExist).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
         return false;
      } else {
         let isQuestBox = true;
         sectionValidationItem.forEach(container => {
            const questBox = container.querySelectorAll('.quest-box');
            if(questBox.length <= 0) isQuestBox = false;
         });
         if (!isQuestBox) {
            preventDuplicate.disabled = false;
            new MsgModalBuilder().init().alertBody(message.survey.survey_js_question_notExist).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
            return false;
         }
      }
      
      for(const questContainerElement of questContainerList){
         let tmSrvySectInfo = {};
         //부제
         let sectTitle    = questContainerElement.querySelector(".quest-title > input");
         let sectSubtitle    = questContainerElement.querySelector(".quest-sub-title > input");
         //설문 타입/순서
         let sectTypeCd    = questContainerElement.querySelector(".sectTypeCd > option:checked").value;
         let sectId       = questContainerElement.dataset.sctnId;
         let sectSqno    = questContainerElement.dataset.sctnOrdr;
         
         tmSrvySectInfo.srvyId = srvyId;
         tmSrvySectInfo.sectId = sectId;
         tmSrvySectInfo.sectTitle = sectTitle.value;
         tmSrvySectInfo.sectSubtitle = sectSubtitle.value;
         tmSrvySectInfo.sectTypeCd = sectTypeCd;
         tmSrvySectInfo.sectSqno = Number(sectSqno);
         
         
         let questBoxList = questContainerElement.getElementsByClassName("quest-box");
         let tmSrvyQstnList = [];    //질문 테이블
         for(const questBoxElement of questBoxList){
            const questName = questBoxElement.querySelector('.quest-name');
            let tmSrvyQstnInfo       = {};
            let tmSrvyAnsList       = [];
            let qstnTitle          = questBoxElement.querySelector(".quest-name > input");
            let qstnType          = questName.dataset.qstnType;
            let metadata          = questName.dataset.metadata;
            let qstnSqno          = Number(questName.dataset.qstnOrdr);
            let qstnId             = questName.dataset.qstnId;
            
            if(qstnType === 'CHECKBOX' || qstnType === 'RADIO' || qstnType === 'SELECTBOX'){
               let optionList = questBoxElement.getElementsByClassName("answ-content");
               for(const optionElement of optionList){
                  let tmSrvyAnsInfo    = {};
                  let ansCnts    = optionElement.value;
                  
                  //객관식 밸리데이션
                  if(optionList.length < 2) {
                  preventDuplicate.disabled = false;
                     new MsgModalBuilder().init().alertBody(message.survey.survey_js_multiple_choice_answers_must).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
                     return false
                  }
                  
                  if(!isNull(ansCnts)){
                     let ansSqno      = optionElement.dataset.answSort;
                     let ansId      = optionElement.dataset.answId;
                     let etcYn       = optionElement.dataset.othersYn;
                     
                     tmSrvyAnsInfo.ansId    = ansId;
                     tmSrvyAnsInfo.qstnId    = qstnId;
                     tmSrvyAnsInfo.ansCnts    = ansCnts;
                     tmSrvyAnsInfo.etcYn    = etcYn;
                     tmSrvyAnsInfo.ansSqno    = ansSqno;
                     tmSrvyAnsList.push(tmSrvyAnsInfo);
                  }
               }
            }
            
            tmSrvyQstnInfo.qstnTitle    = qstnTitle.value;
            tmSrvyQstnInfo.sectId      = sectId;
            tmSrvyQstnInfo.qstnId      = qstnId;
            tmSrvyQstnInfo.qstnTypeCd    = qstnType;
            tmSrvyQstnInfo.metadataCd    = metadata;
            tmSrvyQstnInfo.qstnSqno    = qstnSqno;
            tmSrvyQstnInfo.tmSrvyAnsList= tmSrvyAnsList;            
            tmSrvyQstnList.push(tmSrvyQstnInfo);
         }
         tmSrvySectInfo.tmSrvyQstnList = tmSrvyQstnList;
         tmSrvySectList.push(tmSrvySectInfo);
      }
      tmExmnMngUpdateDTO.tmSrvySectList    = tmSrvySectList;
      tmExmnMngUpdateDTO.deleteSectArray    = deleteSectArray;
      tmExmnMngUpdateDTO.deleteQstnArray    = deleteQstnArray;
      tmExmnMngUpdateDTO.deleteAnsArray    = deleteAnsArray;
      tmExmnMngUpdateDTO.srvyId         = srvyId;
      tmExmnMngUpdateDTO.srvyTitle      = srvyTitle;
      tmExmnMngUpdateDTO.cstmYn    		  = cstmYn;
      tmExmnMngUpdateDTO.srvyType         = srvyType;
      //tmExmnMngUpdateDTO.startDt         = startDt+"-01-01T00:00:00";
      //tmExmnMngUpdateDTO.endDt         = endDt+"-12-31T00:00:00";
      
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
            new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
               modal.close();
               window.location.href = __contextPath__+"/datamng/survey/"+srvyId;
            }).open();
         }else{
            preventDuplicate.disabled = false;
            new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
         }
      })
   } else {
      preventDuplicate.disabled = false;
   }
}

//설문지 제목 수정
function updateInvstMngSurveySectInfo(){
   //중복클릭방지
	preventDuplicate.disabled = true;
   
   	if(validation('#surveyTitleUpdateBox')) {
	   	let tmExmnMngUpdateDTO = {};   //설문 부문 테이블
	   	let srvyId = document.getElementById("srvyId").value;
	   	let srvyTitle = document.getElementById("surveyTitleInput").value;
	  	tmExmnMngUpdateDTO.srvyId         = srvyId;
	  	tmExmnMngUpdateDTO.srvyTitle      = srvyTitle;
	  	
	  	const loading = new setLoading().start();
	  	fetch(__contextPath__+"/datamng/survey/sect/"+srvyId,{
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
				new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
					window.location.href = __contextPath__+"/datamng/survey/"+srvyId;
					modal.close();
				}).open();
			}else{
				preventDuplicate.disabled = false;
				new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
					loading.end();
					modal.close();
				}).open();
			}	
		})
		.catch((error) => {
			console.error('Error:', error);
		})
		.finally(() => {
			loading.end();
		});
		
	} else {
		preventDuplicate.disabled = false;
	}
}

let surveyTitleUpdate = () => {
	const tableBox = document.getElementById('surveyTitleBox');
	const title = document.getElementById('surveyTtlteDetail');
	const html = `
					<div id="surveyTitleUpdateBox" class="flex-center gap8">
						<input type="text" id="surveyTitleInput" class="input-table-text validation-tag flex-1" placeholder="${message.survey.survey_js_survey_title}" onkeyup="valid(this);"/>
						<input type="button" class="is-basic-button" onclick="surveyTitleCancel();" value="${message.common.button_cancel}"/>
						<input type="button" class="is-key-button" onclick="updateInvstMngSurveySectInfo();" value="${message.common.button_save}"/>
					</div>
				 `
	title.classList.add('none');
	tableBox.insertAdjacentHTML('beforeend', html);
}

let surveyTitleCancel = () => {
	document.getElementById('surveyTitleUpdateBox').remove();
	document.getElementById('surveyTtlteDetail').classList.remove('none');
}

//조사 정보 삭제
function deleteInvstInfo(exmnmngId){
   preventDuplicate.disabled = true;
   new MsgModalBuilder().init().alertBody(message.survey.survey_js_invest_delete_confirm).footer(3,message.common.button_confirm,function(button, modal){
      fetch(__contextPath__ + "/datamng/invst/" + exmnmngId,{
         method: "DELETE",
      })
      .then(response => response.json())
      .then(result => {
            if(result.code == '200'){
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
                  window.location.href= __contextPath__ + "/datamng/invst";
               }).open();
            }else{
               preventDuplicate.disabled = false;
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
               }).open();
            }
         })
      modal.close();
   }, message.common.button_cancel, function(button, modal){
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
function mapToLocation(tazcd) {
   let type = document.getElementById("exmnType").value;
   if((type === "TM" || type === 'MCC') && tazcd) {
      tazcd = tazcd.toString();
      let province = tazcd.slice(0,1);
      let distrct = tazcd.slice(1,2);
      let dsd = "0"+tazcd.slice(2,4);
      let gn = tazcd.slice(4,tazcd.length);
      
      const evt = new Event('change');
      function chgEvent(name, value) {
         document.querySelectorAll(".location-select[data-type='"+name+"'] option").forEach((elem) => {
            if(elem.dataset.cd === value)  {
               elem.setAttribute("selected", true);
               document.querySelector(".location-select[data-type='"+name+"']").dispatchEvent(evt);
            }
         });
      }
      
      chgEvent("provinNm", province);
      chgEvent("districtNm", distrct);
      chgEvent("dsdNm", dsd);
      chgEvent("gnNm", gn);
   }
}

updateLocation = (tazcd) => {
	tazcd = tazcd.toString();
	let province = tazcd.slice(0,1);
	let distrct = tazcd.slice(1,2);
	let dsd = tazcd.slice(2,5);
	let gn = tazcd.slice(5,tazcd.length);
	const evt = new Event('change');
	function chgEvent(name, value) {
		document.querySelectorAll(".location-select[data-type='"+name+"'] option").forEach((elem) => {
	        if(elem.dataset.cd === value)  {
	           elem.setAttribute("selected", true);
	           document.querySelector(".location-select[data-type='"+name+"']").dispatchEvent(evt);
	        }
		});
	}
      
	chgEvent("provinNm", province);
	chgEvent("districtNm", distrct);
	chgEvent("dsdNm", dsd);
	chgEvent("gnNm", gn);
}
//조사 타입별 노출 옵션
function typeOption(select = true){
   const surveyType = document.getElementById('exmnType');
   const typeValue = surveyType.value;
   const surveyOD = document.querySelectorAll('.survey-OD');
   const surveyTM = document.querySelectorAll('.survey-TM');
   const locationBox = document.querySelectorAll('.location-event > div');
   const locationDisplay = document.getElementById('displayExmnLc');
   const locationUpdate = document.getElementById('updateExmnLc');
   const roadSidePointEl = document.getElementById('roadSidePoint');
   const roadSideDefaultEl = document.querySelector('.roadside-pointer-default');
   const roadSideEl = document.querySelector('.roadside-pointer-update');
   const roadNameDefaultEl = document.querySelector('.roadname-default');
   const roadNameEl = document.querySelector('.roadname-update');
   const roadNameInput = document.querySelector('#surveyPointNameDefault');
   const roadNameText = document.querySelectorAll('.survey-text-value');
   const roadsideUpdateBtn = document.getElementById('roadsideUpdate');
   const roadNameUpdateBtn = document.getElementById('roadNameUpdate');
   const directContainer = document.getElementById('surveyDirectionContainer');
   const directAppendBox = document.getElementById('surveyDirectionAppendBox');
   
   if(typeValue == 'OD' || typeValue == 'AXLELOAD' || typeValue == 'ROADSIDE') {
      surveyOD.forEach(item => {
         item.classList.remove('none')
         item.querySelectorAll('.input-table-text').forEach(input => input.classList.add('validation-tag'));
      });
      surveyTM.forEach(item => {
         item.classList.add('none')
         item.querySelectorAll('.validation-tag').forEach(input => input.classList.remove('validation-tag'));
         if(typeValue == 'AXLELOAD') {
            document.getElementById('surveyTM').classList.remove('none');
            document.querySelectorAll('#surveyTM .input-table-text').forEach(input => input.classList.add('validation-tag'));
         }
      });
      if(roadSidePointEl) {
	      roadSidePointEl.classList.add('none');
	      roadSidePointEl.querySelectorAll('.select-list-box').forEach(select => select.classList.remove('validation-tag'));
      }
      if(typeValue == 'ROADSIDE') {
		 roadSidePointEl.classList.remove('none');
		 roadSidePointEl.querySelectorAll('.select-list-box').forEach(select => select.classList.add('validation-tag'));
		 if(roadNameDefaultEl) {
			 roadSideDefaultEl.classList.add('none');
			 roadSideEl.classList.remove('none');
			 roadsideUpdateBtn.classList.add('none');
		 }
	  }
      mapRemove();
   } else {
      surveyOD.forEach(item => { 
         item.classList.add('none')
         item.querySelectorAll('.input-table-text').forEach(input => input.classList.remove('validation-tag'));
         item.querySelectorAll('.select-list-box').forEach(select => select.classList.remove('validation-tag'));
      });
      surveyTM.forEach(item => {
         item.classList.remove('none')
         item.querySelectorAll('.input-table-text').forEach(input => input.classList.add('validation-tag'));
         item.querySelectorAll('.select-list-box').forEach(select => select.classList.add('validation-tag'));
      });
      if(roadSidePointEl) {
	      roadSidePointEl.classList.add('none');
	      roadSidePointEl.querySelectorAll('.select-list-box').forEach(select => select.classList.remove('validation-tag'));
      }
      if(roadNameDefaultEl){
	      roadNameDefaultEl.classList.add('none');
	      roadNameEl.classList.remove('none');
	      roadNameInput.value = "";
	      roadNameText.forEach(x => x.innerText = '');
	      roadNameUpdateBtn.classList.add('none');
	  }
	  
      roadNameEl.querySelectorAll('.select-list-box').forEach(s => {
		s.querySelectorAll('option').forEach(o => {
			if(o.value == '') o.selected = true;
		})
	  })
	  
	  if(directContainer) directAppendBox.innerHTML = surveyDirectionHtml();
	  
      mapRemove();
  	  (async () => {
	      let mapgl = await mapboxGl();
	      setTazCodeLayer(mapgl);
	      setMapZoomControl(mapgl);
	      setMapSearch(mapgl);
	      setTrafficClickMaker(mapgl, mapToLocation);
	  })();   
      
   }
   
   //조사위치 노출,비노출, 버튼 비노출
   if(locationDisplay){
      locationDisplay.classList.add('none');
      locationUpdate.classList.remove('none');
      locationUpdate.querySelector('input[type="button"]').classList.add('none')
   }
   
   
   locationBox.forEach((location,idx) => {
      if(idx !== 0) {
         location.remove();
      } else {
         location.querySelector('.select-list-box').value = '';
      }
   });
   
   if(select == true) {
      infoReset('.input-table-text', '.select-date-box .select-list-box', '00');
   }
}

let surveyDirectionHtml = () => {
	const html = `
				<tr class="drct-wrap" data-drct-sqno="1">
					<td class="text-center directionNumber">1</td>
					<td><input type="text" class="input-table-text startlcNm validation-tag" id="startlcNm" name="startlcNm" placeholder="${message.invest.invstSave_placeholder_starting_point}" onkeyup="valid(this);"/></td>
					<td><input type="text" class="input-table-text endlcNm validation-tag" id="endlcNm" name="endlcNm" placeholder="${message.invest.invstSave_placeholder_end_point}" onkeyup="valid(this);"/></td>
					<td class="text-center">
				    	<button type="button" onclick="directionRemove(this);">
				        	<img src="/images/delete_img.png" class="cm-csimg" alt="${message.invest.invstSave_alt_delete}">
				    	</button>
				    </td>
				</tr>
				`
	return html
}






let roadSideSelect = (_this) => {

	const selectEl = _this;
	const nextSelectEl = document.getElementById('surveyRegion');
	const regionType = document.getElementById('regionType');
	const column = regionType.options[regionType.selectedIndex].dataset.column;
	document.getElementById('surveyPointNameDefault').name = '';
	const surveyPointNameInput = document.getElementById('surveyPointNameInput');
	let option = '';
	
	if(selectEl.value == 'RTC001') {
		surveyPointNameInput.name = column;
		option = `
				<option value="">${message.common.select}</option>
				<option value="3,A1">3(A1)</option>
				<option value="4,A1">4(A1)</option>
				<option value="5,A2">5(A2)</option>
				<option value="6,A3">6(A3)</option>
				<option value="7,A4">7(A4)</option>
				<option value="8,A7">8(A7)</option>
				<option value="9,A8">9(A8)</option>
				<option value="10,B114">10(B114)</option>
				<option value="11,B158">11(B158)</option>
				<option value="12,B322">12(B322)</option>
				<option value="13,B356">13(B356)</option>
				<option value="14,B363">14(B363)</option>
				<option value="16,B421">16(B421)</option>
				<option value="17,B445">17(B445)</option>
				<option value="18,B456">18(B456)</option>
				<option value="19,B503">19(B503)</option>	
			 	`
	} else if(selectEl.value == 'RTC002') {
		surveyPointNameInput.name = column;
		option = `
				<option value="">${message.common.select}</option>
				<option value="1,Welipenna Interchange Toll Booth">1(Welipenna Interchange Toll Booth)</option>
				<option value="2,Canowin Arcade Side A/B">2(Canowin Arcade Side A/B)</option>
				<option value="3,Dodangoda Interchange Toll Booth">3(Dodangoda Interchange Toll Booth)</option>
				<option value="4,Gelanigama Interchange Toll Booth">4(Gelanigama Interchange Toll Booth)</option>
				<option value="5,Kahathuduwa Interchange Toll Booth">5(Kahathuduwa Interchange Toll Booth)</option>
				<option value="6,Kottawa Interchange Toll Booth">6(Kottawa Interchange Toll Booth)</option>
				<option value="7,Athurugiriya Interchange Toll Booth">7(Athurugiriya Interchange Toll Booth)</option>
				<option value="8,Kaduwela Interchage Toll Booth">8(Kaduwela Interchage Toll Booth)</option>
				<option value="9,Kadawatha Interchange Toll Booth">9(Kadawatha Interchange Toll Booth)</option>
				<option value="10,Kerawalapitiya Interchange Toll Booth">10(Kerawalapitiya Interchange Toll Booth)</option>
				<option value="11,Ja Ela Interchage Toll Booth">11(Ja Ela Interchage Toll Booth)</option>
			 	`
	} else {
		option = `<option value="">${message.common.select}</option>`
		surveyPointNameInput.name = "";
	}
	nextSelectEl.innerHTML = option;
}

let roadSidePointUpdate = (_this) => {
	const element = _this;
	const parent = element.closest('.flex-center');
	const pointerDefault = parent.querySelector('.roadside-pointer-default');
	const pointerUpdate = parent.querySelector('.roadside-pointer-update');
	if(pointerUpdate.classList.contains('none')) {
		pointerDefault.classList.add('none');
		pointerUpdate.classList.remove('none');
		pointerUpdate.querySelectorAll('.select-list-box').forEach(select => select.classList.add('validation-tag'));
		element.value = message.common.button_cancel;
	} else {
		pointerUpdate.classList.add('none');
		pointerDefault.classList.remove('none');
		pointerUpdate.querySelectorAll('.select-list-box').forEach(select => select.classList.remove('validation-tag'));
		element.value = message.common.button_update;
		const locationPointType = document.getElementById('locationPointType').value;
		document.getElementById('surveyPointNameDefault').name = locationPointType;
		document.getElementById('surveyPointNameInput').name = '';
	}
}

let roadNameSelect = (_this) => {
	const selectEl = _this;
	const nextSelectEl = document.getElementById('surveyPointName');
	const roadNmType = document.getElementById('roadNmType');
	const column = roadNmType.options[roadNmType.selectedIndex].dataset.column;
	document.getElementById('surveyPointNameDefault').name = '';
	const surveyPointNameInput = document.getElementById('surveyPointNameInput');
	let option = '';
	
	if(selectEl.value == 'RTC001') {
		surveyPointNameInput.name = column;
		option = `
				<option value="">${message.common.select}</option>
				<option value="1,E01">1(E01)</option>
				<option value="2,E04">2(E04)</option>
				<option value="3,A1">3(A1)</option>
				<option value="4,A1">4(A1)</option>
				<option value="5,A2">5(A2)</option>
				<option value="6,A3">6(A3)</option>
				<option value="7,A4">7(A4)</option>
				<option value="8,A6">8(A7)</option>
				<option value="9,A8">9(A8)</option>
				<option value="10,B114">10(B114)</option>
				<option value="11,B158">11(B158)</option>
				<option value="12,B332">12(B332)</option>
				<option value="13,B356">13(B356)</option>
				<option value="14,B363">14(B363)</option>
				<option value="15,B408">15(B408)</option>
				<option value="16,B421">16(B421)</option>		
				<option value="17,B445">17(B445)</option>		
				<option value="18,B456">18(B456)</option>		
				<option value="19,B503">19(B503)</option>		
			 	`
	} else if(selectEl.value == 'RTC003') {
		surveyPointNameInput.name = column;
		option = `
				<option value="">${message.common.select}</option>
				<option value="20,E01">1(E01)</option>
				<option value="21,E01">2(E01)</option>
				<option value="22,E01">3(E01)</option>
				<option value="23,E01">4(E01)</option>
				<option value="24,E01">5(E01)</option>
				<option value="25,E02">6(E02)</option>
				<option value="26,E02">7(E02)</option>
				<option value="27,E02">8(E02)</option>
				<option value="28,E02">9(E02)</option>
				<option value="29,A1">10(A1)</option>
				<option value="30,A1">11(A1)</option>
				<option value="31,A1">12(A1)</option>
				<option value="32,A3">13(A3)</option>
				<option value="33,A2">14(A2)</option>
				<option value="34,A2">15(A2)</option>
				<option value="35,A2">16(A2)</option>		
				<option value="36,A4">17(A4)</option>		
				<option value="37,E03">18(E03)</option>		
				<option value="38,B146">19(B146)</option>			
				<option value="39,AB10">20(AB10)</option>			
				<option value="40,B157">21(B157)</option>			
				<option value="41,A8">22(A8)</option>			
				<option value="42,A8">23(A8)</option>			
				<option value="43,A3">24(A3)</option>			
				<option value="44,B472">25(B472)</option>			
				<option value="45,A1">26(A1)</option>			
				<option value="46,B47">27(B47)</option>			
				<option value="47,B84">28(B84)</option>			
				<option value="48,B208">29(B208)</option>			
				<option value="49,B111">30(B111)</option>			
				<option value="50,B208">31(B208)</option>			
				<option value="51,B214">32(B214)</option>			
				<option value="52,B157">33(B157)</option>			
				<option value="53,B421">34(B421)</option>			
				<option value="54,B304">35(B304)</option>			
				<option value="55,A4">36(A4)</option>			
				<option value="56,A2">37(A2)</option>			
				<option value="57,B445">38(B445)</option>			
				<option value="58,B459">39(B459)</option>			
				<option value="59,B458">40(B458)</option>			
			 	`
	} else {
		option = `<option value="">${message.common.select}</option>`
	}
	nextSelectEl.innerHTML = option;
}

let roadNameUpdateFn = function(_this) {
	const element = _this;
	const parent = element.closest('.flex-center');
	const roadNameDefault = parent.querySelector('.roadname-default');
	const roadNameUpdate = parent.querySelector('.roadname-update');
	if(roadNameUpdate.classList.contains('none')) {
		roadNameDefault.classList.add('none');
		roadNameUpdate.classList.remove('none');
		roadNameUpdate.querySelectorAll('.select-list-box').forEach(select => select.classList.add('validation-tag'));
		document.getElementById('roadNm').classList.add('validation-tag');
		element.value = message.common.button_cancel;
	} else {
		roadNameUpdate.classList.add('none');
		roadNameDefault.classList.remove('none');
		roadNameUpdate.querySelectorAll('.select-list-box').forEach(select => select.classList.remove('validation-tag'));
		document.getElementById('roadNm').classList.remove('validation-tag');
		element.value = message.common.button_update;
		const locationPointType = document.getElementById('locationPointType').value;
		document.getElementById('surveyPointNameDefault').name = locationPointType;
	}
}
 

//input, select 초기화
function infoReset(inputReset, selectReset = null, selectValue = ''){
   const resetItem = document.querySelectorAll(`${inputReset}`);
   resetItem.forEach(reset => reset.value = '');
   if(selectReset !== null) {
      const resetItem2 = document.querySelectorAll(`${selectReset}`);
      resetItem2.forEach(reset => reset.value = `${selectValue}`);
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
                  new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
                  window.location.reload();
               }).open();
               }else{
                  preventDuplicate.disabled = false;
                  new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
               }
            }
      )   
}

//questSectionHTML
function surveySectionHTML(sectType = "", isPreset, callback){
   const containerAddBox = document.getElementById('questSurveyList');
   const containerIndex = document.querySelectorAll('.quest-container').length+1;
   let container = document.createElement('div');
   container.className= "quest-container";
   container.setAttribute("sctn-ordr", containerIndex);
   container.setAttribute("sctn-type", sectType);
   let html =`
               <div class="quest-wrap">
              	  <div class="quest-body-wrap">
	                <div class="quest-title-wrap">
           			    <div class="quest-title">
							<input type="text" id="surveyTitle${containerIndex}" class="input-reset validation-tag"
								placeholder="${message.survey.survey_js_placeholder_invest_name}" />
							  ${!isPreset ?  `<input type="button" class="quest-close" onclick="questContainerRemove(this);"/>` : ''}
						</div>
						<div class="quest-sub-title">
							<input type="text" id="surveySubTitle${containerIndex}" class="input-reset validation-tag"
								placeholder="${message.survey.survey_js_placeholder_invest_sub_name}" />
						</div>
						<div class="select-div-box quest-select-title ${isPreset ? "none" : ""}">
							<select class="select-list-box sectTypeCd validation-tag table-select quest-select"
								data-validation="select">
								<option value="" selected>${message.survey.survey_js_type_select}</option>
							    ${sectTypeCdArray.map(x => `<option value="${x.cd}">${x.cdNm}</option>`)}
							</select>
						</div>
	                  </div>    
	                  <div class="quest-grid-box sortable">
               `;
      if(!isPreset) {
        html += `
         	  <div class="quest-box sortable-box" onmouseover="mouseOverSortable(this);">
					<div class="quest-list-wrap">
						<div class="sortable-handle"><span></span></div>
						<div class="quest-list">
							<div class="quest-head">
								<span class="quest-number">Q1</span>
								<input type="button" class="quest-close"onclick="questBoxRemove(this);" />
							</div>
							<div class="quest-title-box">
								<span class="quest-name" data-qstn-ordr="1" data-meta="${metadataCdArray[0].cd}" data-metadata="${metadataCdArray[0].cd}" data-qstn-type="SUBJECTIVE">
									<input type="text" class="validation-tag"
										placeholder="${message.survey.survey_js_placeholder_question_title}" />
								</span>
								<div class="select-div-box">
									<select class="select-list-box table-select quest-select select-type"
										onchange="optionSelected(this, ${isPreset});">
										<option value="SUBJECTIVE">${message.survey.survey_js_subjective}</option>
										<option value="SELECTBOX">${message.survey.survey_js_selectbox}</option>
										<option value="RADIO">${message.survey.survey_js_radio}</option>
										<option value="CHECKBOX">${message.survey.survey_js_checkbox}</option>
										<option value="DATE">${message.survey.survey_js_date}</option>
										<option value="TIME">${message.survey.survey_js_time}</option>
										<option value="LOCATION">${message.survey.survey_js_location}</option>
										<option value="GPS">${message.survey.survey_js_gps}</option>
									</select>
								</div>
							</div>
							<div class="select-div-box mb8">
								 <select class="select-list-box table-select quest-select wd100" onchange="optionSelectedMetadata(this);">
		                             ${metadataCdArray.map(x => `<option value="${x.cd}">${x.cdNm}</option>`)}
		                        </select>
							</div>
							<div class="list-answer">
								<div class="quest-view-item">
									<img src="/images/quest_text.png" alt="short"
										class="quest-view-img">
									<span class="quest-view-text">${message.survey.survey_js_subjective_answer_box}</span>
								</div>
							</div>
						</div>
					</div>
				</div>
                 `;
                     
      }

         html +=`</div>`;
         
   if(!isPreset) {
      html += `
    		<div class="quest-add-box">
				<input type="button" class="is-sub-button" onclick="questBoxAdd(this, false, '', 'Y');"
					value="${message.survey.survey_js_button_quest_box_add}"/>
				<input type="button" class="is-sub-button" onclick="surveySectionHTML();"
					value="${message.survey.survey_js_button_section_add}"/>
			</div>
            `;
   }
      html +=`</div></div>`;
   container.innerHTML = html;
   containerAddBox.appendChild(container);
   
   const defaultButtonBox = document.getElementById('questDefaultBox');
   const containerLength = document.querySelectorAll('.quest-container').length;
   if(containerAddBox <= 1) defaultButtonBox.classList.add('none');

   if(typeof callback === "function") callback(container)
}


// questList
function questBoxAdd(_this, isPreset, callback, cstmYn  = 'N') {
   const questContainer = isPreset ? _this : _this.closest('.quest-container');
   const questAddBox = questContainer.querySelector('.quest-grid-box');
   const questBoxIndex = questAddBox.querySelectorAll('.quest-box').length+1;
   const hidingPresetElementStyle = isPreset ? ' readonly  style="display:none;"' : '';
   let elem = document.createElement('div');
   elem.className= "quest-box sortable-box";
   elem.onmouseover = mouseOverSortable.bind(elem);
   let html = `
   			<div class="quest-list-wrap">
				<div class="sortable-handle"><span></span></div>
				<div class="quest-list">
					<div class="quest-head">
						<span class="quest-number">Q${questBoxIndex}</span>
			  `
   				if(cstmYn === 'Y'){
		html +=			`<input type="button" class="quest-close"onclick="questBoxRemove(this);" />`
				}					  
		html +=				
					`</div>
					<div class="quest-title-box">
						<span class="quest-name" data-qstn-ordr="${questBoxIndex}" data-meta="${metadataCdArray[0].cd}" data-metadata="${metadataCdArray[0].cd}" data-qstn-type="SUBJECTIVE">
							<input type="text" class="validation-tag quest-name__input"
								placeholder="${message.survey.survey_js_placeholder_question_title}" />
						</span>
						<div class="select-div-box">
							<select class="select-list-box table-select quest-select select-type" onchange="optionSelected(this, ${isPreset});" ${hidingPresetElementStyle}>
								<option value="SUBJECTIVE">${message.survey.survey_js_subjective}</option>
								<option value="SELECTBOX">${message.survey.survey_js_selectbox}</option>
								<option value="RADIO">${message.survey.survey_js_radio}</option>
								<option value="CHECKBOX">${message.survey.survey_js_checkbox}</option>
								<option value="DATE">${message.survey.survey_js_date}</option>
								<option value="TIME">${message.survey.survey_js_time}</option>
								<option value="LOCATION">${message.survey.survey_js_location}</option>
								<option value="GPS">${message.survey.survey_js_gps}</option>
							</select>
						</div>
					</div>
					<div class="select-div-box mb8">
						<select class="select-list-box table-select quest-select select-meta" onchange="optionSelectedMetadata(this);" ${hidingPresetElementStyle}>
	                        ${metadataCdArray.map(x => `<option value="${x.cd}">${x.cdNm}</option>`)}
	                     </select>
					</div>
					<div class="list-answer">
						<div class="quest-view-item">
							<img src="/images/quest_text.png" alt="short" class="quest-view-img">
							<span class="quest-view-text">${message.survey.survey_js_subjective_answer_box}</span>
						</div>
					</div>
				</div>
			</div>   
   `
   elem.innerHTML = html;
   questAddBox.appendChild(elem);
   if(typeof callback === "function") callback(elem);
}
function optionSelectedMetadata(_this) {
   const appendParent = _this.closest('.quest-box');
   const questType = appendParent.querySelector('.quest-name');
   questType.setAttribute('data-metadata', _this.value);
}

// selected layout show/hide
function optionSelected(_this, isPreset) {
   const selectedItem = _this.value;
   const appendParent = _this.closest('.quest-list');
   const questitem = _this.closest('.quest-box').querySelector('.list-answer');
   const questType = appendParent.querySelector('.quest-name');
   
   //select, radio, checkbox
   let multipleItem = '<div class="list-answer">'
      multipleItem +=   '<ul class="all-answer">'               
      multipleItem +=      '<li class="option-list">'
      if(!isPreset) {
		 multipleItem += '<span class="option-number">1.</span>'
         multipleItem += '<input type="text" name="qstnAnswContent" class="answ-content validation-tag" placeholder="'+message.survey.survey_js_placeholder_new_option+'" data-answ-sort="1" data-others-yn="N">'
         multipleItem += '<input type="button" onclick="optionRemove(this)" value="'+message.common.button_delete+'"/>'
      }
      multipleItem +=      '</li>'
      multipleItem +=   '</ul>'
      if(!isPreset) {
         multipleItem += '<div class="list-append">'
         multipleItem += '<input type="button" class="mr8" onclick="optionAdd(this)" value="'+message.survey.survey_js_button_option_add+'"/>'
         multipleItem += '<input type="button" onclick="optionEtcAdd(this)" value="'+message.survey.survey_js_button_etc_option_add+'"/>'
      }
      multipleItem +=   '</div>'
      multipleItem +='</div>'
   //short answer
   const shortAnswerItem = `
                     <div class="list-answer">
                        <div class="quest-view-item">
                           <span class="quest-view-img"><img src="/images/quest_text.png" alt="short"></span>
                           <span class="quest-view-text">${message.survey.survey_js_subjective_answer_box}</span>
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
   
   //gps
   const gpsItem =`
               <div class="list-answer">
                  <div class="quest-view-item">
                     <span class="quest-view-img"><img src="/images/quest_location.png" alt="short"></span>
                     <span class="quest-view-text">GPS</span>
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
   } else if(selectedItem === 'GPS') {
	  appendParent.insertAdjacentHTML('beforeend', gpsItem);
      questType.setAttribute('data-qstn-type', 'GPS');	
   }   
}

// questboxSortable
function mouseOverSortable(_this){
    $(".sortable").sortable({
      handle: '.sortable-handle',
      stop: function(e, ui){
         const sortableBox = e.target.querySelectorAll('.sortable-box');
         sortableBox.forEach(function(item,idx){
            item.querySelector('.quest-name').setAttribute('data-qstn-ordr', idx+1);
            item.querySelector('.quest-number').textContent ='Q'+(idx+1);
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
   
   new MsgModalBuilder().init().alertBody(message.authRqst.authSetting_delete_confirm_message).footer(3,message.common.button_confirm,function(button, modal){
      closest.remove();
      const container = document.querySelectorAll('.quest-container').length;
      if(container == 0) defaultButtonBox.classList.remove('none');
      
      if(!isNull(sectId)){
         deleteSectArray.push(sectId);
      }
      
      modal.close();
   }, message.common.button_cancel, function(button, modal){
      modal.close();
   }).open();
}

// 설문 삭제
function deleteInvstMngSurveySctn(srvyId){
   new MsgModalBuilder().init().alertBody(message.survey.survey_js_survey_delete_confirm).footer(3,message.common.button_confirm,function(button, modal){
      fetch(__contextPath__ + "/datamng/survey/" + srvyId,{
         method: "DELETE",
      })
      .then(response => response.json())
      .then(result => {
            if(result.code == '200'){
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
                  window.location.href= __contextPath__ + "/datamng/survey";
               }).open();
            }else{
               preventDuplicate.disabled = false;
               new MsgModalBuilder().init().alertBody(result.message).footer(4,message.common.button_confirm,function(button, modal){
                  modal.close();
               }).open();
            }
         })
      modal.close();
   }, message.common.button_cancel, function(button, modal){
      modal.close();
   }).open();
}   

//select year set
let dateYearSet = (i = 30) => {
   let yearOption = '';
   const getYear = new Date().getFullYear();
   const startYear = getYear - i;
   const endYear = getYear + i;
   const yearHTML = document.querySelectorAll('.dateYear');
   const surveySaveHTML = document.querySelector('.survey-select-save');
   if(!surveySaveHTML) yearOption = `<option value="">${message.common.select}</option>`;
   
   for(let y = startYear; y <= endYear; y++) {
      yearOption += `<option value="${y}">${y}</option>`;
   }
   
   yearHTML.forEach(select => {
      select.insertAdjacentHTML('beforeend', yearOption);
      //select.querySelector(`option[value='${getYear}']`).selected = true;
   });
}


const dateYearSetMinAndMax = (min = 1970 , max = new Date().getFullYear()) => {
	let yearOption = '';
	const yearHTML = document.querySelectorAll('.dateYear');
    const surveySaveHTML = document.querySelector('.survey-select-save');
    
    if(!surveySaveHTML) yearOption = `<option value="">${message.common.select}</option>`;
   
    for(let i = min; i <= max; i++) {
      yearOption += `<option value="${i}">${i}</option>`;
    }
    
   yearHTML.forEach(select => {
      select.insertAdjacentHTML('beforeend', yearOption);
      //select.querySelector(`option[value='${getYear}']`).selected = true;
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

    const html =   '<div class="chart-box">'+
                '   <canvas></canvas>'+
                '   <div class="percent"></div>'+
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
let metaDataArr = {};

/*function validateDateOrder(data) {
    const smdKeys = Object.keys(data[]).filter(key => key.startsWith('SMD'));
    
    for (let i = 1; i < smdKeys.length; i++) {
        const currentKey = smdKeys[i];
        const previousKey = smdKeys[i - 1];

        const currentDate = new Date(data[][currentKey].date);
        const previousDate = new Date(data[][previousKey].date);

        if (currentDate < previousDate) {
		    alert(`상위 시간보다 하위 시간이 더 늦을 수 없습니다.`)
			return
        } else {
            console.log(`${currentKey} 된대`);
        }
    }
}*/
let setDateTimeValidCheck = (data) => {
    let success = true;
    
    for (const [parentIndex, codes] of Object.entries(data)) {
        const sortedEntries = Object.entries(codes).sort((a, b) => {
            let numA = parseInt(a[0].replace('SMD', ''), 10);
            let numB = parseInt(b[0].replace('SMD', ''), 10);
            return numA - numB;
        });

        for (let i = 0; i < sortedEntries.length - 1; i++) {
            const [currentCode, currentDatetime] = sortedEntries[i];
            const [nextCode, nextDatetime] = sortedEntries[i + 1];
            const currentDate = new Date(currentDatetime);
            const nextDate = new Date(nextDatetime);
            
            if (currentDate > nextDate) {
				const modalContain = document.querySelector('.modal-container');
				if(!modalContain) {
	                new MsgModalBuilder().init().alertBody(message.schedule.scheduleDetail_ans_modify_time).footer(4, message.common.button_confirm, function (button, modal) {
	                    modal.close();
	                }).open();
				}
                console.log(data)
                success = false;
            }
        }
    }

    return success; 	    
}
 
let dateTimeValid = () => {
	let result = true;
	const dateSect = document.querySelectorAll('[data-type="dateTime"]');
	let dataAll = {};
	
	dateSect.forEach(dateSects=>{
		const dataSqno = dateSects.dataset.sectSqno;
		const dataCodeNm = dateSects.dataset.metaDataCode;
		const dataUpdate = dateSects.dataset.update;
		const dateValue = dateSects.querySelector('.kecc-date-value').value; 
		
		if(dataUpdate === 'Y'){
			if(!isNull(dateValue)) {
			    if(!dataAll[dataSqno]) dataAll[dataSqno] = {};
    			dataAll[dataSqno][dataCodeNm] = dateValue;
				
			}
		}
	})
	
	if(!setDateTimeValidCheck(dataAll)) result = false
	return result
}

let scheduleKeccSurveyUpdata = () => {
	let updateResult = false;
	const srvyrsltId = document.getElementById('srvyrsltId').value;
	const questInputBox = document.querySelectorAll('.quset-kecc-result-box');
	
	let keccData = {
		srvyrsltId : srvyrsltId,
		ansList : new Array()
	};
	
	
	
	if(validation('#scheduleHistoryContainer')){
		if(dateTimeValid()) {
		
			questInputBox.forEach(list => {
				const type = list.dataset.type;
				
				
				if(type === 'input') {
					const questId = list.querySelector('.srvyansId').value;
					const questValue = list.querySelector('.kecc-result').value;
					const questUpdate = list.querySelector('.kecc-result').dataset.update
					
					if(questUpdate === 'Y') {
						keccData.ansList.push({
							 srvyansId : questId,
							 ansCnts : questValue,
							 etcYn : 'N'
						});
						updateResult = true;
					}
					
				} else if(type === 'checked') {
					const questUpdate = list.querySelector('.quest-kecc-result').dataset.update
					const checkedItem = list.querySelector('.quest-result-input:checked');
					if(questUpdate === 'Y') {
						if(checkedItem) {
							const checkedValue = checkedItem.closest('.input-result-value-list').querySelector('.kecc-value').value;
							const checkedCode = checkedItem.closest('.input-result-value-list').querySelector('.kecc-value').dataset.metaDataCode;
							const checkedId = checkedItem.closest('.quest-kecc-result').querySelector('.srvyansId').value;
							if(checkedValue === 'Other' && checkedCode === 'SMD018') {
								const checkedDirect = checkedItem.closest('.input-result-value-list').querySelector('.survey-input-direct').value;
								keccData.ansList.push({
									srvyansId : checkedId,
									ansCnts : checkedDirect,
									etcYn : 'Y'
								});
							} else {
								keccData.ansList.push({
									srvyansId : checkedId,
									ansCnts : checkedValue,
									etcYn : 'N'
								});
							}
						}	
						updateResult = true;		
					}
				} else if(type === 'dateTime') {
					const questId = list.querySelector('.srvyansId').value;
					const questValue = list.querySelector('.kecc-date-value').value;
					const questElapsedTime = list.querySelector('.kecc-date-elapsedTime').value;
					const listUpdate = list.dataset.update;
					
					if(listUpdate === 'Y') {
						if(questValue != '') {
							keccData.ansList.push({
				                srvyansId: questId,
				                ansCnts: questValue+questElapsedTime,
				                etcYn: 'N'
							})
						}
						updateResult = true;
					}
				}
			});
			
			if(updateResult === false) {
				new MsgModalBuilder().init().alertBody(message.schedule.scheduleDetail_ans_not_modify).footer(4,message.common.button_confirm,function(button, modal){
		        	modal.close();
		        }).open();
				return
			}
		}

		/* 데이터 담겨 져있는 변수 KeccData*/
		console.log(keccData)
	}

}


let setKeccDate = (_this) => {
	const element = _this;
	const parent = element.closest('.input-result-value-list');
	const date = parent.querySelector('.startPicker').value;
	const hour = parent.querySelector('.kecc-hour option:checked').value;
	const minute = parent.querySelector('.kecc-minute option:checked').value;
	const resultValue = parent.querySelector('.kecc-date-value');
	const container = element.closest('.kecc-quest-wrap'); 
	const wrapSqno = element.closest('.quset-kecc-result-box').dataset.sectSqno;
	const dateItemBox = container.querySelectorAll(`[data-sect-sqno="${wrapSqno}"]`);
	
	if(date != '' && hour != '' && minute != '') {
		resultValue.value = `${date} ${hour}:${minute}`;
		dateItemBox.forEach(box =>  {
			const metaData = box.dataset.metaDataCode; 
			const boxDate = box.querySelector('.startPicker').value;
			const boxHour = box.querySelector('.kecc-hour option:checked').value;
			const boxMinute = box.querySelector('.kecc-minute option:checked').value;
			
			box.dataset.update = 'Y';
			
			if(!metaDataArr[wrapSqno]) metaDataArr[wrapSqno] = {};
	        if(!metaDataArr[wrapSqno][metaData]) {
	            metaDataArr[wrapSqno][metaData] = {
	                date : `${boxDate} ${boxHour}:${boxMinute}`
	            };
	        } else {
				metaDataArr[wrapSqno][metaData].date = `${boxDate} ${boxHour}:${boxMinute}`;
			}
		});
		
		// 00:00 데이터를 제외하고 다시 가공
		const filterData = (data) => {
		    return Object.keys(data).reduce((acc, key) => {
		        acc[key] = Object.keys(data[key]).reduce((subAcc, subKey) => {
		            if (data[key][subKey].date !== " 00:00") {
		                subAcc[subKey] = data[key][subKey];
		            }
		            return subAcc;
		        }, {});
		        return acc;
		    }, {});
		};
		
		metaDataArr = filterData(metaDataArr);
		
		const parseDate = (dateStr) => {
		    if (!dateStr || dateStr.trim() === '00:00') {
		        return null;
		    }
		
		    const [datePart, timePart] = dateStr.split(' ');
		    const formattedDateStr = `${datePart}T${timePart}`;
		    
		    const date = new Date(formattedDateStr);
		    return date;
		};
		
		const minutesToHM = (minutes) => {
		    const hours = Math.floor(minutes / 60);
		    const mins = minutes % 60;
		    return `${String(hours).padStart(2, '0')}:${String(mins).padStart(2, '0')}`;
		};		

		const calculateTimeDifferences = (data) => {
		    const items = data[wrapSqno];
		    const keys = Object.keys(items)
		        .map(key => ({
		            key,
		            date: parseDate(items[key].date)
		        }))
		        .filter(item => item.date !== null);
		
		    const results = {};
		
		    for (let i = 1; i < keys.length; i++) {
		        const prevItem = keys[i - 1];
		        const currentItem = keys[i];
		
		        const currentDate = currentItem.date;
		        const prevDate = prevItem.date;
		        
		        if (isNaN(currentDate.getTime()) || isNaN(prevDate.getTime())) {
		            continue;
		        }
		
		        const diffInMs = currentDate - prevDate;
		        const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
				
				const differenceHM = minutesToHM(diffInMinutes);
				
		        results[currentItem.key] = {
					sectSqno : wrapSqno,
		            metaData : currentItem.key,
		            elapsedTime : differenceHM
		        };
		    }
		
		    return results;
		};

		metaDataArr = calculateTimeDifferences(metaDataArr);
		
		
		for(const key in metaDataArr) {
			const item = metaDataArr[key];
			const container = document.querySelector(`[data-sect-sqno="${item.sectSqno}"][data-meta-data-code=${item.metaData}]`);
			const elElapsedTime = container.querySelector('.kecc-date-elapsedTime');
			elElapsedTime.value = `,${item.elapsedTime}`
		}
	}
}


let setKeccUpdateChecked = (_this, type = 'input') => {
	const element = _this;
	
//	const quesInput = document.querySelectorAll('.kecc-result');
//		console.log(quesInput);
//		quesInput.forEach(quesInputs =>{
//			console.log('빈 값 입니다.');
//			if (quesInputs.value === '') {
//			    quesInputs.placeholder = "빈 값 입니다.";
//			}
//		})
	
	if(type === 'input') {
		element.dataset.update = 'Y';
		if(element.type === 'text'){
			element.classList.add('validation-tag');
		}
	} else if(type === 'checked') {
		const parent = element.closest('.quest-kecc-result');
		const label = element.closest('.input-result-value-list');
		parent.dataset.update = 'Y';
		
		const directInput = label.querySelector('.survey-input-direct');
		const allDirectInput = parent.querySelector('.survey-input-direct');
		if(element.checked == true) {
			if(directInput) {
				directInput.readOnly = false;
				directInput.setAttribute('onkeyup', 'setKeccUpdateChecked(this, "checked")');
			} else {
				if(allDirectInput) {
					allDirectInput.readOnly = true;
					allDirectInput.setAttribute('onkeyup', '');
					allDirectInput.value = '';
				}
			}
		}	
	}
	
}

