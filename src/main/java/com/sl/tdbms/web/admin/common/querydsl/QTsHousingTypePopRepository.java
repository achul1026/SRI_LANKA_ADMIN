package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsHousingTypePop;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTsHousingTypePop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsHousingTypePopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsHousingTypePop tsHousingTypePop = QTsHousingTypePop.tsHousingTypePop;
	
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;

    public TsPopulationStatisticsRegionDTO getProvinceJsonAndDistrictJson() {
        List<Tuple> districtList = queryFactory.select(
        		tsHousingTypePop.provinCd,
        		tcDsdMng.provinNm,
        		tsHousingTypePop.districtCd,
        		tcDsdMng.districtNm
        		)
                .from(tsHousingTypePop)
                .innerJoin(tcDsdMng).on(tsHousingTypePop.provinCd.eq(tcDsdMng.provinCd).and(tsHousingTypePop.districtCd.eq(tcDsdMng.districtCd)))
                .groupBy(tsHousingTypePop.provinCd,tcDsdMng.provinNm,
                		tsHousingTypePop.districtCd,tcDsdMng.districtNm)
                .orderBy(tsHousingTypePop.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();
        ArrayNode districtJsonArray = mapper.createArrayNode();
        ArrayNode districtCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : districtList) {
            provinceJsonArray.add(tuple.get(tcDsdMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tsHousingTypePop.provinCd));
            districtJsonArray.add(tuple.get(tcDsdMng.districtNm));
            districtCdJsonArray.add(tuple.get(tsHousingTypePop.districtCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        dto.setDistrictJSON(districtJsonArray.toString());
        dto.setDistrictCdJSON(districtCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsHousingTypePop> results = queryFactory
    		    .selectFrom(tsHousingTypePop)
    		    .where(
    		    		tsHousingTypePop.popmngId.in(searchDTO.getPopmngIdArr())
    		        .and(tsHousingTypePop.provinCd.eq(searchDTO.getProvince()))
    		        .and(tsHousingTypePop.districtCd.eq(searchDTO.getDistrict()))
    		    )
    		    .orderBy(tsHousingTypePop.provinCd.asc())
    		    .fetch();
    	
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

		String[] labelArr = {"1 story", "2 story", "2+ story", "House/Annex", "Flat", "Condominium", "Twin house", "Room", "Hut/Shanty"};

		if(results != null && !results.isEmpty()) {
			for (TsHousingTypePop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getDsDivision().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.getOneStory(),
			       result.getTwoStory(),
			       result.getMultiStory(),
			       result.getHouseAnnex(),
			       result.getFlat(),
			       result.getCondominium(),
			       result.getTwinHouse(),
			       result.getRoom(),
			       result.getHutShanty()
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
