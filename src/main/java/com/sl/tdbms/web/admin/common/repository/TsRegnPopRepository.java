package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsRegnPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsRegnPopRepository extends JpaRepository<TsRegnPop, String>{

	List<TsRegnPop> findAllByPopmngId(String popmngId);
}