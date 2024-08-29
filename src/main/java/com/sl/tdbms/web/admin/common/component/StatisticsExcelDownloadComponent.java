package com.sl.tdbms.web.admin.common.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyExcelDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyExcelResultDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsExcelDTO;
import com.sl.tdbms.web.admin.common.querydsl.QTlSrvyAnsRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

@Component
public class StatisticsExcelDownloadComponent {
	
	@Autowired
	QTlSrvyAnsRepository qTlSrvyAnsRepository;

    public void odStatsDataExcelDownload(HttpServletResponse response,List<TlSrvyExcelDTO> result) throws IOException{
    	ObjectMapper objectMapper = new ObjectMapper();
        List<CommonCdDTO> headerList = null;
        List<TlSrvyExcelResultDTO> bodyList = new ArrayList<>();

        if (!result.isEmpty()) {
            TlSrvyExcelDTO headerSearchDTO = result.stream()
                .max(Comparator.comparingInt(dto -> dto.getSectSqnoArr().size()))
                .orElse(null);

            if (headerSearchDTO != null) {
                headerList = qTlSrvyAnsRepository.getSurveyHeaderInfo(headerSearchDTO);

                if (headerList != null && !headerList.isEmpty()) {
                	for(TlSrvyExcelDTO searchOption : result) {
                		List<TlSrvyExcelResultDTO> bodyData = qTlSrvyAnsRepository.getSurveyResults(searchOption);
                		bodyList.addAll(bodyData);
                	}
                }
                
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Survey Results");

                // 헤더 생성
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue(CommonUtils.getMessage("common.table.number"));
                headerRow.createCell(1).setCellValue(CommonUtils.getMessage("common.table.investType"));
                headerRow.createCell(2).setCellValue(CommonUtils.getMessage("statistics.odStatsDataExcelDownload.investigatorName"));
                headerRow.createCell(3).setCellValue(CommonUtils.getMessage("statistics.odStatsDataExcelDownload.investigatorContact"));

                for (int i = 0; i < headerList.size(); i++) {
                    headerRow.createCell(i + 4).setCellValue(headerList.get(i).getCdNm());
                }

                int rowNum = 1;
                for (TlSrvyExcelResultDTO dto : bodyList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rowNum - 1); // No
                    row.createCell(1).setCellValue(dto.getExmnType().getName()); // 조사유형
                    row.createCell(2).setCellValue(dto.getPollsterNm()); // 조사원이름
                    row.createCell(3).setCellValue(dto.getPollsterTel()); // 조사원연락처

                    List<Map<String, Object>> ansCntsArray = objectMapper.readValue(dto.getAnsCntsArray(), new TypeReference<List<Map<String, Object>>>(){});

                    int ansIndex = 0;

                    for (int i = 0; i < headerList.size(); i++) {
                        String code = headerList.get(i).getCd();
                        String value = "-";
                        
                        if (ansIndex < ansCntsArray.size() && code.equals(ansCntsArray.get(ansIndex).get("code"))) {
                            value = ansCntsArray.get(ansIndex).get("name") != null ? ansCntsArray.get(ansIndex).get("name").toString() : "-";
                            ansIndex++;
                        }

                        row.createCell(i + 4).setCellValue(value);
                    }
                }

                for (int i = 0; i < headerList.size() + 4; i++) {
                    sheet.autoSizeColumn(i);
                }
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=survey_results.xlsx");
                try {
                    workbook.write(response.getOutputStream());
                    response.getOutputStream().flush();
                } finally {
                    workbook.close();
                }
            }
        }
    }

    public void roadsideStatsDataExcelDownload(HttpServletResponse response,List<TlSrvyExcelDTO> result) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        List<CommonCdDTO> headerList = null;
        List<TlSrvyExcelResultDTO> bodyList = new ArrayList<>();

        if (!result.isEmpty()) {
            TlSrvyExcelDTO headerSearchDTO = result.stream()
                    .max(Comparator.comparingInt(dto -> dto.getSectSqnoArr().size()))
                    .orElse(null);

            if (headerSearchDTO != null) {
                headerList = qTlSrvyAnsRepository.getSurveyHeaderInfo(headerSearchDTO);

                if (headerList != null && !headerList.isEmpty()) {
                    for(TlSrvyExcelDTO searchOption : result) {
                        List<TlSrvyExcelResultDTO> bodyData = qTlSrvyAnsRepository.getSurveyResults(searchOption);
                        bodyList.addAll(bodyData);
                    }
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Survey Results");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue(CommonUtils.getMessage("common.table.number"));
                headerRow.createCell(1).setCellValue(CommonUtils.getMessage("common.table.investType"));
                headerRow.createCell(2).setCellValue(CommonUtils.getMessage("statistics.roadsideStatsDataExcelDownload.cordonLine"));
                headerRow.createCell(3).setCellValue(CommonUtils.getMessage("statistics.roadsideStatsDataExcelDownload.toolbooth"));
                headerRow.createCell(4).setCellValue(CommonUtils.getMessage("statistics.odStatsDataExcelDownload.investigatorName"));
                headerRow.createCell(5).setCellValue(CommonUtils.getMessage("statistics.odStatsDataExcelDownload.investigatorContact"));

                for (int i = 0; i < headerList.size(); i++) {
                    headerRow.createCell(i + 6).setCellValue(headerList.get(i).getCdNm());
                }

                int rowNum = 1;
                for (TlSrvyExcelResultDTO dto : bodyList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rowNum - 1); // No
                    row.createCell(1).setCellValue(dto.getExmnType().getName()); // 조사유형
                    row.createCell(2).setCellValue(dto.getCordonLine()); // 코든라인
                    row.createCell(3).setCellValue(dto.getTollBooth()); // 툴부스
                    row.createCell(4).setCellValue(dto.getPollsterNm()); // 조사원이름
                    row.createCell(5).setCellValue(dto.getPollsterTel()); // 조사원연락처

                    List<Map<String, Object>> ansCntsArray = objectMapper.readValue(dto.getAnsCntsArray(), new TypeReference<List<Map<String, Object>>>(){});
                    
                    for (int i = 0; i < headerList.size(); i++) {
                        String code = headerList.get(i).getCd();
                        String value = "-";
                        
                        for (Map<String, Object> ansCnts : ansCntsArray) {
                            if (code.equals(ansCnts.get("code"))) {
                                value = ansCnts.get("name") != null ? ansCnts.get("name").toString() : "-";
                            }
                        }
                        row.createCell(i + 6).setCellValue(value);
                    }
                }

                for (int i = 0; i < headerList.size() + 6; i++) {
                    sheet.autoSizeColumn(i);
                }
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=survey_results.xlsx");
                try {
                    workbook.write(response.getOutputStream());
                    response.getOutputStream().flush();
                } finally {
                    workbook.close();
                }
            }
        }
    }

	public void axleloadAndTrafficStatsDataExcelDownload(HttpServletResponse response, TsTrafficStatisticsExcelDTO result) throws IOException {
		Workbook workbook = null;
        try {
    		workbook = new XSSFWorkbook();
            Sheet sheetByVehicle = workbook.createSheet(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.vehicleSheet"));
            Sheet sheetByTime = workbook.createSheet(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.timeSheet"));

    		// 갑지
            Row topHeaderRow = sheetByVehicle.createRow(0);
            sheetByVehicle.addMergedRegion(new CellRangeAddress( 0, 0, 5, 17));
            
            topHeaderRow.createCell(5).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.vehicleSheet"));
            Row middleHeaderRow = sheetByVehicle.createRow(1);
            middleHeaderRow.createCell(0).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.spot"));
            middleHeaderRow.createCell(1).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.xCoordinate"));
            middleHeaderRow.createCell(2).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.yCoordinate"));
            middleHeaderRow.createCell(3).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.laneCnt"));
            middleHeaderRow.createCell(4).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.avgSpd"));
            
            String[] headerArr = result.getVehicleInfoSheet().getVehicleListSheet().getHeaderArr().split(",");
    
            int headerIndex = 5;
            for(String item : headerArr) {
            	middleHeaderRow.createCell(headerIndex).setCellValue(item);
            	headerIndex++;
            }
            
            middleHeaderRow.createCell(headerIndex).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.total"));
            
            TsTrafficStatisticsExcelDTO.VehicleInfoSheet vehicleInfoSheet = result.getVehicleInfoSheet();
            
            int rowNum = 2;
            Row row = sheetByVehicle.createRow(rowNum);
            row.createCell(0).setCellValue(vehicleInfoSheet.getPointNm());
            row.createCell(1).setCellValue(vehicleInfoSheet.getLat());
            row.createCell(2).setCellValue(vehicleInfoSheet.getLon());
            row.createCell(3).setCellValue(vehicleInfoSheet.getLaneCnt());
            row.createCell(4).setCellValue("-");
            
            String[] trfvlmArr = vehicleInfoSheet.getVehicleListSheet().getTrfvlmArr().split(",");
            
            int trfvlmIndex = 5;
            int totTrfvlm = 0;
            for(String item : trfvlmArr) {
            	row.createCell(trfvlmIndex).setCellValue(item);
            	totTrfvlm += Integer.parseInt(item);
            	trfvlmIndex++;
            }
            
            row.createCell(trfvlmIndex).setCellValue(totTrfvlm);
            
    		// 시간별 교통량
            Row headerRow = sheetByTime.createRow(0);
            headerRow.createCell(0).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.spot"));
            headerRow.createCell(1).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.year"));
            headerRow.createCell(2).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.month"));
            headerRow.createCell(3).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.day"));
            headerRow.createCell(4).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.dayOfWeek"));
            headerRow.createCell(5).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.direction"));
            
            int index = 6;
            for(int i=0; i < 24; i++) {
            	headerRow.createCell(index).setCellValue(i + "~" + (i+1) + "시");
            	index++;
            	if(index == 30) {
            		break;
            	}
            }
            headerRow.createCell(index).setCellValue(CommonUtils.getMessage("statistics.trafficStatsDataExcelDownload.total"));
            
            List<TsTrafficStatisticsExcelDTO.TimeInfoSheet> timeInfoList = result.getTimeInfoSheet();
            
            int dataRowNum = 1;
            for(TsTrafficStatisticsExcelDTO.TimeInfoSheet item : timeInfoList) {
            	Row timeRow = sheetByTime.createRow(dataRowNum);
            	
            	timeRow.createCell(0).setCellValue(item.getPointNm());
            	timeRow.createCell(1).setCellValue(item.getStatsYy());
            	timeRow.createCell(2).setCellValue(item.getStatsMm());
            	timeRow.createCell(3).setCellValue(item.getStatsDd());
            	timeRow.createCell(4).setCellValue(item.getDayOfWeek());
            	timeRow.createCell(5).setCellValue("-");
            	
            	String[] DataArr = item.getTimeListSheet().getTrfvlmArr().split(",");
                
            	int arrRowNum = 6;
            	int totalTrfvlm = 0;
            	for(String data : DataArr) {
            		timeRow.createCell(arrRowNum).setCellValue(data);
            		totalTrfvlm += Integer.parseInt(data);
            		arrRowNum++;
            	}
            	timeRow.createCell(arrRowNum).setCellValue(totalTrfvlm);
                dataRowNum++;
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=survey_results.xlsx");
            
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } finally {
        	if(workbook != null) {
        		workbook.close();        		
        	}
        }
	}
}
