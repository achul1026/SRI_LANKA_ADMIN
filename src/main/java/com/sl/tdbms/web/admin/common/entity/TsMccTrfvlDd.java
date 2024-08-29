package com.sl.tdbms.web.admin.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class TsMccTrfvlDd extends BaseEntity{
    @Id
    private String statsYymmdd;
    private String exmnmngId;
    private String tazCd;
    private String startlcNm;
    private String endlcNm;
    private String vhclClsf;
    private long trfvlm;
}
