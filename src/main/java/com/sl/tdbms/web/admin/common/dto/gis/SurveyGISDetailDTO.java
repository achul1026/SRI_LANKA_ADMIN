package com.sl.tdbms.web.admin.common.dto.gis;

import com.sl.tdbms.web.admin.common.enums.code.ColorTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SurveyGISDetailDTO {

	private String exmnmngId;
	private String usermngId;
	private String srvyId;
	private String srvyTitle;
	private String mngrBffltd;
	private String exmnpicId;
	private String exmnNm;
	private String cstmYn;
	private String roadCd;
	private String roadDescr;
	private String partcptCd;
	private BigDecimal laneCnt;
	private ExmnTypeCd exmnType;
	private String exmnTypeNm;
	private ColorTypeCd colrCd;
	private ExmnSttsCd sttsCd;
	private String sttsCdNm;
	private String userNm;
	private LocalDateTime startDt;
	private LocalDateTime endDt;
	private String registId;
	private String exmnLc;
	private String exmnDiv;
	private String exmnRange;
	private String dsdCd;
	private String gnCd;
	private String tazCd;
	private BigDecimal exmnNop;
	private BigDecimal goalCnt;
	private BigDecimal lat;
	private BigDecimal lon;
	private String cordonLine;
	private String tollBooth;
	private String screenLine;
	private Long realExmnCnt;

	public void setExmnType(ExmnTypeCd exmnType) {
		if(!CommonUtils.isNull(exmnType)) {
			this.exmnTypeNm = exmnType.getName();
		}
		this.exmnType = exmnType;
	}

	public void setSttsCd(ExmnSttsCd sttsCd) {
		if(!CommonUtils.isNull(sttsCd)) {
			this.sttsCdNm = sttsCd.getName();
		}
		this.sttsCd = sttsCd;
	}
	public void setExmnLc(String exmnLc) {
		if(!CommonUtils.isNull(exmnLc)) {
			this.exmnLc = exmnLc.replace(',','>');
		}
	}
	
}
