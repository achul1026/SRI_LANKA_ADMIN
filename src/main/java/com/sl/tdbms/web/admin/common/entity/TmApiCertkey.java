package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data //OPENAPI 인증키 관리
public class TmApiCertkey {

    @Id
    private String certkeyId; //인증 키 아이디
    
    private String email; //이메일
    
    private String apiKey; //API 키
    
    private String endYmd; //종료 일자
    
    private String sttsCd; //상태 코드
    
    private LocalDateTime rqstDt; //요청 일시
    
    private LocalDateTime athrztDt; //승인 일시
    
    private String athrztId; //승인 아이디
    
    private String aplyRsn; //신청 사유

}
