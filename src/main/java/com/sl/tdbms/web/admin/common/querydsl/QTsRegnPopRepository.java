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
import com.sl.tdbms.web.admin.common.entity.QTsPopMng;
import com.sl.tdbms.web.admin.common.entity.QTsRegnPop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRegnPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsRegnPop tsRegnPop = QTsRegnPop.tsRegnPop;
	
	private QTsPopMng tsPopMng = QTsPopMng.tsPopMng;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsRegnPop.district.toUpperCase())
                .distinct()
                .from(tsRegnPop)
                .orderBy(tsRegnPop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<Tuple> results = queryFactory
    		    .select(
    		        tsRegnPop.dsDivision.as("name"),
    		        Expressions.stringTemplate(
    		        		"jsonb_agg_ordered_asc({0}, {1}, {2})",
    		                tsPopMng.popmngTitle,
    		                tsRegnPop.totalPopltcnt,
    		                tsRegnPop.popmngId
    		        ).as("data")
    		    )
    		    .from(tsRegnPop)
    		    .innerJoin(tsPopMng)
    		    .on(tsRegnPop.popmngId.eq(tsPopMng.popmngId))
    		    .where(tsRegnPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsRegnPop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .groupBy(tsRegnPop.dsDivision)
    		    .fetch();

            List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

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

            TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
            dto.setStatisticsList(infoList);
            dto.setLabelArray(labelArray);
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
