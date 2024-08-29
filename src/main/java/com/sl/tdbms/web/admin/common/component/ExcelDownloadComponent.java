package com.sl.tdbms.web.admin.common.component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Component
public class ExcelDownloadComponent {

	/**
	 * @brief : 엑셀 다운로드
	 * @details : 엑셀 다운로드
	 * @author : KC.KIM
	 * @date : 2024.04.25
	 * @param : resp
	 * @param : headerArray(헤더 데이터)
	 * @param : bodyList(데이터 리스트)
	 * @param : fileName(파일명)
	 * @return :
	 * @throws IOException
	 */
	/*public void excelDownload(HttpServletResponse resp, String[] headerArray, List<Object> bodyList, String fileName) {
		try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
			SXSSFSheet sheet = workbook.createSheet(fileName);
			sheet.trackAllColumnsForAutoSizing();
			int rowNo = 0;

			SXSSFRow row = sheet.createRow(rowNo++);

			int headLength = makeHeader(row, headerArray);

			row = sheet.createRow(rowNo);
			if(null != bodyList && !bodyList.isEmpty()){
				makeBody(sheet, row, bodyList, headLength, rowNo);
			}

			for(int i=0;i<headLength;i++){
				sheet.autoSizeColumn(i);
			}

			try {
				resp.setContentType("ms-vnd/excel");
				resp.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");

				workbook.write(resp.getOutputStream());
			}catch (IOException ignored){
			}
		} catch (IOException e) {
			throw new CommonException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
		}
	}*/

	public int makeHeader(SXSSFRow row, String[] exHeader) {
		for(int i=0; i < exHeader.length; i++) {
			row.createCell(i).setCellValue(exHeader[i]);
		}
		return exHeader.length;
	}

	public void makeBody(SXSSFSheet sheet, SXSSFRow row, List<Object> bodyList, int headLength, int rowNo) {
		int cellNo = 0;

		for(int i=0; i < bodyList.size(); i++) {
			if(i % headLength  == 0) {
				row = sheet.createRow(rowNo++);
				cellNo = 0;
			}
			row.createCell(cellNo).setCellValue(String.valueOf(bodyList.get(i)));
			cellNo++;

		}
	}
	
	/**
	  * @Method Name : excelDownloadForsurveyHistory
	  * @작성일 : 2024. 7. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 조사 현황 및 일정 상세 결과 엑셀 다운로드
	  * @param resp
	  * @param headerArray
	  * @param bodyList
	  * @param fileName
	  */
	public void excelDownload(HttpServletResponse resp, String[] headerArray, List<String[]> bodyList, String fileName) {
		try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
			SXSSFSheet sheet = workbook.createSheet(fileName);
			sheet.trackAllColumnsForAutoSizing();
			int rowNo = 0;

			SXSSFRow row = sheet.createRow(rowNo++);

			int headLength = makeHeader(row, headerArray);

			row = sheet.createRow(rowNo);
			
			String cellValue;
			for(String[] body : bodyList) {
				int cellNo = 0;
//				for(String value : body) {
				for(int i = 0; i < headLength; i++) { 
					cellValue = body.length <= i ? "-" : body[i];
					row.createCell(cellNo++).setCellValue(cellValue);
				}
				row = sheet.createRow(++rowNo);
				cellNo = 0;
			}

			for(int i=0;i<headLength;i++){
				sheet.autoSizeColumn(i);
			}

			resp.setContentType("ms-vnd/excel");
			resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20") + ".xls");

			workbook.write(resp.getOutputStream());
		} catch (IOException e) {
			throw new CommonException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
		}
	}

	
	public void invstrBulkUploadExcelDownload(HttpServletResponse resp) throws IOException {
		Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Upload Sheet");
		
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue(CommonUtils.getMessage("invst.invstDetail.name")); 	// 이름
        headerRow.createCell(1).setCellValue(CommonUtils.getMessage("invst.invstDetail.contact")); 	// 연락처
        headerRow.createCell(2).setCellValue(CommonUtils.getMessage("invst.invstDetail.email")); 	// 이메일
        headerRow.createCell(3).setCellValue(CommonUtils.getMessage("invst.invstDetail.role")); 	// 역할
        
        Cell cell = headerRow.getCell(3);

        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();

        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 2); // 주석의 크기를 지정하는데 사용
        anchor.setRow1(headerRow.getRowNum());
        anchor.setRow2(headerRow.getRowNum() + 3); // 주석의 크기를 지정하는데 사용

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(CommonUtils.getMessage("invst.invstDetail.role.message")));

        cell.setCellComment(comment);
        
        String[] options = {CommonUtils.getMessage("invst.invstDetail.leader"), CommonUtils.getMessage("invst.invstDetail.member")};
        
        CellRangeAddressList addressList = new CellRangeAddressList(0, 50, 3, 3);

        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(options);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setShowErrorBox(true);
        
        sheet.addValidationData(dataValidation);
        
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=investigator_bulk_upload.xlsx");
        try {
            workbook.write(resp.getOutputStream());
            resp.getOutputStream().flush();
        } finally {
            workbook.close();
        }
	}
	
}
