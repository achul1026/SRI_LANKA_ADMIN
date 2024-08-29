package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsHousingTypePop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsHousingTypePopRepository extends JpaRepository<TsHousingTypePop, String>{

	List<TsHousingTypePop> findAllByPopmngId(String popmngId);

}