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
 * 지역 별 사립 학교 현황 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsPrivateSchoolPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String province;
	private String district;
	private String provinCd;
	private String districtCd;
    @Column(name = "schools_by_type_1ab")
    private BigDecimal schoolsByType1AB;
    @Column(name = "schools_by_type_1ac")
    private BigDecimal schoolsByType1AC;
    @Column(name = "schools_by_type_2")
    private BigDecimal schoolsByType2;
    private BigDecimal maleStudents;
    private BigDecimal femaleStudents;
    private BigDecimal studentsSinhalaMedium;
    private BigDecimal studentsTamilMedium;
    private BigDecimal studentsEnglishMedium;
    private BigDecimal maleTeachers;
    private BigDecimal femaleTeachers;
    
    public TsPrivateSchoolPop(PopulationStatsDTO populationStatsDTO, String provinCd,String districtCd) {
    	this.province = populationStatsDTO.getProvince();
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.provinCd = provinCd;
        this.districtCd = districtCd;
        this.district = populationStatsDTO.getDistrict();
        this.schoolsByType1AB = populationStatsDTO.getSchoolsByType1AB();
        this.schoolsByType1AC = populationStatsDTO.getSchoolsByType1AC();
        this.schoolsByType2 = populationStatsDTO.getSchoolsByType2();
        this.maleStudents = populationStatsDTO.getMaleStudents();
        this.femaleStudents = populationStatsDTO.getFemaleStudents();
        this.studentsSinhalaMedium = populationStatsDTO.getStudentsSinhalaMedium();
        this.studentsTamilMedium = populationStatsDTO.getStudentsTamilMedium();
        this.studentsEnglishMedium = populationStatsDTO.getStudentsEnglishMedium();
        this.maleTeachers = populationStatsDTO.getMaleTeachers();
        this.femaleTeachers = populationStatsDTO.getFemaleTeachers();
    }
}
