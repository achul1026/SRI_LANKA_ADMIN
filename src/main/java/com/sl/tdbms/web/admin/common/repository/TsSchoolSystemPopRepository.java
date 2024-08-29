package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsSchoolSystemPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsSchoolSystemPopRepository extends JpaRepository<TsSchoolSystemPop, String>{

	List<TsSchoolSystemPop> findAllByPopmngId(String popmngId);

}