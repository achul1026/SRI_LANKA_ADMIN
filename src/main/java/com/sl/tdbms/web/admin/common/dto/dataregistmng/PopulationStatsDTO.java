package com.sl.tdbms.web.admin.common.dto.dataregistmng;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PopulationStatsDTO {
	//공통
	private String popmngId;	//부모테이블 고유Id
	private String fileId;		//부모테이블 파일 고유 Id
	private String district;	
	private String dsDivision;
	private String dsdId;
	
	//지역별 #TsRegnPop
	private BigDecimal totalPopltncnt; //인구수
	//연령별 #TsAgePop
	private BigDecimal ageGroup0To4;	
	private BigDecimal ageGroup5To9;
	private BigDecimal ageGroup10To14;
	private BigDecimal ageGroup15To19;
	private BigDecimal ageGroup20To24;
	private BigDecimal ageGroup25To29;
	private BigDecimal ageGroup30To34;
	private BigDecimal ageGroup35To39;
	private BigDecimal ageGroup40To44;
	private BigDecimal ageGroup45To49;
	private BigDecimal ageGroup50To54;
	private BigDecimal ageGroup55To59;
	private BigDecimal ageGroup60To64;
	private BigDecimal ageGroup65To69;
	private BigDecimal ageGroup70To74;
	private BigDecimal ageGroup75To79;
	private BigDecimal ageGroup80To84;
	private BigDecimal ageGroup85To89;
	private BigDecimal ageGroup90To94;
	private BigDecimal ageGroup95AndAbove;
	
	//경제 활동 인구 통계 #TsEcnmcActPop
    private BigDecimal totalPopulationAged15AndAbove;
    private BigDecimal employed;
    private BigDecimal unemployed;
    private BigDecimal economicallyNotActive;
    
    //학생수 통계 #TsStdntPop
    private BigDecimal primarySchoolStudents;
    private BigDecimal secondarySchoolStudents;
    private BigDecimal gceOlStudents;
    private BigDecimal gceAlStudents;
    private BigDecimal degreeAndAboveStudents;
    private BigDecimal noSchooling;
    
    //가구별 인구 수 통계 #TsHouseHoldPop
    private BigDecimal household1;
    private BigDecimal household2;
    private BigDecimal household3;
    private BigDecimal household4;
    private BigDecimal household5;
    private BigDecimal household6;
    private BigDecimal household7;
    private BigDecimal household8;
    private BigDecimal household9;
    private BigDecimal household10Above;
    private BigDecimal avgHouseholdSize;
    
    //주택 유형별 #TsHousingTypePop
    private BigDecimal oneStory;		//1층주택
    private BigDecimal twoStory;		//2층주택
    private BigDecimal multiStory;		//2층 이상 주택
    private BigDecimal houseAnnex;		//주택/부속 건물
    private BigDecimal flat;			//아파트
    private BigDecimal condominium;		//콘도미니엄
    private BigDecimal twinHouse;		//쌍둥이 주택
    private BigDecimal room;			//방
    private BigDecimal hutShanty;		//오두막/초가집
    
    //가구 거주 주택 단위별 #TsOccHousingUnitPop
	private String gnDivision;
	private String gnNo;
    private BigDecimal householdOwned;
    private BigDecimal rentGovermentOwned;
    private BigDecimal rentIndvslOwned;
    private BigDecimal rentFree;
    private BigDecimal encroached;
    private BigDecimal otherOccupied;
    
    //전국 지역 별 학교 현황 통계 #TsNationalSchoolPop
    private String educationZone;
    private BigDecimal totalShools;
    private BigDecimal nationalSchools;
    private BigDecimal provincialSchools;
    private BigDecimal schoolsByType1AB;
    private BigDecimal schoolsByType1C;
    private BigDecimal schoolsByType2;
    private BigDecimal schoolsByType3;
    private BigDecimal studentPopulation1to50;
    private BigDecimal studentPopulation51to100;
    private BigDecimal studentPopulation101to200;
    private BigDecimal studentPopulation201to500;
    private BigDecimal studentPopulation501to1000;
    private BigDecimal studentPopulationAbove1000;
    
    //지역 별 사립 학교 현황 통계 #TsPrivateSchoolPop
	private String province;
    private BigDecimal schoolsByType1AC;
    private BigDecimal maleStudents;
    private BigDecimal femaleStudents;
    private BigDecimal studentsSinhalaMedium;
    private BigDecimal studentsTamilMedium;
    private BigDecimal studentsEnglishMedium;
    private BigDecimal maleTeachers;
    private BigDecimal femaleTeachers;
    
    //지역별 특수 학교 현황 통계 #TsSpecialSchoolPop
    private BigDecimal schools;
    
    //pirivena 지역 별 학교 현황 통계 #TsPirivenaSchoolPop
    private BigDecimal numberOfPirivenaMulika;
    private BigDecimal numberOfPirivenaMaha;
    private BigDecimal numberOfPirivenaVidyaYathana;
    private BigDecimal numberOfStudentsClergy;
    private BigDecimal numberOfStudentsLaity;
    private BigDecimal approvedTeachersClergy;
    private BigDecimal approvedTeachersLaity;
    private BigDecimal unapprovedTeachersClergy;
    private BigDecimal unapprovedTeachersLaity;
    
    //지역별 교원수 통계 교육 #TsFacultyEducationPop
    private String educationDivision;
    private BigDecimal teachers;
    private BigDecimal studentTeacherRatio;
    
    //지역별 교원수 통계 - 학생 #TsFacultyStudentsPop 위컬럼 동일
    
    //지역별 교원수 통계 - 주기 #TsFacultyCyclePop
    private BigDecimal cycleG1To5;
    private BigDecimal cycleG6To11;
    private BigDecimal cycleG12To13;
    
    //학교 시스템별 교사 학원 수 통계  #TsSchoolSystemPop
    private BigDecimal nationalStudents;
    private BigDecimal provincialStudents;
    private BigDecimal male;
    private BigDecimal female;
    
    //기타 산업 시설 현황 - 비공식 비농업 #TsIdstryFacInformalPop
	private BigDecimal industryNumber;
    private BigDecimal industryPercentage;
    private BigDecimal tradeNumber;
    private BigDecimal tradePercentage;
    private BigDecimal servicesNumber;
    private BigDecimal servicesPercentage;

    //기타 산업시설 현황 - 형식 #TsIdstryFacFormalPop
    private BigDecimal numberOfEstablishments;
    private BigDecimal personsEngaged;
    private BigDecimal employees;
    private BigDecimal salariesAndWages;
    private BigDecimal valueOfOutput;
    private BigDecimal valueOfIntermediateConsumptions;
    private BigDecimal valueAdded;
    private BigDecimal grossAdditionsToFixedAssets;
    
    //부문별 생산성 인구 수 통계 #TsIndstrialOrdSectorsPop
    private BigDecimal intermediateConsumption;
    
    //고용 상태별 인구수 통계 위 컬럼과 동일#TsEmplymntStatusPop
    
    //토지면적별 통계 #TsLandAreaPop
    private int numIdx;
    private String districtId;
    private String provinceId;
    private BigDecimal area;
    
    //지역별 총 생산량 통계 #TsGrossRegnProdPop
    private BigDecimal agriculture;
    private BigDecimal industry;
    private BigDecimal services;
    private BigDecimal gdpAtCurrentMarketPrice;
    private String years;
    
    //차량 등록 별 통계 #TsVehicleRegPop
    private String vhclCateg;
    private BigDecimal jan;
    private BigDecimal feb;
    private BigDecimal mar;
    private BigDecimal apr;
    private BigDecimal may;
    private BigDecimal jun;
    private BigDecimal jul;
    private BigDecimal aug;
    private BigDecimal sep;
    private BigDecimal oct;
    private BigDecimal nov;
    private BigDecimal dec;
}
