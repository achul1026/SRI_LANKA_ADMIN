package com.sl.tdbms.web.admin.web.service.statistics;

import com.sl.tdbms.web.admin.common.dto.neighbor.NbMccTmStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.neighbor.NbMetroStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.neighbor.StatisticsSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * packageName    : com.sl.tdbms.web.admin.web.service.statistics
 * fileName       : NbStatisticsDownService.java
 * author         : kjg08
 * date           : 24. 7. 29.
 * description    : 통계 데이터 관리 및 제공 서비스 클래스
 * METRO와 MCC,TM 두 가지 타입의 통계 데이터를 처리하며,
 * 데이터 조회, 페이징, 엑셀 다운로드 기능을 제공합니다.
 * JdbcTemplate을 사용하여 직접 SQL 쿼리를 실행하고, 결과를 DTO로 매핑합니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 29.        kjg08           최초 생성
 */
@Service
@RequiredArgsConstructor
public class NbStatisticsDownService {
    private static final Logger logger = LoggerFactory.getLogger(NbStatisticsDownService.class);
    private final JdbcTemplate jdbcTemplate;

    /**
     * 사용 가능한 연도 목록을 조회합니다.
     *
     * @return 사용 가능한 연도 목록
     * @throws DatabaseException 데이터베이스 조회 중 오류 발생 시
     */
    public List<String> getAvailableYears() {
        String sql = "SELECT DISTINCT year FROM (" +
                "    SELECT DISTINCT SUBSTRING(stats_yymmdt, 1, 4) AS year FROM srlk.ts_fixed_onhr " +
                "    UNION " +
                "    SELECT DISTINCT SUBSTRING(stats_yymmdt, 1, 4) AS year FROM srlk.ts_mvmneq_onhr" +
                ") AS combined_years " +
                "ORDER BY year DESC";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("year"));
        } catch (DataAccessException e) {
            logger.error("사용 가능한 연도 조회 중 오류 발생", e);
            throw new DatabaseException("사용 가능한 연도를 조회하는 중 오류가 발생했습니다", e);
        }
    }

    /**
     * 전체 데이터 수를 조회합니다.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @param year 조회 연도 (선택적)
     * @return 전체 데이터 수
     * @throws DatabaseException 데이터베이스 조회 중 오류 발생 시
     */
    public long getTotalCount(String item, String year) {
        String sql = "SELECT COUNT(*) FROM (" + getBaseQuery(item) + ") p";
        try {
            if (year != null && !year.isEmpty()) {
                sql += " WHERE EXTRACT(YEAR FROM TO_DATE(p.\"DATE OF COUNT\", 'DD-Mon-YY'))::text = ?";
                return jdbcTemplate.queryForObject(sql, Long.class, year);
            } else {
                return jdbcTemplate.queryForObject(sql, Long.class);
            }
        } catch (DataAccessException e) {
            logger.error("전체 데이터 수 조회 중 오류 발생. 항목: {}, 연도: {}", item, year, e);
            throw new DatabaseException("전체 데이터 수를 조회하는 중 오류가 발생했습니다", e);
        }
    }


    /**
     * 페이징된 쿼리를 생성합니다.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @param year 조회 연도 (선택적)
     * @return 페이징된 SQL 쿼리
     */
    private String buildPagedQuery(String item, String year) {
        StringBuilder sql = new StringBuilder(getBaseQuery(item));
        if (year != null && !year.isEmpty()) {
            sql.insert(sql.lastIndexOf("ORDER BY"), " WHERE EXTRACT(YEAR FROM TO_DATE(p.\"DATE OF COUNT\", 'DD-Mon-YY'))::text = ? ");
        }
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        return sql.toString();
    }


    /**
     * 모든 통계 데이터를 스트리밍 방식으로 처리합니다.
     *
     * @param item     통계 항목 (METRO 또는 MCC,TM)
     * @param year     조회 연도 (선택적)
     * @param consumer 데이터 처리를 위한 Consumer 객체
     * @throws DatabaseException 데이터베이스 조회 중 오류 발생 시
     */
    public void streamAllStatisticsData(String item, String year, Consumer<Object> consumer) {
        String sql = getBaseQuery(item);
        if (year != null && !year.isEmpty()) {
            int orderByIndex = sql.lastIndexOf("ORDER BY");
            sql = sql.substring(0, orderByIndex) +
                    " WHERE EXTRACT(YEAR FROM TO_DATE(p.\"DATE OF COUNT\", 'DD-Mon-YY'))::text = ? " +
                    sql.substring(orderByIndex);

            try {
                jdbcTemplate.query(sql, new Object[]{year}, (ResultSet rs) -> {
                    Object dto = "METRO".equals(item) ? mapRowToMetroDTO(rs, 0) : mapRowToMccTmDTO(rs, 0);
                    consumer.accept(dto);
                });
            } catch (DataAccessException e) {
                logger.error("데이터 스트리밍 중 오류 발생. 항목: {}, 연도: {}", item, year, e);
                throw new DatabaseException("데이터를 스트리밍하는 중 오류가 발생했습니다", e);
            }
        } else {
            try {
                jdbcTemplate.query(sql, (ResultSet rs) -> {
                    Object dto = "METRO".equals(item) ? mapRowToMetroDTO(rs, 0) : mapRowToMccTmDTO(rs, 0);
                    consumer.accept(dto);
                });
            } catch (DataAccessException e) {
                logger.error("데이터 스트리밍 중 오류 발생. 항목: {}", item, e);
                throw new DatabaseException("데이터를 스트리밍하는 중 오류가 발생했습니다", e);
            }
        }
    }


    /**
     * 통계 데이터 요약을 조회합니다.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @param year 조회 연도 (선택적)
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 통계 데이터 요약 객체
     * @throws DatabaseException        데이터베이스 조회 중 오류 발생 시
     * @throws IllegalArgumentException 잘못된 항목 유형이 제공될 경우
     */
    public StatisticsSummaryDTO getStatisticsSummary(String item, String year, int page, int size) {
        String sql = buildPagedQuery(item, year);
        //logger.debug("생성된 SQL 쿼리: {}", sql);
        long totalCount = getTotalCount(item, year);

        List<?> content = new ArrayList<>();

        if ("METRO".equals(item) || "MCC,TM".equals(item)) {
            List<Object> params = new ArrayList<>();
            if (year != null && !year.isEmpty()) {
                params.add(year);
            }
            params.add((page - 1) * size);
            params.add(size);

            try {
                content = jdbcTemplate.query(
                        sql,
                        params.toArray(),
                        "METRO".equals(item) ? this::mapRowToMetroDTO : this::mapRowToMccTmDTO
                );
            } catch (DataAccessException e) {
                logger.error("통계 데이터 조회 중 오류 발생. 항목: {}, 연도: {}, 페이지: {}, 크기: {}", item, year, page, size, e);
                throw new DatabaseException("통계 데이터를 조회하는 중 오류가 발생했습니다", e);
            }
        } else {
            throw new IllegalArgumentException("잘못된 항목 유형: " + item);
        }

        //logger.debug("쿼리 결과 크기: {}", content.size());

        return new StatisticsSummaryDTO(
                totalCount,
                (int) Math.ceil((double) totalCount / size),
                page,
                content
        );
    }

    /**
     * ResultSet을 MccTmStatisticsDTO 객체로 매핑합니다.
     *
     * @param rs     ResultSet 객체
     * @param rowNum 행 번호
     * @return 매핑된 NbMccTmStatisticsDTO 객체
     * @throws SQLException SQL 예외 발생 시
     */
    private NbMccTmStatisticsDTO mapRowToMccTmDTO(ResultSet rs, int rowNum) throws SQLException {
        NbMccTmStatisticsDTO dto = new NbMccTmStatisticsDTO();
        dto.setRouteNo(rs.getString("ROUTE NO"));
        dto.setNameOfRoad(rs.getString("NAME OF ROAD"));
        dto.setProv(rs.getString("PROV"));
        dto.setCe(rs.getString("CE"));
        dto.setLat(rs.getDouble("Lat"));
        dto.setLon(rs.getDouble("Long"));
        dto.setTypeOfCount(rs.getString("TYPE OF COUNT"));
        dto.setDateOfCount(rs.getString("DATE OF COUNT"));
        dto.setTotalNoOfVeh(rs.getInt("TOTAL NO.OF VEH."));
        dto.setHrsOfCount(rs.getInt("HRS OF COUNT"));
        dto.setMclPercentage(rs.getDouble("MCL %"));
        dto.setTwlPercentage(rs.getDouble("TWL %"));
        dto.setCarPercentage(rs.getDouble("CAR %"));
        dto.setVanPercentage(rs.getDouble("VAN %"));
        dto.setMbuPercentage(rs.getDouble("MBU %"));
        dto.setLbuPercentage(rs.getDouble("LBU %"));
        dto.setLgvPercentage(rs.getDouble("LGV %"));
        dto.setMg1Percentage(rs.getDouble("MG1 %"));
        dto.setMg2Percentage(rs.getDouble("MG2 %"));
        dto.setHg3Percentage(rs.getDouble("HG3 %"));
        dto.setAg3Percentage(rs.getDouble("AG3 %"));
        dto.setAg4Percentage(rs.getDouble("AG4 %"));
        dto.setAg5Percentage(rs.getDouble("AG5 %"));
        dto.setAg6Percentage(rs.getDouble("AG6 %"));
        dto.setFvhPercentage(rs.getDouble("FVH %"));
        dto.setCumPercentageOfVeh(rs.getDouble("CUM % OF VEH"));
        dto.setMclCount(rs.getInt("MCL COUNT"));
        dto.setTwlCount(rs.getInt("TWL COUNT"));
        dto.setCarCount(rs.getInt("CAR COUNT"));
        dto.setVanCount(rs.getInt("VAN COUNT"));
        dto.setMbuCount(rs.getInt("MBU COUNT"));
        dto.setLbuCount(rs.getInt("LBU COUNT"));
        dto.setLgvCount(rs.getInt("LGV COUNT"));
        dto.setMg1Count(rs.getInt("MG1 COUNT"));
        dto.setMg2Count(rs.getInt("MG2 COUNT"));
        dto.setHg3Count(rs.getInt("HG3 COUNT"));
        dto.setAg3Count(rs.getInt("AG3 COUNT"));
        dto.setAg4Count(rs.getInt("AG4 COUNT"));
        dto.setAg5Count(rs.getInt("AG5 COUNT"));
        dto.setAg6Count(rs.getInt("AG6 COUNT"));
        dto.setFvhCount(rs.getInt("FVH COUNT"));
        dto.setTotal(rs.getInt("TOTAL"));
        return dto;
    }

    /**
     * ResultSet을 MetroStatisticsDTO 객체로 매핑합니다.
     *
     * @param rs     ResultSet 객체
     * @param rowNum 행 번호
     * @return 매핑된 NbMetroStatisticsDTO 객체
     * @throws SQLException SQL 예외 발생 시
     */
    private NbMetroStatisticsDTO mapRowToMetroDTO(ResultSet rs, int rowNum) throws SQLException {
        NbMetroStatisticsDTO dto = new NbMetroStatisticsDTO();
        dto.setRouteNo(rs.getString("ROUTE NO"));
        dto.setNameOfRoad(rs.getString("NAME OF ROAD"));
        dto.setProv(rs.getString("PROV"));
        dto.setCe(rs.getString("CE"));
        dto.setLat(rs.getDouble("Lat"));
        dto.setLon(rs.getDouble("Long"));
        dto.setTypeOfCount(rs.getString("TYPE OF COUNT"));
        dto.setDateOfCount(rs.getString("DATE OF COUNT"));
        dto.setTotalNoOfVeh(rs.getInt("TOTAL NO.OF VEH."));
        dto.setHrsOfCount(rs.getInt("HRS OF COUNT"));
        dto.setMclPercentage(rs.getDouble("MCL %"));
        dto.setTwlPercentage(rs.getDouble("TWL %"));
        dto.setCarPercentage(rs.getDouble("CAR %"));
        dto.setLgvPercentage(rs.getDouble("LGV %"));
        dto.setMpgvPercentage(rs.getDouble("MP&GV %"));
        dto.setLpgvPercentage(rs.getDouble("LP&GV %"));
        dto.setHg3Percentage(rs.getDouble("HG3 %"));
        dto.setAg3Percentage(rs.getDouble("AG3 %"));
        dto.setAg4Percentage(rs.getDouble("AG4 %"));
        dto.setAg5Percentage(rs.getDouble("AG5 %"));
        dto.setAg6Percentage(rs.getDouble("AG6 %"));
        dto.setCumPercentageOfVeh(rs.getDouble("CUM % OF VEH"));
        dto.setMclCount(rs.getInt("MCL COUNT"));
        dto.setTwlCount(rs.getInt("TWL COUNT"));
        dto.setCarCount(rs.getInt("CAR COUNT"));
        dto.setLgvCount(rs.getInt("LGV COUNT"));
        dto.setMpgvCount(rs.getInt("MP&GV COUNT"));
        dto.setLpgvCount(rs.getInt("LP&GV COUNT"));
        dto.setHg3Count(rs.getInt("HG3 COUNT"));
        dto.setAg3Count(rs.getInt("AG3 COUNT"));
        dto.setAg4Count(rs.getInt("AG4 COUNT"));
        dto.setAg5Count(rs.getInt("AG5 COUNT"));
        dto.setAg6Count(rs.getInt("AG6 COUNT"));
        dto.setTotal(rs.getInt("TOTAL"));
        return dto;
    }

    /**
     * 기본 SQL 쿼리를 생성합니다.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @return 기본 SQL 쿼리 문자열
     * @throws IllegalArgumentException 잘못된 항목 유형이 제공될 경우
     */
    private String getBaseQuery(String item) {
        if ("METRO".equals(item)) {
            return "WITH daily_stats AS (" +
                    "    SELECT " +
                    "        TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24') AS count_date," +
                    "        s.instllc_id," +
                    "        s.taz_cd," +
                    "        SUM(s.trfvlm) AS total_vehicles," +
                    "        COUNT(DISTINCT EXTRACT(HOUR FROM TO_TIMESTAMP(s.stats_yymmdt, 'YYYYMMDDHH24'))) AS hours_counted," +
                    "        s.vhcl_clsf," +
                    "        SUM(s.trfvlm) AS vehicle_count" +
                    "    FROM " +
                    "        srlk.ts_fixed_onhr s" +
                    "    GROUP BY " +
                    "        TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24'), s.instllc_id, s.taz_cd, s.vhcl_clsf" +
                    "    UNION ALL" +
                    "    SELECT " +
                    "        TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24') AS count_date," +
                    "        s.instllc_id," +
                    "        s.taz_cd," +
                    "        SUM(s.trfvlm) AS total_vehicles," +
                    "        COUNT(DISTINCT EXTRACT(HOUR FROM TO_TIMESTAMP(s.stats_yymmdt, 'YYYYMMDDHH24'))) AS hours_counted," +
                    "        s.vhcl_clsf," +
                    "        SUM(s.trfvlm) AS vehicle_count" +
                    "    FROM " +
                    "        srlk.TS_MVMNEQ_ONHR s" +
                    "    GROUP BY " +
                    "        TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24'), s.instllc_id, s.taz_cd, s.vhcl_clsf" +
                    ")," +
                    "percentages AS (" +
                    "    SELECT " +
                    "        count_date," +
                    "        instllc_id," +
                    "        taz_cd," +
                    "        'Metro' AS \"TYPE OF COUNT\"," +
                    "        TO_CHAR(count_date, 'DD-Mon-YY') AS \"DATE OF COUNT\"," +
                    "        SUM(total_vehicles) AS \"TOTAL NO.OF VEH.\"," +
                    "        MAX(hours_counted) AS \"HRS OF COUNT\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC011' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MCL %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC012' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"TWL %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC006' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"CAR %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC008' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"LGV %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC009' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MP&GV %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC010' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"LP&GV %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC007' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"HG3 %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC001' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG3 %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf in ('MVC002', 'MVC0013') THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG4 %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf IN ('MVC003', 'MVC004') THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG5 %\"," +
                    "        ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MVC005' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG6 %\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC011' THEN vehicle_count ELSE 0 END) AS \"MCL COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC012' THEN vehicle_count ELSE 0 END) AS \"TWL COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC006' THEN vehicle_count ELSE 0 END) AS \"CAR COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC008' THEN vehicle_count ELSE 0 END) AS \"LGV COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC009' THEN vehicle_count ELSE 0 END) AS \"MP&GV COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC010' THEN vehicle_count ELSE 0 END) AS \"LP&GV COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC007' THEN vehicle_count ELSE 0 END) AS \"HG3 COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC001' THEN vehicle_count ELSE 0 END) AS \"AG3 COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf in ('MVC002', 'MVC0013') THEN vehicle_count ELSE 0 END) AS \"AG4 COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf IN ('MVC003', 'MVC004') THEN vehicle_count ELSE 0 END) AS \"AG5 COUNT\"," +
                    "        SUM(CASE WHEN vhcl_clsf = 'MVC005' THEN vehicle_count ELSE 0 END) AS \"AG6 COUNT\"," +
                    "        SUM(total_vehicles) AS \"TOTAL\"" +
                    "    FROM " +
                    "        daily_stats" +
                    "    GROUP BY " +
                    "        count_date, instllc_id, taz_cd" +
                    ")," +
                    "road_equipment_match AS (" +
                    "    (SELECT DISTINCT ON (b.instllc_id) " +
                    "           a.road_cd, a.road_descr, b.instllc_id, b.instllc_nm, " +
                    "           b.lon, b.lat " +
                    "    FROM srlk.tc_road_mng a " +
                    "    CROSS JOIN srlk.tl_fixed_cur b " +
                    "    ORDER BY b.instllc_id, a.geom <-> ST_SetSRID(ST_MakePoint(b.lon, b.lat), 4326)) " +
                    "    UNION ALL " +
                    "    (SELECT DISTINCT ON (b.instllc_id) " +
                    "           a.road_cd, a.road_descr, b.instllc_id, b.instllc_nm, " +
                    "           b.lon, b.lat " +
                    "    FROM srlk.tc_road_mng a " +
                    "    CROSS JOIN srlk.TL_MVMNEQ_CUR b " +
                    "    ORDER BY b.instllc_id, a.geom <-> ST_SetSRID(ST_MakePoint(b.lon, b.lat), 4326)) " +
                    ")" +
                    "SELECT " +
                    "    rem.road_cd AS \"ROUTE NO\"," +
                    "    rem.road_descr AS \"NAME OF ROAD\"," +
                    "    COALESCE(dsd.PROVIN_NM, gn.PROVIN_NM) AS \"PROV\"," +
                    "    COALESCE(dsd.DISTRICT_NM, gn.DISTRICT_NM) AS \"CE\"," +
                    "    rem.lat AS \"Lat\"," +
                    "    rem.lon AS \"Long\"," +
                    "    p.\"TYPE OF COUNT\"," +
                    "    p.\"DATE OF COUNT\"," +
                    "    p.\"TOTAL NO.OF VEH.\"," +
                    "    p.\"HRS OF COUNT\"," +
                    "    p.\"MCL %\"," +
                    "    p.\"TWL %\"," +
                    "    p.\"CAR %\"," +
                    "    p.\"LGV %\"," +
                    "    p.\"MP&GV %\"," +
                    "    p.\"LP&GV %\"," +
                    "    p.\"HG3 %\"," +
                    "    p.\"AG3 %\"," +
                    "    p.\"AG4 %\"," +
                    "    p.\"AG5 %\"," +
                    "    p.\"AG6 %\"," +
                    "    ROUND((p.\"MCL %\" + p.\"TWL %\" + p.\"CAR %\" + p.\"LGV %\" + p.\"MP&GV %\" + p.\"LP&GV %\" + " +
                    "           p.\"HG3 %\" + p.\"AG3 %\" + p.\"AG4 %\" + p.\"AG5 %\" + p.\"AG6 %\") * 101 / 100, 2) AS \"CUM % OF VEH\"," +
                    "    p.\"MCL COUNT\"," +
                    "    p.\"TWL COUNT\"," +
                    "    p.\"CAR COUNT\"," +
                    "    p.\"LGV COUNT\"," +
                    "    p.\"MP&GV COUNT\"," +
                    "    p.\"LP&GV COUNT\"," +
                    "    p.\"HG3 COUNT\"," +
                    "    p.\"AG3 COUNT\"," +
                    "    p.\"AG4 COUNT\"," +
                    "    p.\"AG5 COUNT\"," +
                    "    p.\"AG6 COUNT\"," +
                    "    p.\"TOTAL\"" +
                    "FROM " +
                    "    percentages p " +
                    "    JOIN road_equipment_match rem ON p.instllc_id = rem.instllc_id " +
                    "    LEFT JOIN srlk.TC_DSDAR_MNG dm ON p.taz_cd = dm.DSTRCT_CD " +
                    "    LEFT JOIN srlk.TC_GNAR_MNG gm ON p.taz_cd = gm.DSTRCT_CD " +
                    "    LEFT JOIN srlk.TC_DSD_MNG dsd ON dm.DSD_ID = dsd.DSD_ID " +
                    "    LEFT JOIN srlk.TC_GN_MNG gn ON gm.GN_ID = gn.GN_ID " +
                    "ORDER BY " +
                    "    rem.road_cd, p.\"DATE OF COUNT\"";
        } else if ("MCC,TM".equals(item)) {
            return "WITH daily_stats AS ( " +
                    "SELECT " +
                    "TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24') AS count_date, " +
                    "s.EXMNMNG_ID, " +
                    "s.taz_cd, " +
                    "SUM(s.trfvlm) AS total_vehicles, " +
                    "COUNT(DISTINCT EXTRACT(HOUR FROM TO_TIMESTAMP(s.stats_yymmdt, 'YYYYMMDDHH24'))) AS hours_counted, " +
                    "s.vhcl_clsf, " +
                    "SUM(s.trfvlm) AS vehicle_count, " +
                    "'MCC' AS count_type " +
                    "FROM " +
                    "srlk.ts_mcc_trfvl_onhr s " +
                    "GROUP BY " +
                    "TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24'), s.EXMNMNG_ID, s.taz_cd, s.vhcl_clsf " +
                    "UNION ALL " +
                    "SELECT " +
                    "TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24') AS count_date, " +
                    "s.EXMNMNG_ID, " +
                    "s.taz_cd, " +
                    "SUM(s.trfvlm) AS total_vehicles, " +
                    "COUNT(DISTINCT EXTRACT(HOUR FROM TO_TIMESTAMP(s.stats_yymmdt, 'YYYYMMDDHH24'))) AS hours_counted, " +
                    "s.vhcl_clsf, " +
                    "SUM(s.trfvlm) AS vehicle_count, " +
                    "'TM' AS count_type " +
                    "FROM " +
                    "srlk.ts_tm_trfvl_onhr s " +
                    "GROUP BY " +
                    "TO_DATE(s.stats_yymmdt, 'YYYYMMDDHH24'), s.EXMNMNG_ID, s.taz_cd, s.vhcl_clsf " +
                    "), " +
                    "percentages AS ( " +
                    "SELECT " +
                    "count_date, " +
                    "EXMNMNG_ID, " +
                    "taz_cd, " +
                    "count_type AS \"TYPE OF COUNT\", " +
                    "TO_CHAR(count_date, 'DD-Mon-YY') AS \"DATE OF COUNT\", " +
                    "SUM(total_vehicles) AS \"TOTAL NO.OF VEH.\", " +
                    "MAX(hours_counted) AS \"HRS OF COUNT\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC011' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MCL %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC012' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"TWL %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC006' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"CAR %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC008' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"VAN %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC009' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MBU %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC010' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"LBU %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC007' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"LGV %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC001' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MG1 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC002' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"MG2 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC003' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"HG3 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC004' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG3 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC005' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG4 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC013' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG5 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC014' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"AG6 %\", " +
                    "ROUND(100.0 * SUM(CASE WHEN vhcl_clsf = 'MTC015' THEN vehicle_count ELSE 0 END) / NULLIF(SUM(vehicle_count), 0), 2) AS \"FVH %\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC011' THEN vehicle_count ELSE 0 END) AS \"MCL COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC012' THEN vehicle_count ELSE 0 END) AS \"TWL COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC006' THEN vehicle_count ELSE 0 END) AS \"CAR COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC008' THEN vehicle_count ELSE 0 END) AS \"VAN COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC009' THEN vehicle_count ELSE 0 END) AS \"MBU COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC010' THEN vehicle_count ELSE 0 END) AS \"LBU COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC007' THEN vehicle_count ELSE 0 END) AS \"LGV COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC001' THEN vehicle_count ELSE 0 END) AS \"MG1 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC002' THEN vehicle_count ELSE 0 END) AS \"MG2 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC003' THEN vehicle_count ELSE 0 END) AS \"HG3 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC004' THEN vehicle_count ELSE 0 END) AS \"AG3 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC005' THEN vehicle_count ELSE 0 END) AS \"AG4 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC013' THEN vehicle_count ELSE 0 END) AS \"AG5 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC014' THEN vehicle_count ELSE 0 END) AS \"AG6 COUNT\", " +
                    "SUM(CASE WHEN vhcl_clsf = 'MTC015' THEN vehicle_count ELSE 0 END) AS \"FVH COUNT\", " +
                    "SUM(total_vehicles) AS \"TOTAL\" " +
                    "FROM " +
                    "daily_stats " +
                    "GROUP BY " +
                    "count_date, EXMNMNG_ID, taz_cd, count_type " +
                    "), " +
                    "road_exam_match AS ( " +
                    "SELECT DISTINCT ON (er.EXMNMNG_ID) " +
                    "rm.road_cd, rm.road_descr, er.EXMNMNG_ID, er.EXMN_LAT, er.EXMN_LON " +
                    "FROM srlk.TL_EXMN_RSLT er " +
                    "CROSS JOIN LATERAL ( " +
                    "SELECT road_cd, road_descr, geom " +
                    "FROM srlk.tc_road_mng " +
                    "ORDER BY geom <-> ST_SetSRID(ST_MakePoint(er.EXMN_LON, er.EXMN_LAT), 4326) " +
                    "LIMIT 1 " +
                    ") rm " +
                    ") " +
                    "SELECT " +
                    "rem.road_cd AS \"ROUTE NO\", " +
                    "rem.road_descr AS \"NAME OF ROAD\", " +
                    "COALESCE(dsd.PROVIN_NM, gn.PROVIN_NM) AS \"PROV\", " +
                    "COALESCE(dsd.DISTRICT_NM, gn.DISTRICT_NM) AS \"CE\", " +
                    "rem.EXMN_LAT AS \"Lat\", " +
                    "rem.EXMN_LON as \"Long\", " +
                    "p.\"TYPE OF COUNT\", " +
                    "p.\"DATE OF COUNT\", " +
                    "p.\"TOTAL NO.OF VEH.\", " +
                    "p.\"HRS OF COUNT\", " +
                    "p.\"MCL %\", " +
                    "p.\"TWL %\", " +
                    "p.\"CAR %\", " +
                    "p.\"VAN %\", " +
                    "p.\"MBU %\", " +
                    "p.\"LBU %\", " +
                    "p.\"LGV %\", " +
                    "p.\"MG1 %\", " +
                    "p.\"MG2 %\", " +
                    "p.\"HG3 %\", " +
                    "p.\"AG3 %\", " +
                    "p.\"AG4 %\", " +
                    "p.\"AG5 %\", " +
                    "p.\"AG6 %\", " +
                    "p.\"FVH %\", " +
                    "ROUND((p.\"MCL %\" + p.\"TWL %\" + p.\"CAR %\" + p.\"VAN %\" + p.\"MBU %\" + p.\"LBU %\" + " +
                    "p.\"LGV %\" + p.\"MG1 %\" + p.\"MG2 %\" + p.\"HG3 %\" + p.\"AG3 %\" + p.\"AG4 %\" + p.\"AG5 %\" + p.\"AG6 %\" + p.\"FVH %\") * 101 / 100, 2) AS \"CUM % OF VEH\", " +
                    "p.\"MCL COUNT\", " +
                    "p.\"TWL COUNT\", " +
                    "p.\"CAR COUNT\", " +
                    "p.\"VAN COUNT\", " +
                    "p.\"MBU COUNT\", " +
                    "p.\"LBU COUNT\", " +
                    "p.\"LGV COUNT\", " +
                    "p.\"MG1 COUNT\", " +
                    "p.\"MG2 COUNT\", " +
                    "p.\"HG3 COUNT\", " +
                    "p.\"AG3 COUNT\", " +
                    "p.\"AG4 COUNT\", " +
                    "p.\"AG5 COUNT\", " +
                    "p.\"AG6 COUNT\", " +
                    "p.\"FVH COUNT\", " +
                    "p.\"TOTAL\" " +
                    "FROM " +
                    "percentages p " +
                    "JOIN road_exam_match rem ON p.EXMNMNG_ID = rem.EXMNMNG_ID " +
                    "LEFT JOIN srlk.TC_DSDAR_MNG dm ON p.taz_cd = dm.DSTRCT_CD " +
                    "LEFT JOIN srlk.TC_DSD_MNG dsd ON dm.DSD_ID = dsd.DSD_ID " +
                    "LEFT JOIN srlk.TC_GNAR_MNG gm ON p.taz_cd = gm.DSTRCT_CD " +
                    "LEFT JOIN srlk.TC_GN_MNG gn ON gm.GN_ID = gn.GN_ID " +
                    "ORDER BY " +
                    "rem.road_cd, p.\"DATE OF COUNT\"";
        } else {
            throw new IllegalArgumentException("잘못된 항목 유형: " + item);
        }
    }


    /**
     * 데이터베이스 관련 예외를 처리하기 위한 사용자 정의 예외 클래스
     */
    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
