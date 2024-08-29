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
 * 학교 시스템별 교사 학생수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsSchoolSystemPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsdId;
    private String educationZone;
    private BigDecimal nationalStudents;
    private BigDecimal provincialStudents;
    @Column(name = "schools_by_type_1ab")
    private BigDecimal schoolsByType1AB;
    @Column(name = "schools_by_type_1c")
    private BigDecimal schoolsByType1C;
    @Column(name = "schools_by_type_2")
    private BigDecimal schoolsByType2;
    @Column(name = "schools_by_type_3")
    private BigDecimal schoolsByType3;
    private BigDecimal male;
    private BigDecimal female;
    private BigDecimal studentTeacherRatio;
	
	public TsSchoolSystemPop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
        this.educationZone = populationStatsDTO.getEducationZone();
        this.nationalStudents = populationStatsDTO.getNationalStudents();
        this.provincialStudents = populationStatsDTO.getNationalStudents();
        this.schoolsByType1AB = populationStatsDTO.getSchoolsByType1AB();
        this.schoolsByType1C = populationStatsDTO.getSchoolsByType1C();
        this.schoolsByType2 = populationStatsDTO.getSchoolsByType2();
        this.schoolsByType3= populationStatsDTO.getSchoolsByType3();
        this.male = populationStatsDTO.getMale();
        this.female = populationStatsDTO.getFemale();
        this.studentTeacherRatio = populationStatsDTO.getStudentTeacherRatio();
    }
}
