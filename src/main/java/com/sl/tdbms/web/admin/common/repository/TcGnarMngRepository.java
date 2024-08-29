package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TcGnarMng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcGnarMngRepository extends JpaRepository<TcGnarMng, Integer>{

	void deleteAllByDstrctCd(String dstrctCd);

}
