package com.sl.tdbms.web.admin.common.dto.neighbor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * packageName    : com.sl.tdbms.web.admin.common.dto.neighbor
 * fileName       : StatisticsSummaryDTO.java
 * author         : kjg08
 * date           : 24. 8. 7.
 * description    : 통계 데이터의 요약 정보를 담는 제네릭 DTO 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 8. 7.        kjg08           최초 생성
 * 24. 8. 14.       kjg08           주석 추가 및 예외 처리 개선
 */
@Getter
@Setter
@ToString
public class StatisticsSummaryDTO<T> {
    private final long totalCount;    // 전체 데이터 개수
    private final int totalPages;     // 전체 페이지 수
    private final int currentPage;    // 현재 페이지 번호
    private final List<T> content;    // 현재 페이지의 데이터 목록

    /**
     * StatisticsSummaryDTO의 생성자
     *
     * @param totalCount  전체 데이터 개수
     * @param totalPages  전체 페이지 수
     * @param currentPage 현재 페이지 번호
     * @param content     현재 페이지의 데이터 목록
     * @throws IllegalArgumentException 유효하지 않은 입력값이 있을 경우
     */
    public StatisticsSummaryDTO(long totalCount, int totalPages, int currentPage, List<T> content) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.content = content != null ? content : Collections.emptyList();
    }


}