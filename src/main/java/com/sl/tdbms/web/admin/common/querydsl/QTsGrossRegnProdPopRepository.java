package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsGrossRegnProdPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsGrossRegnProdPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsGrossRegnProdPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsGrossRegnProdPop tsGrossRegnProdPop = QTsGrossRegnProdPop.tsGrossRegnProdPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> districtList = queryFactory.select(
        		tsGrossRegnProdPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsGrossRegnProdPop)
                .innerJoin(tcDsdMng).on(tsGrossRegnProdPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsGrossRegnProdPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsGrossRegnProdPop.provinCd.asc())
                .fetch();
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : districtList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsGrossRegnProdPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsGrossRegnProdPop> results = queryFactory
    		    .selectFrom(tsGrossRegnProdPop)
    		    .where(tsGrossRegnProdPop.popmngId.in(searchDTO.getPopmngIdArr())
    		    		.and(tsGrossRegnProdPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsGrossRegnProdPop.years.asc())
    		    .fetch();
    	
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

		String[] labelArr = {
				"Agriculture",                       // agriculture
				"Industry",                          // industry
				"Services",                          // services
				"Gross Domestic Product (GDP), at Current Market Prices" // gdpAtCurrentMarketPrice
		};
		if(results != null && !results.isEmpty()) {
			for (TsGrossRegnProdPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = String.valueOf(result.getYears());
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getAgriculture(),
				   result.getIndustry(),
				   result.getServices(),
				   result.getGdpAtCurrentMarketPrice()
			   );
			   
			   info.setTitle(title);
			   info.setCountArray(resultCnt.toArray(new BigDecimal[0]));
			   infoList.add(info);
			}
    	}
		dto.setLabelArray(labelArr);
		dto.setStatisticsList(infoList);
        return dto;
    }
}
