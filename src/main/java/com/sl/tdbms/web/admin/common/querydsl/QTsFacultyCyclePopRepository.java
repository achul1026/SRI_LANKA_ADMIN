package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyCyclePop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsFacultyCyclePop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsFacultyCyclePopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsFacultyCyclePop tsFacultyCyclePop = QTsFacultyCyclePop.tsFacultyCyclePop;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsFacultyCyclePop.district.toUpperCase())
                .distinct()
                .from(tsFacultyCyclePop)
                .orderBy(tsFacultyCyclePop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsFacultyCyclePop> results = queryFactory
    		    .selectFrom(tsFacultyCyclePop)
    		    .where(tsFacultyCyclePop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsFacultyCyclePop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .orderBy(tsFacultyCyclePop.educationZone.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "Cycle G1 To G5",
			    "Cycle G6 To G11",
			    "Cycle G12 To G13"
			};
		
		if (results != null && !results.isEmpty()) {
			for (TsFacultyCyclePop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getEducationZone().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getCycleG1To5(),
			       result.getCycleG6To11(),
			       result.getCycleG12To13()
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
