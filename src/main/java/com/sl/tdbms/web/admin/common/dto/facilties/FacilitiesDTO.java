package com.sl.tdbms.web.admin.common.dto.facilties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sl.tdbms.web.admin.common.entity.TlFixedCur;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqCur;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqLog;
import com.sl.tdbms.web.admin.common.entity.TmInstllcRoad;
import com.sl.tdbms.web.admin.common.entity.TmVdsInstllc;

import lombok.Data;
	
@Data
public class FacilitiesDTO {
	
	private String roadCd; //도로 코드
	private String instllcId; //설치 위치 아이디
	private String cameraId; //카메라 아이디
	private String eqpmntId; //장비 아이디
	private String eqpmntClsf; //장비 분류
	private String instllcNm; //설치위치 명
	private String instllcDescr; //설치위치 설명
	private String drctCd; //방향 코드
	private String useYn; //사용 여부
	private BigDecimal laneCnt; //차로 수
	private BigDecimal lat; //위도
	private BigDecimal lon; //경도
	private LocalDateTime registDt; //등록 일시
	private LocalDateTime updtDt; //수정 일시
	private LocalDateTime clctDt; //Portable Metro Count 등록 일시
	
	public TmVdsInstllc toTmVdsInstllc() {
		return TmVdsInstllc.builder()
				.instllcId(instllcId)
				.instllcNm(instllcNm)
				.instllcDescr(instllcDescr)
				.cameraId(cameraId)
				.lat(lat)
				.lon(lon)
				.build();
	}
	
	public TmInstllcRoad toTmInstllcRoad() {
		return TmInstllcRoad.builder()
				.instllcId(instllcId)
				.roadCd(roadCd)
				.instllcId(instllcId)
				.eqpmntClsf(eqpmntClsf)
				.drctCd(drctCd)
				.laneCnt(laneCnt)
				.useYn(useYn)
				.build();
	}
	
	public TlFixedCur toTlFixedCur() {
		return TlFixedCur.builder()
				.instllcId(instllcId)
				.instllcNm(instllcNm)
				.lat(lat)
				.lon(lon)
				.build();
	}
	
	public TlMvmneqCur toTlMvmneqCur() {
		return TlMvmneqCur.builder()
				.instllcId(instllcId)
				.instllcNm(instllcNm)
//				.instllcDescr(instllcDescr)
//				.eqpmntId(eqpmntId)
				.lat(lat)
				.lon(lon)
//				.clctDt(clctDt)
				.build();
	}
	
	public TlMvmneqLog toTlMvmneqLog() {
		return TlMvmneqLog.builder()
				.instllcId(instllcId)
				.instllcNm(instllcNm)
//				.instllcDescr(instllcDescr)
//				.eqpmntId(eqpmntId)
				.lat(lat)
				.lon(lon)
				.clctDt(clctDt)
				.build();
	}
}
