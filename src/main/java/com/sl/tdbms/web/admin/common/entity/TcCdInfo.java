package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //공통코드 관리
@EqualsAndHashCode(callSuper=true)
public class TcCdInfo extends CreateEntity{

    @Id
    private String cdId = CommonUtils.getUuid(); //코드 아이디

    private String grpcdId; //그룹 코드 아이디

    private String cd; //코드

    private String cdnmEng; //코드 명 eng
    
    private String cdnmKor; //코드 명 kor
    
    private String cdnmSin; //코드 명 sin

    private String cddescrEng; //코드 설명 eng
    
    private String cddescrKor; //코드 설명 kor
    
    private String cddescrSin; //코드 설명 sin

    private String useYn; //사용 여부

}
