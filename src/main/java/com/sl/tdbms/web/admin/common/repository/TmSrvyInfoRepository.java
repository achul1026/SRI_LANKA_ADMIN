package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TmSrvyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmSrvyInfoRepository extends JpaRepository<TmSrvyInfo, String>{

	void deleteAllBySrvyId(String srvyId);

}
