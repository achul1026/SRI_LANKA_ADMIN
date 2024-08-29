package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyStudentsPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsFacultyStudentsPopRepository extends JpaRepository<TsFacultyStudentsPop, String>{

	List<TsFacultyStudentsPop> findAllByPopmngId(String popmngId);

}