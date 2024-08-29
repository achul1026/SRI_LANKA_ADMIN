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
 * 기타 산업 시설 현황 - 형식
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsIdstryFacFormalPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String provinCd;
	private String districtCd;
    private BigDecimal numberOfEstablishments;
    private BigDecimal personsEngaged;
    private BigDecimal employees;
    private BigDecimal salariesAndWages;
    private BigDecimal valueOfOutput;
    private BigDecimal valueOfIntermediateConsumptions;
    private BigDecimal valueAdded;
    private BigDecimal grossAdditionsToFixedAssets;
    
    public TsIdstryFacFormalPop(PopulationStatsDTO populationStatsDTO, String provinCd, String districtCd) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.provinCd = provinCd;
        this.districtCd = districtCd;
        this.district = populationStatsDTO.getDistrict();
        this.numberOfEstablishments = populationStatsDTO.getNumberOfEstablishments();
        this.personsEngaged = populationStatsDTO.getPersonsEngaged();
        this.employees = populationStatsDTO.getEmployees();
        this.salariesAndWages = populationStatsDTO.getSalariesAndWages();
        this.valueOfOutput = populationStatsDTO.getValueOfOutput();
        this.valueOfIntermediateConsumptions = populationStatsDTO.getValueOfIntermediateConsumptions();
        this.valueAdded = populationStatsDTO.getValueAdded();
        this.grossAdditionsToFixedAssets = populationStatsDTO.getGrossAdditionsToFixedAssets();
    }
}
