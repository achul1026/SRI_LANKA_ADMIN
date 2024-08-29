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
 * 지역별 교원수 통계 주기
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsFacultyCyclePop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsdId;
    private String educationZone;
    @Column(name = "cycle_g1_to_5")
    private BigDecimal cycleG1To5;
    @Column(name = "cycle_g6_to_11")
    private BigDecimal cycleG6To11;
    @Column(name = "cycle_g12_to_13")
    private BigDecimal cycleG12To13;
	
	public TsFacultyCyclePop(PopulationStatsDTO populationStatsDTO , String dsdId) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.popstatId = CommonUtils.getUuid();
		this.dsdId = dsdId;
        this.educationZone = populationStatsDTO.getEducationZone();
        this.cycleG1To5 = populationStatsDTO.getCycleG1To5();
        this.cycleG6To11 = populationStatsDTO.getCycleG6To11();
        this.cycleG12To13 = populationStatsDTO.getCycleG12To13();
    }
}
