package com.sri.lanka.traffic.admin.common.dto.invst;

import java.time.LocalDateTime;

import com.sri.lanka.traffic.admin.common.enums.code.ColorTypeCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnScheduleSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnTypeCd;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;

@Data
public class TmExmnScheduleDTO {
	//목록 데이터
	private String exmnmngId;
	private String usermngId;
	private String mngrBffltd;
	private String exmnpicId;
	private String exmnNm;
	private String roadCd;
	private ExmnTypeCd exmnType;
	private String exmnTypeNm;
	private ColorTypeCd colrCd;
	private ExmnSttsCd sttsCd;
	private String sttsCdNm;
	private String exmnScheduleSttsCd;
	private String exmnScheduleSttsNm;
	private String userNm;
	private LocalDateTime startDt;
	private LocalDateTime endDt;
	private String registId;
	private String exmnLc;
	
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
	public void setExmnScheduleSttsCd(String exmnScheduleSttsCd) {
		if(!CommonUtils.isNull(exmnScheduleSttsCd)) {
			this.exmnScheduleSttsNm = ExmnScheduleSttsCd.getEnums(exmnScheduleSttsCd).getName();
		}
		this.exmnScheduleSttsCd = exmnScheduleSttsCd;
	}
	
	//캘린더 데이터
	private String title;
	private String backgroundColor;
	private String start;
	private String end;
	
}
