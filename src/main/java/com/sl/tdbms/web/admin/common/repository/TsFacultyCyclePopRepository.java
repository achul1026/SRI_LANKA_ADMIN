package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyCyclePop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsFacultyCyclePopRepository extends JpaRepository<TsFacultyCyclePop, String>{

	List<TsFacultyCyclePop> findAllByPopmngId(String popmngId);

}