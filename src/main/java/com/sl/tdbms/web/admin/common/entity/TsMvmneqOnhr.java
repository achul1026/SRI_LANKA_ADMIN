package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 이동형장비 1시간 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsMvmneqOnhr extends BaseEntity{
	@Id
	private String statsYymmdt;	
	private String deviceId;
	private String instllcId;			
	private String vhclDrct;		
	private String vhclClsf;		
	private long trfvlm;			
	private long avgSpeed;		
}
