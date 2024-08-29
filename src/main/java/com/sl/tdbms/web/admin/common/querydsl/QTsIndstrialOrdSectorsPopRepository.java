package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIndstrialOrdSectorsPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsIndstrialOrdSectorsPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsIndstrialOrdSectorsPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsIndstrialOrdSectorsPop tsIndstrialOrdSectorsPop = QTsIndstrialOrdSectorsPop.tsIndstrialOrdSectorsPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsIndstrialOrdSectorsPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsIndstrialOrdSectorsPop)
                .innerJoin(tcDsdMng).on(tsIndstrialOrdSectorsPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsIndstrialOrdSectorsPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsIndstrialOrdSectorsPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsIndstrialOrdSectorsPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsIndstrialOrdSectorsPop> results = queryFactory
    		    .selectFrom(tsIndstrialOrdSectorsPop)
    		    .where(
    		    		tsIndstrialOrdSectorsPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsIndstrialOrdSectorsPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsIndstrialOrdSectorsPop.provinCd.asc())
    		    .fetch();

		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
        String[] labelArr = {
            "Value of output (Rs.)",                      // output
            "Value of Intermediate consumptions (Rs.)",   // intermediateConsumption
            "Value added (Rs.)"                           // valueAdded
        };
		
		if (results != null && !results.isEmpty()) {
			for (TsIndstrialOrdSectorsPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getValueOfOutput(),
				   result.getIntermediateConsumption(),
				   result.getValueAdded()
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
