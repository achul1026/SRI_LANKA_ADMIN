package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDetailDTO.TcMenuAuthInfo;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuAuthDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuAuthDTO.SubAuthMenuInfo;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngMenuCheckDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcMenuAuth;
import com.sri.lanka.traffic.admin.common.entity.QTcMenuMng;
import com.sri.lanka.traffic.admin.common.entity.TcMenuAuth;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcMenuAuthRepository {

	private final JPAQueryFactory queryFactory;

	private QTcMenuAuth tcMenuAuth = QTcMenuAuth.tcMenuAuth;

	private QTcMenuMng tcMenuMng = QTcMenuMng.tcMenuMng;

	private QTcMenuMng parentTcMenuMng = new QTcMenuMng("parentTcMenuMng");

	/**
	 * @Method Name : getAuthMenuList
	 * @작성일 : 2024. 1. 26.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 권한 메뉴 목록 호출
	 * @param authId
	 * @return
	 */
	public List<TcMenuAuthInfo> getAuthMenuList(String authId) {

		// 각 메뉴 ID 별 auth_menu_id 조회
		Map<String, String> menuauthIdMap = queryFactory.select(tcMenuAuth.menuId, tcMenuAuth.menuauthId)
														.from(tcMenuAuth).where(tcMenuAuth.authId.eq(authId)).fetch().stream().collect(Collectors
														.toMap(tuple -> tuple.get(tcMenuAuth.menuId), tuple -> tuple.get(tcMenuAuth.menuauthId)));

		List<TcMenuAuthInfo> result = queryFactory.select(Projections.bean(
															TcMenuAuthInfo.class, 
															parentTcMenuMng.menuId, 
															parentTcMenuMng.uppermenuCd, 
															parentTcMenuMng.menuNm,
															Expressions.stringTemplate("STRING_AGG({0}, ',')", new CaseBuilder()
																	.when(tcMenuAuth.srchYn.eq("Y").or(tcMenuAuth.inputYn.eq("Y"))
																			.or(tcMenuAuth.updtYn.eq("Y")).or(tcMenuAuth.delYn.eq("Y")))
																	.then("Y").otherwise("N")).as("subMenuCheckedVal"),
															Expressions.stringTemplate(
																	"jsonb_agg(" + "jsonb_build_object(" + "'menuId', {0}, 'menuNm', {1},"
																			+ "'menuSqno', {2},'uppermenuCd', {3},"
																			+ "'inputYn', {4},'srchYn', {5}," + "'updtYn', {6},'delYn', {7},"
																			+ "'menuauthId', {8}" + ")" + ")",
																			tcMenuMng.menuId, tcMenuMng.menuNm, tcMenuMng.menuSqno, tcMenuMng.uppermenuCd,
																			tcMenuAuth.inputYn.coalesce("N"), tcMenuAuth.srchYn.coalesce("N"),
																			tcMenuAuth.updtYn.coalesce("N"), tcMenuAuth.delYn.coalesce("N"),
																			tcMenuAuth.menuauthId).as("jsonSubMenuList")))
				.from(parentTcMenuMng).leftJoin(tcMenuMng).on(parentTcMenuMng.menuCd.eq(tcMenuMng.uppermenuCd))
				.join(tcMenuAuth).on(tcMenuMng.menuId.eq(tcMenuAuth.menuId).and(tcMenuAuth.authId.eq(authId)))
				.where(parentTcMenuMng.uppermenuCd.isNull().and(parentTcMenuMng.useYn.eq("Y"))
						.and(parentTcMenuMng.bscmenuYn.eq("N")))
				.groupBy(parentTcMenuMng.menuId).orderBy(parentTcMenuMng.menuSqno.asc()).fetch();
		
		result.forEach(authMenuInfo -> {
			String menuId = authMenuInfo.getMenuId();
			if (menuauthIdMap.containsKey(menuId)) {
				authMenuInfo.setMenuauthId(menuauthIdMap.get(menuId));
			}
		});

		return result;
	}

	/**
	 * @Method Name : getAuthMenuListByMenuIdArr
	 * @작성일 : 2024. 1. 26.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 권한 메뉴 목록
	 * @param menuIdArr
	 * @return
	 */
	public List<TcMenuAuth> getAuthMenuListByMenuIdArr(String[] menuIdArr) {

		return queryFactory.select(tcMenuAuth).from(tcMenuAuth).where(tcMenuAuth.menuId.in(menuIdArr)).fetch();
	}

	/**
	 * @Method Name : getSideBarMenuList
	 * @작성일 : 2024. 1. 29.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 사이드바 메뉴 목록 조회
	 * @param authId
	 * @return
	 */
	public List<TcMenuAuthDTO> getSideAuthMenuList(String authId) {
		List<TcMenuAuthDTO> result = queryFactory
				.select(Projections.bean(TcMenuAuthDTO.class, tcMenuAuth.authId, parentTcMenuMng.menuId, parentTcMenuMng.menuCd,
						parentTcMenuMng.uppermenuCd, parentTcMenuMng.menuNm, parentTcMenuMng.uppermenuUrlpttrn,
						parentTcMenuMng.menuUrlpttrn))
				.from(parentTcMenuMng).leftJoin(tcMenuAuth)
				.on(parentTcMenuMng.menuId.eq(tcMenuAuth.menuId).and(tcMenuAuth.authId.eq(authId)))
				.where(parentTcMenuMng.uppermenuCd.isNull()
						.and(parentTcMenuMng.useYn.eq("Y").and(tcMenuAuth.srchYn.eq("Y")).or(parentTcMenuMng.bscmenuYn.eq("Y"))))
				.orderBy(parentTcMenuMng.menuSqno.asc()).fetch();
		if (!CommonUtils.isNull(result)) {
			result.stream().filter(x -> !CommonUtils.isNull(x))
					.forEach(parentMenuList -> parentMenuList.setSubMenuList(queryFactory
							.select(Projections.bean(SubAuthMenuInfo.class, tcMenuAuth.authId, tcMenuMng.menuId, tcMenuMng.menuCd,
									tcMenuMng.uppermenuCd, tcMenuMng.menuNm, tcMenuMng.uppermenuUrlpttrn, tcMenuMng.menuUrlpttrn))
							.from(tcMenuMng).join(tcMenuAuth)
							.on(tcMenuMng.menuId.eq(tcMenuAuth.menuId).and(tcMenuAuth.authId.eq(authId)))
							.where(tcMenuMng.uppermenuCd.eq(parentMenuList.getMenuCd())
									.and(tcMenuMng.useYn.eq("Y").and(tcMenuAuth.srchYn.eq("Y"))))
							.orderBy(tcMenuMng.menuSqno.asc()).fetch()));

		}
		return result;
	}

	/**
	 * @Method Name : getAuthMenuListByAuthority
	 * @작성일 : 2024. 3. 14.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 권한과 일치하는 메뉴 목록 조회
	 * @param menuAuth
	 * @return
	 */
	public List<TcMenuMngMenuCheckDTO> getAuthMenuListByAuthority(TcMenuAuth menuAuth) {
		List<TcMenuMngMenuCheckDTO> result = queryFactory
				.select(Projections.bean(TcMenuMngMenuCheckDTO.class, tcMenuMng.menuId, tcMenuMng.uppermenuUrlpttrn,
						tcMenuAuth.inputYn, tcMenuAuth.srchYn, tcMenuAuth.updtYn, tcMenuAuth.delYn))
				.from(tcMenuAuth).innerJoin(tcMenuMng).on(tcMenuAuth.menuId.eq(tcMenuMng.menuId))
				.where(tcMenuMng.uppermenuCd.isNotNull().and(tcMenuAuth.authId.eq(menuAuth.getAuthId()))
						.and(authOption(menuAuth)))
				.fetch();

		return result;
	}

	/**
	 * @Method Name : authOption
	 * @작성일 : 2024. 3. 14.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 권한 조건 설정
	 * @param menuAuth
	 * @return
	 */
	private BooleanExpression authOption(TcMenuAuth menuAuth) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(tcMenuAuth)) {
			if (!CommonUtils.isNull(menuAuth.getInputYn())) {
				result = QRepositorySupport.toEqExpression(tcMenuAuth.inputYn, menuAuth.getInputYn());
			} else if (!CommonUtils.isNull(menuAuth.getSrchYn())) {
				result = QRepositorySupport.toEqExpression(tcMenuAuth.srchYn, menuAuth.getSrchYn());
			} else if (!CommonUtils.isNull(menuAuth.getUpdtYn())) {
				result = QRepositorySupport.toEqExpression(tcMenuAuth.updtYn, menuAuth.getUpdtYn());
			} else {
				result = QRepositorySupport.toEqExpression(tcMenuAuth.delYn, menuAuth.getDelYn());
			}
		}
		return result;
	}
}
