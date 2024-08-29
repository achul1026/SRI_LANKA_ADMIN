//modal custom
function isNull(value){
	if(typeof value == "undefined" || value == null || value == "null" || (value.constructor.name.toLowerCase() == "string" && value == "")) {
		return true;
	}else{
		return false;
	}
}

function randomString(num) {
    const char = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    const values = new Uint8Array(num);
    window.crypto.getRandomValues(values);
    let result = '';
    for (let i = 0; i < num; i++) {
        result += char[values[i] % char.length];
    }
    return result;
}

/*
alert 
ex) new ModalBuilder().init().alertBody('컨텐츠').footer(4,'버튼명',function(button, modal){modal.close();}).open();
success
ex) new ModalBuilder().init().successBody('컨텐츠').footer(4,'버튼명',function(button, modal){modal.close();}).open();
modal
ex) new ModalBuilder().init('타이틀').ajaxBody(url, data).footer(2,'버튼명',function(button, modal){modal.close();}).open(); 
*/

///데이터 모달
let ModalBuilder = function(){
    let _self = this;
    let titleText = null;
    let opening = false;
    let $container, $inner, $wrap, $content, $header, $modal;
    let containerId, modalId;
    let promise = null;
    let $headerCloseBtn = $(`<button type="button" class="is-close"><img src="/../images/modal_close.png" alt="닫기"></button>`);
    _self.$body = null;

    const UI = {
        WRAP : function(containerId, modalId, width) {
            $container = $(`<div id="${containerId}" class="modal-container"></div>`);
            $content = $(`<div id="${modalId}" class="modal-content"></div>`);

            if(isNull(width)){
                $content.css({"min-width":""});
            } else {
                $content.css({"min-width":width+"px"});
            }

            $modal = $(`<div id="modal-background"><span class="blind">모달백그라운드</span></div>`)
            $inner = $(`<div id="modal-wrap" class="test"></div>`);
            $inner.append($content);
            $container.append($modal);
            $container.append($inner);
            return $container;
        },
        HEADER : function(title) {
            if(title != undefined || title != null) {
                return $(`<div id="modal-header"><h3 id="modal-title">${title}</h3></div>`)
            }
        },
        BODY : function(content) {
            if(typeof content !== 'string') {
                let body = $(`<div id="modal-body"></div>`);
                body.find('#modal-txt').append(content);
                return body;
            }else{
                return $(`<div id="modal-body">${content}</div>`);
            }
        },
		ALERT_BODY : function(content) {
			let alertBody = $(`<div id="modal-body"></div>`);
			if(content !== null){
				alertBody.find('#modal-body').append(`${content}`)
			}
			return alertBody;
		},
		SUCCESS_BODY : function(content) {
			let alertBody = $(`<div id="modal-body"></div>`);
			if(content !== null){
				alertBody.find('#modal-body').append(`${content}`)
			}
			return alertBody;
		},
		FOOTER : function(buttonText, callback){
			let $footerWrap = $(`<div id="modal-footer"></div>`);
			let $footerButton = $(`<button type="button" class="is-key-button modalOverLapping modal-ft-btn">${buttonText}</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					 const modalContainer = document.querySelectorAll('.modal-container');
					 const overLap = document.querySelector('.modalOverLap');
					 callback($(this), _self);
 					 if(overLap){
						 modalContainer.forEach(modal => {
							modal.classList.remove('modalOverLap');
							document.body.classList.add('scroll-hidden');
						 })
					 }
				}
			});
			$footerWrap.append($footerButton);
			return $footerWrap; 
		},
		FOOTER_CUSTOM : function(buttonText, callback){
			let $footerWrap = $(`<div></div>`);
			let $footerButton = $(`<button type="button" class="is-button footer-one-button">${buttonText}</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					 const modalContainer = document.querySelectorAll('.modal-container');
					 const overLap = document.querySelector('.modalOverLap');
					 callback($(this), _self);
					 if(overLap){
						 modalContainer.forEach(modal => {
							modal.classList.remove('modalOverLap');
							document.body.classList.add('scroll-hidden');
						 })
					 }
				}
			});
			$footerWrap.append($footerButton);
			return $footerWrap; 
		},
		FOOTER_TWO_BUTTON : function(buttonText, callback){
			let $footerWrap = $(`<div id="modal-footer"></div>`);
			let $footerCloseButton = $(`<button type="button" class="is-sub-button">닫기</button>`);
			let $footerButton = $(`<button type="button" class="is-button">${buttonText}</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
/*			$footerCloseButton.on("click", function(){
				_self.close();
			});*/
			
			$footerWrap.append($footerCloseButton);
			$footerWrap.append($footerButton);
			return $footerWrap;
		},
		FOOTER_CUSTOM_TWO_BUTTON : function(OneButtonText, callback, TwoButtonText, callback2){
			let $footerWrap = $(`<div id="modal-footer"></div>`);
			let $footerCloseButton = $(`<button type="button" class="is-basic-button">${TwoButtonText}</button>`);
			let $footerButton = $(`<button type="button" class="is-key-button modalOverLapping">${OneButtonText}</button>`);
			
			
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
		 	$footerCloseButton.on("click", function(){
				if(!isNull(callback)){
					callback2($(this), _self);
				}
			});
			

			$footerWrap.append($footerButton);
			$footerWrap.append($footerCloseButton);
			return $footerWrap;
		},
		FOOTER_CUSTOM_THREE_BUTTON : function(OneButtonText, callback, TwoButtonText, callback2, ThreeButtonText, callback3){
			let $footerWrap = $(`<div id="modal-footer"></div>`);
			let $footerButton = $(`<button type="button" class="is-key-button">${OneButtonText}</button>`);
			let $footerCloseButton = $(`<button type="button" class="is-basic-button">${TwoButtonText}</button>`);
			let $footerCloseButton2 = $(`<button type="button" class="is-basic-button">${ThreeButtonText}</button>`);
			
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
			$footerCloseButton.on("click", function(){
				if(!isNull(callback2)){
					callback2($(this), _self);
				}
			});
			$footerCloseButton2.on("click", function(){
				if(!isNull(callback3)){
					callback3($(this), _self);
				}
			});
			

			$footerWrap.append($footerButton);
			$footerWrap.append($footerCloseButton);
			$footerWrap.append($footerCloseButton2);
			return $footerWrap;
		}
    }

    const checkInit = function(){
        if($wrap == null){
            console.error("Modal 오픈은 init() 으로 시작해주세요.");
            return false;
        }
        return true;
    }

    _self.init = function (title, width, isHeader = true){
        let id = randomString(6)+"-"+Date.now();
        opening = true;
        containerId = "modal-container-"+id;
        modalId = "modal-"+id;
        $wrap = UI.WRAP(containerId,modalId,width);
        titleText = title;
        if(isHeader == null || isHeader){
            $header = UI.HEADER(title);
            $content.append($header);
        }
        if(!isNull($header)){
            $headerCloseBtn.on("click", function(){
                _self.close();
            })
            $header.append($headerCloseBtn)
        }
        return _self;
    }
    
    /*모달 데이터 버전*/
     _self.initData = function (title, width, isHeader = true){
        let id = randomString(6)+"-"+Date.now();
        opening = true;
        containerId = "modal-container-"+id;
        modalId = "modal-"+id;
        $wrap = UI.WRAP(containerId,modalId,width);
        titleText = title;
        if(isHeader == null || isHeader){
            $header = UI.HEADER(title);
            $content.append($header);
        }
        if(!isNull($header)){
            $headerCloseBtn.on("click", function(){
                _self.close();
            })
            $header.append($headerCloseBtn)
        }
        return _self;
    }

    _self.body = function(content){
        if(!checkInit()) return false;
        _self.$body = UI.BODY(content);
        $content.append(_self.$body);
        return _self;
    }

	_self.alertBody = function(content){
		if(!checkInit()) return false;
		_self.$alertBody = UI.ALERT_BODY(content);
		$content.append(_self.$alertBody);
		return _self;
	}
	
	_self.successBody = function(content){
		if(!checkInit()) return false;
		_self.$successBody = UI.SUCCESS_BODY(content);
		$content.append(_self.$successBody);
		return _self;
	}
	
	// html 로드
	_self.ajaxBody = function(url,data, method = "get", contentType) {
		let opt = {};
		promise = new Promise(function(resolve, reject){
			window.modalUrl = url;
			if(method == 'post'){
				opt = {
					type : method,
					url : url,
					async : false,
					contentType : "application/json",
					dataType : "html",
					success : function(html){
						_self.$body = UI.BODY(html);
						$content.append(_self.$body);
						resolve();
					}
				}
			}else{
				opt = {
					type : method,
					url : url,
					async : false,
					dataType : "html",
					success : function(html){
						_self.$body = UI.BODY(html);
						$content.append(_self.$body);
						resolve();
					}
				}
			}
			if(!isNull(contentType)) opt.contentType = contentType
			if(!isNull(data)) opt.data = data;
			$.ajax(opt);
		});
		return _self;
	}

	_self.footer = function(type, buttonText, callback, customCancelButtonText, callback2, customCancelButtonText2, cancelCallback) {
		
		if(!checkInit()) return false;
		let footerButtonType = "";
		if(parseInt(type) == 1) footerButtonType = "FOOTER";
		if(parseInt(type) == 2) footerButtonType = "FOOTER_TWO_BUTTON";
		if(parseInt(type) == 3) footerButtonType = "FOOTER_CUSTOM_TWO_BUTTON";
		if(parseInt(type) == 4) footerButtonType = "FOOTER_CUSTOM";
		if(parseInt(type) == 5) footerButtonType = "FOOTER_CUSTOM_THREE_BUTTON";
		if(footerButtonType == "FOOTER_TWO_BUTTON"){
			$footer = UI[footerButtonType](buttonText, callback, cancelCallback);
		} else if(footerButtonType == "FOOTER_CUSTOM_TWO_BUTTON"){
			$footer = UI[footerButtonType](buttonText, callback, customCancelButtonText, callback2);
		} else if(footerButtonType == "FOOTER_CUSTOM_THREE_BUTTON") {
			$footer = UI[footerButtonType](buttonText, callback, customCancelButtonText, callback2, customCancelButtonText2, cancelCallback);
		} else {
			$footer = UI[footerButtonType](buttonText, callback);	
		}
		
		$content.append($footer);
		return _self;
	}

    _self.open = function(callback){
        if(!checkInit()) return false;
        if(promise !== null) {
            promise.then(function(){
                promise = null;
                generate(callback);
                return;
            });
        }
        $("body").addClass('scroll-hidden');
        generate(callback);
        return _self;
    }

    _self.close = function(){
		window.modalUrl = null;
		$content.fadeOut(10, function(){
            $("#"+containerId).remove();
        });
        $("body").removeClass("scroll-hidden");
	}

   function generate(callback){
        $("body").append($container).ready(function(){
            $container.show();
            opening = false;
        })
   }
	modalOverLap();
	return _self;
}

////얼럿 전용 모달
let MsgModalBuilder = function(){
    let _self = this;
    let titleText = null;
    let opening = false;
    let $container, $inner, $wrap, $content, $header, $modal;
    let containerId, modalId;
    let promise = null;
    let $headerCloseBtn = $(`<button type="button" class="is-close"><img src="/../images/white_close.png" alt="닫기"></button>`);
    _self.$body = null;

    const UI = {
        WRAP : function(containerId, modalId, width) {
            $container = $(`<div id="${containerId}" class="modal-container"></div>`);
            $content = $(`<div id="${modalId}" class="modal-content"></div>`);

            if(isNull(width)){
                $content.css({"min-width":""});
            } else {
                $content.css({"min-width":width+"px"});
            }

            $modal = $(`<div id="modal-background"><span class="blind">모달백그라운드</span></div>`)
            $inner = $(`<div id="modalMsgWrap"></div>`);
            $inner.append($content);
            $container.append($modal);
            $container.append($inner);
            return $container;
        },
        HEADER : function(title) {
            if(title != undefined || title != null) {
                return $(`<div id="modal-header"><h3 id="modal-title">${title}</h3></div>`)
            }
        },
        BODY : function(content) {
            if(typeof content !== 'string') {
                let body = $(`<div id="modal-body"><div id="modal-txt"></div></div>`);
                body.find('#modal-txt').append(content);
                return body;
            }else{
                return $(`<div id="modal-body"><div id="modal-txt">${content}</div></div>`);
            }
        },
		ALERT_BODY : function(content) {
			let alertBody = $(`<div id="modal-body"><div id="modal-txt"></div></div>`);
			if(content !== null){
				alertBody.find('#modal-txt').append(`${content}`)
			}
			return alertBody;
		},
		SUCCESS_BODY : function(content) {
			let alertBody = $(`<div id="modal-body"><div id="modal-txt"></div></div>`);
			if(content !== null){
				alertBody.find('#modal-txt').append(`${content}`)
			}
			return alertBody;
		},
		FOOTER : function(buttonText, callback){
			let $footerWrap = $(`<div id="msgModalFooter"></div>`);
			let $footerButton = $(`<button type="button" class="is-key-button modal-msg-btn modalOverLapping">${buttonText}</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					 const modalContainer = document.querySelectorAll('.modal-container');
					 const overLap = document.querySelector('.modalOverLap');
					 callback($(this), _self);
 					 if(overLap){
						 modalContainer.forEach(modal => {
							modal.classList.remove('modalOverLap');
							document.body.classList.add('scroll-hidden');
						 })
					 }
				}
			});
			$footerWrap.append($footerButton);
			return $footerWrap; 
		},
		FOOTER_CUSTOM : function(buttonText, callback){
			let $footerWrap = $(`<div></div>`);
			let $footerButton = $(`<button type="button" class="is-button modal-msg-btn footer-one-button">${buttonText}</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					 const modalContainer = document.querySelectorAll('.modal-container');
					 const overLap = document.querySelector('.modalOverLap');
					 callback($(this), _self);
					 if(overLap){
						 modalContainer.forEach(modal => {
							modal.classList.remove('modalOverLap');
							document.body.classList.add('scroll-hidden');
						 })
					 }
				}
			});
			$footerWrap.append($footerButton);
			return $footerWrap; 
		},
		FOOTER_TWO_BUTTON : function(buttonText, callback){
			let $footerWrap = $(`<div id="msgModalFooter"></div>`);
			let $footerButton = $(`<button type="button" class="is-button modal-msg-btn">${buttonText}</button>`);
			let $footerCloseButton = $(`<button type="button" class="is-sub-button modal-msg-btn">닫기</button>`);
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
/*			$footerCloseButton.on("click", function(){
				_self.close();
			});*/
			
			$footerWrap.append($footerCloseButton);
			$footerWrap.append($footerButton);
			return $footerWrap;
		},
		FOOTER_CUSTOM_TWO_BUTTON : function(OneButtonText, callback, TwoButtonText, callback2){
			let $footerWrap = $(`<div id="msgModalFooter"></div>`);
			let $footerCloseButton = $(`<button type="button" class="is-basic-button modal-msg-btn">${TwoButtonText}</button>`);
			let $footerButton = $(`<button type="button" class="is-key-button modal-msg-btn modalOverLapping">${OneButtonText}</button>`);
			
			
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
		 	$footerCloseButton.on("click", function(){
				if(!isNull(callback)){
					callback2($(this), _self);
				}
			});
			

			$footerWrap.append($footerButton);
			$footerWrap.append($footerCloseButton);
			return $footerWrap;
		},
		FOOTER_CUSTOM_THREE_BUTTON : function(OneButtonText, callback, TwoButtonText, callback2, ThreeButtonText, callback3){
			let $footerWrap = $(`<div id="msgModalFooter"></div>`);
			let $footerButton = $(`<button type="button" class="is-key-button modal-msg-btn">${OneButtonText}</button>`);
			let $footerCloseButton = $(`<button type="button" class="is-basic-button modal-msg-btn">${TwoButtonText}</button>`);
			let $footerCloseButton2 = $(`<button type="button" class="is-basic-button modal-msg-btn">${ThreeButtonText}</button>`);
			
			$footerButton.on("click", function(){
				if(!isNull(callback)){
					callback($(this), _self);
				}
			});
			
			$footerCloseButton.on("click", function(){
				if(!isNull(callback2)){
					callback2($(this), _self);
				}
			});
			$footerCloseButton2.on("click", function(){
				if(!isNull(callback3)){
					callback3($(this), _self);
				}
			});
			

			$footerWrap.append($footerButton);
			$footerWrap.append($footerCloseButton);
			$footerWrap.append($footerCloseButton2);
			return $footerWrap;
		}
    }

    const checkInit = function(){
        if($wrap == null){
            console.error("Modal 오픈은 init() 으로 시작해주세요.");
            return false;
        }
        return true;
    }

    _self.init = function (title, width, isHeader = true){
        let id = randomString(6)+"-"+Date.now();
        opening = true;
        containerId = "modal-container-"+id;
        modalId = "modal-"+id;
        $wrap = UI.WRAP(containerId,modalId,width);
        titleText = title;
        if(isHeader == null || isHeader){
            $header = UI.HEADER(title);
            $content.append($header);
        }
        if(!isNull($header)){
            $headerCloseBtn.on("click", function(){
                _self.close();
            })
            $header.append($headerCloseBtn)
        }
        return _self;
    }
    
    /*모달 데이터 버전*/
     _self.initData = function (title, width, isHeader = true){
        let id = randomString(6)+"-"+Date.now();
        opening = true;
        containerId = "modal-container-"+id;
        modalId = "modal-"+id;
        $wrap = UI.WRAP(containerId,modalId,width);
        titleText = title;
        if(isHeader == null || isHeader){
            $header = UI.HEADER(title);
            $content.append($header);
        }
        if(!isNull($header)){
            $headerCloseBtn.on("click", function(){
                _self.close();
            })
            $header.append($headerCloseBtn)
        }
        return _self;
    }

    _self.body = function(content){
        if(!checkInit()) return false;
        _self.$body = UI.BODY(content);
        $content.append(_self.$body);
        return _self;
    }

	_self.alertBody = function(content){
		if(!checkInit()) return false;
		_self.$alertBody = UI.ALERT_BODY(content);
		$content.append(_self.$alertBody);
		return _self;
	}
	
	_self.successBody = function(content){
		if(!checkInit()) return false;
		_self.$successBody = UI.SUCCESS_BODY(content);
		$content.append(_self.$successBody);
		return _self;
	}
	
	// html 로드
	_self.ajaxBody = function(url,data, method = "get", contentType) {
		let opt = {};
		promise = new Promise(function(resolve, reject){
			window.modalUrl = url;
			if(method == 'post'){
				opt = {
					type : method,
					url : url,
					async : false,
					contentType : "application/json",
					dataType : "html",
					success : function(html){
						_self.$body = UI.BODY(html);
						$content.append(_self.$body);
						resolve();
					}
				}
			}else{
				opt = {
					type : method,
					url : url,
					async : false,
					dataType : "html",
					success : function(html){
						_self.$body = UI.BODY(html);
						$content.append(_self.$body);
						resolve();
					}
				}
			}
			if(!isNull(contentType)) opt.contentType = contentType
			if(!isNull(data)) opt.data = data;
			$.ajax(opt);
		});
		return _self;
	}

	_self.footer = function(type, buttonText, callback, customCancelButtonText, callback2, customCancelButtonText2, cancelCallback) {
		
		if(!checkInit()) return false;
		let footerButtonType = "";
		if(parseInt(type) == 1) footerButtonType = "FOOTER";
		if(parseInt(type) == 2) footerButtonType = "FOOTER_TWO_BUTTON";
		if(parseInt(type) == 3) footerButtonType = "FOOTER_CUSTOM_TWO_BUTTON";
		if(parseInt(type) == 4) footerButtonType = "FOOTER_CUSTOM";
		if(parseInt(type) == 5) footerButtonType = "FOOTER_CUSTOM_THREE_BUTTON";
		if(footerButtonType == "FOOTER_TWO_BUTTON"){
			$footer = UI[footerButtonType](buttonText, callback, cancelCallback);
		} else if(footerButtonType == "FOOTER_CUSTOM_TWO_BUTTON"){
			$footer = UI[footerButtonType](buttonText, callback, customCancelButtonText, callback2);
		} else if(footerButtonType == "FOOTER_CUSTOM_THREE_BUTTON") {
			$footer = UI[footerButtonType](buttonText, callback, customCancelButtonText, callback2, customCancelButtonText2, cancelCallback);
		} else {
			$footer = UI[footerButtonType](buttonText, callback);	
		}
		
		$content.append($footer);
		return _self;
	}

    _self.open = function(callback){
        if(!checkInit()) return false;
        if(promise !== null) {
            promise.then(function(){
                promise = null;
                generate(callback);
                return;
            });
        }
        $("body").addClass('scroll-hidden');
        generate(callback);
        return _self;
    }

    _self.close = function(){
		window.modalUrl = null;
		$content.fadeOut(10, function(){
            $("#"+containerId).remove();
        });
        $("body").removeClass("scroll-hidden");
	}

   function generate(callback){
        $("body").append($container).ready(function(){
            $container.show();
            opening = false;
        })
   }
	modalOverLap();
	return _self;
}

//modal 2개 겹칠때
function modalOverLap(){
	const modalContainer = document.querySelectorAll('.modal-container');
	modalContainer.forEach(modal => modal.classList.add('modalOverLap'));
}
