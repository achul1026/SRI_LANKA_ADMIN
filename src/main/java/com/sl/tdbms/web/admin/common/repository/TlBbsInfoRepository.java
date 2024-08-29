package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TlBbsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TlBbsInfoRepository extends JpaRepository<TlBbsInfo, String>{

}
