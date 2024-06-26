package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;

public interface TmExmnPollsterRepository extends JpaRepository<TmExmnPollster, String>{

	
	/**
	  * @Method Name : findAllByExmnmngIdOrderByRegistDtAsc
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사원 목록 조회
	  * @param exmnmngId
	  * @return
	  */
	List<TmExmnPollster> findAllByExmnmngIdOrderByRegistDtAsc(String exmnmngId);
}
