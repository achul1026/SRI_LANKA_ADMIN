package com.sri.lanka.traffic.admin.common.dto.invst;

import java.util.List;

import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;

import lombok.Data;

@Data
public class TmExmnPollsterSaveDTO {
	
	private String exmnmngId;
	private List<TmExmnPollster> tmExmnPollsterList;
	private String[] deletePollsterIdArray;
	
}
