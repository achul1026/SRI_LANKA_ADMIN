package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //OPENAPI 제공이력
public class TlApiPvsn {

    @Id
    private LocalDateTime pvsnDt; //제공 일시
    
    private String certkeyId; //인증 키 아이디
    
    private String srvcId; //서비스 아이디
    
    private BigDecimal dataCnt; //데이터 수

}
