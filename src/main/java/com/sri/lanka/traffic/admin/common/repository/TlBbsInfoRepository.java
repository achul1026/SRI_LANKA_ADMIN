package com.sri.lanka.traffic.admin.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sri.lanka.traffic.admin.common.entity.TlBbsInfo;

@Repository
public interface TlBbsInfoRepository extends JpaRepository<TlBbsInfo, String>{

}
