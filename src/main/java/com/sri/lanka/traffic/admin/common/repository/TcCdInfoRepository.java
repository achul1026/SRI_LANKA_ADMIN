package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;

public interface TcCdInfoRepository extends JpaRepository<TcCdInfo, String>{

	void deleteAllByGrpcdId(String grpId);

	TcCdInfo findByCd(String bffltdCd);

	TcCdInfo findByCdNm(String type);

}
