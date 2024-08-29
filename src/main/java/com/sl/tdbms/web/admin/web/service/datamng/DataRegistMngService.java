package com.sl.tdbms.web.admin.web.service.datamng;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.component.PopulationStatisticsComponent;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationSearchDTO;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.TsPopMngDTO;
import com.sl.tdbms.web.admin.common.entity.TsPopFile;
import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import com.sl.tdbms.web.admin.common.querydsl.QTsPopMngRepository;
import com.sl.tdbms.web.admin.common.repository.TsPopFileRepository;
import com.sl.tdbms.web.admin.common.repository.TsPopMngRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Service
public class DataRegistMngService {
	
	@Autowired
    PopulationStatisticsComponent populationStatisticsComponent;
	
	@Autowired
    QTsPopMngRepository qTsPopMngRepository;
	
	@Autowired
    TsPopMngRepository tsPopMngRepository;
	
	@Autowired
    TsPopFileRepository tsPopFileRepository;
	
	/**
	 * @Method downLoadPopStatsForm
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구통계데이터 양식 조회
	 * @param response
	 * @param popStatTypeCd
	 * @throws IOException
	 */
	public void downLoadPopStatsForm(HttpServletResponse response, String popStatTypeCd) throws IOException {
		populationStatisticsComponent.downLoadPopStatsForm(response, popStatTypeCd);
	}

	/**
	 * @Method populationDataUploadAndSave
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구 통계 데이터 엑셀업로드 및 데이터 등록
	 * @param tsPopMng
	 * @param file
	 * @return
	 */
	@Transactional
	public String populationDataUploadAndSave(TsPopMng tsPopMng, MultipartFile file) {
		return populationStatisticsComponent.populationDataSave(tsPopMng,file);
	}

	/**
	 * @Method getTsPopMngList
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @param populationSearchDTO
	 * @Method 설명 : 인구통계데이터 목록 조회
	 * @param paging
	 * @return
	 */
	public List<TsPopMngDTO> getTsPopMngList(PopulationSearchDTO populationSearchDTO, PagingUtils paging) {
		return qTsPopMngRepository.getTsPopMngList(populationSearchDTO, paging);
	}

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구통계데이터 목록 카운트 조회
	 * @param populationSearchDTO
	 * @return
	 */
	public Long getTotalCount(PopulationSearchDTO populationSearchDTO) {
		return qTsPopMngRepository.getTotalCount(populationSearchDTO);
	}
	
	/**
	 * @Method getPopMngDetail
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구통계데이터 목록 상세 조회
	 * @param popmngId
	 * @return
	 */
	public TsPopMngDTO getPopMngDetail(String popmngId) {
		return qTsPopMngRepository.getTsPopMngDetail(popmngId);
	}

	
	/**
	 * @Method fileDownLoad
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 업로드 파일 다운로드
	 * @param response
	 * @param fileId
	 * @throws IOException 
	 */
	public void fileDownLoad(HttpServletResponse response, String fileId) throws IOException {
		if (CommonUtils.isNull(fileId)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		TsPopFile tsPopFile =tsPopFileRepository.findOneByFileId(fileId);
		
		if(tsPopFile != null) {
			String uploadPath = tsPopFile.getFilePath() + File.separator + tsPopFile.getFileNm();
			File file = new File(uploadPath);
			
			if (!file.exists()) {
				throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
			}
			
			// 파일 이름 설정
			String encodedFileName = URLEncoder.encode(tsPopFile.getOrgFileNm(), "UTF-8").replaceAll("\\+", "%20");
			
			byte[] fileByte;
			fileByte = FileUtils.readFileToByteArray(file);
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + encodedFileName + "\"");
			response.setContentLength(fileByte.length);
			
			try (FileInputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
				int bytesRead = 0;
				byte[] buffer = new byte[4096];
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
			}
		} else {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		}
	}

	@Transactional
	public void deletePopmng(String popmngId) {
	    TsPopMng tsPopMng = tsPopMngRepository.findOneByPopmngId(popmngId);

	    //인구 통계 데이터 삭제 
	    populationStatisticsComponent.populationDataDelete(tsPopMng);
	    
	    if (tsPopMng == null) {
	        throw new CommonException(ErrorCode.ENTITY_DATA_NOT_FOUND);
	    }

	    if (tsPopMng.getTsPopFile() != null) {
	        // 업로드된 파일 제거
	        populationStatisticsComponent.fileDelete(tsPopMng.getTsPopFile());
	        // 파일 제거 후 DB 삭제
	        tsPopFileRepository.delete(tsPopMng.getTsPopFile());
	    }

	    // 상위 테이블 모두 삭제 후 DB 삭제
	    tsPopMngRepository.delete(tsPopMng);
	}

	@Transactional
	public void tsPopMngUpdate(TsPopMng tsPopMng) {
        TsPopMng existingPopMng = tsPopMngRepository.findById(tsPopMng.getPopmngId())
                .orElseThrow(() -> new CommonException(ErrorCode.ENTITY_DATA_NOT_FOUND));
        
        existingPopMng.setPopmngTitle(tsPopMng.getPopmngTitle());
        existingPopMng.setPopmngCnts(tsPopMng.getPopmngCnts());
        tsPopMngRepository.save(existingPopMng);
	}

}
