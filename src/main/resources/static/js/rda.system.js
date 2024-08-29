let checkedRegionContentArray = [];
let checkedRegionDisplayCodeArray = [];
let regionCodeDefaultArray = [];

//타즈리스트에서 select 리스트로
let tazSelectAddTr = (_this, type='') => {
	const tbody = document.getElementById('modalSelectTbody');
	const notDataTr = tbody.querySelector('.regionSelectNotdata');
	const checkbox = _this;
	const parent = checkbox.closest('tr');
	const regionCode = parent.querySelector('.region-list-code').value;
	const displayCode = parent.querySelector('.region-code').value;
	let regionContent = [];
	let appendHtml = '';
	if(type == 'TAZ'){
		const regionLat = _this.dataset.lat;
		const regionLon = _this.dataset.lon;
		regionContent = [regionLat,regionLon];
		appendHtml = `
						<td>
					 		${regionContent[0]}
					 		<input type="hidden" class="select-list-name" value="${regionContent}"/>
					 	</td>
						<td>
					 		${regionContent[1]}
					 		<input type="hidden" class="select-list-name" value="${regionContent}"/>
					 	</td>
					 	`;
	} else {
		regionContent = [parent.querySelector('.region-list-name').value];
		appendHtml = `<td>
				 		${regionContent}
				 		<input type="hidden" class="select-list-name" value="${regionContent}"/>
				 	</td>`;
	}
	
	const html = `
				 <tr class="region-select-tr" data-rc="${regionCode}" data-dc="${displayCode}">
				 	<td>
				 		${displayCode}
				 		<input type="hidden" class="select-list-code" value="${regionCode}"/>
				 	</td>
				 	`
				 	+ appendHtml
				 	+`<td>
				 		<img src="/images/delete_img.png" class="cm-csimg" onclick="tazSelectRemoveTr(this, 'input')">
				 	</td>
				 </tr>
				 `
	tbody.insertAdjacentHTML('beforeend', html);
	checkedRegionContentArray.push([regionCode, regionContent, '']);
	checkedRegionDisplayCodeArray.push(displayCode);
	notDataTr.classList.add('none');
}

//리스트 삭제
let tazSelectRemoveTr = (_this, inputButton = 'not') => {
	let regionCode;
	const tbody = document.getElementById('modalSelectTbody');
	const checkbox = _this;
	const parent = checkbox.closest('tr');
	
	//tazList 에서 체크된걸 다시 눌렀을때 없애는거
	if(inputButton == 'not') {
		regionCode = parent.querySelector('.region-list-code').value;
		displayCode = parent.querySelector('.region-code').value;
		const tr = tbody.querySelector(`[data-rc="${regionCode}"`);
		checkedRegionContentArray = checkedRegionContentArray.filter((code) => code[0] !== regionCode);
		checkedRegionDisplayCodeArray = checkedRegionDisplayCodeArray.filter((dstrct) => dstrct !== displayCode);
		if(tr) tr.remove();
		
	//selectList 에서 삭제 버튼 눌렀을때	
	} else if(inputButton == 'input') {
		regionCode = parent.dataset.rc;
		displayCode = parent.dataset.dc;
		const tazListCode = document.querySelector(`[data-region-code="${regionCode}"]`);
		checkedRegionContentArray = checkedRegionContentArray.filter((taz) => taz[0] !== regionCode);
		checkedRegionDisplayCodeArray = checkedRegionDisplayCodeArray.filter((dstrct) => dstrct !== displayCode);
		if(tazListCode) tazListCode.checked = false;
		parent.remove();
	}
	
	tazNotSelect(tbody);
}

//tazList 페이지네이션, 등록모달버튼누를때
let regionPagingAdd = (type = '') => {
	let html = '';
	const tbody = document.getElementById('modalSelectTbody');
	regionCodeDefaultArray = checkedRegionContentArray.map(innerArray => innerArray.slice());
	for(let i = 0; i < checkedRegionContentArray.length; i++) {
		const tazCheckBox = document.querySelector(`[data-region-code="${checkedRegionContentArray[i][0]}"`);			 
		let appendHtml = `<td>
					 		${checkedRegionContentArray[i][1]}
					 		<input type="hidden" class="select-list-name" value="${checkedRegionContentArray[i][1]}"/>
					 		<input type="hidden" class="select-list-cnt" value="${checkedRegionContentArray[i][2]}"/>
					 	</td>`;
		if(type == 'TAZ'){
			appendHtml = `
						<td>
					 		${checkedRegionContentArray[i][1][0]}
					 		<input type="hidden" class="select-list-name" value="${checkedRegionContentArray[i][1]}"/>
					 		<input type="hidden" class="select-list-cnt" value="${checkedRegionContentArray[i][2]}"/>
					 	</td>
					 	<td>
					 		${checkedRegionContentArray[i][1][1]}
					 		<input type="hidden" class="select-list-name" value="${checkedRegionContentArray[i][1]}"/>
					 		<input type="hidden" class="select-list-cnt" value="${checkedRegionContentArray[i][2]}"/>
					 	</td>
					 	`;
		}
		html = `
				 <tr data-rc="${checkedRegionContentArray[i][0]}" data-dc="${checkedRegionDisplayCodeArray[i]}" class="region-select-tr">
				 	<td>
				 		${checkedRegionDisplayCodeArray[i]}
				 		<input type="hidden" class="select-list-code" value="${checkedRegionContentArray[i][0]}"/>
				 	</td>
			 	`
			 	+ appendHtml
			 	+ `
				 	<td>
				 	<img src="/images/delete_img.png" class="cm-csimg" onclick="tazSelectRemoveTr(this, 'input')">
				 	
				 </tr>
			   `
		tbody.insertAdjacentHTML('beforeend', html);
		
		//페이징때 선택한 타즈코드가 있으면 true
		if(tazCheckBox) tazCheckBox.checked = true;
	}
	
	tazNotSelect(tbody);
}

let tazModalSave = (type='') => {
	const addBox = document.getElementById('regionSelectAddBox');
	addBox.querySelectorAll('tr').forEach(tr => {
		if(!tr.classList.contains('regionListNotData')){
			tr.remove()
		} else {
			tr.classList.add('none');
		}
	})
	for(let i = 0; i < checkedRegionContentArray.length; i++) {
		let appendHtml = '';
		if(type == 'TAZ'){
			appendHtml = `
							<td>
						 		${checkedRegionContentArray[i][1][0]}
						 		<input type="hidden" value="${checkedRegionContentArray[i][1]}"/>
						 	</td>
					 		<td>
						 		${checkedRegionContentArray[i][1][1]}
						 		<input type="hidden" value="${checkedRegionContentArray[i][1]}"/>
						 	</td>
						`;
		} else {
			appendHtml = `
							<td>
						 		${checkedRegionContentArray[i][1]}
						 		<input type="hidden" value="${checkedRegionContentArray[i][1]}"/>
						 	</td>
					 	`;
		}
		html = `
				 <tr data-rc="${checkedRegionContentArray[i][0]}" data-dc="${checkedRegionDisplayCodeArray[i]}" class="region-select-tr">
				 	<td>
				 		${checkedRegionDisplayCodeArray[i]}
				 		<input type="hidden" value="${checkedRegionContentArray[i][0]}"/>
				 	</td>
				 	`
				 	+ appendHtml			 	
				 	+`
				 	<td>
				 		<input type="hidden" class="taz-district-code" value="${checkedRegionDisplayCodeArray[i]}"/>
					 	<input type="text" class="input-table-text region-code-percentage" value="${checkedRegionContentArray[i][2]}" placeholder="${message.region.system_js_enter}"/>
				 	</td>
				 	<td><input type="button" class="taz-button-reset" value="${message.common.button_delete}" onclick="tazListRemove(this)"/></td>
				 </tr>
			   `
		addBox.insertAdjacentHTML('beforeend', html);
	}	 
}

let tazNotSelect = (parent) => {
	const notDataTr = parent.querySelector('.regionSelectNotdata');
	const updateTr = parent.querySelector('.region-select-tr');
	if(updateTr) {
		notDataTr.classList.add('none');
	} else {
		notDataTr.classList.remove('none');
	}
}

let tazListRemove = (_this) => {
	const parent = _this.closest('tr');
	const tbody = _this.closest('#regionSelectAddBox');
	const notData = tbody.querySelector('.regionListNotData');
	const regionCode = parent.dataset.tc;
	const displayCode = parent.dataset.dc;
	checkedRegionContentArray = checkedRegionContentArray.filter((taz) => taz[0] !== regionCode);
	checkedRegionDisplayCodeArray = checkedRegionDisplayCodeArray.filter((dstrct) => dstrct !== displayCode);
	parent.remove();
	
	const tr = tbody.querySelectorAll('.region-select-tr').length;
	if(tr == 0) notData.classList.remove('none');
}
