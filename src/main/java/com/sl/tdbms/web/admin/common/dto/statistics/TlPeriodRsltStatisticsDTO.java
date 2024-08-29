package com.sl.tdbms.web.admin.common.dto.statistics;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TlPeriodRsltStatisticsDTO {
    String dataType;
    List<CommonCdDTO> directionList;
    List<PeriodChartData> chartData;

    @Data
    public static class PeriodChartData{
        Integer dayOfTheWeek;
        String statsDate;
        String instllcNm;
        String drctCd;
        String cdNm;
        Long trfvlm;
        Double avgSpeed;
    }
}
