package com.sl.tdbms.web.admin.common.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationFileDTO;
import com.sl.tdbms.web.admin.common.querydsl.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationStatsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TcDsdMng;
import com.sl.tdbms.web.admin.common.entity.TcGnMng;
import com.sl.tdbms.web.admin.common.entity.TsAgePop;
import com.sl.tdbms.web.admin.common.entity.TsEcnmcActPop;
import com.sl.tdbms.web.admin.common.entity.TsEmplymntStatusPop;
import com.sl.tdbms.web.admin.common.entity.TsFacultyCyclePop;
import com.sl.tdbms.web.admin.common.entity.TsFacultyEducationPop;
import com.sl.tdbms.web.admin.common.entity.TsFacultyStudentsPop;
import com.sl.tdbms.web.admin.common.entity.TsGrossRegnProdPop;
import com.sl.tdbms.web.admin.common.entity.TsHouseHoldPop;
import com.sl.tdbms.web.admin.common.entity.TsHousingTypePop;
import com.sl.tdbms.web.admin.common.entity.TsIdstryFacFormalPop;
import com.sl.tdbms.web.admin.common.entity.TsIdstryFacInformalPop;
import com.sl.tdbms.web.admin.common.entity.TsIndstrialOrdSectorsPop;
import com.sl.tdbms.web.admin.common.entity.TsLandAreaPop;
import com.sl.tdbms.web.admin.common.entity.TsNationalSchoolPop;
import com.sl.tdbms.web.admin.common.entity.TsOccHousingUnitPop;
import com.sl.tdbms.web.admin.common.entity.TsPirivenaSchoolPop;
import com.sl.tdbms.web.admin.common.entity.TsPopFile;
import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import com.sl.tdbms.web.admin.common.entity.TsPrivateSchoolPop;
import com.sl.tdbms.web.admin.common.entity.TsRegnPop;
import com.sl.tdbms.web.admin.common.entity.TsSchoolSystemPop;
import com.sl.tdbms.web.admin.common.entity.TsSpecialSchoolPop;
import com.sl.tdbms.web.admin.common.entity.TsStdntPop;
import com.sl.tdbms.web.admin.common.entity.TsVehicleRegPop;
import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;
import com.sl.tdbms.web.admin.common.repository.TcDsdMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcGnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TsAgePopRepository;
import com.sl.tdbms.web.admin.common.repository.TsEcnmcActPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsEmplymntStatusPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsFacultyCyclePopRepository;
import com.sl.tdbms.web.admin.common.repository.TsFacultyEducationPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsFacultyStudentsPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsGrossRegnProdPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsHouseHoldPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsHousingTypePopRepository;
import com.sl.tdbms.web.admin.common.repository.TsIdstryFacFormalPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsIdstryFacInformalPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsIndstrialOrdSectorsPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsLandAreaPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsNationalSchoolPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsOccHousingUnitPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsPirivenaSchoolPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsPopFileRepository;
import com.sl.tdbms.web.admin.common.repository.TsPopMngRepository;
import com.sl.tdbms.web.admin.common.repository.TsPrivateSchoolPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsRegnPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsSchoolSystemPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsSpecialSchoolPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsStdntPopRepository;
import com.sl.tdbms.web.admin.common.repository.TsVehicleRegPopRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Component
public class PopulationStatisticsComponent {
	
	@Autowired
	TsPopMngRepository tsPopMngRepository;
	
	@Autowired
	TsPopFileRepository tsPopFileRepository;
	
	@Autowired
	TcDsdMngRepository tcDsdMngRepository;
	
	@Autowired
	TcGnMngRepository tcGnMngRepository;
	
	@Autowired
	TsRegnPopRepository tsRegnPopRepository;
	
	@Autowired
	TsAgePopRepository tsAgePopRepository;
	
	@Autowired
	TsEcnmcActPopRepository tsEcnmcActPopRepository;
	
	@Autowired
	TsStdntPopRepository tsStdntPopRepository;
	
	@Autowired
	TsHouseHoldPopRepository tsHouseHoldPopRepository;
	
	@Autowired
	TsHousingTypePopRepository tsHousingTypePopRepository;
	
	@Autowired
	TsOccHousingUnitPopRepository tsOccHousingUnitPopRepository;
	
	@Autowired
	TsNationalSchoolPopRepository tsNationalSchoolPopRepository;
	
	@Autowired
	TsPrivateSchoolPopRepository tsPrivateSchoolPopRepository;
	
	@Autowired
	TsSpecialSchoolPopRepository tsSpecialSchoolPopRepository;
	
	@Autowired
	TsPirivenaSchoolPopRepository tsPirivenaSchoolPopRepository;
	
	@Autowired
	TsFacultyEducationPopRepository tsFacultyEducationPopRepository;
	
	@Autowired
	TsFacultyStudentsPopRepository tsFacultyStudentsPopRepository;
	
	@Autowired
	TsFacultyCyclePopRepository tsFacultyCyclePopRepository;
	
	@Autowired
	TsSchoolSystemPopRepository tsSchoolSystemPopRepository;
	
	@Autowired
	TsIdstryFacInformalPopRepository tsIdstryFacInformalPopRepository;
	
	@Autowired
	TsIdstryFacFormalPopRepository tsIdstryFacFormalPopRepository;
	
	@Autowired
	TsIndstrialOrdSectorsPopRepository tsIndstrialOrdSectorsPopRepository;
	
	@Autowired
	TsEmplymntStatusPopRepository tsEmplymntStatusPopRepository;
	
	@Autowired
	TsLandAreaPopRepository tsLandAreaPopRepository;
	
	@Autowired
	TsGrossRegnProdPopRepository tsGrossRegnProdPopRepository;
	
	@Autowired
	TsVehicleRegPopRepository tsVehicleRegPopRepository;
	
	@Autowired
    QTsRegnPopRepository qTsRegnPopRepository;
	
	@Autowired
    QTsAgePopRepository qTsAgePopRepository;
	
	@Autowired
    QTsEcnmcActPopRepository qTsEcnmcActPopRepository;
	
	@Autowired
    QTsStdntPopRepository qTsStdntPopRepository;
	
	@Autowired
    QTsHouseHoldPopRepository qTsHouseHoldPopRepository;
	
	@Autowired
	QTsHousingTypePopRepository qTsHousingTypePopRepository;
	
	@Autowired
	QTsOccHousingUnitPopRepository qTsOccHousingUnitPopRepository;
	
	@Autowired
	QTsNationalSchoolPopRepository qTsNationalSchoolPopRepository;
	
	@Autowired
	QTsPrivateSchoolPopRepository qTsPrivateSchoolPopRepository;
	
	@Autowired
	QTsSpecialSchoolPopRepository qTsSpecialSchoolPopRepository;
	
	@Autowired
	QTsPirivenaSchoolPopRepository qTsPirivenaSchoolPopRepository;
	
	@Autowired
	QTsFacultyEducationPopRepository qTsFacultyEducationPopRepository;
	
	@Autowired
	QTsFacultyStudentsPopRepository qTsFacultyStudentsPopRepository;

	@Autowired
	QTsFacultyCyclePopRepository qTsFacultyCyclePopRepository;
	
	@Autowired
	QTsSchoolSystemPopRepository qTsSchoolSystemPopRepository;
	
	@Autowired
	QTsIdstryFacInformalPopRepository qTsIdstryFacInformalPopRepository;
	
	@Autowired
	QTsIdstryFacFormalPopRepository qTsIdstryFacFormalPopRepository;
	
	@Autowired
	QTsIndstrialOrdSectorsPopRepository qTsIndstrialOrdSectorsPopRepository;
	
	@Autowired
	QTsEmplymntStatusPopRepository qTsEmplymntStatusPopRepository;
	
	@Autowired
	QTsLandAreaPopRepository qTsLandAreaPopRepository;
	
	@Autowired
	QTsGrossRegnProdPopRepository qTsGrossRegnProdPopRepository;
	
	@Autowired
	QTsVehicleRegPopRepository qTsVehicleRegPopRepository;

	@Autowired
	QTsPopFileRepository qTsPopFileRepository;

	@Value("${file.upload.path}")
	String uploadPath;
	
	/**
	  * @Method Name : downLoadPopStatsForm
	  * @Date : 2024. 6. 04.
	  * @Author : KY.LEE
	  * @Method Brief : 엑셀 양식 다운로드
	  * @param response
	  * @param code
	  * @throws IOException
	  */
	public void downLoadPopStatsForm(HttpServletResponse response, String code) throws IOException {
		// filePath에 fileName이 포함되지 않은 경우
		if (CommonUtils.isNull(code)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		ClassPathResource resource = new ClassPathResource("static/excel/population/popStatsForm_"+code+".xlsx");
		
		File file = new File(resource.getURI());
		
		if (!file.exists()) {
			throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
		
	    // 파일 이름 설정
	    String fileName = PopStatsTypeCd.getNameByCode(code) + ".xlsx";
	    String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		
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
	}
	
	
	/**
	  * @Method Name : populationDataSave
	  * @Date : 2024. 6. 04.
	  * @Author : KY.LEE
	  * @Method Brief : 인구통계데이터 등록
	  * @param tsPopMng
	  * @param file
	  * @throws IOException 
	  */
	@Transactional
	public String populationDataSave(TsPopMng tsPopMng, MultipartFile file){
		String[] extsChk = {"xls", "xlsx"};
	    String popmngId = CommonUtils.getUuid();
	    String fileId = CommonUtils.getUuid();
	    String uploadFilePath = null;
	    
	    PopStatsTypeCd popmngType = tsPopMng.getPopmngType();
	    try {
	        String fileOriginName = file.getOriginalFilename();
	        String fileName = generateFileNm(fileOriginName);
	        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
	        String folderPth = makeDirectory(uploadPath + File.separator + LocalDate.now());
	        if (chkFileExt(fileName, extsChk)) {
	        	uploadFilePath = folderPth + File.separator + fileName;
	            File uploadFile = new File(uploadFilePath);
	            if (!file.isEmpty()) {
	                try (FileOutputStream fo = new FileOutputStream(uploadFilePath)) {
	                    byte[] fileBytes = file.getBytes();
	                    fo.write(fileBytes);
	                } catch (IOException e) {
	                    throw new CommonException(ErrorCode.FILE_UPLOAD_FAILED);
	                }
	            }
	            List<Map<String,String>> excelData = readToPopulationDataForExcel(uploadFile,popmngType);
	            
	            if(!excelData.isEmpty()) {
	            	excelDataSave(excelData,popmngType,popmngId,fileId);
	            } else {
	            	throw new CommonException(ErrorCode.EXCEL_DATA_IS_NULL);
	            }

	            TsPopFile tsPopFile = new TsPopFile();
	            tsPopFile.setFileId(fileId);
	            tsPopFile.setFileFilext(ext);
	            tsPopFile.setOrgFileNm(fileOriginName);
	            tsPopFile.setFileNm(fileName);
	            tsPopFile.setFilePath(folderPth);
	            
	            tsPopFile.setFileSize(new BigDecimal(file.getSize()));
	            tsPopMng.setPopmngId(popmngId);
	            tsPopMng.setTsPopFile(tsPopFile);  
	            tsPopMngRepository.save(tsPopMng);
	        } else {
	            throw new CommonException(ErrorCode.FILE_EXTENSION_NOT_AVAILABLE);
	        }
	    } catch (CommonException e) {
	        if (uploadFilePath != null) {
	            deleteUploadFile(uploadFilePath);
	        }
	    	throw new CommonException(e.getErrorCode(),e.getMessage());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	//오류 발생시 업로드 파일 제거
	        if (uploadFilePath != null) {
	            deleteUploadFile(uploadFilePath);
	        }
	        throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED);
	    }
	    return popmngId;
	}
	
	
	/**
	  * @Method Name : readToPopulationDataForExcel
	  * @Date : 2024. 6. 04.
	  * @Author : KY.LEE
	  * @Method Brief : 인구 통계데이터 엑셀 파일 읽기
	  * @param uploadFile
	  * @param popStatsTypeCd
	  */
	private List<Map<String,String>> readToPopulationDataForExcel(File uploadFile, PopStatsTypeCd popStatsTypeCd) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		try (FileInputStream fis = new FileInputStream(uploadFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis)){
			String[] header = popStatsTypeCd.getHeader();
			
			int rowindex = 0;
			int columnindex= 0;
			int sheetNum = workbook.getNumberOfSheets();
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			int rows = sheet.getPhysicalNumberOfRows();

			//엑셀 validation
			//시트수 한개인지 확인
			if(sheetNum >= 2) {
				throw new CommonException(ErrorCode.EXCEL_SHEET_TOO_MANY);
			}
			
			//로우에 데이터 없으면안됨
			if(rows > 0) {
				//0번째는 헤드로 생략 데이터 넣기
				for(rowindex = 1; rowindex < rows; rowindex++){
					XSSFRow row = sheet.getRow(rowindex);
					Map<String,String> resultMap = new HashMap<String,String>();
					if(row != null){
						int cells = row.getPhysicalNumberOfCells();
						for(columnindex = 0; columnindex <= cells; columnindex++){
							XSSFCell cell = row.getCell(columnindex);
							String value="";
							
							if(cell==null){
								continue;
							}else{
								//타입별로 내용 읽기
								switch (cell.getCellType()){
	                            case FORMULA:
	                                value =  cell.getCellFormula();
	                                break;
	                            case NUMERIC:
	                                double numericValue = cell.getNumericCellValue();
	                                if (numericValue == Math.floor(numericValue)) {
	                                    value = String.valueOf((int) numericValue);
	                                } else {
	                                    value = String.valueOf(numericValue);
	                                }

	                                break;
	                            case STRING:
	                                value =  cell.getStringCellValue();
	                                break;
	                            case BLANK:
	                                value = "";
	                                break;
	                            case ERROR:
	                                value = String.valueOf(cell.getErrorCellValue());
	                                break;
	                            case BOOLEAN:
	                                value = String.valueOf(cell.getBooleanCellValue());
	                                break;
	                            case _NONE:
	                                break;
	                            default:
	                                break;
								}
							}
			                // 헤더에 맞게만 데이터 넣고 나머지는 버림
			                if(columnindex < header.length) {
			                    resultMap.put(header[columnindex], value.trim());
			                } 
						}
					}
					result.add(resultMap);
				}
			} else {
				throw new CommonException(ErrorCode.EXCEL_DATA_IS_NULL);
			}
		} catch (FileNotFoundException e) {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		} catch (IOException e) {
			throw new CommonException(ErrorCode.POPULATION_DATA_INSERT_FAILED);
		} catch (IllegalStateException e) {
			throw new CommonException(ErrorCode.UNKNOWN_ERROR);
		}
		
		return result;
	}
	
	/**
     * @Method Name : excelDataSave
     * @작성일 : 2042. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 데이터 insert
     */
	@Transactional
    public void excelDataSave(List<Map<String, String>> excelData, PopStatsTypeCd popmngType, String popmngId, String fileId) {
		List<TcDsdMng> dsdList = tcDsdMngRepository.findAllByUseYnOrderByDsdId("Y");
		List<TcGnMng> gnList = tcGnMngRepository.findAllByUseYnOrderByGnId("Y");
		
		ObjectMapper mapper = new ObjectMapper();
		List<PopulationStatsDTO> populationStatsDTOList = null;
		try {
			populationStatsDTOList = mapper.convertValue(excelData, new TypeReference<List<PopulationStatsDTO>>() {});
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			throw new CommonException(ErrorCode.EXCEL_DATA_PARSE_FAILED);
		}
		
		if(populationStatsDTOList == null  || populationStatsDTOList.isEmpty()) {
			throw new CommonException(ErrorCode.EXCEL_DATA_IS_NULL);
		}
		
		switch(popmngType) {
		case POPULATION_BY_REGION:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsRegnPop tsRegnPop = dsdList.stream()
                .filter(tcDsdMng ->
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                    TsRegnPop saveTsRegnPop = new TsRegnPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsRegnPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision())));
            	tsRegnPopRepository.save(tsRegnPop);
			}
			break;
		case POPULATION_BY_AGE:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsAgePop tsAgePop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsAgePop saveTsAgePop = new TsAgePop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsAgePop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision())));
				tsAgePopRepository.save(tsAgePop);
			}
			break;
		case POPULATION_BY_ECONOMIC_ACTIBITY:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
			
				TsEcnmcActPop tsEcnmcActPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsEcnmcActPop saveTsEcnmcActPop = new TsEcnmcActPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsEcnmcActPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision())));
				tsEcnmcActPopRepository.save(tsEcnmcActPop);
			}
			break;
		case THE_NUMBER_OF_STUDENTS:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
			
				TsStdntPop tsStdntPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsStdntPop saveTsStdntPop  = new TsStdntPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsStdntPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision())));

				tsStdntPopRepository.save(tsStdntPop);
			}
			break;
		case POPULATION_BY_HOUSEHOLD:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
			
				TsHouseHoldPop tsHouseHoldPop = dsdList.stream()
                .filter(tcDsdMng -> 
                	CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsHouseHoldPop saveTsHouseHoldPop  = new TsHouseHoldPop(populationStatsDTO, tcDsdMng.getProvinCd() ,tcDsdMng.getDistrictCd());
                    return saveTsHouseHoldPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
//                }).orElseGet(() -> {
//                	System.out.println("DistrictNm :" + populationStatsDTO.getDistrict());
//                	System.out.println("DsdNm : " + populationStatsDTO.getDsDivision());
//                	System.out.println("GnDivision : " + populationStatsDTO.getGnDivision());
//                	return null; // 적절한 기본 값을 반환
//                });
				
//				if(tsHouseHoldPop != null)
				tsHouseHoldPopRepository.save(tsHouseHoldPop);
			}
			break;
		case HOUSEHOLD_POPULATION_BY_HOUSING_TYPE:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsHousingTypePop tsHousingTypePop = dsdList.stream()
						.filter(tcDsdMng ->
							CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
						.findAny()
						.map(tcDsdMng -> {
							TsHousingTypePop saveTsHousingTypePop  = new TsHousingTypePop(populationStatsDTO,tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
							return saveTsHousingTypePop;
						}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
				tsHousingTypePopRepository.save(tsHousingTypePop);
			}
			break;
		case NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsOccHousingUnitPop tsOccHousingUnitPop = gnList.stream()
						.filter(tcGnMng ->
						CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcGnMng.getDistrictNm())) &&
						CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcGnMng.getDsdNm())) &&
						("".equals(populationStatsDTO.getGnDivision()) || CommonUtils.normalizeString(populationStatsDTO.getGnDivision()).equals(CommonUtils.normalizeString(tcGnMng.getGnNm()))))
						.findAny()
						.map(tcGnMng -> {
							TsOccHousingUnitPop saveTsOccHousingUnitPop  = new TsOccHousingUnitPop(populationStatsDTO,tcGnMng.getGnId());
							return saveTsOccHousingUnitPop;
						}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD_GN",populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision(),populationStatsDTO.getGnDivision())));
//						.orElseGet(() -> {
//				              System.out.println("DistrictNm :" + populationStatsDTO.getDistrict());
//				              System.out.println("DsdNm : " + populationStatsDTO.getDsDivision());
//				              System.out.println("GnDivision : " + populationStatsDTO.getGnDivision());
//				              return null; // 적절한 기본 값을 반환
//				          });
				tsOccHousingUnitPopRepository.save(tsOccHousingUnitPop);
			}
			break;
		case SCHOOL_STATUS_BY_REGION_NATIONAL:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsNationalSchoolPop tsNationalSchoolPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getEducationZone()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsNationalSchoolPop saveTsNationalSchoolPop = new TsNationalSchoolPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsNationalSchoolPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getEducationZone())));
				tsNationalSchoolPopRepository.save(tsNationalSchoolPop);
			}
			break;
		case SCHOOL_STATUS_BY_REGION_PRIVATE:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsPrivateSchoolPop tsPrivateSchoolPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getProvince()).equals(CommonUtils.normalizeString(tcDsdMng.getProvinNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsPrivateSchoolPop saveTsPrivateSchoolPop = new TsPrivateSchoolPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsPrivateSchoolPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_PROVINCE_DISTRICT",populationStatsDTO.getProvince(),populationStatsDTO.getDistrict())));
				tsPrivateSchoolPopRepository.save(tsPrivateSchoolPop);
			}
			break;
		case SCHOOL_STATUS_BY_REGION_SPECIAL:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsSpecialSchoolPop tsSpecialSchoolPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getProvince()).equals(CommonUtils.normalizeString(tcDsdMng.getProvinNm())) &&
	                CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsSpecialSchoolPop saveTsSpecialSchoolPop = new TsSpecialSchoolPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsSpecialSchoolPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_PROVINCE_DISTRICT",populationStatsDTO.getProvince(),populationStatsDTO.getDistrict())));
				tsSpecialSchoolPopRepository.save(tsSpecialSchoolPop);
			}
			break;
		case SCHOOL_STATUS_BY_REGION_PIRIVENA:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsPirivenaSchoolPop tsPirivenaSchoolPop = dsdList.stream()
                .filter(tcDsdMng -> CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsPirivenaSchoolPop saveTsPirivenaSchoolPop = new TsPirivenaSchoolPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsPirivenaSchoolPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
				tsPirivenaSchoolPopRepository.save(tsPirivenaSchoolPop);
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_EDUCATION:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsFacultyEducationPop tsFacultyEducationPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getEducationZone()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsFacultyEducationPop saveTsFacultyEducationPop = new TsFacultyEducationPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsFacultyEducationPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getEducationZone())));
				tsFacultyEducationPopRepository.save(tsFacultyEducationPop);
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_STUDENTS:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsFacultyStudentsPop tsFacultyStudentsPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getEducationZone()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsFacultyStudentsPop saveTsFacultyStudentsPop = new TsFacultyStudentsPop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsFacultyStudentsPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getEducationZone())));
				
				tsFacultyStudentsPopRepository.save(tsFacultyStudentsPop);
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_CYCLE:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsFacultyCyclePop tsFacultyCyclePop = dsdList.stream()
                .filter(tcDsdMng ->
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getEducationZone()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsFacultyCyclePop saveTsFacultyCyclePop = new TsFacultyCyclePop(populationStatsDTO, tcDsdMng.getDsdId());
                    return saveTsFacultyCyclePop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getEducationZone())));
				tsFacultyCyclePopRepository.save(tsFacultyCyclePop);
			}
			break;
		case THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsSchoolSystemPop tsSchoolSystemPop = dsdList.stream()
						.filter(tcDsdMng -> 
							CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
							CommonUtils.normalizeString(populationStatsDTO.getEducationZone()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
						.findAny()
						.map(tcDsdMng -> {
							TsSchoolSystemPop saveTsSchoolSystemPop = new TsSchoolSystemPop(populationStatsDTO, tcDsdMng.getDsdId());
							return saveTsSchoolSystemPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT_DSD",populationStatsDTO.getDistrict(),populationStatsDTO.getEducationZone())));
				tsSchoolSystemPopRepository.save(tsSchoolSystemPop);
			}
			break;
		case STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsIdstryFacInformalPop tsIdstryFacInformalPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsIdstryFacInformalPop saveTsIdstryFacInformalPop = new TsIdstryFacInformalPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsIdstryFacInformalPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
				tsIdstryFacInformalPopRepository.save(tsIdstryFacInformalPop);
			}
			break;
		case STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsIdstryFacFormalPop tsIdstryFacFormalPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsIdstryFacFormalPop saveTsIdstryFacFormalPop = new TsIdstryFacFormalPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsIdstryFacFormalPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
				tsIdstryFacFormalPopRepository.save(tsIdstryFacFormalPop);
			}
			break;
		case INDUSTRIAL_PRODUCTIVITY_BY_SECTORS:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsIndstrialOrdSectorsPop tsIndstrialOrdSectorsPop = dsdList.stream()
                .filter(tcDsdMng ->
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsIndstrialOrdSectorsPop saveTsIndstrialOrdSectorsPop = new TsIndstrialOrdSectorsPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsIndstrialOrdSectorsPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));

				tsIndstrialOrdSectorsPopRepository.save(tsIndstrialOrdSectorsPop);
			}
			break;
		case STATUS_OF_EMPLOYMENT:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsEmplymntStatusPop tsEmplymntStatusPop = dsdList.stream()
                .filter(tcDsdMng -> 
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsEmplymntStatusPop saveTsEmplymntStatusPop = new TsEmplymntStatusPop(populationStatsDTO, tcDsdMng.getProvinCd(),tcDsdMng.getDistrictCd());
                    return saveTsEmplymntStatusPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_DISTRICT",populationStatsDTO.getDistrict())));
				tsEmplymntStatusPopRepository.save(tsEmplymntStatusPop);
			}
			break;
		case LAND_AREA:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsLandAreaPop tsLandAreaPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getProvince()).equals(CommonUtils.normalizeString(tcDsdMng.getProvinNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getDistrict()).equals(CommonUtils.normalizeString(tcDsdMng.getDistrictNm())) &&
					CommonUtils.normalizeString(populationStatsDTO.getDsDivision()).equals(CommonUtils.normalizeString(tcDsdMng.getDsdNm())))
                .findAny()
                .map(tcDsdMng -> {
                	populationStatsDTO.setDsdId(tcDsdMng.getDsdId());
                	TsLandAreaPop saveTsLandAreaPop = new TsLandAreaPop(populationStatsDTO);
                    return saveTsLandAreaPop;
				}).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_PROVINCE_DISTRICT_DSD",populationStatsDTO.getProvince(),populationStatsDTO.getDistrict(),populationStatsDTO.getDsDivision())));
				tsLandAreaPopRepository.save(tsLandAreaPop);
			}
			break;
		case GROSS_REGIONAL_PRODUCT:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsGrossRegnProdPop tsGrossRegnProdPop = dsdList.stream()
                .filter(tcDsdMng -> 
	                CommonUtils.normalizeString(populationStatsDTO.getProvince()).equals(CommonUtils.normalizeString(tcDsdMng.getProvinNm())))
                .findAny()
                .map(tcDsdMng -> {
                	TsGrossRegnProdPop saveTsGrossRegnProdPop = new TsGrossRegnProdPop(populationStatsDTO, tcDsdMng.getProvinCd());
                    return saveTsGrossRegnProdPop;
                }).orElseThrow(() -> new CommonException(ErrorCode.EXCEL_LOCAL_INFO_INVALID, CommonUtils.getMessage("enums.ErrorCode.NOT_FOUND_PROVINCE",populationStatsDTO.getProvince())));
				tsGrossRegnProdPopRepository.save(tsGrossRegnProdPop);
			}
			break;
		case VEHICLE_REGISTRATION:
			for(PopulationStatsDTO populationStatsDTO : populationStatsDTOList) {
				populationStatsDTO.setPopmngId(popmngId);
				populationStatsDTO.setFileId(fileId);
				
				TsVehicleRegPop tsVehicleRegPop = new TsVehicleRegPop(populationStatsDTO);
				tsVehicleRegPopRepository.save(tsVehicleRegPop);
			}
			break;
		default:
			throw new CommonException(ErrorCode.CANNOT_BE_CAST_ENTITY);
		}
	}

	/**
     * @Method Name : generateFileNm
     * @작성일 : 2042. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 저장 파일명 생성
     * @return
     */
	private String generateFileNm(String fileNm) {		
		String uuid = CommonUtils.getUuid();
		String ext = StringUtils.getFilenameExtension(fileNm);
		return uuid + "." + ext;
	}
	
	/**
     * @Method Name : chkFileExt
     * @작성일 : 2024. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 파일 확장자 체크
     * @return
     */
	public boolean chkFileExt(String fileNm, String[] arrExt) {
		boolean isContatin = false;
		if(fileNm != null) {
			String ext = fileNm.substring(fileNm.lastIndexOf(".")+1, fileNm.length());
			
			isContatin = Arrays.asList(arrExt).contains(ext);
		}
		return isContatin;
	}
	
	/**
     * @Method Name : makeDirectory
     * @작성일 : 2024. 6. 10.
     * @작성자 : KY.LEE
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
     * @Method Name : deleteUploadFile
     * @작성일 : 2024. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 파일 삭제
     */
	public void deleteUploadFile(String filePath){
		File uploadFile = new File(filePath);
		
		if(uploadFile.exists()) {
			uploadFile.delete();
		}
	}

	/**
     * @Method Name : fileDelete
     * @작성일 : 2024. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 파일 삭제
     */
	public void fileDelete(TsPopFile tsPopFile) {
		if(tsPopFile != null) {
			String uploadPath = tsPopFile.getFilePath() + File.separator + tsPopFile.getFileNm();
			File file = new File(uploadPath);
			if(file != null) {
				file.delete();
			}
		} else {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		} 
	}


	/**
     * @Method Name : populationDataDelete
     * @작성일 : 2024. 6. 10.
     * @작성자 : KY.LEE
     * @Method 설명 : 파일 삭제
     */
	@Transactional
	public void populationDataDelete(TsPopMng tsPopMng) {
		if(tsPopMng == null) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		String popmngId = tsPopMng.getPopmngId();
		PopStatsTypeCd popmngType = tsPopMng.getPopmngType();
		switch(popmngType) {
		case POPULATION_BY_REGION:
			List<TsRegnPop> regnPopList = tsRegnPopRepository.findAllByPopmngId(popmngId);
			if(!regnPopList.isEmpty()) {
				for(TsRegnPop tsRegnPop : regnPopList) {
					tsRegnPopRepository.delete(tsRegnPop);
				}
			}
			break;
		case POPULATION_BY_AGE:
			List<TsAgePop> tsAgePopList = tsAgePopRepository.findAllByPopmngId(popmngId);
			if(!tsAgePopList.isEmpty()) {
				for(TsAgePop tsAgePop : tsAgePopList) {
					tsAgePopRepository.delete(tsAgePop);
				}
			}
			break;
		case POPULATION_BY_ECONOMIC_ACTIBITY:
			List<TsEcnmcActPop> tsEcnmcActPopList = tsEcnmcActPopRepository.findAllByPopmngId(popmngId);
			if(!tsEcnmcActPopList.isEmpty()) {
				for(TsEcnmcActPop tsEcnmcActPop : tsEcnmcActPopList) {
					tsEcnmcActPopRepository.delete(tsEcnmcActPop);
				}
			}
			break;
		case THE_NUMBER_OF_STUDENTS:
			List<TsStdntPop> tsStdntPopList = tsStdntPopRepository.findAllByPopmngId(popmngId);
			if(!tsStdntPopList.isEmpty()) {
				for(TsStdntPop tsStdntPop : tsStdntPopList) {
					tsStdntPopRepository.delete(tsStdntPop);
				}
			}
			break;
		case POPULATION_BY_HOUSEHOLD:
			List<TsHouseHoldPop> tsHouseHoldPopList = tsHouseHoldPopRepository.findAllByPopmngId(popmngId);
			if(!tsHouseHoldPopList.isEmpty()) {
				for(TsHouseHoldPop tsHouseHoldPop : tsHouseHoldPopList) {
					tsHouseHoldPopRepository.delete(tsHouseHoldPop);
				}
			}
			break;
		case HOUSEHOLD_POPULATION_BY_HOUSING_TYPE:
			List<TsHousingTypePop> tsHousingTypePopList = tsHousingTypePopRepository.findAllByPopmngId(popmngId);
			if(!tsHousingTypePopList.isEmpty()) {
				for(TsHousingTypePop tsHousingTypePop : tsHousingTypePopList) {
					tsHousingTypePopRepository.delete(tsHousingTypePop);
				}
			}
			break;
		case NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT:
			List<TsOccHousingUnitPop> tsOccHousingUnitPopList = tsOccHousingUnitPopRepository.findAllByPopmngId(popmngId);
			if(!tsOccHousingUnitPopList.isEmpty()) {
				for(TsOccHousingUnitPop tsOccHousingUnitPop : tsOccHousingUnitPopList) {
					tsOccHousingUnitPopRepository.delete(tsOccHousingUnitPop);
				}
			}
			break;
		case SCHOOL_STATUS_BY_REGION_NATIONAL:
			List<TsNationalSchoolPop> tsNationalSchoolPopList = tsNationalSchoolPopRepository.findAllByPopmngId(popmngId);
			if(!tsNationalSchoolPopList.isEmpty()) {
				for(TsNationalSchoolPop tsNationalSchoolPop : tsNationalSchoolPopList) {
					tsNationalSchoolPopRepository.delete(tsNationalSchoolPop);
				}
			}
			break;
		case SCHOOL_STATUS_BY_REGION_PRIVATE:
			List<TsPrivateSchoolPop> tsPrivateSchoolPopList = tsPrivateSchoolPopRepository.findAllByPopmngId(popmngId);
			if(!tsPrivateSchoolPopList.isEmpty()) {
				for(TsPrivateSchoolPop tsPrivateSchoolPop : tsPrivateSchoolPopList) {
					tsPrivateSchoolPopRepository.delete(tsPrivateSchoolPop);
				}
			}
			break;
		case SCHOOL_STATUS_BY_REGION_SPECIAL:
			List<TsSpecialSchoolPop> tsSpecialSchoolPopList = tsSpecialSchoolPopRepository.findAllByPopmngId(popmngId);
			if(!tsSpecialSchoolPopList.isEmpty()) {
				for(TsSpecialSchoolPop tsSpecialSchoolPop : tsSpecialSchoolPopList) {
					tsSpecialSchoolPopRepository.delete(tsSpecialSchoolPop);
				}
			}
			break;
		case SCHOOL_STATUS_BY_REGION_PIRIVENA:
			List<TsPirivenaSchoolPop> tsPirivenaSchoolPopList = tsPirivenaSchoolPopRepository.findAllByPopmngId(popmngId);
			if(!tsPirivenaSchoolPopList.isEmpty()) {
				for(TsPirivenaSchoolPop tsPirivenaSchoolPop  : tsPirivenaSchoolPopList) {
					tsPirivenaSchoolPopRepository.delete(tsPirivenaSchoolPop);
				}
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_EDUCATION:
			List<TsFacultyEducationPop> tsFacultyEducationPopList = tsFacultyEducationPopRepository.findAllByPopmngId(popmngId);
			if(!tsFacultyEducationPopList.isEmpty()) {
				for(TsFacultyEducationPop tsFacultyEducationPop  : tsFacultyEducationPopList) {
					tsFacultyEducationPopRepository.delete(tsFacultyEducationPop);
				}
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_STUDENTS:
			List<TsFacultyStudentsPop> tsFacultyStudentsPopList = tsFacultyStudentsPopRepository.findAllByPopmngId(popmngId);
			if(!tsFacultyStudentsPopList.isEmpty()) {
				for(TsFacultyStudentsPop tsFacultyStudentsPop  : tsFacultyStudentsPopList) {
					tsFacultyStudentsPopRepository.delete(tsFacultyStudentsPop);
				}
			}
			break;
		case NUMBER_OF_FACULTY_BY_REGION_CYCLE:
			List<TsFacultyCyclePop> tsFacultyCyclePopList = tsFacultyCyclePopRepository.findAllByPopmngId(popmngId);
			if(!tsFacultyCyclePopList.isEmpty()) {
				for(TsFacultyCyclePop tsFacultyCyclePop  : tsFacultyCyclePopList) {
					tsFacultyCyclePopRepository.delete(tsFacultyCyclePop);
				}
			}
			break;
		case THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM:
			List<TsSchoolSystemPop> tsSchoolSystemPopList = tsSchoolSystemPopRepository.findAllByPopmngId(popmngId);
			if(!tsSchoolSystemPopList.isEmpty()) {
				for(TsSchoolSystemPop tsSchoolSystemPop : tsSchoolSystemPopList) {
					tsSchoolSystemPopRepository.delete(tsSchoolSystemPop);
				}
			}
			break;
		case STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL:
			List<TsIdstryFacInformalPop> tsIdstryFacInformalPopList = tsIdstryFacInformalPopRepository.findAllByPopmngId(popmngId);
			if(!tsIdstryFacInformalPopList.isEmpty()) {
				for(TsIdstryFacInformalPop tsIdstryFacInformalPop : tsIdstryFacInformalPopList) {
					tsIdstryFacInformalPopRepository.delete(tsIdstryFacInformalPop);
				}
			}
			break;
		case STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL:
			List<TsIdstryFacFormalPop> tsIdstryFacFormalPopList = tsIdstryFacFormalPopRepository.findAllByPopmngId(popmngId);
			if(!tsIdstryFacFormalPopList.isEmpty()) {
				for(TsIdstryFacFormalPop tsIdstryFacFormalPop : tsIdstryFacFormalPopList) {
					tsIdstryFacFormalPopRepository.delete(tsIdstryFacFormalPop);
				}
			}
			break;
		case INDUSTRIAL_PRODUCTIVITY_BY_SECTORS:
			List<TsIndstrialOrdSectorsPop> tsIndstrialOrdSectorsPopList = tsIndstrialOrdSectorsPopRepository.findAllByPopmngId(popmngId);
			if(!tsIndstrialOrdSectorsPopList.isEmpty()) {
				for(TsIndstrialOrdSectorsPop tsIndstrialOrdSectorsPop : tsIndstrialOrdSectorsPopList) {
					tsIndstrialOrdSectorsPopRepository.delete(tsIndstrialOrdSectorsPop);
				}
			}
			break;
		case STATUS_OF_EMPLOYMENT:
			List<TsEmplymntStatusPop> tsEmplymntStatusPopList = tsEmplymntStatusPopRepository.findAllByPopmngId(popmngId);
			if(!tsEmplymntStatusPopList.isEmpty()) {
				for(TsEmplymntStatusPop tsEmplymntStatusPop : tsEmplymntStatusPopList) {
					tsEmplymntStatusPopRepository.delete(tsEmplymntStatusPop);
				}
			}
			break;
		case LAND_AREA:
			List<TsLandAreaPop> tsLandAreaPopList = tsLandAreaPopRepository.findAllByPopmngId(popmngId);
			if(!tsLandAreaPopList.isEmpty()) {
				for(TsLandAreaPop tsLandAreaPop : tsLandAreaPopList) {
					tsLandAreaPopRepository.delete(tsLandAreaPop);
				}
			}
			break;
		case GROSS_REGIONAL_PRODUCT:
			List<TsGrossRegnProdPop> tsGrossRegnProdPopList = tsGrossRegnProdPopRepository.findAllByPopmngId(popmngId);
			if(!tsGrossRegnProdPopList.isEmpty()) {
				for(TsGrossRegnProdPop tsGrossRegnProdPop : tsGrossRegnProdPopList) {
					tsGrossRegnProdPopRepository.delete(tsGrossRegnProdPop);
				}
			}
			break;
		case VEHICLE_REGISTRATION:
			List<TsVehicleRegPop> tsVehicleRegPopList = tsVehicleRegPopRepository.findAllByPopmngId(popmngId);
			if(!tsVehicleRegPopList.isEmpty()) {
				for(TsVehicleRegPop tsVehicleRegPop : tsVehicleRegPopList) {
					tsVehicleRegPopRepository.delete(tsVehicleRegPop);
				}
			}
			break;
		default:
			throw new CommonException(ErrorCode.CANNOT_BE_CAST_ENTITY);
		}
	}

	/**
     * @Method Name : getRegionInfo
     * @작성일 : 2024. 7. 01.
     * @작성자 : KY.LEE
     * @Method 설명 : 지역정보조회
     */
	public TsPopulationStatisticsRegionDTO getRegionInfo(TsPopulationStatisticsSearchDTO searchDTO) {
		TsPopulationStatisticsRegionDTO result = null;
		
		switch(searchDTO.getPopStatTypeCd()) {
	    	case POPULATION_BY_REGION:
	    		result = qTsRegnPopRepository.getDistrictJson();
	    		break;
	    	case POPULATION_BY_AGE:
	    		result = qTsAgePopRepository.getDistrictJson();
	    		break;
	    	case POPULATION_BY_ECONOMIC_ACTIBITY:
	    		result = qTsEcnmcActPopRepository.getDistrictJson();
	    		break;
	    	case THE_NUMBER_OF_STUDENTS:
	    		result = qTsStdntPopRepository.getDistrictJson();
	    		break;
	    	case POPULATION_BY_HOUSEHOLD:
	    		result = qTsHouseHoldPopRepository.getProvinceJson();
	    		break;
	    	case HOUSEHOLD_POPULATION_BY_HOUSING_TYPE:
	    		result = qTsHousingTypePopRepository.getProvinceJsonAndDistrictJson();
	    		break;
	    	case NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT:
	    		result = qTsOccHousingUnitPopRepository.getProvinceJsonAndDistrictJsonAndDsdJson();
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_NATIONAL:
	    		result = qTsNationalSchoolPopRepository.getDistrictJson();
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_PRIVATE:
	    		result = qTsPrivateSchoolPopRepository.getProvinceJson();
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_SPECIAL:
	    		result = qTsSpecialSchoolPopRepository.getProvinceJson();
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_PIRIVENA:
	    		result = qTsPirivenaSchoolPopRepository.getProvinceJson();
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_EDUCATION:
	    		result = qTsFacultyEducationPopRepository.getDistrictAndDsdJson();
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_STUDENTS:
	    		result = qTsFacultyStudentsPopRepository.getDistrictJson();
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_CYCLE:
	    		result = qTsFacultyCyclePopRepository.getDistrictJson();
	    		break;
	    	case THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM:
	    		result = qTsSchoolSystemPopRepository.getDistrictJson();
	    		break;
	    	case STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL:
	    		result = qTsIdstryFacInformalPopRepository.getProvinceJson();
	    		break;
	    	case STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL:
	    		result = qTsIdstryFacFormalPopRepository.getProvinceJson();
	    		break;
	    	case INDUSTRIAL_PRODUCTIVITY_BY_SECTORS:
	    		result = qTsIndstrialOrdSectorsPopRepository.getProvinceJson();
	    		break;
	    	case STATUS_OF_EMPLOYMENT:
	    		result = qTsEmplymntStatusPopRepository.getProvinceJson();
	    		break;
	    	case LAND_AREA:
	    		result = qTsLandAreaPopRepository.getProvinceJsonAndDistrictJson();
	    		break;
			case GROSS_REGIONAL_PRODUCT:
				result = qTsGrossRegnProdPopRepository.getProvinceJson();
				break;
			case VEHICLE_REGISTRATION:
				return null;
			default:
				return null;
		}
    	return result;
	}

	/**
     * @Method Name : getStatisticsData
     * @작성일 : 2024. 7. 01.
     * @작성자 : KY.LEE
     * @Method 설명 : 차트데이터 조회
     */
	public TsPopulationRsltStatisticsDTO getStatisticsData(TsPopulationStatisticsSearchDTO searchDTO) {
		TsPopulationRsltStatisticsDTO result = null;
		
		switch(searchDTO.getPopStatTypeCd()) {
	    	case POPULATION_BY_REGION:
	    		result = qTsRegnPopRepository.getChartData(searchDTO);
	    		break;
	    	case POPULATION_BY_AGE:
	    		result = qTsAgePopRepository.getChartData(searchDTO);
	    		break;
	    	case POPULATION_BY_ECONOMIC_ACTIBITY:
	    		result = qTsEcnmcActPopRepository.getChartData(searchDTO);
	    		break;
	    	case THE_NUMBER_OF_STUDENTS:
	    		result = qTsStdntPopRepository.getChartData(searchDTO);
	    		break;
	    	case POPULATION_BY_HOUSEHOLD:
	    		result = qTsHouseHoldPopRepository.getChartData(searchDTO);
	    		break;
	    	case HOUSEHOLD_POPULATION_BY_HOUSING_TYPE:
	    		result = qTsHousingTypePopRepository.getChartData(searchDTO);
	    		break;
	    	case NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT:
	    		result = qTsOccHousingUnitPopRepository.getChartData(searchDTO);
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_NATIONAL:
	    		result = qTsNationalSchoolPopRepository.getChartData(searchDTO);
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_PRIVATE:
	    		result = qTsPrivateSchoolPopRepository.getChartData(searchDTO);
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_SPECIAL:
	    		result = qTsSpecialSchoolPopRepository.getChartData(searchDTO);
	    		break;
	    	case SCHOOL_STATUS_BY_REGION_PIRIVENA:
	    		result = qTsPirivenaSchoolPopRepository.getChartData(searchDTO);
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_EDUCATION:
	    		result = qTsFacultyEducationPopRepository.getChartData(searchDTO);
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_STUDENTS:
	    		result = qTsFacultyStudentsPopRepository.getChartData(searchDTO);		
	    		break;
	    	case NUMBER_OF_FACULTY_BY_REGION_CYCLE:
	    		result = qTsFacultyCyclePopRepository.getChartData(searchDTO);
	    		break;
	    	case THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM:
	    		result = qTsSchoolSystemPopRepository.getChartData(searchDTO);
	    		break;
	    	case STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL:
	    		result = qTsIdstryFacInformalPopRepository.getChartData(searchDTO); 
	    		break;
	    	case STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL:
	    		result = qTsIdstryFacFormalPopRepository.getChartData(searchDTO);
	    		break;
	    	case INDUSTRIAL_PRODUCTIVITY_BY_SECTORS:
	    		result = qTsIndstrialOrdSectorsPopRepository.getChartData(searchDTO);
	    		break;
	    	case STATUS_OF_EMPLOYMENT:
	    		result= qTsEmplymntStatusPopRepository.getChartData(searchDTO);
	    		break;
	    	case LAND_AREA:
	    		result = qTsLandAreaPopRepository.getChartData(searchDTO);
	    		break;
			case GROSS_REGIONAL_PRODUCT:
				result = qTsGrossRegnProdPopRepository.getChartData(searchDTO);
				break;
			case VEHICLE_REGISTRATION:
				result = qTsVehicleRegPopRepository.getChartData(searchDTO);
				break;
			default:
				return null;
		}
		return result;
	}

	public void excelDownLoad(HttpServletResponse response, TsPopulationStatisticsSearchDTO searchDTO) {
		List<TsPopulationFileDTO> fileList = qTsPopFileRepository.getPopulationFileList(searchDTO);

		if (fileList != null && !fileList.isEmpty()) {
			String zipFileName = CommonUtils.getMessage("statistics.excelDownLoad.population")+".zip";
			String encodedZipFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedZipFileName + "\"");

			try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
				for (TsPopulationFileDTO fileDTO : fileList) {
					String filePath = fileDTO.getFilePath() + File.separator + fileDTO.getFileNm();
					File file = new File(filePath);

					if (file.exists()) {
						try (FileInputStream fis = new FileInputStream(file)) {
							ZipEntry zipEntry = new ZipEntry(fileDTO.getOrgFileNm());
							zipOut.putNextEntry(zipEntry);

							FileCopyUtils.copy(fis, zipOut);

							zipOut.closeEntry();
						}
					}
				}
				zipOut.finish();
			} catch (IOException e) {
				throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
			}
		}
	}
}
