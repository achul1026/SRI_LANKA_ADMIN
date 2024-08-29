package com.sl.tdbms.web.admin.common.dto.gis;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class StatisticsGISSearchMetroCountDetailDTO {

	private String instllcId;
	private String searchStartDt;
	private String searchEndDt;
	private String searchVhclDrctCd;

	public void setSearchVhclDrctCd(String searchVhclDrctCd) {
		this.searchVhclDrctCd = searchVhclDrctCd;
		if(CommonUtils.isNull(searchVhclDrctCd)) {
			this.searchVhclDrctCd = null;
		}
	}

}
