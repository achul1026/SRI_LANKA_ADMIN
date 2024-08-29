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
 * 전국 지역 별 학교 현황 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsNationalSchoolPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
    private String educationZone;
    private String dsdId;
    private BigDecimal totalShools;
    private BigDecimal nationalSchools;
    private BigDecimal provincialSchools;
    @Column(name = "schools_by_type_1ab")
    private BigDecimal schoolsByType1AB;
    @Column(name = "schools_by_type_1c")
    private BigDecimal schoolsByType1C;
    @Column(name = "schools_by_type_2")
    private BigDecimal schoolsByType2;
    @Column(name = "schools_by_type_3")
    private BigDecimal schoolsByType3;
    @Column(name = "student_population_1to50")
    private BigDecimal studentPopulation1to50;
    @Column(name = "student_population_51to100")
    private BigDecimal studentPopulation51to100;
    @Column(name = "student_population_101to200")
    private BigDecimal studentPopulation101to200;
    @Column(name = "student_population_201to500")
    private BigDecimal studentPopulation201to500;
    @Column(name = "student_population_501to1000")
    private BigDecimal studentPopulation501to1000;
    @Column(name = "student_population_above_1000")
    private BigDecimal studentPopulationAbove1000;
    
    public TsNationalSchoolPop(PopulationStatsDTO populationStatsDTO,String dsdId) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.district = populationStatsDTO.getDistrict();
        this.educationZone = populationStatsDTO.getEducationZone();
        this.dsdId = dsdId;
        this.totalShools = populationStatsDTO.getTotalShools();
        this.nationalSchools = populationStatsDTO.getNationalSchools();
        this.provincialSchools = populationStatsDTO.getProvincialSchools();
        this.schoolsByType1AB = populationStatsDTO.getSchoolsByType1AB();
        this.schoolsByType1C = populationStatsDTO.getSchoolsByType1C();
        this.schoolsByType2 = populationStatsDTO.getSchoolsByType2();
        this.schoolsByType3 = populationStatsDTO.getSchoolsByType3();
        this.studentPopulation1to50 = populationStatsDTO.getStudentPopulation1to50();
        this.studentPopulation51to100 = populationStatsDTO.getStudentPopulation51to100();
        this.studentPopulation101to200 = populationStatsDTO.getStudentPopulation101to200();
        this.studentPopulation201to500 = populationStatsDTO.getStudentPopulation201to500();
        this.studentPopulation501to1000 = populationStatsDTO.getStudentPopulation501to1000();
        this.studentPopulationAbove1000 = populationStatsDTO.getStudentPopulationAbove1000();
    }
}
