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
 * 기타 산업 시설 현황 - 비공식 비농업
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsIdstryFacInformalPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String provinCd;
	private String districtCd;
	private BigDecimal industryNumber;
    private BigDecimal industryPercentage;
    private BigDecimal tradeNumber;
    private BigDecimal tradePercentage;
    private BigDecimal servicesNumber;
    private BigDecimal servicesPercentage;
    
    public TsIdstryFacInformalPop(PopulationStatsDTO populationStatsDTO,String provinCd,String districtCd) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.district = populationStatsDTO.getDistrict();
        this.provinCd = provinCd;
        this.districtCd = districtCd;
        this.industryNumber = populationStatsDTO.getIndustryNumber();
        this.industryPercentage = populationStatsDTO.getIndustryPercentage();
        this.tradeNumber = populationStatsDTO.getTradeNumber();
        this.tradePercentage = populationStatsDTO.getTradePercentage();
        this.servicesNumber = populationStatsDTO.getServicesNumber();
        this.servicesPercentage = populationStatsDTO.getServicesPercentage();  
    }
}
