package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyStudentsPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsFacultyStudentsPop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsFacultyStudentsPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsFacultyStudentsPop tsFacultyStudentsPop = QTsFacultyStudentsPop.tsFacultyStudentsPop;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsFacultyStudentsPop.district.toUpperCase())
                .distinct()
                .from(tsFacultyStudentsPop)
                .orderBy(tsFacultyStudentsPop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsFacultyStudentsPop> results = queryFactory
    		    .selectFrom(tsFacultyStudentsPop)
    		    .where(tsFacultyStudentsPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsFacultyStudentsPop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .orderBy(tsFacultyStudentsPop.educationZone.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "Student Population 1 to 50",
			    "Student Population 51 to 100",
			    "Student Population 101 to 200",
			    "Student Population 201 to 500",
			    "Student Population 501 to 1000",
			    "Student Population Above 1000"
			};
		
		if (results != null && !results.isEmpty()) {
			for (TsFacultyStudentsPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getEducationZone().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
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
