package com.sri.lanka.traffic.admin.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.lanka.traffic.admin.common.entity.TlBbsFile;

public interface TlBbsFileRepository extends JpaRepository<TlBbsFile, String>{

	List<TlBbsFile> findByFilegrpId(String filegrpId);

	void deleteByFilegrpId(String filegrpId);

	boolean countByFilegrpId(String filegrpId);

	Optional<TlBbsFile> findOneByFilegrpId(String filegrpId);

}
