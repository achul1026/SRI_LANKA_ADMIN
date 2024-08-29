package com.sl.tdbms.web.admin.support.exception;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	
	//0 ~ 1000
	INVALID_PARAMETER(400, "enums.ErrorCode.INVALID_PARAMETER"),
	INTERNAL_SERVER_ERROR(500, "enums.ErrorCode.INTERNAL_SERVER_ERROR"),
	
	//회원가입 1000~2000
	REGEX_NOT_FOUND(1001, "enums.ErrorCode.REGEX_NOT_FOUND"),					//정규식 못찾았을때
	REQUIRED_FIELDS(1002,"enums.ErrorCode.REQUIRED_FIELDS"),					//필수 파라미터 누락
	VALIDATION_FAILED(1003,"enums.ErrorCode.VALIDATION_FAILED"),				//유효성 검증 실패
	
	// 사용자 2000~3000
	NOT_FOUND_USER_INFO(2001, "enums.ErrorCode.NOT_FOUND_USER_INFO"),
	MNGR_STTS_NOT_NORMAL(2002, "enums.ErrorCode.MNGR_STTS_NOT_NORMAL"),
	PERMISSION_DENIED(2003, "enums.ErrorCode.PERMISSION_DENIED"), 				// 허가가 거부 되었습니다.
	
	// 조사 관련 3000~4000
	IN_PROGRESS_OR_COMPLETE(3001,"enums.ErrorCode.IN_PROGRESS_OR_COMPLETE"),
	
	// 엑셀 관련 8000~8999
	EXCEL_DATA_IS_NULL(8000,"enums.ErrorCode.EXCEL_DATA_IS_NULL"),
	EXCEL_SHEET_TOO_MANY(8001,"enums.ErrorCode.EXCEL_SHEET_TOO_MANY"),
	EXCEL_CELL_TOO_MANY(8002,"enums.ErrorCode.EXCEL_CELL_TOO_MANY"),
	EXCEL_DATA_PARSE_FAILED(8003,"enums.ErrorCode.EXCEL_DATA_PARSE_FAILED"),
	EXCEL_LOCAL_INFO_INVALID(8004,"enums.ErrorCode.EXCEL_LOCAL_INFO_INVALID"),
	
	//공통 9000 ~ 9999
	ENUM_CONVERTER_FAIL(9001, "enums.ErrorCode.ENUM_CONVERTER_FAIL"),
	DUPLICATION_DATA(9002, "enums.ErrorCode.DUPLICATION_DATA"),
	EMPTY_DATA(9003, "enums.ErrorCode.EMPTY_DATA"),
	ENTITY_SAVE_FAILED(9004, "enums.ErrorCode.ENTITY_SAVE_FAILED"),
	ENTITY_DELETE_FAILED(9005, "enums.ErrorCode.ENTITY_DELETE_FAILED"),
	FILE_DOWNLOAD_FAILED(9006, "enums.ErrorCode.FILE_DOWNLOAD_FAILED"),
	FILE_UPLOAD_FAILED(9007, "enums.ErrorCode.FILE_UPLOAD_FAILED"),
	FILE_EXTENSION_NOT_AVAILABLE(9008, "enums.ErrorCode.FILE_EXTENSION_NOT_AVAILABLE"),
	FILE_NOT_FOUND(9009,"enums.ErrorCode.FILE_NOT_FOUND"),
	POPULATION_DATA_INSERT_FAILED(9010, "enums.ErrorCode.POPULATION_DATA_INSERT_FAILED"),
	CANNOT_BE_CAST_ENTITY(9011, "enums.ErrorCode.CANNOT_BE_CAST_ENTITY"),
	ENTITY_DATA_NOT_FOUND(9012, "enums.ErrorCode.ENTITY_DATA_NOT_FOUND"),
	DATA_PARSE_FAILED(9013, "enums.ErrorCode.DATA_PARSE_FAILED"),
	EXCEL_DOWNLOAD_FAILED(9014, "enums.ErrorCode.DATA_PARSE_FAILED"),
	UNKNOWN_ERROR(9999, "enums.ErrorCode.UNKNOWN_ERROR")
	;
	
	private int status;
	private String message;
	
	public String getMessage() {
		return CommonUtils.getMessage(message);
	}
	public String getMessage(Object[] args) {
		return CommonUtils.getMessage(message, args);
	}
	
}
