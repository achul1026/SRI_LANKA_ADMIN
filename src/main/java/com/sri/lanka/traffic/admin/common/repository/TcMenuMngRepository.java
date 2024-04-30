package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcMenuMng;

public interface TcMenuMngRepository extends JpaRepository<TcMenuMng, String>{

	/**
	  * @Method Name : findByUppermenuCdOrderByMenuSqno
	  * @작성일 : 2024. 1. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 정보 조회
	  * @param uppermenuCd
	  * @return
	  */
	List<TcMenuMng> findByUppermenuCdOrderByMenuSqno(String uppermenuCd);
	
	/**
	  * @Method Name : findAllByUppermenuCdOrMenuCd
	  * @작성일 : 2024. 1. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 목록 조회
	  * @param uppermenuCd
	  * @param menuCd
	  * @return
	  */
	List<TcMenuMng> findAllByUppermenuCdOrMenuCd(String uppermenuCd, String menuCd);

	/**
	  * @Method Name : findAllByBscmenuYn
	  * @작성일 : 2024. 1. 29.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 기본 설정 메뉴 목록 조회
	  * @param bscmenuYn
	  * @return
	  */
	List<TcMenuMng> findAllByBscmenuYn(String bscmenuYn);

	/**
	 * @Method Name : findByMenuUrlpttrn
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 현재 URL 메뉴 조회
	 * @param menuUrlpttrn
	 * @return
	 */
	TcMenuMng findByMenuUrlpttrn(String menuUrlpttrn);

	/**
	  * @Method Name : findAllByUppermenuCd
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 하위 메뉴 목록 조회
	  * @param menuCd
	  * @return
	  */
	List<TcMenuMng> findAllByUppermenuCd(String menuCd);

}
