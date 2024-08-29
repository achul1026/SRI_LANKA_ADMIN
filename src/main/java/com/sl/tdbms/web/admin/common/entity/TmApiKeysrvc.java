package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //OPENAPI 인증키 서비스 관리
@NoArgsConstructor
public class TmApiKeysrvc {

    @Id
    private String srvcId; //서비스 아이디
    
    private String certkeyId; //인증 키 아이디

}
