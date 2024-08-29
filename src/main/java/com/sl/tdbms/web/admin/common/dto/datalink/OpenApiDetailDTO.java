package com.sl.tdbms.web.admin.common.dto.datalink;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmApiPvsnitem;
import com.sl.tdbms.web.admin.common.entity.TmApiRqstitem;
import com.sl.tdbms.web.admin.common.entity.TmApiSrvc;

import lombok.Data;

@Data
public class OpenApiDetailDTO{
	private TmApiSrvc tmApiSrvc;
	private List<TmApiRqstitem> tmApiRqstitemList;
	private List<TmApiPvsnitem> tmApiPvsnitemList;
}
