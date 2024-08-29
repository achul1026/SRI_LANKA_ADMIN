package com.sl.tdbms.web.admin.common.util;

import com.sl.tdbms.web.admin.common.dto.neighbor.NbMccTmStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.neighbor.NbMetroCountMngDTO;
import com.sl.tdbms.web.admin.common.dto.neighbor.NbMetroStatisticsDTO;
import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * packageName    : com.sl.tdbms.web.admin.common.util
 * fileName       : NbParserUtils.java
 * author         : kjg08
 * date           : 24. 7. 22.
 * description    : 파일 파싱, 데이터 처리, 엑셀 리포트 생성을 위한 유틸리티 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 22.        kjg08           최초 생성
 */

/**
 * NbParserUtils 클래스는 파일 파싱, 데이터 처리, 엑셀 리포트 생성 등의 기능을 제공하는 유틸리티 클래스입니다.
 * 주요 기능으로는 파일 파싱, 엑셀 리포트 생성, 데이터 처리 등이 있습니다.
 */
public class NbParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(NbParserUtils.class);

    // 설치 위치 이름과 해당 ID를 매핑하는 정적 맵
    private static final Map<String, String> siteNameToIdMap = new HashMap<>();

    /**
     * 파일을 파싱하여 DTO 목록을 반환합니다.
     *
     * @param file           업로드된 파일
     * @param validSiteNames 유효한 사이트 이름 목록
     * @return 파싱된 DTO 목록과 에러 정보를 포함한 ParseResult 객체
     * @throws IOException 파일 읽기 중 발생할 수 있는 IO 예외
     */
    public static ParseResult parseFile(MultipartFile file, Set<String> validSiteNames) throws IOException {
        List<NbMetroCountMngDTO> dataList = new ArrayList<>();
        Map<Integer, String> errorLines = new HashMap<>();
        List<Integer> invalidTypeLines = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        SiteInfo siteInfo = extractSiteInfo(fileName, validSiteNames);
        int totalLines = 0;
        int totalRecords = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isDataSection = false;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                totalLines++;
                if (line.startsWith("DS Trig Num Ht YYYY-MM-DD hh:mm:ss Dr  Speed")) {
                    isDataSection = true;
                    continue;
                }
                if (line.startsWith("In profile:")) {
                    break;
                }
                if (isDataSection && !line.trim().isEmpty()) {
                    totalRecords++; // 모든 데이터 라인을 레코드로 간주
                    try {
                        NbMetroCountMngDTO data = parseLine(line);
                        if (data != null) {
                            data.setLineNumber(lineNumber);
                            if (!data.getVehicleType().contains("???")) {
                                data.setVhclDrct(siteInfo.direction);
                                dataList.add(data);
                            } else {
                                invalidTypeLines.add(lineNumber);
                            }
                        } else {
                            errorLines.put(lineNumber, "Invalid data format");
                        }
                    } catch (Exception e) {
                        logger.error("Error parsing line {}: {}", lineNumber, e.getMessage());
                        errorLines.put(lineNumber, "Parsing error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file {}: {}", fileName, e.getMessage());
            throw new IOException("파일 읽기 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

        if (!errorLines.isEmpty()) {
            logger.warn("Errors in file {}: {}", fileName, errorLines);
        }

        return new ParseResult(dataList, errorLines, invalidTypeLines, siteInfo.name, siteInfo.direction, totalLines, totalRecords);
    }

    /**
     * 한 줄을 파싱하여 DTO 객체를 생성합니다.
     *
     * @param line 파싱할 문자열
     * @return 파싱된 DTO 객체 또는 파싱 실패 시 null 반환
     */
    private static NbMetroCountMngDTO parseLine(String line) {
        String[] tokens = line.split("\\s+");
        int expectedFieldCount = 16;  // 'o' 문자 이전에 예상되는 필드 수

        // 'o' 문자 이전의 토큰 수 확인
        int actualFieldCount = 0;
        for (String token : tokens) {
            if (token.contains("o")) {
                break;
            }
            actualFieldCount++;
        }

        if (actualFieldCount != expectedFieldCount) {
            return null;  // 필드 수가 맞지 않으면 null 반환
        }

        try {
            NbMetroCountMngDTO data = new NbMetroCountMngDTO();
            data.setDatasetIdentifier(tokens[0]);
            data.setTriggerNumber(tokens[1]);
            data.setHeight(tokens[2]);
            data.setDateTime(tokens[3] + " " + tokens[4]);
            data.setDirectionCode(tokens[5]);
            data.setSpeed(Double.parseDouble(tokens[6]));
            data.setWheelbase(Double.parseDouble(tokens[7]));
            data.setHeadway(Double.parseDouble(tokens[8]));
            data.setGap(Double.parseDouble(tokens[9]));
            data.setAxleCount(Integer.parseInt(tokens[10]));
            data.setGroupCount(Integer.parseInt(tokens[11]));
            data.setRhoValue(Double.parseDouble(tokens[12]));
            data.setVehicleClass(Integer.parseInt(tokens[13]));
            data.setNumber(tokens[14]);
            data.setVehicleType(tokens[15]);
            return data;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            logger.error("Error parsing line: {}", line, e);
            return null;  // 파싱 중 오류 발생 시 null 반환
        }
    }

    /**
     * 파일 이름에서 사이트 정보를 추출합니다.
     *
     * @param fileName       파일 이름
     * @param validSiteNames 유효한 사이트 이름 목록
     * @return 추출된 사이트 정보
     */
    public static SiteInfo extractSiteInfo(String fileName, Set<String> validSiteNames) {
        // 파일 확장자 제거
        fileName = fileName.replaceFirst("[.][^.]+$", "");

        // 괄호와 그 내용 제거
        fileName = fileName.replaceAll("\\([^)]*\\)", "");

        // 숫자와 점으로 시작하는 부분 제거 (예: "4. ")
        fileName = fileName.replaceAll("^\\d+\\.\\s*", "");

        String siteName = null;
        String direction = null;

        // 유효한 사이트 이름 찾기
        for (String validName : validSiteNames) {
            int index = fileName.toUpperCase().indexOf(validName);
            if (index != -1) {
                siteName = validName;
                // 사이트 이름 뒤의 문자열 검사
                String remaining = fileName.substring(index + validName.length()).trim();
                if (!remaining.isEmpty()) {
                    if (remaining.startsWith("0") || remaining.startsWith("1")) {
                        direction = remaining.substring(0, 1);
                    } else if (remaining.length() > 1 && (remaining.charAt(1) == '0' || remaining.charAt(1) == '1')) {
                        direction = String.valueOf(remaining.charAt(1));
                    }
                }
                break;
            }
        }

        // 사이트 이름을 찾지 못했다면, 첫 번째 단어를 사용
        if (siteName == null) {
            String[] words = fileName.split("\\s+");
            if (words.length > 0) {
                siteName = words[0].toUpperCase();
                if (words.length > 1 && (words[1].equals("0") || words[1].equals("1"))) {
                    direction = words[1];
                }
            }
        }

        // 방향을 찾지 못했다면, 파일 이름에서 0 또는 1을 찾아봄
        if (direction == null) {
            Pattern pattern = Pattern.compile("\\b[01]\\b");
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                direction = matcher.group();
            }
        }

        return new SiteInfo(siteName, direction);
    }

    /**
     * 엑셀 리포트를 생성합니다.
     *
     * @param outputStream 엑셀 파일을 쓸 OutputStream
     * @param item         메트로 또는 MCC,TM 구분
     * @param dataConsumer 데이터를 제공할 Consumer
     * @throws IOException IO 예외 발생 시
     */
    public static void generateExcelReport(OutputStream outputStream, String item, Consumer<Consumer<Object>> dataConsumer) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Statistics Data");
            createReport(sheet, item, dataConsumer);
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error("Error generating Excel report: {}", e.getMessage());
            throw new IOException("엑셀 리포트 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 엑셀 리포트를 구성하는 메서드입니다.
     *
     * @param sheet        엑셀 시트 객체
     * @param item         메트로 또는 MCC,TM 구분
     * @param dataConsumer 데이터 Consumer
     */
    private static void createReport(Sheet sheet, String item, Consumer<Consumer<Object>> dataConsumer) {
        createTitle(sheet);
        createHeaderRows(sheet, item);
        createFilterRow(sheet, item);
        fillDataRows(sheet, item, dataConsumer);
        setColumnWidths(sheet, item);
        setAutoFilter(sheet, item);
    }

    /**
     * 필터 행을 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param item  메트로 또는 MCC,TM 구분
     */
    private static void createFilterRow(Sheet sheet, String item) {
        Row filterRow = sheet.createRow(3);
        CellStyle filterStyle = createFilterStyle(sheet.getWorkbook());

        int columnCount = "METRO".equals(item) ? 34 : 42;
        for (int i = 0; i < columnCount; i++) {
            Cell cell = filterRow.createCell(i);
            cell.setCellStyle(filterStyle);
        }
    }

    /**
     * 필터 스타일을 생성합니다.
     *
     * @param workbook 엑셀 워크북 객체
     * @return 필터 스타일 객체
     */
    private static CellStyle createFilterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 타이틀을 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     */
    private static void createTitle(Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("VEHICLE COMPOSITION OF TRAFFIC ON NATIONAL HIGHWAYS");
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setBorderBottom(BorderStyle.DOUBLE);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 33));
    }

    /**
     * 엑셀 시트에 헤더 행을 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param item  메트로 또는 MCC,TM 구분
     */
    private static void createHeaderRows(Sheet sheet, String item) {
        Row headerRow1 = sheet.createRow(1);
        Row headerRow2 = sheet.createRow(2);

        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle subHeaderStyle = createSubHeaderStyle(sheet.getWorkbook());

        // 공통 헤더 생성
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 0, "ROUTE NO", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 1, "NAME OF ROAD", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 2, "PROV", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 3, "CE", 1, headerStyle);
        createGroupHeader(sheet, headerRow1, headerRow2, 4, "LOCATION", 2, headerStyle, subHeaderStyle);
        createSubHeaderCell(headerRow2, 4, "Lat", 1, subHeaderStyle);
        createSubHeaderCell(headerRow2, 5, "Long", 1, subHeaderStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 6, "TYPE OF COUNT", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 7, "DATE OF COUNT", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 8, "TOTAL NO.OF VEH.", 1, headerStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 9, "HRS OF COUNT", 1, headerStyle);

        // METRO 또는 MCC,TM에 따른 특정 헤더 생성
        if ("METRO".equals(item)) {
            createMetroHeaders(sheet, headerRow1, headerRow2, headerStyle, subHeaderStyle);
        } else if ("MCC,TM".equals(item)) {
            createMccTmHeaders(sheet, headerRow1, headerRow2, headerStyle, subHeaderStyle);
        }

        headerRow1.setHeight((short) 400);
        headerRow2.setHeight((short) 400);

        setRowBorder(sheet, 1, BorderStyle.THIN);
        setRowBorder(sheet, 2, BorderStyle.MEDIUM);
    }

    /**
     * METRO 항목에 대한 특정 헤더를 생성합니다.
     */
    private static void createMetroHeaders(Sheet sheet, Row headerRow1, Row headerRow2, CellStyle headerStyle, CellStyle subHeaderStyle) {
        String[] metroVehicleTypes = {"MCL", "TWL", "CAR", "LGV", "MP&GV", "LP&GV", "HG3", "AG3", "AG4", "AG5", "AG6"};
        createGroupHeader(sheet, headerRow1, headerRow2, 10, "% CONTRIBUTION OF EACH VEHICLE TYPE", metroVehicleTypes.length, headerStyle, subHeaderStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 10 + metroVehicleTypes.length, "CUM % OF VEH", 1, headerStyle);
        createGroupHeader(sheet, headerRow1, headerRow2, 11 + metroVehicleTypes.length, "NUMBER OF VEHICLE OF EACH TYPE", metroVehicleTypes.length, headerStyle, subHeaderStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 11 + 2 * metroVehicleTypes.length, "TOTAL", 1, headerStyle);

        for (int i = 0; i < metroVehicleTypes.length; i++) {
            createSubHeaderCell(headerRow2, 10 + i, metroVehicleTypes[i] + " %", 1, subHeaderStyle);
            createSubHeaderCell(headerRow2, 11 + metroVehicleTypes.length + i, metroVehicleTypes[i] + " COUNT", 1, subHeaderStyle);
        }
    }

    /**
     * MCC,TM 항목에 대한 특정 헤더를 생성합니다.
     */
    private static void createMccTmHeaders(Sheet sheet, Row headerRow1, Row headerRow2, CellStyle headerStyle, CellStyle subHeaderStyle) {
        String[] mccTmVehicleTypes = {"MCL", "TWL", "CAR", "VAN", "MBU", "LBU", "LGV", "MG1", "MG2", "HG3", "AG3", "AG4", "AG5", "AG6", "FVH"};
        createGroupHeader(sheet, headerRow1, headerRow2, 10, "% CONTRIBUTION OF EACH VEHICLE TYPE", mccTmVehicleTypes.length, headerStyle, subHeaderStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 10 + mccTmVehicleTypes.length, "CUM % OF VEH", 1, headerStyle);
        createGroupHeader(sheet, headerRow1, headerRow2, 11 + mccTmVehicleTypes.length, "NUMBER OF VEHICLE OF EACH TYPE", mccTmVehicleTypes.length, headerStyle, subHeaderStyle);
        createMergedHeaderCell(sheet, headerRow1, headerRow2, 11 + 2 * mccTmVehicleTypes.length, "TOTAL", 1, headerStyle);

        for (int i = 0; i < mccTmVehicleTypes.length; i++) {
            createSubHeaderCell(headerRow2, 10 + i, mccTmVehicleTypes[i] + " %", 1, subHeaderStyle);
            createSubHeaderCell(headerRow2, 11 + mccTmVehicleTypes.length + i, mccTmVehicleTypes[i] + " COUNT", 1, subHeaderStyle);
        }
    }

    /**
     * 그룹 헤더를 생성합니다.
     */
    private static void createGroupHeader(Sheet sheet, Row row1, Row row2, int column, String value, int columnSpan, CellStyle headerStyle, CellStyle subHeaderStyle) {
        createHeaderCell(row1, column, value, columnSpan, headerStyle);
        for (int i = 0; i < columnSpan; i++) {
            createSubHeaderCell(row2, column + i, "", 1, subHeaderStyle);
        }
        setGroupHeaderBorder(sheet, row1.getRowNum(), row2.getRowNum(), column, column + columnSpan - 1);
    }

    /**
     * 그룹 헤더의 테두리를 설정합니다.
     */
    private static void setGroupHeaderBorder(Sheet sheet, int topRow, int bottomRow, int leftCol, int rightCol) {
        CellRangeAddress region = new CellRangeAddress(topRow, bottomRow, leftCol, rightCol);

        // 모든 외곽 테두리를 MEDIUM으로 설정
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);

        // 내부 세로선
        for (int col = leftCol + 1; col <= rightCol; col++) {
            RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(bottomRow, bottomRow, col, col), sheet);
        }
    }

    /**
     * 지정된 영역의 테두리를 설정합니다.
     */
    private static void setRegionBorder(Sheet sheet, CellRangeAddress region, BorderStyle borderStyle) {
        RegionUtil.setBorderTop(borderStyle, region, sheet);
        RegionUtil.setBorderBottom(borderStyle, region, sheet);
        RegionUtil.setBorderLeft(borderStyle, region, sheet);
        RegionUtil.setBorderRight(borderStyle, region, sheet);
    }

    /**
     * 지정된 행의 모든 셀에 대해 테두리를 설정합니다.
     */
    private static void setRowBorder(Sheet sheet, int rowIndex, BorderStyle borderStyle) {
        Row row = sheet.getRow(rowIndex);
        for (Cell cell : row) {
            CellStyle style = cell.getCellStyle();
            style.setBorderBottom(borderStyle);
            cell.setCellStyle(style);
        }
    }

    /**
     * 헤더 스타일을 생성합니다.
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 서브 헤더 스타일을 생성합니다.
     */
    private static CellStyle createSubHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 병합된 헤더 셀을 생성합니다.
     */
    private static void createMergedHeaderCell(Sheet sheet, Row row1, Row row2, int column, String value, int columnSpan, CellStyle style) {
        Cell cell = row1.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(row1.getRowNum(), row2.getRowNum(), column, column + columnSpan - 1));
        setRegionBorder(sheet, new CellRangeAddress(row1.getRowNum(), row2.getRowNum(), column, column + columnSpan - 1), BorderStyle.MEDIUM);
    }

    /**
     * 헤더 셀을 생성합니다.
     */
    private static void createHeaderCell(Row row, int column, String value, int columnSpan, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        if (columnSpan > 1) {
            row.getSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), column, column + columnSpan - 1));
        }
    }

    /**
     * 서브 헤더 셀을 생성합니다.
     */
    private static void createSubHeaderCell(Row row, int column, String value, int columnSpan, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        CellStyle newStyle = row.getSheet().getWorkbook().createCellStyle();
        newStyle.cloneStyleFrom(style);
        newStyle.setBorderLeft(BorderStyle.THIN);
        newStyle.setBorderRight(BorderStyle.THIN);
        newStyle.setBorderTop(BorderStyle.THIN);
        newStyle.setBorderBottom(BorderStyle.MEDIUM);
        cell.setCellStyle(newStyle);
    }

    /**
     * 데이터 행을 채웁니다.
     */
    private static void fillDataRows(Sheet sheet, String item, Consumer<Consumer<Object>> dataConsumer) {
        AtomicInteger rowNum = new AtomicInteger(4);
        CellStyle dataStyle = createDataStyle(sheet.getWorkbook());

        dataConsumer.accept(dto -> {
            Row row = sheet.createRow(rowNum.getAndIncrement());
            try {
                if ("METRO".equals(item)) {
                    fillMetroDataRow(row, (NbMetroStatisticsDTO) dto, dataStyle);
                } else {
                    fillMccTmDataRow(row, (NbMccTmStatisticsDTO) dto, dataStyle);
                }
            } catch (ClassCastException e) {
                logger.error("Error casting data object: {}", e.getMessage());
            }
        });
    }

    /**
     * 자동 필터를 설정합니다.
     */
    private static void setAutoFilter(Sheet sheet, String item) {
        if (sheet instanceof SXSSFSheet) {
            SXSSFSheet sxssfSheet = (SXSSFSheet) sheet;
            int lastColumn = "METRO".equals(item) ? 33 : 41;
            sxssfSheet.setAutoFilter(new CellRangeAddress(3, sxssfSheet.getLastRowNum(), 0, lastColumn));
        }
    }

    /**
     * 데이터 스타일을 생성합니다.
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderToAll(style);
        return style;
    }

    /**
     * 모든 셀에 대해 테두리를 설정합니다.
     */
    private static void setBorderToAll(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    /**
     * 메트로 데이터 행을 채웁니다.
     */
    private static void fillMetroDataRow(Row row, NbMetroStatisticsDTO stat, CellStyle style) {
        createCell(row, 0, stat.getRouteNo(), style);
        createCell(row, 1, stat.getNameOfRoad(), style);
        createCell(row, 2, stat.getProv(), style);
        createCell(row, 3, stat.getCe(), style);
        createCell(row, 4, stat.getLat(), style);
        createCell(row, 5, stat.getLon(), style);
        createCell(row, 6, stat.getTypeOfCount(), style);
        createCell(row, 7, stat.getDateOfCount(), style);
        createCell(row, 8, stat.getTotalNoOfVeh(), style);
        createCell(row, 9, stat.getHrsOfCount(), style);
        createCell(row, 10, stat.getMclPercentage(), style);
        createCell(row, 11, stat.getTwlPercentage(), style);
        createCell(row, 12, stat.getCarPercentage(), style);
        createCell(row, 13, stat.getLgvPercentage(), style);
        createCell(row, 14, stat.getMpgvPercentage(), style);
        createCell(row, 15, stat.getLpgvPercentage(), style);
        createCell(row, 16, stat.getHg3Percentage(), style);
        createCell(row, 17, stat.getAg3Percentage(), style);
        createCell(row, 18, stat.getAg4Percentage(), style);
        createCell(row, 19, stat.getAg5Percentage(), style);
        createCell(row, 20, stat.getAg6Percentage(), style);
        createCell(row, 21, stat.getCumPercentageOfVeh(), style);
        createCell(row, 22, stat.getMclCount(), style);
        createCell(row, 23, stat.getTwlCount(), style);
        createCell(row, 24, stat.getCarCount(), style);
        createCell(row, 25, stat.getLgvCount(), style);
        createCell(row, 26, stat.getMpgvCount(), style);
        createCell(row, 27, stat.getLpgvCount(), style);
        createCell(row, 28, stat.getHg3Count(), style);
        createCell(row, 29, stat.getAg3Count(), style);
        createCell(row, 30, stat.getAg4Count(), style);
        createCell(row, 31, stat.getAg5Count(), style);
        createCell(row, 32, stat.getAg6Count(), style);
        createCell(row, 33, stat.getTotal(), style);
    }

    /**
     * MCC, TM 데이터 행을 채웁니다.
     */
    private static void fillMccTmDataRow(Row row, NbMccTmStatisticsDTO stat, CellStyle style) {
        createCell(row, 0, stat.getRouteNo(), style);
        createCell(row, 1, stat.getNameOfRoad(), style);
        createCell(row, 2, stat.getProv(), style);
        createCell(row, 3, stat.getCe(), style);
        createCell(row, 4, stat.getLat(), style);
        createCell(row, 5, stat.getLon(), style);
        createCell(row, 6, stat.getTypeOfCount(), style);
        createCell(row, 7, stat.getDateOfCount(), style);
        createCell(row, 8, stat.getTotalNoOfVeh(), style);
        createCell(row, 9, stat.getHrsOfCount(), style);
        createCell(row, 10, stat.getMclPercentage(), style);
        createCell(row, 11, stat.getTwlPercentage(), style);
        createCell(row, 12, stat.getCarPercentage(), style);
        createCell(row, 13, stat.getVanPercentage(), style);
        createCell(row, 14, stat.getMbuPercentage(), style);
        createCell(row, 15, stat.getLbuPercentage(), style);
        createCell(row, 16, stat.getLgvPercentage(), style);
        createCell(row, 17, stat.getMg1Percentage(), style);
        createCell(row, 18, stat.getMg2Percentage(), style);
        createCell(row, 19, stat.getHg3Percentage(), style);
        createCell(row, 20, stat.getAg3Percentage(), style);
        createCell(row, 21, stat.getAg4Percentage(), style);
        createCell(row, 22, stat.getAg5Percentage(), style);
        createCell(row, 23, stat.getAg6Percentage(), style);
        createCell(row, 24, stat.getFvhPercentage(), style);
        createCell(row, 25, stat.getCumPercentageOfVeh(), style);
        createCell(row, 26, stat.getMclCount(), style);
        createCell(row, 27, stat.getTwlCount(), style);
        createCell(row, 28, stat.getCarCount(), style);
        createCell(row, 29, stat.getVanCount(), style);
        createCell(row, 30, stat.getMbuCount(), style);
        createCell(row, 31, stat.getLbuCount(), style);
        createCell(row, 32, stat.getLgvCount(), style);
        createCell(row, 33, stat.getMg1Count(), style);
        createCell(row, 34, stat.getMg2Count(), style);
        createCell(row, 35, stat.getHg3Count(), style);
        createCell(row, 36, stat.getAg3Count(), style);
        createCell(row, 37, stat.getAg4Count(), style);
        createCell(row, 38, stat.getAg5Count(), style);
        createCell(row, 39, stat.getAg6Count(), style);
        createCell(row, 40, stat.getFvhCount(), style);
        createCell(row, 41, stat.getTotal(), style);
    }

    /**
     * 셀을 생성하고 값을 설정합니다.
     */
    private static void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value != null) {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    /**
     * 열 너비를 설정합니다.
     */
    private static void setColumnWidths(Sheet sheet, String item) {
        int columnCount = "METRO".equals(item) ? 34 : 42;
        for (int i = 0; i < columnCount; i++) {
            sheet.setColumnWidth(i, 15 * 256); // 15 characters wide
        }
        sheet.setColumnWidth(1, 30 * 256); // NAME OF ROAD column wider
    }

    /**
     * 사이트 정보를 나타내는 내부 클래스입니다.
     */
    public static class SiteInfo {
        public String name;
        public String direction;

        public SiteInfo(String name, String direction) {
            this.name = name;
            this.direction = direction;
        }
    }

    /**
     * 파일 파싱 결과를 나타내는 클래스입니다.
     */
    public static class ParseResult {
        public List<NbMetroCountMngDTO> dataList;
        public Map<Integer, String> errorLines;
        public List<Integer> invalidTypeLines;
        public String siteName;
        public String direction;
        public int totalLines;
        public int totalRecords;


        public ParseResult(List<NbMetroCountMngDTO> dataList, Map<Integer, String> errorLines,
                           List<Integer> invalidTypeLines, String siteName, String direction,
                           int totalLines, int totalRecords) {
            this.dataList = dataList;
            this.errorLines = errorLines;
            this.invalidTypeLines = invalidTypeLines;
            this.siteName = siteName;
            this.direction = direction;
            this.totalLines = totalLines;
            this.totalRecords = totalRecords;
        }
    }

    /**
     * 파일 파싱 중 발생한 예외를 나타내는 클래스입니다.
     */
    @Getter
    public static class FileParsingException extends RuntimeException {
        private final String fileName;
        private final List<String> errorLines;

        public FileParsingException(String fileName, List<String> errorLines) {
            super("파일 파싱 중 오류가 발생했습니다: " + fileName);
            this.fileName = fileName;
            this.errorLines = errorLines;
        }

        public FileParsingException(String fileName, List<String> errorLines, Throwable cause) {
            super("파일 파싱 중 오류가 발생했습니다: " + fileName, cause);
            this.fileName = fileName;
            this.errorLines = errorLines;
        }
    }
}