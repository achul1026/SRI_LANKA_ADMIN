package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsNationalSchoolPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsNationalSchoolPopRepository extends JpaRepository<TsNationalSchoolPop, String>{

	List<TsNationalSchoolPop> findAllByPopmngId(String popmngId);

}