package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.StatisticsGISSearchDetailDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TmVdsInstllc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TmVdsInstllcRepository extends JpaRepository<TmVdsInstllc, String>{


    @Query(value =
            "select "+
                    "TVI.INSTLLC_ID as instllcId, "+
                    "TVI.INSTLLC_NM as instllcNm, "+
                    "TVI.CAMERA_ID as cameraId, "+
                    "TVI.LAT as lat, "+
                    "TVI.LON as lon, "+
                    "TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location , "+
                    "case when max(TVP.PASS_DT) is null or max(TVP.PASS_DT) < now()  - interval '10 minutes' then 'vds-danger-icon' else 'vds-icon' end as icon ,"+
                    "case when max(TVP.PASS_DT) is null or max(TVP.PASS_DT) < now()  - interval '10 minutes' then 'error' else 'nomal' end as status "+
            "from "+
            "srlk.TM_VDS_INSTLLC TVI "+
            "inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TVI.LON , TVI.LAT), 4326)) "+
            "left join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
            "left join srlk.TL_VDS_PASS TVP on TVI.CAMERA_ID = TVP.CAMERA_ID and TVP.PASS_DT >= CURRENT_DATE "+
            "where (:#{#fcilitiesGISSearchDTO.searchDsdCd} IS NULL OR tss.DSTRCT_CD like cast(:#{#fcilitiesGISSearchDTO.searchDsdCd} AS TEXT) || '%') "+
            "group by TVI.INSTLLC_ID,TDM.DSD_ID ",
            nativeQuery = true)
    List<Map<String,Object>> getFacilitiesVDSList(@Param("fcilitiesGISSearchDTO") FacilitiesGISSearchDTO fcilitiesGISSearchDTO);

    @Query(value =
            "select "+
                    "(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as RNUM , "+
                    "RESULT_DATA.VHCL_CLSFNM as vhclClsf , "+
                    //"RESULT_DATA.VHCL_CLSF as vhclClsf , "+
                    "RESULT_DATA.TRFVLM as trfvlm , "+
                    "ROUND(RESULT_DATA.TRFVLM / SUM(RESULT_DATA.trfvlm) OVER (PARTITION BY RESULT_DATA.CAMERA_ID) * 100) AS rate "+
            "from ( "+
                    "select "+
                        "TVP.CAMERA_ID , "+
                        "COUNT(TVP.VHCL_CLSF) AS TRFVLM, "+
                        "TVP.VHCL_CLSF, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSFNM "+
                        //"TVP.VHCL_CLSFNM "+
                    "from "+
                        "srlk.TM_VDS_INSTLLC TVI  "+
                    "inner join srlk.TL_VDS_PASS TVP on TVI.CAMERA_ID  = TVP.CAMERA_ID "+
                    "left join srlk.TC_CD_INFO TCI on TVP.vhcl_clsf = tci.CD and tci.GRPCD_ID  = :grpcdId "+
                    "where "+
                        "TVI.INSTLLC_ID = :instllcId "+
                    "and TVP.PASS_DT >= CURRENT_DATE "+
                    "group by TVP.CAMERA_ID,TVP.VHCL_CLSF ,TVP.VHCL_CLSFNM, TCI.CDNM_ENG, TCI.CDNM_KOR, TCI.CDNM_SIN "+
            ")RESULT_DATA "+
            "order by RESULT_DATA.TRFVLM DESC ",
            nativeQuery = true)
    List<Map<String,Object>> getFacilitiesVDSDetail(@Param("instllcId") String instllcId, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "SELECT " +
            	"TVI.INSTLLC_ID AS \"value\", "+
            	"TVI.INSTLLC_NM AS \"name\", "+
            	"TRM.ROAD_DESCR AS \"roadDescr\" " +
            "FROM srlk.TM_VDS_INSTLLC TVI "	+
            "INNER JOIN srlk.TM_INSTLLC_ROAD TIR " + 
            	"ON TVI.INSTLLC_ID = TIR.INSTLLC_ID " +
            "INNER JOIN srlk.TC_ROAD_MNG TRM " +
            	"ON TIR.ROAD_CD = TRM.ROAD_CD " +
            "WHERE TVI.CAMERA_ID = :#{#searchDTO.facltId}",
            nativeQuery = true)
	List<Map<String, Object>> getTmVdsInstllcListBySearchDTO(@Param("searchDTO") TsTrafficStatisticsSearchDTO searchDTO);

    @Query(value =
            "select "+
                    "TVI.INSTLLC_ID as instllcId, "+
                    "TVI.INSTLLC_NM as instllcNm, "+
                    "TVI.CAMERA_ID as cameraId, "+
                    "TVI.LAT as lat, "+
                    "TVI.LON as lon, "+
                    "COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
                    "TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
                    "sum(TVO.TRFVLM) as totalCnt, "+
                    "ROUND(avg(TVO.AVG_SPEED),0) as totalAvg, "+
                    "TCI.CD as vhclDrctCd, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
            "from "+
                    "srlk.TM_VDS_INSTLLC TVI "+
            "inner join srlk.TM_INSTLLC_ROAD TIR on tvi.INSTLLC_ID = tir.INSTLLC_ID "+
            "left join srlk.TC_CD_INFO TCI on tir.DRCT_CD = tci.CD and tci.GRPCD_ID = :grpcdId "+
            "inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TVI.LON , TVI.LAT), 4326)) "+
            "inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
            "inner join srlk.TS_VDS_ONHR TVO on TVI.CAMERA_ID = TVO.CAMERA_ID "+
            "where LEFT(TVO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDTO.searchStartDt},8) and LEFT(TVO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDTO.searchEndDt},8) "+
            "and  RIGHT(TVO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDTO.searchStartDt},2) and RIGHT(TVO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDTO.searchEndDt},2) "+
            "and TIR.eqpmnt_clsf = 'FCT001' "+
            "and 	tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
            "group by TVI.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID,TCI.CDNM_ENG,TCI.CDNM_kor,TCI.CDNM_SIN,TCI.CD  "
            ,
            nativeQuery = true)
    List<Map<String,Object>> getStatisticsVDSListForTime(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "select "+
                    "TCI.CD as vhclDrctCd, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
            "from "+
            "srlk.TM_VDS_INSTLLC TVI "+
            "inner join srlk.TM_INSTLLC_ROAD TIR on tvi.INSTLLC_ID = tir.INSTLLC_ID "+
            "inner join srlk.TC_CD_INFO TCI on tir.DRCT_CD = tci.CD and tci.GRPCD_ID = :grpcdId "+
            "where TVI.instllc_id = :instllcId "
            ,
            nativeQuery = true
    )
    List<Map<String,Object>> getVhclDrct(@Param("lang") String lang, @Param("grpcdId") String grpcdId, @Param("instllcId") String instllcId);

    @Query(value =
            "select "+
                    "TVI.INSTLLC_ID as instllcId, "+
                    "TVI.INSTLLC_NM as instllcNm, "+
                    "TVI.CAMERA_ID as cameraId, "+
                    "TVI.LAT as lat, "+
                    "TVI.LON as lon, "+
                    "COALESCE(cast(TIR.lane_cnt as TEXT),'-') as laneCnt, "+
                    "TDM.PROVIN_NM || ' > ' || TDM.DISTRICT_NM || ' > ' || TDM.DSD_NM as location  , "+
                    "sum(TVM.TRFVLM) as totalCnt, "+
                    "ROUND(avg(TVM.AVG_SPEED),0) as totalAvg, "+
                    "TCI.CD as vhclDrctCd, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclDrct "+
            "from "+
                    "srlk.TM_VDS_INSTLLC TVI "+
            "inner join srlk.TM_INSTLLC_ROAD TIR on tvi.INSTLLC_ID = tir.INSTLLC_ID "+
            "left join srlk.TC_CD_INFO TCI on tir.DRCT_CD = tci.CD and tci.GRPCD_ID = :grpcdId "+
            "inner join srlk.tc_shape_srlk tss on ST_Intersects(tss.dstrct_gis , ST_SetSRID(ST_MakePoint(TVI.LON , TVI.LAT), 4326)) "+
            "inner join srlk.TC_DSD_MNG TDM on concat(left(TDM.DSD_ID,2),RIGHT(TDM.DSD_ID,2)) = LEFT (tss.DSTRCT_CD,4) "+
            "inner join srlk.TS_VDS_MM TVM  on TVI.CAMERA_ID = TVM.CAMERA_ID "+
            "where TVM.STATS_YYMM >= :#{#statisticsGISSearchDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDTO.searchEndDt} "+
            "and 	tss.DSTRCT_CD like cast(:#{#statisticsGISSearchDTO.searchDsdCd} AS TEXT) || '%' "+
            "and TIR.eqpmnt_clsf = 'FCT001' "+
            "group by TVI.INSTLLC_ID,TIR.lane_cnt,TDM.DSD_ID,TCI.CDNM_ENG,TCI.CDNM_kor,TCI.CDNM_SIN,TCI.CD  "
            ,
            nativeQuery = true)
    List<Map<String,Object>> getStatisticsVDSListForMonth(@Param("statisticsGISSearchDTO") StatisticsGISSearchDTO statisticsGISSearchDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "select "+
                    "result_time_data.time_list as dateInfo, "+
                    "COALESCE(sum(TVO.TRFVLM),0) as trfvlm, "+
                    "COALESCE(round(avg(TVO.avg_speed),0),0) as avgSpeed "+
            "from ( "+
                   "with search_date as( "+
                   "select GENERATE_SERIES( "+
                   "cast(RIGHT(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) as numeric),cast(RIGHT(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) as numeric) "+
                ") as  time_list "+
            ") "+
                "select "+
                    "time_list, "+
                    "case when time_list < 10 then '0' || cast(time_list as varchar) else cast(time_list as varchar) end as db_time "+
                "from "+
                    "search_date "+
            ")result_time_data "+
            "left join srlk.TS_VDS_ONHR TVO on result_time_data.db_time = RIGHT(TVO.STATS_YYMMDT,2) and TVO.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
            "and LEFT(TVO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDetailDTO.searchStartDt},8) and LEFT(TVO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDetailDTO.searchEndDt},8) "+
            "and  RIGHT(TVO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) and RIGHT(TVO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) "+
            "group by result_time_data.time_list "+
            "order by result_time_data.time_list "
        ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSLineGraphForTime(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);

    @Query(value =
            "select "+
                    "(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
                    "RESULT_DATA.VHCL_CLSF as vhclclsf, "+
                    "RESULT_DATA.TRFVLM as trfvlm, "+
                    "ROUND(RESULT_DATA.trfvlm / SUM(RESULT_DATA.TRFVLM) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100, 0) as rate "+
            "from ( "+
                    "select "+
                        "TVO.INSTLLC_ID, "+
                        "SUM(TVO.TRFVLM) as TRFVLM, "+
                        "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
                    "from srlk.TS_VDS_ONHR TVO "+
                    "left join srlk.TC_CD_INFO TCI on tvo.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
                    "where LEFT(TVO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDetailDTO.searchStartDt},8) and LEFT(TVO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDetailDTO.searchEndDt},8) "+
                    "and  RIGHT(TVO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) and RIGHT(TVO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) "+
                    "and TVO.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVO.INSTLLC_ID,TVO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
            ")RESULT_DATA "+
            "order by RESULT_DATA.TRFVLM desc "
            ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSTableForTrfvlmTime(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);
    
    @Query(value =
                    "select coalesce("+
                        "(select  SUM(TVO.TRFVLM) "+
                    "from srlk.TS_VDS_ONHR TVO "+
                    "where LEFT(TVO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDetailDTO.searchStartDt},8) and LEFT(TVO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDetailDTO.searchEndDt},8) "+
                    "and  RIGHT(TVO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) and RIGHT(TVO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) "+
                    "and TVO.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVO.INSTLLC_ID ),0)"
            ,
            nativeQuery = true
    )
    long getTotalCntForTrfvlmTime(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);
    
    @Query(value =
            "select "+
                    "(ROW_NUMBER() OVER(order by avg(TVO.AVG_SPEED) DESC) ) as rnum, "+
                    "COALESCE(ROUND(avg(TVO.AVG_SPEED),0),0)  as avgspeed, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclclsf "+
            "from srlk.TS_VDS_ONHR TVO "+
            "left join srlk.TC_CD_INFO TCI on tvo.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
            "where tvo.STATS_YYMMDT >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and tvo.STATS_YYMMDT <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
            "and TVO.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
            "group by TVO.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
            "order by avg(TVO.AVG_SPEED) DESC  "
            ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSTableForAvgSpeedTime(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "select "+
                    "result_month_data.month_list as dateInfo, "+
                    "COALESCE(sum(TVM.TRFVLM),0) as trfvlm, "+
                    "COALESCE(round(avg(TVM.avg_speed),0),0) as avgSpeed "+
            "from ( "+
                   "with search_date as( "+
                       "select GENERATE_SERIES( "+
                       "cast(right(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) as numeric),cast(right(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) as numeric) "+
                    ") as  month_list "+
                ") "+
                    "select "+
                        "month_list, "+
                        "case when month_list < 10 then '0' || cast(month_list as varchar) else cast(month_list as varchar) end as db_month "+
                    "from "+
                        "search_date "+
                ")result_month_data "+
            "left join srlk.TS_VDS_MM TVM on result_month_data.db_month = RIGHT(TVM.STATS_YYMM,2) and TVM.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} and TVM.STATS_YYMM >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
            "group by result_month_data.month_list "+
    "order by result_month_data.month_list "
        ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSLineGraphForMonth(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);

    @Query(value =
                   "select coalesce("+
                       "(select SUM(TVM.TRFVLM) "+
                    "from srlk.TS_VDS_MM TVM "+
                    "where TVM.STATS_YYMM >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
                    "and TVM.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVM.INSTLLC_ID),0) "
            ,
            nativeQuery = true
    )
    long getTotalCntForTrfvlmMonth(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);
    @Query(value =
            "select coalesce("+
                    "(select  AVG(TVO.AVG_SPEED) "+
                    "from srlk.TS_VDS_ONHR TVO "+
                    "where LEFT(TVO.STATS_YYMMDT,8) >= LEFT(:#{#statisticsGISSearchDetailDTO.searchStartDt},8) and LEFT(TVO.STATS_YYMMDT,8) <= LEFT(:#{#statisticsGISSearchDetailDTO.searchEndDt},8) "+
                    "and  RIGHT(TVO.STATS_YYMMDT,2) >= RIGHT(:#{#statisticsGISSearchDetailDTO.searchStartDt},2) and RIGHT(TVO.STATS_YYMMDT,2) <= RIGHT(:#{#statisticsGISSearchDetailDTO.searchEndDt},2) " +
                    "and TVO.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVO.INSTLLC_ID ),0)"
            ,
            nativeQuery = true
    )
    long getTotalAvgForAvgSpeedTime(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);

    @Query(value =
            "select coalesce("+
                    "(select AVG(TVM.AVG_SPEED) "+
                    "from srlk.TS_VDS_MM TVM "+
                    "where TVM.STATS_YYMM >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
                    "and TVM.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVM.INSTLLC_ID),0) "
            ,
            nativeQuery = true
    )
    long getTotalAvgForAvgSpeedMonth(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO);
    
    @Query(value =
            "select "+
                "(ROW_NUMBER() OVER(order by RESULT_DATA.TRFVLM DESC) ) as rnum, "+
                "RESULT_DATA.VHCL_CLSF as vhclclsf, "+
                "RESULT_DATA.TRFVLM as trfvlm, "+
                "ROUND(RESULT_DATA.trfvlm / SUM(RESULT_DATA.TRFVLM) OVER (PARTITION BY RESULT_DATA.INSTLLC_ID) * 100, 0) as rate "+
            "from ( "+
                   "select "+
                       "TVM.INSTLLC_ID, "+
                       "SUM(TVM.TRFVLM) as TRFVLM, "+
                        "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as VHCL_CLSF "+
                    "from srlk.TS_VDS_MM TVM "+
                    "left join srlk.TC_CD_INFO TCI on TVM.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
                    "where TVM.STATS_YYMM >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
                    "and TVM.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
                    "group by TVM.INSTLLC_ID,TVM.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
            ")RESULT_DATA "+
            "order by RESULT_DATA.TRFVLM desc "
            ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSTableForTrfvlmMonth(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "select "+
                    "(ROW_NUMBER() OVER(order by avg(TVM.AVG_SPEED) DESC) ) as rnum, "+
                    "COALESCE(ROUND(avg(TVM.AVG_SPEED),0),0)  as avgspeed, "+
                    "(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclclsf "+
            "from srlk.TS_VDS_MM TVM "+
            "left join srlk.TC_CD_INFO TCI on TVM.VHCL_CLSF = tci.CD and tci.GRPCD_ID = :grpcdId "+
            "where TVM.STATS_YYMM >= :#{#statisticsGISSearchDetailDTO.searchStartDt} and TVM.STATS_YYMM <= :#{#statisticsGISSearchDetailDTO.searchEndDt} "+
            "and TVM.INSTLLC_ID = :#{#statisticsGISSearchDetailDTO.instllcId} "+
            "group by TVM.VHCL_CLSF,tci.CDNM_KOR,tci.CDNM_SIN,tci.CDNM_ENG "+
            "order by avg(TVM.AVG_SPEED) DESC  "
            ,
            nativeQuery = true
    )
    List<Map<String,Object>> getStatisticsVDSTableForAvgSpeedMonth(@Param("statisticsGISSearchDetailDTO") StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO, @Param("grpcdId") String grpcdId, @Param("lang") String lang);

    @Query(value =
            "select "+
                    "tvi.instllc_nm , "+
                    "tvi.lon , "+
                    "tvi.lat , "+
                    "'-' as laneCnt, "+
                    "coalesce(round(avg(tvy.avg_speed),0),0) as avgSpeed, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '0'),0) as mvc001Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '1'),0) as mvc002Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '2'),0) as mvc003Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '3'),0) as mvc004Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '4'),0) as mvc005Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '5'),0) as mvc006Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '6'),0) as mvc007Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '8'),0) as mvc008Cnt, "+
                    "coalesce(sum(tvy.trfvlm) filter(where tvy.vhcl_clsf = '9'),0) as mvc009Cnt, "+
                    "coalesce(round(sum(tvy.trfvlm),0),0) as trfvlm "+
            "from "+
                    "srlk.tm_vds_instllc tvi "+
            "left join srlk.ts_vds_yy tvy on tvi.instllc_id = tvy.instllc_id and tvy.stats_yy = :searchYear "+
            "group by tvi.instllc_id "+
            "order by tvi.instllc_nm  "
        ,
        nativeQuery = true
    )
    List<Map<String,Object>> getVDSListForExcel(@Param("searchYear") String searchYear);

	/**
	  * @Method Name : getMaxInstllcId
	  * @작성일 : 2024. 8. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 최대 인덱스 값 조회
	  * @return
	  */
    @Query(value = "SELECT "
    		+ "			CAST(CAST(MAX(CAST(tvi.instllc_id AS BIGINT)) + 1 AS BIGINT) AS TEXT) "
    		+ "		FROM "
    		+ "			srlk.tm_vds_instllc tvi "
    		, nativeQuery = true)
    String getMaxInstllcIdByVds();

}
