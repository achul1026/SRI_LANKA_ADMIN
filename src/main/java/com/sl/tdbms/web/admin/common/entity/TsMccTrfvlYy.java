package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 연령 별 인구 수 통계
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsMccTrfvlYy extends BaseEntity{
    @Id
    private String statsYy;
    private String exmnmngId;
    private String tazCd;
    private String startlcNm;
    private String endlcNm;
    private String vhclClsf;
    private long trfvlm;
}
