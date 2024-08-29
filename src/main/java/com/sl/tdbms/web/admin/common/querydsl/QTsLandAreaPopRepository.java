package com.sl.tdbms.web.admin.common.querydsl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsLandAreaPop;
import com.sl.tdbms.web.admin.common.entity.QTsPopMng;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsLandAreaPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsLandAreaPop tsLandAreaPop = QTsLandAreaPop.tsLandAreaPop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
	private QTsPopMng tsPopMng = QTsPopMng.tsPopMng;

    public TsPopulationStatisticsRegionDTO getProvinceJsonAndDistrictJson() {
        List<Tuple> districtList = queryFactory.select(
        		tcDsdMng.provinCd,
        		tcDsdMng.provinNm,
        		tcDsdMng.districtCd,
        		tcDsdMng.districtNm
        		)
                .from(tsLandAreaPop)
                .innerJoin(tcDsdMng).on(tsLandAreaPop.dsdId.eq(tcDsdMng.dsdId))
                .groupBy(tcDsdMng.provinCd,tcDsdMng.provinNm,
                		tcDsdMng.districtCd,tcDsdMng.districtNm)
                .orderBy(tcDsdMng.provinCd.asc())
                .fetch();
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();
        ArrayNode districtJsonArray = mapper.createArrayNode();
        ArrayNode districtCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : districtList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tcDsdMng.provinCd));
            districtJsonArray.add(tuple.get(tcDsdMng.districtNm));
            districtCdJsonArray.add(tuple.get(tcDsdMng.districtCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        dto.setDistrictJSON(districtJsonArray.toString());
        dto.setDistrictCdJSON(districtCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<Tuple> results = queryFactory
    		    .select(
		    		tsLandAreaPop.dsDivision.as("name"),
    		        Expressions.stringTemplate(
    		        		"jsonb_agg_ordered_asc({0}, {1}, {2})",
    		                tsPopMng.popmngTitle,
    		                tsLandAreaPop.area,
    		                tsLandAreaPop.popmngId
    		        ).as("data")
    		    )
    		    .from(tsLandAreaPop)
    		    .innerJoin(tsPopMng)
    		    .on(tsLandAreaPop.popmngId.eq(tsPopMng.popmngId))
    		    .where(
    		    		tsLandAreaPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsLandAreaPop.districtId.eq(searchDTO.getProvince()+searchDTO.getDistrict()))
    		    )
    		    .groupBy(tsLandAreaPop.dsDivision,tsLandAreaPop.numIdx)
    		    .orderBy(tsLandAreaPop.numIdx.asc())
    		    .fetch();
    	
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

		if(results != null && !results.isEmpty()) {
          String[] labelArray = new String[results.size()];
            for (int i = 0 ; i < results.size(); i++) {
            	Tuple tuple = results.get(i);
                String dsDivision = tuple.get(0, String.class);
                String dataJsonString = tuple.get(1, String.class);

                List<DataPair> dataPairs = extractDataPairsFromJson(dataJsonString);

                if(dataPairs != null) {
                	List<String> labels = new ArrayList<>();
                	List<Long> values = new ArrayList<>();
                	for (DataPair pair : dataPairs) {
                		labels.add(pair.getTitle());
                		values.add(pair.getCount());
                	}
                	TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
                	info.setValues(values);
                	info.setLabels(labels);
                	infoList.add(info);
                }
                labelArray[i] = dsDivision;
            }
            dto.setLabelArray(labelArray);
    	}
		dto.setStatisticsList(infoList);
        return dto;
    }
    
    private List<DataPair> extractDataPairsFromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<DataPair>>() {});
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Data
    public static class DataPair {
        private String title;
        private Long count;
    }
}
