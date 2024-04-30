package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sri.lanka.traffic.admin.common.enums.ValidationPattern;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;
import com.sri.lanka.traffic.admin.common.validation.CustomPatternValidation;

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
	@NotBlank(message="validation.error.required")
	@Size(min = 6, max = 12, message = "validation.error.size")
	@CustomPatternValidation(value = ValidationPattern.USERID, message = "validation.error.pattern.id")
    private String userId; //사용자 아이디

    @Column(length = 255, nullable = false)
	@NotBlank(message="validation.error.required")
	@Size(min = 8, max = 64, message = "validation.error.size")
    private String userPswd; //사용자 비밀번호

    @Column(length = 32, nullable = false)
    private String userAuth; //사용자 권한

    @Column(length = 50)
	@NotBlank(message="validation.error.required")
	@Size(min = 4, max = 30, message = "validation.error.size")
	@CustomPatternValidation(value = ValidationPattern.NAME, message = "validation.error.pattern.name")
    private String userNm; //사용자 명

    @Column(length = 20)
	@NotBlank(message="validation.error.required")
//	@Size(min = 4, max = 30, message = "validation.error.size") TODO 연락처 사이즈 및 패턴 확인 필요
//	@CustomPatternValidation(value = ValidationPattern.TEL, message = "validation.error.pattern.tel")
    private String userTel; //사용자 전화번호

    @Column(length = 32)
	@NotBlank(message="validation.error.required")
	@Size(min = 12, max = 30, message = "validation.error.size")
	@CustomPatternValidation(value = ValidationPattern.EMAIL, message = "validation.error.pattern.email")
    private String userEmail; //사용자 이메일

    @Column(length = 50)
    private String userBffltd; //사용자 소속
    
    @Column(length = 50)
    private String userDept; //사용자 부서

    @Column(length = 10)
	@Convert(converter = AthrztSttsCd.Converter.class)
    private AthrztSttsCd athrztStts; //승인 상태

    @Column(length = 10)
	@Convert(converter = UserTypeCd.Converter.class)
	private UserTypeCd userType; //사용자 유형

}
