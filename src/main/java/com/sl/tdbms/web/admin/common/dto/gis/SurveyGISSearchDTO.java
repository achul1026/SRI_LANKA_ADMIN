package com.sl.tdbms.web.admin.common.dto.gis;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class SurveyGISSearchDTO {

	private String searchDsdCd;
	private String searchMngrBffltd;
	private String[] searchExmnTypeCdArr = {"ETC001","ETC002","ETC003","ETC004","ETC005"};
	
	
	public void setSearchExmnTypeCd(String searchExmnTypeCd) {
		if(!CommonUtils.isNull(searchExmnTypeCd)) {
			this.searchExmnTypeCdArr = new String[]{searchExmnTypeCd};
		}
	}
	
	public void setSearchMngrBffltd(String searchMngrBffltd) {
		this.searchMngrBffltd = searchMngrBffltd;
		if(CommonUtils.isNull(searchMngrBffltd)) {
			this.searchMngrBffltd = null;
		}
	}
	
	public void setSearchDsdCd(String searchDsdCd) {
		this.searchDsdCd = searchDsdCd;
		if(CommonUtils.isNull(searchDsdCd)) {
			this.searchDsdCd = null;
		}
	}
}
