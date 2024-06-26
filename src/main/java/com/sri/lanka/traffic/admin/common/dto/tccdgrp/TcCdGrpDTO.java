package com.sri.lanka.traffic.admin.common.dto.tccdgrp;

import com.sri.lanka.traffic.admin.common.dto.common.CommonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TcCdGrpDTO extends CommonDTO {
	
	private String grpcdId;
	private String grpCd;
	private String grpcdNm;
	private String useYn;
	
}
