package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import lombok.Getter;

@Getter
public enum QstnTypeCd implements CommonEnumType<String> {
	
	SUBJECTIVE("QTC001","주관식"),
	RADIO("QTC002","라디오"),
	CHECKBOX("QTC003","체크박스"),
	TIME("QTC004","시간"),
	ADDRESS("QTC005","주소"),
	SEARCH("QTC006","검색")
	;
	
	private String code; 
	private String name;
	
	QstnTypeCd(String code, String name) {
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
