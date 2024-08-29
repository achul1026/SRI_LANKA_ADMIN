package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationStatsDTO;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 지역 별 인구 수 통계 테이블
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsRegnPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsDivision;
	private String dsdId;
	private BigDecimal totalPopltncnt;
	
	public TsRegnPop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.dsDivision = populationStatsDTO.getDsDivision();
		this.totalPopltncnt = populationStatsDTO.getTotalPopltncnt();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
	}
}
