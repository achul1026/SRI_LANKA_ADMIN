package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmExmnDrct;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;

import lombok.Data;

@Data
public class TmExmnMngUpdateDTO {
	
	private TmExmnMng tmExmnMng;

	private List<TmExmnDrct> tmExmnDrctList;
	
	private String[] deleteDrctArray;
}
