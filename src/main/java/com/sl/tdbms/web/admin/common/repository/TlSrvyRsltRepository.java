package com.sl.tdbms.web.admin.common.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TlSrvyRslt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;

public interface TlSrvyRsltRepository extends JpaRepository<TlSrvyRslt, String>{



    @Query(value =
            "SELECT " +
                    "RESULT_DATA.SRVYRSLT_ID AS \"srvyrsltId\" " +
            "FROM ( " +
                    "SELECT " +
                    "TSR.SRVYRSLT_ID, " +
                    "MAX(CASE WHEN TSA.SRVY_METADATA_CD = 'SMD055' THEN TSA.ANS_CNTS END) AS START_LOCATIN_CD, " +
                    "MAX(CASE WHEN TSA.SRVY_METADATA_CD = 'SMD061' THEN TSA.ANS_CNTS END) AS END_LOCATIN_CD " +
                    "FROM " +
                    "srlk.TM_EXMN_MNG TEM " +
                    "INNER JOIN srlk.TL_EXMN_RSLT TER ON TEM.EXMNMNG_ID = TER.EXMNMNG_ID " +
                    "INNER JOIN srlk.TL_SRVY_RSLT TSR ON TER.EXMNRSLT_ID = TSR.EXMNRSLT_ID " +
                    "INNER JOIN srlk.TL_SRVY_ANS TSA ON TSR.SRVYRSLT_ID = TSA.SRVYRSLT_ID AND TSA.SRVY_METADATA_CD IN ('SMD055','SMD061') " +
                    "WHERE TEM.EXMN_TYPE = 'ETC004' " +
                    "AND TEM.STTS_CD = 'ESC004' " +
                    "AND TO_CHAR(TSR.EXMNSTART_DT, 'YYYY') = :#{#searchDTO.searchDate} " +
                    "AND TEM.SRVY_ID = :#{#searchDTO.searchSrvyId} " +
                    "GROUP BY TSR.SRVYRSLT_ID " +
            ") RESULT_DATA " +
            "WHERE SUBSTRING(RESULT_DATA.START_LOCATIN_CD, 1, :#{#searchDTO.originSubstringIdx}) = :#{#searchDTO.searchCd} " +
            "AND SUBSTRING(RESULT_DATA.END_LOCATIN_CD, 1, :#{#searchDTO.destinationSubstringIdx}) = :#{#searchDTO.destinationSearchCd}",
            nativeQuery = true
    )
    public List<String> getSrvyRsltIdListByStatisticsSearch(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);

    @Modifying
    @Query(value = "UPDATE srlk.tl_srvy_rslt SET regist_dt = :registDt WHERE srvyrslt_id = :srvyrsltId", nativeQuery = true)
    void updateRegistDtBySrvyrsltId(@Param("registDt") LocalDateTime registDt, @Param("srvyrsltId") String srvyrsltId);
}
