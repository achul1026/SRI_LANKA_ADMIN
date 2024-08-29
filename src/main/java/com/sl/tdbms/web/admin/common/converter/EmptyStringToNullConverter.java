package com.sl.tdbms.web.admin.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

@Converter
public class EmptyStringToNullConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String param) {
		return CommonUtils.isNull(param) ? null : param;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData;
	}
}