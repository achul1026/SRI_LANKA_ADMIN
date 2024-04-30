$(function(){
	
	//date -> time  아직 미완성
	
	// 24/01/22 팀장님이 나중에 짜라고 하셨음  아직 미완성
	$('.startHour').on('change', function(){
		const startDate = $('.startPicker').val();
		const endDate = $('.endPicker').val();
		const startHour = $('.startHour').val();
		const endHour = $('.endHour');
		
		if(startDate != '' && endDate != '' && startDate === endDate) {
			endHour.find('option').each(function() {
	      		if ($(this).val() < startHour) {
	        		$(this).prop('disabled', true);
	      		} else {
	        		$(this).prop('disabled', false);
				}
			})
		}
	})
	
	// 24/01/22 팀장님이 나중에 짜라고 하셨음 아직 미완성
	$('.endHour').on('change', function(){
		const startDate = $('.startPicker').val();
		const endDate = $('.endPicker').val();
		const startHour = $('.startHour');
		const endHour = $('.endHour').val();
		
		
		if(startDate != '' && endDate != '' && startDate === endDate) {
			startHour.find('option').each(function() {
	      		if ($(this).val() > endHour) {
	        		$(this).prop('disabled', true);
	      		} else {
	        		$(this).prop('disabled', false);
			    }
			})
		}
	})
})