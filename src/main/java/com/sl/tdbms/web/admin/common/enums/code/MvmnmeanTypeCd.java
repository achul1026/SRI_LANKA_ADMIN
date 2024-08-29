package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum MvmnmeanTypeCd implements CommonEnumType<String> {

	MCL("MTC001","enums.MvmnmeanTypeCd.MCL"),
	TWL("MTC002","enums.MvmnmeanTypeCd.TWL"),
	CAR("MTC003","enums.MvmnmeanTypeCd.CAR"),
	VAN("MTC004","enums.MvmnmeanTypeCd.VAN"),
	MBU("MTC005","enums.MvmnmeanTypeCd.MBU"),
	LBU("MTC006","enums.MvmnmeanTypeCd.LBU"),
	LGV("MTC007","enums.MvmnmeanTypeCd.LGV"),
	MG1("MTC008","enums.MvmnmeanTypeCd.MG1"),
	MG2("MTC009","enums.MvmnmeanTypeCd.MG2"),
	HG3("MTC010","enums.MvmnmeanTypeCd.HG3"),
	AG3("MTC011","enums.MvmnmeanTypeCd.AG3"),
	AG4("MTC012","enums.MvmnmeanTypeCd.AG4"),
	AG5("MTC013","enums.MvmnmeanTypeCd.AG5"),
	AG6("MTC014","enums.MvmnmeanTypeCd.AG6"),
	FVH("MTC015","enums.MvmnmeanTypeCd.FVH");
	
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
		return CommonUtils.getMessage(name);
	}
	
	public static class Converter extends EnumConverter<MvmnmeanTypeCd, String> {
        public Converter() {
            super(MvmnmeanTypeCd.class);
        }
    }
}
