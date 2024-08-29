//locationModal, Value Add
function locationSave(_this){
	const locationValueSave = _this.closest('.survey-location-box').querySelector('.locationSave');
	new ModalBuilder().init(message.invest.search_location).ajaxBody("/invstmng/location/save").footer(3,message.common.button_confirm,function(button, modal){
		const locationValue = document.querySelector('.surveyLocationValue').value;
		locationValueSave.value = locationValue;
		modal.close();
	}, message.common.button_cancel, function(button, modal){
		locationValueSave.value = "";
		modal.close();		
	}).open();
}


async function rdaLocation(elementId, gnNm = "gnNm",dsdLength = 3, gnLength = 3, pageType="general"){
	if(gnNm == null) gnNm = '';
	let addressOrder = ["provinNm","districtNm","dsdNm", gnNm];
	let createdDivArray = [];
	let provinceLocation;

    await fetch('/common/dstrct/location')
    .then(response => response.json())
    .then(result => {
        locations = JSON.parse(result.data.location); 
        provinceLocation = locations.reduce(function(prev, cur){
            //빈배열(-1)일때 option 반환
       
            if (prev.findIndex(({ provinNm }) => provinNm === cur.provinNm) === -1) {
				
		        let locationObject = {};
		        locationObject.provinNm = cur.provinNm;
		        locationObject.provinCd = cur.provinCd;
			    prev.push(locationObject);
			  }
            //if(prev.indexOf(cur.provinNm) == -1) prev.push(cur)
            return prev;
        }, [])
    })
    .catch(error => console.error('Error:', error));
    
    function selectAppend(){
        if(this.constructor.name === "HTMLSelectElement" && this.dataset.idx !== 0) {
            //parseInt 문자열 -> 숫자
            let startIdx = parseInt(this.dataset.idx);
            for(let i = startIdx+1; i < createdDivArray.length;i++) {
                createdDivArray[i].element.remove();
            }
            //배열자르기
            createdDivArray.splice(startIdx+1, createdDivArray.length-(startIdx+1));
        }

        const selectLength = createdDivArray.length;
        //select length에 맞는 배열 찾기
        const type = addressOrder[selectLength];
        let option = "<option value=''>"+message.common.select+"</option>";
        let filteredLocation;

        //select next Y,N check
        let hasNext = false;
        if(type === "provinNm") {
            filteredLocation = provinceLocation;
            //provinceLocation 갯수만큼 option 생성
            for(const item of provinceLocation){
                option +=  "<option value='"+item.provinNm+"' data-cd='"+item.provinCd+"'>"+item.provinNm+"</option>";
            };
            //selectNext Y
            hasNext = true;
        }else{
            //location json 에 있는 type 과 내가 select한 option 비교 후 
            filteredLocation = locations.filter(obj => obj[this.dataset.type] === this.value);
            const nextLocationNm = filteredLocation.reduce(function(prev,cur){
                //빈배열(-1)일때 option 반환
                if(prev.indexOf(cur[type]) == -1 && cur[type]) {
					
					//code 값 세팅 default district 
	                let cdValue = cur.districtCd;
	                if(type === 'dsdNm') cdValue = setNumberDigit(cur.dsdCd,dsdLength);
	                if(type === 'gnNm') cdValue = setNumberDigit(cur.gnCd,gnLength);
	                
                    prev.push(cur[type]);
                    hasNext = true;
                    option +=  "<option value='"+cur[type]+"' data-cd='"+cdValue+"'>"+cur[type]+"</option>";
                }
                return prev;
            }, [])
        }
        //false 면 종료
        if(!hasNext) return;
        
		let container = document.getElementById(`${elementId}`);
		
		//아이디 없을때 찾기
		if(!container) {
			//console.error(`'${elementId}'`);
			return
		}
		
        //div append
        let div = document.createElement('div');
		div.classList.add('select-div-box');	
        div.dataset.box = selectLength;
        div.dataset.type = `${type}-box`;

        //option Append
        let cr = document.createElement('select');
        cr.innerHTML = option;
        cr.dataset.idx = selectLength;
        cr.dataset.type = type;
        if(cr.dataset.idx != 3){
	        cr.dataset.validation = 'locationSelect';
	        cr.classList.add('select-list-box', 'scroll', 'location-select', 'table-select',`${type}-select`, 'validation-tag')
		} else {
			cr.classList.add('select-list-box', 'scroll', 'location-select', 'table-select',`${type}-select`)
		}
		
		if(pageType.startsWith('statistic') && (type === 'dsdNm' || type === 'gnNm')){
			cr.classList.add(pageType);
            if(pageType.endsWith('mcc')){
                cr.setAttribute('data-search-type','searchRegion');
                cr.setAttribute('data-append-location','searchRoadCd');
            }
		}
        cr.dataset.filteredLocation = filteredLocation;
        createdDivArray.push({element : div, type :  type});

        container.appendChild(div);
        div.appendChild(cr)
        cr.addEventListener("change", selectAppend.bind(cr));
    }
    selectAppend();
}
