package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //메시지 관리
@EqualsAndHashCode(callSuper=true)
public class MsgQueue extends CreateEntity{

    @Id
    private String msgId = CommonUtils.getUuid(); //메시지 아이디

    private String msgType; //메시지 타입
    
    private String status; //발신 상태
    
    private String title; //발신 상태

    private String content; //내용

    private String sender; //발신자
    
    private String receiver; //수신자
    
    private BigDecimal retry; //발신 횟수
    
    private LocalDateTime sendDt; //발신 일

}
