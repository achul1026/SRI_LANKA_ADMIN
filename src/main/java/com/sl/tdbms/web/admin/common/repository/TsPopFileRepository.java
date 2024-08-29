package com.sl.tdbms.web.admin.common.repository;

import com.sl.tdbms.web.admin.common.entity.TsPopFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TsPopFileRepository extends JpaRepository<TsPopFile, String>{

	/**
	 * @Method findOneByFileId
	 * @Method 설명 : 본인키 단건 조회
	 * @param fileId
	 * @return TsPopFile
	 */
	TsPopFile findOneByFileId(String fileId);
}
