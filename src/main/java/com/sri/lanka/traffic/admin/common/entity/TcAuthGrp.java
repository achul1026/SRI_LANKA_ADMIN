package com.sri.lanka.traffic.admin.common.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //권한 그룹 관리
@EqualsAndHashCode(callSuper=true)
public class TcAuthGrp extends BaseEntity {

    @Id
    private String authgrpId = CommonUtils.getUuid(); //권한 그룹 아이디

    private String authgrpNm; //권한 그룹 명
    
    private String authgrpDescr; //권한 그룹 설명
    
    private String bffltdCd; //소속 코드
    
    private String bscauth_yn; //기본 권한 여부

    @OneToMany(mappedBy = "tcAuthGrpInfo" ,fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonManagedReference
    private List<TcMenuAuth> tcMenuAuthList;

}   
