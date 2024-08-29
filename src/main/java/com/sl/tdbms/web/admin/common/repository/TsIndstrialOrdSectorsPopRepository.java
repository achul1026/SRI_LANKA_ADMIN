package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIndstrialOrdSectorsPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsIndstrialOrdSectorsPopRepository extends JpaRepository<TsIndstrialOrdSectorsPop, String>{

	List<TsIndstrialOrdSectorsPop> findAllByPopmngId(String popmngId);

}