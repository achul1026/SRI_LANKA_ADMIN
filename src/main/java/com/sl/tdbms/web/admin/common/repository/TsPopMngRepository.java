package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsPopMngRepository extends JpaRepository<TsPopMng, String>{

	/**
	 * @Method :findOneByPopmngId
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @param popmngId
	 * @return
	 */
	TsPopMng findOneByPopmngId(String popmngId);
}
