package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.ArrayList;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmExmnMng;

import lombok.Data;

@Data
public class TmExmnScheduleDetailDTO {
	
	private int totalCnt = 0;
	private List<TmExmnScheduleInfo> tmExmnScheduleList = new ArrayList<>();
	
	@Data
	public static class TmExmnScheduleInfo {
		private TmExmnMng tmExmnMng;
		private String pollsterNm;
	}
	
}
