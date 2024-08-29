package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIdstryFacFormalPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsIdstryFacFormalPopRepository extends JpaRepository<TsIdstryFacFormalPop, String>{

	List<TsIdstryFacFormalPop> findAllByPopmngId(String popmngId);

}