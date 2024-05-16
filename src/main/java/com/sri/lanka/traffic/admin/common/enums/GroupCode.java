package com.sri.lanka.traffic.admin.common.enums;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum GroupCode {
	
	TRFC_INVST_KND_CD("TRFC_INVST_KND_CD","교통 조사 종류 코드"),
	SRVY_INVST_KND_CD("SRVY_INVST_KND_CD","설문 조사 종류 코드"),
	INVST_KND_CD("INVST_KND_CD","조사 종류 코드"),
	MNGR_STTS_CD("MNGR_STTS_CD","관리자 상태 코드"),
	BFFLTD_CD("BFFLTD_CD","소속 코드"),
	DEPT_CD("DEPT_CD","부서 코드"),
	MAIN_MENU_CD("MAIN_MENU_CD","메인 메뉴 코드"),
	SUB_MENU_CD("SUB_MENU_CD","하위 메뉴 코드"),
	INVST_QSTN_TYPE("INVST_QSTN_TYPE","조사 질문 유형"),
	SECT_TYPE_CD("SECT_TYPE_CD","설문 부문 유형 코드"),
	SRVY_TYPE_CD("SRVY_TYPE_CD","설문 유형 코드"),
	QSTN_TYPE_CD("QSTN_TYPE_CD","설문 질문 코드"),
	BBS_TYPE_CD("BBS_TYPE_CD","게시글 타입 코드"),
	;
	
	private String code;
	private String name;

	GroupCode(String code, String name){
		this.code = code;
		this.name = name;
	};
	
	public static GroupCode getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(GroupCode r : GroupCode.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}
}
