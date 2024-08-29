package com.sl.tdbms.web.admin.common.dto.mngr;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.sl.tdbms.web.admin.common.dto.common.CommonDTO;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
@Data
@EqualsAndHashCode(callSuper=true)
public class TcUserMngSignUpDTO extends CommonDTO {
	
	private String usermngId;
	
	private String authgrpId;
	
	@NotBlank(message="validation.error.required")
	@Size(min = 6, max = 13, message = "validation.error.size")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "validation.error.pattern.id")
	private String userId;
	
	@NotBlank(message="validation.error.required")
	@Size(min = 8, max = 64, message = "validation.error.size")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$", message = "validation.error.pattern.password")
	private String userPswd;
	
	@NotBlank(message="validation.error.required")
	private String userBffltd;
	 
	@NotBlank(message="validation.error.required")
	private String userDept;
	 
	@NotBlank(message="validation.error.required")
	@Size(min = 12, max = 30, message = "validation.error.size")
    @Pattern(regexp = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "validation.error.pattern.email")
	private String userEmail;
	
	private UserTypeCd userType;
	
	private AthrztSttsCd athrztStts;
	
	@NotBlank(message="validation.error.required")
	@Size(min = 4, max = 30, message = "validation.error.size")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "validation.error.pattern.name")
	private String userNm;
	
	private String userAuth;
	
	@NotBlank(message="validation.error.required")
	@Size(min = 10, max = 10, message = "validation.error.size")
    @Pattern(regexp = "^0\\d{9}$", message = "validation.error.pattern.tel")
	private String userTel;
	
	private String authgrpNm;
	
	private String bffltdNm;
	
	private String deptNm;
	
}
