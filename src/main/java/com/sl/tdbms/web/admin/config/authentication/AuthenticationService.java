package com.sl.tdbms.web.admin.config.authentication;

import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private QTcUserMngRepository qTcUserMngRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    	LoginMngrDTO result = qTcUserMngRepository.getTcUserInfoByUserId(username);

        AuthenticationEntity authenticationEntity = null;

        if(!CommonUtils.isNull(result) && !CommonUtils.isNull(result.getUserPswd())){
        	if(!AthrztSttsCd.APPROVAL.equals(result.getAthrztStts())) {
        		throw new CommonException(ErrorCode.MNGR_STTS_NOT_NORMAL);
        	}
        	authenticationEntity = new AuthenticationEntity(result);
        	authenticationEntity.setUserId(result.getUserId());
        	authenticationEntity.setUserAuth(result.getUserAuth());
        	authenticationEntity.setUserPswd(result.getUserPswd());
        	authenticationEntity.setUserBffltd(result.getUserBffltd());
        	authenticationEntity.setResetpswdYn(result.getResetpswdYn());
        	
        }else{
            throw new UsernameNotFoundException(username);
        }

    	return authenticationEntity;
    }
}
