package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsSchoolSystemPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsSchoolSystemPop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsSchoolSystemPopRepository {

	private final JPAQueryFactory queryFactory;

	private QTsSchoolSystemPop tsSchoolSystemPop = QTsSchoolSystemPop.tsSchoolSystemPop;
	
    public TsPopulationStatisticsRegionDTO getDistrictJson() {
        List<String> districtList = queryFactory.select(tsSchoolSystemPop.district.toUpperCase())
                .distinct()
                .from(tsSchoolSystemPop)
                .orderBy(tsSchoolSystemPop.district.toUpperCase().asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtArray = mapper.valueToTree(districtList);

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsSchoolSystemPop> results = queryFactory
    		    .selectFrom(tsSchoolSystemPop)
    		    .where(tsSchoolSystemPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(Expressions.stringTemplate(
    		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
    		            tsSchoolSystemPop.district
    		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
    		    )
    		    .orderBy(tsSchoolSystemPop.dsdId.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
        String[] labelArr = {
            "National Level",             // levelNational
            "Provincial Level",           // levelProvincial
            "Type of School 1AB",         // typeOfSchool1ab
            "Type of School 1C",          // typeOfSchool1c
            "Type of School 2",           // typeOfSchool2
            "Type of School 3",           // typeOfSchool3
            "Male Students",              // male
            "Female Students",            // female
            "Student-Teacher Ratio"       // studentTeacherRatio
        };
		
		if (results != null && !results.isEmpty()) {
			for (TsSchoolSystemPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getEducationZone().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getNationalStudents(),
			       result.getProvincialStudents(),
			       result.getSchoolsByType1AB(),
			       result.getSchoolsByType1C(),
			       result.getSchoolsByType2(),
			       result.getSchoolsByType3(),
			       result.getMale(),
			       result.getFemale(),
			       result.getStudentTeacherRatio()
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
