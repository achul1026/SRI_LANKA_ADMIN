package com.sl.tdbms.web.admin.common.enums.code;

import com.sl.tdbms.web.admin.common.converter.EnumConverter;
import com.sl.tdbms.web.admin.common.enums.code.common.CommonEnumType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.Getter;

@Getter
public enum PopStatsTypeCd implements CommonEnumType<String> {
	//지역 별 인구 수 통계 
	POPULATION_BY_REGION("PST000","enums.PopStatsTypeCd.POPULATION_BY_REGION",
			new String[] {"district","dsDivision","totalPopltncnt"}),
	//연령 별 인구 수 통계
	POPULATION_BY_AGE("PST001","enums.PopStatsTypeCd.POPULATION_BY_AGE",
			new String[] {"district","dsDivision","ageGroup0To4","ageGroup5To9","ageGroup10To14","ageGroup15To19","ageGroup20To24","ageGroup25To29","ageGroup30To34","ageGroup35To39","ageGroup40To44","ageGroup45To49","ageGroup50To54","ageGroup55To59","ageGroup60To64","ageGroup65To69","ageGroup70To74","ageGroup75To79","ageGroup80To84","ageGroup85To89","ageGroup90To94","ageGroup95AndAbove"}), 
	//경제 활동 인구 통계
	POPULATION_BY_ECONOMIC_ACTIBITY("PST002","enums.PopStatsTypeCd.POPULATION_BY_ECONOMIC_ACTIBITY",
			new String[] {"district", "dsDivision", "totalPopulationAged15AndAbove","employed", "unemployed","economicallyNotActive"}), 
	//학생 수 통계
	THE_NUMBER_OF_STUDENTS("PST003","enums.PopStatsTypeCd.THE_NUMBER_OF_STUDENTS",
			new String[] {"district", "dsDivision", "primarySchoolStudents","secondarySchoolStudents","gceOlStudents","gceAlStudents","degreeAndAboveStudents", "noSchooling"}), 
	//가구별 인구 수 통계
	POPULATION_BY_HOUSEHOLD("PST004","enums.PopStatsTypeCd.POPULATION_BY_HOUSEHOLD",
			new String[] {"district","household1","household2","household3","household4","household5","household6","household7","household8","household9","household10Above","avgHouseholdSize"}),
	//주택 유형 별 가구 인구수 통계
	HOUSEHOLD_POPULATION_BY_HOUSING_TYPE("PST005","enums.PopStatsTypeCd.HOUSEHOLD_POPULATION_BY_HOUSING_TYPE",
			new String[] {"dsDivision","oneStory","twoStory","multiStory","houseAnnex","flat","condominium","twinHouse","room","hutShanty"}), 
	//가구 거주 주택 단위 별 인구 수 통계
	NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT("PST006","enums.PopStatsTypeCd.NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT",
			new String[] {"district","dsDivision","gnDivision","gnNo","householdOwned","rentGovermentOwned","rentIndvslOwned","rentFree","encroached","otherOccupied"}),
	//전국 지역 별 학교 현황 통계
	SCHOOL_STATUS_BY_REGION_NATIONAL("PST007","enums.PopStatsTypeCd.SCHOOL_STATUS_BY_REGION_NATIONAL",
			new String[] {"district","educationZone","totalShools","nationalSchools","provincialSchools","schoolsByType1AB","schoolsByType1C","schoolsByType2","schoolsByType3","studentPopulation1to50","studentPopulation51to100","studentPopulation101to200","studentPopulation201to500","studentPopulation501to1000","studentPopulationAbove1000"}),
	//지역 별 사립 학교 현황 통계
	SCHOOL_STATUS_BY_REGION_PRIVATE("PST008","enums.PopStatsTypeCd.SCHOOL_STATUS_BY_REGION_PRIVATE",
			new String[] {"province","district","schoolsByType1AB","schoolsByType1AC","schoolsByType2","maleStudents","femaleStudents","studentsSinhalaMedium","studentsTamilMedium","studentsEnglishMedium","maleTeachers","femaleTeachers"}),
	//지역 별 특수 학교 현황 통계
	SCHOOL_STATUS_BY_REGION_SPECIAL("PST009","enums.PopStatsTypeCd.SCHOOL_STATUS_BY_REGION_SPECIAL",
			new String[] {"province","district","schools","maleStudents","femaleStudents","maleTeachers","femaleTeachers"}),
	//pirivena 지역 별 학교 현황 통계
	SCHOOL_STATUS_BY_REGION_PIRIVENA("PST010","enums.PopStatsTypeCd.SCHOOL_STATUS_BY_REGION_PIRIVENA",
			new String[] {"district","numberOfPirivenaMulika","numberOfPirivenaMaha","numberOfPirivenaVidyaYathana","numberOfStudentsClergy","numberOfStudentsLaity","approvedTeachersClergy","approvedTeachersLaity","unapprovedTeachersClergy","unapprovedTeachersLaity"}),
	//지역별 교원수 통계 - 교육
	NUMBER_OF_FACULTY_BY_REGION_EDUCATION("PST011","enums.PopStatsTypeCd.NUMBER_OF_FACULTY_BY_REGION_EDUCATION",
			new String[] {"district","educationZone","educationDivision","nationalSchools","provincialSchools","maleStudents","femaleStudents","teachers","studentTeacherRatio"}), 
	//지역별 교원수 통계 - 학생
	NUMBER_OF_FACULTY_BY_REGION_STUDENTS("PST012","enums.PopStatsTypeCd.NUMBER_OF_FACULTY_BY_REGION_STUDENTS",
			new String[] {"district","educationZone","studentPopulation1to50","studentPopulation51to100","studentPopulation101to200","studentPopulation201to500","studentPopulation501to1000","studentPopulationAbove1000"}), 
	//지역별 교원수 통계 - 주기
	NUMBER_OF_FACULTY_BY_REGION_CYCLE("PST013","enums.PopStatsTypeCd.NUMBER_OF_FACULTY_BY_REGION_CYCLE",
			new String[] {"district","educationZone","cycleG1To5","cycleG6To11","cycleG12To13"}), 
	//학교 시스템별 교사 학생 수 통계
	THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM("PST014","enums.PopStatsTypeCd.THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM",
			new String[] {"district","educationZone","nationalStudents","provincialStudents","schoolsByType1AB","schoolsByType1C","schoolsByType2","schoolsByType3","male","female","studentTeacherRatio"}),
	//기타 산업 시설 현황 - 비공식 비농업
	STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL("PST015","enums.PopStatsTypeCd.STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL",
			new String[] {"district","industryNumber","industryPercentage","tradeNumber","tradePercentage","servicesNumber","servicesPercentage"}),
	//기타 산업시설 현황 - 형식
	STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL("PST016","enums.PopStatsTypeCd.STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL",
			new String[] {"district","numberOfEstablishments","personsEngaged","employees","salariesAndWages","valueOfOutput","valueOfIntermediateConsumptions","valueAdded","grossAdditionsToFixedAssets"}),
	//부문별 생산성 인구 수 통계
	INDUSTRIAL_PRODUCTIVITY_BY_SECTORS("PST017" , "enums.PopStatsTypeCd.INDUSTRIAL_PRODUCTIVITY_BY_SECTORS",
			new String[] {"district","valueOfOutput","intermediateConsumption","valueAdded"}),
	//고용 상태별 인구수 통계
	STATUS_OF_EMPLOYMENT("PST018" , "enums.PopStatsTypeCd.STATUS_OF_EMPLOYMENT",
			new String[] {"district","industryNumber","industryPercentage","tradeNumber","tradePercentage","servicesNumber","servicesPercentage"}),
	//토지면적별 통계
	LAND_AREA("PST019","enums.PopStatsTypeCd.LAND_AREA",
			new String[] {"numIdx","dsDivision","dsdId","district","districtId","province","provinceId","area"}),
	//지역별 총 생산량 통계
	GROSS_REGIONAL_PRODUCT("PST020","enums.PopStatsTypeCd.GROSS_REGIONAL_PRODUCT",
			new String[] {"province","agriculture","industry","services","gdpAtCurrentMarketPrice","years"}),
	//차량 등록 별 통계
	VEHICLE_REGISTRATION("PST021","enums.PopStatsTypeCd.VEHICLE_REGISTRATION",
			new String[] {"vhclCateg","jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"}),
	;
	
	String code;
	String name;
	String[] header;
	
	private PopStatsTypeCd(String code,String name, String[] header) {
		this.code = code;
		this.name = name;
		this.header = header;
	}

	@Override
    public String getCode() {
        return code;
    }
	
	@Override
	public String getName() {
		return CommonUtils.getMessage(name);
	}

	
	public static class Converter extends EnumConverter<PopStatsTypeCd, String> {
        public Converter() {
            super(PopStatsTypeCd.class);
        }
    }
	
	public static PopStatsTypeCd getPopStatTypeCdByCode(String code) {
		for(PopStatsTypeCd r : PopStatsTypeCd.values()) {
			if(r.code.equals(code)) {
				return r;
			}
		}
		return null;
	}

	public static String[] getHeaderByCode(String code) {
		for(PopStatsTypeCd r : PopStatsTypeCd.values()) {
			if(r.code.equals(code)) {
				return r.header;
			}
		}
		return null;
	}
	
	public static String getNameByCode(String code) {
		for(PopStatsTypeCd r : PopStatsTypeCd.values()) {
			if(r.code.equals(code)) {
				return r.getName();
			}
		}
		return "";
	}
}
