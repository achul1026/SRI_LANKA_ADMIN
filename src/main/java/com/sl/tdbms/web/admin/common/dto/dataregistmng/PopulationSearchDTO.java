package com.sl.tdbms.web.admin.common.dto.dataregistmng;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public class PopulationSearchDTO extends SearchCommonDTO {
	private PopStatsTypeCd popStatTypeCd;
	private String bffltdCd;
}
