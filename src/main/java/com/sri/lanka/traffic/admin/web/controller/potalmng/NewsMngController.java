package com.sri.lanka.traffic.admin.web.controller.potalmng;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

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

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.entity.TlBbsFile;
import com.sri.lanka.traffic.admin.common.entity.TlBbsFileGrp;
import com.sri.lanka.traffic.admin.common.entity.TlBbsInfo;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.potalmng.PotalMngService;

@Controller
@RequestMapping("/potalmng/news")
public class NewsMngController {
	
	@Autowired
	private QTlBbsInfoRepository qTlBbsInfoRepository;
	
	@Autowired
	private TlBbsInfoRepository tlBbsInfoRepository;
	
	@Autowired
	private PotalMngService potalMngService;
	
	@Autowired
	private TlBbsFileGrpRepository tlBbsFileGrpRepository;
	
	@Autowired
	private TlBbsFileRepository tlBbsFileRepository;
	
	/**
	  * @Method Name : newsListPage
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 목록 화면
	  * @param model
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String newsListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		String type = "news";
		List<TlBbsInfo> sriNewsList = qTlBbsInfoRepository.getBbsList(searchCommonDTO, paging, type);
		long totalCnt = qTlBbsInfoRepository.getTotalCount(searchCommonDTO, type);
		
		paging.setTotalCount(totalCnt);

		model.addAttribute("sriNewsList", sriNewsList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/potalmng/newsList";
	}
	
	/**
	  * @Method Name : potalmngNewsSave
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 등록 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String potalmngNewsSave(Model model) {
		String mngrId = LoginMngrUtils.getUserId();
		model.addAttribute("writer", mngrId);
		return "views/potalmng/modal/newsSave";
	}
	
	/**
	  * @Method Name : newsSave
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 등록
	  * @param tlBbsInfo
	  * @param files
	  * @param extsChk
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@Transactional
	@PostMapping("/save")
	public @ResponseBody CommonResponse<?> newsSave(TlBbsInfo tlBbsInfo
														, @RequestParam("files") List<MultipartFile> files
														, @RequestParam("extsChk") String[] extsChk){
		if (files != null) {
			if (!potalMngService.fileUpload(files, tlBbsInfo, extsChk)) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "소식 등록 실패");
			}
		}
		tlBbsInfoRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "소식이 등록 되었습니다.");
	}
	
	/**
	  * @Method Name : potalmngNewsDetail
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 수정 페이지
	  * @param model
	  * @param brdId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{brdId}")
	public String potalmngNewsDetail(Model model, @PathVariable String brdId) {
		TlBbsInfo findNews = tlBbsInfoRepository.findById(brdId).get();
		Optional<TlBbsFileGrp> tlBbsFileGrp = tlBbsFileGrpRepository.findByBbsId(findNews.getBbsId());
		if (!tlBbsFileGrp.isEmpty()) {
			List<TlBbsFile> tlBbsFile = tlBbsFileRepository.findByFilegrpId(tlBbsFileGrp.get().getFilegrpId());
			
			model.addAttribute("tlBbsFile", tlBbsFile);
		}
		model.addAttribute("tlBbsInfo", findNews);
		
		return "views/potalmng/modal/newsUpdate";
	}
	
	/**
	  * @Method Name : newsUpdate
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 수정
	  * @param tlBbsInfo
	  * @param brdId
	  * @param files
	  * @param extsChk
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@Transactional
	@PutMapping("/{brdId}")
	public @ResponseBody CommonResponse<?> newsUpdate(TlBbsInfo tlBbsInfo
														, @PathVariable String brdId
														, @RequestParam("files") List<MultipartFile> files
														, @RequestParam("extsChk") String[] extsChk){
		tlBbsInfo = potalMngService.setTlBbsInfoInfo(tlBbsInfo, brdId);
		if (files != null) {
			if (!potalMngService.fileUpload(files, tlBbsInfo, extsChk)) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "파일 업로드 실패");
			}
		}
		tlBbsInfoRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "소식이 수정 되었습니다.");
	}
	
	/**
	  * @Method Name : newsDelete
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 소식 선택 삭제
	  * @param body
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping
	public @ResponseBody CommonResponse<?> newsDelete(@RequestBody Map<String, List<String>> body){
		List<String> brdIds = body.get("brdIds");
        for (String brdId : brdIds) {
            try {
				potalMngService.deletePotalAndRelatedFiles(brdId);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "소식 삭제 실패");
			}
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "소식이 삭제 되었습니다.");
	}
	
	/**
	  * @Method Name : fileDelete
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 첨부파일 삭제
	  * @param fileId
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{fileId}")
	public @ResponseBody CommonResponse<?> fileDelete(@PathVariable("fileId") String fileId){
		if (!potalMngService.fileDelete(fileId)) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "첨부파일이 삭제 실패.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "첨부파일이 삭제 되었습니다.");
	}
	
	/**
	  * @Method Name : fileDownload
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 첨부파일 다운로드
	  * @param response
	  * @param fileId
	  */
//	@GetMapping("/download/{fileId}")
//	public void fileDownload(HttpServletResponse response, @PathVariable("fileId") String fileId) {
//		Optional<tTlBbsFile> fileMng = tlBbsFileRepository.findById(fileId);
//		if (fileMng.isPresent()) {
//			FileComponent.fileDownload(response, fileMng.get().getFileNm(), fileMng.get().getFileOriginNm(), fileMng.get().getFilePath());
//		}
//	}
	
}