package com.sl.tdbms.web.admin.web.service.datamng;

import com.sl.tdbms.web.admin.common.dto.neighbor.NbMetroCountMngDTO;
import com.sl.tdbms.web.admin.common.util.NbParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * packageName    : com.sl.tdbms.web.admin.web.service.datamng
 * fileName       : NbMetroCountMngService.java
 * author         : kjg08
 * date           : 24. 7. 22.
 * description    : 메트로 카운트 데이터 관리 서비스 클래스.
 * 고정형 및 이동형 장비 데이터를 처리하고, 데이터베이스에 저장합니다.
 * 파일 업로드 결과를 집계하고 처리 이력을 관리합니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 22.        kjg08           최초 생성
 */
@Service
public class NbMetroCountMngService {
    private static final Logger logger = LoggerFactory.getLogger(NbMetroCountMngService.class);
    private static final int BATCH_SIZE = 5000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, String> fixedSiteNameToIdMap;
    private Map<String, String> mobileSiteNameToIdMap;

    private Map<String, Object> finalResult;
    private Timestamp startPassDt;
    private Timestamp endPassDt;
    private int totalInserted;
    private boolean anySuccessfulInsert;

    /**
     * 서비스 클래스의 생성자입니다.
     * 최종 결과를 초기화합니다.
     */
    public NbMetroCountMngService() {
        resetFinalResult();
    }

    /**
     * 서비스 초기화 메소드입니다.
     * 고정형 및 이동형 장비 위치 정보를 데이터베이스에서 로드합니다.
     */
    @PostConstruct
    public void init() {
        fixedSiteNameToIdMap = new HashMap<>();
        mobileSiteNameToIdMap = new HashMap<>();
        loadSiteNameToIdMap();
    }

    /**
     * 최종 결과를 초기화하는 메소드입니다.
     */
    private void resetFinalResult() {
        finalResult = new HashMap<>();
        finalResult.put("successfulFiles", new ArrayList<String>());
        finalResult.put("failedFiles", new ArrayList<String>());
        finalResult.put("fileErrors", new HashMap<String, List<String>>());
        startPassDt = null;
        endPassDt = null;
        totalInserted = 0;
        anySuccessfulInsert = false;
    }

    /**
     * 데이터베이스에서 고정형 및 이동형 장비 위치 정보를 로드하는 메소드입니다.
     */
    private void loadSiteNameToIdMap() {
        try {
            // 고정형 장비 정보 로드
            String fixedSql = "SELECT instllc_id, instllc_nm FROM srlk.tl_fixed_cur";
            jdbcTemplate.query(fixedSql, (rs, rowNum) -> {
                fixedSiteNameToIdMap.put(rs.getString("instllc_nm").toUpperCase(), rs.getString("instllc_id"));
                return null;
            });

            // 이동형 장비 정보 로드
            String mobileSql = "SELECT instllc_id, instllc_nm FROM srlk.tl_mvmneq_cur";
            jdbcTemplate.query(mobileSql, (rs, rowNum) -> {
                mobileSiteNameToIdMap.put(rs.getString("instllc_nm").toUpperCase(), rs.getString("instllc_id"));
                return null;
            });

            //logger.info("고정형 장비 위치 {}개, 이동형 장비 위치 {}개를 로드했습니다.", fixedSiteNameToIdMap.size(), mobileSiteNameToIdMap.size());
        } catch (DataAccessException e) {
            logger.error("장비 위치 정보 로드 중 오류 발생", e);
            throw new RuntimeException("장비 위치 정보를 로드할 수 없습니다.", e);
        }
    }

    /**
     * 장비 유형과 사이트 이름에 따라 사이트 ID를 반환하는 메소드입니다.
     *
     * @param siteName      사이트 이름
     * @param equipmentType 장비 유형 ("fixed" 또는 "mobile")
     * @return 사이트 ID 또는 null (해당하는 ID가 없는 경우)
     */
    private String getSiteId(String siteName, String equipmentType) {
        if ("fixed".equals(equipmentType)) {
            return fixedSiteNameToIdMap.get(siteName.toUpperCase());
        } else if ("mobile".equals(equipmentType)) {
            return mobileSiteNameToIdMap.get(siteName.toUpperCase());
        }
        return null;
    }

    /**
     * 업로드된 파일을 처리하는 메소드입니다.
     *
     * @param file          업로드된 파일
     * @param equipmentType 장비 유형 ("fixed" 또는 "mobile")
     * @return 처리 결과를 담은 Map 객체
     * @throws IOException 파일 처리 중 발생할 수 있는 입출력 예외
     */
    @Transactional
    public Map<String, Object> processFile(MultipartFile file, String equipmentType) throws IOException {
        Instant start = Instant.now();
        Map<String, Object> result = new HashMap<>();

        String equipmentTypeCode = "fixed".equals(equipmentType) ? "ETC007" : "ETC006";

        try {
            Set<String> validSiteNames = "fixed".equals(equipmentType)
                    ? new HashSet<>(fixedSiteNameToIdMap.keySet())
                    : new HashSet<>(mobileSiteNameToIdMap.keySet());

            NbParserUtils.ParseResult parseResult = NbParserUtils.parseFile(file, validSiteNames);
            String siteId = getSiteId(parseResult.siteName, equipmentType);

            if (siteId == null) {
                logger.warn("유효하지 않은 사이트 이름 ({}형 장비). 추출된 이름: '{}', 방향: '{}', 원본 파일명: '{}'",
                        equipmentType, parseResult.siteName, parseResult.direction, file.getOriginalFilename());
                ((List<String>) finalResult.get("failedFiles")).add(file.getOriginalFilename());
                result.put("errorMessage", "유효하지 않은 사이트 이름: " + parseResult.siteName);
                return result;
            }

            int inserted;
            if ("ETC007".equals(equipmentTypeCode)) {
                inserted = processFixedEquipmentData(parseResult.dataList, siteId, parseResult.direction);
            } else if ("ETC006".equals(equipmentTypeCode)) {
                inserted = processMobileEquipmentData(parseResult.dataList, siteId, parseResult.direction);
            } else {
                throw new IllegalArgumentException("유효하지 않은 장비 유형 코드: " + equipmentTypeCode);
            }

            if (inserted > 0) {
                anySuccessfulInsert = true;
                totalInserted += inserted;

                if (!parseResult.dataList.isEmpty()) {
                    updatePassDates(parseResult.dataList);
                }
            }

            updateFinalResult(file.getOriginalFilename(), inserted, parseResult);

            result.put("inserted", inserted);
            result.put("totalRecords", parseResult.dataList.size());
            result.put("errorLines", parseResult.errorLines);

        } catch (Exception e) {
            ((List<String>) finalResult.get("failedFiles")).add(file.getOriginalFilename());
            logger.error("파일 처리 실패: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        //logger.debug("파일 {} 처리 시간: {} 초", file.getOriginalFilename(), duration.getSeconds());

        return result;
    }

    /**
     * 처리된 데이터의 시작 및 종료 날짜를 업데이트하는 메소드입니다.
     *
     * @param dataList 처리된 데이터 리스트
     */
    private void updatePassDates(List<NbMetroCountMngDTO> dataList) {
        Timestamp firstTimestamp = Timestamp.valueOf(dataList.get(0).getDateTime());
        Timestamp lastTimestamp = Timestamp.valueOf(dataList.get(dataList.size() - 1).getDateTime());

        if (startPassDt == null || firstTimestamp.before(startPassDt)) {
            startPassDt = firstTimestamp;
        }
        if (endPassDt == null || lastTimestamp.after(endPassDt)) {
            endPassDt = lastTimestamp;
        }
    }

    /**
     * 최종 처리 결과를 반환하고 초기화하는 메소드입니다.
     *
     * @return 최종 처리 결과를 담은 Map 객체
     */
    public Map<String, Object> getFinalResult() {
        if (anySuccessfulInsert && startPassDt != null && endPassDt != null) {
            insertUploadHistory(anySuccessfulInsert ? "ETC007" : "ETC006", startPassDt, endPassDt);
        }

        finalResult.put("totalInserted", totalInserted);

        Map<String, Object> result = new HashMap<>(finalResult);
        resetFinalResult();
        return result;
    }

    /**
     * 고정형 장비 데이터를 처리하여 데이터베이스에 삽입하는 메소드입니다.
     *
     * @param dataList  처리할 데이터 리스트
     * @param siteId    사이트 ID
     * @param direction 방향
     * @return 삽입된 레코드 수
     */
    private int processFixedEquipmentData(List<NbMetroCountMngDTO> dataList, String siteId, String direction) {
        int inserted = 0;
        AtomicInteger sequenceNumber = new AtomicInteger(1);

        String sql = "INSERT INTO srlk.tl_fixed_pass (sqno, pass_dt, instllc_id, vhcl_drct, vhcl_clsf, vhcl_speed, dr, wb, hdway, gap, ax, gp, rho, cl, ds, trig_num, ht) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (sqno, instllc_id, vhcl_drct) DO NOTHING";

        for (int i = 0; i < dataList.size(); i += BATCH_SIZE) {
            List<NbMetroCountMngDTO> batch = dataList.subList(i, Math.min(i + BATCH_SIZE, dataList.size()));
            try {
                int[] updateCounts = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        NbMetroCountMngDTO dto = batch.get(j);
                        setStatementParameters(ps, dto, siteId, direction, sequenceNumber);
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                });

                for (int count : updateCounts) {
                    if (count > 0) {
                        inserted += count;
                    }
                }
            } catch (DataAccessException e) {
                logger.error("고정형 장비 데이터 배치 삽입 중 오류 발생", e);
                // 개별 레코드 삽입 시도
                for (NbMetroCountMngDTO dto : batch) {
                    try {
                        int affected = jdbcTemplate.update(sql, ps -> setStatementParameters(ps, dto, siteId, direction, sequenceNumber));
                        if (affected > 0) {
                            inserted++;
                        }
                    } catch (DataAccessException ex) {
                        logger.error("개별 레코드 삽입 중 오류 발생: {}", ex.getMessage());
                    }
                }
            }
        }

        return inserted;
    }

    /**
     * 이동형 장비 데이터를 처리하여 데이터베이스에 삽입하는 메소드입니다.
     *
     * @param dataList  처리할 데이터 리스트
     * @param siteId    사이트 ID
     * @param direction 방향
     * @return 삽입된 레코드 수
     */
    private int processMobileEquipmentData(List<NbMetroCountMngDTO> dataList, String siteId, String direction) {
        int inserted = 0;
        AtomicInteger sequenceNumber = new AtomicInteger(1);

        String sql = "INSERT INTO srlk.tl_mvmneq_pass (sqno, pass_dt, instllc_id, vhcl_drct, vhcl_clsf, vhcl_speed, dr, wb, hdway, gap, ax, gp, rho, cl, ds, trig_num, ht) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (sqno, instllc_id, vhcl_drct) DO NOTHING";

        for (int i = 0; i < dataList.size(); i += BATCH_SIZE) {
            List<NbMetroCountMngDTO> batch = dataList.subList(i, Math.min(i + BATCH_SIZE, dataList.size()));
            try {
                int[] updateCounts = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        NbMetroCountMngDTO dto = batch.get(j);
                        setStatementParameters(ps, dto, siteId, direction, sequenceNumber);
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                });

                for (int count : updateCounts) {
                    if (count > 0) {
                        inserted += count;
                    }
                }
            } catch (DataAccessException e) {
                logger.error("이동형 장비 데이터 배치 삽입 중 오류 발생", e);
                // 개별 레코드 삽입 시도
                for (NbMetroCountMngDTO dto : batch) {
                    try {
                        int affected = jdbcTemplate.update(sql, ps -> setStatementParameters(ps, dto, siteId, direction, sequenceNumber));
                        if (affected > 0) {
                            inserted++;
                        }
                    } catch (DataAccessException ex) {
                        logger.error("개별 레코드 삽입 중 오류 발생: {}", ex.getMessage());
                    }
                }
            }
        }

        return inserted;
    }

    /**
     * PreparedStatement에 파라미터를 설정하는 메소드입니다.
     *
     * @param ps             PreparedStatement 객체
     * @param dto            데이터 전송 객체
     * @param siteId         사이트 ID
     * @param direction      방향
     * @param sequenceNumber 시퀀스 번호
     * @throws SQLException SQL 예외 발생 시
     */
    private void setStatementParameters(PreparedStatement ps, NbMetroCountMngDTO dto, String siteId, String direction, AtomicInteger sequenceNumber) throws SQLException {
        // sqno 생성 (초 단위까지만 포함)
        String dateTimeStr = dto.getDateTime().substring(0, 19).replace(" ", "_");
        String sqno = dateTimeStr + "_" + sequenceNumber.getAndIncrement();

        ps.setString(1, sqno);
        ps.setTimestamp(2, Timestamp.valueOf(dto.getDateTime()));
        ps.setString(3, siteId);
        ps.setString(4, direction);
        ps.setString(5, String.valueOf(dto.getVehicleType()));
        ps.setDouble(6, dto.getSpeed());
        ps.setString(7, dto.getDirectionCode());
        ps.setDouble(8, dto.getWheelbase());
        ps.setDouble(9, dto.getHeadway());
        ps.setDouble(10, dto.getGap());
        ps.setInt(11, dto.getAxleCount());
        ps.setInt(12, dto.getGroupCount());
        ps.setDouble(13, dto.getRhoValue());
        ps.setInt(14, dto.getVehicleClass());
        ps.setString(15, dto.getDatasetIdentifier());
        ps.setString(16, dto.getTriggerNumber());
        ps.setString(17, dto.getHeight());
    }

    /**
     * 메트로 장비 데이터 처리 후 이력을 테이블에 삽입하는 메소드입니다.
     *
     * @param equipmentType 장비 유형
     * @param startPassDt   시작 날짜
     * @param endPassDt     종료 날짜
     */
    private void insertUploadHistory(String equipmentType, Timestamp startPassDt, Timestamp endPassDt) {
        String sql = "INSERT INTO srlk.tl_metro_upload_history (create_dt, metro_type, start_pass_dt, end_pass_dt) " +
                "VALUES (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    Timestamp.from(Instant.now()),
                    equipmentType,
                    startPassDt,
                    endPassDt
            );
            //logger.info("업로드 이력 삽입 완료 - 장비 유형: {}, 시작: {}, 종료: {}", equipmentType, startPassDt, endPassDt);
        } catch (DataAccessException e) {
            logger.error("업로드 이력 삽입 중 오류 발생", e);
            throw new RuntimeException("업로드 이력을 기록하는 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 최종 결과를 업데이트하는 메소드입니다.
     *
     * @param fileName    파일 이름
     * @param inserted    삽입된 레코드 수
     * @param parseResult 파싱 결과
     */
    @Autowired
    private MessageSource messageSource;

    private void updateFinalResult(String fileName, int inserted, NbParserUtils.ParseResult parseResult) {
        List<String> errors = new ArrayList<>();
        Locale locale = LocaleContextHolder.getLocale();

        Set<Integer> problemLines = new TreeSet<>();
        problemLines.addAll(parseResult.errorLines.keySet());
        problemLines.addAll(parseResult.invalidTypeLines);
        problemLines.addAll(parseResult.dataList.stream()
                .skip(inserted)
                .map(NbMetroCountMngDTO::getLineNumber)
                .collect(Collectors.toSet()));

        if (!problemLines.isEmpty()) {
            String lineNumbers = formatProblemLines(problemLines);
            String uninsertedLinesMsg = messageSource.getMessage("metrocount.upload.uninsertedLines",
                    new Object[]{lineNumbers},
                    locale);
            errors.add(uninsertedLinesMsg);
        }

        int totalRecords = parseResult.totalRecords;

        if (inserted == 0) {
            ((List<String>) finalResult.get("failedFiles")).add(fileName);
        } else if (inserted < totalRecords) {
            ((Map<String, List<String>>) finalResult.get("fileErrors")).put(fileName, errors);
        } else {
            ((List<String>) finalResult.get("successfulFiles")).add(fileName);
        }

        String insertionSummaryMsg = messageSource.getMessage("metrocount.upload.insertionSummary",
                new Object[]{totalRecords, inserted},
                locale);
        errors.add(insertionSummaryMsg);
        ((Map<String, List<String>>) finalResult.get("fileErrors")).put(fileName, errors);
    }

    private String formatProblemLines(Set<Integer> lines) {
        List<Integer> lineList = new ArrayList<>(lines);
        if (lineList.size() <= 100) {
            return lineList.stream().map(String::valueOf).collect(Collectors.joining(", "));
        } else {
            return lineList.stream().limit(100).map(String::valueOf).collect(Collectors.joining(", ")) + "...";
        }
    }
}