package com.sl.tdbms.web.admin.common.dto.statistics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TsPopulationFileDTO {
    private String popmngId;
    private String orgFileNm;
    private String filePath;
    private String fileNm;
    private BigDecimal fileSize;
}
