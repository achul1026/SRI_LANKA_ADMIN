package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationStatsDTO;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 경제 활동 인구 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsEcnmcActPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsDivision;
	private String dsdId;
    @Column(name = "total_population_aged_15_and_above")
    private BigDecimal totalPopulationAged15AndAbove;
    private BigDecimal employed;
    private BigDecimal unemployed;
    private BigDecimal economicallyNotActive;
	
	public TsEcnmcActPop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.dsDivision = populationStatsDTO.getDsDivision();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
		this.totalPopulationAged15AndAbove = populationStatsDTO.getTotalPopulationAged15AndAbove();
		this.employed = populationStatsDTO.getEmployed();
		this.unemployed = populationStatsDTO.getUnemployed();
		this.economicallyNotActive = populationStatsDTO.getEconomicallyNotActive();
	}
}
