package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngInfoDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcMenuMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcMenuMngRepository {

	private final JPAQueryFactory queryFactory;
	private QTcMenuMng tcMenuMng = QTcMenuMng.tcMenuMng;
	private QTcMenuMng parentTcMenuMng = new QTcMenuMng("parentTcMenuMng");

	/**
	 * @Method Name : getMenuList
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 메뉴 목록 조회
	 * @param commonDTO
	 * @param paging
	 * @return
	 */
	public List<TcMenuMngDTO> getMenuList(TcMenuMngDTO tcMenuMngDTO) {
		List<TcMenuMngDTO> result = queryFactory
				.select(Projections.bean(TcMenuMngDTO.class, parentTcMenuMng.menuSqno, parentTcMenuMng.menuId,
						parentTcMenuMng.uppermenuCd, parentTcMenuMng.menuNm, parentTcMenuMng.uppermenuUrlpttrn,
						parentTcMenuMng.menuUrlpttrn, parentTcMenuMng.bscmenuYn, parentTcMenuMng.useYn, parentTcMenuMng.registDt,parentTcMenuMng.menuCd,
						Expressions.stringTemplate(
								"jsonb_agg(" + "jsonb_build_object(" + "'menuId', {0}, 'menuNm', {1},"
										+ "'menuUrlpttrn', {2}, 'uppermenuUrlpttrn', {3}," + "'useYn', {4}, 'menuSqno', {5},"
										+ "'uppermenuCd', {6} , 'menuCd', {7}" + ")" + ")",
										tcMenuMng.menuId, tcMenuMng.menuNm, tcMenuMng.menuUrlpttrn, tcMenuMng.uppermenuUrlpttrn, tcMenuMng.useYn,
										tcMenuMng.menuSqno, tcMenuMng.uppermenuCd,tcMenuMng.menuCd).as("jsonSubMenuList")))
				.from(parentTcMenuMng).leftJoin(tcMenuMng)
				.on(parentTcMenuMng.menuCd.eq(tcMenuMng.uppermenuCd)
						.and(QRepositorySupport.toEqExpression(tcMenuMng.useYn, tcMenuMngDTO.getUseYn())))
				.where(parentTcMenuMng.uppermenuCd.isNull()
						.and(QRepositorySupport.toEqExpression(parentTcMenuMng.useYn, tcMenuMngDTO.getUseYn()))
						.and(QRepositorySupport.toEqExpression(parentTcMenuMng.bscmenuYn, tcMenuMngDTO.getBscmenuYn())))
				.groupBy(parentTcMenuMng.menuId).orderBy(parentTcMenuMng.menuSqno.asc()).fetch();
		return result;
	}

	public TcMenuMngInfoDTO getMenuInfo(TcMenuMngInfoDTO menuInfo) {
		TcMenuMngInfoDTO result = queryFactory
				.select(Projections.bean(TcMenuMngInfoDTO.class, parentTcMenuMng.menuNm, parentTcMenuMng.menuUrlpttrn,
						tcMenuMng.menuNm.as("subMenuNm"), tcMenuMng.menuUrlpttrn.as("subMenuUrl")))
				.from(parentTcMenuMng).leftJoin(tcMenuMng)
				.on(tcMenuMng.uppermenuCd.eq(parentTcMenuMng.menuCd).and(tcMenuMng.menuUrlpttrn.eq(menuInfo.getMenuUrlpttrn())))
				.where(parentTcMenuMng.uppermenuUrlpttrn.eq(menuInfo.getUppermenuUrlpttrn())).fetchOne();
		return result;
	}

}
