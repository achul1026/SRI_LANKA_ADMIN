package com.sri.lanka.traffic.admin.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TlBbsFileGrp;

public interface TlBbsFileGrpRepository extends JpaRepository<TlBbsFileGrp, String>{

	Optional<TlBbsFileGrp> findByBbsId(String bbsId);

	void save(Optional<TlBbsFileGrp> tlBbsFileGrp);

}
