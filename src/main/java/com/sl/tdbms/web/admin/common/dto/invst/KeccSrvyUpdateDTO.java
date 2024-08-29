package com.sl.tdbms.web.admin.common.dto.invst;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class KeccSrvyUpdateDTO {
    String srvyrsltId;
    List<KeccSrvyAns> ansList;

    @Data
    public static class KeccSrvyAns{
        String ansCnts;
        String etcYn;
        String srvyansId;
    }
}
