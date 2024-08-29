package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsTravelFeeInfoYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelFeeInfoYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelFeeInfoYy tsTravelFeeInfoYy = QTsTravelFeeInfoYy.tsTravelFeeInfoYy;


	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelFeeTypeList(TlExmnRsltStatisticsSearchDTO searchDTO){


		return null;
	}

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getAvgTravelModeTransportationList(TlExmnRsltStatisticsSearchDTO searchDTO){


		return null;
	}
}
