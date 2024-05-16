package com.sri.lanka.traffic.admin.config.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sri.lanka.traffic.admin.common.dto.common.LoginMngrDTO;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;

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
        	
        }else{
            throw new UsernameNotFoundException(username);
        }

    	return authenticationEntity;
    }
}
