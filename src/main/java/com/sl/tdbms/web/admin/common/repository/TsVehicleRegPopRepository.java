package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsVehicleRegPop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsVehicleRegPopRepository extends JpaRepository<TsVehicleRegPop, String>{

	List<TsVehicleRegPop> findAllByPopmngId(String popmngId);

}
