package com.sri.lanka.traffic.admin.web.service.datamng;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngUpdateDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectDetailDTO.TmSrvySectInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectDetailDTO.TmSrvySectInfo.TmSrvyQstnInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO.TmSrvySectSaveInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO.TmSrvySectSaveInfo.TmSrvyQstnSaveInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO.TmSrvySectSaveInfo.TmSrvyQstnSaveInfo.TmSrvyAnsSaveInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO.TmSrvySectUpdateInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO.TmSrvySectUpdateInfo.TmSrvyQstnUpdateInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO.TmSrvySectUpdateInfo.TmSrvyQstnUpdateInfo.TmSrvyAnsUpdateInfo;
import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;
import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyAns;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyInfo;
import com.sri.lanka.traffic.admin.common.entity.TmSrvyQstn;
import com.sri.lanka.traffic.admin.common.entity.TmSrvySect;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnDrctRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnPollsterRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmSrvyAnsRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmSrvyQstnRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmSrvySectRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnDrctRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnPollsterRepository;
import com.sri.lanka.traffic.admin.common.repository.TmSrvyAnsRepository;
import com.sri.lanka.traffic.admin.common.repository.TmSrvyInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TmSrvyQstnRepository;
import com.sri.lanka.traffic.admin.common.repository.TmSrvySectRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;

/**
  * @FileName : InvstMngComponent.java
  * @Project : sri_lanka_admin
  * @Date : 2024. 1. 31. 
  * @작성자 : NK.KIM
  * @프로그램 설명 : 조사관리 컴퍼넌트
  */
/**
  * @FileName : InvstMngService.java
  * @Project : sri_lanka_admin
  * @Date : 2024. 3. 28. 
  * @작성자 : NK.KIM
  * @프로그램 설명 :
  */
@Service
public class InvstMngService {
	
	@Autowired
	TmExmnMngRepository tmExmnMngRepository;
	
	@Autowired
	TmExmnPollsterRepository tmExmnPollsterRepository; 
	
	@Autowired
	QTmExmnPollsterRepository qtmExmnPollsterRepository; 
	
	@Autowired
	TmSrvySectRepository tmSrvySectRepository;
	
	@Autowired
	TmExmnDrctRepository tmExmnDrctRepository;
	
	@Autowired
	TmSrvyQstnRepository tmSrvyQstnRepository;
	
	@Autowired
	TmSrvyAnsRepository tmSrvyAnsRepository;
	
	@Autowired
	TmSrvyInfoRepository tmSrvyInfoRepository;
	
	@Autowired
	QTmSrvyAnsRepository qTmSrvyAnsRepository;
	
	@Autowired
	QTmExmnDrctRepository qTmExmnDrctRepository;
	
	@Autowired
	QTmExmnMngRepository qTmExmnMngRepository;
	
	@Autowired
	QTmSrvyQstnRepository qTmSrvyQstnRepository;
	
	@Autowired
	QTmSrvySectRepository qTmSrvySectRepository;
	
	/**
	  * @Method Name : saveTrfcInvst
	  * @작성일 : 2024. 1. 31.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 등록
	  * @param sriInvstMaster
	  * @param sriTrfcInvst
	  */
	@Transactional
	public void saveInvstInfo(TmExmnMngSaveDTO tmExmnMngSaveDTO) throws CommonException  {
		try {
			//교통 조사는 설문지가 없으므로 상태값 작성 완료
			TmExmnMng tmExmnMng = tmExmnMngSaveDTO.getTmExmnMng(); 
			ExmnSttsCd exmnSttsCd = ExmnSttsCd.INVEST_WRITE_COMPLETED;
			
			if("true".equals(tmExmnMng.getExmnType().getHasDrct())){
				
				// 방향 값이 비어있을 경우 또는 중복되는 경우
				List<TmExmnDrct> tmExmnDrctList = tmExmnMngSaveDTO.getTmExmnDrctList();
				Set<String> locationPairs = new HashSet<>();

				for (TmExmnDrct tmExmnDrct : tmExmnDrctList) {
				    String startLocation = tmExmnDrct.getStartlcNm();
				    String endLocation = tmExmnDrct.getEndlcNm();

				    // 위치 값이 비어있는지 체크
				    if (startLocation.isEmpty() || endLocation.isEmpty()) {
				        throw new CommonException(ErrorCode.VALIDATION_FAILED, "Start or end location cannot be empty.");
				    }

				    // 스타트와 엔드 위치 조합을 문자열로 생성
				    String locationPair = startLocation + "-" + endLocation;

				    // 중복 위치 조합 체크
				    if (!locationPairs.add(locationPair)) {
				        throw new CommonException(ErrorCode.VALIDATION_FAILED, "Duplicate start-end location pair found: " + locationPair);
				    }
				}
				
				tmExmnDrctList.forEach(x -> x.setExmnmngId(tmExmnMng.getExmnmngId()));
				tmExmnDrctRepository.saveAll(tmExmnDrctList);
			}
			
			tmExmnMng.setSttsCd(exmnSttsCd);
			tmExmnMngRepository.save(tmExmnMng);
			
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Examination range save failed");
		}
	}
	
	/**
	  * @Method Name : updateInvstInfo
	  * @작성일 : 2024. 3. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사정보 수정
	  * @param tmExmnMng
	  */
	@Transactional
	public void updateInvstInfo(TmExmnMngUpdateDTO tmExmnMngUpdateDTO,String exmnmngId) {
		try {
			
			TmExmnMngDTO srvyInvstInfo = qTmExmnMngRepository.getInvstInfo(exmnmngId);
			
			if (CommonUtils.isNull(srvyInvstInfo)) {
				throw new CommonException(ErrorCode.EMPTY_DATA, "Survey is empty");
			}
			
			TmExmnMng tmExmnMng = tmExmnMngUpdateDTO.getTmExmnMng();
			
			tmExmnMngRepository.save(tmExmnMng);
			
			if("traffic".equals(tmExmnMng.getExmnType().getType())){
				// 방향 값이 비어있을 경우 또는 중복되는 경우
				List<TmExmnDrct> tmExmnDrctList = tmExmnMngUpdateDTO.getTmExmnDrctList();
				Set<String> locationPairs = new HashSet<>();

				for (TmExmnDrct tmExmnDrct : tmExmnDrctList) {
				    String startLocation = tmExmnDrct.getStartlcNm();
				    String endLocation = tmExmnDrct.getEndlcNm();

				    // 위치 값이 비어있는지 체크
				    if (startLocation.isEmpty() || endLocation.isEmpty()) {
				        throw new CommonException(ErrorCode.VALIDATION_FAILED, "Start or end location cannot be empty.");
				    }

				    // 스타트와 엔드 위치 조합을 문자열로 생성
				    String locationPair = startLocation + "-" + endLocation;

				    // 중복 위치 조합 체크
				    if (!locationPairs.add(locationPair)) {
				        throw new CommonException(ErrorCode.VALIDATION_FAILED, "Duplicate start-end location pair found: " + locationPair);
				    }
				}
				tmExmnDrctRepository.saveAll(tmExmnDrctList);
			}
			
			if(!CommonUtils.isNull(tmExmnMngUpdateDTO.getDeleteDrctArray()) && tmExmnMngUpdateDTO.getDeleteDrctArray().length > 0) {
				qTmExmnDrctRepository.deleteByIdArr(tmExmnMngUpdateDTO.getDeleteDrctArray());
			}
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Examination range save failed");
		}
	}
	
	/**
	  * @Method Name : saveSrvyInvstQstn
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 질문 등록
	  * @param sriSrvyQstn
	  */
	@Transactional
	public void saveSrvyInvstQstn(TmSrvySectSaveDTO tmSrvySectSaveDTO) {
		
		if(CommonUtils.isNull(tmSrvySectSaveDTO)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Survey section data is empty");
		}
		
		try {
			String srvyId = CommonUtils.getUuid();
			tmSrvySectSaveDTO.setSrvyId(srvyId);
			tmSrvyInfoRepository.save(tmSrvySectSaveDTO.toEntity());
			
			for(TmSrvySectSaveInfo tmSrvySectInfo : tmSrvySectSaveDTO.getTmSrvySectList()) {
				String sectId = CommonUtils.getUuid();
				tmSrvySectInfo.setSectId(sectId);
				tmSrvySectInfo.setSrvyId(srvyId);
				tmSrvySectRepository.save(tmSrvySectInfo.toEntity());
				
				if(!CommonUtils.isNull(tmSrvySectInfo.getTmSrvyQstnList())) {
					for(TmSrvyQstnSaveInfo tmSrvyQstnInfo : tmSrvySectInfo.getTmSrvyQstnList()) {
						String qstnId = CommonUtils.getUuid();
						tmSrvyQstnInfo.setQstnId(qstnId);
						tmSrvyQstnInfo.setSectId(sectId);
						tmSrvyQstnRepository.save(tmSrvyQstnInfo.toEntity());
						
						if(!CommonUtils.isNull(tmSrvyQstnInfo.getTmSrvyAnsList())) {
							for(TmSrvyAnsSaveInfo tmSrvyAnsInfo : tmSrvyQstnInfo.getTmSrvyAnsList()) {
								tmSrvyAnsInfo.setQstnId(qstnId);
								tmSrvyAnsRepository.save(tmSrvyAnsInfo.toEntity());
							}// end for SrvyQstnAnswList
						}// null check SrvyQstnAnswList
					}// end for SrvyQstnList
				}// null check SrvyQstnList
			}// end for SrvySctnList
			
			//조사 상태 수정
//			invstInfo.get().setSttsCd(ExmnSttsCd.INVEST_WRITE_COMPLETED);
//			tmExmnMngRepository.save(invstInfo.get());
			
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Survey range save failed");
		}
		
	}
	
	/**
	  * @Method Name : updateSrvyInvstQstn
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 양식 수정
	  * @param tmSrvySectUpdateDTO
	  * @param exmnmngId
	  */
	@Transactional
	public void updateSrvyInvstQstn(TmSrvySectUpdateDTO tmSrvySectUpdateDTO,String srvyId) {
		
		Optional<TmSrvyInfo> survyInfo = tmSrvyInfoRepository.findById(srvyId);
		
		if (!survyInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		
		//설문 정보 저장
		survyInfo.get().setSrvyTitle(tmSrvySectUpdateDTO.getSrvyTitle());
		survyInfo.get().setSrvyType(tmSrvySectUpdateDTO.getSrvyType());
		survyInfo.get().setStartDt(tmSrvySectUpdateDTO.getStartDt());
		survyInfo.get().setEndDt(tmSrvySectUpdateDTO.getEndDt());
		tmSrvyInfoRepository.save(survyInfo.get());
		
		if(CommonUtils.isNull(tmSrvySectUpdateDTO)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Survey section data is empty");
		}
		
		try {
			for(TmSrvySectUpdateInfo tmSrvySectInfo : tmSrvySectUpdateDTO.getTmSrvySectList()) {
				String sectId = CommonUtils.isNull(tmSrvySectInfo.getSectId()) ? CommonUtils.getUuid() : tmSrvySectInfo.getSectId();
				tmSrvySectInfo.setSectId(sectId);
				tmSrvySectRepository.save(tmSrvySectInfo.toEntity());
				
				if(!CommonUtils.isNull(tmSrvySectInfo.getTmSrvyQstnList())) {
					for(TmSrvyQstnUpdateInfo tmSrvyQstnInfo : tmSrvySectInfo.getTmSrvyQstnList()) {
						String qstnId = CommonUtils.isNull(tmSrvyQstnInfo.getQstnId()) ? CommonUtils.getUuid() : tmSrvyQstnInfo.getQstnId();
						tmSrvyQstnInfo.setQstnId(qstnId);
						tmSrvyQstnInfo.setSectId(sectId);
						tmSrvyQstnRepository.save(tmSrvyQstnInfo.toEntity());
						
						if(!CommonUtils.isNull(tmSrvyQstnInfo.getTmSrvyAnsList())) {
							for(TmSrvyAnsUpdateInfo tmSrvyAnsInfo : tmSrvyQstnInfo.getTmSrvyAnsList()) {
								tmSrvyAnsInfo.setQstnId(qstnId);
								TmSrvyAns updateTmSrvyAns = tmSrvyAnsInfo.toEntity();
								String ansId = CommonUtils.isNull(tmSrvyAnsInfo.getAnsId()) ? CommonUtils.getUuid() : tmSrvyAnsInfo.getAnsId();
								updateTmSrvyAns.setAnsId(ansId);
								tmSrvyAnsRepository.save(updateTmSrvyAns);
							}// end for SrvyQstnAnswList
						}// null check SrvyQstnAnswList
					}// end for SrvyQstnList
				}// null check SrvyQstnList
			}// end for SrvySctnList
			
			//답변정보삭제
			if(tmSrvySectUpdateDTO.getDeleteAnsArray().length > 0) {
				qTmSrvyAnsRepository.deleteByIdArr(tmSrvySectUpdateDTO.getDeleteAnsArray());
			}
			//질문정보삭제
			if(tmSrvySectUpdateDTO.getDeleteQstnArray().length > 0) {
				qTmSrvyAnsRepository.deleteByQstnIdArr(tmSrvySectUpdateDTO.getDeleteQstnArray());
				qTmSrvyQstnRepository.deleteByIdArr(tmSrvySectUpdateDTO.getDeleteQstnArray());
			}
			//부문 삭제
			if(tmSrvySectUpdateDTO.getDeleteSectArray().length > 0) {
				String[] deleteQstnIdArr = qTmSrvyQstnRepository.getQstnIdArrBySectIdArr(tmSrvySectUpdateDTO.getDeleteSectArray());
				qTmSrvyAnsRepository.deleteByQstnIdArr(deleteQstnIdArr);
				qTmSrvyQstnRepository.deleteByIdArr(deleteQstnIdArr);
				qTmSrvySectRepository.deleteByIdArr(tmSrvySectUpdateDTO.getDeleteSectArray());
			}
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Survey save failed");
		}
		
	}
	
	
	/**
	  * @Method Name : getSrvySectList
	  * @작성일 : 2024. 3. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문지 목록 조회
	  * @param srvyId
	  * @return
	  */
	public TmSrvySectDetailDTO getSrvySectInfo(String srvyId){
		TmSrvySectDetailDTO tmSrvySectDetailDTO = new TmSrvySectDetailDTO();
		List<TmSrvySect> dbTmSrvySectList = tmSrvySectRepository.findAllBySrvyIdOrderBySectSqnoAsc(srvyId);
		//설문 항목 목록 호출
		if(!CommonUtils.isNull(dbTmSrvySectList)) {
			
			//Entity -> set tmExmnMngSrvySectDTO 
			tmSrvySectDetailDTO.setTmSrvySectList(dbTmSrvySectList);
			
			//DTO에 설문 저장된 목록 For
			for(TmSrvySectInfo tmSrvySectInfo : tmSrvySectDetailDTO.getTmSrvySectList()) {
				
				//설문 질문 목록 호출
				List<TmSrvyQstn> dbTmSrvyQstnList = tmSrvyQstnRepository.findAllBySectIdOrderByQstnSqnoAsc(tmSrvySectInfo.getSectId());
				
				//Entity -> set TmSrvySectInfo				
				tmSrvySectInfo.setTmSrvyQstnList(dbTmSrvyQstnList);
				
				if(!CommonUtils.isNull(dbTmSrvyQstnList)) {
					
					//DTO에 질문 저장된 목록 For
					for(TmSrvyQstnInfo tmSrvyQstnInfo : tmSrvySectInfo.getTmSrvyQstnList()) {
						QstnTypeCd qstnTypeCd = tmSrvyQstnInfo.getQstnTypeCd();
						//답변은 RADIO or CHECKBOX
						if(QstnTypeCd.RADIO.equals(qstnTypeCd) || QstnTypeCd.CHECKBOX.equals(qstnTypeCd)
								|| QstnTypeCd.SELECTBOX.equals(qstnTypeCd)) {
							//설문 답변 목록 호출
							List<TmSrvyAns> dbTmSrvyAnsList = tmSrvyAnsRepository.findAllByQstnIdOrderByAnsSqnoAsc(tmSrvyQstnInfo.getQstnId());
							if(!CommonUtils.isNull(dbTmSrvyAnsList)) {
								tmSrvyQstnInfo.setTmSrvyAnsList(dbTmSrvyAnsList);
							}
						}
					}
				}
			}
		}
		return tmSrvySectDetailDTO;
	}
	
	/**
	  * @Method Name : getInvstInfo
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 상세 정보
	  * @param exmnmngId
	  * @return
	  */
	public TmExmnMngDTO getInvstInfo(String exmnmngId) {
		TmExmnMngDTO srvyInvstInfo = qTmExmnMngRepository.getInvstInfo(exmnmngId);
		
		if (CommonUtils.isNull(srvyInvstInfo)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Survey is empty");
		}
		
		return srvyInvstInfo;
	}
	
	/**
	  * @Method Name : updateExmnRange
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 반경 업데이트
	  * @param exmnmngId
	  * @param exmnRange
	  */
	@Transactional
	public void updateExmnRange(String exmnmngId,String exmnRange) {
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		
		try {
			invstInfo.get().setExmnRange(exmnRange);
			tmExmnMngRepository.save(invstInfo.get());
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Examination range save failed");
		}
	}

	/**
	 * @Method Name : updateEdeleteInvstInfoxmnRange
	 * @작성일 : 2024. 3. 28.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 정보 삭제
	 * @param exmnmngId
	 */
	@Transactional
	public void deleteInvstInfo(String exmnmngId) {
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}

		//상태가 진행전이지 않은경우
		if(!"notYetProgress".equals(invstInfo.get().getSttsCd().getStatus())){
			throw new CommonException(ErrorCode.IN_PROGRESS_OR_COMPLETE, "In progress or complete");
		}
		try {
			//설문 조사 인경우
			if("survey".equals(invstInfo.get().getExmnType().getType())){
				//설문지 답변/질문/부문 테이블 삭제
				List<TmSrvySect> tmSrvySectList = tmSrvySectRepository.findAllBySrvyIdOrderBySectSqnoAsc(exmnmngId);
				if(!CommonUtils.isListNull(tmSrvySectList)){
					for(TmSrvySect tmSrvySect : tmSrvySectList){
						List<TmSrvyQstn> tmSrvyQstnList = tmSrvyQstnRepository.findAllBySectIdOrderByQstnSqnoAsc(tmSrvySect.getSectId());
						if(!CommonUtils.isListNull(tmSrvyQstnList)){
							for(TmSrvyQstn tmSrvyQstn : tmSrvyQstnList){
								if(tmSrvyQstn.getQstnType().equals(QstnTypeCd.CHECKBOX) || tmSrvyQstn.getQstnType().equals(QstnTypeCd.RADIO)){
									List<TmSrvyAns> tmSrvyAnsList = tmSrvyAnsRepository.findAllByQstnIdOrderByAnsSqnoAsc(tmSrvyQstn.getQstnId());
									tmSrvyAnsRepository.deleteAll(tmSrvyAnsList);
								}
							}

							tmSrvyQstnRepository.deleteAll(tmSrvyQstnList);
						}
					}

					tmSrvySectRepository.deleteAll(tmSrvySectList);
				}
			}else{
				//교통량인경우 방향 테이블 삭제
				List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
				if(!CommonUtils.isListNull(tmExmnDrctList)){
					tmExmnDrctRepository.deleteAll(tmExmnDrctList);
				}
			}

			//조사원 삭제
			List<TmExmnPollster> tmExmnPollsterList = tmExmnPollsterRepository.findAllByExmnmngIdOrderByRegistDtAsc(exmnmngId);
			if(!CommonUtils.isListNull(tmExmnPollsterList)){
				tmExmnPollsterRepository.deleteAll(tmExmnPollsterList);
			}

			tmExmnMngRepository.delete(invstInfo.get());
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_DELETE_FAILED, "Examination delete failed");
		}
	}

	@Transactional
	public void deleteSurvey(String srvyId) {
		 // 섹션 조회
		List<TmSrvySect> sects = tmSrvySectRepository.findAllBySrvyId(srvyId);
		for(TmSrvySect sect : sects) {
			// 질문 조회
			List<TmSrvyQstn> qstns = tmSrvyQstnRepository.findAllBySectId(sect.getSectId());
					
			for(TmSrvyQstn qstn : qstns) {
				// 답변 삭제
            	tmSrvyAnsRepository.deleteAllByQstnId(qstn.getQstnId());
	            // 질문 삭제
            	tmSrvyQstnRepository.deleteAllByQstnId(qstn.getQstnId());
	        }
	        // 섹션 삭제
	        tmSrvySectRepository.deleteAllBySectId(sect.getSectId());
	    }
	    // 설문조사 정보 삭제
		tmSrvyInfoRepository.deleteAllBySrvyId(srvyId);
	}

}
