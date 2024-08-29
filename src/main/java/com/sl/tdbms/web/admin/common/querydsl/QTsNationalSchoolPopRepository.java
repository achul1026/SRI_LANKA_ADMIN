package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsNationalSchoolPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsNationalSchoolPop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsNationalSchoolPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsNationalSchoolPop tsNationalSchoolPop = QTsNationalSchoolPop.tsNationalSchoolPop;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsNationalSchoolPop.district.toUpperCase())
                .distinct()
                .from(tsNationalSchoolPop)
                .orderBy(tsNationalSchoolPop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsNationalSchoolPop> results = queryFactory
    		    .selectFrom(tsNationalSchoolPop)
    		    .where(tsNationalSchoolPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsNationalSchoolPop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .orderBy(tsNationalSchoolPop.educationZone.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "All Schools",
			    "Schools By Level National",
			    "Schools By Level Provincial",
			    "Schools By Type 1AB",
			    "Schools By Type 1C",
			    "Schools By Type 2",
			    "Schools By Type 3",
			    "Student Population 1 to 50",
			    "Student Population 51 to 100",
			    "Student Population 101 to 200",
			    "Student Population 201 to 500",
			    "Student Population 501 to 1000",
			    "Student Population Above 1000"
			};
		
		if (results != null && !results.isEmpty()) {
			for (TsNationalSchoolPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getEducationZone().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getTotalShools(),
			       result.getNationalSchools(),
			       result.getProvincialSchools(),
			       result.getSchoolsByType1AB(),
			       result.getSchoolsByType1C(),
			       result.getSchoolsByType2(),
			       result.getSchoolsByType3(),
			       result.getStudentPopulation1to50(),
			       result.getStudentPopulation51to100(),
			       result.getStudentPopulation101to200(),
			       result.getStudentPopulation201to500(),
			       result.getStudentPopulation501to1000(),
			       result.getStudentPopulationAbove1000()
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
