package com.sl.tdbms.web.admin.common.repository;

import java.util.List;
import java.util.Optional;

import com.sl.tdbms.web.admin.common.entity.TlBbsFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TlBbsFileRepository extends JpaRepository<TlBbsFile, String>{

	List<TlBbsFile> findByFilegrpId(String filegrpId);

	void deleteByFilegrpId(String filegrpId);

	boolean countByFilegrpId(String filegrpId);

	Optional<TlBbsFile> findOneByFilegrpId(String filegrpId);

}
