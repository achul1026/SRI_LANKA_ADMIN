package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsAgePop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsAgePopRepository extends JpaRepository<TsAgePop, String>{

	List<TsAgePop> findAllByPopmngId(String popmngId);
}