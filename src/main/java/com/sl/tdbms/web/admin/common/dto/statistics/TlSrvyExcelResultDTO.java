package com.sl.tdbms.web.admin.common.dto.statistics;

import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TlSrvyExcelResultDTO {
    private ExmnTypeCd exmnType;
    private String pollsterNm;
    private String pollsterTel;
    private String ansCntsArray;
    private String cordonLine;
    private String tollBooth;
}
