package com.sl.tdbms.web.admin.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sl.tdbms.web.admin.common.entity.TlMvmneqLog;

public interface TlMvmneqLogRepository extends JpaRepository<TlMvmneqLog, String>{

	void deleteAllByInstllcId(String instllcId);

	List<TlMvmneqLog> findAllByInstllcIdOrderByClctDt(String instllcId);

}
