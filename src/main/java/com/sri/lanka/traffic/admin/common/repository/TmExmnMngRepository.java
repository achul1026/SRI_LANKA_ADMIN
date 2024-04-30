package com.sri.lanka.traffic.admin.common.repository;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;

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
	  * @param partcptCd
	  * @return
	  */
    @Query(value = 
        "SELECT " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :completeSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP  THEN tem.EXMNMNG_ID END) AS \"progressCompleteCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD  = :progressingSttsCd AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP  THEN tem.EXMNMNG_ID END) AS \"progressingCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT >= CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notYetProgressCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN tem.STTS_CD NOT IN(:completeSttsCd,:progressingSttsCd) AND tem.START_DT < CURRENT_TIMESTAMP AND (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) = tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notProgressCnt\", " +
        "  		COUNT(DISTINCT CASE WHEN (SELECT COUNT(POLLSTER_ID) FROM srlk.tm_exmn_pollster WHERE EXMNMNG_ID = tem.EXMNMNG_ID) < tem.EXMN_NOP THEN tem.EXMNMNG_ID END) AS \"notYetInvestigatorCnt\" " +
        "FROM " +
        "  		srlk.tm_exmn_mng tem " +
        "LEFT JOIN " +
        "  		srlk.tm_exmn_pollster tep ON tem.exmnmng_id = tep.EXMNMNG_ID " +
        "WHERE " +
        "  		(tem.START_DT >= :startDate AND tem.START_DT <= :endDate OR tem.END_DT >= :startDate AND tem.END_DT <= :endDate" + 
        "  		 AND :startDate BETWEEN tem.START_DT AND tem.END_DT OR :endDate BETWEEN tem.START_DT AND tem.END_DT)" + 
        "AND " +
        "  		tem.STTS_CD != :writingSttsCd ", 
        nativeQuery = true)
    Map<String,Object> getScheduleStatistics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate
    										, @Param("completeSttsCd") String completeSttsCd, @Param("writingSttsCd") String writingSttsCd, @Param("progressingSttsCd") String progressingSttsCd);
}
