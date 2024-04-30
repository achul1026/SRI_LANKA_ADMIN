package com.sri.lanka.traffic.admin.support.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	
	//0 ~ 1000
	INVALID_PARAMETER(400, "pleace Check Parameter Value"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	
	//회원가입 1000~2000
	REGEX_NOT_FOUND(1001, "Regex not found"),					//정규식 못찾았을때
	REQUIRED_FIELDS(1002,"Required field are missing"),			//필수 파라미터 누락
	VALIDATION_FAILED(1003,"validation failed"),				//유효성 검증 실패
	
	// 사용자 2000~3000
	NOT_FOUND_USER_INFO(2001, "Not Found User Information"),
	MNGR_STTS_NOT_NORMAL(2002, "This Account Status Is Not Normal."),
	PERMISSION_DENIED(2003, "Permission denied."), 				// 허가가 거부 되었습니다.
	
	// 조사 관련 3000~4000
	IN_PROGRESS_OR_COMPLETE(3001,"In progress or complete"),
	
	//공통 9000 ~ 9999
	ENUM_CONVERTER_FAIL(9001, "Enum Converter Fail"),
	DUPLICATION_DATA(9002, "Data already exists."),
	EMPTY_DATA(9003, "Data is empty"),
	ENTITY_SAVE_FAILED(9004, "Entity save failed"),
	ENTITY_DELETE_FAILED(9005, "Entity delete failed"),
	FILE_DOWNLOAD_FAILED(9006, "File download failed")
	;
	
	private int status;
	private String message;
	
}
