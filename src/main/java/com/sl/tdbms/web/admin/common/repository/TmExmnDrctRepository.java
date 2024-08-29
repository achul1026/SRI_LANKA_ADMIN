package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmExmnDrct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmExmnDrctRepository extends JpaRepository<TmExmnDrct, String>{
	
	/**
	  * @Method Name : findAllByExmnmngIdOrderByDrctSqnoAsc
	  * @작성일 : 2024. 3. 27.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 방향목록 조회 
	  * @param exmnmngId
	  * @return
	  */
	List<TmExmnDrct> findAllByExmnmngIdOrderByDrctSqnoAsc(String exmnmngId);

	void deleteByExmnmngId(String exmnmngId);
}
