package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyEducationPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsFacultyEducationPopRepository extends JpaRepository<TsFacultyEducationPop, String>{

	List<TsFacultyEducationPop> findAllByPopmngId(String popmngId);

}