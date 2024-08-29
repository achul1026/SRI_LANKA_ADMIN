package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;

import lombok.Data;

@Data
@MappedSuperclass // BaseEntity를 상속한 엔티티들은 아래 필드들을 컬럼으로 인식하게 된다.
@EntityListeners(AuditingEntityListener.class)  // Auditing(자동으로 값 매핑) 기능 추가
public class BaseEntity {
	
	@Column(length = 32, updatable = false)
	private String registId = CommonUtils.isNull(LoginMngrUtils.getUserId()) ? null : LoginMngrUtils.getUserId();
	
	@CreatedDate
	@Column(updatable = false ,nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime registDt;
	
	@Column(length = 32)
	private String updtId;
	
	@LastModifiedDate
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime updtDt;
	
}
