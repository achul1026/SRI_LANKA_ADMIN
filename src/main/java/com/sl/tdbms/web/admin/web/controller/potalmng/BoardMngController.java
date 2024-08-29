package com.sl.tdbms.web.admin.web.controller.potalmng;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.bbs.BbsInfoFile;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TlBbsInfo;
import com.sl.tdbms.web.admin.common.enums.code.BbsTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTlBbsInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TlBbsInfoRepository;
import com.sl.tdbms.web.admin.web.service.potalmng.PotalMngService;
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
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;

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
	private QTcCdInfoRepository qTcCdInfoRepository;
	
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
		
		List<TcCdInfoDTO> bbsCdList = qTcCdInfoRepository.getTcCdInfoListNotCd(GroupCode.BBS_TYPE_CD.getCode(), BbsTypeCd.ALARM.getCode());
		
		model.addAttribute("bbsCdList", bbsCdList);
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
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String potalmngBoardSave(Model model) {
		String mngrId = LoginMngrUtils.getUserId();
		List<TcCdInfoDTO> bbsCdList = qTcCdInfoRepository.getTcCdInfoListNotCd(GroupCode.BBS_TYPE_CD.getCode(),BbsTypeCd.ALARM.getCode());
		model.addAttribute("writer", mngrId);
		model.addAttribute("bbsCdList", bbsCdList);
		return "views/potalmng/boardSave";
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
														, @RequestParam("files") List<MultipartFile> files){
		String resMsg = CommonUtils.getMessage("board.boardSave.complete");
		try {
    		if(!files.get(0).isEmpty()) potalMngService.fileUpload(files, tlBbsInfo);
    		
			tlBbsInfoRepository.save(tlBbsInfo);
			
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
			
		} catch(CommonResponseException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("board.boardSave.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
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
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{bbsId}")
	public String potalmngBoardDetail(Model model, @PathVariable String bbsId) {
		BbsInfoFile findBoard = qTlBbsInfoRepository.getBbsInfo(bbsId);
//		TlBbsInfo findBoard = tlBbsInfoRepository.findById(bbsId).get();
		if (!CommonUtils.isListNull(findBoard.getSubFiles())) {
			model.addAttribute("tlBbsFile", findBoard.getSubFiles());
		}
		model.addAttribute("tlBbsInfo", findBoard);
		
		return "views/potalmng/boardDetail";
	}
	
	/**
	  * @Method Name : potalmngBoardUpdate
	  * @작성일 : 2024. 6. 21.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 게시글 수정 페이지
	  * @param model
	  * @param bbsId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{bbsId}/update")
	public String potalmngBoardUpdate(Model model, @PathVariable String bbsId) {
		BbsInfoFile findBoard = qTlBbsInfoRepository.getBbsInfo(bbsId);
		if (!CommonUtils.isListNull(findBoard.getSubFiles())) {
			model.addAttribute("tlBbsFile", findBoard.getSubFiles());
		}
		List<TcCdInfoDTO> bbsCdList = qTcCdInfoRepository.getTcCdInfoListNotCd(GroupCode.BBS_TYPE_CD.getCode(),BbsTypeCd.ALARM.getCode());

		model.addAttribute("tlBbsInfo", findBoard);
		model.addAttribute("bbsCdList", bbsCdList);
		
		return "views/potalmng/boardUpdate";
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
														, @RequestParam("files") List<MultipartFile> files){
		String resMsg = CommonUtils.getMessage("board.boardUpdate.complete");
		try {
			tlBbsInfo = potalMngService.setTlBbsInfoInfo(tlBbsInfo, bbsId);
			
			if(!files.get(0).isEmpty()) potalMngService.fileUpload(files, tlBbsInfo);
			
			tlBbsInfoRepository.save(tlBbsInfo);
			
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (CommonResponseException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("board.boardUpdate.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
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
		String resMsg = CommonUtils.getMessage("board.boardDelete.complete");
		List<String> bbsIds = body.get("bbsIds");
        for (String bbsId : bbsIds) {
            try {
				potalMngService.deletePotalAndRelatedFiles(bbsId);
			} catch (Exception e) {
				resMsg = CommonUtils.getMessage("board.boardDelete.fail");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, resMsg);
			}
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
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
		String resMsg = CommonUtils.getMessage("board.fileDelete.complete");
		if (!potalMngService.fileDelete(fileId)) {
			resMsg = CommonUtils.getMessage("board.fileDelete.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, resMsg);
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
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