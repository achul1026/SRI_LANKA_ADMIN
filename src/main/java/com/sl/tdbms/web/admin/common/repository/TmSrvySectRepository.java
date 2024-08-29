package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmSrvySect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmSrvySectRepository extends JpaRepository<TmSrvySect, String>{

	/**
	  * @Method Name : findAllBySrvyId
	  * @작성일 : 2024. 3. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 부문 목록 조회
	  * @param exmnmngId
	  * @return
	  */
	List<TmSrvySect> findAllBySrvyIdOrderBySectSqnoAsc(String exmnmngId);

	List<TmSrvySect> findAllBySrvyId(String srvyId);

	void deleteAllBySectId(String sectId);

}
