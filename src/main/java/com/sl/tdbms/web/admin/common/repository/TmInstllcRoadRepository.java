package com.sl.tdbms.web.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.entity.TmInstllcRoad;

public interface TmInstllcRoadRepository extends JpaRepository<TmInstllcRoad, String>{

	@Modifying
	@Query(value = "UPDATE srlk.tm_instllc_road " +
		       "SET road_cd = :#{#instllcRoad.roadCd}, " +
		       "    eqpmnt_clsf = :#{#instllcRoad.eqpmntClsf}, " +
		       "    drct_cd = :#{#instllcRoad.drctCd}, " +
		       "    use_yn = :#{#instllcRoad.useYn}, " +
		       "    lane_cnt = :#{#instllcRoad.laneCnt},"+ 
		       "	updt_dt = NOW() " +
		       "WHERE instllc_id = :#{#instllcRoad.instllcId} AND eqpmnt_clsf = :#{#instllcRoad.eqpmntClsf}",
		       nativeQuery = true)
	void updateInstllcRoad(@Param("instllcRoad") TmInstllcRoad instllcRoad);

	TmInstllcRoad findByInstllcId(String instllcId);

//	void deleteByInstllcId(String instllcId);

	TmInstllcRoad findByInstllcIdAndEqpmntClsf(String instllcId, String eqpmntClsf);

	TmInstllcRoad findByRoadCdAndInstllcIdAndEqpmntClsfAndDrctCd(String roadCd, String maxInstllcId, String eqpmntClsf, String drctCd);

	void deleteByInstllcIdAndEqpmntClsf(String instllcId, String typeCd);
	
}
