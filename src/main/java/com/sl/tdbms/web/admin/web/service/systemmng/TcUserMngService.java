package com.sl.tdbms.web.admin.web.service.systemmng;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.component.MsgComponent;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngSaveDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngSignUpDTO;
import com.sl.tdbms.web.admin.common.entity.MsgQueue;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.TcUserMng;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TcUserMngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.enums.MsgTemplateType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;


@Service
public class TcUserMngService {
	
	@Autowired
	private TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	private TcCdInfoRepository tcCdInfoRepository;
	
	@Autowired
	private TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
	private AuthRqstMngService authRqstMngService;
	
	@Value("${srilanka.auth.role}")
	public String AUTH_ROLE;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    MsgComponent msgComponent;
	
	public void saveTcUserMng(TcUserMngSignUpDTO tcUserMngDTO) throws CommonException {
		String resMsg;
		try {
			TcUserMng tcUserMng = new TcUserMng(tcUserMngDTO);
			// 일반 관리자 권한 조회 및 적용
			Optional<TcAuthGrp> tcAuthMng = tcAuthGrpRepository.findByBffltdCdAndBscauthYn(tcUserMng.getUserBffltd(), "Y");
			if (tcAuthMng.isPresent()) {
				tcUserMng.setAuthgrpId(tcAuthMng.get().getAuthgrpId());
			} else {
				String bffltdNm = tcCdInfoRepository.findOneByCd(tcUserMng.getUserBffltd()).getCdnmEng();
				
				TcAuthGrp newTcAuthGrp = new TcAuthGrp();
				newTcAuthGrp.setAuthgrpNm(bffltdNm + " - General Authority");
				newTcAuthGrp.setAuthgrpDescr(
						"General Authority"
//						CommonUtils.getMessage("mngr.general.manager.descript")
						);
				newTcAuthGrp.setBffltdCd(tcUserMng.getUserBffltd());
				newTcAuthGrp.setBscauthYn("Y");
				authRqstMngService.saveAuthGrp(newTcAuthGrp);
				tcUserMng.setAuthgrpId(newTcAuthGrp.getAuthgrpId());
			}
			
			String mngrId = CommonUtils.getUuid();
			tcUserMng.setUsermngId(mngrId);
			
			if(CommonUtils.isNull(tcUserMng.getUserPswd())) {
				resMsg = CommonUtils.getMessage("login.join.fail.notExist.password");
				throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
			}
			
			//비밀번호 spring security bcrypt 방식 
			String mngrAccountPwd = passwordEncoder.encode(tcUserMng.getUserPswd());
			
			tcUserMng.setUserPswd(mngrAccountPwd);
			tcUserMng.setUserAuth(AUTH_ROLE);
			tcUserMng.setUserType(UserTypeCd.GENERAL);
			tcUserMng.setAthrztStts(AthrztSttsCd.NOT_APPROVED);
			tcUserMng.setRegistDt(LocalDateTime.now());
			
			tcUserMngRepository.save(tcUserMng);
			
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("login.join.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

	/**
	  * @Method Name : setMngrInfo
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 입력한 관리자 정보 설정
	  * @param tcUserMngSaveDTO
	  * @return
	  */
	public void setTcUserMngInfo(TcUserMngSaveDTO tcUserMngSaveDTO) throws CommonException {
		String resMsg = CommonUtils.getMessage("mngr.setTcUserMngInfo.fail");
		String userId = LoginMngrUtils.getUserId();
		try {
			TcUserMng newTcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngSaveDTO.getUserId());
			if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserTel())) newTcUserMng.setUserTel(tcUserMngSaveDTO.getUserTel()); 
			if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserEmail())) {
				if (!validateEmail(tcUserMngSaveDTO.getUserEmail())) {
					resMsg = CommonUtils.getMessage("login.setUserInfo.validation.email.fail");
					throw new CommonException(ErrorCode.VALIDATION_FAILED, resMsg);
				}
				newTcUserMng.setUserEmail(tcUserMngSaveDTO.getUserEmail()); 
			}
//		if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserBffltd())) newTcUserMng.setUserBffltd(tcUserMngSaveDTO.getUserBffltd()); 
			if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserDept())) newTcUserMng.setUserDept(tcUserMngSaveDTO.getUserDept()); 
			if (!CommonUtils.isNull(tcUserMngSaveDTO.getAthrztSttsCd())) newTcUserMng.setAthrztStts(AthrztSttsCd.getEnums(tcUserMngSaveDTO.getAthrztSttsCd())); 
			if (!CommonUtils.isNull(tcUserMngSaveDTO.getAuthgrpId())) newTcUserMng.setAuthgrpId(tcUserMngSaveDTO.getAuthgrpId()); 
			newTcUserMng.setUpdtId(userId);
			
			tcUserMngRepository.save(newTcUserMng);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

	/**
	  * @Method Name : changeMngrAccountPwd
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 검증 및 새로운 비밀번호 설정
	  * @param tcUserMngSaveDTO
	  * @return
	  */
	public void changeTcUserMngAccountPwd(TcUserMngSaveDTO tcUserMngSaveDTO) throws CommonException {
		String resMsg =  CommonUtils.getMessage("mngr.changeTcUserMngAccountPwd.fail");
		try {
			if (!validatePassword(tcUserMngSaveDTO.getUserPswd())) {
				resMsg = CommonUtils.getMessage("login.setUserInfo.validation.password.fail");
				throw new CommonException(ErrorCode.VALIDATION_FAILED, resMsg);
			}
			
			String userId = LoginMngrUtils.getUserId();
			
			TcUserMng tcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngSaveDTO.getUserId());
			if (!passwordEncoder.matches(tcUserMngSaveDTO.getUserPswd(), tcUserMng.getUserPswd())) {
				resMsg = CommonUtils.getMessage("mngr.mngrPwUpdate.mismatch");
				throw new CommonException(ErrorCode.VALIDATION_FAILED, resMsg);
			}
			String newMngrAccountPwd = passwordEncoder.encode(tcUserMngSaveDTO.getNewUserPswd());
			
			tcUserMng.setUserPswd(newMngrAccountPwd);
			tcUserMng.setUpdtId(userId);
			
			tcUserMngRepository.save(tcUserMng);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
		
	}
	
	/**
	  * @Method Name : resetUserPswd
	  * @작성일 : 2024. 5. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 찾기 시 임시비밀번호 발급
	  * @param tcUserMngDTO
	  * @throws CommonException
	  */
	@Transactional
	public void resetUserPswd(TcUserMngDTO tcUserMngDTO) throws CommonException {
		String resMsg = CommonUtils.getMessage("login.loginPwFindInfo.fail");
		try {
			TcUserMng newTcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngDTO.getUserId());
			if (CommonUtils.isNull(newTcUserMng) || !newTcUserMng.getUserEmail().equals(tcUserMngDTO.getUserEmail())
					|| !newTcUserMng.getUserId().equals(tcUserMngDTO.getUserId())) {
				resMsg = CommonUtils.getMessage("login.loginIdFindInfo.dismatch.id");
				throw new CommonResponseException(ErrorCode.EMPTY_DATA, resMsg);
			}
			String tempPswd = CommonUtils.getTempPassword(8);
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("name", newTcUserMng.getUserNm());
			paramMap.put("id", CommonUtils.getMaskString(newTcUserMng.getUserId(), 6));
			paramMap.put("tempPswd", tempPswd);
			
			ResourceBundle bundle = ResourceBundle.getBundle("messages.email.findPassword", LocaleContextHolder.getLocale());
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = bundle.getString(key);
				paramMap.put(key, value);
			}
			
			MsgQueue msgQueue = new MsgQueue();
			msgQueue.setReceiver(newTcUserMng.getUserEmail());
			
			msgComponent.saveMsgQueue(msgQueue, MsgTemplateType.FIND_PASSWORD, paramMap);
			
			String mngrAccountPwd = passwordEncoder.encode(tempPswd);
			
			newTcUserMng.setUserPswd(mngrAccountPwd);
			newTcUserMng.setResetpswdYn("Y");
			tcUserMngRepository.save(newTcUserMng);
		} catch (Exception e) {
			throw new CommonResponseException(ErrorCode.EMPTY_DATA, resMsg);
		}
	}

	/**
	  * @Method Name : changUserPswd
	  * @작성일 : 2024. 5. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 변경
	  * @param userPswd
	  * @throws Exception
	  */
	public void changUserPswd(String userPswd) throws CommonException {
		String resMsg = CommonUtils.getMessage("login.loginPwChange.fail");
		try {
			if (!validatePassword(userPswd)) {
				resMsg = CommonUtils.getMessage("login.setUserInfo.validation.password.fail");
				throw new CommonException(ErrorCode.VALIDATION_FAILED, resMsg);
			}
			
			TcUserMng tcUserMng = tcUserMngRepository.findOneByUserId(LoginMngrUtils.getUserId());
			String newMngrAccountPwd = passwordEncoder.encode(userPswd);
			tcUserMng.setUserPswd(newMngrAccountPwd);
			tcUserMng.setResetpswdYn("N");
			
			tcUserMngRepository.save(tcUserMng);
		} catch (Exception e) {
			throw new CommonResponseException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}

    /**
      * @Method Name : validateEmail
      * @작성일 : 2024. 5. 23.
      * @작성자 : SM.KIM
      * @Method 설명 : 이메일 유효성 검사
      * @param email
      * @return
      */
	public boolean validateEmail(String email) {
	    // 이메일 형식
	    Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}

    /**
      * @Method Name : validatePassword
      * @작성일 : 2024. 5. 23.
      * @작성자 : SM.KIM
      * @Method 설명 : 비밀번호 유효성 검사
      * @param password
      * @return
      */
    public boolean validatePassword(String password) {
        // 숫자, 영문, 특수문자를 포함한 8자 이상 20이하
    	Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*]).{8,20}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    /**
      * @Method Name : accountApproval
      * @작성일 : 2024. 5. 24.
      * @작성자 : SM.KIM
      * @Method 설명 : 계정 승인
      * @param tcUserMngSaveDTO
      * @throws CommonException
      */
    @Transactional
	public void accountApproval(TcUserMngSaveDTO tcUserMngSaveDTO) {
    	String resMsg = CommonUtils.getMessage("mngr.accountApproval.fail");
    	try {
    		TcUserMng tcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngSaveDTO.getUserId());
    		tcUserMng.setAthrztStts(AthrztSttsCd.APPROVAL);
    		tcUserMngRepository.save(tcUserMng);
    		
    		Map<String,Object> paramMap = new HashMap<>();
    		paramMap.put("name", tcUserMng.getUserNm());
    		paramMap.put("id", CommonUtils.getMaskString(tcUserMng.getUserId(), 6));
    		paramMap.put("aprvDT", CommonUtils.formatLocalDateTime(LocalDateTime.now()));
    		
    		ResourceBundle bundle = ResourceBundle.getBundle("messages.email.joinComplete", LocaleContextHolder.getLocale());
    		Enumeration<String> keys = bundle.getKeys();
    		while (keys.hasMoreElements()) {
    			String key = keys.nextElement();
    			String value = bundle.getString(key);
    			paramMap.put(key, value);
    		}
    		
    		MsgQueue msgQueue = new MsgQueue();
    		msgQueue.setReceiver(tcUserMng.getUserEmail());
    		
    		msgComponent.saveMsgQueue(msgQueue, MsgTemplateType.JOIN_COMPLETE, paramMap);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.UNKNOWN_ERROR, resMsg);
		}
	}
}
