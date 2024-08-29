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
 * 지역별 총 생산량 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsGrossRegnProdPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String province;
	private String provinCd;
    private BigDecimal agriculture;
    private BigDecimal industry;
    private BigDecimal services;
    private BigDecimal gdpAtCurrentMarketPrice;
    private String years;
	
	public TsGrossRegnProdPop(PopulationStatsDTO populationStatsDTO,String provinCd) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.popstatId = CommonUtils.getUuid();
        this.province = populationStatsDTO.getProvince();
        this.provinCd = provinCd;
        this.agriculture = populationStatsDTO.getAgriculture();
        this.industry = populationStatsDTO.getIndustry();
        this.services = populationStatsDTO.getServices();
        this.gdpAtCurrentMarketPrice = populationStatsDTO.getGdpAtCurrentMarketPrice();
        this.years = populationStatsDTO.getYears();
	}
}
