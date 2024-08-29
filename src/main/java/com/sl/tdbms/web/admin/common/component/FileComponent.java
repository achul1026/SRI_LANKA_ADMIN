package com.sl.tdbms.web.admin.common.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.dto.invst.InvstrResultDTO;
import com.sl.tdbms.web.admin.common.entity.TlBbsFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.sl.tdbms.web.admin.common.repository.TlBbsFileRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

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
	public void fileUpload(List<MultipartFile> files, String fileGroupId) {
		
		try {
			// 포털 관리 게시물 확장자 제한
			String[] extsChk = {"jpg", "png"};
			String folderPth = makeDirectory(uploadPath + File.separator + LocalDate.now());
			for (MultipartFile file : files) {
		        TlBbsFile tlBbsfile = new TlBbsFile();
		        String fileOriginName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(fileOriginName);
		        String fileName = CommonUtils.getUuid() + "." + ext;


		        // 확장자가 유효한지 체크
				if ( Arrays.asList(extsChk).contains(ext) ) {
					tlBbsfile.setFileNm(fileName);
					tlBbsfile.setFileFilext(ext);
					tlBbsfile.setOrgFilenm(fileOriginName);
					tlBbsfile.setFilePath(folderPth);
					tlBbsfile.setFileSize(new BigDecimal(file.getSize()));
					tlBbsfile.setFilegrpId(fileGroupId);
					
					tlBbsFileRepository.save(tlBbsfile);
					
					file.transferTo(new File(folderPth, fileName));
				} else {
		        	throw new CommonResponseException(ErrorCode.FILE_UPLOAD_FAILED);
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonResponseException(ErrorCode.FILE_UPLOAD_FAILED);
		}
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
		File file = new File(tlBbsfile.getFilePath() + File.separator + tlBbsfile.getFileNm());
		try {
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
     * @Method Name : makeDirectory
     * @작성일 : 2023. 9. 11.
     * @작성자 : KC.KIM
     * @Method 설명 : 파일 경로 생성
     * @return
     */
	private String makeDirectory(String path) {
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getPath();
	}

	/**
     * @Method Name : uploadInvstrBulkExcel
     * @작성일 : 2024. 8. 26.
     * @작성자 : KC.KIM
     * @Method 설명 : 모달 외부 조사원 대량 등록 엑셀 파일 데이터 조회
     * @return
     */
	public List<InvstrResultDTO> uploadInvstrBulkExcel(MultipartFile file) {
		List<InvstrResultDTO> result = new ArrayList<InvstrResultDTO>();
		
		try {
			InputStream is = file.getInputStream();
			
			Workbook workbook = new XSSFWorkbook(is);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			int rowNum = sheet.getPhysicalNumberOfRows();
			
			for(int i=1; i<rowNum; i++) {
				InvstrResultDTO invstrResultDTO = new InvstrResultDTO();
				Row row = sheet.getRow(i);
				
				int cellNum = row.getPhysicalNumberOfCells();
				for(int j=0; j<cellNum; j++) {
					switch (j) {
					case 0:
						invstrResultDTO.setName(row.getCell(j).getStringCellValue());
						break;
					case 1:
						invstrResultDTO.setContact(row.getCell(j).getStringCellValue());
						break;
					case 2:
						invstrResultDTO.setEmail(row.getCell(j).getStringCellValue());
						break;
					case 3:
						String role = row.getCell(j).getStringCellValue();
						
						if(CommonUtils.getMessage("invst.invstDetail.leader").equals(role)) {
							role = "LEADER";
						}else if(CommonUtils.getMessage("invst.invstDetail.member").equals(role)){
							role = "MEMBER";							
						}
						
						invstrResultDTO.setRole(role);
						break;
					default:
						break;
					}
				}
				result.add(invstrResultDTO);
			}
		} catch (IOException e) {
			throw new CommonException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		
		return result;
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
	
//	public void sampleFileDownload(HttpServletResponse response,String fileName) {
//        
//        try {
//        	String filePath = fileSampleDownloadPath+fileName;
//        	ClassPathResource resource = new ClassPathResource(filePath);
//        	File file = new File(resource.getURL().getPath());
//            // 파일 길이를 가져온다.
//            int fSize = (int) file.length();
//            // 파일이 존재
//            if (fSize > 0) {
//                String encodedFilename = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileName, "UTF-8");
//                response.setContentType("text/csv; charset=UTF-8");
//                response.setHeader("Content-Disposition", encodedFilename);
//                response.setContentLengthLong(fSize);
//
//                BufferedInputStream in = null;
//                BufferedOutputStream out = null;
//
//                in = new BufferedInputStream(new FileInputStream(file));
//
//                out = new BufferedOutputStream(response.getOutputStream());
//
//                try {
//                    byte[] buffer = new byte[4096];
//                    int bytesRead = 0;
//                    while ((bytesRead = in .read(buffer)) != -1) {
//                        out.write(buffer, 0, bytesRead);
//                    }
//                    out.flush();
//                } finally {
//                    in.close();
//                    out.close();
//                }
//            } else {
//                throw new FileNotFoundException("파일이 없습니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED, "File download failed");
//        }
//	}
	
}
