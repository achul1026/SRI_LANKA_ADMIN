package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchMetroCountDetailDTO;
import com.sl.tdbms.web.admin.common.entity.TlFixedCur;

public interface TlFixedCurRepository extends JpaRepository<TlFixedCur, String>{
	
	@Query(value =
			"select "+
					"TFC.INSTLLC_ID as instllcId, "+
					"TFC.INSTLLC_NM  as instllcNm, "+
					"TFC.LAT  as lat, "+
					"TFC.LON  as lon, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location , "+
					"case when max(TFP.PASS_DT) is null or  max(TFP.PASS_DT) IS NULL OR  max(TFP.PASS_DT) < now()  - interval '10 minutes' then 'metro-danger-icon' else 'metro-icon' end as icon ,"+
					"case when max(TFP.PASS_DT) is null or  max(TFP.PASS_DT) IS NULL OR  max(TFP.PASS_DT) < now()  - interval '10 minutes' then 'error' else 'nomal' end as status "+
			"from "+
			"srlk.TL_FIXED_CUR TFC "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TFC.LON , TFC.LAT), 4326)) "+
			"left join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"left join srlk.TL_FIXED_PASS TFP on TFC.INSTLLC_ID = tfp.INSTLLC_ID and TFP.PASS_DT >= CURRENT_DATE "+
			"where (:#{#fcilitiesGISSearchDTO.searchDsdCd} IS NULL OR tss.DSTRCT_CD like cast(:#{#fcilitiesGISSearchDTO.searchDsdCd} AS TEXT) || '%') "+
			"group by TFC.INSTLLC_ID ,TDM.DSD_ID "
			,
			nativeQuery = true)
	List<Map<String,Object>> getFacilitiesFixedMetroCountList(@Param("fcilitiesGISSearchDTO") FacilitiesGISSearchDTO fcilitiesGISSearchDTO);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
					"RESULT_DATA.VHCL_CLSF as vhclClsf , "+
					"RESULT_DATA.TRFVLM  as trfvlm , "+
					"ROUND(RESULT_DATA.TRFVLM / SUM(RESULT_DATA.trfvlm) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100)   as rate "+
			"from ( "+
					"select "+
						"TFC.INSTLLC_ID, "+
						"COUNT(TFP.VHCL_CLSF) AS TRFVLM, "+
						"TFP.VHCL_CLSF "+
					"from "+
					"srlk.TL_FIXED_CUR TFC "+
					"inner join srlk.TL_FIXED_PASS TFP on TFC.INSTLLC_ID = TFP.INSTLLC_ID "+
					"where "+
					"TFC.INSTLLC_ID = :instllcId "+
					"and TFP.PASS_DT >= CURRENT_DATE "+
					"group by TFC.INSTLLC_ID,TFP.VHCL_CLSF "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM DESC ",
			nativeQuery = true)
	List<Map<String,Object>> getFacilitiesFixedMetroCountDetail(@Param("instllcId") String instllcId);


	@Query(value =
			"select "+
					"TFC.INSTLLC_ID as instllcId, "+
					"TFC.INSTLLC_NM as instllcNm, "+
					"TFC.LAT as lat, "+
					"TFC.LON as lon, "+
					"COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
					"sum(TFM.TRFVLM) as totalCnt, "+
					"ROUND(avg(TFM.AVG_SPEED),0) as totalAvg "+
			"from "+
					"srlk.TL_FIXED_CUR TFC "+
			"inner join srlk.TM_INSTLLC_ROAD TIR on TFC.INSTLLC_ID = tir.INSTLLC_ID "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TFC.LON , TFC.LAT), 4326)) "+
			"inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"inner join srlk.TS_FIXED_MM TFM  on TFC.INSTLLC_ID = TFM.INSTLLC_ID "+
			"where TFM.STATS_YYMM >= :#{#statisticsGISSearchDTO.searchStartDt} and TFM.STATS_YYMM <= :#{#statisticsGISSearchDTO.searchEndDt} "+
			"and tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
			"and TIR.eqpmnt_clsf = 'FCT002' "+
			"group by TFC.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountListForMonth(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO);
	@Query(value =
			"select "+
					"TFC.INSTLLC_ID as instllcId, "+
					"TFC.INSTLLC_NM as instllcNm, "+
					"TFC.LAT as lat, "+
					"TFC.LON as lon, "+
					"COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
					"TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
					"sum(TFO.TRFVLM) as totalCnt, "+
					"ROUND(avg(TFO.AVG_SPEED),0) as totalAvg "+
			"from "+
					"srlk.TL_FIXED_CUR TFC "+
			"inner join srlk.TM_INSTLLC_ROAD TIR on TFC.INSTLLC_ID = tir.INSTLLC_ID "+
			"inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TFC.LON , TFC.LAT), 4326)) "+
			"inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
			"inner join srlk.TS_FIXED_ONHR TFO  on TFC.INSTLLC_ID = TFO.INSTLLC_ID "+
			"where LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDTO.searchEndDt},8) "+
			"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDTO.searchEndDt},2) "+
			"and 	tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
			"and TIR.eqpmnt_clsf = 'FCT002' "+
			"group by TFC.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID "
		,
		nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountListForTime(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO);


	@Query(value =
			"select "+
					"result_time_data.time_list as dateInfo, "+
					"COALESCE(sum(TFO.TRFVLM),0) as trfvlm, "+
					"COALESCE(round(avg(TFO.avg_speed),0),0) as avgSpeed "+
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
			"left join srlk.TS_FIXED_ONHR TFO on result_time_data.db_time = RIGHT(TFO.STATS_YYMMDT,2) " +
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"and TFO.STATS_YYMMDT >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TFO.STATS_YYMMDT <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"group by result_time_data.time_list "+
			"order by result_time_data.time_list "
			,
			nativeQuery = true)
	List<Map<String,Object>> getStatisticsFixedMetroCountLineGraphForTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);


	@Query(value =
			"select "+
						"TCI.CD as vhclDrctCd, "+
						"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
			"from "+
			"srlk.TS_FIXED_ONHR TFO "+
			"left join srlk.TC_CD_INFO TCI on TFO.VHCL_DRCT = TCI.CD and TCI.GRPCD_ID = :grpcdId "+
			"where  LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
			"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
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
						   "TFO.INSTLLC_ID, "+
						   "SUM(TFO.TRFVLM) as TRFVLM, "+
							"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
					"from srlk.TS_FIXED_ONHR TFO "+
					"left join srlk.TC_CD_INFO TCI on TFO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
					"where  LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
					"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID,TFO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM desc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountTableForTrfvlmTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"coalesce( "+
					"( select "+
					"sum(TFO.trfvlm) "+
					"from srlk.TS_FIXED_ONHR TFO "+
					"where  LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
					"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID ) "+
					", 0 )"
			,
			nativeQuery = true
	)
	long getTotalCntForTrfvlmTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);
	@Query(value =
			"select "+
					"coalesce( "+
					"( select "+
					"AVG(TFO.AVG_SPEED) "+
					"from srlk.TS_FIXED_ONHR TFO "+
					"where  LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
					"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID ) "+
					", 0 )"
			,
			nativeQuery = true
	)
	long getTotalAvgForAvgSpeedTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
				"(ROW_NUMBER() OVER(order by avg(TFO.AVG_SPEED) DESC) ) as rnum, "+
				"COALESCE(ROUND(avg(TFO.AVG_SPEED),0),0)  as avgSpeed, "+
				"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclClsf "+
			"from srlk.TS_FIXED_ONHR TFO "+
			"left join srlk.TC_CD_INFO TCI on TFO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
			"where  LEFT(TFO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},8) and LEFT(TFO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},8) "+
			"and  RIGHT(TFO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt},2) and RIGHT(TFO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt},2) "+
			"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by TFO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			"order by avg(TFO.AVG_SPEED) DESC "
		,
		nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountTableForAvgSpeedTime(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"result_month_data.month_list as dateInfo, "+
					"COALESCE(sum(TFM.TRFVLM),0) as trfvlm, "+
					"COALESCE(round(avg(TFM.avg_speed),0),0) as avgSpeed "+
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
			"left join srlk.TS_FIXED_MM TFM on result_month_data.db_month = RIGHT(TFM.STATS_YYMM,2) " +
					"and TFM.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} " +
					"and TFM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} " +
					"and TFM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFM.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by result_month_data.month_list "+
			"order by result_month_data.month_list "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountLineGraphForMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
					"TCI.CD as vhclDrctCd, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
			"from "+
			"srlk.TS_FIXED_MM TFM "+
			"left join srlk.TC_CD_INFO TCI on TFM.VHCL_DRCT = TCI.CD and TCI.GRPCD_ID = :grpcdId "+
			"where TFM.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} " +
			"and TFM.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"group by TCI.CD,TCI.CDNM_ENG,TCI.CDNM_KOR,TCI.CDNM_SIN "+
			"order by TCI.CD asc "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getVhclDrctForMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,@Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select coalesce( "+
					"(select "+
					"sum(TFO.trfvlm) "+
					"from srlk.TS_FIXED_MM TFO "+
					"where TFO.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TFO.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID) "+
					",0 )"
			,
			nativeQuery = true
	)
	long getTotalCntForTrfvlmMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);
	@Query(value =
			"select coalesce( "+
					"(select "+
					"AVG(TFO.AVG_SPEED) "+
					"from srlk.TS_FIXED_MM TFO "+
					"where TFO.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TFO.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID) "+
					",0 )"
			,
			nativeQuery = true
	)
	long getTotalAvgForAvgSpeedMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
					"RESULT_DATA.VHCL_CLSF as vhclClsf, "+
					"RESULT_DATA.TRFVLM as trfvlm, "+
					"ROUND(RESULT_DATA.trfvlm / SUM(RESULT_DATA.TRFVLM) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100, 0) as rate "+
			"from ( "+
				   "select "+
					   "TFO.INSTLLC_ID, "+
					   "SUM(TFO.TRFVLM) as TRFVLM, "+
						"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
					"from srlk.TS_FIXED_MM TFO "+
					"left join srlk.TC_CD_INFO TCI on TFO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
					"where TFO.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TFO.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
					"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
					"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
					"group by TFO.INSTLLC_ID,TFO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			")RESULT_DATA "+
			"order by RESULT_DATA.TRFVLM desc "
		,
		nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountTableForTrfvlmMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"(ROW_NUMBER() OVER(order by avg(TFO.AVG_SPEED) DESC) ) as rnum, "+
					"COALESCE(ROUND(avg(TFO.AVG_SPEED),0),0)  as avgSpeed, "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclClsf "+
			"from srlk.TS_FIXED_MM TFO "+
			"left join srlk.TC_CD_INFO TCI on TFO.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
			"where TFO.STATS_YYMM >= :#{#statisticsGISSearchMetroCountDetailDTO.searchStartDt} and TFO.STATS_YYMM <= :#{#statisticsGISSearchMetroCountDetailDTO.searchEndDt} "+
			"and TFO.INSTLLC_ID = :#{#statisticsGISSearchMetroCountDetailDTO.instllcId} "+
			"and (:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} IS NULL OR TFO.VHCL_DRCT = cast(:#{#statisticsGISSearchMetroCountDetailDTO.searchVhclDrctCd} AS TEXT)) "+
			"group by TFO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
			"order by avg(TFO.AVG_SPEED) DESC "
			,
			nativeQuery = true
	)
	List<Map<String,Object>> getStatisticsFixedMetroCountTableForAvgSpeedMonth(@Param("statisticsGISSearchMetroCountDetailDTO") StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

	@Query(value =
			"select "+
					"tfc.instllc_nm , "+
					"tfc.lon , "+
					"tfc.lat , "+
					" '-' as laneCnt, "+
					"coalesce(round(avg(tfy.avg_speed),0),0) as avgSpeed, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC001'),0) as mvc001Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC002'),0) as mvc002Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC003'),0) as mvc003Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC004'),0) as mvc004Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC005'),0) as mvc005Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC006'),0) as mvc006Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC007'),0) as mvc007Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC008'),0) as mvc008Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC009'),0) as mvc009Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC010'),0) as mvc010Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC011'),0) as mvc011Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC012'),0) as mvc012Cnt, "+
					"coalesce(sum(tfy.trfvlm) filter(where tfy.vhcl_clsf = 'MVC013'),0) as mvc013Cnt, "+
					"coalesce(round(sum(tfy.trfvlm),0),0) as trfvlm "+
			"from "+
					"srlk.tl_fixed_cur tfc "+
			"left join srlk.ts_fixed_yy tfy on tfc.instllc_id = tfy.instllc_id and tfy.stats_yy = :searchYear "+
			"group by tfc.instllc_id "+
			"order by tfc.instllc_nm "
	,
	nativeQuery = true
	)
	List<Map<String,Object>> getFixedMetroCountListForExcel(@Param("searchYear") String searchYear);
	/**
	  * @Method Name : getMaxInstllcIdByFixed
	  * @작성일 : 2024. 8. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 최대 인덱스 값 조회
	  * @return
	  */
   @Query(value = "SELECT "
   		+ "			CAST(CAST(MAX(CAST(tfc.instllc_id AS BIGINT)) + 1 AS BIGINT) AS TEXT) "
   		+ "		FROM "
   		+ "			srlk.tl_fixed_cur tfc "
   		, nativeQuery = true)
	String getMaxInstllcIdByFixed();
}
