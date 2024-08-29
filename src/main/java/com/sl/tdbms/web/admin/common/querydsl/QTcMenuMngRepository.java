package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngDTO;
import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngInfoDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcMenuMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcMenuMngRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTcMenuMng tcMenuMng = QTcMenuMng.tcMenuMng;
	
	private QTcMenuMng parentTcMenuMng = new QTcMenuMng("parentTcMenuMng");
	
	private QTcCdInfo parentTcCdInfo = new QTcCdInfo("parentTcCdInfo");
	
	private QTcCdInfo tcCdInfo = new QTcCdInfo("tcCdInfo");
	
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
						parentTcMenuMng.uppermenuCd,
//						parentTcMenuMng.menunmEng, parentTcMenuMng.menunmKor, parentTcMenuMng.menunmSin, 
						QRepositorySupport.getCodeInfoNamePath(parentTcCdInfo).as("menuNm"),
						parentTcMenuMng.uppermenuUrlpttrn, parentTcMenuMng.menuUrlpttrn, parentTcMenuMng.bscmenuYn,
						parentTcMenuMng.useYn, parentTcMenuMng.registDt,parentTcMenuMng.menuCd,
						Expressions.stringTemplate(
								"jsonb_agg(" + "jsonb_build_object(" + "'menuId', {0}, 'menuNm', {1},"
										+ "'menuUrlpttrn', {2}, 'uppermenuUrlpttrn', {3}," + "'useYn', {4}, 'menuSqno', {5},"
										+ "'uppermenuCd', {6} , 'menuCd', {7}" + ")" + ")",
										tcMenuMng.menuId, QRepositorySupport.getCodeInfoNamePath(tcCdInfo), tcMenuMng.menuUrlpttrn, tcMenuMng.uppermenuUrlpttrn, tcMenuMng.useYn,
										tcMenuMng.menuSqno, tcMenuMng.uppermenuCd,tcMenuMng.menuCd).as("jsonSubMenuList")))
				.from(parentTcMenuMng)
				.leftJoin(tcMenuMng).on(parentTcMenuMng.menuCd.eq(tcMenuMng.uppermenuCd)
						.and(QRepositorySupport.toEqExpression(tcMenuMng.useYn, tcMenuMngDTO.getUseYn())))
				.leftJoin(parentTcCdInfo).on(parentTcCdInfo.cd.eq(parentTcMenuMng.menuCd))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tcMenuMng.menuCd))
				.where(parentTcMenuMng.uppermenuCd.isNull()
						.and(QRepositorySupport.toEqExpression(parentTcMenuMng.useYn, tcMenuMngDTO.getUseYn()))
						.and(QRepositorySupport.toEqExpression(parentTcMenuMng.bscmenuYn, tcMenuMngDTO.getBscmenuYn())))
				.groupBy(parentTcMenuMng.menuId, QRepositorySupport.getCodeInfoNamePath(parentTcCdInfo)).orderBy(parentTcMenuMng.menuSqno.asc()).fetch();
		return result;
	}

	/**
	  * @Method Name : getMenuInfo
	  * @작성자 : SM.KIM
	  * @Method 설명 : 메뉴 정보 조회
	  * @param menuInfo
	  * @return
	  */
	public TcMenuMngInfoDTO getMenuInfo(TcMenuMngInfoDTO menuInfo) {
		TcMenuMngInfoDTO result = queryFactory
				.select(Projections.bean(TcMenuMngInfoDTO.class, QRepositorySupport.getCodeInfoNamePath(parentTcCdInfo).as("menuNm"), parentTcMenuMng.menuUrlpttrn,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("subMenuNm"), tcMenuMng.menuUrlpttrn.as("subMenuUrl")))
				.from(parentTcMenuMng)
				.leftJoin(tcMenuMng).on(tcMenuMng.uppermenuCd.eq(parentTcMenuMng.menuCd).and(tcMenuMng.menuUrlpttrn.eq(menuInfo.getMenuUrlpttrn())))
				.leftJoin(parentTcCdInfo).on(parentTcCdInfo.cd.eq(parentTcMenuMng.menuCd))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tcMenuMng.menuCd))
				.where(parentTcMenuMng.uppermenuUrlpttrn.eq(menuInfo.getUppermenuUrlpttrn())).fetchOne();
		return result;
	}
	
	/**
	  * @Method Name : getMenuInfoById
	  * @작성일 : 2024. 6. 7.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 메뉴 정보 조회 by menuId
	  * @param menuId
	  * @return
	  */
	public TcMenuMngInfoDTO getMenuInfoById(String menuId) {
		TcMenuMngInfoDTO result = queryFactory
				.select(Projections.bean(TcMenuMngInfoDTO.class, tcMenuMng.menuCd, tcMenuMng.bscmenuYn, tcMenuMng.menuId, tcMenuMng.menuSqno, tcMenuMng.uppermenuCd
						, tcMenuMng.uppermenuUrlpttrn, tcMenuMng.useYn, QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("menuNm"), tcMenuMng.menuUrlpttrn))
				.from(tcMenuMng)
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tcMenuMng.menuCd))
				.where(tcMenuMng.menuId.eq(menuId)).groupBy(tcMenuMng.menuId, QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).fetchOne();
		return result;
	}

}
