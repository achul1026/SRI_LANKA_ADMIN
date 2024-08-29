package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsEmplymntStatusPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsEmplymntStatusPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsEmplymntStatusPopRepository {

	private final JPAQueryFactory queryFactory;

	private  QTsEmplymntStatusPop tsEmplymntStatusPop =  QTsEmplymntStatusPop.tsEmplymntStatusPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsEmplymntStatusPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsEmplymntStatusPop)
                .innerJoin(tcDsdMng).on(tsEmplymntStatusPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsEmplymntStatusPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsEmplymntStatusPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsEmplymntStatusPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsEmplymntStatusPop> results = queryFactory
    		    .selectFrom(tsEmplymntStatusPop)
    		    .where(
    		    		tsEmplymntStatusPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsEmplymntStatusPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsEmplymntStatusPop.provinCd.asc())
    		    .fetch();

		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
        String[] labelArr = {
            "Industry Number",        // industryNumber
            "Industry Percentage",    // industryPercentage
            "Trade Number",           // tradeNumber
            "Trade Percentage",       // tradePercentage
            "Services Number",        // servicesNumber
            "Services Percentage"     // servicesPercentage
        };
		
		if (results != null && !results.isEmpty()) {
			for (TsEmplymntStatusPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getIndustryNumber(),
				   result.getIndustryPercentage(),
				   result.getTradeNumber(),
				   result.getTradePercentage(),
				   result.getServicesNumber(),
				   result.getServicesPercentage()
			   );
			   info.setTitle(title);
			   info.setCountArray(resultCnt.toArray(new BigDecimal[0]));
			   infoList.add(info);
			}
		}
		dto.setStatisticsList(infoList);
        dto.setLabelArray(labelArr);
        return dto;
    }
}
