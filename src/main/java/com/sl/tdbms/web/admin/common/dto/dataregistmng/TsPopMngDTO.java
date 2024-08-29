package com.sl.tdbms.web.admin.common.dto.dataregistmng;

import java.time.LocalDateTime;

import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;

import lombok.Data;

@Data
public class TsPopMngDTO {
	private String popmngId;
	private String fileId;
	private String orgFilenm;
	private String popmngTitle;
	private PopStatsTypeCd popmngType;
	private String popmngCnts;
	private String userBffltd;
	private String userBffltdNm;
    private String startYear;
    private String endYear;
	private LocalDateTime registDt;
	private String registId;
}
