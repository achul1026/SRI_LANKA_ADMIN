package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIdstryFacInformalPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsIdstryFacInformalPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsIdstryFacInformalPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsIdstryFacInformalPop tsIdstryFacInformalPop = QTsIdstryFacInformalPop.tsIdstryFacInformalPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsIdstryFacInformalPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsIdstryFacInformalPop)
                .innerJoin(tcDsdMng).on(tsIdstryFacInformalPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsIdstryFacInformalPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsIdstryFacInformalPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsIdstryFacInformalPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsIdstryFacInformalPop> results = queryFactory
    		    .selectFrom(tsIdstryFacInformalPop)
    		    .where(
    		    		tsIdstryFacInformalPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsIdstryFacInformalPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsIdstryFacInformalPop.provinCd.asc())
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
			for (TsIdstryFacInformalPop result : results) {
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
