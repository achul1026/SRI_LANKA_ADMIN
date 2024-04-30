package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;

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
}
