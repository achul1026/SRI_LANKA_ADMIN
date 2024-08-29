package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcGnMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcGnMngRepository extends JpaRepository<TcGnMng, String>{
	
	/**
	  * @Method Name : findAllByOrderByGnId
	  * @작성일 : 2024. 6. 11.
	  * @작성자 : KY.LEE
	  * @Method 설명 : Gn 전체 조회
	  * @return List<TcGnMng> 
	  */
	 List<TcGnMng> findAllByUseYnOrderByGnId(String useYn);
}
