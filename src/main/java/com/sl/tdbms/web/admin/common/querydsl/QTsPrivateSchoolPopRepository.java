package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsPrivateSchoolPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsPrivateSchoolPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPrivateSchoolPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPrivateSchoolPop qTsPrivateSchoolPop = QTsPrivateSchoolPop.tsPrivateSchoolPop;

	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;

	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		qTsPrivateSchoolPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(qTsPrivateSchoolPop)
                .innerJoin(tcDsdMng).on(qTsPrivateSchoolPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(qTsPrivateSchoolPop.provinCd,tcDsdMng.provinNm)
                .orderBy(qTsPrivateSchoolPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(qTsPrivateSchoolPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsPrivateSchoolPop> results = queryFactory
    		    .selectFrom(qTsPrivateSchoolPop)
    		    .where(qTsPrivateSchoolPop.popmngId.in(searchDTO.getPopmngIdArr())
	            .and(qTsPrivateSchoolPop.provinCd.eq(searchDTO.getProvince())))
    		    .orderBy(qTsPrivateSchoolPop.provinCd.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "Type Of School 1AB",
			    "Type Of School 1AC",
			    "Type Of School 2",
			    "Students Male",
			    "Students Female",
			    "Students Sinhala Medium",
			    "Students Tamil Medium",
			    "Students English Medium",
			    "Teachers Male",
			    "Teachers Female"
			};

		if (results != null && !results.isEmpty()) {
			for (TsPrivateSchoolPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getSchoolsByType1AB(),
			       result.getSchoolsByType1AC(),
			       result.getSchoolsByType2(),
			       result.getMaleStudents(),
			       result.getFemaleStudents(),
			       result.getStudentsSinhalaMedium(),
			       result.getStudentsTamilMedium(),
			       result.getStudentsEnglishMedium(),
			       result.getMaleTeachers(),
			       result.getFemaleTeachers()
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
