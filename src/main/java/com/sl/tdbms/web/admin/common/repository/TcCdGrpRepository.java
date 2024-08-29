package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TcCdGrp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcCdGrpRepository extends JpaRepository<TcCdGrp, String>{

	TcCdGrp findOneByGrpCd(String grpCd);

}
