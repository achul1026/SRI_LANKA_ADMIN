package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TcCdInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TcCdInfoRepository extends JpaRepository<TcCdInfo, String>{

	void deleteAllByGrpcdId(String grpId);

	TcCdInfo findByCd(String cd);

	TcCdInfo findByCdnmKor(String type);

	TcCdInfo findOneByCd(String cd);

	@Query(value =
			"select "+
					"(case when 'kor' = :lang then tci.CDNM_KOR when 'sin' = :lang then tci.CDNM_SIN  else tci.CDNM_ENG  end) as vhclclsf "+
			"from "+
				"srlk.tc_cd_info tci "+
			"where "+
				"tci.grpcd_id = :grpcdId "+
			"order by tci.cd	 "
		,
			nativeQuery = true
	)
	List<String> getVhclclsfList(@Param("grpcdId") String grpcdId,@Param("lang") String lang);
}
