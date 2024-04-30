package com.sri.lanka.traffic.admin.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;

public interface TcAuthMngRepository extends JpaRepository<TcAuthMng, String>{

	TcAuthMng findOneByAuthId(String authId);

	Optional<TcAuthMng> findByAuthgrpId(String authgrpId);

	void deleteAllByAuthgrpId(String authgrpId);

	/**
	  * @Method Name : findByAuthNm
	  * @작성일 : 2024. 4. 24.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 일반관리자 권한 조회 (일단 authNm으로 조회하고 추후 변경)
	  * @param string
	  * @return
	  */
	Optional<TcAuthMng> findByAuthNm(String string);

}
