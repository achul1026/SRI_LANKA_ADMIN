window.addEventListener('load', () => {
	//sidebarActive
	const sidebarActive = document.querySelector('.side-depth2.active');
	if(sidebarActive) {
		const parent = sidebarActive.closest('.sidebar-list-box'); 
		sidebarActive.closest('.side-depth2-box').style.display = 'block';
		parent.classList.add('active', 'on');
		parent.querySelector('.side-depth1').classList.add('notClick');
	}
 	$('.side-depth1').not('.notClick').on('click', function(){
		$(this).parent().siblings().not('.on').removeClass('active');
		$('#sidebarInfoWrap').removeClass('active');
		$(this).parent().toggleClass('active');
	    $(this).parent().siblings('.sidebar-list-box').not('.on').find(".side-depth2-box").stop().slideUp(300);
	    $('.user-info-list-box').stop().slideUp(300);
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

//즐겨찾기
function bookMark(){
	new ModalBuilder().init('즐겨찾기', '1000').ajaxBody("/common/bookmark").footer(4,'닫기',function(button, modal){}).open();
	document.querySelector('.footer-one-button').remove();
}