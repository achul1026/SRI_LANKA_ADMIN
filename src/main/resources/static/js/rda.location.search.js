//locationModal, Value Add
function locationSave(_this){
	const locationValueSave = _this.closest('.survey-location-box').querySelector('.locationSave');
	new ModalBuilder().init('주소 찾기').ajaxBody("/invstmng/location/save").footer(3,'확인',function(button, modal){
		const locationValue = document.querySelector('.surveyLocationValue').value;
		locationValueSave.value = locationValue;
		modal.close();
	}, '취소', function(button, modal){
		locationValueSave.value = "";
		modal.close();		
	}).open();
}


//locationList
function locationSearch(selectContainerClassName){
	let selectContainer = document.querySelector('.'+selectContainerClassName);
	let locationInputSave = document.querySelector('.surveyLocationValue');
	let selectTwoElement = null;
	let selectThreeElement = null;
	let locations;
	
	const selectOneElementBox = document.createElement('select');
	selectContainer.appendChild(selectOneElementBox);
	selectOneElementBox.classList.add('startLocation', 'scroll', 'select-list-box');
	
	fetch('/js/location.json') 
	.then(response => response.json()) 
	.then(data => {
		locations = data.location; 
		const selectOneElement = document.querySelector(`.${selectContainerClassName} .startLocation`);
		const optionDefault = document.createElement('option');
		optionDefaul(selectOneElement, optionDefault);
		
		locations.forEach(obj => {
			const provinceName = obj.ProvinceName;
			if(!isDuplicateOption(selectOneElement, provinceName)) {
				selectOption(selectOneElement, 'province-name', provinceName);				
			}
		})
		
		//두번째 주소 Add
		selectOneElement.addEventListener('change', () => {
			selectTwoElement?.remove();
			selectThreeElement?.remove();
			locationInputSave.value = "";
			
			if(!isNull($(selectOneElement).val())){
				let provinceNameValue = selectOneElement.value;
				selectTwoElement = document.createElement('select');
				selectTwoElement.classList.add('select-list-box', 'scroll');
				
				const optionDefault = document.createElement('option');
				optionDefaul(selectTwoElement, optionDefault);			
				
				locations.forEach(obj => {
					const provinceName = obj.ProvinceName;
					const districtName = obj.DistrictName;
					
					if(provinceNameValue === provinceName){
						if(!isDuplicateOption(selectTwoElement, districtName)) {
							selectTwoElement.classList.add('twoLocation');
							selectContainer.appendChild(selectTwoElement);
							selectOption(selectTwoElement, 'district-name', districtName);
						}
					}
				})
				
				locationInputSave.value = "";
				locationInputSave.value = provinceNameValue;
				
				//세번째 주소add
				let selectDrstrict = document.querySelector(`.${selectContainerClassName} .twoLocation`);
				selectDrstrict.addEventListener('change', () => {
					selectThreeElement?.remove();
					locationInputSave.value = provinceNameValue;
					if(!isNull($(selectDrstrict).val())){
						let districtNameValue = selectDrstrict.value;
						selectThreeElement = document.createElement('select');
						selectContainer.appendChild(selectThreeElement);
						selectThreeElement.classList.add('select-list-box', 'scroll');
						
						const optionDefault = document.createElement('option');
						optionDefaul(selectThreeElement, optionDefault);		
						
						locations.forEach(obj => {
							const districtName = obj.DistrictName;		
							const dsdName = obj.DsdName;
								
							if(districtNameValue === districtName){
								if(!isDuplicateOption(selectThreeElement, dsdName)) {
									selectThreeElement.classList.add('threeLocation');
									selectOption(selectThreeElement, 'dsd-name', dsdName);
								}
							}
						})
						
						locationInputSave.value = "";
						locationInputSave.value = provinceNameValue+','+districtNameValue;
						
						//input text에 주소 add
						let selectDsd = document.querySelector(`.${selectContainerClassName} .threeLocation`);
						selectDsd.addEventListener('change', () => {
							const DsdNameValue =  selectDsd.value;
							locationInputSave.value = "";
							locationInputSave.value = provinceNameValue+', '+ districtNameValue+', '+ DsdNameValue;
						})
					}
				})
			}
		})
	})
	.catch(error => console.error('Error:', error));
}

//중복 주소 제거
function isDuplicateOption(select, value) {
	for (let i = 0; i < select.options.length; i++) {
		if (select.options[i].value === value) {
	  		return true;
		}
	}
	return false;
}

//option 기본
function optionDefaul(selectElement, optionElement) {
	selectElement.appendChild(optionElement);
	optionElement.textContent = '선택';
	optionElement.setAttribute('disabled', '');
}

//select opition add
function selectOption(optionParentElement, optionClassName, locationItem){
	const option = document.createElement('option');
	option.classList.add(optionClassName);
	option.value = locationItem;
	option.textContent = locationItem;
	optionParentElement.appendChild(option);
}