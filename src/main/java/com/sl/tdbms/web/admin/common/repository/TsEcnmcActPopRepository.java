package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsEcnmcActPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsEcnmcActPopRepository extends JpaRepository<TsEcnmcActPop, String>{

	List<TsEcnmcActPop> findAllByPopmngId(String popmngId);
}