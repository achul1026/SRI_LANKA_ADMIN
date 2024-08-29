package com.sl.tdbms.web.admin.common.dto.invst;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcRoadMng;
import com.sl.tdbms.web.admin.common.entity.TmExmnDrct;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;

import lombok.Data;

@Data
public class TmExmnMngSaveDTO {
	
	private TmExmnMng tmExmnMng;
	
	private TcRoadMng tcRoadMng;

	private List<TmExmnDrct> tmExmnDrctList;
	
}
