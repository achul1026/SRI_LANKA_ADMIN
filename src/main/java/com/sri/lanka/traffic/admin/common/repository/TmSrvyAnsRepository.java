package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TmSrvyAns;

public interface TmSrvyAnsRepository extends JpaRepository<TmSrvyAns, String>{

	/**
	  * @Method Name : findAllByQstnId
	  * @작성일 : 2024. 3. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 답변 목록 조회
	  * @param qstnId
	  * @return
	  */
	List<TmSrvyAns> findAllByQstnIdOrderByAnsSqnoAsc(String qstnId);
}
