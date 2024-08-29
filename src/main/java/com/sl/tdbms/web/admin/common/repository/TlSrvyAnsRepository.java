package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.entity.TlSrvyAns;

public interface TlSrvyAnsRepository extends JpaRepository<TlSrvyAns, String>{

	/**
	  * @Method Name : getResultListForTableSurveyHistory
	  * @작성일 : 2024. 7. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 조사 현황 상세 설문 조사 결과 조회
	  * @param srvyrsltId
	  * @return
	  */
	
	@Query(value ="SELECT " +
            "tsa.sect_type AS \"sectType\", tsa.qstn_sqno AS \"qstnSqno\", " +
            "(CASE WHEN ans_cnts IS NULL OR trim(ans_cnts) = '' or split_part(ans_cnts,',',1) = 'null' THEN '-' ELSE "
            + "(CASE WHEN tsa.srvy_metadata_cd IN :metadataCode THEN split_part(ans_cnts,',',1) ELSE ans_cnts END)"
            + " END) AS \"ansCnt\", " +
            "tsa.sect_sqno AS \"sectSqno\", " +
            "(CASE WHEN :lang = 'eng' THEN tci.cdnm_eng WHEN :lang = 'kor' THEN tci.cdnm_kor WHEN :lang = 'sin' THEN tci.cdnm_sin END)"
            + " AS \"qstnNm\" " +
            "FROM srlk.tl_srvy_ans tsa " +
            "LEFT JOIN srlk.tc_cd_info tci ON tsa.srvy_metadata_cd = tci.cd " +
            "WHERE tsa.srvyrslt_id = :srvyrsltId " +
            "GROUP BY tsa.sect_type, tsa.qstn_sqno, ans_cnts, tsa.sect_sqno, tci.cdnm_eng, tci.cdnm_kor, tci.cdnm_sin, tsa.srvy_metadata_cd " +
            "ORDER BY tsa.sect_type, tsa.sect_sqno, tsa.qstn_sqno ", 
    nativeQuery = true)
	List<Map<String, Object>> getResultListForTableSurveyHistory(@Param("srvyrsltId") String srvyrsltId, @Param("lang") String lang, @Param("metadataCode") List<String> metadataCode);
	
	// 메타코드를 이용한 테이블 헤더 표현 방식 변경
//	@Query(value = "SELECT " +
//		    "    sect_type AS \"sectType\", " +
//		    "    tsa.qstn_sqno AS \"qstnSqno\", " +
//		    "    (CASE WHEN ans_cnts IS NULL OR trim(ans_cnts) = '' THEN '-' ELSE ans_cnts END) AS \"ansCnt\", " +
//		    "    tsa.sect_sqno AS \"sectSqno\", " +
//		    "    (CASE WHEN tsa.srvy_metadata_cd IN :metadataCode THEN tsa.qstn_title " +
//		    "        ELSE CONCAT(tci.cdnm_eng, ' ', tsa.sect_sqno, '-', " +
//		    "            ROW_NUMBER() OVER (PARTITION BY tci.cdnm_eng, tsa.sect_sqno " +
//		    "                               ORDER BY tsa.qstn_sqno) - " +
//		    "            SUM(CASE WHEN tsa.srvy_metadata_cd IN :metadataCode THEN 1 ELSE 0 END) " +
//		    "                OVER (PARTITION BY tci.cdnm_eng, tsa.sect_sqno " +
//		    "                      ORDER BY tsa.qstn_sqno " +
//		    "                      ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)" +
//		    "        ) " +
//		    "    END) AS \"qstnNm\" " +
//		    "FROM srlk.tl_srvy_ans tsa " +
//		    "LEFT JOIN srlk.tc_cd_info tci ON tsa.sect_type = tci.cd " +
//		    "WHERE tsa.srvyrslt_id = :srvyrsltId " +
//		    "ORDER BY sect_type, tsa.sect_sqno, tsa.qstn_sqno",
//    nativeQuery = true)
//	List<Map<String, Object>> getResultListForTableSurveyHistory(@Param("srvyrsltId") String srvyrsltId, @Param("metadataCode") List<String> metadataCode);


	/**
	  * @Method Name : getResultListForTableHistorySurveyTotalCnt
	  * @작성일 : 2024. 7. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 조사 현황 상세 설문 조사 결과 조회 갯수
	  * @param srvyrsltId
	  * @return
	  */
//	@Query(value = 
//			"SELECT COUNT(TOTAL_CNT.*) FROM (" +
//			"SELECT " +
//			"sect_type as \"sectType\", tsa.qstn_sqno as \"qstnSqno\", " +
//			"(case when ans_cnts is null or trim(ans_cnts) = '' then '-' else ans_cnts end) as \"ansCnt\", " +
//            "tsa.sect_sqno as \"sectSqno\", " +
//            "CONCAT(tci.cdnm_eng, ' ', tsa.sect_sqno, '-', tsa.qstn_sqno) as \"qstnNm\" " +
//            "FROM srlk.tl_srvy_ans tsa " +
//            "LEFT JOIN srlk.tc_cd_info tci ON tsa.sect_type = tci.cd " +
//            "WHERE tsa.srvyrslt_id = :srvyrsltId " +
//            "GROUP BY sect_type, tsa.qstn_sqno, ans_cnts, tsa.sect_sqno, tci.cdnm_eng " +
//            "ORDER BY sect_type, tsa.sect_sqno, tsa.qstn_sqno "+
//            ") TOTAL_CNT",
//    nativeQuery = true)
//	int getResultListForTableHistorySurveyTotalCnt(@Param("srvyrsltId") String srvyrsltId);
	
	/**
	  * @Method Name : getSurveyHistoryResultListForExcel
	  * @작성일 : 2024. 7. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문조사결과 엑셀 다운로드 데이터
	  * @param srvyrsltId
	  * @param lang
	  * @param metadataCode
	  * @return
	  */
	@Query(value = ""
			+ " SELECT "
			+ "		CAST(JSONB_BUILD_OBJECT('ansCnt',JSONB_AGG(RESULT.ans_cnts ORDER BY RESULT.sect_type, RESULT.sect_sqno, RESULT.qstn_sqno),"
			+ " 	'qstnNm',JSONB_AGG(concat(RESULT.qstn_nm,RESULT.sect_sqno,'-',RESULT.qstn_sqno) ORDER BY RESULT.sect_type, RESULT.sect_sqno, RESULT.qstn_sqno)) AS TEXT) AS \"resultData\" "
			+ " FROM (	"
			+ "		SELECT"
			+ "			tsa.srvyrslt_id, tsa.sect_type, tsa.qstn_sqno, tsa.sect_sqno, tsr.regist_dt, "
			+ "        	(CASE WHEN tsa.ans_cnts IS NULL OR trim(ans_cnts) = '' or split_part(tsa.ans_cnts,',',1) = 'null' THEN '-' ELSE "
			+ "            (CASE WHEN tsa.srvy_metadata_cd IN :metadataCode THEN split_part(tsa.ans_cnts,',',1) ELSE tsa.ans_cnts END) "
			+ "             END) AS \"ans_cnts\", "
			+ "        	(CASE "
			+ "            	WHEN :lang = 'eng' THEN tci.cdnm_eng "
			+ "            	WHEN :lang = 'kor' THEN tci.cdnm_kor "
			+ "            	WHEN :lang = 'sin' THEN tci.cdnm_sin "
			+ "        	END) AS qstn_nm "
			+ "     FROM"
			+ "        	srlk.tl_srvy_ans tsa "
			+ "     LEFT JOIN"
			+ "        	srlk.tc_cd_info tci "
			+ "            ON tsa.srvy_metadata_cd = tci.cd "
			+ "     LEFT JOIN"
			+ "        	srlk.tl_srvy_rslt tsr "
			+ "            ON tsa.srvyrslt_id = tsr.srvyrslt_id "
			+ " 	WHERE"
			+ "        	tsa.srvyrslt_id IN :srvyrsltId"
			+ "     GROUP by"
			+ "			tsa.srvyrslt_id, tsa.sect_type, tsa.qstn_sqno, tsa.ans_cnts, tsa.sect_sqno, tci.cdnm_eng, tci.cdnm_kor, tci.cdnm_sin, tsa.srvy_metadata_cd, tsr.regist_dt "
			+ "     ORDER by"
			+ "    		tsa.srvyrslt_id, tsa.sect_type, tsa.sect_sqno, tsa.qstn_sqno, tsr.regist_dt "
			+ " ) RESULT"
			+ "    GROUP BY RESULT.srvyrslt_id, RESULT.regist_dt "
			+ "    ORDER BY RESULT.regist_dt DESC ",
			nativeQuery = true)
	List<Map<String, Object>> getSurveyHistoryResultListForExcel(@Param("srvyrsltId") List<String> srvyrsltId, @Param("lang") String lang, @Param("metadataCode") List<String> metadataCode);

	/**
	 * methodName : findSurveyResults
	 * author : Peo.Lee
	 * date : 2024-08-26
	 * description : kecc권한 설문 결과 조회
	 * @param srvyrsltId
	 * @return List<Object [ ]>
	 */
	@Query(value = "SELECT " +
			"tsa.srvyans_id AS \"srvyansId\", " +
			"tsa.qstn_title AS \"qstnTitle\", " +
			"tsa.ans_cnts AS \"ansCnts\", " +
			"tsa.sect_sqno AS \"sectSqno\", " +
			"tsa.sect_type AS \"sectType\", " +
			"tsa.qstn_sqno AS \"qstnSqno\", " +
			"tsa.srvy_metadata_cd AS \"srvyMetadataCd\", " +
			"tsa.qstn_type AS \"qstnType\", " +
			"tsa.etc_yn AS \"etcYn\", " +
			"CASE WHEN A.ans_cnts IS NOT NULL THEN CAST(A.ans_cnts AS text) ELSE null END AS \"ansList\" " +  // ::text 대신 CAST 사용
			"FROM srlk.tl_srvy_ans tsa " +
			"INNER JOIN srlk.tl_srvy_rslt tsr ON tsr.srvyrslt_id = tsa.srvyrslt_id " +
			"INNER JOIN srlk.tl_exmn_rslt ter ON tsr.exmnrslt_id = ter.exmnrslt_id " +
			"INNER JOIN srlk.tm_exmn_mng tem ON ter.exmnmng_id = tem.exmnmng_id " +
			"INNER JOIN srlk.tm_srvy_info tsi ON tem.srvy_id = tsi.srvy_id " +
			"LEFT OUTER JOIN ( " +
			"  SELECT " +
			"  tss.srvy_id, " +
			"  tss.sect_id, " +
			"  tss.sect_type, " +
			"  tsq.qstn_title, " +
			"  tsq.qstn_sqno, " +
			"  tsq.srvy_metadata_cd, " +
			"  CAST(jsonb_agg(tmsa.ans_cnts ORDER BY tmsa.ans_sqno) FILTER (WHERE tmsa.ans_cnts IS NOT NULL) AS text) AS ans_cnts " +
			"  FROM srlk.tm_srvy_qstn tsq " +
			"  INNER JOIN srlk.tm_srvy_sect tss ON tsq.sect_id = tss.sect_id " +
			"  LEFT OUTER JOIN srlk.tm_srvy_ans tmsa ON tmsa.qstn_id = tsq.qstn_id " +
			"  GROUP BY tss.sect_id, tss.sect_type, tsq.qstn_title, tsq.qstn_sqno, tsq.srvy_metadata_cd " +
			") A ON A.srvy_id = tsi.srvy_id AND tsa.sect_type = A.sect_type AND A.srvy_metadata_cd = tsa.srvy_metadata_cd " +
			"WHERE tsa.srvyrslt_id = :srvyrsltId " +
			"GROUP BY tsa.srvyans_id, tsa.qstn_title, tsa.ans_cnts, tsa.sect_sqno, tsa.sect_type, tsa.qstn_sqno, tsa.srvy_metadata_cd, tsa.qstn_type, tsa.etc_yn, A.ans_cnts " +
			"ORDER BY tsa.sect_sqno, tsa.sect_type, tsa.qstn_sqno",
			nativeQuery = true)
	List<Map<String, Object>> findSurveyResults(@Param("srvyrsltId") String srvyrsltId);

	@Modifying
	@Query(value = "UPDATE srlk.tl_srvy_ans SET ans_cnts = :ansCnts, etc_yn = :etcYn WHERE srvyans_id = :srvyansId", nativeQuery = true)
	void updateAnsCntsAndEtcYnBySrvyansId(@Param("ansCnts") String ansCnts, @Param("etcYn") String etcYn, @Param("srvyansId") String srvyansId);

}
