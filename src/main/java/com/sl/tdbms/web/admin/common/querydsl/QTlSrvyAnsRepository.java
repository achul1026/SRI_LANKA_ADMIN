package com.sl.tdbms.web.admin.common.querydsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyExcelDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyExcelResultDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTlExmnRslt;
import com.sl.tdbms.web.admin.common.entity.QTlSrvyAns;
import com.sl.tdbms.web.admin.common.entity.QTlSrvyRslt;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTmSrvyAns;
import com.sl.tdbms.web.admin.common.entity.QTmSrvyInfo;
import com.sl.tdbms.web.admin.common.entity.QTmSrvyQstn;
import com.sl.tdbms.web.admin.common.entity.QTmSrvySect;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlSrvyAnsRepository {

	private final JPAQueryFactory queryFactory;

	private QTlExmnRslt tlExmnRslt = QTlExmnRslt.tlExmnRslt;

	private QTlSrvyRslt tlSrvyRslt = QTlSrvyRslt.tlSrvyRslt;
	
	private QTlSrvyAns tlSrvyAns = QTlSrvyAns.tlSrvyAns;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	private QTmSrvyInfo tmSrvyInfo = QTmSrvyInfo.tmSrvyInfo;

	private QTmSrvyQstn tmSrvyQstn = QTmSrvyQstn.tmSrvyQstn;

	private QTmSrvySect tmSrvySect = QTmSrvySect.tmSrvySect;

	private QTmSrvyAns tmSrvyAns = QTmSrvyAns.tmSrvyAns;
	
	public List<TlSrvyExcelDTO> getSurveyRsltListForSearchOption(TlExmnRsltStatisticsSearchDTO searchDTO,ExmnTypeCd exmnTypeCd){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tlSrvyAns.ansCnts, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tlSrvyAns.ansCnts, 1, searchDTO.getDestinationSubstringIdx());
		
        StringTemplate arrayAggSectSqno = Expressions.stringTemplate("array_agg_int({0})",tlSrvyAns.sectSqno);
        
        NumberTemplate<Integer> extractedYear = Expressions.numberTemplate(Integer.class, "EXTRACT(YEAR FROM {0})", tlSrvyRslt.exmnstartDt);

        List<Tuple> results = queryFactory
            .select(tlSrvyAns.srvyrsltId, arrayAggSectSqno)
            .from(tlSrvyAns)
            .innerJoin(tlSrvyRslt).on(tlSrvyAns.srvyrsltId.eq(tlSrvyRslt.srvyrsltId))
            .innerJoin(tlExmnRslt).on(tlSrvyRslt.exmnrsltId.eq(tlExmnRslt.exmnrsltId))
            .where(
                tlExmnRslt.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE)
                .and(tlExmnRslt.exmnType.eq(exmnTypeCd))
                .and(tlSrvyAns.srvyMetadataCd.in("SMD055", "SMD061"))
                .and(
                    tlSrvyAns.srvyMetadataCd.eq("SMD055").and(departureTazCode.eq(searchDTO.getSearchCd()))
                    .or(tlSrvyAns.srvyMetadataCd.eq("SMD061").and(destinationTazCode.eq(searchDTO.getSearchCd())))
                )
                .and(extractedYear.eq(Integer.parseInt(searchDTO.getSearchDate())))
           )
            .groupBy(tlSrvyAns.srvyrsltId)
            .having(tlSrvyAns.srvyMetadataCd.countDistinct().eq(2L))
            .fetch();
        
        List<TlSrvyExcelDTO> resultList = results.stream().map(tuple -> {
            String srvyrsltId = tuple.get(tlSrvyAns.srvyrsltId);
            Object arrayValue = tuple.get(arrayAggSectSqno);
            List<BigDecimal> seqArr;
            if (arrayValue instanceof BigDecimal[]) {
                seqArr = Arrays.asList((BigDecimal[]) arrayValue);
            } else if (arrayValue instanceof String) {
                seqArr = Stream.of(((String) arrayValue).replaceAll("[{}]", "").split(","))
                               .map(BigDecimal::new)
                               .distinct()
                               .collect(Collectors.toList());
            } else {
                seqArr = Collections.emptyList();
            }
            return new TlSrvyExcelDTO(srvyrsltId, seqArr);
    	}).sorted((result1, result2) -> Integer.compare(
            result2.getSectSqnoArr().size(),
            result1.getSectSqnoArr().size()
        )).collect(Collectors.toList());

        return resultList;
	}

	public List<CommonCdDTO> getSurveyHeaderInfo(TlSrvyExcelDTO headerSearchDTO) {
		List<CommonCdDTO> headList = new ArrayList<CommonCdDTO>();
		String lang = LocaleContextHolder.getLocale().toString();
		StringExpression cdNm = getCdNm(lang);
		
		 List<CommonCdDTO> results = queryFactory
	        .select(Projections.bean(CommonCdDTO.class,
	        		tcCdInfo.cd,
	        		cdNm.as("cdNm")
    		))
	        .from(tlSrvyAns)
	        .innerJoin(tcCdInfo).on(tcCdInfo.cd.eq(tlSrvyAns.srvyMetadataCd))
	        .where(
	            tlSrvyAns.srvyrsltId.eq(headerSearchDTO.getSrvyrsltId())
	            .and(tlSrvyAns.sectSqno.in(headerSearchDTO.getSectSqnoArr()))
	        )
	        .orderBy(tlSrvyAns.sectSqno.asc())
	        .fetch();
		
		headList.addAll(results);
		
		return headList;
	}
	
	public StringExpression getCdNm(String lang) {
		switch(lang) {
        case "kor":
            return tcCdInfo.cdnmKor;
        case "eng":
            return tcCdInfo.cdnmEng;
        case "sin":
            return tcCdInfo.cdnmSin;
        default:
            return tcCdInfo.cdnmSin;
		}
	}


	public List<TlSrvyExcelResultDTO> getSurveyResults(TlSrvyExcelDTO searchDTO) {
        StringTemplate jsonbAggAnsCnts = Expressions.stringTemplate(
            "jsonb_agg_cd_ordered_asc_triple({0},{1}, {2},{3},{4})",
            tlSrvyAns.srvyMetadataCd,
            tlSrvyAns.ansCnts,
            tlSrvyAns.sectType,
            tlSrvyAns.sectSqno,
            tlSrvyAns.qstnSqno
        );

	    List<TlSrvyExcelResultDTO> results = queryFactory
	        .select(Projections.bean(TlSrvyExcelResultDTO.class,
	        		tlExmnRslt.exmnType.as("exmnType"),
	        		tlExmnRslt.cordonLine.as("cordonLine"),
	        		tlExmnRslt.tollBooth.as("tollBooth"),
	                tlSrvyRslt.pollsterNm.as("pollsterNm"),
	                tlSrvyRslt.pollsterTel.as("pollsterTel"),
	                jsonbAggAnsCnts.as("ansCntsArray")
	        ))
	        .from(tlSrvyAns)
	        .innerJoin(tlSrvyRslt).on(tlSrvyAns.srvyrsltId.eq(tlSrvyRslt.srvyrsltId))
	        .innerJoin(tlExmnRslt).on(tlSrvyRslt.exmnrsltId.eq(tlExmnRslt.exmnrsltId))
	        .where(
	            tlSrvyAns.srvyrsltId.eq(searchDTO.getSrvyrsltId())
	            .and(tlSrvyAns.sectSqno.in(searchDTO.getSectSqnoArr()))
	        )
	        .groupBy(tlExmnRslt.exmnType,tlExmnRslt.cordonLine,tlExmnRslt.tollBooth,tlSrvyRslt.pollsterNm,tlSrvyRslt.pollsterTel)
	        .fetch();

	    return results;
	}
}
