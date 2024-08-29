package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsOccHousingUnitPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsOccHousingUnitPopRepository extends JpaRepository<TsOccHousingUnitPop, String>{

	List<TsOccHousingUnitPop> findAllByPopmngId(String popmngId);
	
}