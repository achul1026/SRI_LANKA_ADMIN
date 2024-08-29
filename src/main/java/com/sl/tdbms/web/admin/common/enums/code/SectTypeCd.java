//package com.sl.tdbms.web.admin.common.enums.code;
//
//import com.sl.tdbms.web.admin.common.converter.EnumConverter;
//import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
//
//import lombok.Getter;
//
//@Getter
//public enum SectTypeCd implements CommonEnumType<String> {
//	
//	FURNITURE("STC001","가구 실태 조사"),
//	INDIVIDUAL("STC002","개인 실태 조사"),
//	TRAFFIC("STC003","통행 실태 조사"),
//	ROADSIDE("STC004","노측 면접 조사"),
//	AXLELOAD("STC005","AXLE LOAD 조사"),
//	;
//	
//	private String code; 
//	private String name;
//
//	SectTypeCd(String code, String name) {
//		this.code = code;
//		this.name = name;
//	}
//	
//	@Override
//    public String getCode() {
//        return code;
//    }
//	
//	@Override
//	public String getName() {
//		return name;
//	}
//	
//	public static class Converter extends EnumConverter<SectTypeCd, String> {
//        public Converter() {
//            super(SectTypeCd.class);
//        }
//    }
//}
