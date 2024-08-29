package com.sl.tdbms.web.admin.common.enums;

import lombok.Getter;

@Getter
public enum PersonalTransportationChartType {
	TRAVEL_MODE_TRANSPORTATION("travelModeTransportationChart","PIE",false),
	TRAVEL_PURPOSE("travelPurposeChart","PIE",false),
	TRAVEL_DEPARTURE("travelDepartureChart","PIE",false),
	TRAVEL_DESTINATION("travelDestinationChart","PIE",false),
	TRAVEL_DEPARTURE_TIME("travelDepartureTimeChart","LINE",true),
	TRAVEL_DESTINATION_TIME("travelDestinationTimeChart","LINE",true),
	AVG_TRAVEL_PURPOSE_TIME("avgTravelPurposeTimeChart","AVG",true),
//	AVG_TRAVEL_TRANSFORT_TIME("avgTravelTransfortTimeChart",null,true),
	TRAVEL_TRANSFORT_CNT("travelTransfortCntChart","PIE",false),
	AVG_TRAVEL_TRANSFORT_HOUR("avgTravelTransfortHourChart","AVG",false),
	TRAVEL_USE_HIGHWAY("travelUseHighwayChart","PIE",false),
//	TRAVEL_FEE_TYPE("travelFeeTypeChart","LINE",false),
	AVG_TRAVEL_MODE_TRANSPORTATION("avgTravelModeTransportationChart","AVG",true),
//	AVG_TRAVEL_FEE("avgTravelFeeChart",null,true),
	PERSONAL_STATUS_BY_AGE("personalStatusByAge","PIE",false),
	PERSONAL_STATUS_BY_GENDER("personalStatusByGender","TABLE",false),
	PERSONAL_STATUS_BY_EDUCATION("personalStatusByEducation","TABLE",false),
	PERSONAL_STATUS_BY_OCCUPATION("personalStatusByOccupation","TABLE",false),
	PERSONAL_STATUS_BY_WORKINGDAY("personalStatusByWorkingday","TABLE",false),
	;

	private String appendId;
	private String chartType;
	private boolean isSelectBox;

	private PersonalTransportationChartType(String appendId, String chartType,boolean isSelectBox) {
		this.appendId = appendId;
		this.chartType = chartType;
		this.isSelectBox = isSelectBox;
	}
	

	
}