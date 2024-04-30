package com.sri.lanka.traffic.admin.web.service.potalmng;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sri.lanka.traffic.admin.common.component.FileComponent;
import com.sri.lanka.traffic.admin.common.entity.TlBbsFile;
import com.sri.lanka.traffic.admin.common.entity.TlBbsFileGrp;
import com.sri.lanka.traffic.admin.common.entity.TlBbsInfo;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;


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
	public void deletePotalAndRelatedFiles(String brdId) throws Exception {
		tlBbsInfoRepository.deleteById(brdId);

	    Optional<TlBbsFileGrp> tlBbsFileGrp = tlBbsFileGrpRepository.findByBbsId(brdId);
	    if (tlBbsFileGrp.isPresent()) {
	    	String filegrpId = tlBbsFileGrp.get().getFilegrpId();

		    List<TlBbsFile> tlBbsFile = tlBbsFileRepository.findByFilegrpId(filegrpId);
		    for(TlBbsFile file : tlBbsFile) {
		        if (!fileDelete(file.getFileId())) {
		        }
		    }
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
	public boolean fileUpload(List<MultipartFile> files, TlBbsInfo tlBbsInfo, String[] extChk) {
	    Optional<TlBbsFileGrp> tlBbsFileGrpOptional = tlBbsFileGrpRepository.findByBbsId(tlBbsInfo.getBbsId());
	    TlBbsFileGrp tlBbsFileGrp;
	    
	    if (tlBbsFileGrpOptional.isEmpty()) {
	        // Optional이 비어있을 경우 새 인스턴스 생성
	    	tlBbsFileGrp = new TlBbsFileGrp();
	    	tlBbsFileGrp.setBbsId(tlBbsInfo.getBbsId());
	    } else {
	        // 이미 존재하는 경우
	    	tlBbsFileGrp = tlBbsFileGrpOptional.get();
	    }
	    if (!fileComponent.fileUpload(files, tlBbsFileGrp.getFilegrpId(), extChk)) return false;
	    
	    tlBbsInfoRepository.save(tlBbsInfo);
	    tlBbsFileGrpRepository.save(tlBbsFileGrp);
	    return true;
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
			if(!fileComponent.fileDelete(fileMng.get())) { 
				return false;
			}
			tlBbsFileRepository.deleteById(fileId);
			
			Optional<TlBbsFile> extraFileMng = tlBbsFileRepository.findOneByFilegrpId(fileMng.get().getFilegrpId());
			if (extraFileMng.isEmpty()) {
				tlBbsFileGrpRepository.deleteById(fileMng.get().getFilegrpId());
			}
		}
		return true;
	}

}
