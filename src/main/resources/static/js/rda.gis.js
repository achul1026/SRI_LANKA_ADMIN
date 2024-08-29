// 모니터링
let gisSurveySearchData = {};
let gisFacilitiesSearchData = {};
let surveyInterval = null;
let surveyDetailInterval = null;
let surveyDetailExmnmngId = null;
let surveyDetailLng = null;
let surveyDetailLat = null;
let facilitiesInterval = null;
let facilitiesVDSInterval = null;
let facilitiesMetroInterval = null;
let facilitiesMetroMoveInterval = null;
let facilitiesDetailInterval = null;
// GIS통계
let gisStatisticsSearchData = {};

/*대시보드 범례*/
function legendToggle(_this){
	$(_this).next().slideToggle(300);
	$(_this).find('.map-legend-arrow').toggleClass('active');
}

let gisViewStyle = () => {
	document.body.classList.add('dashboard');
	document.getElementById('header').remove();
} 

//차종선택 checkbox event
let checkBoxChange = (_this) => {
	const label = _this.closest('label');
	const allChecked = document.getElementById('allChecked');
	if(_this.checked == true){
		label.classList.add('active')
	} else {
		label.classList.remove('active');
		if(allChecked.checked == true) {
			allChecked.checked = false;
			allChecked.closest('label').classList.remove('active');
		}
	}
}
//차종선택 allcheckbox event
let allCheckBoxChange = (_this) => {
	const checkboxItem = document.querySelectorAll('.transport-checkbox');
	if(_this.checked == true) {
		_this.closest('label').classList.add('active');
		checkboxItem.forEach(checkbox => {
			if(checkbox.checked == false) checkbox.click();
		});
	} else {
		_this.closest('label').classList.remove('active');
		checkboxItem.forEach(checkbox => checkbox.click());			
	}		
}

//검색 취소 및 다른 메뉴 선택시
let facilitiesClose = () => {
	const container = document.querySelectorAll('.dashborad-search-box');
	const selectBox = document.querySelectorAll('.location-event .select-list-box');
	const checkBox = document.querySelectorAll('.transport-box input[type="checkbox"]');
	const checkLabel = document.querySelectorAll('.transport-box label');
	const facilitiesLocation = document.querySelectorAll('#facilitiesLocationSearch .select-div-box');
	const surveyLocation = document.querySelectorAll('#surveyLocationSearch .select-div-box');
	const searchButton = document.querySelectorAll('.dashboard-search-button');
	
	container.forEach(box => box.classList.add('none'));
	for(let i = 1; i < facilitiesLocation.length; i++) facilitiesLocation[i].remove();
	for(let i = 1; i < surveyLocation.length; i++) surveyLocation[i].remove();
	selectBox.forEach(select => select.value = '');
	checkLabel.forEach(label => {
		if(label.classList.contains('active')) label.classList.remove('active');
	})
	checkBox.forEach(checkbox => checkbox.checked = false);
	searchButton.forEach(button => button.classList.remove('active'));
}

let statisticsSerachType = (_this) => {
	const thisEl = _this;
	const thisData = _this.dataset.serach;
	const thisParent = _this.closest('.dashborad-search-box');
	const thisDateType = thisParent.querySelector('.search-date-type');
	thisEl.closest('.facilities-search-item').querySelector('.is-key-button').classList.remove('is-key-button')
	thisEl.classList.add('is-key-button');
	
	if(thisData === 'month') {
		thisParent.querySelectorAll('.serach-time').forEach(div => {
			div.classList.add('none');
			div.querySelectorAll('.input-table-text').forEach(input => input.classList.remove('validation-tag'))
		})
		thisParent.querySelector('.serach-month').classList.remove('none');
		thisParent.querySelectorAll('.serach-month input').forEach(input => input.classList.add('validation-tag'));
		thisParent.querySelectorAll('.serach-time select').forEach(select => select.classList.remove('validation-tag'));
		thisDateType.value = thisData;
	} else if(thisData === 'time') {
		thisParent.querySelector('.serach-month').classList.add('none');
		thisParent.querySelectorAll('.serach-time').forEach(div => {
			div.classList.remove('none');
			div.querySelectorAll('.input-table-text').forEach(input => input.classList.add('validation-tag'))
		})
		thisParent.querySelectorAll('.serach-time select').forEach(select => select.classList.add('validation-tag'));
		thisParent.querySelectorAll('.serach-month input').forEach(input => input.classList.remove('validation-tag'));
		thisDateType.value = thisData;
	}
	
}


let gisChart = (data, dataDefault) => {
    let setData = [];
    let setLabel = [];
    const type = dataDefault.resultType;
    
    data.lineGraphList.forEach(item => {
        setLabel.push(item.dateInfo);
        if (type === 'trfvlm') {
            setData.push(parseInt(item.trfvlm) || 0);
        } else if (type === 'avgspeed') {
            setData.push(parseInt(item.avgSpeed) || 0);
        }
    });
	
    new rdaChart(ChartType.LINE).init("stDsChart")
    .setDataSetArrayLabel(setLabel)
    .setDataSet({
            label:"value",
            data:setData,
            backgroundColor:'#3A7DFF',
            borderRadius:3,
            borderWidth:2,
            borderColor:'#3A7DFF',
            fill: false,
    })
    /*.setTicksStep(200)*/
    .setLabelDisplay(false)
    .setLineXGrid(true)
    .setLineYGridTick('rgba(0, 0, 0, 0.1')
    .setLineXGridTick('rgba(0, 0, 0, 0.1')
    .setLineYGridColor('rgba(0, 0, 0, 0.1')
    .setLineXGridColor('rgba(0, 0, 0, 0.1')
    .setLineXTickAuto(false)
    .draw();
}

let gisSearchHtml = (data) => {
	const setType = data.properties.resultType
	const setLocation = data.properties.location
	const setCoordinates = data.geometry.coordinates
	const setRoadLanes = data.properties.laneCnt;
	
    let html =
    	`
        <div class="mapbox-popup-container">
            <div class="mapbox-popup-header">
                <h4 class="mapbox-popup-title">${setType == 'avgspeed' ? 'The Average Traffic Speed By Hour' : 'Cumulative Traffic By Hour'}</h4>
                <button type="button" class="mapbox-popup-close" onclick="mapboxPopupRemove()">
                    <img src="/images/close_light.png" alt="close"/>
                </button>
            </div>
            <div class="mapbox-popup-wrap">
                <ul class="mapbox-popup-gis-content">
                    <li>Description : <span>${setLocation}</span></li>
                    <li>Coordinates : <span>${setCoordinates}</span></li>
                    <li>Number Of Road Lanes : <span>${setRoadLanes}</span></li>
                </ul>
            `        
        
        return html;
}


let setStatisticsDetail = (detailData, data) => {
	// 상세 데이터
	const setDetailData = detailData;
	// 기본 레이어 데이터
	const setDataDefault = data;
	
	const mapboxWrap = document.querySelector('.mapbox-popup-wrap');
	
	const setDirection = setDetailData.drctCdList[0].vhclDrct;
	const setTotalCnt = setDetailData.totalCnt
	const setType = setDataDefault.resultType
	const setSearchType = setDataDefault.type
	
	const container = document.getElementById('mapbox-popup-append-box');
	const liAppendItem = document.getElementById('directionTypeList');
	if(container) container.remove();
	if(liAppendItem) liAppendItem.remove();
	
	let html =`
			</ul>
				<div id="mapbox-popup-append-box">
                <div class="mapbox-popup-chart-box scroll">
                	<div id="stDsChart" class="mapbox-popup-chart"></div>
            	</div>
                <div id="mapboxGisTable" class="mapbox-popup-table-box scroll">
                	<div class="mapbox-popup-total-box" onclick="mapPopupTableShow(this);">
                		<div>
              `
        	   if(setType == 'avgspeed'){
			   		html +=	`<span>평균 통행 속도</span>
	                		<span class="mapbox-popup-total"><strong id="resultTotal">(${setTotalCnt}km/h)</strong></span>
							`
			   } else if(setType == 'trfvlm'){
			   		html +=	`<span>누적 교통량</span>
	                		<span class="mapbox-popup-total"><strong id="resultTotal">${numberComma(setTotalCnt)}</strong>대</span>
							`
			   }         	
       html +=`        	</div>
                		<button type="button" id="mapbox-popup-arrow"><img src="/images/join_select_arrow.png" src="arrow"/></button>
                	</div>
					<table id="mapPopupTable" class="list-table">
                		<thead>
                			<tr>
                				<th>Rank</th>
                				<th>Type</th>
        	   `
						        if(setType == 'avgspeed') {
									html += `<th>Speed</th>`
								} else if(setType == 'trfvlm') {
									html += `<th>Percentage</th>
											 <th>Total</th>`
								}
		html += `
                			</tr>
                		</thead>
                		<tbody>
                `
                		if(Array.isArray(setDetailData.tableDataList) && setDetailData.tableDataList.length > 0) {
	                		let rank = 1;
	            		    const totalValue = setDetailData.tableDataList.reduce((sum, item) => {
							    const trfvlmValue = item.trfvlm !== "null" && item.trfvlm !== null ? parseInt(item.trfvlm, 10) : 0;
							    return sum + trfvlmValue;
							}, 0);
	
						    setDetailData.tableDataList.forEach(item => {
						        if(setType == 'avgspeed') {
						            html += `
							                <tr>
							                    <td>${rank++}</td>
							                    <td>${item.vhclClsf}</td>
							                    <td><span>${item.avgSpeed}</span>km</td>
							                </tr>
						            		`;
						        } else if(setType == 'trfvlm') {
						            html += `
							                <tr>
							                    <td>${rank++}</td>
							                    <td>${item.vhclClsf}</td>
							                    <td>${item.rate}%</td>
							                    <td>${numberComma(item.trfvlm)}</td>
							                </tr>
								            `;
						        }
						    });
						} else {
					        if(setType == 'avgspeed') {
					            html += `
						                <tr>
						                    <td colspan="3">데이터가 없습니다.</td>
						                </tr>
					            		`;
					        } else if(setType == 'trfvlm') {
					            html += `
						                <tr>
						                    <td colspan="4">데이터가 없습니다.</td>
						                </tr>
					            		`;
					        }
						}
		html += `		
                		</tbody>
                	</table>
            	</div>
            	</div>
        	</div>
        </div>`
        
        mapboxWrap.insertAdjacentHTML('beforeend', html)
        const mapboxContent = document.querySelector('.mapbox-popup-gis-content');
        
        let directionHtml = '';
		if(setSearchType === 'vds') {
			directionHtml +=	`<li>Direction : <span>${setDirection}</span></li>`
		} else {
			directionHtml +=	`<li id="directionTypeList">Direction : 
									<select id="directionType" class="select-list-box table-select">
										<option value="">전체</option>
								`
			setDetailData.drctCdList.forEach(item => {
				directionHtml +=`		<option value="${item.vhclDrctCd}">${item.vhclDrct}</option>`
			})
		    directionHtml +=	`	</select>
					 			 </li>`;
		}
		 
		mapboxContent.insertAdjacentHTML('beforeend', directionHtml)
		gisChart(setDetailData, setDataDefault)    
		const directionChangeEl = document.getElementById('directionType');
		if(directionChangeEl) {
			const vhclDrctCd = setDataDefault.searchVhclDrctCd;
			directionChangeEl.setAttribute('onchange', `setDirectionChange(this, "${setSearchType}");`);
			if(vhclDrctCd) {
				directionChangeEl.querySelectorAll('option').forEach(option => {
					if(option.value == vhclDrctCd) {
						option.selected = true;
					}
				})
			}
		}
}


let mapPopupTableShow = (_this) => {
	$(_this).parent('#mapboxGisTable').toggleClass('active');
    $('#mapPopupTable').stop().slideToggle(300);	
}

/* requset interval */
class setTimeInterval {
	constructor() {
        this.lastExecutionTime = Date.now();
        this.requestAnimationFrameId = null;
	}
	
	start = (eventFunction, exmnmngId, lng, lat, multi = 'defalut') => {
        const checkTime = () => {
            const currentTime = Date.now();
            const elapsedTime = currentTime - this.lastExecutionTime;

			// 1분
            if (elapsedTime >= 100000) {
				if(multi === 'defalut') {
	                eventFunction(exmnmngId, lng, lat);
				} else if(multi === 'manySurvey') {
					eventFunction(exmnmngId, lng, lat, multi);
				}
                this.lastExecutionTime = currentTime;
            }

            this.requestAnimationFrameId = requestAnimationFrame(checkTime);
        };
        this.requestAnimationFrameId = requestAnimationFrame(checkTime);
	};
	
	facilityStart = (eventTarget) => {
        const checkTime = () => {
            const currentTime = Date.now();
            const elapsedTime = currentTime - this.lastExecutionTime;

			// 1분
            if (elapsedTime >= 100000) {
                setFacilityDetailInterval(eventTarget);
                this.lastExecutionTime = currentTime;
            }

            this.requestAnimationFrameId = requestAnimationFrame(checkTime);
        };
        this.requestAnimationFrameId = requestAnimationFrame(checkTime);		
	};
	
	clear = (layerName) => {
		if(map.getLayer(layerName)) {
			map.removeLayer(layerName);
			map.removeSource(layerName);
		}
		cancelAnimationFrame(this.requestAnimationFrameId);
	};
}

// 시설물 위치 시설물 유형 select
let setFacilitiesSelectType = (_this) => {
	const optionValue = _this.querySelector('option:checked').value
	const button = document.getElementById('facilitiesSearchBtn');
	
	if(optionValue === ''){
		button.setAttribute('onclick', "setFacilitySearch('ONLY', false);");	
	} else if(optionValue === 'VDS') {
		button.setAttribute('onclick', "setFacilitySearch('VDS', false);");
	} else if(optionValue === 'METRO') {
		button.setAttribute('onclick', "setFacilitySearch('METRO', false);");
	} else if(optionValue === 'METRO-MOVE') {
		button.setAttribute('onclick', "setFacilitySearch('METRO-MOVE', false);");
	}
}

let setLegendType = (legendType, type, typeMulti = false) => {
	const facilitiesBox = document.querySelector('#facilitiesLegendBox');
	const surveyBox = document.querySelector('#surveyLegendBox'); 
	const legendList = document.querySelectorAll('.map-legend-list > li > ul > li');
	const legendEl = document.querySelector(`[data-legend-type="${type}"]`);
	const legendMultiType = document.querySelector('[data-legend-type="multi"]');
	
	legendList.forEach(li => li.classList.add('none'));
	if(legendType == 'facilities'){
		if(type === 'ONLY') {
			facilitiesBox.querySelectorAll('li').forEach(li => li.classList.remove('none'))
		} else {
			legendEl.classList.remove('none');
		}
	}
	
	if(legendType == 'survey') {
		if(type === 'ONLY') {
			surveyBox.querySelectorAll('li').forEach(li => li.classList.remove('none'))
		} else {
			if(typeMulti === true) {
				legendMultiType.classList.remove('none');
				legendEl.classList.remove('none');
			}
		}
	}
}

let setStatisticsLegendType = (type) => {
	const legendBox = document.getElementById('facilitiesLegendBox');
	const trfvlmBox = document.getElementById('trfvlmLegendBox');
	const avgspeedBox = document.getElementById('avgspeedLegendBox');
	const trafficLegendHtml = `
								<ul id="trfvlmLegendBox">
									<li>
										<span class="legendClusters legendColor1000"></span>
										<span class="map-legend-list-name">1000미만</span>
									</li>
									<li>
										<span class="legendClusters legendColor5000"></span>
										<span class="map-legend-list-name">1001 ~ 5000</span>
									</li>
									<li>
										<span class="legendClusters legendColorMax"></span>
										<span class="map-legend-list-name">5001이상</span>
									</li>
								</ul>
							  `;
	const speedLegendHtml = `
							<ul id="avgspeedLegendBox">
								<li>
									<span class="legendClusters legendColor1000"></span>
									<span class="map-legend-list-name">50km 미만</span>
								</li>
								<li>
									<span class="legendClusters legendColor5000"></span>
									<span class="map-legend-list-name">51 ~ 100km</span>
								</li>
								<li>
									<span class="legendClusters legendColorMax"></span>
									<span class="map-legend-list-name">101km 이상</span>
								</li>
							</ul>
							`
	if(type === 'trfvlm') {
		if(avgspeedBox) avgspeedBox.remove();
		legendBox.insertAdjacentHTML('beforeend', trafficLegendHtml)
	} else if(type === 'avgspeed') {
		if(trfvlmBox) trfvlmBox.remove();
		legendBox.insertAdjacentHTML('beforeend', speedLegendHtml)
	}						 
}

