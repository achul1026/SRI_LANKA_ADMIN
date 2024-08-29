package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationFileDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTsPopFile;
import com.sl.tdbms.web.admin.common.entity.QTsPopMng;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTsPopFileRepository {

	private final JPAQueryFactory queryFactory;

	private QTsPopFile tsPopFile = QTsPopFile.tsPopFile;

	private QTsPopMng tsPopMng = QTsPopMng.tsPopMng;

	public List<TsPopulationFileDTO> getPopulationFileList(TsPopulationStatisticsSearchDTO searchDTO) {
		List<TsPopulationFileDTO> result = queryFactory.select(
						Projections.bean(TsPopulationFileDTO.class,
								tsPopMng.popmngId.as("popmngId"),
								tsPopFile.orgFileNm.as("orgFileNm"),
								tsPopFile.filePath.as("filePath"),
								tsPopFile.fileNm.as("fileNm"),
								tsPopFile.fileSize.as("fileSize")
								))
				.from(tsPopMng)
				.leftJoin(tsPopMng.tsPopFile, tsPopFile)
				.fetch();
		return result;
	}
}
