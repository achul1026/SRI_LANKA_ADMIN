package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * VDS 월 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TsVdsMm extends BaseEntity{
	@Id
	private String statsYymm;		// 통계 월
	private String cameraId;		// 카메라 아이디
	private String instllcId;		// 장비 아이디
	private String vhclClsf;		// 차량분류
	private long trfvlm;		// 교통량
	private long avgSpeed;		// 평균속도
	private LocalDateTime registDt;	// 등록일시
	private String registId;		// 등록 아이디
	private LocalDateTime updtDt;	// 수정일시
	private String updtId;			// 수정 아이디
}
