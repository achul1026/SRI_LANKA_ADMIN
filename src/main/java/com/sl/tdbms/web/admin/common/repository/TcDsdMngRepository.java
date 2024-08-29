package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcDsdMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcDsdMngRepository extends JpaRepository<TcDsdMng, String>{

	/**
	  * @Method Name : findAllOrderByDsdId
	  * @작성일 : 2024. 6. 11.
	  * @작성자 : KY.LEE
	  * @Method 설명 : DSD 전체 조회
	  * @return List<TcDsdMng> 
	  */
	List<TcDsdMng> findAllByUseYnOrderByDsdId(String useYn);

}
