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
 * 차량 등록 별 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsVehicleRegPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
    private String vhclCateg;
    private BigDecimal jan;
    private BigDecimal feb;
    private BigDecimal mar;
    private BigDecimal apr;
    private BigDecimal may;
    private BigDecimal jun;
    private BigDecimal jul;
    private BigDecimal aug;
    private BigDecimal sep;
    private BigDecimal oct;
    private BigDecimal nov;
    private BigDecimal dec;
	
	public TsVehicleRegPop(PopulationStatsDTO populationStatsDTO) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.popstatId = CommonUtils.getUuid();
        this.vhclCateg = populationStatsDTO.getVhclCateg();
        this.jan = populationStatsDTO.getJan();
        this.feb = populationStatsDTO.getFeb();
        this.mar = populationStatsDTO.getMar();
        this.apr = populationStatsDTO.getApr();
        this.may = populationStatsDTO.getMay();
        this.jun = populationStatsDTO.getJun();
        this.jul = populationStatsDTO.getJul();
        this.aug = populationStatsDTO.getAug();
        this.sep = populationStatsDTO.getSep();
        this.oct = populationStatsDTO.getOct();
        this.nov = populationStatsDTO.getNov();
        this.dec = populationStatsDTO.getDec();
	}
}
