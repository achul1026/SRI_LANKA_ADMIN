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
 * 주택 유형 별 가구 인구수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TsHousingTypePop extends CreateEntity{
	private String popmngId;		//부모테이블 고유Id
	private String fileId;			//부모테이블 파일 고유 Id
	@Id
	private String popstatId;		//고유아이디
	private String dsDivision;
	private String provinCd;
	private String districtCd;
    private BigDecimal oneStory;		//1층주택
    private BigDecimal twoStory;		//2층주택
    private BigDecimal multiStory;		//2층 이상 주택
    private BigDecimal houseAnnex;		//주택/부속 건물
    private BigDecimal flat;			//아파트
    private BigDecimal condominium;		//콘도미니엄
    private BigDecimal twinHouse;		//쌍둥이 주택
    private BigDecimal room;			//방
    private BigDecimal hutShanty;		//오두막/초가집
    
    public TsHousingTypePop(PopulationStatsDTO populationStatsDTO,String provinCd, String districtCd) {
        this.popmngId = populationStatsDTO.getPopmngId();
        this.fileId = populationStatsDTO.getFileId();
        this.dsDivision = populationStatsDTO.getDsDivision();
        this.popstatId = CommonUtils.getUuid();
        this.provinCd = provinCd;
        this.districtCd = districtCd;
        this.oneStory = populationStatsDTO.getOneStory();
        this.twoStory = populationStatsDTO.getTwoStory();
        this.multiStory = populationStatsDTO.getMultiStory();
        this.houseAnnex = populationStatsDTO.getHouseAnnex();
        this.flat = populationStatsDTO.getFlat();
        this.condominium = populationStatsDTO.getCondominium();
        this.twinHouse = populationStatsDTO.getTwinHouse();
        this.room = populationStatsDTO.getRoom();
        this.hutShanty = populationStatsDTO.getHutShanty();
    }
}
