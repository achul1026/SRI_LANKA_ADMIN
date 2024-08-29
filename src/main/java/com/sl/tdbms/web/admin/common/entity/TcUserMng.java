package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngSignUpDTO;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //사용자 관리
@EqualsAndHashCode(callSuper=true)
public class TcUserMng extends BaseEntity{

    @Id
    @Column(length = 32, nullable = false)
    private String usermngId; //사용자 관리 아이디
    
    @Column(length = 32)
    private String authgrpId; //권한 아이디
    
    @Column(length = 32, nullable = false)
//	@NotBlank(message="validation.error.required")
//	@Size(min = 6, max = 13, message = "validation.error.size")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "validation.error.pattern.id")
//	@CustomPatternValidation(value = ValidationPattern.USERID, message = "validation.error.pattern.id")
    private String userId; //사용자 아이디

    @Column(length = 255, nullable = false)
//	@NotBlank(message="validation.error.required")
//	@Size(min = 8, max = 64, message = "validation.error.size")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$", message = "validation.error.pattern.password")
    private String userPswd; //사용자 비밀번호

    @Column(length = 32, nullable = false)
    private String userAuth; //사용자 권한

    @Column(length = 50)
//	@NotBlank(message="validation.error.required")
//	@Size(min = 4, max = 30, message = "validation.error.size")
//    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "validation.error.pattern.name")
//	@CustomPatternValidation(value = ValidationPattern.NAME, message = "validation.error.pattern.name")
    private String userNm; //사용자 명

    @Column(length = 20)
//	@NotBlank(message="validation.error.required")
//	@Size(min = 10, max = 10, message = "validation.error.size")
//    @Pattern(regexp = "^07\\d{8}$", message = "validation.error.pattern.tel")
//	@CustomPatternValidation(value = ValidationPattern.TEL, message = "validation.error.pattern.tel")
    private String userTel; //사용자 전화번호

    @Column(length = 32)
//	@NotBlank(message="validation.error.required")
//	@Size(min = 12, max = 30, message = "validation.error.size")
//    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", message = "validation.error.pattern.email")
//	@CustomPatternValidation(value = ValidationPattern.EMAIL, message = "validation.error.pattern.email")
    private String userEmail; //사용자 이메일

    @Column(length = 50)
//    @NotBlank(message="validation.error.required")
    private String userBffltd; //사용자 소속
    
    @Column(length = 50)
//    @NotBlank(message="validation.error.required")
    private String userDept; //사용자 부서

    @Column(length = 10)
	@Convert(converter = AthrztSttsCd.Converter.class)
    private AthrztSttsCd athrztStts; //승인 상태

    @Column(length = 10)
	@Convert(converter = UserTypeCd.Converter.class)
	private UserTypeCd userType; //사용자 유형
    
    @Column(length = 1)
    private String resetpswdYn = "N"; //비밀번호 초기화 여부
    
    // 기본 생성자
    public TcUserMng() {
    }
    
    public TcUserMng(TcUserMngSignUpDTO tcUserMngDTO) {
		this.usermngId = tcUserMngDTO.getUsermngId();
		this.authgrpId = tcUserMngDTO.getAuthgrpId();
		this.userId = tcUserMngDTO.getUserId();
		this.userPswd = tcUserMngDTO.getUserPswd();
		this.userBffltd = tcUserMngDTO.getUserBffltd();
		this.userDept = tcUserMngDTO.getUserDept();
		this.userEmail = tcUserMngDTO.getUserEmail();
		this.userType = tcUserMngDTO.getUserType();
		this.athrztStts = tcUserMngDTO.getAthrztStts();
		this.userNm = tcUserMngDTO.getUserNm();
		this.userAuth = tcUserMngDTO.getUserAuth();
		this.userTel = tcUserMngDTO.getUserTel();
	}

}
