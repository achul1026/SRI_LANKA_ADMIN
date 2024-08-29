package com.sl.tdbms.web.admin.common.dto.neighbor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * packageName    : com.sl.tdbms.web.admin.common.dto.neighbor
 * fileName       : NbMetroStatisticsDTO.java
 * author         : kjg08
 * date           : 24. 7. 25.
 * description    : METRO 교통 통계 데이터를 위한 DTO 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 25.        kjg08           최초 생성
 * 24. 8. 14.        kjg08           주석 추가 및 예외 처리 개선
 */

@Getter
@Setter
@ToString
public class NbMetroStatisticsDTO {
    private String routeNo;           // 도로 번호
    private String nameOfRoad;        // 도로명
    private String prov;              // 지역(도)
    private String ce;                // 수석 엔지니어 부서
    private Double lat;               // 위도
    private Double lon;               // 경도
    private String typeOfCount;       // 계수 유형
    private String dateOfCount;       // 계수 날짜
    private Integer totalNoOfVeh;     // 총 차량 수
    private Integer hrsOfCount;       // 하루간 총 계수 시간

    // 차종별 비율 (%)
    private Double mclPercentage;     // 오토바이
    private Double twlPercentage;     // 삼륜차
    private Double carPercentage;     // 승용차
    private Double lgvPercentage;     // 경트럭
    private Double mpgvPercentage;    // 중형 승용차
    private Double lpgvPercentage;    // 대형 승용차
    private Double hg3Percentage;     // 대형 트럭 3
    private Double ag3Percentage;     // 특수 차량 3
    private Double ag4Percentage;     // 특수 차량 4
    private Double ag5Percentage;     // 특수 차량 5
    private Double ag6Percentage;     // 특수 차량 6

    private Double cumPercentageOfVeh; // 누적 차량 비율

    // 차종별 수량
    private Integer mclCount;         // 오토바이 수
    private Integer twlCount;         // 삼륜차 수
    private Integer carCount;         // 승용차 수
    private Integer lgvCount;         // 경트럭 수
    private Integer mpgvCount;        // 중형 승용차 수
    private Integer lpgvCount;        // 대형 승용차 수
    private Integer hg3Count;         // 대형 트럭 3 수
    private Integer ag3Count;         // 특수 차량 3 수
    private Integer ag4Count;         // 특수 차량 4 수
    private Integer ag5Count;         // 특수 차량 5 수
    private Integer ag6Count;         // 특수 차량 6 수

    private Integer total;            // 총합

    public NbMetroStatisticsDTO() {
        // 기본 생성자
    }

    /**
     * 위도와 경도의 유효성을 검사합니다.
     *
     * @throws IllegalArgumentException 위도나 경도가 유효하지 않은 범위일 경우
     */
    public void validateCoordinates() {
        if (lat != null && (lat < -90 || lat > 90)) {
            throw new IllegalArgumentException("유효하지 않은 위도 값입니다: " + lat);
        }
        if (lon != null && (lon < -180 || lon > 180)) {
            throw new IllegalArgumentException("유효하지 않은 경도 값입니다: " + lon);
        }
    }


    /**
     * 각 차종의 수량 합계가 total 필드와 일치하는지 검증합니다.
     *
     * @throws IllegalStateException 수량 합계가 total과 일치하지 않을 경우
     */
    public void validateTotalCount() {
        int calculatedTotal = Stream.of(mclCount, twlCount, carCount, lgvCount,
                        mpgvCount, lpgvCount, hg3Count, ag3Count,
                        ag4Count, ag5Count, ag6Count)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        if (total != null && calculatedTotal != total) {
            throw new IllegalStateException("차종별 수량의 합계가 total 필드와 일치하지 않습니다. 계산된 합계: " + calculatedTotal + ", total 필드: " + total);
        }
    }
}