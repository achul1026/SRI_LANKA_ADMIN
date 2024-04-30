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
@Data //권한 관리
@EqualsAndHashCode(callSuper=true)
public class TcAuthMng extends BaseEntity{

    @Id
    private String authId = CommonUtils.getUuid(); //권한 아이디

    private String authgrpId; // 권한 그룹 아이디
    
    private String authNm; //권한 명

    private String authDescr; //권한 설명
    
    @OneToMany(mappedBy = "tcAuthMngInfo" ,fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonManagedReference
    private List<TcMenuAuth> tcMenuAuthList;

}   
