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
 * 토지면적별 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsLandAreaPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
    private int numIdx;
    private String dsDivision;
    private String dsdId;
    private String district;
    private String districtId;
    private String province;
    private String provinceId;
    private BigDecimal area;
	
	public TsLandAreaPop(PopulationStatsDTO populationStatsDTO) {
		this.popmngId = populationStatsDTO.getPopmngId();
		this.fileId = populationStatsDTO.getFileId();
		this.district = populationStatsDTO.getDistrict();
		this.popstatId = CommonUtils.getUuid();
        this.numIdx = populationStatsDTO.getNumIdx();
        this.dsDivision = populationStatsDTO.getDsDivision();
        this.dsdId = populationStatsDTO.getDsdId();
        this.district = populationStatsDTO.getDistrict();
        this.districtId = populationStatsDTO.getDistrictId();
        this.province = populationStatsDTO.getProvince();
        this.provinceId = populationStatsDTO.getProvinceId();
        this.area = populationStatsDTO.getArea();
	}
}
