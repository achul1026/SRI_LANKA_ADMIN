package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sri.lanka.traffic.admin.common.entity.TcAuthGrp;

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

	Optional<TcAuthGrp> findByAuthgrpNmAndBffltdCd(String name, String userBffltd);

	/**
	  * @Method Name : findAllByBscauthYnNotYN
	  * @작성일 : 2024. 5. 10.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 조회 by 기본 권한 여부
	  * @param bscauthYn
	  * @return
	  */
	@Query(value = "SELECT * FROM srlk.tc_auth_grp WHERE bscauth_yn <> :bscauthYn", nativeQuery = true)
	List<TcAuthGrp> findAllByBscauthYnNotYN(@Param("bscauthYn") String bscauthYn);

}
