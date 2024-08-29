package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsExcelDTO.TimeInfoSheet;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TsAxleloadYy;	

public interface TsAxleloadYyRepository extends JpaRepository<TsAxleloadYy, String>{

	@Query(value = 
			"SELECT " + 
				"TRFVLM_STATISTICS.cdnm_eng AS \"vhclClsf\", " +
				"TRFVLM_STATISTICS.CNT AS \"trfvlm\", " +
				"ROUND(TRFVLM_STATISTICS.cnt / SUM(TRFVLM_STATISTICS.cnt) OVER (PARTITION BY TRFVLM_STATISTICS.INSTLLC_ID) * 100, 2) AS \"rate\" " + 
			"FROM ( " +
				"SELECT " + 
					"TRFVL_LIST.CDNM_ENG, " +
					"TRFVL_LIST.INSTLLC_ID, " +
					"SUM(TRFVL_LIST.CNT) AS cnt " +
				"FROM( " +
					"SELECT " + 
						"TAY.CNT, " +
						"TAY.INSTLLC_ID, " +
						"TCI.CDNM_ENG " +
					"FROM srlk.TS_AXLELOAD_YY TAY " + 
					"LEFT JOIN srlk.TC_CD_INFO TCI ON TAY.VHCL_CLSF = TCI.CD AND TCI.USE_YN = 'Y' " +
					"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} " +
						"AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
				")TRFVL_LIST " +
				"GROUP BY TRFVL_LIST.cdnm_eng, TRFVL_LIST.instllc_id " +
				"ORDER BY cnt DESC " +
			") TRFVLM_STATISTICS",
			nativeQuery = true)
	List<Map<String, Object>> getTsTrfRsltStatistcsList(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"AX_NUM AS \"axleType\", " +
				"ROUND(CAST(COUNT(AX_NUM) AS NUMERIC) / ( " +
										"SELECT " +
											"COUNT(AX_NUM) " +
										"FROM srlk.TS_AXLELOAD_YY " +
										"WHERE STATS_YY = :#{#searchDTO.searchDate} AND INSTLLC_ID = :#{#searchDTO.siteId} " +
										") * 100 , 2) AS \"rate\" " +
			"FROM srlk.TS_AXLELOAD_YY " +
			"WHERE STATS_YY = :#{#searchDTO.searchDate} AND INSTLLC_ID = :#{#searchDTO.siteId} " +
			"GROUP BY AX_NUM " +
			"ORDER BY AX_NUM ", 
			nativeQuery = true)
	List<Map<String, Object>> getTsAxleRsltStatisticsList(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
	
	@Query(value = 
			"SELECT " +
				"TFC.INSTLLC_NM AS \"pointNm\", " +
				"TFC.LAT AS \"lat\", " +
				"TFC.LON AS \"lon\", " +
				"TIR.LANE_CNT AS \"laneCnt\" " +
			"FROM srlk.TS_AXLELOAD_YY TAY " +
			"INNER JOIN srlk.TL_FIXED_CUR TFC " +
				"ON TAY.INSTLLC_ID = TFC.INSTLLC_ID " +
			"INNER JOIN srlk.TC_CD_INFO TCI " +
				"ON TAY.VHCL_CLSF = TCI.CD " +
			"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
				"ON TAY.INSTLLC_ID = TIR.INSTLLC_ID " +
			"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} " +
				"AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
			"GROUP BY TFC.INSTLLC_NM, TFC.LAT, TFC.LON, TIR.LANE_CNT",
			nativeQuery = true)
	Map<String, Object> getAxleloadFixedRsltInfoForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
	
	@Query(value = 
			"SELECT " +
				"TMC.INSTLLC_NM AS \"pointNm\", " +
				"TMC.LAT AS \"lat\", " +
				"TMC.LON AS \"lon\", " +
				"TIR.LANE_CNT AS \"laneCnt\" " +
			"FROM srlk.TS_AXLELOAD_YY TAY " +
			"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
				"ON TAY.INSTLLC_ID = TMC.INSTLLC_ID " +
			"INNER JOIN srlk.TC_CD_INFO TCI " +
				"ON TAY.VHCL_CLSF = TCI.CD " +
			"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
				"ON TAY.INSTLLC_ID = TIR.INSTLLC_ID " +
			"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} " +
				"AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
			"GROUP BY TMC.INSTLLC_NM, TMC.LAT, TMC.LON, TIR.LANE_CNT",
				nativeQuery = true)
	Map<String, Object> getAxleloadMovedRsltInfoForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"WITH BY_VEHICLE_AXLELOAD_STATS_LIST AS( " +
				"SELECT " +
					"VEHICLE_DATA.CDNM_ENG, " +
					"COALESCE(DB_DATA.cnt, 0) " +
				"FROM( " +
					"SELECT TCI.CDNM_ENG from srlk.TC_CD_INFO TCI where TCI.GRPCD_ID = :#{#searchDTO.grpCdId} " +
				") VEHICLE_DATA " +
				"LEFT JOIN( " +
					"SELECT " +
						"TCI.CDNM_ENG, " +
						"SUM(TAY.CNT) AS \"cnt\" " +
					"FROM srlk.TS_AXLELOAD_YY TAY " +
					"INNER JOIN srlk.TL_FIXED_CUR TFC " +
						"ON TAY.INSTLLC_ID = TFC.INSTLLC_ID " +
					"INNER JOIN srlk.TC_CD_INFO TCI " +
						"ON TAY.VHCL_CLSF = TCI.CD " +
					"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
						"ON TAY.INSTLLC_ID = TIR.INSTLLC_ID " +
					"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} " +
						"AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
					"GROUP BY TCI.CDNM_ENG" +
				")DB_DATA on VEHICLE_DATA.CDNM_ENG = DB_DATA.CDNM_ENG " +
			") " +
			"SELECT  " +
				"ARRAY_TO_STRING(ARRAY_AGG(CDNM_ENG), ',') AS \"headerArr\", " +
				"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
			"FROM BY_VEHICLE_AXLELOAD_STATS_LIST ",
			nativeQuery = true)
	Map<String, Object> getAxleloadFixedRsltListForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
	
	@Query(value = 
			"WITH BY_VEHICLE_AXLELOAD_STATS_LIST AS( " +
					"SELECT " +
					"VEHICLE_DATA.CDNM_ENG, " +
					"COALESCE(DB_DATA.cnt, 0) " +
					"FROM( " +
						"SELECT TCI.CDNM_ENG from srlk.TC_CD_INFO TCI where TCI.GRPCD_ID = :#{#searchDTO.grpCdId} " +
					") VEHICLE_DATA " +
					"LEFT JOIN( " +
						"SELECT " +
							"TCI.CDNM_ENG, " +
							"SUM(TAY.CNT) AS \"cnt\" " +
						"FROM srlk.TS_AXLELOAD_YY TAY " +
						"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
							"ON TAY.INSTLLC_ID = TMC.INSTLLC_ID " +
						"INNER JOIN srlk.TC_CD_INFO TCI " +
							"ON TAY.VHCL_CLSF = TCI.CD " +
						"INNER JOIN srlk.TM_INSTLLC_ROAD TIR " +
							"ON TAY.INSTLLC_ID = TIR.INSTLLC_ID " +
						"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} " +
							"AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
						"GROUP BY TCI.CDNM_ENG" +
					")DB_DATA on VEHICLE_DATA.CDNM_ENG = DB_DATA.CDNM_ENG " +
				") " +
				"SELECT  " +
					"ARRAY_TO_STRING(ARRAY_AGG(CDNM_ENG), ',') AS \"headerArr\", " +
					"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
				"FROM BY_VEHICLE_AXLELOAD_STATS_LIST ",
				nativeQuery = true)
	Map<String, Object> getAxleloadMovedRsltListForVehicleExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"TFC.INSTLLC_NM AS \"pointNm\", " +
				"DATE_PART('year', TAY.REGIST_DT) AS \"statsYy\", " +
				"DATE_PART('month', TAY.REGIST_DT) AS \"statsMm\", " +
				"DATE_PART('day', TAY.REGIST_DT) AS \"statsDd\", " +
				"CASE WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
				"ELSE '-' " +
				"END AS \"dayOfWeek\" " +
			"FROM srlk.TS_AXLELOAD_YY TAY " +
			"INNER JOIN srlk.TL_FIXED_CUR TFC " +
				"ON TAY.INSTLLC_ID = TFC.INSTLLC_ID " +
			"INNER JOIN srlk.TC_CD_INFO TCI " +
				"ON TAY.VHCL_CLSF = TCI.CD " +
			"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
			"GROUP BY TFC.INSTLLC_NM,DATE_PART('year', TAY.REGIST_DT),DATE_PART('month', TAY.REGIST_DT),DATE_PART('day', TAY.REGIST_DT), EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE))",
			nativeQuery = true)
	List<Map<String, Object>> getAxleloadFixedRsltInfoForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);
	
	@Query(value = 
			"SELECT " +
				"TMC.INSTLLC_NM AS \"pointNm\", " +
				"DATE_PART('year', TAY.REGIST_DT) AS \"statsYy\", " +
				"DATE_PART('month', TAY.REGIST_DT) AS \"statsMm\", " +
				"DATE_PART('day', TAY.REGIST_DT) AS \"statsDd\", " +
				"CASE WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
					"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
					"ELSE '-' " +
				"END AS \"dayOfWeek\" " +
			"FROM srlk.TS_AXLELOAD_YY TAY " +
			"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
				"ON TAY.INSTLLC_ID = TMC.INSTLLC_ID " +
			"INNER JOIN srlk.TC_CD_INFO TCI " +
				"ON TAY.VHCL_CLSF = TCI.CD " +
			"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
			"GROUP BY TMC.INSTLLC_NM,DATE_PART('year', TAY.REGIST_DT),DATE_PART('month', TAY.REGIST_DT),DATE_PART('day', TAY.REGIST_DT), EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE))",
				nativeQuery = true)
	List<Map<String, Object>> getAxleloadMovedRsltInfoForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value = 
			"WITH BY_TIME_AXLELOAD_STATS_LIST AS( " +
					"SELECT " +
						"TIME_DATA.GRAPH_TIME, " +
						"COALESCE(DB_DATA.TRFVLM, 0) " +
					"FROM ( " +
						"SELECT GENERATE_SERIES(1, 24) as GRAPH_TIME " +
					")TIME_DATA " +
					"LEFT JOIN ( " +
							"SELECT " +
								"TFC.INSTLLC_NM, " +
								"DATE_PART('year', TAY.REGIST_DT) as year, " +
								"DATE_PART('month', TAY.REGIST_DT) as month, " +
								"DATE_PART('day', TAY.REGIST_DT) as day, " +
								"CASE WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
									"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
									"ELSE '-' " +
								"END AS DAY_OF_WEEK, " +
								"DATE_PART('hour', TAY.REGIST_DT) AS START_HOUR, " +
								"SUM(TAY.CNT) AS TRFVLM " +
							"FROM srlk.TS_AXLELOAD_YY TAY " +
							"INNER JOIN srlk.TL_FIXED_CUR TFC " +
								"ON TAY.INSTLLC_ID = TFC.INSTLLC_ID " +
							"INNER JOIN srlk.TC_CD_INFO TCI " +
								"ON TAY.VHCL_CLSF = TCI.CD " +
							"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
								"AND TFC.INSTLLC_NM = :#{#item.pointNm} AND DATE_PART('year', TAY.REGIST_DT) = :#{#item.statsYy} AND DATE_PART('month', TAY.REGIST_DT) = :#{#item.statsMm} "  +
								"AND DATE_PART('day', TAY.REGIST_DT) = :#{#item.statsDd} " +
							"GROUP BY TFC.INSTLLC_NM,DATE_PART('year', TAY.REGIST_DT),DATE_PART('month', TAY.REGIST_DT),DATE_PART('day', TAY.REGIST_DT), " +
							"DAY_OF_WEEK, DATE_PART('hour', TAY.REGIST_DT) " +
					")DB_DATA on TIME_DATA.GRAPH_TIME = DB_DATA.START_HOUR " +
					"ORDER BY TIME_DATA.GRAPH_TIME ASC " +
				") " +
				"SELECT  " +
					"ARRAY_TO_STRING(ARRAY_AGG(graph_time), ',') AS \"headerArr\", " +
					"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
				"FROM BY_TIME_AXLELOAD_STATS_LIST ",
			nativeQuery = true)
	Map<String, Object> getAxleloadFixedRsltListForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO, @Param("item") TimeInfoSheet item);
	
	@Query(value = 
			"WITH BY_TIME_AXLELOAD_STATS_LIST AS( " +
				"SELECT " +
				"TIME_DATA.GRAPH_TIME, " +
				"COALESCE(DB_DATA.TRFVLM, 0) " +
				"FROM ( " +
					"SELECT GENERATE_SERIES(1, 24) as GRAPH_TIME " +
				")TIME_DATA " +
				"LEFT JOIN ( " +
					"SELECT " +
						"TMC.INSTLLC_NM, " +
						"DATE_PART('year', TAY.REGIST_DT) as year, " +
						"DATE_PART('month', TAY.REGIST_DT) as month, " +
						"DATE_PART('day', TAY.REGIST_DT) as day, " +
						"CASE WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 1 THEN 'Sun' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 2 THEN 'Mon' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 3 THEN 'Tue' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 4 THEN 'Wed' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 5 THEN 'Thu' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 6 THEN 'Fri' " +
							"WHEN EXTRACT(ISODOW FROM CAST(TAY.REGIST_DT AS DATE)) = 7 THEN 'Sat' " +
							"ELSE '-' " +
						"END AS DAY_OF_WEEK, " +
						"DATE_PART('hour', TAY.REGIST_DT) AS START_HOUR, " +
						"SUM(TAY.CNT) AS TRFVLM " +
					"FROM srlk.TS_AXLELOAD_YY TAY " +
					"INNER JOIN srlk.TL_MVMNEQ_CUR TMC " +
						"ON TAY.INSTLLC_ID = TMC.INSTLLC_ID " +
					"INNER JOIN srlk.TC_CD_INFO TCI " +
						"ON TAY.VHCL_CLSF = TCI.CD " +
					"WHERE TAY.STATS_YY = :#{#searchDTO.searchDate} AND TAY.INSTLLC_ID = :#{#searchDTO.siteId} " +
						"AND TMC.INSTLLC_NM = :#{#item.pointNm} AND DATE_PART('year', TAY.REGIST_DT) = :#{#item.statsYy} AND DATE_PART('month', TAY.REGIST_DT) = :#{#item.statsMm} "  +
						"AND DATE_PART('day', TAY.REGIST_DT) = :#{#item.statsDd} " +
						"GROUP BY TMC.INSTLLC_NM,DATE_PART('year', TAY.REGIST_DT),DATE_PART('month', TAY.REGIST_DT),DATE_PART('day', TAY.REGIST_DT), " +
						"DAY_OF_WEEK, DATE_PART('hour', TAY.REGIST_DT) " +
					")DB_DATA on TIME_DATA.GRAPH_TIME = DB_DATA.START_HOUR " +
					"ORDER BY TIME_DATA.GRAPH_TIME ASC " +
				") " +
				"SELECT  " +
				"ARRAY_TO_STRING(ARRAY_AGG(graph_time), ',') AS \"headerArr\", " +
				"ARRAY_TO_STRING(ARRAY_AGG(coalesce), ',') AS \"trfvlmArr\" " +
				"FROM BY_TIME_AXLELOAD_STATS_LIST ",
				nativeQuery = true)
	Map<String, Object> getAxleloadMovedRsltListForTimeExcelDownload(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO, @Param("item") TimeInfoSheet item);
}
