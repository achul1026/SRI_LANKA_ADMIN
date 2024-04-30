package com.sri.lanka.traffic.admin.web.service.systemmng;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.entity.TmAuthRqst;
import com.sri.lanka.traffic.admin.common.enums.code.RqstSttsCd;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmAuthRqstRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;


@Service
public class AuthRqstMngService {
	
	@Autowired
	TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	TmAuthRqstRepository tmAuthRqstRepository;
	
	/**
	  * @Method Name : setUserAuth
	  * @작성일 : 2024. 4. 18.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 승인 또는 반려
	  * @param authId
	  * @param authrqstId
	  * @param isApproval
	  */
	@Transactional
	public void setUserAuth(String authId, String userId, String authrqstId, boolean isApproval) {
		try {
			if (isApproval) {
				TcUserMng userMng = tcUserMngRepository.findOneByUserId(userId);
				userMng.setAuthId(authId);
				tcUserMngRepository.save(userMng);
			}
			
			TmAuthRqst tmAuthRqst = tmAuthRqstRepository.findById(authrqstId).get();
			
			RqstSttsCd status = isApproval ? RqstSttsCd.APPROVAL : RqstSttsCd.REJECT;
			tmAuthRqst.setRqstStts(status);
			tmAuthRqst.setUserId(LoginMngrUtils.getUserId());
			tmAuthRqst.setAthrztDt(LocalDateTime.now());
			tmAuthRqstRepository.save(tmAuthRqst);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "User Authority save failed");
		}
	}
	
}
