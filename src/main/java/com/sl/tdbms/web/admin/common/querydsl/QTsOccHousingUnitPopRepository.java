package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsRegionDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcGnMng;
import com.sl.tdbms.web.admin.common.entity.QTsOccHousingUnitPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsOccHousingUnitPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsOccHousingUnitPop tsOccHousingUnitPop = QTsOccHousingUnitPop.tsOccHousingUnitPop;
	
	private QTcGnMng tcGnMng = QTcGnMng.tcGnMng;

    public TsPopulationStatisticsRegionDTO getProvinceJsonAndDistrictJsonAndDsdJson() {
        List<Tuple> districtList = queryFactory.select(
        		tcGnMng.provinCd,
        		tcGnMng.provinNm,
        		tcGnMng.districtNm,
        		tcGnMng.districtCd,
        		tcGnMng.dsdNm,
        		tcGnMng.dsdCd
        		)
                .from(tsOccHousingUnitPop)
                .innerJoin(tcGnMng).on(tcGnMng.gnId.eq(tsOccHousingUnitPop.gnId))
                .groupBy(        		
        		tcGnMng.provinCd,
        		tcGnMng.provinNm,
        		tcGnMng.districtNm,
        		tcGnMng.districtCd,
        		tcGnMng.dsdNm,
        		tcGnMng.dsdCd)
                .orderBy(tcGnMng.provinCd.asc())
                .fetch();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode provinceJsonArray = mapper.createArrayNode();
        ArrayNode provinceCdJsonArray = mapper.createArrayNode();
        ArrayNode districtJsonArray = mapper.createArrayNode();
        ArrayNode districtCdJsonArray = mapper.createArrayNode();
        ArrayNode dsdJsonArray = mapper.createArrayNode();
        ArrayNode dsdCdJsonArray = mapper.createArrayNode();

        for (Tuple tuple : districtList) {
            provinceJsonArray.add(tuple.get(tcGnMng.provinNm));
            provinceCdJsonArray.add(tuple.get(tcGnMng.provinCd));
            districtJsonArray.add(tuple.get(tcGnMng.districtNm));
            districtCdJsonArray.add(tuple.get(tcGnMng.districtCd));
            dsdJsonArray.add(tuple.get(tcGnMng.dsdNm));
            dsdCdJsonArray.add(tuple.get(tcGnMng.dsdCd));
        }

        TsPopulationStatisticsRegionDTO dto = new TsPopulationStatisticsRegionDTO();
        dto.setProvinceJSON(provinceJsonArray.toString());
        dto.setProvinceCdJSON(provinceCdJsonArray.toString());
        dto.setDistrictJSON(districtJsonArray.toString());
        dto.setDistrictCdJSON(districtCdJsonArray.toString());
        dto.setDsdJSON(dsdJsonArray.toString());
        dto.setDsdCdJSON(dsdCdJsonArray.toString());
        return dto;
    }
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<Tuple> results = queryFactory
    		    .select(
		    		tsOccHousingUnitPop.householdOwned.sum(),
		    		tsOccHousingUnitPop.rentGovermentOwned.sum(),
		    		tsOccHousingUnitPop.rentIndvslOwned.sum(),
		    		tsOccHousingUnitPop.rentFree.sum(),
		    		tsOccHousingUnitPop.encroached.sum(),
		    		tsOccHousingUnitPop.otherOccupied.sum()
    		    )
    		    .from(tsOccHousingUnitPop)
    		    .where(
    		    		tsOccHousingUnitPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        	,eqGnId(searchDTO.getProvince(),searchDTO.getDistrict(),searchDTO.getDsDivision())
	    		)
    		    .fetch();
    	 
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

		String[] labelArr = {"Owned by Household Member", "Rent/Lease Government Owned", "Rent/Lease Privately Owned", 
		          "Rent Free", "Encroached", "Other Occupied"};

		if(results != null && !results.isEmpty()) {
			for (Tuple result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
			       result.get(tsOccHousingUnitPop.householdOwned.sum()),
			       result.get(tsOccHousingUnitPop.rentGovermentOwned.sum()),
			       result.get(tsOccHousingUnitPop.rentIndvslOwned.sum()),
			       result.get(tsOccHousingUnitPop.rentFree.sum()),
			       result.get(tsOccHousingUnitPop.encroached.sum()),
			       result.get(tsOccHousingUnitPop.otherOccupied.sum())
			   );
			   info.setCountArray(resultCnt.toArray(new BigDecimal[0]));
			   infoList.add(info);
			}
    	}
		
    	List<Tuple> tableResults = queryFactory
    		    .select(
	                new CaseBuilder()
                    .when(tsOccHousingUnitPop.gnDivision.isNotNull().and(tsOccHousingUnitPop.gnDivision.ne("")))
                    .then(tsOccHousingUnitPop.gnDivision)
                    .otherwise(tsOccHousingUnitPop.dsDivision).as("GN_ID"),
		    		tsOccHousingUnitPop.householdOwned,
		    		tsOccHousingUnitPop.rentGovermentOwned,
		    		tsOccHousingUnitPop.rentIndvslOwned,
		    		tsOccHousingUnitPop.rentFree,
		    		tsOccHousingUnitPop.encroached,
		    		tsOccHousingUnitPop.otherOccupied
    		    )
    		    .from(tsOccHousingUnitPop)
    		    .where(
    		    		tsOccHousingUnitPop.popmngId.in(searchDTO.getPopmngIdArr())
    		        	,eqGnId(searchDTO.getProvince(),searchDTO.getDistrict(),searchDTO.getDsDivision())
	    		)
    		    .fetch();
    	List<Map<String,Object>> resultTableData = new ArrayList<Map<String,Object>>();
    	if(tableResults != null) {
    		for(int i = 0; i < tableResults.size(); i++) {
    			Map<String,Object> tableData = new HashMap<String,Object>();
    			tableData.put("gnId", tableResults.get(i).get(new CaseBuilder()
                        .when(tsOccHousingUnitPop.gnDivision.isNotNull().and(tsOccHousingUnitPop.gnDivision.ne("")))
                        .then(tsOccHousingUnitPop.gnDivision)
                        .otherwise(tsOccHousingUnitPop.dsDivision).as("GN_ID")));
    			tableData.put("ownedByHouseholdMember", tableResults.get(i).get(tsOccHousingUnitPop.householdOwned));
    			tableData.put("rentLeaseGovernmentOwned", tableResults.get(i).get(tsOccHousingUnitPop.rentGovermentOwned));
    			tableData.put("rentLeasePrivatelyOwned", tableResults.get(i).get(tsOccHousingUnitPop.rentIndvslOwned));
    			tableData.put("rentFree", tableResults.get(i).get(tsOccHousingUnitPop.rentFree));
    			tableData.put("encroached", tableResults.get(i).get(tsOccHousingUnitPop.encroached));
    			tableData.put("otherOccupied", tableResults.get(i).get(tsOccHousingUnitPop.otherOccupied));
    			resultTableData.add(tableData);
    		}
    	}
    	dto.setTableData(resultTableData);
		dto.setStatisticsList(infoList);
        dto.setLabelArray(labelArr);
        return dto;
    }

	private BooleanExpression eqGnId(String provinceCd, String districtCd, String dsDivisionCd) {
	    if (provinceCd == null || districtCd == null || dsDivisionCd == null) {
	        return null;
	    }

	    // dsDivisionCd를 3자리 형식으로 변환
	    String formattedDsDivisionCd = String.format("%03d", Integer.parseInt(dsDivisionCd));
	    String likePattern = provinceCd + districtCd + formattedDsDivisionCd + "%";
	    StringPath gnIdPath = tsOccHousingUnitPop.gnId;
	    return gnIdPath.like(likePattern);
	}
}
