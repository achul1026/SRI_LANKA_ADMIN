window.addEventListener('load', () => {
	//sidebarActive
 	$('.side-depth1').not('.active').on('click', function(){
		$(this).parent().siblings().not('.on').removeClass('active');
		$('#sidebarInfoWrap').removeClass('active');
		$(this).parent().toggleClass('active');
	    $(this).parent().siblings('.sidebar-list-box').not('.on').find(".side-depth2-box").stop().slideUp(300);
	    $(this).next().stop().slideToggle(300);
	})
	
	//divisionActive
	$('.division-title-box').not('.division-not-active').on('click', function(){
		$(this).parent().toggleClass('active');
	    $(this).next().stop().slideToggle(300);
	})
	
	//sidebar userinfo
	$('#sidebarUserBox').on('click', function(){
		$('.sidebar-list-box').not('.on').find(".side-depth2-box").stop().slideUp(300);
		$('.sidebar-list-box').removeClass('active');
		if($('#sidebarInfoWrap').hasClass('active')){
			$('#sidebarInfoWrap').removeClass('active');
		} else {
			$('#sidebarInfoWrap').addClass('active');
		}
		$(this).next().stop().slideToggle(300);
	})
})

