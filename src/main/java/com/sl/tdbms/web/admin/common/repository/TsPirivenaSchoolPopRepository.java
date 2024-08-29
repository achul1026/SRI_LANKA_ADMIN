package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsPirivenaSchoolPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsPirivenaSchoolPopRepository extends JpaRepository<TsPirivenaSchoolPop, String>{

	List<TsPirivenaSchoolPop> findAllByPopmngId(String popmngId);

}