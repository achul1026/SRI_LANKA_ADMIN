package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //사무국 관리
@EqualsAndHashCode(callSuper=true)
public class TcDsdMng extends BaseEntity{

    @Id
    private String dsdId; //스리랑카사무국아이디
    
    private String provinCd; //스리랑카행정주코드
    
    private String provinNm; //스리랑카행정주명
    
    private String districtCd; //스리랑카행정구역코드
    
    private String districtNm; //스리랑카행정구역명
    
    private String dsdCd; //스리랑카사무국코드
    
    private String dsdNm; //스리랑카사무국명
    
    private String useYn; //사용여부

}
