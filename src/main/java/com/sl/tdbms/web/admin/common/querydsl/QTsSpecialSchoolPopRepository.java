package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsSpecialSchoolPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsSpecialSchoolPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsSpecialSchoolPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsSpecialSchoolPop qTsSpecialSchoolPop = QTsSpecialSchoolPop.tsSpecialSchoolPop;

	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		qTsSpecialSchoolPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(qTsSpecialSchoolPop)
                .innerJoin(tcDsdMng).on(qTsSpecialSchoolPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(qTsSpecialSchoolPop.provinCd,tcDsdMng.provinNm)
                .orderBy(qTsSpecialSchoolPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(qTsSpecialSchoolPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsSpecialSchoolPop> results = queryFactory
    		    .selectFrom(qTsSpecialSchoolPop)
    		    .where(qTsSpecialSchoolPop.popmngId.in(searchDTO.getPopmngIdArr())
	            .and(qTsSpecialSchoolPop.provinCd.eq(searchDTO.getProvince())))
    		    .orderBy(qTsSpecialSchoolPop.provinCd.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "Schools",
			    "Students Male",
			    "Students Female",
			    "Teachers Male",
			    "Teachers Female"
			};

		if (results != null && !results.isEmpty()) {
			for (TsSpecialSchoolPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getSchools(),
			       result.getMaleStudents(),
			       result.getFemaleStudents(),
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
