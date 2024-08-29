package com.sl.tdbms.web.admin.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AXLELOAD 년 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsAxleloadYy extends BaseEntity{
	@Id
	private String statsYy;
	private String dataType;
	private String instllcId;
	private String vhclClsf;
	private int axNum;
	private int cnt;
	private LocalDateTime registDt;	// 등록일시
	private String registId;		// 등록 아이디
	private LocalDateTime updtDt;	// 수정일시
	private String updtId;			// 수정 아이디
}