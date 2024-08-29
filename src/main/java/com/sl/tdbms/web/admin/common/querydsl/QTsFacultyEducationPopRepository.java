package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsFacultyEducationPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsFacultyEducationPop;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsFacultyEducationPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsFacultyEducationPop tsFacultyEducationPop = QTsFacultyEducationPop.tsFacultyEducationPop;

	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getDistrictAndDsdJson() {
        List<Tuple> districtList = queryFactory.select(
        		tcDsdMng.districtCd,
        		tcDsdMng.districtNm,
        		tcDsdMng.dsdCd,
        		tcDsdMng.dsdNm
        		)
                .from(tsFacultyEducationPop)
                .innerJoin(tcDsdMng).on(tsFacultyEducationPop.dsdId.eq(tcDsdMng.dsdId))
                .groupBy(
            		tcDsdMng.districtCd,
            		tcDsdMng.districtNm,
            		tcDsdMng.dsdCd,
            		tcDsdMng.dsdNm
        		)
                .orderBy(tcDsdMng.districtCd.asc(),tcDsdMng.dsdCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode districtJsonArray = mapper.createArrayNode();
        ArrayNode dsdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : districtList) {
        	districtJsonArray.add(tuple.get(tcDsdMng.districtNm));
            dsdJsonArray.add(tuple.get(tcDsdMng.dsdNm));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setDistrictJSON(districtJsonArray.toString());
        dto.setDistrictCdJSON(districtJsonArray.toString());
        dto.setDsdCdJSON(dsdJsonArray.toString());
        dto.setDsdJSON(dsdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsFacultyEducationPop> results = queryFactory
    		    .selectFrom(tsFacultyEducationPop)
                .innerJoin(tcDsdMng).on(tsFacultyEducationPop.dsdId.eq(tcDsdMng.dsdId))
    		    .where(tsFacultyEducationPop.popmngId.in(searchDTO.getPopmngIdArr())
		        .and(Expressions.stringTemplate(
		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
		            tsFacultyEducationPop.district
		        ).eq(CommonUtils.normalizeString(searchDTO.getDistrict())))
		        .and(Expressions.stringTemplate(
		            "REPLACE(UPPER({0}), '[^A-Z]', '')",
		            tsFacultyEducationPop.educationZone
		        ).eq(CommonUtils.normalizeString(searchDTO.getDsDivision()))))
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "National Schools",
			    "Provincial Schools",
			    "Male Students",
			    "Female Students",
			    "Teachers",
			    "Student Teacher Ratio"
			};

		if (results != null && !results.isEmpty()) {
			for (TsFacultyEducationPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getNationalSchools(),
				   result.getProvincialSchools(),
				   result.getMaleStudents(),
				   result.getFemaleStudents(),
				   result.getTeachers(),
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
