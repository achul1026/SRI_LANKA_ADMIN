package com.sl.tdbms.web.admin.common.enums;

import lombok.Getter;

@Getter
public enum RoadSideChartType {
	MODE_OF_TRANSPORTATION("modeOfTransportationChart","PIE", true),
	NUMBER_OF_PASSENGERS("numberOfPassengersChart","PIE", true),
	TYPE_OF_DEPARTURE_POINT("typeOfDeparturePointChart","PIE", false),
	TYPE_OF_ARRIVAL_POINT("typeOfArrivalPointChart","PIE", false),
	PURPOSE_OF_TRAVEL("purposeOfTravelChart","PIE", false),
	DEPARTURE_TIME("departureTimeChart","LINE",true),
	HOUR_TRANSPORTATION("sideHourTransportationChart","BAR",true),
//	HOUR_PURPOSE("hourPurposeChart","LINE",true),
	;

	private String appendId;
	private String chartType;
	private boolean isSelectBox;

	private RoadSideChartType(String appendId, String chartType, boolean isSelectBox) {
		this.appendId = appendId;
		this.chartType = chartType;
		this.isSelectBox = isSelectBox;
	}
	

	
}