package com.sri.lanka.traffic.admin.common.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sri.lanka.traffic.admin.common.entity.TlExmnRslt;

public interface TlExmnRsltRepository extends JpaRepository<TlExmnRslt, String>{

	
	/**
	 * @Method Name : getScheduleStatistics
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 데이터가 존재하는 시간 가져오기(이력화면)
	 * @param exmnmngId
	 * @return
	 */
	@Query(value =
			"SELECT " +
					"DATA_RESULT.DATA_HOUR as \"dataHour\" , " +
					"DATA_RESULT.LCCHG_RSN as \"lcchgRsn\" " +
			"FROM (" +
					"SELECT " +
						"CASE WHEN TTR.LCCHG_RSN IS NOT NULL OR TTI.TRFVLMRSLT_ID IS NULL THEN CONCAT(TO_CHAR(TTR.REGIST_DT ,'HH24'),':00') " +
						"ELSE CONCAT(TO_CHAR(TTI.FTNMINUNIT_TIME,'HH24'),':00') " +
						"END AS DATA_HOUR, " +
						"CASE WHEN TTR.LCCHG_RSN IS NULL AND TTI.TRFVLMRSLT_ID IS NULL THEN \'NO_DATA\' " +
						"WHEN TTI.FTNMINUNIT_TIME IS NOT NULL THEN NULL " +
						"ELSE TTR.LCCHG_RSN END AS LCCHG_RSN " +
					"FROM srlk.TM_EXMN_MNG TEM " +
					"INNER JOIN srlk.TL_EXMN_RSLT TER ON TEM.EXMNMNG_ID = TER.EXMNMNG_ID " +
					"LEFT JOIN srlk.TL_TRFVL_RSLT TTR ON TER.EXMNRSLT_ID = TTR.EXMNRSLT_ID " +
					"LEFT JOIN srlk.TL_TRFVL_INFO TTI ON TTR.TRFVLMEXMN_ID = TTI.TRFVLMEXMN_ID " +
					"WHERE TEM.EXMNMNG_ID = :exmnmngId " +
					"AND DATE_TRUNC('DAY', TTR.REGIST_DT) = :searchDate " +
					"AND TTR.STARTLC_NM = :startlcNm " +
					"AND TTR.ENDLC_NM = :endlcNm " +
					"GROUP BY TER.EXMNRSLT_ID, TO_CHAR(TTI.FTNMINUNIT_TIME,'HH24'), TO_CHAR(TTR.REGIST_DT ,'HH24'), TTR.LCCHG_RSN, TTI.TRFVLMRSLT_ID " +
			") DATA_RESULT " +
			"GROUP BY DATA_RESULT.DATA_HOUR, DATA_RESULT.LCCHG_RSN " +
			"ORDER BY DATA_RESULT.DATA_HOUR ASC ",
					nativeQuery = true)
	List<Map<String,Object>> getTimeListForHistory(@Param("exmnmngId") String exmnmngId,@Param("searchDate") LocalDate searchDate
													,@Param("startlcNm") String startlcNm,@Param("endlcNm") String endlcNm);
	/**
	 * @Method Name : getDateListForTableHistory
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 이력 테이블 목록 호출
	 * @param exmnmngId
	 * @param startlcNm
	 * @param endlcNm
	 * @return
	 */
	@Query(value =
					"SELECT " +
						"TO_CHAR(HISTORY_RESULT.START_DT,'YYYY-MM-dd') AS \"invstDt\", " + 
						"COUNT(HISTORY_RESULT.DATA_DT) AS \"completeCnt\", " +
						"STRING_AGG(CASE WHEN HISTORY_RESULT.LCCHG_RSN != '-' THEN HISTORY_RESULT.LCCHG_RSN ELSE NULL END ,'') AS \"lcchgRsn\" " +
					"FROM ( " +
							"WITH RECURSIVE INVST_DATE_LIST AS ( " +
								"SELECT " +
									"TER.EXMNRSLT_ID AS EXMNRSLT_ID, " +
									"TER.START_DT AS START_DT " +
								"FROM " +
									"srlk.TL_EXMN_RSLT TER " +
								"WHERE TER.EXMNMNG_ID = :exmnmngId " +
								"UNION ALL " +
								"SELECT EXMNRSLT_ID,START_DT + INTERVAL '1' day FROM INVST_DATE_LIST WHERE START_DT < CURRENT_DATE " + 
							") " +
							"SELECT " +
								"IDL.START_DT, " +
								"TO_CHAR(TTI.FTNMINUNIT_TIME, 'YYYY-MM-dd HH24') as DATA_DT, " +
								"COALESCE (TTR.LCCHG_RSN ,'-') AS LCCHG_RSN " +
							"FROM INVST_DATE_LIST IDL " +
							"LEFT JOIN srlk.TL_TRFVL_RSLT TTR ON IDL.EXMNRSLT_ID = TTR.EXMNRSLT_ID AND IDL.START_DT = TTR.EXMNSTART_DT AND TTR.STARTLC_NM = :startlcNm AND TTR.ENDLC_NM = :endlcNm " +
							"LEFT JOIN srlk.TL_TRFVL_INFO TTI ON TTR.TRFVLMEXMN_ID = TTI.TRFVLMEXMN_ID " +
							"GROUP BY IDL.START_DT, TO_CHAR(TTI.FTNMINUNIT_TIME, 'YYYY-MM-dd HH24') ,TTR.TRFVLMEXMN_ID " + 
							"ORDER BY IDL.START_DT " +
					") HISTORY_RESULT " +
					"GROUP BY HISTORY_RESULT.START_DT " +
					"ORDER BY HISTORY_RESULT.START_DT DESC " +
					"OFFSET :offsetCount LIMIT :limitCount ",
							nativeQuery = true)
	List<Map<String,Object>> getDateListForTableHistory(@Param("exmnmngId") String exmnmngId,@Param("startlcNm") String startlcNm,@Param("endlcNm") String endlcNm
														,@Param("offsetCount") long offsetCount,@Param("limitCount") long limitCount);
	
	/**
	  * @Method Name : getDateListForTableHistoryTotalCnt
	  * @작성일 : 2024. 5. 10.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 이력 테이블 목록 카운트 
	  * @param exmnmngId
	  * @param startlcNm
	  * @param endlcNm
	  * @return
	  */
	@Query(value =
			"SELECT COUNT(TOTAL_CNT.*) FROM (" +
				"SELECT " +
					"TO_CHAR(HISTORY_RESULT.START_DT,'YYYY-MM-dd') AS \"invstDt\", " + 
					"COUNT(HISTORY_RESULT.DATA_DT) AS \"completeCnt\", " +
					"STRING_AGG(CASE WHEN HISTORY_RESULT.LCCHG_RSN != '-' THEN HISTORY_RESULT.LCCHG_RSN ELSE NULL END ,'') AS \"lcchgRsn\" " +
				"FROM ( " +
						"WITH RECURSIVE INVST_DATE_LIST AS ( " +
							"SELECT " +
								"TER.EXMNRSLT_ID AS EXMNRSLT_ID, " +
								"TER.START_DT AS START_DT " +
							"FROM " +
								"srlk.TL_EXMN_RSLT TER " +
							"WHERE TER.EXMNMNG_ID = :exmnmngId " +
							"UNION ALL " +
							"SELECT EXMNRSLT_ID,START_DT + INTERVAL '1' day FROM INVST_DATE_LIST WHERE START_DT < CURRENT_DATE " + 
						") " +
						"SELECT " +
							"IDL.START_DT, " +
							"TO_CHAR(TTI.FTNMINUNIT_TIME, 'YYYY-MM-dd HH24') as DATA_DT, " +
							"COALESCE (TTR.LCCHG_RSN ,'-') AS LCCHG_RSN " +
						"FROM INVST_DATE_LIST IDL " +
						"LEFT JOIN srlk.TL_TRFVL_RSLT TTR ON IDL.EXMNRSLT_ID = TTR.EXMNRSLT_ID AND IDL.START_DT = TTR.EXMNSTART_DT AND TTR.STARTLC_NM = :startlcNm AND TTR.ENDLC_NM = :endlcNm " +
						"LEFT JOIN srlk.TL_TRFVL_INFO TTI ON TTR.TRFVLMEXMN_ID = TTI.TRFVLMEXMN_ID " +
						"GROUP BY IDL.START_DT, TO_CHAR(TTI.FTNMINUNIT_TIME, 'YYYY-MM-dd HH24') ,TTR.TRFVLMEXMN_ID " + 
						"ORDER BY IDL.START_DT " +
				") HISTORY_RESULT " +
				"GROUP BY HISTORY_RESULT.START_DT " +
				"ORDER BY HISTORY_RESULT.START_DT DESC " +
			") TOTAL_CNT",
					nativeQuery = true)
	long getDateListForTableHistoryTotalCnt(@Param("exmnmngId") String exmnmngId,@Param("startlcNm") String startlcNm,@Param("endlcNm") String endlcNm);
}
