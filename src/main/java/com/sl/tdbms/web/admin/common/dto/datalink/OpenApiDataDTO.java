package com.sl.tdbms.web.admin.common.dto.datalink;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmApiPvsnitem;
import com.sl.tdbms.web.admin.common.entity.TmApiRqstitem;

import lombok.Data;

@Data
public class OpenApiDataDTO{

	//등록
	private String srvcNm;
	private String srvcClsf;
	private String pvsnInst;
	private String mngrId;
	private String exmnpicId;
	private String srvcDescr;
	private String srvcUrl;
	
	private String tmApiRqstitemListJSON;
	private String tmApiPvsnitemListJSON;

	//수정
	private String srvcId;
    private List<TmApiRqstitem> tmApiRqstitemList;
    private List<TmApiPvsnitem> tmApiPvsnitemList;
    private List<String> deleteReqItemArray;
    private List<String> deleteResItemArray;
}
