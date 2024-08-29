package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 이동형장비 년 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsMvmneqYy extends BaseEntity{
	@Id
	private String statsYy;			// 통계년
	private String deviceId;		// 장치 아이디
	private String instllcId;		// 설치 위치 아아디
	private String vhclDrct;		// 차량 방향
	private String vhclClsf;		// 차량 분류
	private long trfvlm;			// 교통량
	private long avgSpeed;			// 평균 속도
	private LocalDateTime registDt;	// 등록일시
	private String registId;		// 등록 아이디
	private LocalDateTime updtDt;	// 수정일시
	private String updtId;			// 수정 아이디
}
