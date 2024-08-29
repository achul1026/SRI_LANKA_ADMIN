package com.sl.tdbms.web.admin.common.repository;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.entity.TmApiKeysrvc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmApiKeysrvcRepository extends JpaRepository<TmApiKeysrvc, String>{

	@Transactional
	void deleteBySrvcIdAndCertkeyId(String srvcId, String certkeyId);

	TmApiKeysrvc findBySrvcIdAndCertkeyId(String srvcId, String certkeyId);
}
