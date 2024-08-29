package com.sl.tdbms.web.admin.common.dto.datalink;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpenApiSearchDTO extends SearchCommonDTO {
	private String bffltdCd;
}
