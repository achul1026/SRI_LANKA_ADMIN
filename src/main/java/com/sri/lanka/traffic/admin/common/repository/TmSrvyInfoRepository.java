package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TmSrvyInfo;

public interface TmSrvyInfoRepository extends JpaRepository<TmSrvyInfo, String>{

	void deleteAllBySrvyId(String srvyId);

}
