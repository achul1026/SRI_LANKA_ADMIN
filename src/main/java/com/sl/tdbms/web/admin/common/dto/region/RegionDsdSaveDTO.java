package com.sl.tdbms.web.admin.common.dto.region;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionDsdSaveDTO {
	private String dsdId;
	List<TazInfo> tazInfoArr;
	
	@Data
	@NoArgsConstructor
	public static class TazInfo {
		private String dstrctCd;
		private BigDecimal dstrbCnt;
	}
}
