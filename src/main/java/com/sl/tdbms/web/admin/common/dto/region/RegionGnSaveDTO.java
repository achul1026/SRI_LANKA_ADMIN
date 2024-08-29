package com.sl.tdbms.web.admin.common.dto.region;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionGnSaveDTO {
	private String dstrctCd;
	List<GnInfo> gnInfoArr;
	
	@Data
	@NoArgsConstructor
	public static class GnInfo {
		private String gnId;
		private BigDecimal dstrbCnt;
	}
}
