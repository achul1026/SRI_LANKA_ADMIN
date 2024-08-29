package com.sl.tdbms.web.admin.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CommonUtils {

	private CommonUtils() {};

	private static MessageSource messageSource;

    @Autowired
    private MessageSource messageSourceAutowired;

    @PostConstruct
    public void init() {
        messageSource = messageSourceAutowired;
    }
	
	/**
	  * @Method Name : getUuid
	  * @작성일 : 2023. 9. 11.
	  * @작성자 : KY.LEE
	  * @Method 설명 : UUID 생성
	  * @return String
	  */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	
	/**
	 * @Method Name : getUuid
	 * @작성일 : 2023. 11. 02.
	 * @작성자 : KY.LEE
	 * @Method 설명 : UUID 생성 글자수 제한
	 * @param int maxLength
	 * @return String
	 */
	public static String getUuid(int maxLength) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		uuid = uuid.substring(0, maxLength);
		return uuid;
	}
	
	/**
	  * @Method Name : isNull
	  * @작성일 : 2023. 8. 22.
	  * @작성자 : 82103
	  * @Method 설명 : Null Check
	  * @param paramVal
	  * @return boolean
	  */
	public static boolean isNull(Object paramVal) {
		boolean result = false;
		if("".equals(paramVal) || paramVal == null || "null".equals(paramVal)) {
			result = true;
		}
		return result;
	}
	
	/**
	 * @Method Name : isListNull
	 * @작성일 : 2023. 8. 22.
	 * @작성자 : 82103
	 * @Method 설명 : Null Check
	 * @param paramVal
	 * @return boolean
	 */
	public static boolean isListNull(List<?> paramVal) {
		boolean result = false;
		if(paramVal == null || paramVal.isEmpty() || paramVal.size() == 0) {
			result = true;
		}
		return result;
	}
	
	/**
	  * @Method Name : phoneNumAddHyphen
	  * @작성일 : 2023. 8. 22.
	  * @작성자 : KC.KIM
	  * @Method 설명 : 휴대전화번호 하이픈 추가
	  * @param 
	  * @return number
	  */
	public static String phoneNumAddHyphen(String number) {
		  if(CommonUtils.isNull(number)) {
			  return "";
		  }
	      String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
	      return number.replaceAll(regEx, "$1-$2-$3");
	}
	
	/**
	  * @Method Name : removeHyphen
	  * @작성일 : 2023. 8. 22.
	  * @작성자 : KC.KIM
	  * @Method 설명 : removeHyphen
	  * @param 
	  * @return number
	  */
	public static String removeHyphen(String str) {
		  if(CommonUtils.isNull(str)) {
			  return "";
		  }
	      return str.replace("-", "");
	}

	/**
	  * @Method Name : removeHyphen
	  * @작성일 : 2023. 8. 22.
	  * @작성자 : KC.KIM
	  * @Method 설명 : removeHyphen
	  * @param 
	  * @return number
	  */
	public static String dateToDatetimeStr(String date, String type) {
		if(!CommonUtils.isNull(date)) {
			String result = "";
			switch (type) {
			case "startDate":
				result = date + " 00:00:00";
				break;
			case "endDate":
				result = date + " 23:59:59";
				break;
			case "startDateHour":
				result = date + " 00";
				break;
			case "endDateHour":
				result = date + " 23";
				break;
			}
			
			return result;
		  }
		return null;
	}
	
	/**
	  * @Method Name : setDateTimeToDateString
	  * @작성일 : 2023. 9. 25.
	  * @작성자 : KY.LEE
	  * @Method 설명 : DateString 값 + n값 Set하기
	  * @param String Date(yyyy-MM-dd) , Integer Time 00-23,String pattern(Date pattern ex)yyyy-MM-dd HH:mm:ss) , int calenderType Calendar.HOUR 
	  * @return String
	  * @throws ParseException 
	  */
	public static String setDateTimeToDateString(String strDate,Integer time, String pattern,int calendarType) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = format.parse(strDate + " " + time);

		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.add(calendarType, time);
		
		return format.format(cal.getTime());
	}
	
	
	/**
	  * @Method Name : pgArrayToJavaArray
	  * @작성일 : 2023. 9. 12.
	  * @작성자 : NK.KIM
	  * @Method 설명 : PostgresSql Array - > Java Array
	  * @param pgArray
	  * @return
	  */
	/*public static List<Object> pgArrayToJavaArray(Object pgArray){
		PgArray arrayData = (PgArray) pgArray;
		Object deserializedArray = null;
		try {
			deserializedArray = arrayData.getArray();
		} catch (SQLException e) {
		}
		
		return Arrays.asList((Object[]) deserializedArray);
	}*/
	
	/**
	  * @Method Name : getCalculationDateToString
	  * @작성일 : 2023. 9. 12.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 날짜 연산
	  * @param Integer  , String , int ex)Calendar.HOUR
	  * @return
	  */
	public static String getCalculationDateToString(Integer number,String pattern,int calendarType) {
		
		String returnTime = null;
		Date today = new Date();
		
		SimpleDateFormat sdformat = new SimpleDateFormat(pattern);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(calendarType, number);
		
		returnTime = sdformat.format(cal.getTime());
		
		return returnTime;
	}
	
	/**
	  * @Method Name : getTimeForStringDate
	  * @작성일 : 2023. 10. 25.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 날짜 String값을 시간값으로 return HH:mm
	  * @param String 데이트 형식
	  * @return
	 * @throws ParseException 
	  */
	public static String getTimeForStringDate(String date,String pattern) throws ParseException {
		String result = "";
		
		SimpleDateFormat sdformat = new SimpleDateFormat(pattern);
		Date paramDate = sdformat.parse(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(paramDate);
		
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int times = cal.get(Calendar.MINUTE);
		
		String strHours = "";
		String strTimes = "";
		
		if(hours < 10) {
			strHours = "0"+String.valueOf(hours);
		} else {
			strHours = String.valueOf(hours);
		}
		
		if(times < 10) {
			strTimes = "0"+String.valueOf(times);
		} else {
			strTimes = String.valueOf(times);
		}
		
		result = strHours+":"+strTimes;
		
		return result;
	}
	
	/**
	  * @Method Name : getArrIdx
	  * @작성일 : 2023.10. 16.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 배열 인덱스 찾기
	  * @param String[], String 
	  * @return
	  */
	public static int getArrIdx(String[] strArr, String target) {
		int idx = Arrays.binarySearch(strArr, target);
		return (idx < 0)? -1 : idx;
	}
	
	/**
	  * @Method Name : isDouble
	  * @작성일 : 2023.11. 16.
	  * @작성자 : KC.KIM
	  * @Method 설명 : 문자열이 double인지 체크
	  * @param String 
	  * @return
	  */
	public static boolean isDouble(String strValue) {
	    try {
	      Double.parseDouble(strValue);
	      return true;
	    } catch (NumberFormatException ex) {
	      return false;
	    }
	  }
	
	/**
	  * @Method Name : isLong
	  * @작성일 : 2023.11. 17.
	  * @작성자 : KC.KIM
	  * @Method 설명 : 문자열이 long인지 체크
	  * @param String 
	  * @return
	  */
	public static boolean isLong(String strValue) {
	    try {
	      Long.parseLong(strValue);
	      return true;
	    } catch (NumberFormatException ex) {
	      return false;
	    }
	}
	
	/**
	  * @Method Name : extractDate
	  * @작성일 : 2023.11. 17.
	  * @작성자 : KC.KIM
	  * @Method 설명 : 문자열에서 날짜 정보만 추출(2023년 11월 17일 > 20231117) / 날짜가 2개 이상일 경우 콤마(,) 포함
	  * @param dateStr
	  * @return
	  */
	public static String extractDateWithComma(String dateStr) {
		//dateStr = "0000년 00월 00일 (0요일) ";
		String result = "";
		// 2021년 04월 09일(금), 2021년 08월 13일 (금요일) 
		if(!CommonUtils.isNull(dateStr)) {
			result = dateStr.replaceAll("년", "").replaceAll("월", "").replaceAll("요일", "")
					.replaceAll("\\(", "").replaceAll("일", "").replaceAll("\\)", "").replaceAll(" ", "");					
		}
		return result;
	}
	
	public static boolean chkContainString(String value) {
		if(value.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	  * @Method Name : isDateChk
	  * @작성일 : 2023.12. 08.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 유효 날짜 체크
	  * @param 
	  * @return
	  */
	public static boolean isDateChk(String date, String pattern) {
		boolean isDateChk = true;
		if(date != null && !"".equals(date)) {
			try {
				SimpleDateFormat  dateFormat = new  SimpleDateFormat(pattern);
				dateFormat.setLenient(false);
				dateFormat.parse(date);
			} catch(ParseException e) {
				isDateChk = false;
			}
		} 
		return isDateChk;
	}
	
	/**
	  * @Method Name : isTimeChk
	  * @작성일 : 2023.12. 08.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 유효 시간 체크
	  * @param 
	  * @return
	  */
	public static boolean isTimeChk(String strTime) {
		boolean isDateChk = true;
		
		int time = Integer.parseInt(strTime);
		if(0 > time) {
			isDateChk = false;
		} else if(time > 24) {
			isDateChk = false;
		}
		
		return isDateChk;
	}
	
	/**
	  * @Method Name : getRownum
	  * @작성일 : 2024. 1. 10.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 페이징 GET ROWNUM 
	  * @param paging
	  * @param rowIdx
	  * @return
	  */
	public int getRownum(PagingUtils paging, Long rowIdx) {
		if(CommonUtils.isNull(paging) || CommonUtils.isNull(rowIdx)) {
			//exception
		}
		Long rownum = paging.getTotalCount() - (paging.getPageSize() * (paging.getPageNo()-1)) - rowIdx;
		return rownum.intValue();
	}
	
	/**
	  * @Method Name : formatLocalDateTime
	  * @작성일 : 2024. 1. 11.
	  * @작성자 : NK.KIM
	  * @Method 설명 : localdatetime format
	  * @param localDateTime
	  * @param pattern
	  * @return
	  */
	public static String formatLocalDateTime(LocalDateTime localDateTime, String... pattern) {
		
		String formatPattern = "yyyy-MM-dd HH:mm:ss";
		
		if(!CommonUtils.isNull(pattern) && pattern.length != 0) {
			formatPattern = pattern[0];
		}
		return localDateTime.format(DateTimeFormatter.ofPattern(formatPattern));
	}
	
	/**
	  * @Method Name : getAuthMenuAllChecked
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 상세 > 메뉴접근 > 전체 체크박스 
	  * @param tcMenuAuthInfo
	  * @return
	  */
	public String getAuthMenuAllChecked(TcAuthGrpDetailDTO.TcMenuAuthInfo tcMenuAuthInfo) {
		String result = "N";
		if(!CommonUtils.isNull(tcMenuAuthInfo)) {
			result = tcMenuAuthInfo.getMenuAuthCheck();
		}
		return result;
	}
	
	/**
	 * @Method Name : getAuthMenuAllChecked
	 * @작성일 : 2024. 1. 25.
	 * @작성자 : NK.KIM
	 * @Method 설명 :  서브 메뉴 전체 체크박스 
	 * @param authMenuList
	 * @return
	 */
	public boolean getAuthSubMenuAllChecked(TcAuthGrpDetailDTO.TcMenuAuthInfo.SubAuthMenuInfo subAuthMenuInfo) {
		boolean result = false;
		if(!CommonUtils.isNull(subAuthMenuInfo)) {
			if("Y".equals(subAuthMenuInfo.getSrchYn()) && "Y".equals(subAuthMenuInfo.getInputYn()) &&
				"Y".equals(subAuthMenuInfo.getUpdtYn()) && "Y".equals(subAuthMenuInfo.getDelYn())) {
				result = true;
			}
		}
		return result;
	}
	
	public static LocalDateTime getStringToLocalDateTimeFormat(String stringDate, String formatPattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
		return LocalDateTime.parse(stringDate+"T00:00:00",formatter);
	}
	
	/**
	  * @Method Name : getUrlPattrn
	  * @작성일 : 2024. 2. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : Url SubString
	  * @param urlPath
	  * @param replaceStr
	  * @param endPath
	  * @return
	  */
	public static String getUrlPattrn(String urlPath, char replaceStr, Boolean notAddedStr) {
		
		String resultPath = urlPath;
		try {
			long urlPathChk = urlPath.chars().filter(c -> c == replaceStr).count();
			if (urlPathChk > 1) {
				int secondIdx = urlPath.indexOf(replaceStr, 1);
				resultPath = urlPath.substring(0, secondIdx);
				if (!notAddedStr) {
					resultPath += "/**";
				}
			} else {
				resultPath = urlPath + "/**";
			}
		} catch (Exception e) {
			resultPath = urlPath;
		}
		
		return resultPath;
	}
	/**
	  * @Method Name : findThirdIndex
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : N번째 인덱스 찾기
	  * @param currentUrl
	  * @param replaceStr
	  * @return
	  */
	public static int findNthIndex(String targetStr, char replaceStr, int findIdx) {
	    int index = -1;
	    for (int i = 0; i < findIdx; i++) {
	        if (index != -1) {
	            index = targetStr.indexOf(replaceStr, index + 1);
	        } else {
	            index = targetStr.indexOf(replaceStr);
	        }
	        if (index == -1) break;
	    }
	    return index;
	}
	
	/**
	  * @Method Name : getRandomKey
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 코드 렌덤 생성
	  * @param key_len
	  * @return
	  */
	public static String getRandomKey(int key_len) {
		Random rnd = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 1; i <= key_len; i++) {
			if (rnd.nextBoolean())
				buf.append((char) (rnd.nextInt(26) + 65)); // 0~25(26개) + 65
			else
				buf.append(rnd.nextInt(10));
		}
		return buf.toString();
	}
	
	/**
	  * @Method Name : getlistToJsonString
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : list to json string 파싱
	  * @param paramList
	  * @return
	  */
	public static String getListToJsonString(List<?> paramList) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
		    json = objectMapper.writeValueAsString(paramList);
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		}
		return json;
	}
	
	/**
	  * @Method Name : getLocalDateTimeStartOfDay
	  * @작성일 : 2024. 4. 3.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 현재 달의 첫 날 정보
	  * @return
	  */
	public static LocalDateTime getMonthStartOfDay(LocalDate currentDate) {
        
        // 현재 달의 첫 번째 날을 LocalDateTime으로 변환하여 시간을 00:00:00으로 설정합니다.
        LocalDateTime startOfMonth = LocalDateTime.of(currentDate.withDayOfMonth(1), LocalTime.MIN);
        
        return startOfMonth;
	}
	
	/**
	 * @Method Name : getLocalDateTimeStartOfDay
	 * @작성일 : 2024. 4. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 현재 달의 마지막 날 정보
	 * @return
	 */
	public static LocalDateTime getMonthEndOfDay(LocalDate currentDate) {
		// 현재 달의 마지막 날을 LocalDateTime으로 변환하여 시간을 23:59:59로 설정합니다.
		YearMonth currentYearMonth = YearMonth.from(currentDate);
		LocalDateTime lastDayOfMonth = LocalDateTime.of(currentYearMonth.atEndOfMonth(), LocalTime.MAX).withNano(0);
		
		return lastDayOfMonth;
	}
	
	/**
	 * @Method Name : getStartOfDay
	 * @작성일 : 2024. 4. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 오늘 시작 시간(00:00:00) 
	 * @return LocalDateTime
	 */
	public static LocalDateTime getStartOfDay(LocalDate currentDate) {
		
		LocalDateTime startOfToday = LocalDateTime.of(currentDate, LocalTime.MIN);
		
		return startOfToday;
	}
	
	/**
	 * @Method Name : getEndOfDay
	 * @작성일 : 2024. 4. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 날짜의 마지막 시간(23:59:59)
	 * @return
	 */
	public static LocalDateTime getEndOfDay(LocalDate currentDate) {
		LocalDateTime endOfToday = LocalDateTime.of(currentDate, LocalTime.MAX).withNano(0);
		return endOfToday;
	}
	
	/**
	  * @Method Name : getStringDateFormatByDateAndPattrn
	  * @작성일 : 2024. 5. 22.
	  * @작성자 : NK.KIM
	  * @Method 설명 : Date Type Format -> String Date
	  * @param date
	  * @param pattrn
	  * @return
	  */
	public static String getStringDateFormatByDateAndPattrn(Date date, String pattrn) {
		if(CommonUtils.isNull(pattrn)) {
			pattrn = "yyyy-MM-dd HH:mm:ss";
		}
		DateFormat dateFormat = new SimpleDateFormat(pattrn);
        String resultDate = dateFormat.format(date);
		return resultDate;
	}
	
	/**
	  * @Method Name : maskString
	  * @작성일 : 2024. 5. 22.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 마스킹처리
	  * @param str
	  * @param start
	  * @return
	  */
	public static String getMaskString(String str, int start) {
        if (str == null || str.length() < start) {
            return str;
        }

        StringBuilder masked = new StringBuilder(str.substring(0, start));
        for (int i = start; i < str.length(); i++) {
            masked.append('*');
        }

        return masked.toString();
    }
	
    /**
      * @Method Name : getTempPassword
      * @작성일 : 2024. 5. 23.
      * @작성자 : SM.KIM
      * @Method 설명 : 임시 비밀번호 생성
      * @param length
      * @return
      */
    public static String getTempPassword(int length) {
    	char[] numberCharacters = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };
//    	char[] uppercaseCharacters = new char[] {
//                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
//                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
//        };
    	char[] lowercaseCharacters = new char[] {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };
    	char[] specialSymbolCharacters = new char[] {
                '@', '!', '#', '$', '%', '^', '&', '*'
        };
    	
    	char[] rndAllCharacters = new char[numberCharacters.length + lowercaseCharacters.length + specialSymbolCharacters.length];
        System.arraycopy(numberCharacters, 0, rndAllCharacters, 0, numberCharacters.length);
        System.arraycopy(lowercaseCharacters, 0, rndAllCharacters, numberCharacters.length, lowercaseCharacters.length);
        System.arraycopy(specialSymbolCharacters, 0, rndAllCharacters, numberCharacters.length + lowercaseCharacters.length, specialSymbolCharacters.length);
    	
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        
        List<Character> passwordCharacters = new ArrayList<>();

        passwordCharacters.add(numberCharacters[random.nextInt(numberCharacters.length)]);
//        passwordCharacters.add(uppercaseCharacters[random.nextInt(uppercaseCharacters.length)]);
        passwordCharacters.add(lowercaseCharacters[random.nextInt(lowercaseCharacters.length)]);
        passwordCharacters.add(specialSymbolCharacters[random.nextInt(specialSymbolCharacters.length)]);

        int rndAllCharactersLength = rndAllCharacters.length;
        for (int i = 0; i < length-3; i++) {
            passwordCharacters.add(rndAllCharacters[random.nextInt(rndAllCharactersLength)]);
        }
        
        Collections.shuffle(passwordCharacters);

        for (Character character : passwordCharacters) {
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }
    
	/**
	 * @Method Name : getCookiesValue
	 * @작성일 : 2024. 6. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 쿠키 값 가져오기
	 * @param request
	 * @return String
	 */

	public static String getLangValue(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		String langValue = "eng";
		if(!CommonUtils.isNull(cookies)){
			for(Cookie c : cookies) {
				if("lang".equals(c.getName())){
					langValue = c.getValue();
				}
			}
		}
		return langValue;
	}
	
	/**
	  * @Method Name : getMessage
	  * @작성일 : 2024. 6. 10.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 다국어 메시지 가져오기
	  * @param messageKey
	  * @param lang
	  * @return
	  */
	public static String getMessage(String messageKey, Object... args) {
		String lang = LocaleContextHolder.getLocale().toString();
		return messageSource.getMessage(messageKey, args, new Locale(lang));
//		if (args != null && args.length == 0) {
//			return messageSource.getMessage(messageKey + "_WITH_ARGS", args, new Locale(lang));
//		} else {
//			return messageSource.getMessage(messageKey, null, new Locale(lang));
//		}
	}
	
	/**
	  * @Method Name : getMenuFirstPath
	  * @작성일 : 2024. 6. 11.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 첫번째 path 가져오기
	  * @param param
	  * @return
	  */
	public static String getMenuFirstPath(String param) {
		String result = "";
		if(!CommonUtils.isNull(param)) {
			result = param.split("/")[1];
		}
		return result;
	}

	/**
	 * @Method Name : numberFormatComma
	 * @작성일 : 2024. 6. 11.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 천단위 콤마
	 * @param param
	 * @return
	 */
	public static String getNumberFormatComma(String param){
		String result = "0";
		if(!CommonUtils.isNull(param)) {
			result = NumberFormat.getInstance().format(param);
		}
		return result;
	}

	/**
	 * @Method Name : normalizeString
	 * @작성일 : 2024. 6. 19.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 엑셀 추출데이터 영어값만 비교
	 * @param str
	 * @return
	 */
    public static String normalizeString(String str) {
        if (str == null) return "";
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("[^A-Za-z]", "").toUpperCase();
    }
    
	/**
	  * @Method Name : getLanguageCode
	  * @작성일 : 2024. 6. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 언어 코드 변환
	  * @return
	  */
	public static String getLanguageCode() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        switch (lang) {
            case "eng": 
                return "en";
            case "sin": 
                return "si";
            case "kor": 
                return "ko";
            default:
                return "en";
        }
    }

	public static boolean compareSurveyEndDtAndToday(LocalDateTime endDt){
		boolean result = false;
		if(LocalDateTime.now().isBefore(endDt)){
			result = true;
		}
		return result;
	}

	public static String onlyNumbers(String numberString) {
		if (numberString == null || numberString.isEmpty()) {
			return "";
		}
		return numberString.replaceAll("[^0-9]", "");
	}

	/**
	 * methodName : makeTimeArr
	 * author : Peo.Lee
	 * date : 2024-08-27
	 * description : 스트링 숫자 배열 생성
	 * @param maxNumber
	 * @return String[]
	 */
	public static String[] makeTimeArr(int maxNumber){
		String[] times = new String[maxNumber];
		for (int i = 0; i < times.length; i++){
			if(i < 10) {
				times[i] = "0"+i;
			} else {
				times[i] = String.format("%02d", i);
			}
		}
		return times;
	}
}

