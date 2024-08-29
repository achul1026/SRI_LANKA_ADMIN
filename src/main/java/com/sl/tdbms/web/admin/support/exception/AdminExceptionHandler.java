package com.sl.tdbms.web.admin.support.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

@ControllerAdvice
@RestControllerAdvice
public class AdminExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView exceptionHandler(Exception e){
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("views/common/errorPage");
		return mav;
	}
	
	// LoginCheckException
	@ExceptionHandler(value = NoLoginException.class)
	public ModelAndView loginChkExceptionHandler(NoLoginException ne) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/login");
		return mav;
	}
	
	// customRuntimeException 
	@ExceptionHandler(value = CommonResponseException.class)
	public ResponseEntity<ErrorResponse> adminExceptionHandler(CommonException ce, String message) {
		ErrorResponse response = new ErrorResponse(ce.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ce.getErrorCode().getStatus()));
	}
	
	//BindingResult Validation 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> result = new HashMap<>();
        
        if (exception.getBindingResult().hasErrors()) {
            FieldError error = exception.getBindingResult().getFieldError();
            if (error != null) {
                Object[] arguments = error.getArguments();
                String resMsg = CommonUtils.getMessage(error.getDefaultMessage(), arguments);
                String msgTrgt = error.getField();

                result.put("code", 9999);
                result.put("message", resMsg);
                result.put("msgTrgt", msgTrgt);
            }
        }
        
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
	
}
