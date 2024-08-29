package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsStdntPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsStdntPopRepository extends JpaRepository<TsStdntPop, String>{

	List<TsStdntPop> findAllByPopmngId(String popmngId);

}