package com.sl.tdbms.web.admin.common.dto.statistics;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TlSrvyExcelDTO {
	private String srvyrsltId;
    private List<BigDecimal> sectSqnoArr;
    
	public TlSrvyExcelDTO(String srvyrsltId, List<BigDecimal> sectSqnoArr) {
		super();
		this.srvyrsltId = srvyrsltId;
		this.sectSqnoArr = sectSqnoArr;
	}
}
