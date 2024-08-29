package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data //Faq 관리
@EqualsAndHashCode(callSuper=true)
public class TcFaqMng extends BaseEntity{

    @Id
    private String faqId = CommonUtils.getUuid(); //FAQ 아이디

    private String faqType; //FAQ 유형

    private String faqQstn; //FAQ 질문

    private String faqAns; //FAQ 답변

    private String dspyYn; //표출 여부

}