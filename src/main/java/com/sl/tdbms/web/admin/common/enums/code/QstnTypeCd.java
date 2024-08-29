package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import lombok.Getter;

@Getter
public enum QstnTypeCd implements CommonEnumType<String> {
	
	SUBJECTIVE("QTC001","enums.QstnTypeCd.SUBJECTIVE","SINGLE","/images/quest_text.png","enums.QstnTypeCd.SUBJECTIVE.desc"),
	RADIO("QTC002","enums.QstnTypeCd.RADIO","MULTIPLE",null,null),
	CHECKBOX("QTC003","enums.QstnTypeCd.CHECKBOX","MULTIPLE",null,null),
	TIME("QTC004","enums.QstnTypeCd.TIME","SINGLE","/images/quest_time.png","enums.QstnTypeCd.TIME.desc"),
	LOCATION("QTC005","enums.QstnTypeCd.LOCATION","SINGLE","/images/quest_location.png","enums.QstnTypeCd.LOCATION.desc"),
	SELECTBOX("QTC006","enums.QstnTypeCd.SELECTBOX","MULTIPLE",null,null),
	DATE("QTC007","enums.QstnTypeCd.DATE","SINGLE","/images/quest_date.png","enums.QstnTypeCd.DATE.desc"),
	GPS("QTC008","enums.QstnTypeCd.GPS","SINGLE","/images/quest_location.png","enums.QstnTypeCd.GPS.desc"),
	;
	
	private String code; 
	private String name;
	private String type;
	private String imgPath;
	private String desc;
	
	QstnTypeCd(String code, String name, String type, String imgPath, String desc) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.imgPath = imgPath;
		this.desc = desc;
	}
	
	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return CommonUtils.getMessage(name);
	}
	
	public String getDesc() {
		return CommonUtils.getMessage(desc);
	}

	public static QstnTypeCd getEnums(String code) {
		if(!CommonUtils.isNull(code)) {
			for(QstnTypeCd r : QstnTypeCd.values()) {
				if(r.code.equals(code)) {
					return r;
				}
			}
		}
		return null;
	}

	public static class Converter extends EnumConverter<QstnTypeCd, String> {
        public Converter() {
            super(QstnTypeCd.class);
        }
    }
}
