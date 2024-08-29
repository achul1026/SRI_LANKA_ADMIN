package com.sl.tdbms.web.admin.web.service.potalmng;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.component.FileComponent;
import com.sl.tdbms.web.admin.common.entity.TlBbsFile;
import com.sl.tdbms.web.admin.common.entity.TlBbsFileGrp;
import com.sl.tdbms.web.admin.common.entity.TlBbsInfo;
import com.sl.tdbms.web.admin.common.repository.TlBbsFileGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TlBbsFileRepository;
import com.sl.tdbms.web.admin.common.repository.TlBbsInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;


@Service
public class PotalMngService {
	
	@Autowired
	private TlBbsInfoRepository tlBbsInfoRepository;

	@Autowired
	private TlBbsFileGrpRepository tlBbsFileGrpRepository;
	
	@Autowired
	private TlBbsFileRepository tlBbsFileRepository;
	
	@Autowired
	private FileComponent fileComponent;
	
	/**
	  * @Method Name : setReportInfo
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : TlBbsInfo 정보 설정
	  * @param tlBbsInfo
	  * @param id
	  * @return
	  */
	public TlBbsInfo setTlBbsInfoInfo(TlBbsInfo tlBbsInfo, String id) {
		
		String mngrId = LoginMngrUtils.getUserId();
		Optional<TlBbsInfo> newTlBbsInfo = tlBbsInfoRepository.findById(id);
		if (newTlBbsInfo.isPresent()) {
			if (!CommonUtils.isNull(tlBbsInfo.getBbsType())) newTlBbsInfo.get().setBbsType(tlBbsInfo.getBbsType());
			if (!CommonUtils.isNull(tlBbsInfo.getBbsTitle())) newTlBbsInfo.get().setBbsTitle(tlBbsInfo.getBbsTitle());
			if (!CommonUtils.isNull(tlBbsInfo.getBbsCnts())) newTlBbsInfo.get().setBbsCnts(tlBbsInfo.getBbsCnts());
			if (!CommonUtils.isNull(tlBbsInfo.getDspyYn())) newTlBbsInfo.get().setDspyYn(tlBbsInfo.getDspyYn());
		}
		newTlBbsInfo.get().setUpdtId(mngrId);
		return newTlBbsInfo.get();
	}
	
	/**
	  * @Method Name : deleteNoticeAndRelatedFiles
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈 게시글과 관련 파일 삭제
	  * @param brdId
	  * @return
	  * @throws Exception
	  */
	@Transactional
	public void deletePotalAndRelatedFiles(String brdId) throws Exception {
		tlBbsInfoRepository.deleteById(brdId);

	    Optional<TlBbsFileGrp> tlBbsFileGrp = tlBbsFileGrpRepository.findByBbsId(brdId);
	    if (tlBbsFileGrp.isPresent()) {
	    	String filegrpId = tlBbsFileGrp.get().getFilegrpId();

		    List<TlBbsFile> tlBbsFile = tlBbsFileRepository.findByFilegrpId(filegrpId);
		    for(TlBbsFile file : tlBbsFile) {
		        if (!fileDelete(file.getFileId())) {}
		    }
//		    tlBbsFileGrpRepository.deleteById(tlBbsFileGrp.get().getFilegrpId());
	    }
	}
	
	/**
	  * @Method Name : fileUpload
	  * @작성일 : 2024. 2. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈 등록하기 파일 첨부
	  * @param files
	  * @param tlBbsInfo
	  * @param extChk
	  * @return
	  */
	public void fileUpload(List<MultipartFile> files, TlBbsInfo tlBbsInfo) {
		String resMsg = CommonUtils.getMessage("board.fileUpload.fail");
		try {

			// 파일 개수 검증
		    if (files.size() > 5) {
		    	resMsg = CommonUtils.getMessage("board.fileUpload.exceed.length");
		        throw new CommonResponseException(ErrorCode.FILE_UPLOAD_FAILED,resMsg);
		    }

		    // 전체 파일 크기 검증 25MB
		    long totalSize = 0;
		    for (MultipartFile file : files) {
		        totalSize += file.getSize();
		    }

		    if (totalSize > 25 * 1024 * 1024) {
		    	resMsg = CommonUtils.getMessage("board.fileUpload.exceed.size");
		    	throw new CommonResponseException(ErrorCode.FILE_UPLOAD_FAILED,resMsg);
		    }
			
		    Optional<TlBbsFileGrp> tlBbsFileGrpOptional = tlBbsFileGrpRepository.findByBbsId(tlBbsInfo.getBbsId());
		    TlBbsFileGrp tlBbsFileGrp;
		    
		    if (tlBbsFileGrpOptional.isEmpty()) {
		        // 비어있을 경우 새 인스턴스 생성
		    	tlBbsFileGrp = new TlBbsFileGrp();
		    	tlBbsFileGrp.setBbsId(tlBbsInfo.getBbsId());
		    } else {
		        // 이미 존재하는 경우
		    	tlBbsFileGrp = tlBbsFileGrpOptional.get();
		    }
		    fileComponent.fileUpload(files, tlBbsFileGrp.getFilegrpId());
		    
		    tlBbsInfoRepository.save(tlBbsInfo);
		    tlBbsFileGrpRepository.save(tlBbsFileGrp);
		} catch (Exception e) {
			throw new CommonResponseException(ErrorCode.FILE_UPLOAD_FAILED, resMsg);
		}
	}
	
	/**
	  * @Method Name : fileDelete
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 디렉토리 파일 삭제
	  * @param fileId
	  * @return
	  */
	public boolean fileDelete(String fileId) {
		Optional<TlBbsFile> fileMng = tlBbsFileRepository.findById(fileId);
		if (fileMng.isPresent()) {
			
			if(!fileComponent.fileDelete(fileMng.get())) return false;
			tlBbsFileRepository.deleteById(fileId);
			
			Optional<TlBbsFile> fileMngChk = tlBbsFileRepository.findOneByFilegrpId(fileMng.get().getFilegrpId());
			if (!fileMngChk.isPresent()) tlBbsFileGrpRepository.deleteById(fileMng.get().getFilegrpId());
		}
		return true;
	}

}
