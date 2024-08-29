package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortHourYy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortHourYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortHourYy tsTravelTransfortHourYy = QTsTravelTransfortHourYy.tsTravelTransfortHourYy;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelTransfortCntList(TlExmnRsltStatisticsSearchDTO searchDTO){


		return null;
	}

}
