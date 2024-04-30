package com.sri.lanka.traffic.admin.common.dto.invst;

import java.util.List;

import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;

import lombok.Data;

@Data
public class TmExmnMngSaveDTO {
	
	private TmExmnMng tmExmnMng;

	private List<TmExmnDrct> tmExmnDrctList;
	
}
