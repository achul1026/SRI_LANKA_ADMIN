package com.sri.lanka.traffic.admin.web.service.potalmng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sri.lanka.traffic.admin.common.entity.TcSiteMng;
import com.sri.lanka.traffic.admin.common.repository.TcSiteMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;


@Service
public class RelatedSiteMngService {
	
	@Autowired
	private TcSiteMngRepository tcSiteMngRepository;
	
	/**
	  * @Method Name : setSiteInfo
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관련사이트 정보 설정
	  * @param tcSiteMng
	  * @param siteId
	  * @return
	  */
	public TcSiteMng setSiteInfo(TcSiteMng tcSiteMng, String siteId) {
		
//		String mngrId = LoginMngrUtils.getUserId();
		TcSiteMng newTcSiteMng = tcSiteMngRepository.findById(siteId).get();
		
		if (!CommonUtils.isNull(tcSiteMng.getSiteNm())) newTcSiteMng.setSiteNm(tcSiteMng.getSiteNm());
		if (!CommonUtils.isNull(tcSiteMng.getSiteclsfCd())) newTcSiteMng.setSiteclsfCd(tcSiteMng.getSiteclsfCd());
		if (!CommonUtils.isNull(tcSiteMng.getSiteUrl())) newTcSiteMng.setSiteUrl(tcSiteMng.getSiteUrl());
		if (!CommonUtils.isNull(tcSiteMng.getDspyYn())) newTcSiteMng.setDspyYn(tcSiteMng.getDspyYn());
		
//		newSriSiteMng.setUpdId(mngrId);
		
		return newTcSiteMng;
	}

}
