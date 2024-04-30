package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcMenuAuth;

public interface TcMenuAuthRepository extends JpaRepository<TcMenuAuth, String>{
	
	/**
	  * @Method Name : deleteAllByAuthId
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 삭제
	  * @param AuthId
	  */
	void deleteAllByAuthId(String AuthId);

}
