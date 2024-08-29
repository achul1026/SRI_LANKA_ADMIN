package com.sl.tdbms.web.admin.common.dto.bbs;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BbsInfoFile {
	
	private String bbsId;
    private String bbsType;
    private String bbsTypeNm;
    private String bbsTitle;
    private String bbsCnts;
    private String dspyYn;
    private String registId;
    private LocalDateTime registDt;
    
    private List<Files> subFiles;
    
    @Data
	public static class Files {
    	private String fileId;
    	private String orgFilenm;
	}
}
