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
import com.sri.lanka.traffic.admin.common.enums.code.BbsTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.potalmng.PotalMngService;

/**
  * @FileName : BoardMngController.java
  * @Project : sri_lanka_admin
  * @Date : 2024. 5. 3.
  * @작성자 : SM.KIM
  * @프로그램 설명 : 
  */
@Controller
@RequestMapping("/potalmng/board")
public class BoardMngController {
	
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
	  * @Method Name : boardListPage
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시판 목록 화면
	  * @param model
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String boardListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TlBbsInfo> boardList = qTlBbsInfoRepository.getBbsList(searchCommonDTO, paging);
		long totalCnt = qTlBbsInfoRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);

		model.addAttribute("bbsCdList", BbsTypeCd.values());
		model.addAttribute("boardList", boardList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/potalmng/boardList";
	}
	
	/**
	  * @Method Name : potalmngBoardSave
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 등록 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/save")
	public String potalmngBoardSave(Model model) {
		String mngrId = LoginMngrUtils.getUserId();
		model.addAttribute("writer", mngrId);
		model.addAttribute("bbsCdList", BbsTypeCd.values());
		return "views/potalmng/modal/boardSave";
	}
	
	/**
	  * @Method Name : boardSave
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 등록
	  * @param tlBbsInfo
	  * @param files
	  * @param extsChk
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@Transactional
	@PostMapping("/save")
	public @ResponseBody CommonResponse<?> boardSave(TlBbsInfo tlBbsInfo
														, @RequestParam("files") List<MultipartFile> files
														, @RequestParam("extsChk") String[] extsChk){
		int checkNum =1;
		for(MultipartFile file:files){
			if(file.isEmpty()) checkNum=0;
		}
		if(checkNum==1) {
			if (!potalMngService.fileUpload(files, tlBbsInfo, extsChk)) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "게시글 등록 실패");
			}
		}
		tlBbsInfoRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "게시글이 등록 되었습니다.");
	}
	
	/**
	  * @Method Name : potalmngBoardDetail
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 상세 페이지
	  * @param model
	  * @param bbsId
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{bbsId}")
	public String potalmngBoardDetail(Model model, @PathVariable String bbsId) {
		TlBbsInfo findBoard = tlBbsInfoRepository.findById(bbsId).get();
		Optional<TlBbsFileGrp> tlBbsFileGrp = tlBbsFileGrpRepository.findByBbsId(findBoard.getBbsId());
		if (!tlBbsFileGrp.isEmpty()) {
			List<TlBbsFile> tlBbsFile = tlBbsFileRepository.findByFilegrpId(tlBbsFileGrp.get().getFilegrpId());
			
			model.addAttribute("tlBbsFile", tlBbsFile);
		}
		model.addAttribute("tlBbsInfo", findBoard);
		model.addAttribute("bbsCdList", BbsTypeCd.values());
		
		return "views/potalmng/modal/boardUpdate";
	}
	
	/**
	  * @Method Name : boardUpdate
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 수정
	  * @param tlBbsInfo
	  * @param bbsId
	  * @param files
	  * @param extsChk
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@Transactional
	@PutMapping("/{bbsId}")
	public @ResponseBody CommonResponse<?> boardUpdate(TlBbsInfo tlBbsInfo
														, @PathVariable String bbsId
														, @RequestParam("files") List<MultipartFile> files
														, @RequestParam("extsChk") String[] extsChk){
		tlBbsInfo = potalMngService.setTlBbsInfoInfo(tlBbsInfo, bbsId);
		int checkNum =1;
		for(MultipartFile file:files){
			if(file.isEmpty()) checkNum=0;
		}
		if(checkNum==1) {
			if (!potalMngService.fileUpload(files, tlBbsInfo, extsChk)) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "파일 업로드 실패");
			}
		}
		tlBbsInfoRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "게시글이 수정 되었습니다.");
	}
	
	/**
	  * @Method Name : boardDelete
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 삭제
	  * @param body
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping
	public @ResponseBody CommonResponse<?> boardDelete(@RequestBody Map<String, List<String>> body){
		List<String> bbsIds = body.get("bbsIds");
        for (String bbsId : bbsIds) {
            try {
				potalMngService.deletePotalAndRelatedFiles(bbsId);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "게시글 삭제 실패");
			}
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "게시글이 삭제 되었습니다.");
	}
	
	/**
	  * @Method Name : fileDelete
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 파일 삭제
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
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 파일 다운로드
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