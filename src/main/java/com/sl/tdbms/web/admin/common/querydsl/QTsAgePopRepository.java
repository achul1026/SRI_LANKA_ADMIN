package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsAgePop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsAgePop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsAgePopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsAgePop tsAgePop = QTsAgePop.tsAgePop;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsAgePop.district.toUpperCase())
                .distinct()
                .from(tsAgePop)
                .orderBy(tsAgePop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsAgePop> results = queryFactory
    		    .selectFrom(tsAgePop)
    		    .where(tsAgePop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsAgePop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .orderBy(tsAgePop.dsDivision.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {"0~4", "5~9", "10~14", "15~19", "20~24", "25~29", "30~34", "35~39", "40~44", "45~49",
                "50~54", "55~59", "60~64", "65~69", "70~74", "75~79", "80~84", "85~89", "90~94", "95~Above"};

		if (results != null && !results.isEmpty()) {
			for (TsAgePop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDsDivision().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getAgeGroup0To4(),
			       result.getAgeGroup5To9(),
			       result.getAgeGroup10To14(),
			       result.getAgeGroup15To19(),
			       result.getAgeGroup20To24(),
			       result.getAgeGroup25To29(),
			       result.getAgeGroup30To34(),
			       result.getAgeGroup35To39(),
			       result.getAgeGroup40To44(),
			       result.getAgeGroup45To49(),
			       result.getAgeGroup50To54(),
			       result.getAgeGroup55To59(),
			       result.getAgeGroup60To64(),
			       result.getAgeGroup65To69(),
			       result.getAgeGroup70To74(),
			       result.getAgeGroup75To79(),
			       result.getAgeGroup80To84(),
			       result.getAgeGroup85To89(),
			       result.getAgeGroup90To94(),
			       result.getAgeGroup95AndAbove()
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
