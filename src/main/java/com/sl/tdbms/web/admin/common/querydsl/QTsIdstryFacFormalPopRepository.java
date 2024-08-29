package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsIdstryFacFormalPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsIdstryFacFormalPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsIdstryFacFormalPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsIdstryFacFormalPop tsIdstryFacFormalPop = QTsIdstryFacFormalPop.tsIdstryFacFormalPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsIdstryFacFormalPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsIdstryFacFormalPop)
                .innerJoin(tcDsdMng).on(tsIdstryFacFormalPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsIdstryFacFormalPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsIdstryFacFormalPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsIdstryFacFormalPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsIdstryFacFormalPop> results = queryFactory
    		    .selectFrom(tsIdstryFacFormalPop)
    		    .where(
    		    		tsIdstryFacFormalPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsIdstryFacFormalPop.provinCd.eq(searchDTO.getProvince()))
    		    )
    		    .orderBy(tsIdstryFacFormalPop.provinCd.asc())
    		    .fetch();

		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
        String[] labelArr = {
            "No. of establishments",                      // 총 사업체 수
            "Persons engaged (No.)",                      // 참여 인원 수
            "Employees (No.)",                            // 직원 수
            "Salaries & Wages (Rs.)",                     // 급여 및 임금 (루피)
            "Value of output (Rs.)",                      // 산출물의 가치 (루피)
            "Value of Intermediate consumptions (Rs.)",   // 중간 소비의 가치 (루피)
            "Value added (Rs.)",                          // 부가가치 (루피)
            "Gross additions to fixed assets (Rs.)"       // 고정 자산의 총 추가 (루피)
        };
		
		if (results != null && !results.isEmpty()) {
			for (TsIdstryFacFormalPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
					   result.getNumberOfEstablishments(),
					   result.getPersonsEngaged(),
					   result.getEmployees(),
					   result.getSalariesAndWages(),
					   result.getValueOfOutput(),
					   result.getValueOfIntermediateConsumptions(),
					   result.getValueAdded(),
					   result.getGrossAdditionsToFixedAssets()
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
