package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsPrivateSchoolPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsPrivateSchoolPopRepository extends JpaRepository<TsPrivateSchoolPop, String>{

	List<TsPrivateSchoolPop> findAllByPopmngId(String popmngId);

}
