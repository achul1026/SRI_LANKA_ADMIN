package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsHouseHoldPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsHouseHoldPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsHouseHoldPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsHouseHoldPop tsHouseHoldPop = QTsHouseHoldPop.tsHouseHoldPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;

    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsHouseHoldPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsHouseHoldPop)
                .innerJoin(tcDsdMng).on(tsHouseHoldPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsHouseHoldPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsHouseHoldPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsHouseHoldPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsHouseHoldPop> results = queryFactory
    		    .selectFrom(tsHouseHoldPop)
    		    .where(
		    		tsHouseHoldPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsHouseHoldPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsHouseHoldPop.provinCd.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

		String[] labelArr = {"Ccupants in occupied units 1","Ccupants in occupied units 2","Ccupants in occupied units 3","Ccupants in occupied units 4","Ccupants in occupied units 5","Ccupants in occupied units 6","Ccupants in occupied units 7","Ccupants in occupied units 8","Ccupants in occupied units 9","Ccupants in occupied units 10 or above","Average Size of house holds"};

		if(results != null && !results.isEmpty()) {
			for (TsHouseHoldPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getHousehold1(),
			       result.getHousehold2(),
			       result.getHousehold3(),
			       result.getHousehold4(),
			       result.getHousehold5(),
			       result.getHousehold6(),
			       result.getHousehold7(),
			       result.getHousehold8(),
			       result.getHousehold9(),
			       result.getHousehold10Above(),
			       result.getAvgHouseholdSize()
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
