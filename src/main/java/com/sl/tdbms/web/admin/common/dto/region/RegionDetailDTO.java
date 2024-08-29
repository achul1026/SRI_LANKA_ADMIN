package com.sl.tdbms.web.admin.common.dto.region;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RegionDetailDTO {
	private String gnId;
	private String dsdId;
	private String dsdCd;
	private String provinCd;
	private String provinNm;
	private String dsdNm;
	private String districtCd;
	private String districtNm;
	private String dstrctCd;
	private BigDecimal cntrLat;
	private BigDecimal cntrLon;
	
	List<TazCodeInfo> tazCodeInfoList;
	List<GnCodeInfo> gnCodeInfoList;
	
	@Data
	public static class TazCodeInfo{
		private String dstrctId;
		private String dstrctGis;
		private String dstrctCd;
		private BigDecimal cntrLat;
		private BigDecimal cntrLon;
		private BigDecimal distrbCnt;
	}
	
	@Data
	public static class GnCodeInfo{
		private String gnId;
		private String gnNm;
		private String provinNm;
		private String districtNm;
		private String dsdNm;
		private BigDecimal distrbCnt;
	}
}
