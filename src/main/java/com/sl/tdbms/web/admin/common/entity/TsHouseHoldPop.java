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
 * 가구별 인구 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsHouseHoldPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String provinCd;
	private String districtCd;
	@Column(name = "household_1")
    private BigDecimal household1;
	@Column(name = "household_2")
    private BigDecimal household2;
	@Column(name = "household_3")
    private BigDecimal household3;
    @Column(name = "household_4")
    private BigDecimal household4;
    @Column(name = "household_5")
    private BigDecimal household5;
    @Column(name = "household_6")
    private BigDecimal household6;
    @Column(name = "household_7")
    private BigDecimal household7;
    @Column(name = "household_8")
    private BigDecimal household8;
    @Column(name = "household_9")
    private BigDecimal household9;
    @Column(name = "household_10_above")
    private BigDecimal household10Above;
    private BigDecimal avgHouseholdSize;
	
	public TsHouseHoldPop(PopulationStatsDTO populationStatsDTO,String provinCd, String districtCd) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.popstatId = CommonUtils.getUuid();
		this.provinCd = provinCd;
		this.districtCd = districtCd;
		this.household1  = populationStatsDTO.getHousehold1();
		this.household2  = populationStatsDTO.getHousehold2();
		this.household3  = populationStatsDTO.getHousehold3();
		this.household4  = populationStatsDTO.getHousehold4();
		this.household5  = populationStatsDTO.getHousehold5();
		this.household6  = populationStatsDTO.getHousehold6();
		this.household7  = populationStatsDTO.getHousehold7();
		this.household8  = populationStatsDTO.getHousehold8();
		this.household9  = populationStatsDTO.getHousehold9();
		this.household10Above = populationStatsDTO.getHousehold10Above();
		this.avgHouseholdSize = populationStatsDTO.getAvgHouseholdSize();
		
	}
}
