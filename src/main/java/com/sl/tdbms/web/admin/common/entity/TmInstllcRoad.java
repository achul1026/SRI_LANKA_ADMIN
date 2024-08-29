package com.sl.tdbms.web.admin.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data //설치위치 도로매장 관리
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class TmInstllcRoad extends BaseEntity {

	@Id
	private String roadCd; //도로 코드
	
	private String instllcId; //설치 위치 아이디
	
	private String eqpmntClsf; //장비 분류
	
	private String drctCd; //방향 코드
	
	private BigDecimal laneCnt; //차로 수
	
	private String useYn; //사용 여부
	
	public TmInstllcRoad(TmInstllcRoad instllcRoad) {
        this.roadCd = instllcRoad.roadCd;
        this.instllcId = instllcRoad.instllcId;
        this.eqpmntClsf = instllcRoad.eqpmntClsf;
        this.drctCd = instllcRoad.drctCd;
        this.laneCnt = instllcRoad.laneCnt;
        this.useYn = instllcRoad.useYn;
    }
	
	@Builder
	public TmInstllcRoad(String roadCd, String instllcId
			, String eqpmntClsf, String drctCd
			, BigDecimal laneCnt, String useYn) {
		this.roadCd = roadCd;
		this.instllcId = instllcId;
		this.eqpmntClsf = eqpmntClsf;
		this.drctCd = drctCd;
		this.laneCnt = laneCnt;
		this.useYn = useYn;
	}
    
}
