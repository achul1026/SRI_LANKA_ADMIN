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
 * 가구 거주 주택 단위 별 인구 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsOccHousingUnitPop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String district;
	private String dsDivision;
	private String gnDivision;
	private String gnId;
	private String gnNo;
    private BigDecimal householdOwned;
    private BigDecimal rentGovermentOwned;
    private BigDecimal rentIndvslOwned;
    private BigDecimal rentFree;
    private BigDecimal encroached;
    private BigDecimal otherOccupied;
    
    public TsOccHousingUnitPop(PopulationStatsDTO populationStatsDTO, String gnId) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.popstatId = CommonUtils.getUuid();
        this.gnId = gnId;
        this.gnNo = populationStatsDTO.getGnNo();
        this.district = populationStatsDTO.getDistrict();
        this.dsDivision = populationStatsDTO.getDsDivision();
        this.gnDivision = populationStatsDTO.getGnDivision();
        this.householdOwned = populationStatsDTO.getHouseholdOwned();
        this.rentGovermentOwned = populationStatsDTO.getRentGovermentOwned();
        this.rentIndvslOwned = populationStatsDTO.getRentIndvslOwned();
        this.rentFree = populationStatsDTO.getRentFree();
        this.encroached = populationStatsDTO.getEncroached();
        this.otherOccupied = populationStatsDTO.getOtherOccupied();
    }
}
