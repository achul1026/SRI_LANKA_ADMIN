package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //OPENAPI 서비스 관리
@NoArgsConstructor
public class TmApiSrvc {

	@Id
    private String srvcId; //서비스 아이디
    
    private String srvcNm; //서비스 명
    
    private String srvcDescr; //서비스 설명
    
    private String srvcClsf; //서비스 분류
    
    private String pvsnInst; //제공 기관
    
    private String mngrId; //관리자 아이디
    
    private String srvcUrl; //서비스 url
    
    private String useYn; //사용 여부

    //noneCol
    @Transient
    private String mngrNm;

    @CreatedDate
    @Column(updatable = false ,nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime registDt; //등록 일시
    
    public TmApiSrvc(OpenApiDataDTO openApiRegistDTO) {
        BeanUtils.copyProperties(openApiRegistDTO, this);
    }
}
