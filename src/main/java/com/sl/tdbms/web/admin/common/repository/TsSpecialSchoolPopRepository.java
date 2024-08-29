package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsSpecialSchoolPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsSpecialSchoolPopRepository extends JpaRepository<TsSpecialSchoolPop, String>{

	List<TsSpecialSchoolPop> findAllByPopmngId(String popmngId);

}
