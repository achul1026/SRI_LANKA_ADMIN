package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsGrossRegnProdPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsGrossRegnProdPopRepository extends JpaRepository<TsGrossRegnProdPop, String>{

	List<TsGrossRegnProdPop> findAllByPopmngId(String popmngId);

}