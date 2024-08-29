package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIdstryFacInformalPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsIdstryFacInformalPopRepository extends JpaRepository<TsIdstryFacInformalPop, String>{

	List<TsIdstryFacInformalPop> findAllByPopmngId(String popmngId);

}