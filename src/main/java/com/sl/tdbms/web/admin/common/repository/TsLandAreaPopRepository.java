package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsLandAreaPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsLandAreaPopRepository extends JpaRepository<TsLandAreaPop, String>{

	List<TsLandAreaPop> findAllByPopmngId(String popmngId);

}