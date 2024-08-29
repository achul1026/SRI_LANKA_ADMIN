package com.sl.tdbms.web.admin.common.enums;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

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
//	QSTN_TYPE_CD("QSTN_TYPE_CD","설문 질문 코드"),
	BBS_TYPE_CD("BBS_TYPE_CD","게시글 타입 코드"),
	FAQ_TYPE_CD("FAQ_TYPE_CD","FAQ 타입 코드"),
	SRVY_METADATA_CD("SRVY_METADATA_CD","설문 메타데이터 코드"),
	API_SRVC_CLSF_CD("API_SRVC_CLSF_CD","API 서비스 분류"),
	API_ITEM_TYPE_CD("API_ITEM_TYPE_CD","API 항목 유형"),
	FACILITY_TYPE_CD("FACILITY_TYPE_CD","시설물 타입 코드"),
	DRCT_CD("DRCT_CD","방향 코드"),
	TRF_DRCT_CD("TRF_DRCT_CD","교통량 방향 코드"),
	VDS_VHCL_TYPE_CD("VDS_VHCL_TYPE_CD","VDS 차종 코드"),
	METROVHCL_TYPE_CD("METROVHCL_TYPE_CD","메트로카운트 차종 코드"),
	MVMNMEAN_TYPE_CD("MVMNMEAN_TYPE_CD","이동수단 타입"),
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
