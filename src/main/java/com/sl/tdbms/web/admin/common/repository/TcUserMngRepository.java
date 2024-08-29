package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcUserMng;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;

public interface TcUserMngRepository extends JpaRepository<TcUserMng, String>{
	
	/**
	  * @Method Name : findOneByUserId
	  * @작성일 : 2023. 12. 27.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 관리자 조회 By userId
	  * @param userId
	  * @return
	  */
	TcUserMng findOneByUserId(String userId);

	/**
	  * @Method Name : findOneByUsermngIdAndAthrztSttsCd
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 관리자 정보 조회 (아이디 / 상태)
	  * @param usermngId
	  * @param mngrSttsCd
	  * @return
	  */
	TcUserMng findOneByUsermngIdAndAthrztStts(String usermngId, AthrztSttsCd athrztSttsCd);

	/**
	  * @Method Name : findByUserEmail
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 관리자 정보 조회 ( 이메일 )
	  * @param userEmail
	  * @return
	  */
	TcUserMng findByUserEmail(String userEmail);

	/**
	  * @Method Name : existsByUserId
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 관리자 존재 유무 확인
	  * @param object
	  * @return
	  */
	Boolean existsByUserId(Object object);
	
	/**
	  * @Method Name : findAllByAuthgrpId
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 관리자 목록 조회 ( 권한 pk )
	  * @param AuthgrpId
	  * @return
	  */
	List<TcUserMng> findAllByAuthgrpId(String AuthgrpId);

}
