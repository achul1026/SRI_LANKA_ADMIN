let map, locationLng, locationLat, marker, surveyMapLocation;
//center는 조사 정보가 없을때 RDA 사무실
const mapCenterLng = 79.92619137078884;
const mapCenterLat = 6.8996839117806275;
const RDA_ENV = {
	"MARKERS" : [{
		"locationUrl" : "/images/map_location_marker.png", 
	  	"userUrl" : "/images/map_user_marker.png",
	  	"testImg" : "/images/test_img.png",
	  	"testImg2" : "/images/test_img2.png"
	  }]
}

function mapboxGl(lng = mapCenterLng, lat = mapCenterLat, circleMeters = 0){
	return new Promise((resolve, reject) => {
	    mapboxgl.accessToken = 'pk.eyJ1IjoidGFleXUiLCJhIjoiY2xpbzVzcWphMDVlZzNlbndxbzQ4a20zMCJ9.Zy8tFFyQruKS8zQKTh3wKA';
	    map = window.map = new mapboxgl.Map({
	        container: 'map',
	        style:  'mapbox://styles/mapbox/streets-v12',
	        zoom: 15,
			center:[lng, lat],
	    });
	    
	    //주소검색
	    const mapSearch = document.getElementById('mapSearch');
	    if(mapSearch) {
			const geocoder = new MapboxGeocoder({
			    accessToken: mapboxgl.accessToken,
			    mapboxgl: mapboxgl,
			    placeholder:'지역명 검색',
			    countries: 'LK',
			});
			
			map.addControl(geocoder);
			
			geocoder.on('result', function(e){
				map.jumpTo({
					center:e.result.geometry.coordinates,
					zoom:13
				})
			})
		}
	    
		//좌표 등록한 좌표 입력해줄 element	    
		const coordinate = document.getElementById('coordinate');
	    
	    //조사위치 등록
	    const mapLoactionSave = document.getElementById('mapLoactionSave');
	    if(mapLoactionSave){
		    map.on('click', function(e){
				const lngLat = e.lngLat;
				locationLng = e.lngLat.lng;
				locationLat = e.lngLat.lat;
				
				new ModalBuilder().init().alertBody('조사 위치를 등록하시겠습니까?').footer(3,'등록',function(button, modal){
					modal.close();
					coordinate.value = `${locationLng}, ${locationLat}`;
					document.getElementById('lat').value = locationLat;
					document.getElementById('lon').value = locationLng;
				    //조사해야할곳위치		
				    const el = document.createElement('img')
					el.src = RDA_ENV.MARKERS[0].locationUrl
					el.style.width = '26px'
					el.style.height = '36px'
					markerSet(el, locationLng, locationLat);
					
					document.getElementById('surveyMapLocation').value = surveyMapLocation;
					}, '취소', function(button, modal){
						modal.close();	
				}).open();
				
				//위치 등록한곳 주소 받아오기
				fetch(`https://api.mapbox.com/geocoding/v5/mapbox.places/${lngLat.lng},${lngLat.lat}.json?access_token=${mapboxgl.accessToken}`)
			    .then(response => response.json())
			    .then(data => {
					const mapLocation = data.features[0].place_name;
			      	surveyMapLocation = mapLocation.replace(', Sri Lanka', '');
			    });
			})
		}
	    
		map.on('load', () => {
			//조사반경
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
					'fill-opacity': 0.5
				}
			})
			
			//좌표반경 입력하면 기존에 찍었던 마커 다시 찍어주기
			if(coordinate) {
				if(coordinate.value !== ''){
				    //조사해야할곳위치		
				    const el = document.createElement('img')
					el.src = RDA_ENV.MARKERS[0].locationUrl
					el.style.width = '26px'
					el.style.height = '36px'
					marker = new mapboxgl.Marker(el)
					.setLngLat([locationLng, locationLat])
		    		.addTo(map);
				}
			}
			resolve(map);
		})
		
		
	    //조사해야할곳위치	
	    const coordinateDetail = document.getElementById('coordinateDetail'); 	
		if(coordinateDetail) {
		    const el = document.createElement('img')
			el.src = RDA_ENV.MARKERS[0].locationUrl
			el.style.width = '26px'
			el.style.height = '36px'
			markerSet(el, lng, lat);
		}
		
		
		//zoomControl
		const zoomIn = document.getElementById('zoomIn');
		const zoomOut = document.getElementById('zoomOut');
		if(zoomIn && zoomOut) {
			zoomIn.addEventListener('click', () => map.zoomIn());
			zoomOut.addEventListener('click', () => map.zoomOut());
		}
		
		
		//VDS, 등등 목록 마커 호출하기
		const dashBoard = document.getElementById('dashboardSearch');
		if(dashBoard){
			dashBoard.addEventListener('click', () => {
				const testVDS = document.getElementById('TEST_VDS');
				if(testVDS.checked == true) testFacilityMarkerSet();
			})
		}
	})
}
    
    
function markerSet(el, lng, lat){
	if(marker) {
		const range = document.getElementById('exmnRange');
    	mapRemove();
    	mapboxGl(locationLng, locationLat);
		marker.remove();
		marker = null;
		marker = new mapboxgl.Marker(el)
		.setLngLat([lng, lat])
    	.addTo(map);
    	range.value = '';
	} else {
		marker = new mapboxgl.Marker(el)
		.setLngLat([lng, lat])
	    .addTo(map);
	}
}    

function mapRemove(){
	if(map){
		map.remove();
		map = null;
	}
}


function testFacilityMarkerSet(){
	const test = [
		{"lng" : 79.92164938794343, "lat" : 6.899842596750375},
		{"lng" : 79.91741602134726, "lat" : 6.902225833231256},
		{"lng" : 79.92172682757621, "lat" : 6.903635268513483},
		{"lng" : 79.91462819456393, "lat" : 6.9024308422605145},
		{"lng" : 79.9270443490351, "lat" : 6.90112390817778},
		{"lng" : 79.92500510536973, "lat" : 6.896511170814321},
		{"lng" : 79.92064267271763, "lat" : 6.895537364961228}
	]
	
	const testHTML = `
		<div>
			<div>Popup TEST</div>
			<div>팝업테스트해보는중입니다</div>
		</div>
	`
	
	test.forEach(function(marker){
		const el = document.createElement('img')
		el.src = RDA_ENV.MARKERS[0].testImg
		el.style.width = '26px'
		el.style.height = '36px'
		
		const testPopup = new mapboxgl.Popup({offset:25}).setHTML(testHTML);
		
		marker = new mapboxgl.Marker(el)
		.setLngLat([marker.lng, marker.lat])
		.setPopup(testPopup)
		.addTo(map);
	})
}
