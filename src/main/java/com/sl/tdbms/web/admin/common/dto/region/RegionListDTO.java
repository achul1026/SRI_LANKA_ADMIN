package com.sl.tdbms.web.admin.common.dto.region;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionListDTO {
	private String dsdId;
	private String dstrctCd;
	private String districtNm;
	private String provinCd;
	private String provinNm;
	private String dsdCd;
	private String dsdNm;

	private String dstrctGis;
	private BigDecimal cntrLat;
	private BigDecimal cntrLon;
	
	private String gnId;
	
    public RegionListDTO(String dsdId, String provinCd, String provinNm, String districtCd, String districtNm, String dsdCd, String dsdNm
//    		, BigDecimal cntrLat, BigDecimal cntrLon
    		) {
        this.dsdId = dsdId;
        this.provinCd = provinCd;
        this.provinNm = provinNm;
        this.dstrctCd = districtCd;
        this.districtNm = districtNm;
        this.dsdCd = dsdCd;
        this.dsdNm = dsdNm;
//        this.cntrLat = cntrLat;
//    	this.cntrLon = cntrLon;
    }
    
    public RegionListDTO(String gnId, String dstrctCd, String provinNm, String districtNm, String dsdNm, BigDecimal cntrLat, BigDecimal cntrLon) {
    	this.gnId = gnId;
    	this.dstrctCd = dstrctCd;
    	this.provinNm = provinNm;
    	this.districtNm = districtNm;
    	this.dsdNm = dsdNm;
    	this.cntrLat = cntrLat;
    	this.cntrLon = cntrLon;
    }
}
