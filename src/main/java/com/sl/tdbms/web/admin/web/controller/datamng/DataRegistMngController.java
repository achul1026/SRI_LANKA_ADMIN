package com.sl.tdbms.web.admin.web.controller.datamng;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sl.tdbms.web.admin.common.component.FileComponent;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationSearchDTO;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.TsPopMngDTO;
import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.web.service.datamng.DataRegistMngService;
import com.sl.tdbms.web.admin.web.service.systemmng.CodeMngService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Controller
@RequestMapping("/datamng/dataregist")
public class DataRegistMngController {
	
	@Autowired
    CodeMngService codeMngService;
	
	@Autowired
    QTcUserMngRepository qTcUserMngRepository;
	
	@Autowired
    DataRegistMngService dataRegistMngService;
	
	@Autowired
    FileComponent fileComponent;
	
	/**
	  * @Method Name : dataRegistList
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 목록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String dataRegistListPage(Model model, PopulationSearchDTO populationSearchDTO , PagingUtils paging) {
		Long totalCnt = dataRegistMngService.getTotalCount(populationSearchDTO);
		paging.setTotalCount(totalCnt);

		List<TsPopMngDTO> tsPopMngList = dataRegistMngService.getTsPopMngList(populationSearchDTO, paging);
		model.addAttribute("popStatTypeCd", PopStatsTypeCd.values());
		model.addAttribute("bffltdCd", codeMngService.getTcCdInfoForGrpCd("BFFLTD_CD"));
		model.addAttribute("tsPopMngList", tsPopMngList);
		model.addAttribute("paging", paging);
		model.addAttribute("searchOption", populationSearchDTO);
		return "views/datamng/dataRegistList";
	}

	/**
	  * @Method Name : dataRegistSavePage
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 등록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String dataRegistSavePage(Model model) {
		String userId = LoginMngrUtils.getUserId();
		model.addAttribute("loginUser", qTcUserMngRepository.getTcUserInfoByUserId(userId));
		model.addAttribute("popStatTypeCd", PopStatsTypeCd.values());
		return "views/datamng/dataRegistSave";
	}
	
	/**
	  * @Method Name : dataRegistSave
	  * @작성일 : 2024. 6. 05.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 데이터 등록 등록 화면
	  * @param  TsPopMng , multipartFile
	  * @return ResponseEntity
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/save")
	@ResponseBody
	public CommonResponse<?> dataRegistSave(TsPopMng tsPopMng, MultipartFile file){
		String popmngId = "";
		try {
			popmngId = dataRegistMngService.populationDataUploadAndSave(tsPopMng,file);
		}catch(CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		String resMsg = CommonUtils.getMessage("dataRegist.dataRegistSave.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg,popmngId);
	}
	
	/**
	 * @Method Name : excelFormDownload
	 * @작성일 : 2024. 6. 5.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구데이터통계 양식
	 * @param response , popStatTypeCd
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/excelDownload")
	public void excelFormDownload(HttpServletResponse response , @RequestParam(name="popStatTypeCd") String popStatTypeCd) {
		try {
			dataRegistMngService.downLoadPopStatsForm(response, popStatTypeCd);
		} catch (IOException e) {
			throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
	}
	
	/**
	  * @Method Name : dataRegistUpdatePage
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 수정 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/update")
	public String dataRegistUpdatePage(Model model, @RequestParam(name="popmngId") String popmngId){
		model.addAttribute("tsPopMngDTO",dataRegistMngService.getPopMngDetail(popmngId));
		return "views/datamng/dataRegistUpdate";
	}
	
	/**
	  * @Method Name : dataRegistUpdate
	  * @작성일 : 2024. 6. 17.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 데이터 등록 수정 로직
	  * @param  TsPopMng
	  * @return ResponseEntity
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/update")
	@ResponseBody
	public CommonResponse<?> dataRegistUpdate(@RequestBody TsPopMng tsPopMng){
		dataRegistMngService.tsPopMngUpdate(tsPopMng);
		String resMsg = CommonUtils.getMessage("dataRegist.dataRegistUpdate.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
	
	/**
	 * @Method Name : excelFormDownload
	 * @작성일 : 2024. 6. 5.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구데이터통계 양식
	 * @param response , popStatTypeCd
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/fileDownload")
	public void fileDownLoad(HttpServletResponse response , @RequestParam(name="fileId") String fileId) {
		try {
			dataRegistMngService.fileDownLoad(response, fileId);
		} catch (IOException e) {
			throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
	}
	
	/**
	 * @Method Name : dataRegistDelete
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  인구 통계 데이터 삭제
	 * @param popmngId
	 */
	@Authority(authType = AuthType.READ)
	@DeleteMapping("/{popmngId}/delete")
	@ResponseBody
	public CommonResponse<?> dataRegistDelete(@PathVariable String popmngId) {
		try {
			dataRegistMngService.deletePopmng(popmngId);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_DELETE_FAILED);
		}
		String resMsg = CommonUtils.getMessage("dataRegist.dataRegistDelete.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
	
}
