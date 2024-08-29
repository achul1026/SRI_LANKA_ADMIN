package com.sl.tdbms.web.admin.common.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.entity.TlExmnRslt;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;

public interface TlExmnRsltRepository extends JpaRepository<TlExmnRslt, String>{


	/**
	 * @Method Name : getTrfvlmexmnIdList
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 교통량 조사 ID 목록 호출
	 * @param exmnmngId
	 * @param startlcNm
	 * @param endlcNm
	 * @return
	 */
	@Query(value =
			"select "+
					"ttr.trfvlmexmn_id "+
			"from "+
			"srlk.tm_exmn_mng tem "+
			"inner join srlk.tl_exmn_rslt ter on tem.exmnmng_id = ter.exmnmng_id "+
			"inner join srlk.tl_trfvl_rslt ttr on ter.exmnrslt_id = ttr.exmnrslt_id "+
			"inner join srlk.tl_trfvl_info tti on ttr.trfvlmexmn_id = tti.trfvlmexmn_id and tti.trfvlmrslt_id is not null "+
			"where tem.exmnmng_id = :exmnmngId "+
			"AND DATE_TRUNC('DAY', TTR.REGIST_DT) = :searchDate "+
			"and TTR.startlc_nm = :startlcNm "+
			"and TTR.endlc_nm = :endlcNm "+
			"group by ttr.trfvlmexmn_id ",
			nativeQuery = true)
	List<String> getTrfvlmexmnIdList(@Param("exmnmngId") String exmnmngId,@Param("searchDate") LocalDate searchDate
												,@Param("startlcNm") String startlcNm,@Param("endlcNm") String endlcNm);

	/**
	 * @Method Name : getTrfvlmexmnIdList
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 불가사유 목록 및 날짜 목록
	 * @param startlcNm
	 * @param endlcNm
	 * @return
	 */
	@Query(value =
			"select "+
					"ttr.lcchg_rsn AS \"lcchgRsn\", "+
					"TO_CHAR(ttr.regist_dt,'yyyy-MM-dd HH24:mi') AS \"lcchgRsnDt\" "+
			"from "+
					"srlk.tm_exmn_mng tem "+
			"inner join srlk.tl_exmn_rslt ter on tem.exmnmng_id = ter.exmnmng_id "+
			"inner join srlk.tl_trfvl_rslt ttr on ter.exmnrslt_id = ttr.exmnrslt_id "+
			"where tem.exmnmng_id = :exmnmngId "+
			"AND ttr.lcchg_rsn is not null "+
			"and ttr.startlc_nm = :startlcNm "+
			"and ttr.endlc_nm = :endlcNm ",
			nativeQuery = true)
	List<Map<String,Object>> getLcchgRsnList(@Param("exmnmngId") String exmnmngId,
											 @Param("startlcNm") String startlcNm,
											 @Param("endlcNm") String endlcNm);

	/**
	 * @Method Name : getDateListForTableTrafficHistory
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 교통량 이력 테이블 목록 호출
	 * @param trfvlmexmnIdList
	 * @param searchStartDt
	 * @param searchEndDt
	 * @return
	 */
	@Query(value =
			"with survey_date as ( "+
					"select generate_series( "+
						"CAST(:searchStartDt AS timestamp), "+
						"CAST(:searchEndDt AS timestamp), "+
						" (interval '15 minutes') "+
				") as survey_date_time "+
			") "+
			"select "+
				"to_char(si.survey_date_time, 'HH24:mi') AS \"surveyDateTime\", "+
				"case when tti.trfvlmrslt_id is not null then 'Y' else 'N' end AS \"dataYn\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC001' then tti.TRFVLM else 0 end) as \"mclcnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC002' then tti.TRFVLM else 0 end) as \"twlcnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC003' then tti.TRFVLM else 0 end) as \"carcnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC004' then tti.TRFVLM else 0 end) as \"vancnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC005' then tti.TRFVLM else 0 end) as \"mbucnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC006' then tti.TRFVLM else 0 end) as \"lbucnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC007' then tti.TRFVLM else 0 end) as \"lgvcnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC008' then tti.TRFVLM else 0 end) as \"mg1cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC009' then tti.TRFVLM else 0 end) as \"mg2cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC010' then tti.TRFVLM else 0 end) as \"hg3cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC011' then tti.TRFVLM else 0 end) as \"ag3cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC012' then tti.TRFVLM else 0 end) as \"ag4cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC013' then tti.TRFVLM else 0 end) as \"ag5cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC014' then tti.TRFVLM else 0 end) as \"ag6cnt\", "+
				"SUM(case when tti.mvmnmean_type = 'MTC015' then tti.TRFVLM else 0 end) as \"fvhcnt\" "+
			"from "+
				"survey_date si "+
				"left join srlk.tl_trfvl_info tti on si.survey_date_time = tti.ftnminunit_time "+
				"and tti.trfvlmexmn_id in (:trfvlmexmnIdList) "+
			"group by "+
				"si.survey_date_time, "+
				"case when tti.trfvlmrslt_id is not null then 'Y' else 'N' end "+
			"order by "+
				"si.survey_date_time  " ,
			nativeQuery = true)
	List<Map<String,Object>> getDateListForTableTrafficHistory(@Param("trfvlmexmnIdList") List<String> trfvlmexmnIdList,
															   @Param("searchStartDt") LocalDateTime searchStartDt,
															   @Param("searchEndDt") LocalDateTime searchEndDt);
	

	
	/**
	 * @Method Name : getDateListForTableSurveyHistory
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 설문 이력 테이블 목록 호출
	 * @param exmnmngId
	 * @param startlcNm
	 * @param endlcNm
	 * @return
	 */
	@Query(value =
		    "SELECT * FROM (" +
		    	    "SELECT " +
		    	    "  to_char(tsr.regist_dt, 'yyyy-mm-dd HH24:mi') as \"startDt\", " +
		    	    "  tsr.pollster_nm as \"pollsterNm\", " +
		    	    "  tsr.pollster_tel as \"pollsterTel\", " +
		    	    "  tsr.regist_dt as \"registDt\", " +
		    	    "  tsr.srvyrslt_id as \"srvyrsltId\", " +
		    	    "  MAX(CASE WHEN tsa.SRVY_METADATA_CD = :srvyMetadataCd THEN TSA.ANS_CNTS ELSE NULL END) AS \"ansCnts\" " +
		    	    "FROM " +
		    	    "  srlk.tl_exmn_rslt ter " +
		    	    "LEFT JOIN srlk.tl_srvy_rslt tsr ON ter.exmnrslt_id = tsr.exmnrslt_id " +
		    	    "LEFT JOIN srlk.tl_srvy_ans tsa ON tsr.srvyrslt_id = tsa.srvyrslt_id " +
		    	    "WHERE " +
		    	    "  ter.exmnmng_id = :exmnmngId " +
		    	    "  AND tsa.srvyans_id IS NOT NULL " +
		    	    "GROUP BY " +
		    	    "  tsr.pollster_nm, " +
		    	    "  tsr.pollster_tel, " +
		    	    "  tsr.regist_dt, " +
		    	    "  tsr.srvyrslt_id " +
		    	    "ORDER BY " +
		    	    "  tsr.regist_dt DESC " +
		    	    ") tsr " +
		    	    "OFFSET :offsetCount LIMIT :limitCount ",
							nativeQuery = true)
	List<Map<String,Object>> getDateListForTableSurveyHistory(@Param("exmnmngId") String exmnmngId,@Param("offsetCount") long offsetCount
															,@Param("limitCount") long limitCount, @Param("srvyMetadataCd") String srvyMetadataCd);
	
	
	
	@Query(value =
			"SELECT COUNT(TOTAL_CNT.*) FROM (" +
			"SELECT " +
            "  to_char(tsr.exmnstart_dt, 'yyyy-mm-dd') as \"startDt\", " +
            "  tsr.pollster_nm as \"pollsterNm\", " +
            "  tsr.pollster_tel as \"pollsterTel\", " +
            "  tsr.regist_dt as \"registDt\", " +
            "  tsr.srvyrslt_id  as \"srvyrsltId\"" +
            "FROM " +
            "  srlk.tl_exmn_rslt ter " +
            "LEFT JOIN srlk.tl_srvy_rslt tsr ON ter.exmnrslt_id = tsr.exmnrslt_id " +
            "LEFT JOIN srlk.tl_srvy_ans tsa ON tsr.srvyrslt_id = tsa.srvyrslt_id AND tsa.srvyans_id IS NOT NULL " +
            "WHERE " +
            "  ter.exmnmng_id = :exmnmngId " +
            "GROUP BY " +
            "  tsr.exmnstart_dt, " +
            "  tsr.pollster_nm, " +
            "  tsr.pollster_tel, " +
            "  tsr.regist_dt, " +
            "  tsr.srvyrslt_id " +
            "ORDER BY " +
            "  tsr.regist_dt DESC "+
			") TOTAL_CNT",
						nativeQuery = true)
	int getDateListForTableHistorySurveyTotalCnt(@Param("exmnmngId") String exmnmngId);
	
	
	/**
	  * @Method Name : findOneByExmnmngId
	  * @작성일 : 2024. 6. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 결과 상세 조회
	  * @param exmnmngId
	  * @return
	  */
	TlExmnRslt findOneByExmnmngId(String exmnmngId);

	@Query(value =
			"SELECT " +
				"to_char(generate_series("+
				"CAST(:#{#tmExmnMng.startDt} AS timestamp), "+
				"CAST(:today AS timestamp), "+
				" (interval '1' day) "+
			"),'yyyy-mm-dd') AS \"surveyYears\" ",
			nativeQuery = true)
	List<String> getSurveyDateList(@Param("tmExmnMng") TmExmnMng tmExmnMng, @Param("today") LocalDateTime today);

}
