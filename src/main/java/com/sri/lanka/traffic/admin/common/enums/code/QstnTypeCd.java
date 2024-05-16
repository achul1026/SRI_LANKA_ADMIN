package com.sri.lanka.traffic.admin.common.enums.code;

import com.sri.lanka.traffic.admin.common.converter.EnumConverter;
import com.sri.lanka.traffic.admin.common.enums.code.common.CommonEnumType;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import lombok.Getter;

@Getter
public enum QstnTypeCd implements CommonEnumType<String> {
	
	SUBJECTIVE("QTC001","주관식","SINGLE","/images/quest_text.png","주관식 답변란"),
	RADIO("QTC002","라디오","MULTIPLE",null,null),
	CHECKBOX("QTC003","체크박스","MULTIPLE",null,null),
	TIME("QTC004","시간","SINGLE","/images/quest_time.png","Time"),
	LOCATION("QTC005","위치","SINGLE","/images/quest_location.png","Location"),
	SELECTBOX("QTC006","드롭다운","MULTIPLE",null,null),
	DATE("QTC007","날짜","SINGLE","/images/quest_date.png","YYYY-MM-DD"),
//	SEARCH("QTC008","검색","SINGLE","/images/quest_date.png","search"),//TODO::검색 이미지 필요
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
