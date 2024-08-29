package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsPirivenaSchoolPop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsPirivenaSchoolPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPirivenaSchoolPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPirivenaSchoolPop tsPirivenaSchoolPop = QTsPirivenaSchoolPop.tsPirivenaSchoolPop;

	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	
    public TsPopulationStatisticsRegionDTO getProvinceJson() {
        List<Tuple> provinceList = queryFactory.select(
        		tsPirivenaSchoolPop.provinCd,
        		tcDsdMng.provinNm
        		)
                .from(tsPirivenaSchoolPop)
                .innerJoin(tcDsdMng).on(tsPirivenaSchoolPop.provinCd.eq(tcDsdMng.provinCd))
                .groupBy(tsPirivenaSchoolPop.provinCd,tcDsdMng.provinNm)
                .orderBy(tsPirivenaSchoolPop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : provinceList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsPirivenaSchoolPop.provinCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsPirivenaSchoolPop> results = queryFactory
    		    .selectFrom(tsPirivenaSchoolPop)
    		    .where(tsPirivenaSchoolPop.popmngId.in(searchDTO.getPopmngIdArr())
	            .and(tsPirivenaSchoolPop.provinCd.eq(searchDTO.getProvince())))
    		    .orderBy(tsPirivenaSchoolPop.provinCd.asc())
    		    .fetch();
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();
		
		String[] labelArr = {
			    "Number of Pirivena Mulika",
			    "Number of Pirivena Maha",
			    "Number of Pirivena Vidya Yathana",
			    "Number of Students Clergy",
			    "Number of Students Laity",
			    "Approved Teachers Clergy",
			    "Approved Teachers Laity",
			    "Unapproved Teachers Clergy",
			    "Unapproved Teachers Laity"
			};

		if (results != null && !results.isEmpty()) {
			for (TsPirivenaSchoolPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDistrict().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getNumberOfPirivenaMulika(),
				   result.getNumberOfPirivenaMaha(),
				   result.getNumberOfPirivenaVidyaYathana(),
				   result.getNumberOfStudentsClergy(),
				   result.getNumberOfStudentsLaity(),
				   result.getApprovedTeachersClergy(),
				   result.getApprovedTeachersLaity(),
				   result.getUnapprovedTeachersClergy(),
				   result.getUnapprovedTeachersLaity()
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
