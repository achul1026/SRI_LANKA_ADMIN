package com.sl.tdbms.web.admin.common.dto.invst;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeccSrvyRsltDTO {
    String srvyansId;
    String qstnTitle;
    String ansCnts;
    BigDecimal sectSqno;
    String sectType;
    BigDecimal qstnSqno;
    String srvyMetadataCd;
    String qstnType;
    String etcYn;
    List<String> ansList;
}
