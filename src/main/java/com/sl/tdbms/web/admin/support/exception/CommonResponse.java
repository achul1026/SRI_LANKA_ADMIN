package com.sl.tdbms.web.admin.support.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class CommonResponse<T> {
	private int code;
	private String message;
	private String successUrl;
	private T data;
	
	
	
	public CommonResponse(HttpStatus httpStatus, String message, String successUrl, T data) {
		super();
		this.code = httpStatus.value();
		this.message = message;
		this.successUrl = successUrl;
		this.data = data;
	}

	public CommonResponse(HttpStatus httpStatus, String message, T data) {
		super();
		this.code = httpStatus.value();
		this.message = message;
		this.data = data;
	}
	
	public CommonResponse(HttpStatus httpStatus, String message, String successUrl) {
		super();
		this.code = httpStatus.value();
		this.message = message;
		this.successUrl = successUrl;
	}

	public CommonResponse(HttpStatus httpStatus, String message) {
		super();
		this.code = httpStatus.value();
		this.message = message;
	}
	
	public CommonResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public static <T> CommonResponse<Map<String,Object>> successToData(Map<String,Object> resultMap , String message){
		return new CommonResponse<>(HttpStatus.OK, message, resultMap);
	}
	
	public static <T> CommonResponse<T> ResponseCodeAndMessage(HttpStatus httpStatus , String message){
		return new CommonResponse<>(httpStatus, message);
	}

	public static <T> CommonResponse<T> ResponseCodeAndMessage(int code , String message){
		return new CommonResponse<>(code, message);
	}
	public static <T> CommonResponse<T> ResponseCodeAndMessage(HttpStatus httpStatus , String message, T data){
		return new CommonResponse<>(HttpStatus.OK, message, data);
	}
	public static <T> CommonResponse<T> ResponseCodeAndMessageAndSuccessUrl(HttpStatus httpStatus , String message , String successUrl){
		return new CommonResponse<>(httpStatus, message, successUrl);
	}
	public static <T> CommonResponse<T> ResponseSuccess(HttpStatus httpStatus , String message , String successUrl , T data){
		return new CommonResponse<>(HttpStatus.OK, message, successUrl, data);
	}

	public int getCode() {
		return code;
	}

	public void setCode(HttpStatus httpStatus) {
		this.code = httpStatus.value();
	}
	
	public void setCode(int code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	@Override
	public String toString() {
		return "CommonResponse [code=" + code + ", message=" + message + ", successUrl=" + successUrl + ", data=" + data
				+ "]";
	}
}
