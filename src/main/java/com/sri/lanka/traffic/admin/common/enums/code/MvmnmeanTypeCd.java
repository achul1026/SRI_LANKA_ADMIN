package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;

import lombok.Getter;

@Getter
public enum MvmnmeanTypeCd implements CommonEnumType<String> {
	
	MCL("MTC000","MCL"),
	TWL("MTC001","TWL"),
	CAR("MTC002","CAR"),
	VAN("MTC003","VAN"),
	MBU("MTC004","MBU"),
	LBU("MTC005","LBU"),
	LGV("MTC006","LGV"),
	MG1("MTC007","MG1"),
	MG2("MTC008","MG2"),
	HG3("MTC009","HG3"),
	AG3("MTC010","AG3"),
	AG4("MTC011","AG4"),
	AG5("MTC012","AG5"),
	AG6("MTC013","AG6"),
	FVH("MTC014","FVH");
	
	private String code; 
	private String name;
	
	MvmnmeanTypeCd(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return name;
	}
	
	public static class Converter extends EnumConverter<MvmnmeanTypeCd, String> {
        public Converter() {
            super(MvmnmeanTypeCd.class);
        }
    }
}
