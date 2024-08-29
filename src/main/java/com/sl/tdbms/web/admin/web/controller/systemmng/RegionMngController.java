package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDsdSaveDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionGnSaveDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionListDTO;
import com.sl.tdbms.web.admin.common.entity.TcDsdMng;
import com.sl.tdbms.web.admin.common.entity.TcGnMng;
import com.sl.tdbms.web.admin.common.entity.TcShapeSrlk;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.querydsl.QTcDsdMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcDsdarMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcGnMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcGnarMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcShapeSrlkRepository;
import com.sl.tdbms.web.admin.common.repository.TcDsdarMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcGnarMngRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.systemmng.RegionMngService;


@Controller
@RequestMapping("/systemmng/region")
public class RegionMngController {
	
	@Autowired
    RegionMngService regionMngService;

	@Autowired
    QTcDsdMngRepository qTcDsdMngRepository;
	
	@Autowired
    QTcDsdarMngRepository qTcDsdarMngRepository;
	
	@Autowired
    QTcShapeSrlkRepository qTcShapeSrlkRepository;
	
	@Autowired
	QTcGnMngRepository qTcGnMngRepository;
	
	@Autowired
    QTcGnarMngRepository qTcGnarMngRepository;
	
	@Autowired
    TcDsdarMngRepository tcDsdarMngRepository;
	
	@Autowired
	TcGnarMngRepository tcGnarMngRepository;

	/**
	  * @Method Name : regionDsdList
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 목록 조회 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
    public String regionDsdList(Model model){
		return "views/systemmng/regionList";
    }
	
	/**
	  * @Method Name : regionGnList
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 목록 조회 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{type}/list")
    public String regionGnList(@PathVariable(name = "type") String type, SearchCommonDTO searchDTO, PagingUtils paging , Model model){
		long totalCnt = 0L;
		String viewPage = "";
		List<RegionListDTO> regionList = null;
		switch(type) {
			case "dsd" : 
				regionList = regionMngService.getDsdMngList(searchDTO, paging);
				totalCnt = regionMngService.getDsdTotalCnt(searchDTO);
				viewPage = "views/systemmng/tazDsdList";
				break;
			case "gn":
				regionList = regionMngService.getGnMngList(searchDTO, paging);
				totalCnt = regionMngService.getGnTotalCnt(searchDTO);
				viewPage = "views/systemmng/tazGnList";
				break;
		}
		
		paging.setPageSize(10);
		paging.setTotalCount(totalCnt);
		
//		ObjectMapper mapper = new ObjectMapper();
//		ArrayNode dataList = mapper.valueToTree(
//				regionList.stream()
//				.map(region -> List.of(region.getDistrictNm(), region.getProvinNm(), region.getDsdNm()))
//				.flatMap(List::stream).distinct()
//				.collect(Collectors.toList())
//		);
//		model.addAttribute("dataList", dataList);
		
		model.addAttribute("regionList", regionList);
		model.addAttribute("type", type);
//		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		return viewPage;
    }
	
	/**
	  * @Method Name : regionSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 저장 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String regionSave(Model model){
		return "views/systemmng/regionSave";
	}
	
	/**
	  * @Method Name : regionDsdCodeSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 코드 등록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/save/dsd")
	public String regionDsdCodeSave(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
		paging.setPageSize(5);
		paging.setLimitCount(5);
		List<TcDsdMng> dsdList = qTcDsdMngRepository.getDsdList(searchCommonDTO, paging);

		long totalCnt = qTcDsdMngRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		
		model.addAttribute("dsdList", dsdList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/modal/dsdCodeSave";
	}
	
	/**
	  * @Method Name : regionTazCodeSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 :  지역관리 TAZ 코드 등록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/save/taz")
	public String regionTazCodeSave(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
		paging.setPageSize(5);
		paging.setLimitCount(5);
		List<TcShapeSrlk> tazList = qTcShapeSrlkRepository.getTazList(searchCommonDTO, paging);

		long totalCnt = qTcShapeSrlkRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);

		model.addAttribute("tazList", tazList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/modal/tazCodeSave";
	}
	
	/**
	  * @Method Name : dsdMngSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 등록
	  * @param regionDsdSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/dsd/save")
	@ResponseBody
	public CommonResponse<?> dsdMngSave(@RequestBody RegionDsdSaveDTO regionDsdSaveDTO){
		try {
			regionMngService.dsdMngSave(regionDsdSaveDTO);
			String resMsg = CommonUtils.getMessage("region.dsdMngSave.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : regionDsdDetail
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 상세 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{dsdId}")
	public String regionDsdDetail(@PathVariable("dsdId") String dsdId, Model model){
		model.addAttribute("dsdInfo",qTcDsdarMngRepository.getDsdarInfo(dsdId));
		return "views/systemmng/regionDetail";
	}
	
	/**
	  * @Method Name : regionDsdUpdate
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 수정 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{dsdId}/update")
	public String regionDsdUpdate(@PathVariable("dsdId") String dsdId, Model model){
		model.addAttribute("dsdInfo",qTcDsdarMngRepository.getDsdarInfo(dsdId));
		return "views/systemmng/regionUpdate";
	}
	
	/**
	  * @Method Name : regionDsdUpdate
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 수정
	  * @param dsdId
	  * @param regionDsdSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{dsdId}")
	@ResponseBody
	public CommonResponse<?> regionDsdUpdate(@PathVariable("dsdId") String dsdId, @RequestBody RegionDsdSaveDTO regionDsdSaveDTO){
		try {
			regionMngService.dsdMngUpdate(regionDsdSaveDTO);
			String resMsg = CommonUtils.getMessage("region.regionDsdUpdate.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		}catch(Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : regionDsdDelete
	  * @작성일 : 2024. 7. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 DSD 삭제
	  * @param dsdId
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping("/{dsdId}")
	public @ResponseBody CommonResponse<?> regionDsdDelete(@PathVariable("dsdId") String dsdId){
		String resMsg = CommonUtils.getMessage("region.regionDsdDelete.complete");
		try {
			tcDsdarMngRepository.deleteAllByDsdId(dsdId);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch(Exception e) {
			resMsg = CommonUtils.getMessage("region.regionDsdDelete.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	  * @Method Name : regionGnSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 저장 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/gn/save")
	public String regionGnSave(Model model){
		return "views/systemmng/regionGnSave";
	}
	
	/**
	  * @Method Name : regionGnSave
	  * @작성일 : 2024. 8. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN/TAZ 저장
	  * @param regionGnSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/gn/save")
	@ResponseBody
	public CommonResponse<?> regionGnSave(@RequestBody RegionGnSaveDTO regionGnSaveDTO){
		try {
			regionMngService.gnMngSave(regionGnSaveDTO);
			String resMsg = CommonUtils.getMessage("region.regionGnSave.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : regionGnTazCodeSave
	  * @작성일 : 2024. 7. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : GN 탭 등록 페이지 TAZ 코드 등록 모달
	  * @param model
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/gn/save/taz")
	public String regionGnTazCodeSave(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
		paging.setPageSize(5);
		paging.setLimitCount(5);
		List<TcShapeSrlk> tazList = qTcShapeSrlkRepository.getTazList(searchCommonDTO, paging);

		long totalCnt = qTcShapeSrlkRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);

		model.addAttribute("tazList", tazList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/modal/gnTazCodeSave";

	}
	
	/**
	  * @Method Name : regionGnCodeSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 코드 등록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/gn/save/gn")
	public String regionGnCodeSave(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
		paging.setPageSize(5);
		paging.setLimitCount(5);
		List<TcGnMng> gnList = qTcGnMngRepository.getGnList(searchCommonDTO, paging);

		long totalCnt = qTcGnMngRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);

		model.addAttribute("gnList", gnList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		return "views/systemmng/modal/gnCodeSave";
	}
	
	/**
	  * @Method Name : regionGnDsdDetail
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 상세 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/gn/{gnId}")
	public String regionGnCodeDetail(@PathVariable String gnId, Model model){
		model.addAttribute("gnInfo",qTcGnarMngRepository.getGnarInfo(gnId));
		return "views/systemmng/regionGnDetail";
	}
	
	/**
	  * @Method Name : regionGnDsdUpdate
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 수정 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/gn/{dstrctCd}/update")
	public String regionGnCodeUpdate(@PathVariable String dstrctCd, Model model){
		model.addAttribute("gnInfo",qTcGnarMngRepository.getGnarInfo(dstrctCd));
		return "views/systemmng/regionGnUpdate";
	}
	
	/**
	  * @Method Name : regionGnCodeUpdate
	  * @작성일 : 2024. 8. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 수정
	  * @param dstrctCd
	  * @param regionGnSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/gn/{dstrctCd}/update")
	@ResponseBody
	@Transactional
	public CommonResponse<?> regionGnCodeUpdate(@PathVariable("dstrctCd") String dstrctCd, @RequestBody RegionGnSaveDTO regionGnSaveDTO){
		try {
			regionMngService.gnMngUpdate(regionGnSaveDTO);
			String resMsg = CommonUtils.getMessage("region.regionGnCodeUpdate.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		}catch(Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : regionGnDelete
	  * @작성일 : 2024. 8. 28.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 지역관리 GN 코드 삭제
	  * @param dstrctCd
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping("/gn/{dstrctCd}")
	public @ResponseBody CommonResponse<?> regionGnDelete(@PathVariable("dstrctCd") String dstrctCd){
		String resMsg = CommonUtils.getMessage("region.regionGnDelete.complete");
		try {
			tcGnarMngRepository.deleteAllByDstrctCd(dstrctCd);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch(Exception e) {
			resMsg = CommonUtils.getMessage("region.regionGnDelete.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
}

