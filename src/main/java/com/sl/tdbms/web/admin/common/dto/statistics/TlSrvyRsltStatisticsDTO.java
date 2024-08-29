package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import lombok.Data;

@Data
public class TlSrvyRsltStatisticsDTO {
	private String appendId;
	private String chartType;
	private String[] colorArrays = {"#4D95E8","#9A83EA","#D983EA","#626BD2","#FF9843","#FFDD95","86A7FC","#BE5A83","#E06469","#F2B6A0"};
	private List<TlSrvyRsltStatisticsInfo> statisticsList;
	private List<PassengerStatisticsInfo> passengerStatisticsList;
	private List<PurposeStatisticsInfo> purposeStatisticsList;
	private List<String> searchList;
	private Double avgNumber;
	private String enumType;
	private boolean isSelectBox;

	public void isSelectBox(boolean isSelectBox) {
		this.isSelectBox = isSelectBox;
	}

	/*private List<TlSrvyRsltStatisticsInfo> destinationLocationTypeList;*/

	@Data
	public static class TlSrvyRsltStatisticsInfo{
		private String 	name; //이동 수단 유형명
		private long 	value; //이동 수단 교통량
	}
	
	@Data
	public static class PassengerStatisticsInfo {
	    private String passageType;
	    private Long passengerOne;
	    private Long passengerTwo;
	    private Long passengerThree;
	    private Long passengerFour;
	    private Long passengerFive;
	    private Long passengerSixMore;
	}
	
	@Data
	public static class PurposeStatisticsInfo{
	    private String tripPurpose;
	    private Long minGroup0To15;
	    private Long minGroup16To30;
	    private Long minGroup31To45;
	    private Long minGroup46To60;
	    private Long minGroup61To90;
	    private Long minGroup91To120;
	    private Long minGroup121To150;
	    private Long minGroup151To180;
	    private Long minGroup180Above;
	}
}
