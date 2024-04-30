package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcCdGrp;

public interface TcCdGrpRepository extends JpaRepository<TcCdGrp, String>{

	TcCdGrp findOneByGrpCd(String grpCd);

}
