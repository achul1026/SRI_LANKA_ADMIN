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
 * 연령 별 인구 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsAgePop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsDivision;
	private String dsdId;
    @Column(name = "age_group_0_to_4")
    private BigDecimal ageGroup0To4;
    @Column(name = "age_group_5_to_9")
    private BigDecimal ageGroup5To9;
    @Column(name = "age_group_10_to_14")
    private BigDecimal ageGroup10To14;
    @Column(name = "age_group_15_to_19")
    private BigDecimal ageGroup15To19;
    @Column(name = "age_group_20_to_24")
    private BigDecimal ageGroup20To24;
    @Column(name = "age_group_25_to_29")
    private BigDecimal ageGroup25To29;
    @Column(name = "age_group_30_to_34")
    private BigDecimal ageGroup30To34;
    @Column(name = "age_group_35_to_39")
    private BigDecimal ageGroup35To39;
    @Column(name = "age_group_40_to_44")
    private BigDecimal ageGroup40To44;
    @Column(name = "age_group_45_to_49")
    private BigDecimal ageGroup45To49;
    @Column(name = "age_group_50_to_54")
    private BigDecimal ageGroup50To54;
    @Column(name = "age_group_55_to_59")
    private BigDecimal ageGroup55To59;
    @Column(name = "age_group_60_to_64")
    private BigDecimal ageGroup60To64;
    @Column(name = "age_group_65_to_69")
    private BigDecimal ageGroup65To69;
    @Column(name = "age_group_70_to_74")
    private BigDecimal ageGroup70To74;
    @Column(name = "age_group_75_to_79")
    private BigDecimal ageGroup75To79;
    @Column(name = "age_group_80_to_84")
    private BigDecimal ageGroup80To84;
    @Column(name = "age_group_85_to_89")
    private BigDecimal ageGroup85To89;
    @Column(name = "age_group_90_to_94")
    private BigDecimal ageGroup90To94;
    @Column(name = "age_group_95_and_above")
    private BigDecimal ageGroup95AndAbove;
	
	public TsAgePop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.dsDivision = populationStatsDTO.getDsDivision();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
		this.ageGroup0To4 = populationStatsDTO.getAgeGroup0To4();
		this.ageGroup5To9 = populationStatsDTO.getAgeGroup5To9();        
		this.ageGroup10To14 = populationStatsDTO.getAgeGroup10To14();
		this.ageGroup15To19 = populationStatsDTO.getAgeGroup15To19();      
		this.ageGroup20To24 = populationStatsDTO.getAgeGroup20To24();     
		this.ageGroup25To29 = populationStatsDTO.getAgeGroup25To29();
		this.ageGroup30To34 = populationStatsDTO.getAgeGroup30To34();    
		this.ageGroup35To39 = populationStatsDTO.getAgeGroup35To39();    
		this.ageGroup40To44 = populationStatsDTO.getAgeGroup40To44();      
		this.ageGroup45To49 = populationStatsDTO.getAgeGroup45To49();      
		this.ageGroup50To54 = populationStatsDTO.getAgeGroup50To54();      
		this.ageGroup55To59 = populationStatsDTO.getAgeGroup55To59();      
		this.ageGroup60To64 = populationStatsDTO.getAgeGroup60To64();     
		this.ageGroup65To69 = populationStatsDTO.getAgeGroup65To69();     
		this.ageGroup70To74 = populationStatsDTO.getAgeGroup70To74();     
		this.ageGroup75To79 = populationStatsDTO.getAgeGroup75To79();     
		this.ageGroup80To84 = populationStatsDTO.getAgeGroup80To84();     
		this.ageGroup85To89 = populationStatsDTO.getAgeGroup85To89();     
		this.ageGroup90To94 = populationStatsDTO.getAgeGroup90To94();     
		this.ageGroup95AndAbove = populationStatsDTO.getAgeGroup95AndAbove();  
	}
}
