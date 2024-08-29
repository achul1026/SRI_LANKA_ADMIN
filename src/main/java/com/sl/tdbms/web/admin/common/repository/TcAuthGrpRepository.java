package com.sl.tdbms.web.admin.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;

public interface TcAuthGrpRepository extends JpaRepository<TcAuthGrp, String>{

	/**
	  * @Method Name : findOneByAuthgrpId
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 조회 by authgrpId
	  * @param authgrpId
	  * @return
	  */
	TcAuthGrp findOneByAuthgrpId(String authgrpId);

	/**
	  * @Method Name : findAllByBscauthYnNotYN
	  * @작성일 : 2024. 5. 10.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 조회 by 기본 권한 여부
	  * @param bscauthYn
	  * @return
	  */
//	@Query(value = "SELECT * FROM srlk.tc_auth_grp WHERE bscauth_yn <> :bscauthYn", nativeQuery = true)
//	List<TcAuthGrp> findAllByBscauthYnNotYN(@Param("bscauthYn") String bscauthYn);

	/**
	  * @Method Name : findByBffltdCdAndBscauthYn
	  * @작성일 : 2024. 5. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 소속의 기본 권한 조회
	  * @param userBffltd
	  * @param string
	  * @return
	  */
	Optional<TcAuthGrp> findByBffltdCdAndBscauthYn(String userBffltd, String string);

	/**
	  * @Method Name : findByAuthgrpNm
	  * @작성일 : 2024. 6. 20.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 이름으로 권한 조회
	  * @param authgrpNm
	  * @return
	  */
	@Query(value = "SELECT * FROM srlk.tc_auth_grp tag WHERE tag.authgrp_nm = :authgrpNm AND tag.authgrp_id <> :authgrpId", nativeQuery = true)
	Optional<TcAuthGrp> findOneByAuthgrpNm(@Param("authgrpId") String authgrpId, @Param("authgrpNm") String authgrpNm);

}
