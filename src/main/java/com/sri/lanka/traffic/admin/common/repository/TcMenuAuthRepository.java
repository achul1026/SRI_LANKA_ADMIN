package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcMenuAuth;

public interface TcMenuAuthRepository extends JpaRepository<TcMenuAuth, String>{
	
	/**
	  * @Method Name : deleteAllByAuthgrpId
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 권한 삭제 by authgrpId
	  * @param authgrpId
	  */
	void deleteAllByAuthgrpId(String authgrpId);

	/**
	  * @Method Name : deleteAllByMenuId
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 메뉴 권한 삭제 by menuId
	  * @param menuId
	  * @return
	  */
	void deleteAllByMenuId(String menuId);

}
