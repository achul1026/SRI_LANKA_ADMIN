package com.sl.tdbms.web.admin.common.dto.common;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommonDTO {
	
	 private LocalDateTime registDt;
	 private String registId;
	 private LocalDateTime updtDt;
	 private String updtId;
}
