package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TmExmnMng;
import com.sl.tdbms.web.admin.common.dto.gis.SurveyGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TmExmnMngRepository extends JpaRepository<TmExmnMng, String>{
	
	/**
	  * @Method Name : existsByPartcptCd
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 참여코드 중복 체크
	  * @param partcptCd
	  * @return
	  */
	boolean existsByPartcptCd(String partcptCd);
	
	
	/**
	  * @Method Name : getStatistics
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 현황 및 이력 > 월/일 별 통계
	  * @param completeSttsCd
	  * @return
	  */
    @Query(value = 
        "SELECT "+    
        // 조사원 모두 등록했을 때 조건
//        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :completeSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP  THEN tem.EXMNMNG_ID END) AS \"progressCompleteCnt\", " +
//        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :progressingSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP  THEN tem.EXMNMNG_ID END) AS \"progressingCnt\", " +
//        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT >= CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notYetProgressCnt\", " +
//        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT < CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notProgressCnt\", " +
//        "  		COUNT(DISTINCT CASE WHEN (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) < tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notYetInvestigatorCnt\" " +
        //조사원 1명이상이면 조사원 가능
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :completeSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) > 0  THEN tem.EXMNMNG_ID END) AS \"progressCompleteCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :progressingSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) > 0  THEN tem.EXMNMNG_ID END) AS \"progressingCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT >= CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) > 0 THEN tem.EXMNMNG_ID END) AS \"notYetProgressCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT < CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) > 0 THEN tem.EXMNMNG_ID END) AS \"notProgressCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = 0 THEN tem.EXMNMNG_ID END) AS \"notYetInvestigatorCnt\" " +
        "FROM " +
        "  		srlk.tm_exmn_mng tem " +
        "LEFT JOIN " +
        "  		srlk.tm_exmn_pollster tep ON tem.exmnmng_id = tep.EXMNMNG_ID " +
        "LEFT JOIN " +
        "  		srlk.tc_user_mng tum ON tum.usermng_id = tem.usermng_id " +
        "WHERE " +
        "  		(tem.START_DT >= :startDate AND tem.START_DT <= :endDate OR tem.END_DT >= :startDate AND tem.END_DT <= :endDate" + 
        "  		 AND :startDate BETWEEN tem.START_DT AND tem.END_DT OR :endDate BETWEEN tem.START_DT AND tem.END_DT)" +
        "AND " +
        "  		tem.STTS_CD != :writingSttsCd " + 
        "AND " +
		"		(:userBffltd IS NULL OR tum.user_bffltd = cast(:userBffltd AS TEXT))",
        nativeQuery = true)
    Map<String,Object> getScheduleStatistics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userBffltd") String userBffltd
    										, @Param("completeSttsCd") String completeSttsCd, @Param("writingSttsCd") String writingSttsCd, @Param("progressingSttsCd") String progressingSttsCd);


	/**
	  * @Method Name : findAllBySrvyId
	  * @작성일 : 2024. 6. 27.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문지 적용된 조사 목록 조회
	  * @param srvyId
	  * @return
	  */
	List<TmExmnMng> findAllBySrvyId(String srvyId);


	@Query(value =
			"select "+
					"TO_CHAR(START_DT,'yyyy') AS \"searchYear\" "+
			"from "+
			"srlk.TM_EXMN_MNG TEM "+
			"where TEM.STTS_CD = 'ESC004' "+
			"and TEM.EXMN_TYPE = :exmnTypeCd "+
			"group by TO_CHAR(START_DT,'yyyy') "+
			"order by TO_CHAR(START_DT,'yyyy') desc "
			,
			nativeQuery = true)
	List<String> getSurveySearchYears(@Param("exmnTypeCd") String exmnTypeCd);
	
	@Query(value =
			"select "+ 
					"TO_CHAR(RESULT_DATA.EXMNSTART_DT,'yyyy') AS \"searchYear\"  "+ 
			"from ( "+
				"select "+
					"TSR.EXMNSTART_DT "+
				"from "+
					"srlk.TL_EXMN_RSLT TER "+ 
				"left join srlk.TL_SRVY_RSLT TSR on TER.EXMNRSLT_ID = TSR.EXMNRSLT_ID "+ 
				"left join srlk.TL_SRVY_ANS TSA on TSR.SRVYRSLT_ID = TSA.SRVYRSLT_ID "+ 
				"where TER.EXMN_TYPE = :#{#searchDTO.exmnTypeCd.getCode()} "+
				//"and TER.STTS_CD = 'ESC003' "+
				"and TER.CSTM_YN = 'N' "+
				"and TSA.SRVY_METADATA_CD IN('SMD055','SMD061') "+ 
					/*
					 * "and  case when TSA.SRVY_METADATA_CD = 'SMD055'  and SUBSTRING(TSA.ANS_CNTS, 1, :#{#searchDTO.originSubstringIdx}) = :#{#searchDTO.searchCd}  then TRUE "
					 * +
					 * "when TSA.SRVY_METADATA_CD = 'SMD061' and SUBSTRING(TSA.ANS_CNTS, 1, :#{#searchDTO.destinationSubstringIdx}) =:#{#searchDTO.destinationSearchCd} then true else FALSE end "
					 * +
					 */
				"group by TER.EXMNRSLT_ID ,TSR.SRVYRSLT_ID  "+
				"HAVING "+
		        "MAX(CASE WHEN TSA.SRVY_METADATA_CD = 'SMD055' AND TSA.ANS_CNTS IS NOT NULL and SUBSTRING(TSA.ANS_CNTS, 1, :#{#searchDTO.originSubstringIdx}) = :#{#searchDTO.searchCd} THEN 1 ELSE 0 END) = 1 "+
		        "AND MAX(CASE WHEN TSA.SRVY_METADATA_CD = 'SMD061' AND TSA.ANS_CNTS IS NOT null AND SUBSTRING(TSA.ANS_CNTS, 1, :#{#searchDTO.destinationSubstringIdx}) = :#{#searchDTO.destinationSearchCd}  THEN 1 ELSE 0 END) = 1 "+
			")RESULT_DATA "+
			"GROUP BY TO_CHAR(RESULT_DATA.EXMNSTART_DT,'yyyy') "
					,
					nativeQuery = true)
	List<String> getOdStatisticsSearchYears(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);


	/*@Query(value =
			"select "+
					"TEM.EXMNMNG_ID AS \"exmnmngId\",  "+
					"TEM.EXMN_TYPE AS \"exmnType\",  "+
					"TEM.EXMN_NM AS \"exmnNm\",  "+
					"st_x(ST_Centroid(ST_UNION(TSS.DSTRCT_GIS))) AS \"lon\",  "+
					"st_y(ST_Centroid(ST_UNION(TSS.DSTRCT_GIS))) AS \"lat\"  "+
			"from "+
					"srlk.TM_EXMN_MNG TEM "+
			"left join srlk.TC_USER_MNG TUM on TEM.USERMNG_ID = TUM.USERMNG_ID "+
			"inner join srlk.TC_DSDAR_MNG TDM on tem.DSD_CD = tdm.DSD_ID "+
			"left join srlk.TC_SHAPE_SRLK TSS on TDM.DSTRCT_CD  = tss.DSTRCT_CD "+
			"where TEM.EXMN_TYPE in ('ETC003','ETC004','ETC005') "+
			"and (:#{#surveyGISSearchDTO.searchMngrBffltd} IS NULL OR TUM.user_bffltd = cast(:#{#surveyGISSearchDTO.searchMngrBffltd} AS TEXT)) "+
			"and TEM.END_DT >= CURRENT_dATE "+
			"and TEM.STTS_CD != 'ESC001' "+
			"and TSS.DSTRCT_CD like :#{#surveyGISSearchDTO.searchDsdCd} "+
			"group by TEM.EXMNMNG_ID "
			,
			nativeQuery = true)
	List<Map<String,Object>> getSurveyGisDataListVer1(@Param("surveyGISSearchDTO") SurveyGISSearchDTO surveyGISSearchDTO);	*/
	@Query(value =
			"select "+
					"TEM.EXMNMNG_ID AS exmnmngId, "+
					"TEM.EXMN_TYPE AS exmnType, "+
					"TEM.EXMN_NM AS exmnNm, "+
					"REPLACE (TEM.EXMN_LC,',','>') as exmnLc, "+
					"st_x(ST_Centroid(ST_UNION(TSS.DSTRCT_GIS))) AS lon, "+
					"st_y(ST_Centroid(ST_UNION(TSS.DSTRCT_GIS))) AS lat "+
			"from "+
					"srlk.TM_EXMN_MNG TEM "+
			"left join srlk.TC_USER_MNG TUM on TEM.USERMNG_ID = TUM.USERMNG_ID "+
			"left join srlk.TC_SHAPE_SRLK TSS on LEFT(TEM.DSD_CD,2) || RIGHT(TEM.DSD_CD,2) = LEFT(TSS.DSTRCT_CD ,4) "+
			"where TEM.EXMN_TYPE in (:#{#surveyGISSearchDTO.searchExmnTypeCdArr})  "+
			"and (:#{#surveyGISSearchDTO.searchMngrBffltd} IS NULL OR TUM.user_bffltd = cast(:#{#surveyGISSearchDTO.searchMngrBffltd} AS TEXT)) "+
			"and TEM.END_DT >= CURRENT_DATE "+
			"and TEM.STTS_CD != 'ESC001' "+
			"and (:#{#surveyGISSearchDTO.searchDsdCd} IS NULL OR TSS.DSTRCT_CD like cast(:#{#surveyGISSearchDTO.searchDsdCd} AS TEXT) || '%') "+
			"group by TEM.EXMNMNG_ID  "+
			"order by tem.REGIST_DT desc  "
			,
			nativeQuery = true)
	List<Map<String,Object>> getSurveyGisDataList(@Param("surveyGISSearchDTO") SurveyGISSearchDTO surveyGISSearchDTO);
}
