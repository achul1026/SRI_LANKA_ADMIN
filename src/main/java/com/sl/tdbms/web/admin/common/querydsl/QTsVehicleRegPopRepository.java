package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TsVehicleRegPop;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsVehicleRegPop;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsVehicleRegPopRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsVehicleRegPop tsVehicleRegPop = QTsVehicleRegPop.tsVehicleRegPop;
    
    public TsPopulationRsltStatisticsDTO getChartData(TsPopulationStatisticsSearchDTO searchDTO) {
    	List<TsVehicleRegPop> results = queryFactory
    		    .selectFrom(tsVehicleRegPop)
    		    .where(tsVehicleRegPop.popmngId.in(searchDTO.getPopmngIdArr()))
    		    .fetch();
    	
		TsPopulationRsltStatisticsDTO dto = new TsPopulationRsltStatisticsDTO();
		List<TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo> infoList = new ArrayList<>();

        String[] labelArr = {
            "January",    // jan
            "February",   // feb
            "March",      // mar
            "April",      // apr
            "May",        // may
            "June",       // jun
            "July",       // jul
            "August",     // aug
            "September",  // sep
            "October",    // oct
            "November",   // nov
            "December"    // dec
        };
        
		if(results != null && !results.isEmpty()) {
			for (TsVehicleRegPop result : results) {
			   TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo info = new TsPopulationRsltStatisticsDTO.PopulationRsltStatisticsInfo();
			   String title = result.getVhclCateg().toUpperCase();
			   
			   List<BigDecimal> resultCnt = Arrays.asList(
				   result.getJan(),
				   result.getFeb(),
				   result.getMar(),
				   result.getApr(),
				   result.getMay(),
				   result.getJun(),
				   result.getJul(),
				   result.getAug(),
				   result.getSep(),
				   result.getOct(),
				   result.getNov(),
				   result.getDec()
			   );
			   
			   info.setTitle(title);
			   info.setCountArray(resultCnt.toArray(new BigDecimal[0]));
			   infoList.add(info);
			}
    	}
		dto.setLabelArray(labelArr);
		dto.setStatisticsList(infoList);
        return dto;
    }
}
