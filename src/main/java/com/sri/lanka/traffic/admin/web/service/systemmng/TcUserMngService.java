package com.sri.lanka.traffic.admin.web.service.systemmng;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngSaveDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;
import com.sri.lanka.traffic.admin.common.repository.TcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;


@Service
public class TcUserMngService {
	
	@Autowired
	private TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	private TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
	private AuthRqstMngService authRqstMngService;
	
	@Value("${srilanka.auth.role}")
	public String AUTH_ROLE;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public void saveTcUserMng(TcUserMng tcUserMng) {
		try {
			// 일반 관리자 권한 조회 및 적용
			Optional<TcAuthGrp> tcAuthMng = tcAuthGrpRepository.findByAuthgrpNmAndBffltdCd(UserTypeCd.GENERAL.getName(), tcUserMng.getUserBffltd());
			if (tcAuthMng.isPresent()) {
				tcUserMng.setAuthgrpId(tcAuthMng.get().getAuthgrpId());
			} else {
				TcAuthGrp newTcAuthGrp = new TcAuthGrp();
				newTcAuthGrp.setAuthgrpNm(UserTypeCd.GENERAL.getName());
				newTcAuthGrp.setAuthgrpDescr("메뉴 권한 없음");
				newTcAuthGrp.setBffltdCd(tcUserMng.getUserBffltd());
				newTcAuthGrp.setBscauth_yn("Y");
				authRqstMngService.saveAuthGrp(newTcAuthGrp);
				tcUserMng.setAuthgrpId(newTcAuthGrp.getAuthgrpId());
			}
			
			String mngrId = CommonUtils.getUuid();
			tcUserMng.setUsermngId(mngrId);
			
			if(CommonUtils.isNull(tcUserMng.getUserPswd())) {
				throw new CommonException(ErrorCode.EMPTY_DATA, "Password does not exist.");
			}
			//비밀번호 spring security bcrypt 방식 
			String mngrAccountPwd = passwordEncoder.encode(tcUserMng.getUserPswd());
			
			tcUserMng.setUserPswd(mngrAccountPwd);
			tcUserMng.setUserAuth(AUTH_ROLE);
			tcUserMng.setUserType(UserTypeCd.GENERAL);
			tcUserMng.setAthrztStts(AthrztSttsCd.NOT_APPROVED);
			tcUserMng.setRegistDt(LocalDateTime.now());
			
			tcUserMngRepository.save(tcUserMng);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "User data save failed.");
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
	public TcUserMng setTcUserMngInfo(TcUserMngSaveDTO tcUserMngSaveDTO) {
		String userId = LoginMngrUtils.getUserId();
		TcUserMng newTcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngSaveDTO.getUserId());
		if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserTel())) newTcUserMng.setUserTel(tcUserMngSaveDTO.getUserTel()); 
		if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserEmail())) newTcUserMng.setUserEmail(tcUserMngSaveDTO.getUserEmail()); 
		if (!CommonUtils.isNull(tcUserMngSaveDTO.getUserBffltd())) newTcUserMng.setUserBffltd(tcUserMngSaveDTO.getUserBffltd()); 
		if (!CommonUtils.isNull(tcUserMngSaveDTO.getAthrztSttsCd())) newTcUserMng.setAthrztStts(AthrztSttsCd.getEnums(tcUserMngSaveDTO.getAthrztSttsCd())); 
		if (!CommonUtils.isNull(tcUserMngSaveDTO.getAuthgrpId())) newTcUserMng.setAuthgrpId(tcUserMngSaveDTO.getAuthgrpId()); 
		newTcUserMng.setUpdtId(userId);
		
		return newTcUserMng;
	}

	/**
	  * @Method Name : changeMngrAccountPwd
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 검증 및 새로운 비밀번호 설정
	  * @param tcUserMngSaveDTO
	  * @return
	  */
	public TcUserMng changeTcUserMngAccountPwd(TcUserMngSaveDTO tcUserMngSaveDTO) {
		String userId = LoginMngrUtils.getUserId();
		
		TcUserMng tcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngSaveDTO.getUserId());
		if (!passwordEncoder.matches(tcUserMngSaveDTO.getUserPswd(), tcUserMng.getUserPswd())) {
			//예외처리
			return null;
		}
		String newMngrAccountPwd = passwordEncoder.encode(tcUserMngSaveDTO.getNewUserPswd());
		
		tcUserMng.setUserPswd(newMngrAccountPwd);
		tcUserMng.setUpdtId(userId);
		
		return tcUserMng;
	}
	
}
