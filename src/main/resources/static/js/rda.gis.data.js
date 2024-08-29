let vdsDataYn = '';
let fixedDataYn = '';
let potableDataYn = '';

// 시설물 위치 search event
function setFacilitySearch(eventType = 'ONLY', mapLayer = true){
	const form = document.getElementById('facilitiesSearchForm');
    const selectLocation = form.querySelectorAll('.location-select');
    let tazLc = '';
    
    vdsDataYn = true;
	fixedDataYn = true;
	potableDataYn = true;
    
    if(mapLayer === false) allIntervalReset();
	
    selectLocation.forEach(select => {
		const option = select.querySelector('option:checked');
		if(!isNull(option.dataset.cd)) tazLc += option.dataset.cd;
	});
	
	gisFacilitiesSearchData = {
		searchDsdCd : tazLc,
	};
    
    if(facilitiesVDSInterval != null) {
		facilitiesVDSInterval.clear('rdaFacilitiesVDS');
		facilitiesVDSInterval = null;
	}
	if(facilitiesMetroInterval != null) {
		facilitiesMetroInterval.clear('rdaFacilitiesMetro');
		facilitiesMetroInterval = null;
	}
	if(facilitiesMetroMoveInterval != null) {
		facilitiesMetroMoveInterval.clear('rdaFacilitiesMetroMove');
		facilitiesMetroMoveInterval = null;
	}
    
    if(eventType === 'ONLY') {
		setFacilitiesVDSInterval(setFacilitiesVDS);
		setFacilitiesMetroInterval(setFacilitiesMetro);
		setFacilitiesMetroMoveInterval(setFacilitiesMetroMove);
	} else if(eventType === 'VDS') {
		setFacilitiesVDSInterval(setFacilitiesVDS);
		vdsDataYn = eventType;
	} else if(eventType === 'METRO') {
		setFacilitiesMetroInterval(setFacilitiesMetro);
		fixedDataYn = eventType;
	} else if(eventType === 'METRO-MOVE') {
		setFacilitiesMetroMoveInterval(setFacilitiesMetroMove);
		potableDataYn = eventType;
	}
	
	setLegendType('facilities', eventType);
}
let clickEventHandler = null;
let MapEvents = {
	getFacilityDetail : function(e) {
		const properties = e.features[0].properties;
	    if(surveyDetailInterval != null) {
			surveyDetailInterval.clear();
			surveyDetailInterval = null;
		}
		if(facilitiesDetailInterval != null) {
			facilitiesDetailInterval.clear();
			facilitiesDetailInterval = null;
		}
		
	    setFacilityDetailInterval(properties);
	    facilitiesDetailInterval = new setTimeInterval()
	    facilitiesDetailInterval.facilityStart(properties);
	},
	getSurveyDetail : function (e) {
        mapboxPopupRemove();
        const arrowRight = `<img src="/images/bread_arrow.png" alt="arrow">`;
        if(facilitiesDetailInterval != null) {
			facilitiesDetailInterval.clear();
			facilitiesDetailInterval = null;
		}
        
        const properties = e.features[0].properties;
        if (properties.isDuplicate) {
			const multipleSurvey = savedSurveyLocationList.filter(multi => multi.lon === properties.lon && multi.lat === properties.lat);
			properties.exmnLc = properties.exmnLc.replaceAll(">", arrowRight);
            let _HTML = `
                <div class="mapbox-popup-container">
                    <div class="mapbox-popup-header">
                        <h4 class="mapbox-popup-title">${properties.exmnLc}</h4>
                        <button type="button" class="mapbox-popup-close" onclick="mapboxPopupRemove('button')">
                            <img src="/images/close_light.png" alt="close"/>
                        </button>
                    </div>
                    <div class="mapbox-popup-wrap">
                    	 <h4 class="mapbox-over-title">${message.main.gis_data_js_survey_data}<span id="overTotalList">${properties.count}</span></h4>
                        <div id="overlapSearch" class="scroll">
                        	${multipleSurvey.map(multi => `
                              <input type="button" value="${multi.exmnNm}" onclick="setSuveyDetailShow('${multi.exmnmngId}', '${multi.lon}', '${multi.lat}', 'manySurvey', 'start')"/>
                            `).join('')}
                        </div>
                    </div>
                </div>`;
                
			if(surveyDetailInterval != null) {
				surveyDetailInterval.clear();
				surveyDetailInterval = null;
			}
			
            new mapboxgl.Popup({closeOnClick: false})
                .setLngLat(e.lngLat)
                .setHTML(_HTML)
                .addTo(map);
                
        } else {
            setSuveyDetailShow(properties.exmnmngId, properties.lon, properties.lat, 'defalut', 'start');
        }
	}
}

let setFacilitiesDrowPoint = (data, layer) => {
	const geojsonData = {
        type: 'FeatureCollection',
        features: data.map((info, index) => ({
            type: 'Feature',
            properties: {
                ...info,
                id: index,
                icon: info.icon
            },
            geometry: {
                type: 'Point',
                coordinates: [info.lon, info.lat]
            }
        }))
    };
    
    const imagesToLoad = [
        {name: 'vds-icon', url: RDA_ENV.MARKERS[0].vds},
        {name: 'metro-icon', url: RDA_ENV.MARKERS[0].metro},
        {name: 'metro-move-icon', url: RDA_ENV.MARKERS[0].metroMove},
        {name: 'vds-danger-icon', url: RDA_ENV.MARKERS[0].vdsDanger},
        {name: 'metro-danger-icon', url: RDA_ENV.MARKERS[0].metroDanger},
        {name: 'metro-move-danger-icon', url: RDA_ENV.MARKERS[0].metroMoveDanger},
    ];
    
    let imagesLoaded = 0;

    imagesToLoad.forEach(image => {
        map.loadImage(image.url, (error, res) => {
            if (error) throw error;
            if (!map.hasImage(image.name)) {
                map.addImage(image.name, res);
            }
            imagesLoaded++;
            if (imagesLoaded === imagesToLoad.length) {
                addLayerWithImage(geojsonData, layer);
            }
        });
    });
}

let addLayerWithImage = (geojsonData, layer) => {
    if (map.getLayer(layer)) {
        map.getSource(layer).setData(geojsonData);
    } else {
        map.addSource(layer, {
            type: 'geojson',
            data: geojsonData
        });
        
        map.addLayer({
            id: layer,
            type: 'symbol',
            source: layer,
            layout: {
                'icon-image': ['get', 'icon'],
                'icon-size': 0.6,
                'icon-allow-overlap': true,
            },
        });

        map.off('click', layer, MapEvents.getFacilityDetail);
        map.on('click', layer, MapEvents.getFacilityDetail);
    }	
} 


// 시설물 위치 > VDS 
let setFacilitiesVDS = () => {
	const loading = new setLoading().start();
    const queryString = new URLSearchParams(gisFacilitiesSearchData).toString();
	fetch(__contextPath__ + "/main/facilities/vds?"+queryString)
		.then((response) => response.json())
		.then((result) => {
			const facilitiesData = result.data;
			if(facilitiesData != null) {
				setFacilitiesDrowPoint(facilitiesData, 'rdaFacilitiesVDS')
			} else {
			    if(facilitiesVDSInterval != null) {
					facilitiesVDSInterval.clear('rdaFacilitiesVDS');
					facilitiesVDSInterval = null;
				}
			    if(vdsDataYn === 'VDS') {
					new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4, message.common.button_confirm, function(button, modal) {
						modal.close();
					}).open();
				} else {
					vdsDataYn = false;
					setFacilitiesDataCheck();
				}
			}
		})
		.finally(() => {
			loading.end();
		});	
}

// 시설물 위치 > Metro
let setFacilitiesMetro = () => {
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(gisFacilitiesSearchData).toString();
	fetch(__contextPath__ + "/main/facilities/fixed/metrocount?"+queryString)
		.then((response) => response.json())
		.then((result) => {
			const facilitiesData = result.data;
			if(facilitiesData != null) {
				setFacilitiesDrowPoint(facilitiesData, 'rdaFacilitiesMetro')			
			} else {
				if(facilitiesMetroInterval != null) {
					facilitiesMetroInterval.clear('rdaFacilitiesMetro');
					facilitiesMetroInterval = null;
				}
				if(fixedDataYn === 'METRO') {
					new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4, message.common.button_confirm, function(button, modal) {
						modal.close();
					}).open();
				} else {
					fixedDataYn = false;
					setFacilitiesDataCheck();
				}
			}
		})	
		.finally(() => {
			loading.end();
		});	
}

// 시설물 위치 > mertroMove
let setFacilitiesMetroMove = () => {
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(gisFacilitiesSearchData).toString();
	fetch(__contextPath__ + "/main/facilities/move/metrocount?"+queryString)
		.then((response) => response.json())
		.then((result) => {
			const facilitiesData = result.data;
			if(facilitiesData != null) {
				setFacilitiesDrowPoint(facilitiesData, 'rdaFacilitiesMetroMove')
			} else {
				if(facilitiesMetroMoveInterval != null) {
					facilitiesMetroMoveInterval.clear('rdaFacilitiesMetroMove');
					facilitiesMetroMoveInterval = null;					
				}
				if(potableDataYn === 'METRO-MOVE') {
					new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4, message.common.button_confirm, function(button, modal) {
						modal.close();
					}).open();					
				} else {
					potableDataYn = false;
					setFacilitiesDataCheck();
				}
			}
		})	
		.finally(() => {
			loading.end();
		});	
}

let setFacilitiesDataCheck = () => {
	if(vdsDataYn === false && fixedDataYn === false && potableDataYn === false) {
		new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4, message.common.button_confirm, function(button, modal) {
			modal.close();
		}).open();
	}
}

// 조사위치
let savedSurveyLocationList = null;
function setSurveySearch(searchYn = 'N'){
    const form = document.getElementById('surveySearchForm');
    const selectLocation = form.querySelectorAll('.location-select');
    const selectExmnTypeCd = form.querySelector('#gisSurveySearchType option:checked').value;
    const selectMngrBffltd = form.querySelector('#gisSurveySearchAgency option:checked').value;
    let tazLc = '';
    
    selectLocation.forEach(select => {
		const option = select.querySelector('option:checked');
		if(!isNull(option.dataset.cd)) tazLc += option.dataset.cd;
	});
	if(searchYn === 'Y') {
		gisSurveySearchData = {
			searchDsdCd : tazLc,
			searchExmnTypeCd : selectExmnTypeCd,
			searchMngrBffltd : selectMngrBffltd,
		};
		if(selectExmnTypeCd == '') {
			setLegendType('survey', 'ONLY', true)
		} else {
			setLegendType('survey', gisSurveySearchData.searchExmnTypeCd, true)
		}
	} else {
		setLegendType('survey', 'ONLY', true)
	}
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(gisSurveySearchData).toString();
	fetch(__contextPath__ + "/main/survey?"+queryString)
		.then((response) => response.json())
		.then((result) => {
			const surveyLocationList = savedSurveyLocationList = result.data;
			if (surveyLocationList != null) {
				geojsonData = {
					type: 'FeatureCollection',
					features: surveyLocationList.map((info, index) => ({
						type: 'Feature',
						properties: {
							...info,
							id: index,
							icon: info.exmnType,
							isDuplicate: false,
							count: 1
						},
						geometry: {
							type: 'Point',
							coordinates: [info.lon, info.lat]
						}
					}))
				};
			} else {
				if(surveyInterval != null) {
					surveyInterval.clear('rdaSurveyLoc');
					surveyInterval = null;
				}
				new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4, message.common.button_confirm, function(button, modal) {
					modal.close();
				}).open();
				return
			}
		
		    geojsonData.features.forEach((feature, index, features) => {
		        for (let i = index + 1; i < features.length; i++) {
		            if (feature.geometry.coordinates[0] === features[i].geometry.coordinates[0] &&
		                feature.geometry.coordinates[1] === features[i].geometry.coordinates[1]) {
		                feature.properties.isDuplicate = true;
		                features[i].properties.isDuplicate = true;
		                feature.properties.count++;
		                features.splice(i, 1);
		                i--;
		            }
		        }
		    });
		
		    const imagesToLoad = [
		        {name: 'household-icon', url: RDA_ENV.MARKERS[0].household},
		        {name: 'roadside-icon', url: RDA_ENV.MARKERS[0].roadside},
		        {name: 'mcc-icon', url: RDA_ENV.MARKERS[0].mcc},
		        {name: 'tm-icon', url: RDA_ENV.MARKERS[0].tm},
		        {name: 'multi-icon', url: RDA_ENV.MARKERS[0].surveyMulti},
		    ];
		
		    imagesToLoad.forEach(image => {
		        map.loadImage(image.url, (error, res) => {
		            if (error) throw error;
		            if (!map.hasImage(image.name)) {
		                map.addImage(image.name, res);
		            }
		        });
		    });
		    
		    if (map.getLayer('rdaSurveyLoc')) {
		        map.getSource('rdaSurveyLoc').setData(geojsonData);
		    } else {
			    map.addSource('rdaSurveyLoc', {
			        type: 'geojson',
			        data: geojsonData
			    });
			
			    map.addLayer({
			        id: 'rdaSurveyLoc',
			        type: 'symbol',
			        source: 'rdaSurveyLoc',
			        layout: {
                        'icon-image': [
				            'case',
				            ['==', ['get', 'isDuplicate'], true],
				            'multi-icon',
				            ['match', ['get', 'icon'],
			                	'ETC001', 'mcc-icon',
			                	'ETC002', 'tm-icon',
			                    'ETC003', 'roadside-icon',
			                    'ETC004', 'household-icon',
			                    'default-icon'
				            ]
				        ],
						'icon-size': 0.6,
			            'icon-allow-overlap': true,
			        },
			    });
			    map.off('click', 'rdaSurveyLoc', MapEvents.getSurveyDetail);
			    map.on('click', 'rdaSurveyLoc', MapEvents.getSurveyDetail);			
			}
		})
		.finally(() => {
			loading.end();
		});	
}

// 조사위치 > 상세
function setSuveyDetailShow(exmnmngId, lng, lat, multi = 'defalut', setStart) {
	surveyDetailExmnmngId = exmnmngId;
    surveyDetailLng = lng;
    surveyDetailLat = lat;
    
	
	if(setStart === 'start') {
		if(surveyDetailInterval != null) {
			surveyDetailInterval.clear();
			surveyDetailInterval = null;
		}
		if(multi === 'defalut') {
			setSurveyDetailInterval(setSuveyDetailShow, surveyDetailExmnmngId, surveyDetailLng, surveyDetailLat);
		} else if(multi === 'manySurvey') {
			const mapboxPopup = document.querySelector('.mapboxgl-popup');
			copyHTML = document.createElement('div');
			if(mapboxPopup) copyHTML.appendChild(mapboxPopup.cloneNode(true));
			setSurveyDetailInterval(setSuveyDetailShow, surveyDetailExmnmngId, surveyDetailLng, surveyDetailLat, 'manySurvey');
		}
	}
	
	fetch(__contextPath__ + "/main/survey/"+exmnmngId)
		.then((response) => response.json())
		.then((result) => {
			const info = result.data;
			const arrowRight = `<img src="/images/bread_arrow.png" alt="arrow">`;
			let sttsCdNm = message.main.gis_data_js_survey_status_progressing;
			if(info.sttsCd === 'INVEST_PROGRESS'){
				sttsCdNm = message.main.gis_data_js_survey_status_not_yet_progress;
			}else if(info.sttsCd === 'INVEST_COMPLETE'){
				sttsCdNm = message.main.gis_data_js_survey_status_progress_complete;
			}
			
			const startDt = info.startDt.split('T')[0];
			const endDt = info.endDt.split('T')[0];
			info.exmnLc = info.exmnLc.replaceAll(">", arrowRight);
			
			let _HTML = `
				        <div class="mapbox-popup-container">
				            <div class="mapbox-popup-header">
				                <h4 class="mapbox-popup-title">`
				                if(multi === 'manySurvey') { 
						_HTML +=    `<button type="button" class="mapbox-popup-prev" onclick="mapboxPopupPrev();"><img src="/images/prev_arrow.png" alt="prev"/></button>`			
								}
				        _HTML +=    `<span>${info.exmnNm}</span>
				                	<span>Real-time Status</span>
				                </h4>
				                <button type="button" class="mapbox-popup-close" onclick="mapboxPopupRemove('button')">
				                    <img src="/images/close_light.png" alt="close"/>
				                </button>
				            </div>
				            <div class="mapbox-popup-wrap">
				            	<div id="mapBoxPopDetailWrap">
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_region}</dt>
										<dd class="mapbox-popup-info-con">${info.exmnLc}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_type}</dt>
										<dd class="mapbox-popup-info-con">${info.exmnTypeNm}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_name}</dt>
										<dd class="mapbox-popup-info-con">${info.exmnNm}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_manager}</dt>
										<dd class="mapbox-popup-info-con">${info.userNm}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_period}</dt>
										<dd class="mapbox-popup-info-con">${startDt} ~ ${endDt}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_status}</dt>
										<dd class="mapbox-popup-info-con">${sttsCdNm}</dd>
									</dl>
									<dl class="mapbox-pop-detail-info">
										<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_personel}</dt>
										<dd class="mapbox-popup-info-con">${numberComma(info.exmnNop)}</dd>
									</dl>
									`
									if(info.exmnType === 'OD' || info.exmnType === 'ROADSIDE') {
					_HTML +=		`	<dl class="mapbox-pop-detail-info">
											<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_target_number}</dt>
											<dd class="mapbox-popup-info-con">${numberComma(info.goalCnt)}</dd>
										</dl>
										<dl class="mapbox-pop-detail-info">
											<dt class="mapbox-popup-info-name">${message.main.gis_data_js_survey_target_number_real_time}</dt>
											<dd class="mapbox-popup-info-con">${numberComma(info.realExmnCnt)}</dd>
										</dl>
									`	
									}
				    _HTML +=	`</div>
				            </div>
				        </div>`;
				        
			mapboxPopupRemove();	        
		    new mapboxgl.Popup({closeOnClick: false})
			    .setLngLat([lng,lat])
			    .setHTML(_HTML)
			    .addTo(map);				        
        		
		});
}

//  시설물 위치 VDS interval
let setFacilitiesVDSInterval = (_event, button = null) => {
	if(button === 'button') {
		facilitiesVDSInterval.clear('rdaFacilitiesVDS');
		facilitiesVDSInterval = null;
	}
	_event();
	facilitiesVDSInterval = new setTimeInterval();
	facilitiesVDSInterval.start(_event);	
}

//  시설물 위치 fixed interval
let setFacilitiesMetroInterval = (_event, button = null) => {
	if(button === 'button') {
		facilitiesMetroInterval.clear('rdaFacilitiesMetro');
		facilitiesMetroInterval = null;
	}
	_event();
	facilitiesMetroInterval = new setTimeInterval();
	facilitiesMetroInterval.start(_event);	
}

//  시설물 위치 move interval
let setFacilitiesMetroMoveInterval = (_event, button = null) => {
	if(button === 'button') {
		facilitiesMetroMoveInterval.clear('rdaFacilitiesMetroMove');
		facilitiesMetroMoveInterval = null;
	}
	_event();
	facilitiesMetroMoveInterval = new setTimeInterval();
	facilitiesMetroMoveInterval.start(_event);	
}


// 조사 위치 map interval 
let setSurveyInterval = (_event, button = null) => {
	let serachYn = 'N';
	if(button === 'button') {
		serachYn = 'Y';
		mapboxPopupRemove();
		if(surveyInterval != null) {
			surveyInterval.clear('rdaSurveyLoc');
			surveyInterval = null;
		}
		
		if(facilitiesDetailInterval != null) facilitiesDetailInterval.clear();
		if(facilitiesVDSInterval != null) {
			facilitiesVDSInterval.clear('rdaFacilitiesVDS');
			facilitiesVDSInterval = null;
		}
		if(facilitiesMetroInterval != null) {
			facilitiesMetroInterval.clear('rdaFacilitiesMetro');
			facilitiesMetroInterval = null;
		}
		if(facilitiesMetroMoveInterval != null) {
			facilitiesMetroMoveInterval.clear('rdaFacilitiesMetroMove');
			facilitiesMetroMoveInterval = null
		} 
	}
	_event(serachYn);
	surveyInterval = new setTimeInterval();
	surveyInterval.start(_event);
}

// 조사 위치 > 상세 interval 
let setSurveyDetailInterval = (_event, surveyDetailExmnmngId, surveyDetailLng, surveyDetailLat, multi = 'defalut') => {
	surveyDetailInterval = new setTimeInterval()
	if(multi === 'defalut') {
		surveyDetailInterval.start(_event, surveyDetailExmnmngId, surveyDetailLng, surveyDetailLat);
	} else if(multi === 'manySurvey') {
		surveyDetailInterval.start(_event, surveyDetailExmnmngId, surveyDetailLng, surveyDetailLat, multi);
	}
}

let setFacilityDetailInterval = async (properties) => {
    const popupContent = await setFacilityHTML(properties);
    mapboxPopupRemove();
    new mapboxgl.Popup({ closeOnClick: false })
        .setLngLat([properties.lon, properties.lat])
        .setHTML(popupContent)
        .addTo(map);
};

/* facilityHTML */
let setFacilityHTML = async (dataResult) => {
    const info = dataResult;
    let _HTML = `
        <div class="mapbox-popup-container">
            <div class="mapbox-popup-header">
                <h4 class="mapbox-popup-title">${message.main.gis_data_js_facilities_detail}</h4>
                <button type="button" class="mapbox-popup-close" onclick="mapboxPopupRemove('button')">
                    <img src="/images/close_light.png" alt="close"/>
                </button>
            </div>
            <div class="mapbox-popup-wrap">
                <div id="mapBoxPopDetailWrap">
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_facilities_type}</dt>
                        <dd class="mapbox-popup-info-con">${info.type}</dd>
                    </dl>
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_facilities_id}</dt>
                        <dd class="mapbox-popup-info-con">${info.instllcId}</dd>
                    </dl>`;
    if (info.type === 'VDS') {
        _HTML += `
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_facilities_camera_id}</dt>
                        <dd class="mapbox-popup-info-con">${info.cameraId}</dd>
                    </dl>`;
    }
    	_HTML += `
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_facilities_status}</dt>
                        <dd class="mapbox-popup-info-con">${info.status}</dd>
                    </dl>
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_facilities_location}</dt>
                        <dd class="mapbox-popup-info-con">${info.location}</dd>
                    </dl>
                    <dl class="mapbox-pop-detail-info">
                        <dt class="mapbox-popup-info-name">${message.main.gis_data_js_coordinate}</dt>
                        <dd class="mapbox-popup-info-con">${info.lon}, ${info.lat}</dd>
                    </dl>
                </div>
                <div id="mapBoxPopDetailTable">
                    <h5 class="mapbox-popup-table-title">${message.main.gis_data_js_data_detail}</h5>
                    <div class="mapbox-popup-table-box scroll">
                        <table class="list-table mapbox-popup-table">
                            <thead>
                                <tr>
                                    <th>No.</th>
                                    <th>${message.main.gis_data_js_vehicle_type}</th>
                                    <th>${message.main.gis_data_js_rate}</th>
                                    <th>${message.main.gis_data_js_vehicle_count}</th>
                                </tr>
                            </thead>
                            <tbody id="mapBoxPopDetailTbody">`;
	
    const facilitiesData = await setFetchFacilityData(info.type, info.instllcId);
    if (!isNull(facilitiesData)) {
        _HTML += setFacilityTableDataHTML(facilitiesData);
    } else {
        _HTML += `
                                <tr>
                                    <td colspan="4">${message.search.result_not_exist}</td>
                                </tr>`;
    }
    	_HTML += `
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>`;
    return _HTML;
};

let setFacilityTableDataHTML = (data) => {
    let _HTML = '';
    data.forEach(info => {
        _HTML += `
            <tr>
                <td>${info.rnum}</td>
                <td>${info.vhclClsf}</td>
                <td>${info.rate}%</td>
                <td>${info.trfvlm}</td>
            </tr>`;
    });
    return _HTML;
};

let setFetchFacilityData = async (url, instllcId) => {
    let fetchUrl = '/main/facilities/vds/';
    if (url === 'Metro Count') {
        fetchUrl = "/main/facilities/fixed/metrocount/";
    } else if (url === 'Portable Metro Count') {
        fetchUrl = "/main/facilities/move/metrocount/";
    }
    const response = await fetch(__contextPath__ + fetchUrl + instllcId);
    const result = await response.json();
    return result.data;
};

let allIntervalReset = () => {
	mapboxPopupRemove();
	if(surveyInterval != null) {
		surveyInterval.clear('rdaSurveyLoc');
		surveyInterval = null;
	}
    if(facilitiesVDSInterval != null) {
		facilitiesVDSInterval.clear('rdaFacilitiesVDS');
		facilitiesVDSInterval = null;
	}
	if(facilitiesMetroInterval != null) {
		facilitiesMetroInterval.clear('rdaFacilitiesMetro');
		facilitiesMetroInterval = null;
	}
	if(facilitiesMetroMoveInterval != null) {
		facilitiesMetroMoveInterval.clear('rdaFacilitiesMetroMove');
		facilitiesMetroMoveInterval = null;
	}
	if(surveyDetailInterval != null) {
		surveyDetailInterval.clear();
		surveyDetailInterval = null;
	}
	if(facilitiesDetailInterval != null) {
		facilitiesDetailInterval.clear();
		facilitiesDetailInterval = null;
	}
}