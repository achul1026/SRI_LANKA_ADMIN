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
 * 학생 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsStdntPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsDivision;
	private String dsdId;
    private BigDecimal primarySchoolStudents;
    private BigDecimal secondarySchoolStudents;
    private BigDecimal gceOlStudents;
    private BigDecimal gceAlStudents;
    private BigDecimal degreeAndAboveStudents;
    private BigDecimal noSchooling;

    public TsStdntPop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.dsDivision = populationStatsDTO.getDsDivision();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
		this.primarySchoolStudents = populationStatsDTO.getPrimarySchoolStudents();
		this.secondarySchoolStudents = populationStatsDTO.getSecondarySchoolStudents();
		this.gceOlStudents = populationStatsDTO.getGceOlStudents();
		this.gceAlStudents = populationStatsDTO.getGceAlStudents();
		this.degreeAndAboveStudents = populationStatsDTO.getDegreeAndAboveStudents();
		this.noSchooling = populationStatsDTO.getNoSchooling();
	}
}
