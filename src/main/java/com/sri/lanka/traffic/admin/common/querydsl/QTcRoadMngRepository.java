package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.location.TcRoadMngDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcRoadMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcRoadMngRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTcRoadMng tcRoadMng = QTcRoadMng.tcRoadMng;
	
	/**
	  * @Method Name : getTcRoadList
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : SM.KIM
	  * @Method 설명 : TcRoad 리스트 출력
	  * @return
	  */
	public List<TcRoadMngDTO> getTcRoadList() {
		List<TcRoadMngDTO> result = queryFactory
				.select(Projections.bean(TcRoadMngDTO.class,
												tcRoadMng.roadCd, 
												tcRoadMng.roadDescr))
				.from(tcRoadMng).fetch();
		return result;
	}

}
