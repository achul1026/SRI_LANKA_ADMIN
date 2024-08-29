package com.sl.tdbms.web.admin.web.service.datalink;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiDataDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiDetailDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TmApiPvsnitem;
import com.sl.tdbms.web.admin.common.entity.TmApiRqstitem;
import com.sl.tdbms.web.admin.common.entity.TmApiSrvc;
import com.sl.tdbms.web.admin.common.querydsl.QTmApiSrvcRepository;
import com.sl.tdbms.web.admin.common.repository.TmApiPvsnitemRepository;
import com.sl.tdbms.web.admin.common.repository.TmApiRqstitemRepository;
import com.sl.tdbms.web.admin.common.repository.TmApiSrvcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Service
public class OpenApiService {
	
	@Autowired
    QTmApiSrvcRepository qTmApiSrvcRepository;
	
	@Autowired
    TmApiSrvcRepository tmApiSrvcRepository;
	
	@Autowired
    TmApiRqstitemRepository tmApiRqstitemRepository;
	
	@Autowired
    TmApiPvsnitemRepository tmApiPvsnitemRepository;
	
	/**
	 * @Method getOpenApiList
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @param OpenApiSearchDTO
	 * @Method 설명 : OpenApi 목록 조회
	 * @param paging
	 * @return
	 */
	public List<TmApiSrvc> getOpenApiList(OpenApiSearchDTO openApiSearchDTO, PagingUtils paging) {
		return qTmApiSrvcRepository.getTmApiSrvcList(openApiSearchDTO,paging);
	}

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : OpenApi목록 카운트 조회
	 * @param OpenApiSearchDTO
	 * @return
	 */
	public Long getTotalCount(OpenApiSearchDTO openApiSearchDTO) {
		return qTmApiSrvcRepository.getTotalCount(openApiSearchDTO);
	}

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : OpenApi등록
	 * @param OpenApiSearchDTO
	 * @return
	 */
	@Transactional
	public String openApiSave(OpenApiDataDTO openApiRegistDTO) {
		String srvcId = CommonUtils.getUuid();
		
		TmApiSrvc tmApiSrvc = new TmApiSrvc(openApiRegistDTO);
		tmApiSrvc.setSrvcId(srvcId);
		tmApiSrvc.setRegistDt(LocalDateTime.now());
		tmApiSrvc.setUseYn("Y");
		tmApiSrvcRepository.save(tmApiSrvc);
		
		ObjectMapper objectMapper = new ObjectMapper();
		//요청정보 등록
		if(!CommonUtils.isNull(openApiRegistDTO.getTmApiRqstitemListJSON())) {
           List<TmApiRqstitem> tmApiRqstitemList = null;
			try {
				tmApiRqstitemList = objectMapper.readValue(openApiRegistDTO.getTmApiRqstitemListJSON(), new TypeReference<List<TmApiRqstitem>>() {});
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				new CommonException(ErrorCode.CANNOT_BE_CAST_ENTITY);
			}
			
            for (TmApiRqstitem tmApiRqstitem : tmApiRqstitemList) {
            	String itemId = CommonUtils.getUuid();
            	tmApiRqstitem.setSrvcId(srvcId);
            	tmApiRqstitem.setItemId(itemId);
            	tmApiRqstitemRepository.save(tmApiRqstitem);
            }
		}
		
		//응답정보 등록
		if(!CommonUtils.isNull(openApiRegistDTO.getTmApiPvsnitemListJSON())) {
			List<TmApiPvsnitem> tmApiPvsnItemList = null;
			try {
				tmApiPvsnItemList = objectMapper.readValue(openApiRegistDTO.getTmApiPvsnitemListJSON(), new TypeReference<List<TmApiPvsnitem>>() {});
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				new CommonException(ErrorCode.CANNOT_BE_CAST_ENTITY);
			}
			
			for (TmApiPvsnitem tmApiPvsnitem : tmApiPvsnItemList) {
				String itemId = CommonUtils.getUuid();
				tmApiPvsnitem.setSrvcId(srvcId);
				tmApiPvsnitem.setItemId(itemId);
				tmApiPvsnitemRepository.save(tmApiPvsnitem);
			}
		}
		return srvcId;
	}

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 상세 조회
	 * @param OpenApiSearchDTO
	 * @return
	 */
	public OpenApiDetailDTO getOpenApiDetail(String srvcId) {
		return qTmApiSrvcRepository.findOpenApiDetailBySrvcId(srvcId);
	}

	/**
	 * @Method Name : openApiDelete
	 * @작성일 : 2024. 6. 26.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  OPEN API 삭제
	 * @param srvcId
	 */
	@Transactional
	public void deleteOpenAPI(String srvcId) {
		OpenApiDetailDTO openApiDetailDTO = qTmApiSrvcRepository.findOpenApiDetailBySrvcId(srvcId);
		if(openApiDetailDTO != null) {
			if(openApiDetailDTO.getTmApiSrvc() != null) {
				tmApiSrvcRepository.delete(openApiDetailDTO.getTmApiSrvc());
			} else {
				throw new CommonResponseException(ErrorCode.ENTITY_DATA_NOT_FOUND);
			}
			if(openApiDetailDTO.getTmApiRqstitemList() != null && !openApiDetailDTO.getTmApiRqstitemList().isEmpty()) {
				for(TmApiRqstitem tmApiRqstitem : openApiDetailDTO.getTmApiRqstitemList()) {
					tmApiRqstitemRepository.delete(tmApiRqstitem);
				}
			}
			
			if(openApiDetailDTO.getTmApiPvsnitemList() != null && !openApiDetailDTO.getTmApiPvsnitemList().isEmpty()) {
				for(TmApiPvsnitem tmApiPvsnitem : openApiDetailDTO.getTmApiPvsnitemList()) {
					tmApiPvsnitemRepository.delete(tmApiPvsnitem);
				}
			}
		}
	}

	/**
	 * @Method Name : openApiUpdate
	 * @작성일 : 2024. 6. 26.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  OPEN API 수정
	 * @param OpenApiDataDTO
	 */
	@Transactional
	public void openApiUpdate(OpenApiDataDTO openApiRegistDTO) {
		TmApiSrvc tmApiSrvc = new TmApiSrvc(openApiRegistDTO);
		tmApiSrvcRepository.save(tmApiSrvc);
		if(openApiRegistDTO.getDeleteReqItemArray() != null && !openApiRegistDTO.getDeleteReqItemArray().isEmpty()) {
			for(String itemId :openApiRegistDTO.getDeleteReqItemArray()) {
				tmApiRqstitemRepository.deleteById(itemId);
			}
		}
		if(openApiRegistDTO.getDeleteResItemArray() != null && !openApiRegistDTO.getDeleteResItemArray().isEmpty()) {
			for(String itemId :openApiRegistDTO.getDeleteResItemArray()) {
				tmApiPvsnitemRepository.deleteById(itemId);
			}
		}
		if(openApiRegistDTO.getTmApiRqstitemList() != null && !openApiRegistDTO.getTmApiRqstitemList().isEmpty()) {
			for(TmApiRqstitem tmApiRqstitem : openApiRegistDTO.getTmApiRqstitemList()) {
				if(CommonUtils.isNull(tmApiRqstitem.getItemId())) {
					tmApiRqstitem.setItemId(CommonUtils.getUuid());
				}
				tmApiRqstitemRepository.save(tmApiRqstitem);
			}
		}
		if(openApiRegistDTO.getTmApiPvsnitemList() != null && !openApiRegistDTO.getTmApiPvsnitemList().isEmpty()) {
			for(TmApiPvsnitem tmApiPvsnitem : openApiRegistDTO.getTmApiPvsnitemList()) {
				if(CommonUtils.isNull(tmApiPvsnitem.getItemId())) {
					tmApiPvsnitem.setItemId(CommonUtils.getUuid());
				}
				tmApiPvsnitemRepository.save(tmApiPvsnitem);
			}
		}
	}

}
