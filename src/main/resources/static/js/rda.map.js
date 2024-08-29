let map, locationLng, locationLat, marker;
let copyHTML = '';
let mapboxglToken = 'pk.eyJ1IjoidGFleXUiLCJhIjoiY2xpbzVzcWphMDVlZzNlbndxbzQ4a20zMCJ9.Zy8tFFyQruKS8zQKTh3wKA';
//center는 조사 정보가 없을때 RDA 사무실
const mapCenterLng = 79.92619137078884;
const mapCenterLat = 6.8996839117806275;
const RDA_ENV = {
	"MARKERS" : [{
		"locationUrl" : "/images/map_location_marker.png", 
	  	"userUrl" : "/images/map_user_marker.png",
	  	"vds" : "/images/marker_vds.png",
	  	"metro" : "/images/marker_metro.png",
	  	"metroMove" : "/images/marker_metro_move.png",
	  	"vdsDanger" : "/images/marker_error_vds.png",
	  	"metroDanger" : "/images/marker_error_metro.png",
	  	"metroMoveDanger" : "/images/marker_error_metro_move.png",
	  	"household" : "/images/marker_od.png",
	  	"roadside" : "/images/marker_roadside.png",
	  	"mcc" : "/images/marker_mcc.png",
	  	"tm" : "/images/marker_tm.png",
	  	"surveyMulti" : "/images/marker_multi.png"
	  }]
}

function mapboxGl(lng = mapCenterLng, lat = mapCenterLat, zoomLevel = 15, element = 'map'){
	return new Promise((resolve, reject) => {
	    mapboxgl.accessToken = mapboxglToken;
	    map = window.map = new mapboxgl.Map({
	        container: element,
	        style: 'mapbox://styles/mapbox/streets-v12',
	        zoom: zoomLevel,
	        minZoom: 8,
			center:[lng, lat],
        	maxBounds: [
				[79.5, 5.8],
    			[81.9, 9.9]
    		]
	    });
		const scale = new mapboxgl.ScaleControl({
			maxWidth: 80,
			unit: 'metric'
		});
		map.addControl(scale, "bottom-right");
		map.on('load', () => {
			resolve(map);
			setMapCoordinates(element);
		});
		map.on('error', error => reject(error));
	})
}

//맵 스타일 변경
let setMapStyle = (map) => {
	const mapStyleInput = document.querySelectorAll('.map-style');
	mapStyleInput.forEach(input => {
		input.addEventListener('click', (style) => {
			const styleName = style.target.id;
			map.setStyle(`mapbox://styles/mapbox/${styleName}`)
		})
	})	
}

//맵 줌
let setMapZoomControl = (map) => {
	const zoomIn = document.getElementById('zoomIn');
	const zoomOut = document.getElementById('zoomOut');
	if(zoomIn && zoomOut) {
		zoomIn.addEventListener('click', () => map.zoomIn());
		zoomOut.addEventListener('click', () => map.zoomOut());
	}	
}
let trafficVolumeGeoJSON = null;
let findTrafficVolumeLineCoordinates = async (id) => {
	if(!trafficVolumeGeoJSON) {
		await fetch("/js/Traffic_Volume_Line.geojson")
			.then(res => res.json())
			.then(res => {
				trafficVolumeGeoJSON = res;
			})
	}
	let features = trafficVolumeGeoJSON.features;
	let feature = features.find((obj) => obj.properties.id === id);
	if(!feature) {
		new MsgModalBuilder().init().alertBody('Not found Line Geometric data').footer(4, message.common.button_confirm, function(button, modal) {
			modal.close();
		}).open();		
		return;
	}
	return feature.geometry.coordinates;
}

//타즈 레이어
let setTazCodeLayer = (map) => {
    map.addSource('rda-location', {
        'type': 'geojson',
        'data': '/js/rda.tazs.geojson'
    });
    
    map.addLayer({
        'id': 'rda-location-taz',
        'type': 'fill',
        'source': 'rda-location',
        'paint': {
            'fill-color': 'rgba(255, 255, 255, 0.1)'
        }
    });
}

//타즈 레이어
let hoveredTazPolygonId = null;
let setTazCodeColorEventLayer = (map) => {
    map.addSource("rda-location", {
        "type": "vector",
        "url": "mapbox://taeyu.aqs1v0r0",
        "promoteId" : {"rdatazs":"RDA_New_1"}
    });

	map.addLayer({
		'id': 'rda-location-taz',
		'type': 'fill',
		'source': 'rda-location',
		'source-layer': "rdatazs",
		'paint': {
			'fill-color': "#FF8636",
			'fill-opacity': [
				'case',
				['boolean', ['feature-state', 'hover'], false],
				0.5,
				0
			]
		}
	});
	map.addLayer({
		'id': 'rda-location-taz-line',
		'type': 'line',
		'source': 'rda-location',
		'source-layer': "rdatazs",
		'layout': {
			'line-join': 'round',
			'line-cap': 'round'
		},
		'paint': {
			'line-color': "#FF8636",
			'line-width': 2,
			'line-opacity': 0.5,
		},
	});

	map.addLayer({
		'id': 'rda-location-taz-label',
		'type': 'symbol',
		'source': 'rda-location',
		'source-layer': "rdatazs",
		'layout': {
			'text-field': ['get','RDA_New_1'],
		},
		'paint': {
			'text-color': '#ffffff',
			"text-halo-color": "#000",
			"text-halo-width": 1,
		}
	});
}


//맵 주소 검색
let setMapSearch = (map) => {
	const geocoder = new MapboxGeocoder({
	    accessToken: mapboxglToken,
	    mapboxgl: mapboxgl,
	    placeholder:message.map.map_js_placeholder_local_name,
	    language:'en',
	    countries: 'LK',
	    autocomplete: true,
	    
	});
	map.addControl(geocoder);
	
	geocoder.on('result', function(e){
		map.jumpTo({
			center:e.result.geometry.coordinates,
			zoom:13
		})
	})	
}

let setRoadLayer = (map) => {
    map.addSource("rda-road-location", {
        "type": "vector",
        "url": "mapbox://taeyu.dnhiic8m",
        "promoteId" : {"road":"roadcd"}
    });
    
    map.addLayer({
        'id': 'rda-location-road',
        'type': 'line',
        'source': 'rda-road-location',
        'source-layer': "road",
        'paint': {
            'line-color': 'rgba(50, 113, 209, 0.4)',
            'line-width': 20
        }
    });
}

let setLocationKm = (data) => {
	const queryString = new URLSearchParams(data).toString();
	fetch(__contextPath__ + '/common/road/location/distance?'+queryString)
		.then((response) => response.json())
		.then((resultData) => {
	    	const exmnDistance = document.getElementById('exmnDistance');
	    	if(!isNull(resultData.data)) {
		    	const km = resultData.data / 1000;
				exmnDistance.value = `${km}`
			}
		})

}

//조사등록 조사위치(클릭) 마커등록
let setTrafficClickMaker = (map, fn) => {
	const coordinate = document.getElementById('coordinate');
    map.on('click', function(e){
		locationLng = e.lngLat.lng;
		locationLat = e.lngLat.lat;
		
		new MsgModalBuilder().init().alertBody(message.map.map_js_regist_invest_region).footer(3,message.common.button_regist,function(button, modal){
		    const features = map.queryRenderedFeatures(e.point, { layers: ['rda-location-taz'] });
		    if (!features.length) return;
		    const feature = features[0];
		    
			coordinate.value = `${locationLng}, ${locationLat}, (${feature.properties.RDA_New_1})`;
			document.getElementById('lat').value = locationLat;
			document.getElementById('lon').value = locationLng;
			document.getElementById('tazCode').value = feature.properties.RDA_New_1;

			setMarker(locationLng, locationLat)
			
			//조사반경 
			if(map.getLayer('circle')) {
				map.removeLayer('circle');
			    map.removeSource('circle');
			}
			document.getElementById('invstLocatRadiusButton').setAttribute('onclick', 'setTrafficCircleMeters()');
			
			//validation
			if(coordinate.classList.contains('tag-invalid')) coordinate.classList.remove('tag-invalid');
			if(typeof fn === "function") fn(feature.properties.RDA_New_1);
			
		    const roadCd = document.getElementById('roadCd');
		    const roadDescr = document.getElementById('roadDescr');
		    const exmnDistance = document.getElementById('exmnDistance');
		    const roadFeatures = map.queryRenderedFeatures(map.project(e.lngLat), { layers: ['rda-location-road'] });
		    if (roadFeatures.length) {
			    const roadFeature = roadFeatures[0];
				roadDescr.value = roadFeature.properties.roaddescr
				roadCd.value = roadFeature.properties.roadcd

				roadData = {
					roadCd : roadFeature.properties.roadcd,
					lon : locationLng,
					lat : locationLat
				}
				setLocationKm(roadData)
			    
			    if(roadCd.classList.contains('tag-invalid')) roadCd.classList.remove('tag-invalid');
			    if(roadDescr.classList.contains('tag-invalid')) roadDescr.classList.remove('tag-invalid');
			    if(exmnDistance.classList.contains('tag-invalid')) exmnDistance.classList.remove('tag-invalid');
			} else {
				if(!window.inputManual) {
					roadDescr.value = ''
					roadCd.value = ''
					exmnDistance.value = ''
				}
			}
			modal.close();
		}, message.common.button_cancel, function(button, modal){
			modal.close();	
		}).open();
		
		//위치 등록한곳 주소 받아오기
		/* fetch(`https://api.mapbox.com/geocoding/v5/mapbox.places/${lngLat.lng},${lngLat.lat}.json?access_token=${mapboxgl.accessToken}`)
	    .then(response => response.json())
	    .then(data => {
			const mapLocation = data.features[0].place_name;
	      	surveyMapLocation = mapLocation.replace(', Sri Lanka', '');
	    }); */
	    
	})	
}

//조사등록 조사반경
let setTrafficCircleMeters = (lng = locationLng, lat = locationLat) => {
	const coordInateValue = document.getElementById('coordinate').value;
	const locationCircle = document.getElementById('exmnRange');
	const locationCircleValue = locationCircle.value;
	if(coordInateValue !== ''){		
		if(locationCircleValue !== '') {
			if(map.getLayer('circle')) {
				map.removeLayer('circle');
			    map.removeSource('circle');
			}			
			const turfJs = turf.circle([lng, lat], locationCircleValue, {units:'meters'});
			map.addLayer({
				id:'circle',
				type: 'fill',
				source: {
					type:'geojson',
					data: turfJs
				},
				paint: {
					'fill-color':'#B1B1B1',
					'fill-opacity': 0.3
				}
			})
			//validation
			if(locationCircle.classList.contains('tag-invalid')){
				locationCircle.classList.remove('tag-invalid');
			}
		} else {
			new MsgModalBuilder().init().alertBody(message.map.map_js_coordinate_radius).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
		}
	} else {
		new MsgModalBuilder().init().alertBody(message.map.map_js_regist_location).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();				
	}
	
}

/* 마커 start */
let markerOption = (el, lng, lat) => {
	if(marker) {
		const range = document.getElementById('exmnRange');
		marker.remove();
		marker = null;
		marker = new mapboxgl.Marker(el, {offset : [0,-18]})
		.setLngLat([lng, lat])
    	.addTo(map);
    	if(range) range.value = '';
	} else {
		marker = new mapboxgl.Marker(el, {offset : [0,-18]})
		.setLngLat([lng, lat])
	    .addTo(map);
	}
}
    
let setMarker = (lng, lat) => {
    const el = document.createElement('img')
	el.src = RDA_ENV.MARKERS[0].locationUrl
	el.style.width = '26px'
	el.style.height = '36px'
	markerOption(el, lng, lat);
}
/* 마커 end */

// 맵 제거
function mapRemove(){
	if(map){
		map.remove();
		map = null;
	}
}

// 조사상세 조사반경
let setTrafficSurveyCircleMeters = (map, lng, lat, circleMeters) => {
	const turfJs = turf.circle([lng, lat], circleMeters, {units:'meters'});
	map.addLayer({
		id:'circle',
		type: 'fill',
		source: {
			type:'geojson',
			data: turfJs
		},
		paint: {
			'fill-color':'#B1B1B1',
			'fill-opacity': 0.3
		}
	})	
}

// 시설물등록 좌표값 마커 등록
let setCoordinatesMarker = () => {
    const lng = document.getElementById('lngCoordinates').value;
    const lat = document.getElementById('latCoordinates').value;
    const htmlLng = document.getElementById('facilitiesLocationLng');
    const htmlLat = document.getElementById('facilitiesLocationLat');

    if (!lat || !lng) {
        new MsgModalBuilder().init().alertBody(message.map.map_js_coordinate).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
        return;
    }

    if (lat < -90 || lat > 90) {
        new MsgModalBuilder().init().alertBody(message.map.map_js_not_valid_coordinate).footer(4,message.common.button_confirm,function(button, modal){modal.close();}).open();
        return;
    }
    
    setMarker(lng, lat)
    htmlLng.value = lng;
    htmlLat.value = lat;
    
    
    map.flyTo({
        center: [lng, lat],
        essential: true,
        zoom: 15
    });
}

// 팝업 이전으로
let mapboxPopupPrev = () => {
	mapboxPopupRemove();
	surveyDetailInterval.clear();
	document.querySelector('#map').appendChild(copyHTML);
}

// 팝업 제거
let mapboxPopupRemove = (button) => {
    const popup = document.querySelector('.mapboxgl-popup');
	if(popup) popup.remove();
	if(button) {
		if(surveyDetailInterval != null) surveyDetailInterval.clear();
		if(facilitiesDetailInterval != null) facilitiesDetailInterval.clear();
	}
}

// 맵 스타일 변경
let mapStyle = () => {
	const styleList = document.getElementById('mapStyleList');
	if(!styleList.classList.contains('active')){
		styleList.classList.add('active');
	} else {
		styleList.classList.remove('active');
	}
}

let setMapCoordinates = (el) => {
	map.on('mousemove', function(e) {
		const container = document.getElementById(el);
	    const element = document.querySelector('.coordinates')
		const coordinatesBox = document.querySelector('.coordinatesBox');
	    const coordinates = e.lngLat;
	    const html = `<div class="coordinatesBox">
						 <div class="coordinates"></div>
					  </div>`
					  
	    if(!coordinatesBox) container.insertAdjacentHTML('beforeend', html);
	    if(element) element.textContent = `${coordinates.lng}, ${coordinates.lat}`;
	});
	
	map.on('mouseout', function() {
		const coordinatesBox = document.querySelector('.coordinatesBox');
		if(coordinatesBox) coordinatesBox.remove();
	})
}

//설문조사 GPS
let setSurveyGps = (map) => {
    map.addSource("rda-gps", {
        "type": "vector",
        "url": "mapbox://taeyu.aqs1v0r0",
        "promoteId" : {"rdatazs":"RDA_New_1"}
    });
	
    map.addLayer({
        'id': 'rda-gps',
        'type': 'fill',
        'source': 'rda-gps',
        'source-layer': "rdatazs",
        'paint': {
            'fill-color': 'rgba(0, 0, 0, 0)',
        }
    });
    
    map.on('click', function(e){
		gpsLng = e.lngLat.lng;
		gpsLat = e.lngLat.lat;
		
	    const features = map.queryRenderedFeatures(e.point, { layers: ['rda-gps'] });
	    if (!features.length) return;
	    const feature = features[0];
	    gpsTaz = feature.properties.RDA_New_1;
	    
		//GPS 좌표등록
	    const el = document.createElement('img')
		el.src = RDA_ENV.MARKERS[0].locationUrl
		el.style.width = '26px'
		el.style.height = '36px'
		if(marker) marker.remove();
		marker = new mapboxgl.Marker(el, {offset : [0,-18]})
		.setLngLat([gpsLng, gpsLat])
	    .addTo(map);
	    
		//GPS 좌표등록한곳 주소 받아오기
		fetch(`https://api.mapbox.com/geocoding/v5/mapbox.places/${gpsLng},${gpsLat}.json?access_token=${mapboxgl.accessToken}`)
	    .then(response => response.json())
	    .then(data => {
			const mapLocation = data.features[0].place_name;
			gpsLocation = mapLocation.replace(', Sri Lanka', '');
	      	document.getElementById('modalGpsLocation').value = gpsLocation;
	    });					    
	})	    	
}