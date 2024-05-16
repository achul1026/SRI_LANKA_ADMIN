package com.sri.lanka.traffic.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.Data;

@Entity
@Data //메뉴권한 관리
public class TcMenuAuth{

    @Id
    private String menuauthId = CommonUtils.getUuid(); //메뉴 권한 아이디

    private String authgrpId; //권한 아이디

    private String menuId; //메뉴 아이디

    private String inputYn; //입력 여부

    private String srchYn; //조회 여부

    private String updtYn; //수정 여부

    private String delYn; //삭제 여부
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authgrpId", insertable = false, updatable = false)
	@JsonBackReference
	private TcAuthGrp tcAuthGrpInfo;
	
	public TcMenuAuth() {}
	
	public TcMenuAuth(String authgrpId, String menuId, String authYn) {
		this.authgrpId 	= authgrpId;
		this.menuId 	= menuId;
		this.inputYn 	= authYn;
		this.srchYn 	= authYn;
		this.updtYn 	= authYn;
		this.delYn 		= authYn;
	}
	
	public TcMenuAuth(String authgrpId, String inputYn, String srchYn, String updtYn, String delYn) {
		super();
		this.authgrpId	= authgrpId;
		this.inputYn	= inputYn;
		this.srchYn		= srchYn;
		this.updtYn		= updtYn;
		this.delYn		= delYn;
	}

}
