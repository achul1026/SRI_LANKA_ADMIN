package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TcDsdarMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcDsdarMngRepository extends JpaRepository<TcDsdarMng, String>{

	void deleteAllByDsdId(String dsdId);

}
