let gisStatisticsSearch = (_this, resultType) => {
	const thisEl = _this;
	const thisParent = thisEl.closest('.dashborad-search-box');
	const thisDateType = thisParent.querySelector('.search-date-type').value;
	const searchType = thisParent.querySelector('.serach-type option:checked').value;
	const thisLocation = thisParent.querySelectorAll('.location-select');
	let locationTaz = '';
	let startData = '';
	let endData = '';
	let url = '';
	
	const validationEl = thisParent.getAttribute('id');
	if(validation(`#${validationEl}`)) {
		thisLocation.forEach(select => {
			const option = select.querySelector('option:checked');
			if(!isNull(option.dataset.cd)) locationTaz += option.dataset.cd;
		})
		const indexRemove = 2;
		locationTaz = locationTaz.slice(0, indexRemove) + locationTaz.slice(indexRemove + 1);
		
		//month
		let startDt = thisParent.querySelector('.monthStartPicker').value;
			startDt = startDt.replace(/-/g, '');
		let endDt = thisParent.querySelector('.monthEndPicker').value;
			endDt = endDt.replace(/-/g, '');
		
		//time		
		let dateStart = thisParent.querySelector('.startPicker').value;
			dateStart = dateStart.replace(/-/g, '');
		let dateEnd = thisParent.querySelector('.endPicker').value;
			dateEnd = dateEnd.replace(/-/g, '');
		const startTime = thisParent.querySelector('.searchStartHour option:checked').value;
		const endTime = thisParent.querySelector('.searchEndHour option:checked').value;
		
		if(thisDateType === 'month') {
			const startDtVd = startDt.substring(0,4);
			const endDtVd = endDt.substring(0,4);
			if(startDtVd === endDtVd) {
				startData = startDt;
				endData = endDt;
			} else {
				new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_different_year).footer(4,message.common.button_confirm,function(button, modal){
					modal.close();
				}).open();
				return				
			}
		} else if(thisDateType === 'time') {
			startData = dateStart+startTime;
			endData = dateEnd+endTime;
		}		
		
		gisStatisticsSearchData = {
			searchType : thisDateType,
			searchDsdCd : locationTaz,
			searchStartDt : startData,
			searchEndDt : endData,
		}
		
		
		if(searchType === 'vds') {
			url = 'vds';
		} else if(searchType === 'fixedMetroCount') {
			url = 'fixed/metrocount';
		} else if(searchType === 'moveMetroCount') {
			url = 'move/metrocount';
		}
		
		mapboxPopupRemove();
		if(!isNull(url)) setStatisticsLayer(url, resultType, searchType);	
		setStatisticsLegendType(resultType)	
	}
}

let setStatisticsLayer = (url, resultType, searchType) => {
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(gisStatisticsSearchData).toString();
	fetch(__contextPath__ + `/gis/${url}?`+queryString)
		.then((response) => response.json())
		.then((result) => {
			const resultData = result.data
/*			const testData = new Object();
			testData.cameraId = "fid0120231";
			testData.instllcId = "1";
			testData.instllcNm = "VDS2";
			testData.laneCnt = "1";
			testData.lat = "6.91821317";
			testData.location = "Western > Colombo > Sri Jayawardanapura Kotte";
			testData.lon = "79.90937371";
			testData.totalAvg = "100";
			testData.totalCnt = "29133";
			testData.vhclDrct = "콜롬보 방향";
			testData.vhclDrctCd = "0";
			resultData.push(testData)*/
			setMapClusters(resultData, resultType, searchType)
		})	
		.finally(() => {
			loading.end();
		});	
}

//click event 제어
let MapStatisticsEvents = {
	clusters : function(e) {
	    const features = map.queryRenderedFeatures(e.point, {
	        layers: ['rda-clusters']
	    });
	    
	    const clusterId = features[0].properties.cluster_id;
	    map.getSource('rda-source').getClusterExpansionZoom(clusterId, function(err, zoom) {
	        if (err) return;
	
	        map.easeTo({
	            center: features[0].geometry.coordinates,
	            zoom: zoom
	        });
	    });
	},
	pointer : function(e) {
		const transCad = document.getElementById('cadDownSearchBox');
		if(!transCad.classList.contains('none')) transCad.classList.add('none');
		
		const features = e.features[0];
	    const coordinates = features.geometry.coordinates.slice();
	    const instllcId = features.properties.instllcId;
	    const searchType = features.properties.searchType;
	    const resultType = features.properties.resultType;
	    const totalCnt = features.properties.totalCnt; 
	    
	    const searchData = gisStatisticsSearchData;
	    statisticsSearchDetailData = {
			type : searchType,
			searchType : searchData.searchType,
			instllcId : instllcId,
			searchStartDt : searchData.searchStartDt,
			searchEndDt : searchData.searchEndDt,
			resultType : resultType,
			totalCnt : totalCnt
		}
		
		const html = gisSearchHtml(features);
		
		mapboxPopupRemove();
		const popup = new mapboxgl
		.Popup({offset:25, closeOnClick: false})
		.setHTML(html)
        .setLngLat(coordinates)
        .addTo(map);
        
		if(searchType === 'vds') {
			setStatisticsVDSDetail(statisticsSearchDetailData);
		} else if(searchType === 'fixedMetroCount') {
			setStatisticsMetroCountDetail(statisticsSearchDetailData);
		} else if(searchType === 'moveMetroCount') {
			setStatisticsMoveMetroCountDetail(statisticsSearchDetailData);
		}
	}
}

let setMapClusters = (data, resultType, searchType) => {
	let gisGeoJson = null;
    const getTotalValue = (item, type) => {
        if (type === 'trfvlm') {
            return item.totalCnt;
        } else if (type === 'avgspeed') {
            return item.totalAvg;
        } else {
            return 0;
        }
    };
	if(data != null) {
	    gisGeoJson = {
			type: 'FeatureCollection',
	    	features: data.map(item => ({
		        type: 'Feature',
		        properties: {
					resultType: resultType,
					searchType: searchType,
		            location: item.location,
		            instllcId: item.instllcId,
		            totalCnt: getTotalValue(item, resultType),
		            laneCnt: item.laneCnt,
		            vhclDrct: item.vhclDrct
		        },
		        geometry: {
		            type: 'Point',
		            coordinates: [item.lon, item.lat]
		        }
	   		}))
		}
	} else {
		new MsgModalBuilder().init().alertBody(message.statistics.statistics_js_no_data).footer(4,message.common.button_confirm,function(button, modal){
			modal.close();
		}).open();
		return		
	}
	
	const layers = ['rda-clusters', 'rda-cluster-count', 'rda-unclustered-point', 'rda-unclustered-point-label'];
    layers.forEach(layer => {
        if(map.getLayer(layer)) map.removeLayer(layer)
    });

    if(map.getSource('rda-source')) map.removeSource('rda-source')
    
	let circleColor;
	let unclusteredCircleColor;
	let clusterType;
	let clusterText;
	if(resultType === 'avgspeed') {
		circleColor = [
	        'step',
	        ['to-number', ['get', 'sumTrafficTotal'], 0],
	        '#7AD084', 50,
	        '#36BABA', 100,
	        '#FF8888'
    	];
	    unclusteredCircleColor = [
	        'step',
	        ['to-number', ['get', 'totalCnt'], 0],
	        '#7AD084', 50, 
	        '#36BABA', 100, 
	        '#FF8888'
	    ];
	    clusterType = false;
	    clusterText = [
            'concat',
            ['to-string', ['get', 'totalCnt']],
            'km'
        ]
	} else if(resultType ===  'trfvlm') {
        circleColor = [
	        'step',
	        ['to-number', ['get', 'sumTrafficTotal'], 0],
	        '#7AD084', 2000,
	        '#36BABA', 5000,
	        '#FF8888'
        ];
	    unclusteredCircleColor = [
	        'step',
	        ['to-number', ['get', 'totalCnt'], 0],
	        '#7AD084', 2000,
	        '#36BABA', 5000,
	        '#FF8888'
	    ];
	    clusterType = true;
	    clusterText = ['get', 'totalCnt'];
	}    
    
    map.addSource('rda-source', {
        type: 'geojson',
        data: gisGeoJson,
        cluster: clusterType, // 클러스터 활성화
        clusterMaxZoom: 14, // 최대 줌 레벨 설정
        clusterRadius: 100, // 클러스터 반경 설정
        clusterProperties: {
        	'sumTrafficTotal': ['+', ['to-number', ['get', 'totalCnt'], 0]]
   	 	}
        
    });

	//zoom이 멀때 다중으로 보여주는 clusters
    map.addLayer({
        id: 'rda-clusters', 
        type: 'circle',
        source: 'rda-source',
        filter: ['has', 'point_count'],
        paint: {
            'circle-color': circleColor,
            'circle-radius': [
                'step',
                ['get', 'sumTrafficTotal'],
                30, 1000,
                35, 5000,
                40
            ]
        }
    });
	
	//cluster text
    map.addLayer({
        id: 'rda-cluster-count',
        type: 'symbol',
        source: 'rda-source',
        filter: ['has', 'point_count'],
        layout: {
            'text-field': ['get', 'sumTrafficTotal'],
            'text-size': 14,
            'text-font': ['Open Sans Bold']
        },
        paint: {
     	   'text-color': '#fff',
	    }
    });
	
	//zoom 가까이있을때
    map.addLayer({
        id: 'rda-unclustered-point',
        type: 'circle',
        source: 'rda-source',
        filter: ['!', ['has', 'point_count']],
        paint: {
            'circle-color': unclusteredCircleColor,
            'circle-radius': 26,
        }
    });
    
    map.addLayer({
	    id: 'rda-unclustered-point-label',
	    type: 'symbol',
	    source: 'rda-source',
	    filter: ['!', ['has', 'point_count']],
	    layout: {
	        'text-field': clusterText,
	        'text-size': 14,
	        'text-font': ['Open Sans Bold']
	    },
	    paint: {
	        'text-color': '#fff'
	    }
	});
    	
	map.off('click', 'rda-clusters', MapStatisticsEvents.clusters);
	map.on('click', 'rda-clusters', MapStatisticsEvents.clusters);
	
	map.off('click', 'rda-unclustered-point', MapStatisticsEvents.pointer);
	map.on('click', 'rda-unclustered-point', MapStatisticsEvents.pointer);
}


//VDS detail
let setStatisticsVDSDetail = (data) => {
	const type = data.resultType;
	const id = data.instllcId;
	
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(data).toString();
	fetch(__contextPath__ + `/gis/vds/${type}/${id}?`+queryString)
		.then((response) => response.json())
		.then((result) => {
			const detailData = result.data;
			setStatisticsDetail(detailData, data);
		})
		.finally(() => {
			loading.end();
		});	
}

//metro count detail
let setStatisticsMetroCountDetail = (data) => {
	const type = data.resultType;
	const id = data.instllcId;
	
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(data).toString();
	fetch(__contextPath__ + `/gis/fixed/metrocount/${type}/${id}?`+queryString)
		.then((response) => response.json())
		.then((result) => {
			const detailData = result.data;
			setStatisticsDetail(detailData, data);
		})
		.finally(() => {
			loading.end();
		});
}


let setStatisticsMoveMetroCountDetail = (data) => {
	const type = data.resultType;
	const id = data.instllcId;
	
	const loading = new setLoading().start();	
    const queryString = new URLSearchParams(data).toString();
	fetch(__contextPath__ + `/gis/move/metrocount/${type}/${id}?`+queryString)
		.then((response) => response.json())
		.then((result) => {
			const detailData = result.data;
			setStatisticsDetail(detailData, data);
		})
		.finally(() => {
			loading.end();
		});
}

let setDirectionChange = (_this, type) => {
	const value = _this.querySelector('option:checked').value;
	statisticsSearchDetailData.searchVhclDrctCd = value;
	if(type === 'fixedMetroCount') {
		setStatisticsMetroCountDetail(statisticsSearchDetailData)
	} else if(type === 'moveMetroCount') {
		setStatisticsMoveMetroCountDetail(statisticsSearchDetailData);
	}
}

let setCadDown = (_this) => {
	if(validation('#cadDownSearchBox')){
		const loading = new setLoading().start();
		
		const cadParent = _this.closest('#cadDownSearchBox');
		const cadType = cadParent.querySelector('.search-type option:checked').value;
		const cadYear = cadParent.querySelector('#transYear option:checked').value;
		
		setCadData = {
			searchYear : cadYear
		}
			
	    const queryString = new URLSearchParams(setCadData).toString();
		location.href= `${__contextPath__}/gis/transcad/${cadType}?`+queryString
		
		loading.end();
	}
}


let transTypeChangeYear = (_this) => {
	const optionValue = _this.querySelector('option:checked').value;
	const yearSelect = document.getElementById('transYear');
	const optionList = yearSelect.querySelectorAll('option');
	let yearOption = '<option value="">선택</option>';
	
	transTypeData = {
		searchType : optionValue
	}
	
	if(optionValue == '') {
		optionList.forEach(list => {
			if(list.value != '') list.remove();
		})
	} else {
		const loading = new setLoading().start();
			
	    const queryString = new URLSearchParams(transTypeData).toString();
		fetch(__contextPath__ + `/gis/transcad/search/years?`+queryString)
			.then((response) => response.json())
			.then((result) => {
				const detailData = result.data;
				detailData.forEach(value => yearOption += `<option value="${value}">${value}</option>`);
				
				if(optionList) optionList.forEach(option => option.remove());
	
				yearSelect.insertAdjacentHTML('beforeend', yearOption);
			})
			.finally(() => {
				loading.end();
			});
	}
}