package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsMvmneqDd extends BaseEntity{
	@Id
	private String statsYymmdd;		
	private String deviceId;
	private String instllcId;			
	private String vhclDrct;		
	private String vhclClsf;		
	private long trfvlm;			
	private long avgSpeed;		
}
