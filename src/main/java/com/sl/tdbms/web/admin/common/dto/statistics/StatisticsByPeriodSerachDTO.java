package com.sl.tdbms.web.admin.common.dto.statistics;

import lombok.Data;

@Data
public class StatisticsByPeriodSerachDTO {
    private String searchSurveyType;
    private String searchDateType;
    private String startDate;
    private String endDate;
    private String startHour;
    private String endHour;
    private String searchType;
    private String searchContent;
}
