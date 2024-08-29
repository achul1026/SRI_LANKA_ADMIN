package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsExcelDTO.TimeInfoSheet;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TsMvmneqYy;

public interface TsMvmneqYyRepository extends JpaRepository<TsMvmneqYy, String>{

	@Query(value =
			"SELECT "+
					"TO_CHAR(REGIST_DT,'yyyy') AS \"searchYear\" "+
			"FROM "+
			"srlk.TS_MVMNEQ_YY TMY "+
			"GROUP BY TO_CHAR(REGIST_DT,'yyyy') "+
			"ORDER BY TO_CHAR(REGIST_DT,'yyyy') DESC "
			,
			nativeQuery = true)
	List<String> getStatsYears();

	@Query(value = 
			"SELECT " + 
				"TRFVLM_STATISTICS.cdnm_eng AS \"vhclClsf\", " +
				"TRFVLM_STATISTICS.trfvlm AS \"trfvlm\", " +
				"ROUND(TRFVLM_STATISTICS.trfvlm / SUM(TRFVLM_STATISTICS.trfvlm) OVER (PARTITION BY TRFVLM_STATISTICS.instllc_id) * 100, 2) AS \"rate\" " + 
			"FROM ( " +
				"SELECT " + 
					"TRFVL_LIST.cdnm_eng, " +
					"TRFVL_LIST.instllc_id, " +
					"SUM(TRFVL_LIST.trfvlm) AS TRFVLM " +
				"FROM( " +
					"SELECT " + 
						"TMY.trfvlm, " +
						"TMY.instllc_id, " +
						"TCI.cdnm_eng " +
					"FROM srlk.TS_MVMNEQ_YY TMY " + 
					"LEFT JOIN srlk.TC_CD_INFO TCI ON TMY.VHCL_CLSF = TCI.CD AND TCI.USE_YN = 'Y' " +
					"WHERE TMY.stats_yy = :#{#searchDTO.searchDate} " +
						"AND TMY.instllc_id = :#{#searchDTO.siteId} " +
				")TRFVL_LIST " +
				"GROUP BY TRFVL_LIST.cdnm_eng, TRFVL_LIST.instllc_id " +
				"ORDER BY TRFVLM DESC " +
			") TRFVLM_STATISTICS",
			nativeQuery = true)
	List<Map<String, Object>> getTsTrfRsltStatistcsList(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"TRFVLM_STATISTICS.cdnm_eng AS \"vhclClsf\", " +
				"TRFVLM_STATISTICS.avgSpeed " +
			"FROM (" +
				"SELECT " +
					"TRFVL_LIST.cdnm_eng," +
					"TRFVL_LIST.instllc_id, " +
					"SUM(TRFVL_LIST.avg_speed) as avgSpeed " +
				"FROM(" +
					"SELECT " + 
						"TMY.avg_speed," +
						"TMY.instllc_id, " +
						"TCI.cdnm_eng " +
					"FROM srlk.TS_MVMNEQ_YY TMY " + 
					"LEFT JOIN srlk.TC_CD_INFO TCI ON TMY.VHCL_CLSF = TCI.CD AND TCI.USE_YN = 'Y' " +
					"WHERE TMY.stats_yy = :#{#searchDTO.searchDate} " +
						"AND TMY.instllc_id = :#{#searchDTO.siteId} " +
				")TRFVL_LIST " +
				"GROUP BY TRFVL_LIST.cdnm_eng, TRFVL_LIST.instllc_id " +
				"ORDER BY SUM(TRFVL_LIST.avg_speed) DESC " +
			") TRFVLM_STATISTICS",
			nativeQuery = true)
	List<Map<String, Object>> getTsTrfSpdRsltStatisticsList(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value =
			"SELECT " +
				"DISTINCT TMY.STATS_YY AS \"value\" " +
			"FROM srlk.TS_MVMNEQ_YY TMY " +
				"WHERE TMY.instllc_id = :#{#searchDTO.siteId}",
			nativeQuery = true)
	List<Map<String, Object>> getTsMvmneqYyListBySearchDTO(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"TMC.INSTLLC_NM AS \"pointNm\", " +
				"TMC.LAT AS \"lat\", " +
				"TMC.LON AS \"lon\", " +
				"TIR.LANE_CNT AS \"laneCnt\" " +
			"FROM srlk.TS_MVMNEQ_YY TMY " +
			"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
				"ON TMY.INSTLLC_ID = TMC.INSTLLC_ID " +
			"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
				"ON TMC.INSTLLC_ID = TIR.INSTLLC_ID " +
			"WHERE TMY.STATS_YY = :#{#searchDTO.searchDate} AND TMY.INSTLLC_ID = :#{#searchDTO.siteId} " +
				"AND TIR.EQPMNT_CLSF = 'FCT003' " +
			"GROUP BY TMC.INSTLLC_NM, TMC.LAT, TMC.LON, TIR.LANE_CNT",
			nativeQuery = true)
	Map<String, Object> getTrafficRsltInfoForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"TMC.INSTLLC_NM AS \"pointNm\", " +
				"DATE_PART('year', TMY.REGIST_DT) AS \"statsYy\", " +
				"DATE_PART('month', TMY.REGIST_DT) AS \"statsMm\", " +
				"DATE_PART('day', TMY.REGIST_DT) AS \"statsDd\", " +
				"CASE WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
					"ELSE '-' " +
				"END AS \"dayOfWeek\" " +
			"FROM srlk.TS_MVMNEQ_YY TMY " +
			"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
				"ON TMY.INSTLLC_ID = TMC.INSTLLC_ID " +
			"INNER JOIN srlk.TC_CD_INFO TCI " +
				"ON TMY.VHCL_CLSF = TCI.CD " +
			"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
				"ON TMC.INSTLLC_ID = TIR.INSTLLC_ID " +
			"WHERE TMY.STATS_YY = :#{#searchDTO.searchDate} AND TMY.INSTLLC_ID = :#{#searchDTO.siteId} " +
				"AND TIR.EQPMNT_CLSF = 'FCT003' " +
			"GROUP BY TMC.INSTLLC_NM,DATE_PART('year', TMY.REGIST_DT),DATE_PART('month', TMY.REGIST_DT), " +
				"DATE_PART('day', TMY.REGIST_DT), EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE))",
			nativeQuery = true)
	List<Map<String, Object>> getTrafficRsltInfoForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
	
	@Query(value = 
			"WITH BY_VEHICLE_TRAFFIC_STATS_LIST AS( " +
				"SELECT " +
					"VEHICLE_DATA.CDNM_ENG, " +
					"COALESCE(DB_DATA.TRFVLM, 0) " +
				"FROM( " +
					"SELECT TCI.CDNM_ENG from srlk.TC_CD_INFO TCI WHERE TCI.GRPCD_ID = :#{#searchDTO.grpCdId} " +
				") VEHICLE_DATA " +
				"LEFT JOIN( " +
					"SELECT " +
						"TCI.CDNM_ENG, " +
						"SUM(TMY.TRFVLM) AS \"TRFVLM\" " +
					"FROM srlk.TS_MVMNEQ_YY TMY " +
					"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
						"ON TMY.INSTLLC_ID = TMC.INSTLLC_ID " +
					"INNER JOIN srlk.TC_CD_INFO TCI " +
						"ON TMY.VHCL_CLSF = TCI.CD " +
					"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
						"ON TMC.INSTLLC_ID = TIR.INSTLLC_ID " +
					"WHERE TMY.STATS_YY = :#{#searchDTO.searchDate} " +
						"AND TMY.INSTLLC_ID = :#{#searchDTO.siteId} " +
						"AND TIR.EQPMNT_CLSF = 'FCT003' " +
					"GROUP BY TCI.CDNM_ENG " +
				")DB_DATA on VEHICLE_DATA.CDNM_ENG = DB_DATA.CDNM_ENG " +
			") " +
			"SELECT " +
				"ARRAY_TO_STRING(ARRAY_AGG(CDNM_ENG), ',') AS \"headerArr\", " +
				"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
			"FROM BY_VEHICLE_TRAFFIC_STATS_LIST",
			nativeQuery = true)
	Map<String, Object> getTrafficRsltListForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
		
	@Query(value = 
			"WITH BY_TIME_TRAFFIC_STATS_LIST AS( " +
				"SELECT  " +
					"COALESCE(DB_DATA.TRFVLM, 0) " +
				"FROM ( " +
					"SELECT GENERATE_SERIES(1, 24) as GRAPH_TIME " +
				")TIME_DATA " +
				"LEFT JOIN ( " +
					"SELECT " +
						"TMC.INSTLLC_NM, " +
						"DATE_PART('year', TMY.REGIST_DT) AS year, " +
						"DATE_PART('month', TMY.REGIST_DT) AS month, " +
						"DATE_PART('day', TMY.REGIST_DT) AS day, " +
						"DATE_PART('hour', TMY.REGIST_DT) AS dbHour, " +
						"CASE WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TMY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
							"ELSE '-' " +
						"END AS DAY_OF_WEEK, " +
						"DATE_PART('hour', TMY.REGIST_DT) AS START_HOUR, " +
						"SUM(tmy.TRFVLM) AS TRFVLM " +
					"FROM srlk.TS_MVMNEQ_YY TMY  " +
					"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
						"ON TMY.INSTLLC_ID = TMC.INSTLLC_ID " +
					"INNER JOIN srlk.TC_CD_INFO TCI  " +
						"ON TMY.VHCL_CLSF = TCI.CD  " +
					"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
						"ON TMC.INSTLLC_ID = TIR.INSTLLC_ID  " +
					"WHERE TMY.STATS_YY = :#{#searchDTO.searchDate}  " +
						"AND TMY.INSTLLC_ID = :#{#searchDTO.siteId} " +
						"AND DATE_PART('day', TMY.REGIST_DT) = :#{#item.statsDd} " +
						"AND TIR.EQPMNT_CLSF = 'FCT003' " +
					"GROUP BY TMC.INSTLLC_NM,DATE_PART('year', TMY.REGIST_DT),DATE_PART('month', TMY.REGIST_DT), " +
						"DATE_PART('day', tmy.REGIST_DT), DAY_OF_WEEK,DATE_PART('hour', tmy.REGIST_DT) " +
				")DB_DATA ON TIME_DATA.GRAPH_TIME = DB_DATA.START_HOUR " +
				"ORDER BY TIME_DATA.GRAPH_TIME ASC " +
			") " +
			"SELECT  " +
				"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
			"FROM BY_TIME_TRAFFIC_STATS_LIST",
			nativeQuery = true)
	Map<String, Object> getTrafficRsltListForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO, @Param("item") TimeInfoSheet item);

}