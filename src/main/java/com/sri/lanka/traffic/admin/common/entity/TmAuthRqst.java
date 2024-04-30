package com.sri.lanka.traffic.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sri.lanka.traffic.admin.common.enums.code.RqstSttsCd;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;

@Entity
@Data //권한 관리
@EntityListeners(AuditingEntityListener.class)
public class TmAuthRqst {

    @Id
    private String authrqstId = CommonUtils.getUuid(); //권한요청 아이디
    
    private String authgrpId; //권한그룹 아이디
    
    private String usermngId; //요청자 아이디

    private String rqstRsn; //요청 사유
    
    @CreatedDate
	@Column(updatable = false ,nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime rqstDt; //요청 일시
    
    @Convert(converter = RqstSttsCd.Converter.class)
    private RqstSttsCd rqstStts = RqstSttsCd.WAITING; //요청 상태
    
    private String userId; //승인 아이디
    
    private LocalDateTime athrztDt; //승인 일시

}   
