package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsHouseHoldPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsHouseHoldPopRepository extends JpaRepository<TsHouseHoldPop, String>{

	List<TsHouseHoldPop> findAllByPopmngId(String popmngId);

}