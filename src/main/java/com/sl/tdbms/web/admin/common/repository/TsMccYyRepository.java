package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Map;

import com.sl.tdbms.web.admin.common.entity.TsMccTrfvlYy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;

public interface TsMccYyRepository extends JpaRepository<TsMccTrfvlYy, String>{

	@Query(value =
			"SELECT "+
				"TRFVLM_STATISTICS.VHCL_CLSF AS \"vhclClsf\", "+
				"TRFVLM_STATISTICS.VHCL_CLSF_NM AS \"name\", "+
				"TRFVLM_STATISTICS.VHCL_CLSF_TRFVLM AS \"value\", "+
				"ROUND(TRFVLM_STATISTICS.VHCL_CLSF_TRFVLM / SUM(TRFVLM_STATISTICS.VHCL_CLSF_TRFVLM) OVER (PARTITION by TRFVLM_STATISTICS.TAZ_CD) * 100, 0) AS \"rate\" " +
			"FROM" +
			"( "+
				"SELECT "+
						"MCC_TRFVL_LIST.VHCL_CLSF, "+
						"MCC_TRFVL_LIST.TAZ_CD, "+
						"MCC_TRFVL_LIST.CDNM_ENG	 	AS VHCL_CLSF_NM, "+
						"SUM(MCC_TRFVL_LIST.TRFVLM) AS VHCL_CLSF_TRFVLM "+
				"FROM ( "+
						"SELECT "+
								"TMTY.VHCL_CLSF , "+
								"SUBSTRING(TMTY.TAZ_CD,1,:#{#searchDTO.originSubstringIdx}) as TAZ_CD, "+
								"TCI.CDNM_ENG , "+
								"TMTY.TRFVLM "+
						"FROM 	srlk.TS_MCC_TRFVL_YY TMTY "+
						"LEFT JOIN srlk.TC_CD_INFO TCI ON TMTY.VHCL_CLSF = TCI.CD AND TCI.USE_YN = 'Y' "+
						"LEFT JOIN srlk.TM_EXMN_MNG TEM ON TMTY.EXMNMNG_ID = TEM.EXMNMNG_ID "+
						"WHERE TMTY.STATS_YY = :#{#searchDTO.searchDate} "+
						"AND SUBSTRING(TMTY.TAZ_CD, 1, :#{#searchDTO.originSubstringIdx}) = cast(:#{#searchDTO.searchCd} AS TEXT) "+
						"AND (:#{#searchDTO.startlcNm} is null OR TMTY.STARTLC_NM = cast(:#{#searchDTO.startlcNm} AS TEXT)) "+
						"AND (:#{#searchDTO.endlcNm} is null OR TMTY.ENDLC_NM = cast(:#{#searchDTO.endlcNm} AS TEXT))"+
						"AND TEM.ROAD_CD = :#{#searchDTO.searchRoadCd} "+
						"AND TEM.exmn_distance = :#{#searchDTO.searchExmnDistance} "+
					")MCC_TRFVL_LIST "+
				"GROUP BY 	MCC_TRFVL_LIST.VHCL_CLSF, "+
							"MCC_TRFVL_LIST.TAZ_CD, "+
							"MCC_TRFVL_LIST.CDNM_ENG "+
				"ORDER BY SUM(MCC_TRFVL_LIST.TRFVLM) DESC "+
			")TRFVLM_STATISTICS  ",
			nativeQuery = true)
	List<Map<String,Object>> getTrfvlRsltStatisticsListGroupByTazCd(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
				"DISTINCT TEM.ROAD_CD AS \"value\", " +
				"TEM.ROAD_DESCR || '(' || TEM.ROAD_CD || ')'   AS \"name\" " +
			"FROM srlk.TS_MCC_TRFVL_YY TMTY " +
			"INNER JOIN srlk.TM_EXMN_MNG TEM ON TMTY.EXMNMNG_ID = TEM.EXMNMNG_ID " +
				"AND SUBSTRING(TMTY.TAZ_CD, 1, :#{#searchDTO.originSubstringIdx}) = cast(:#{#searchDTO.searchCd} AS TEXT) "+
				"and TEM.stts_cd = 'ESC004'"+
				"AND TEM.ROAD_CD IS NOT NULL " +
				"and TEM.ROAD_CD != '' "
				 ,
			nativeQuery = true)
	List<Map<String, Object>> getTrfvlRsltStatisticsSearchListByRegion(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
					"DISTINCT TEM.exmn_distance AS \"value\", " +
					"TEM.exmn_distance || 'km' AS \"name\" " +
				"FROM srlk.TS_MCC_TRFVL_YY TMTY " +
				"INNER JOIN srlk.TM_EXMN_MNG TEM ON TMTY.EXMNMNG_ID = TEM.EXMNMNG_ID " +
					"AND SUBSTRING(TMTY.TAZ_CD, 1, :#{#searchDTO.originSubstringIdx}) = cast(:#{#searchDTO.searchCd} AS TEXT) "+
					"AND TEM.ROAD_CD = :#{#searchDTO.searchRoadCd} "+
					"and TEM.stts_cd = 'ESC004'"+
					"AND  TEM.exmn_distance IS NOT NULL "+
					"order by TEM.exmn_distance ",
			nativeQuery = true)
	List<Map<String, Object>> getTrfvlRsltStatisticsSearchListByRegionAndRoad(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);

	@Query(value = 
			"SELECT " +
					"DISTINCT TMTY.STATS_YY AS \"value\", " +
					"TMTY.STATS_YY AS \"name\" " +
				"FROM srlk.TS_MCC_TRFVL_YY TMTY " +
				"INNER JOIN srlk.TM_EXMN_MNG TEM ON TMTY.EXMNMNG_ID = TEM.EXMNMNG_ID " +
					"AND SUBSTRING(TMTY.TAZ_CD, 1, :#{#searchDTO.originSubstringIdx}) = cast(:#{#searchDTO.searchCd} AS TEXT) "+
					"AND TEM.ROAD_CD = :#{#searchDTO.searchRoadCd} "+
					"AND TEM.exmn_distance = :#{#searchDTO.searchExmnDistance} "+
					"and TEM.stts_cd = 'ESC004'"+
					"AND TMTY.STATS_YY IS NOT NULL "+
					"order by  TMTY.STATS_YY ",
			nativeQuery = true)
	List<Map<String, Object>> getTrfvlRsltStatisticsSearchListByRegionAndRoadAndDistance(@Param("searchDTO") TlExmnRsltStatisticsSearchDTO searchDTO);
}
