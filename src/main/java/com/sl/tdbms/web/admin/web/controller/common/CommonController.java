package com.sl.tdbms.web.admin.web.controller.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.entity.TcGnMng;
import com.sl.tdbms.web.admin.common.querydsl.QTcGnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcRoadMngRepository;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonController {

	@Autowired
	QTcGnMngRepository qTcGnMngRepository;
	@Autowired
	TcRoadMngRepository tcRoadMngRepository;

	/**
	  * @Method Name : dsdLocationInfo
	  * @작성일 : 2024. 5. 21.
	  * @작성자 : NK.KIM
	  * @Method 설명 : DSD 위치 정보
	  * @return
	 * @throws JsonProcessingException 
	  */
	@GetMapping("/dstrct/location")
	public @ResponseBody CommonResponse<?> dstrctLocationInfo() throws JsonProcessingException {
		List<TcGnMng> tcDsdMngList = qTcGnMngRepository.getDsdLocationList();
		Map<String, Object> result = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		String tcDsdMngJsonList = objectMapper.writeValueAsString(tcDsdMngList);
		result.put("location", tcDsdMngJsonList);
		return CommonResponse.successToData(result, "");
	}

	/**
	 * @Method Name : roadLocationList
	 * @작성일 : 2024. 5. 21.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 도로 정보
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/road/location")
	public @ResponseBody CommonResponse<?> roadLocationList() throws JsonProcessingException {
		List<Map<String,Object>> tcRoadMngList = tcRoadMngRepository.getRoadList();
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", tcRoadMngList);
	}

	/**
	 * @Method Name : roadLocationInfo
	 * @작성일 : 2024. 5. 21.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 도로 정보
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/road/location/distance")
	public @ResponseBody CommonResponse<?> roadLocationDistanceInfo(@RequestParam(name = "roadCd", required = true) String roadCd,
															@RequestParam(name = "lon", required = true) BigDecimal lon,
															@RequestParam(name = "lat", required = true) BigDecimal lat) throws JsonProcessingException {
		BigDecimal locationDistance = tcRoadMngRepository.getLocationDistance(roadCd,lon,lat);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", locationDistance);
	}
}
 