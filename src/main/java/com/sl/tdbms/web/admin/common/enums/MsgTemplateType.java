package com.sl.tdbms.web.admin.common.enums;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum MsgTemplateType {

	JOIN_COMPLETE("/email/joinComplete","enums.MsgTemplateType.JOIN_COMPLETE"),
	FIND_PASSWORD("/email/findPassword","enums.MsgTemplateType.FIND_PASSWORD")
	;
	
	private String path;
	private String title;
	
	private MsgTemplateType(String path, String title) {
		this.path = path;
		this.title = title;
	}
	
	public String getTitle() {
		return CommonUtils.getMessage(title);
	}
	
}