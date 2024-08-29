package com.sl.tdbms.web.admin.common.repository;

import java.util.Optional;

import com.sl.tdbms.web.admin.common.entity.TlBbsFileGrp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TlBbsFileGrpRepository extends JpaRepository<TlBbsFileGrp, String>{

	Optional<TlBbsFileGrp> findByBbsId(String bbsId);

	void save(Optional<TlBbsFileGrp> tlBbsFileGrp);

}
