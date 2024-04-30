package com.sri.lanka.traffic.admin.common.component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.sri.lanka.traffic.admin.common.entity.TlBbsFile;
import com.sri.lanka.traffic.admin.common.repository.TlBbsFileRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;

@Component
public class FileComponent {
	
	@Autowired
	private TlBbsFileRepository tlBbsFileRepository;

	@Value("${file.upload.path}")
	String uploadPath;
	
	@Value("${file.sample.download.path}")
	public String fileSampleDownloadPath;
	
	/**
	  * @Method Name : fileUpload
	  * @작성일 : 2024. 2. 2.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 파일 업로드
	  * @param files
	  * @param fileGroupId
	  * @param extsChk
	  * @return
	  */
	public boolean fileUpload(List<MultipartFile> files, String fileGroupId, String[] extsChk) {
		for (MultipartFile file : files) {
	        TlBbsFile tlBbsfile = new TlBbsFile();
	        String fileOriginName = file.getOriginalFilename();
	        String fileName = CommonUtils.getUuid() + fileOriginName;
	        String ext = FileComponent.getExt(fileOriginName, ".");
	        // 확장자가 유효한지 체크
	        for (String chk  : extsChk) {
				if ( chk.equals(ext) ) {
					try {
						tlBbsfile.setFileNm(fileName);
						tlBbsfile.setFileFilext(ext);
						tlBbsfile.setOrgFilenm(fileOriginName);
						tlBbsfile.setFilePath(uploadPath);
						tlBbsfile.setFileSize(new BigDecimal(file.getSize()));
						tlBbsfile.setFilegrpId(fileGroupId);
						
						tlBbsFileRepository.save(tlBbsfile);
						
						file.transferTo(new File(uploadPath, fileName));
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
	    }
		return true;
	}
	
	/**
	  * @Method Name : getExt
	  * @작성일 : 2024. 2. 2.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 확장자 추출
	  * @param fileName
	  * @param replaceStr
	  * @return
	  */
	public static String getExt(String fileName, String replaceStr) {
		
		String resultExt = fileName;
		try {
			int lastIdx = fileName.lastIndexOf(replaceStr);
			resultExt = fileName.substring(lastIdx + 1, fileName.length());
		} catch (Exception e) {
			resultExt = fileName;
		}
		
		return resultExt;
	}
	
	/**
	  * @Method Name : fileDelete
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 디렉토리에서 파일 삭제
	  * @param tlBbsfile
	  * @return
	  */
	public boolean fileDelete(TlBbsFile tlBbsfile) {
		File file = new File(tlBbsfile.getFilePath() + tlBbsfile.getFileNm());
		try {
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	  * @Method Name : fileDownload
	  * @작성일 : 2024. 2. 5.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 파일 다운로드
	  * @param response
	  * @param fileName
	  * @param fileOriginName
	  * @param fileSampleDownloadPath
	  */
//	public static void fileDownload(HttpServletResponse response, String fileName, String fileOriginName, String fileSampleDownloadPath) {
//		try {
//			String filePath = fileSampleDownloadPath + fileName;
//	    	ClassPathResource resource = new ClassPathResource(filePath);
//	    	File file = new File(resource.getPath());
//	        // 파일 길이를 가져온다.
//	        int fSize = (int) file.length();
//	        // 파일이 존재
//	        if (fSize > 0) {
//	        	String encodedFilename = "attachment; filename*=UTF-8''" + URLEncoder.encode(fileOriginName, "UTF-8");
//	            response.setContentType("text/csv; charset=UTF-8");
//	            response.setHeader("Content-Disposition", encodedFilename);
//	            response.setContentLengthLong(fSize);
//	            BufferedInputStream in = null;
//	            BufferedOutputStream out = null;
//	            in = new BufferedInputStream(new FileInputStream(file));
//	            out = new BufferedOutputStream(response.getOutputStream());
//	            try {
//	                byte[] buffer = new byte[4096];
//	                int bytesRead = 0;
//	                while ((bytesRead = in.read(buffer)) != -1) {
//	                    out.write(buffer, 0, bytesRead);
//	                }
//	                out.flush();
//	            } finally {
//	                in.close();
//	                out.close();
//	            }
//	        } else {
//	            throw new FileNotFoundException("파일이 없습니다.");
//	        }
//		
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//	}
	
	public void sampleFileDownload(HttpServletResponse response,String fileName) {
        
        try {
        	String filePath = fileSampleDownloadPath+fileName;
        	ClassPathResource resource = new ClassPathResource(filePath);
        	File file = new File(resource.getURL().getPath());
            // 파일 길이를 가져온다.
            int fSize = (int) file.length();
            // 파일이 존재
            if (fSize > 0) {
                String encodedFilename = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileName, "UTF-8");
                response.setContentType("text/csv; charset=UTF-8");
                response.setHeader("Content-Disposition", encodedFilename);
                response.setContentLengthLong(fSize);

                BufferedInputStream in = null;
                BufferedOutputStream out = null;

                in = new BufferedInputStream(new FileInputStream(file));

                out = new BufferedOutputStream(response.getOutputStream());

                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = in .read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            } else {
                throw new FileNotFoundException("파일이 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED, "File download failed");
        }
	}
	
}
