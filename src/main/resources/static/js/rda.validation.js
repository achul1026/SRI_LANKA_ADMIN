window.addEventListener('load', () => {
	(function(){
		const targetNodes = document.querySelectorAll("input, select");
		const elements = Array.from(targetNodes);
		elements.forEach((element) => {
			element.addEventListener("invalid", (e) => {
				const errorText = element.parentNode.querySelector(".error-message");
				if(element.validity?.tooShort){
					const minlength = element.getAttribute("minlength");
					const maxlength = element.getAttribute("maxlength");
					const addtext = minlength+(maxlength ? `~${maxlength}`:"");
					errorText.textContent = `글자수는 ${addtext}자 입니다.`;
				} else if(element.validity?.patternMismatch) {
					const patternMisMatchText = element.getAttribute("data-pattern-mismatch-text");
					errorText.textContent = patternMisMatchText ?? "양식에 맞지 않습니다."
				} else {
					const placeholder = element.placeholder;
					errorText.textContent = placeholder;
				} 
				e.preventDefault();
			})
		});
	})();
	
	document.querySelectorAll(".login-input").forEach(function(input){
		input.addEventListener("keyup", function(){
			this.parentNode.querySelector('.error-message').textContent = "";
		})
	});
});


