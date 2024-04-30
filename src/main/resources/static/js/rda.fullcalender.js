/*$(function() {
  const calendarEl = document.getElementById('fullCalendar');
  
  // 10 backgroundColor
  const colorR = '#FF0100';
  const colorYr = '#FE8400';
  const colorY = '#FFFF00';
  const colorGy = '#40CA77';
  const colorG = '#008929';
  const colorBg = '#0097BD';
  const colorB = '#0152A6';
  const colorPb = '#002A95';
  const colorP = '#98128D';
  const colorRp = '#DE056A';
  
  
  let calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    headerToolbar: {          
		left: 'prev,next today',
		center: 'title',      
		right: 'scheduleSaveButton'       
	},
    height:'calc(100vh - 200px)',
    eventBorderColor:'transparent',
    
    customButtons: {
		scheduleSaveButton : {
			text: '등록하기',
			click: function(){
				location.href = '/datamng/schedule/save';
			}
		}
	},
    events: [
      {
        title: 'colombo 가구실태조사',
        start: '2024-01-01',
        end:'2024-01-10',
        backgroundColor: colorRp,
      },
      {
        title: 'OD-21 지점 노측 면접 조사',
        url: 'http://google.com/',
        start: '2024-01-21',
        backgroundColor: colorBg,
      }
    ],
  });

  calendar.render();
});*/