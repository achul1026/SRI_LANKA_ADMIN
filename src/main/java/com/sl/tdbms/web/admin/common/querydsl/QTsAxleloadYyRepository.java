package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TsTrafficStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsAxleloadYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsAxleloadYyRepository {
	private final JPAQueryFactory queryFactory;
	
	private QTsAxleloadYy axleloadYy = QTsAxleloadYy.tsAxleloadYy;
	
	/**
	 * @Method Name : getTsAxleloadYyListBySearchDTO
	 * @작성일 : 2024. 8. 13.
	 * @작성자 : KC.KIM
	 * @Method 설명 : axleload 통계년도 조회
	 */
	public List<String> getTsAxleloadYyListBySearchDTO(TsTrafficStatisticsSearchDTO searchDTO) {
		List<String> result = queryFactory
				.select(axleloadYy.statsYy)
				.from(axleloadYy)
				.where(axleloadYy.instllcId.eq(searchDTO.getSiteId()))
				.groupBy(axleloadYy.statsYy)
				.orderBy(axleloadYy.statsYy.desc())
				.fetch();
		return result;
	}
}
