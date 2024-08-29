package com.sl.tdbms.web.admin.web.controller.datalink;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.ApiKeySrvcSaveDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiRequestListDto;
import com.sl.tdbms.web.admin.common.entity.TmApiCertkey;
import com.sl.tdbms.web.admin.common.entity.TmApiKeysrvc;
import com.sl.tdbms.web.admin.common.querydsl.QTmApiCertkeyRepository;
import com.sl.tdbms.web.admin.common.repository.TmApiCertkeyRepository;
import com.sl.tdbms.web.admin.common.repository.TmApiKeysrvcRepository;
import com.sl.tdbms.web.admin.web.service.systemmng.CodeMngService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/datalink/authentication")
@RequiredArgsConstructor
public class AuthenticationKeyController {

	final QTmApiCertkeyRepository qTmApiCertkeyRepository;
	
	final TmApiCertkeyRepository tmApiCertkeyRepository;
	
	final TmApiKeysrvcRepository tmApiKeysrvcRepository;
	
	final CodeMngService codeMngService;

	/**
	 * @Method Name : authenticationkeyList
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 인증키요청관리 리스트
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String authenticationKeyPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<OpenApiRequestListDto> list = qTmApiCertkeyRepository.getApiCertKeyRequestList(searchCommonDTO, paging);
		long totalCnt = qTmApiCertkeyRepository.getTotalCount(searchCommonDTO);
		paging.setTotalCount(totalCnt);
		model.addAttribute("list",list);
		model.addAttribute("totalCnt",totalCnt);
		model.addAttribute("paging", paging);
		return "views/datalink/authenticationKeyList";
	}

	/**
	 * @Method Name : authenticationkeyDetail
	 * @작성일 : 2024. 04. 15.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 인증키요청관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/detail")
	public String authenticationKeyDetail(Model model, @RequestParam(name="certkeyId",required=true) String certkeyId) {
	    TmApiCertkey certkeyDetail = tmApiCertkeyRepository.findById(certkeyId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid certkeyId: " + certkeyId));
	    model.addAttribute("certKeyDetail", certkeyDetail);
	    model.addAttribute("apiSrvcList", qTmApiCertkeyRepository.getApiSrvcList(certkeyId) );
		return "views/datalink/authenticationKeyDetail";
	}
	
	/**
	 * @Method Name : authenticationKeySave
	 * @작성일 : 2024. 07. 11.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인증키 서비스 연결 모달
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/srvckey/save")
	public String authenticationKeySave(Model model, @RequestParam(name="certkeyId",required=true) String certkeyId , SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		long totalCnt = qTmApiCertkeyRepository.getSrvcKeyConnectCount(certkeyId,searchCommonDTO);
		paging.setPageSize(5);
		paging.setLimitCount(5);
		paging.setTotalCount(totalCnt);
		model.addAttribute("bffltdCd", codeMngService.getTcCdInfoForGrpCd(GroupCode.BFFLTD_CD.getCode()));
		model.addAttribute("apiSrvcList", qTmApiCertkeyRepository.getSrvcKeyConnectList(certkeyId,searchCommonDTO,paging));
		model.addAttribute("searchOption", searchCommonDTO);
		model.addAttribute("paging", paging);
		return "views/datalink/modal/authenticationKeySave";
	}
	
	/**
	 * @Method Name : authenticationKeySave
	 * @작성일 : 2024. 07. 11.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인증키 서비스 연결 모달
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/srvckey/save")
	public  @ResponseBody CommonResponse<?> authenticationKeySaveAjax(Model model ,@RequestBody ApiKeySrvcSaveDTO apiKeySrvcSaveDTO) {
		try {
			if(apiKeySrvcSaveDTO != null) {
				if(!apiKeySrvcSaveDTO.getAddSrvcArr().isEmpty() && !CommonUtils.isNull(apiKeySrvcSaveDTO.getCertkeyId())) {
					for(String srvcId : apiKeySrvcSaveDTO.getAddSrvcArr()) {
						TmApiKeysrvc tmApiKeysrvc = new TmApiKeysrvc();
						tmApiKeysrvc.setSrvcId(srvcId);
						tmApiKeysrvc.setCertkeyId(apiKeySrvcSaveDTO.getCertkeyId());
						tmApiKeysrvcRepository.save(tmApiKeysrvc);
					}
				}
			}
		} catch(Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, CommonUtils.getMessage("api.authenticationKeySaveAjax.fail"));
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, CommonUtils.getMessage("api.authenticationKeySaveAjax.complete"));
	}
	
	/**
	 * @Method Name : apiSttsUpdate
	 * @작성일 : 2024. 7. 11.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  API키 상태값 변경
	 * @param srvcId
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/status/update")
	public @ResponseBody CommonResponse<?> apiSttsUpdate(Model model,@RequestBody TmApiCertkey tmApiCertkey) {
		try {
		    TmApiCertkey certkeyDetail = tmApiCertkeyRepository.findById(tmApiCertkey.getCertkeyId())
		            .orElseThrow(() -> new IllegalArgumentException("Invalid certkeyId: " + tmApiCertkey.getCertkeyId()));

		    certkeyDetail.setSttsCd(tmApiCertkey.getSttsCd());
		    
		    tmApiCertkeyRepository.save(certkeyDetail);
		    return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, CommonUtils.getMessage("api.apiSttsUpdate.complete"));
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, CommonUtils.getMessage("api.apiSttsUpdate.fail"));
		}
	}

	
	/**
	 * @Method Name : apiKeySrvcDelete
	 * @작성일 : 2024. 7. 11.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  API키 , 서비스 연결 제거
	 * @param srvcId,certkeyId
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{certkeyId}/{srvcId}/delete")
	@ResponseBody
	public CommonResponse<?> apiKeySrvcDelete(@PathVariable(name="certkeyId") String certkeyId, @PathVariable(name="srvcId") String srvcId) {
		try {
	        TmApiKeysrvc tmApiKeysrvc = tmApiKeysrvcRepository.findBySrvcIdAndCertkeyId(srvcId, certkeyId);
	        if (tmApiKeysrvc == null) {
	            throw new CommonException(ErrorCode.ENTITY_DATA_NOT_FOUND);
	        }
	        tmApiKeysrvcRepository.deleteBySrvcIdAndCertkeyId(srvcId, certkeyId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(ErrorCode.ENTITY_DELETE_FAILED);
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, CommonUtils.getMessage("api.apiKeySrvcDelete.complete"));
	}
}
