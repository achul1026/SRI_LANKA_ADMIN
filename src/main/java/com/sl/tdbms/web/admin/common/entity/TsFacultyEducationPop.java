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
 * 지역별 교원수 통계 - 교육
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsFacultyEducationPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsdId;
    private String educationZone;
    private String educationDivision;
    private BigDecimal nationalSchools;
    private BigDecimal provincialSchools;
    private BigDecimal maleStudents;
    private BigDecimal femaleStudents;
    private BigDecimal teachers;
    private BigDecimal studentTeacherRatio;
	
	public TsFacultyEducationPop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
        this.educationZone = populationStatsDTO.getEducationZone();
        this.educationDivision = populationStatsDTO.getEducationDivision();
        this.nationalSchools = populationStatsDTO.getNationalSchools();
        this.provincialSchools = populationStatsDTO.getProvincialSchools();
        this.maleStudents = populationStatsDTO.getMaleStudents();
        this.femaleStudents = populationStatsDTO.getFemaleStudents();
        this.teachers = populationStatsDTO.getTeachers();
        this.studentTeacherRatio = populationStatsDTO.getStudentTeacherRatio();	
    }
}
