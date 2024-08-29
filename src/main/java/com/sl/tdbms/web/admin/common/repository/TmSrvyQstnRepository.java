package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmSrvyQstn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmSrvyQstnRepository extends JpaRepository<TmSrvyQstn, String>{

	
	/**
	  * @Method Name : findAllBySectId
	  * @작성일 : 2024. 3. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 질문 목록 조회
	  * @param sectId
	  * @return
	  */
	List<TmSrvyQstn> findAllBySectIdOrderByQstnSqnoAsc(String sectId);

	List<TmSrvyQstn> findAllBySectId(String sectId);

	void deleteAllByQstnId(String qstnId);
}
