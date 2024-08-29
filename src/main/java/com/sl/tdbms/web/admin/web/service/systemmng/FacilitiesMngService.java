package com.sl.tdbms.web.admin.web.service.systemmng;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.dto.facilties.FacilitiesDTO;
import com.sl.tdbms.web.admin.common.entity.TlFixedCur;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqCur;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqLog;
import com.sl.tdbms.web.admin.common.entity.TmInstllcRoad;
import com.sl.tdbms.web.admin.common.entity.TmVdsInstllc;
import com.sl.tdbms.web.admin.common.enums.code.FacilityTypeCd;
import com.sl.tdbms.web.admin.common.repository.TcRoadMngRepository;
import com.sl.tdbms.web.admin.common.repository.TlFixedCurRepository;
import com.sl.tdbms.web.admin.common.repository.TlMvmneqCurRepository;
import com.sl.tdbms.web.admin.common.repository.TlMvmneqLogRepository;
import com.sl.tdbms.web.admin.common.repository.TmInstllcRoadRepository;
import com.sl.tdbms.web.admin.common.repository.TmVdsInstllcRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;


@Service
public class FacilitiesMngService {
	
	@Autowired
    TmInstllcRoadRepository tmInstllcRoadRepository;
	
	@Autowired
    TmVdsInstllcRepository tmVdsInstllcRepository;
	
	@Autowired
    TcRoadMngRepository tcRoadMngRepository;
	
	@Autowired
	TlFixedCurRepository tlFixedCurRepository;
	
	@Autowired
	TlMvmneqCurRepository tlMvmneqCurRepository;
	
	@Autowired
	TlMvmneqLogRepository tlMvmneqLogRepository;
	
	/**
	  * @Method Name : facilitySave
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시설물 타입별 등록
	  * @param facilitiesDTO
	  */
	public void facilitySave(FacilitiesDTO facilitiesDTO) {
		String facilityType = facilitiesDTO.getEqpmntClsf();
		if 		(facilityType.equals(FacilityTypeCd.VDS.getCode())) 					facilityVdsSave(facilitiesDTO);
		else if	(facilityType.equals(FacilityTypeCd.METRO_COUNT.getCode())) 			facilityFixedSave(facilitiesDTO);
		else if	(facilityType.equals(FacilityTypeCd.PORTABLE_METRO_COUNT.getCode())) 	facilityPortableSave(facilitiesDTO);
	}
	
	/**
	  * @Method Name : facilityVdsSave
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 시설물 등록
	  * @param facilitiesDTO
	  */
	@Transactional
	public void facilityVdsSave(FacilitiesDTO facilitiesDTO) {
		// PK 인덱스 설정
		String maxInstllcId = tmVdsInstllcRepository.getMaxInstllcIdByVds();
		facilitiesDTO.setInstllcId(maxInstllcId);
				
		TmVdsInstllc tmVdsInstllc = facilitiesDTO.toTmVdsInstllc();
		tmVdsInstllcRepository.save(tmVdsInstllc);
		
		TmInstllcRoad tmInstllcRoad = facilitiesDTO.toTmInstllcRoad();
		tmInstllcRoadRepository.save(tmInstllcRoad);
	}
	
	/**
	  * @Method Name : facilityFixedSave
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 등록
	  * @param facilitiesDTO
	  */
	@Transactional
	public void facilityFixedSave(FacilitiesDTO facilitiesDTO) {
		// PK 인덱스 설정
		String maxInstllcId = tlFixedCurRepository.getMaxInstllcIdByFixed();
		facilitiesDTO.setInstllcId(maxInstllcId);
		
		TlFixedCur tlFixedCur = facilitiesDTO.toTlFixedCur();
		tlFixedCurRepository.save(tlFixedCur);
		
		TmInstllcRoad tmInstllcRoad = facilitiesDTO.toTmInstllcRoad();
		tmInstllcRoadRepository.save(tmInstllcRoad);
	}
	
	/**
	 * @Method Name : facilityPortableSave
	 * @작성일 : 2024. 8. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 이동형 Metro Count 등록
	 * @param facilitiesDTO
	 */
	@Transactional
	public void facilityPortableSave(FacilitiesDTO facilitiesDTO) {
		// PK 인덱스 설정
		String maxInstllcId = tlMvmneqCurRepository.getMaxInstllcIdByPortable();
		facilitiesDTO.setInstllcId(maxInstllcId);
				
		TmInstllcRoad tmInstllcRoad = facilitiesDTO.toTmInstllcRoad();
		tmInstllcRoadRepository.save(tmInstllcRoad);
		
		LocalDateTime registDt = tmInstllcRoadRepository.findByRoadCdAndInstllcIdAndEqpmntClsfAndDrctCd(facilitiesDTO.getRoadCd(),maxInstllcId,facilitiesDTO.getEqpmntClsf(),facilitiesDTO.getDrctCd()).getRegistDt();
//		LocalDateTime registDt = tmInstllcRoadRepository.findByInstllcIdAndEqpmntClsf(maxInstllcId,facilitiesDTO.getEqpmntClsf()).getRegistDt();
		facilitiesDTO.setClctDt(registDt);
		
		TlMvmneqCur tlMvmneqCur = facilitiesDTO.toTlMvmneqCur();
//		tlMvmneqCur.setClctDt(registDt);
		tlMvmneqCurRepository.save(tlMvmneqCur);
		
		TlMvmneqLog tlMvmneqLog = facilitiesDTO.toTlMvmneqLog();
//				new TlMvmneqLog(tlMvmneqCur);
		tlMvmneqLogRepository.save(tlMvmneqLog);
	}
	
	/**
	  * @Method Name : facilityUpdate
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시설물 타입별 수정
	  * @param facilitiesDTO, instllcId
	  */
	@Transactional
	public void facilityUpdate(FacilitiesDTO facilitiesDTO, String instllcId) {
		
		String facilityType = facilitiesDTO.getEqpmntClsf();
		if 		(facilityType.equals(FacilityTypeCd.VDS.getCode())) 					facilityVdsUpdate(facilitiesDTO, instllcId);
		else if	(facilityType.equals(FacilityTypeCd.METRO_COUNT.getCode())) 			facilityFixedUpdate(facilitiesDTO, instllcId);
		else if	(facilityType.equals(FacilityTypeCd.PORTABLE_METRO_COUNT.getCode())) 	facilityPortableUpdate(facilitiesDTO, instllcId);
		
	}
	
	/**
	  * @Method Name : facilityVdsUpdate
	  * @작성일 : 2024. 8. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 시설물 수정
	  * @param facilitiesDTO
	  * @param instllcId
	  */
	@Transactional
	public void facilityVdsUpdate(FacilitiesDTO facilitiesDTO, String instllcId) {
		TmVdsInstllc vdsInstllc = tmVdsInstllcRepository.findById(instllcId).orElseThrow();
		if(!CommonUtils.isNull(facilitiesDTO.getCameraId())) vdsInstllc.setCameraId(facilitiesDTO.getCameraId());
		if(!CommonUtils.isNull(facilitiesDTO.getLat())) vdsInstllc.setLat(facilitiesDTO.getLat());
		if(!CommonUtils.isNull(facilitiesDTO.getLon())) vdsInstllc.setLon(facilitiesDTO.getLon());
		if(!CommonUtils.isNull(facilitiesDTO.getInstllcNm())) vdsInstllc.setInstllcNm(facilitiesDTO.getInstllcNm());
		if(!CommonUtils.isNull(facilitiesDTO.getInstllcDescr())) vdsInstllc.setInstllcDescr(facilitiesDTO.getInstllcDescr());
		tmVdsInstllcRepository.save(vdsInstllc);
		facilityInstllcRoadUpdate(facilitiesDTO, instllcId);
	}
	
	/**
	  * @Method Name : facilityFixedUpdate
	  * @작성일 : 2024. 8. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 수정
	  * @param facilitiesDTO
	  * @param instllcId
	  */
	@Transactional
	public void facilityFixedUpdate(FacilitiesDTO facilitiesDTO, String instllcId) {
		TlFixedCur tlFixedCur = tlFixedCurRepository.findById(instllcId).orElseThrow();
		if(!CommonUtils.isNull(facilitiesDTO.getLat())) tlFixedCur.setLat(facilitiesDTO.getLat());
		if(!CommonUtils.isNull(facilitiesDTO.getLon())) tlFixedCur.setLon(facilitiesDTO.getLon());
		if(!CommonUtils.isNull(facilitiesDTO.getInstllcNm())) tlFixedCur.setInstllcNm(facilitiesDTO.getInstllcNm());
		tlFixedCurRepository.save(tlFixedCur);
		facilityInstllcRoadUpdate(facilitiesDTO, instllcId);
	}
	
	/**
	 * @Method Name : facilityPortableUpdate
	 * @작성일 : 2024. 8. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 이동형 Metro Count 수정
	 * @param facilitiesDTO
	 * @param instllcId
	 */
	@Transactional
	public void facilityPortableUpdate(FacilitiesDTO facilitiesDTO, String instllcId) {
		facilityInstllcRoadUpdate(facilitiesDTO, instllcId);
		
		TlMvmneqCur tlMvmneqCur = tlMvmneqCurRepository.findById(instllcId).orElseThrow();
		
//		if(!CommonUtils.isNull(facilitiesDTO.getEqpmntId())) tlMvmneqCur.setEqpmntId(facilitiesDTO.getEqpmntId());
		if(!CommonUtils.isNull(facilitiesDTO.getLat())) tlMvmneqCur.setLat(facilitiesDTO.getLat());
		if(!CommonUtils.isNull(facilitiesDTO.getLon())) tlMvmneqCur.setLon(facilitiesDTO.getLon());
		if(!CommonUtils.isNull(facilitiesDTO.getInstllcNm())) tlMvmneqCur.setInstllcNm(facilitiesDTO.getInstllcNm());
//		if(!CommonUtils.isNull(facilitiesDTO.getInstllcDescr())) tlMvmneqCur.setInstllcDescr(facilitiesDTO.getInstllcDescr());
		tlMvmneqCurRepository.save(tlMvmneqCur);
		
		LocalDateTime updtDt = tmInstllcRoadRepository.findByInstllcIdAndEqpmntClsf(facilitiesDTO.getInstllcId(),facilitiesDTO.getEqpmntClsf()).getUpdtDt();
		facilitiesDTO.setClctDt(updtDt);
		
		TlMvmneqLog tlMvmneqLog = facilitiesDTO.toTlMvmneqLog();
		tlMvmneqLogRepository.save(tlMvmneqLog);
	}
	
	/**
	  * @Method Name : facilityInstllcRoadUpdate
	  * @작성일 : 2024. 8. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설치위치 도로매핑 관리(tm_instllc_road) 테이블 수정
	  * @param facilitiesDTO
	  * @param instllcId
	  */
	@Transactional
	public void facilityInstllcRoadUpdate(FacilitiesDTO facilitiesDTO, String instllcId) {
		TmInstllcRoad oriInstllcRoad = tmInstllcRoadRepository.findByInstllcIdAndEqpmntClsf(facilitiesDTO.getInstllcId(),facilitiesDTO.getEqpmntClsf()); // 기존 데이터를 삭제하고 save하는 방법도 고려
		// PK를 변경하기 위해 새로운 객체 생성
		TmInstllcRoad instllcRoad = new TmInstllcRoad(oriInstllcRoad);
		if(!CommonUtils.isNull(facilitiesDTO.getRoadCd())) instllcRoad.setRoadCd(facilitiesDTO.getRoadCd());
		if(!CommonUtils.isNull(facilitiesDTO.getDrctCd())) instllcRoad.setDrctCd(facilitiesDTO.getDrctCd());
		if(!CommonUtils.isNull(facilitiesDTO.getLaneCnt())) instllcRoad.setLaneCnt(facilitiesDTO.getLaneCnt());
		if(!CommonUtils.isNull(facilitiesDTO.getUseYn())) instllcRoad.setUseYn(facilitiesDTO.getUseYn());
		tmInstllcRoadRepository.updateInstllcRoad(instllcRoad);
		
	}
	
}
