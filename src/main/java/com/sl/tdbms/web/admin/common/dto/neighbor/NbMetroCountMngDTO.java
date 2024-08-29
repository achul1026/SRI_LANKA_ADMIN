package com.sl.tdbms.web.admin.common.dto.neighbor;

import lombok.*;

/**
 * packageName    : com.sl.tdbms.web.admin.common.dto.neighbor
 * fileName       : NbMetroCountMngDTO.java
 * author         : kjg08
 * date           : 24. 7. 22.
 * description    : 메트로 카운트 데이터를 담기 위한 DTO 클래스.
 * 이 클래스는 특정 지점에서 수집된 교통 데이터를 포함하며,
 * 각 필드는 데이터의 특정 속성(예: 시간, 속도, 차량 유형 등)을 나타냅니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 22.        kjg08           최초 생성
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NbMetroCountMngDTO {

    /**
     * 시퀀스 번호, 각 데이터 레코드를 고유하게 식별하는 값입니다.
     */
    private String sqno;

    /**
     * 데이터를 수집한 날짜 및 시간.
     * 형식: YYYY-MM-DD hh:mm
     */
    private String dateTime;

    /**
     * 설치 위치 ID, 데이터가 수집된 위치를 식별합니다.
     */
    private String instllcId;

    /**
     * 차량의 방향을 나타냅니다.
     * 0: 콜롬보 방향, 1: 콜롬보 반대 방향.
     */
    private String vhclDrct;

    /**
     * 차량 유형을 나타냅니다.
     * 예: 승용차, 버스, 트럭 등.
     */
    private String vehicleType;

    /**
     * 차량의 속도를 나타냅니다.
     * 음수 값은 허용되지 않으며, 비정상적으로 높은 값도 허용되지 않습니다.
     */
    private double speed;

    /**
     * 방향 코드를 나타냅니다.
     * 시스템 내부에서 사용되는 코드로, 특정 방향을 식별합니다.
     */
    private String directionCode;

    /**
     * 차량의 휠베이스(축간 거리)를 나타냅니다.
     */
    private double wheelbase;

    /**
     * 헤드웨이, 동일한 방향으로 주행하는 마지막 차량의 첫 축과 현재 차량의 첫 축 사이의 시간 간격을 나타냅니다.
     */
    private double headway;

    /**
     * 갭, 동일한 방향으로 주행하는 마지막 차량의 마지막 축과 현재 차량의 첫 축 사이의 시간 간격을 나타냅니다.
     */
    private double gap;

    /**
     * 차량의 축 수를 나타냅니다.
     * 예: 2축, 4축, 6축 트럭.
     */
    private int axleCount;

    /**
     * 차량의 축 그룹 수를 나타냅니다.
     * 시스템이 차량을 그룹화하는 방식에 따라 다릅니다.
     */
    private int groupCount;

    /**
     * 로 값, 센서의 상관 계수를 나타내며 데이터 분석에 사용되는 특정 지표입니다.
     */
    private double rhoValue;

    /**
     * 차량의 분류를 나타냅니다.
     * 예: 승용차, 경형 트럭, 대형 트럭 등.
     */
    private int vehicleClass;

    /**
     * 고유한 차량 번호 또는 데이터 레코드 번호를 나타냅니다.
     */
    private String number;

    /**
     * 데이터셋 식별자, 해당 데이터가 포함된 데이터셋을 식별합니다.
     */
    private String datasetIdentifier;

    /**
     * 트리거 번호, 특정 이벤트 또는 데이터를 고유하게 식별합니다.
     */
    private String triggerNumber;

    /**
     * 차량의 높이 정보를 나타냅니다.
     */
    private String height;

    private int lineNumber;
}
