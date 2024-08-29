package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchMetroCountDetailDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqCur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TlMvmneqCurRepository extends JpaRepository<TlMvmneqCur, String>{
	
	@Query(value =
			"select "+
					"TMC.INSTLLC_ID as instllcId, "+
					"TMC.INSTLLC_NM as instllcNm, "+
					//"TMC.INSTLLC_DESCR as instllcDescr, "+
					"TMC.LAT as lat, "+
					"TMC.LON as lon, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location , "+
					"case when max(TMPT.PASS_DT) is null or max(TMPT.PASS_DT) < now()  - interval '10 minutes' then 'metro-move-danger-icon' else 'metro-move-icon' end as icon, "+
					"case when max(TMPT.PASS_DT) is null or max(TMPT.PASS_DT) < now()  - interval '10 minutes' then 'error' else 'nomal' end as status "+
			"from "+
			"srlk.TL_MVMNEQ_CUR TMC "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TMC.LON , TMC.LAT), 4326)) "+
			"left join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"left join srlk.TL_MVMNEQ_PASS TMPT  on tmc.INSTLLC_ID  = TMPT.INSTLLC_ID and TMPT.PASS_DT >= CURRENT_DATE "+
			"where (:#{#fcilitiesGISSearchDTO.searchDsdCd} IS NULL OR tss.DSTRCT_CD like cast(:#{#fcilitiesGISSearchDTO.searchDsdCd} AS TEXT) || '%') "+
			"group by tmc.INSTLLC_ID,TDM.DSD_ID "
			,
			nativeQuery = true)
	List<Map<String,Object>> getFacilitiesMoveMetroCountList(@Param("fcilitiesGISSearchDTO") FacilitiesGISSearchDTO fcilitiesGISSearchDTO);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
					"RESULT_DATA.VHCL_CLSF as vhclClsf, "+
					"RESULT_DATA.TRFVLM  as trfvlm, "+
					"ROUND(RESULT_DATA.TRFVLM / SUM(RESULT_DATA.trfvlm) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100)  as rate "+
			"from ( "+
					"select "+
						"TMC.INSTLLC_ID, "+
						"COUNT(TMP.VHCL_CLSF) AS TRFVLM, "+
						"TMP.VHCL_CLSF "+
					"from "+
					"srlk.TL_MVMNEQ_CUR TMC "+
					"inner join srlk.TL_MVMNEQ_PASS TMP on TMC.INSTLLC_ID = TMP.INSTLLC_ID "+
					"where "+
					"TMC.INSTLLC_ID = :instllcId "+
					"and TMP.PASS_DT >= CURRENT_DATE "+
					"group by TMC.INSTLLC_ID,TMP.VHCL_CLSF "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM DESC "
			,
			nativeQuery = true)
	List<Map<String,Object>> getFacilitiesMoveMetroCountDetail(@Param("instllcId") String instllcId);

	@Query(value =
            "SELECT " +
            	"TMC.INSTLLC_ID AS \"value\", "+
            	"TMC.INSTLLC_NM AS \"name\" "+
            "FROM srlk.TL_MVMNEQ_CUR TMC "	+
            "WHERE TMC.EQPMNT_ID = :#{#searchDTO.facltId}"
            ,
            nativeQuery = true)
	List<Map<String, Object>> getTlMvmneqCurListBySearchDTO(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

	@Query(value =
			"select "+
					"TMC.INSTLLC_ID as instllcId, "+
					"TMC.INSTLLC_NM as instllcNm, "+
					"TMC.LAT as lat, "+
					"TMC.LON as lon, "+
					"COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
					"sum(TMO.TRFVLM) as totalCnt, "+
					"ROUND(avg(TMO.AVG_SPEED),0) as totalAvg "+
			"from "+
					"srlk.TL_MVMNEQ_CUR TMC "+
			"inner join srlk.TM_INSTLLC_ROAD TIR on TMC.INSTLLC_ID = tir.INSTLLC_ID "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TMC.LON , TMC.LAT), 4326)) "+
			"inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"inner join srlk.TS_MVMNEQ_ONHR TMO  on TMC.INSTLLC_ID = TMO.INSTLLC_ID "+
			"where  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDTO.searchEndDt},8) "+
			"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDTO.searchEndDt},2) "+
			"and tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
			"and TIR.eqpmnt_clsf = 'FCT003' "+
			"group by TMC.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountListForTime(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO);

	@Query(value =
			"select "+
					"result_time_data.time_list as dateInfo, "+
					"COALESCE(sum(TMO.TRFVLM),0) as trfvlm, "+
					"COALESCE(round(avg(TMO.avg_speed),0),0) as avgSpeed "+
			"from ( "+
					"with search_date as( "+
							"select GENERATE_SERIES( "+
							"cast(RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) as numeric),cast(RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) as numeric) "+
					"	) as  time_list "+
					") "+
					"select "+
							"time_list, "+
							"case when time_list < 10 then '0' || cast(time_list as varchar) else cast(time_list as varchar) end as db_time "+
					"from "+
							"search_date "+
					")result_time_data "+
			"left join srlk.TS_MVMNEQ_ONHR TMO on result_time_data.db_time = RIGHT(TMO.STATS_YYMMDT,2) " +
			"and TMO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"and  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
			"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
			"group by result_time_data.time_list "+
			"order by result_time_data.time_list "
		,
		nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountLineGraphForTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
					"TCI.CD as vhclDrctCd, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
					"from "+
			"srlk.TS_MVMNEQ_ONHR TMO "+
			"left join srlk.TC_CD_INFO TCI on TMO.VHCL_DRCT = TCI.CD and TCI.GRPCD_ID = :grpcdId "+
			"where  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
			"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
			"group by TCI.CD,TCI.CDNM_ENG,TCI.CDNM_KOR,TCI.CDNM_SIN "+
			"order by TCI.CD asc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getVhclDrctForTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,@Param("grpcdId") String grpcdId, @Param("lang") String lang);


	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
					"RESULT_DATA.VHCL_CLSF as vhclClsf, "+
					"RESULT_DATA.TRFVLM as trfvlm, "+
					"ROUND(RESULT_DATA.trfvlm / SUM(RESULT_DATA.TRFVLM) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100, 0) as rate "+
			"from ( "+
					"select "+
							"TMO.INSTLLC_ID, "+
							"SUM(TMO.TRFVLM) as TRFVLM, "+
							"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
					"from srlk.TS_MVMNEQ_ONHR TMO "+
					"left join srlk.TC_CD_INFO TCI on TMO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
					"where  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
					"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
					"and TMO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TMO.INSTLLC_ID,TMO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM desc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountTableForTrfvlmTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
					"select coalesce("+
							"(select "+
										 "sum(TMO.trfvlm) "+
								"from srlk.TS_MVMNEQ_ONHR TMO "+
								"where  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
								"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
								"and TMO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
								"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
								"group by TMO.INSTLLC_ID )"+
							", 0 )"
			,
			nativeQuery = true
	)
	long getTotalCntForTrfvlmTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);
	@Query(value =
					"select coalesce("+
							"(select "+
										 "AVG(TMO.AVG_SPEED) "+
								"from srlk.TS_MVMNEQ_ONHR TMO "+
								"where  LEFT(TMO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TMO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
								"and  RIGHT(TMO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TMO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
								"and TMO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
								"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
								"group by TMO.INSTLLC_ID )"+
							", 0 )"
			,
			nativeQuery = true
	)
	long getTotalAvgForAvgSpeedTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by avg(TMO.AVG_SPEED) DESC) ) as rnum, "+
					"COALESCE(ROUND(avg(TMO.AVG_SPEED),0),0)  as avgSpeed, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclClsf "+
			"from srlk.TS_MVMNEQ_ONHR TMO "+
			"left join srlk.TC_CD_INFO TCI on TMO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
			"where TMO.STATS_YYMMDT >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TMO.STATS_YYMMDT <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"and TMO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by TMO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			"order by avg(TMO.AVG_SPEED) DESC "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountTableForAvgSpeedTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"TMC.INSTLLC_ID as instllcId, "+
					"TMC.INSTLLC_NM as instllcNm, "+
					"TMC.LAT as lat, "+
					"TMC.LON as lon, "+
					"COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
					"sum(TMM.TRFVLM) as totalCnt, "+
					"ROUND(avg(TMM.AVG_SPEED),0) as totalAvg "+
			"from "+
					"srlk.TL_MVMNEQ_CUR TMC "+
			"inner join srlk.TM_INSTLLC_ROAD TIR on TMC.INSTLLC_ID = tir.INSTLLC_ID "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TMC.LON , TMC.LAT), 4326)) "+
			"inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"inner join srlk.TS_MVMNEQ_MM TMM on TMC.INSTLLC_ID = TMM.INSTLLC_ID "+
			"where TMM.STATS_YYMM >= :#{#statisticsGISSearchDTO.searchStartDt} and TMM.STATS_YYMM <= :#{#statisticsGISSearchDTO.searchEndDt} "+
			"and tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
			"and TIR.eqpmnt_clsf = 'FCT003' "+
			"group by TMC.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountListForMonth(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO);


	@Query(value =
			"select "+
					"result_month_data.month_list as dateInfo, "+
					"COALESCE(sum(TMM.TRFVLM),0) as trfvlm, "+
					"COALESCE(round(avg(TMM.avg_speed),0),0) as avgSpeed "+
			"from ( "+
					"with search_date as( "+
							"select GENERATE_SERIES( "+
							"cast(right(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) as numeric),cast(right(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) as numeric) "+
						") as  month_list "+
					") "+
					"select "+
							"month_list, "+
							"case when month_list < 10 then '0' || cast(month_list as varchar) else cast(month_list as varchar) end as db_month "+
					"from "+
							"search_date "+
			")result_month_data "+
			"left join srlk.TS_MVMNEQ_MM TMM on result_month_data.db_month = RIGHT(TMM.STATS_YYMM,2) " +
			"and TMM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} " +
			"and TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} " +
			"and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by result_month_data.month_list "+
			"order by result_month_data.month_list "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountLineGraphForMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);


	@Query(value =
			"select "+
					"TCI.CD as vhclDrctCd, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
			"from "+
					"srlk.TS_MVMNEQ_MM TMM "+
			"left join srlk.TC_CD_INFO TCI on TMM.VHCL_DRCT = TCI.CD and TCI.GRPCD_ID = :grpcdId "+
			"where TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} " +
			"and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"group by TCI.CD,TCI.CDNM_ENG,TCI.CDNM_KOR,TCI.CDNM_SIN "+
			"order by TCI.CD asc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getVhclDrctForMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,@Param("grpcdId") String grpcdId, @Param("lang") String lang);


	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
					"RESULT_DATA.VHCL_CLSF as vhclClsf, "+
					"RESULT_DATA.TRFVLM as trfvlm, "+
					"ROUND(RESULT_DATA.trfvlm / SUM(RESULT_DATA.TRFVLM) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100, 0) as rate "+
			"from ( "+
					"select "+
							"TMM.INSTLLC_ID, "+
							"SUM(TMM.TRFVLM) as TRFVLM, "+
							"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
					"from srlk.TS_MVMNEQ_MM TMM "+
					"left join srlk.TC_CD_INFO TCI on TMM.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
					"where TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
					"and TMM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TMM.INSTLLC_ID,TMM.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM desc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountTableForTrfvlmMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);
	
	@Query(value =
					"select "+
							"coalesce( "+
									"(select "+
									 		"sum(TMM.trfvlm) "+
									"from srlk.TS_MVMNEQ_MM TMM "+
									"where TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
									"and TMM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
									"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
									"group by TMM.INSTLLC_ID )"+
									", 0 )"
									
			,
			nativeQuery = true
	)
	long getTotalCntForTrfvlmMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);
	@Query(value =
					"select "+
							"coalesce( "+
									"(select "+
									 		"AVG(TMM.AVG_SPEED) "+
									"from srlk.TS_MVMNEQ_MM TMM "+
									"where TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
									"and TMM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
									"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
									"group by TMM.INSTLLC_ID )"+
									", 0 )"

			,
			nativeQuery = true
	)
	long getTotalAvgForAvgSpeedMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by avg(TMM.AVG_SPEED) DESC) ) as rnum, "+
					"COALESCE(ROUND(avg(TMM.AVG_SPEED),0),0)  as avgSpeed, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclClsf "+
			"from srlk.TS_MVMNEQ_MM TMM "+
			"left join srlk.TC_CD_INFO TCI on TMM.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
			"where TMM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TMM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"and TMM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TMM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by TMM.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			"order by avg(TMM.AVG_SPEED) DESC "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsMoveMetroCountTableForAvgSpeedMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"tmc.instllc_nm , "+
					"tmc.lon , "+
					"tmc.lat , "+
					" '-' as laneCnt, "+
					"coalesce(round(avg(tmy.avg_speed),0),0) as avgSpeed, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC001'),0) as mvc001Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC002'),0) as mvc002Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC003'),0) as mvc003Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC004'),0) as mvc004Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC005'),0) as mvc005Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC006'),0) as mvc006Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC007'),0) as mvc007Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC008'),0) as mvc008Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC009'),0) as mvc009Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC010'),0) as mvc010Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC011'),0) as mvc011Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC012'),0) as mvc012Cnt, "+
					"coalesce(sum(tmy.trfvlm) filter(where tmy.vhcl_clsf = 'MVC013'),0) as mvc013Cnt, "+
					"coalesce(round(sum(tmy.trfvlm),0),0) as trfvlm "+
			"from "+
					"srlk.tl_mvmneq_cur tmc "+
			"left join srlk.ts_mvmneq_yy tmy on tmc.instllc_id = tmy.instllc_id and tmy.stats_yy = :searchYear "+
			"group by tmc.instllc_id "+
			"order by tmc.instllc_nm "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getMoveMetroCountListForExcel(@Param("searchYear") String searchYear);
	/**
	  * @Method Name : getMaxInstllcIdByFixed
	  * @작성일 : 2024. 8. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 최대 인덱스 값 조회
	  * @return
	  */
	@Query(value = "SELECT "
  		+ "			CAST(CAST(MAX(CAST(tmc.instllc_id AS BIGINT)) + 1 AS BIGINT) AS TEXT) "
  		+ "		FROM "
  		+ "			srlk.tl_mvmneq_cur tmc "
  		, nativeQuery = true)
	String getMaxInstllcIdByPortable();
}
