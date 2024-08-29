package com.sl.tdbms.web.admin.common.dto.datalink;

import java.util.List;

import lombok.Data;

@Data
public class ApiKeySrvcSaveDTO {
    private List<String> addSrvcArr;
    private String certkeyId;
}
