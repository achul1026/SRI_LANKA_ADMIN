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
 * pirivena 지역 별 학교 현황 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsPirivenaSchoolPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String provinCd;
	private String districtCd;
    private BigDecimal numberOfPirivenaMulika;
    private BigDecimal numberOfPirivenaMaha;
    private BigDecimal numberOfPirivenaVidyaYathana;
    private BigDecimal numberOfStudentsClergy;
    private BigDecimal numberOfStudentsLaity;
    private BigDecimal approvedTeachersClergy;
    private BigDecimal approvedTeachersLaity;
    private BigDecimal unapprovedTeachersClergy;
    private BigDecimal unapprovedTeachersLaity;
    
    public TsPirivenaSchoolPop(PopulationStatsDTO populationStatsDTO, String provinCd ,String districtCd) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.provinCd = provinCd;
        this.districtCd = districtCd;
        this.district = populationStatsDTO.getDistrict();
        this.numberOfPirivenaMulika = populationStatsDTO.getNumberOfPirivenaMulika();
        this.numberOfPirivenaMaha = populationStatsDTO.getNumberOfPirivenaMaha();
        this.numberOfPirivenaVidyaYathana = populationStatsDTO.getNumberOfPirivenaVidyaYathana();
        this.numberOfStudentsClergy = populationStatsDTO.getNumberOfStudentsClergy();
        this.numberOfStudentsLaity = populationStatsDTO.getNumberOfStudentsLaity();
        this.approvedTeachersClergy = populationStatsDTO.getApprovedTeachersClergy();
        this.approvedTeachersLaity = populationStatsDTO.getApprovedTeachersLaity();
        this.unapprovedTeachersClergy = populationStatsDTO.getUnapprovedTeachersClergy();
        this.unapprovedTeachersLaity = populationStatsDTO.getUnapprovedTeachersLaity();
    }
}
