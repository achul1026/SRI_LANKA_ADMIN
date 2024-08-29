package com.sl.tdbms.web.admin.web.controller.common;

import com.sl.tdbms.web.admin.common.dto.gis.*;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.querydsl.QTmInstllcRoadRepository;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import com.sl.tdbms.web.admin.web.service.gis.GISService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The type Gis controller.
 *
 * @author : Charles Kim
 * @fileName :  GISController
 * @since : 2024-07-08
 */
@RestController
@RequestMapping("/gis")
@RequiredArgsConstructor
public class GISController {

    final private QTmInstllcRoadRepository qTmInstllcRoadRepository;

    final private GISService gISService;

    /**
     * methodName : getStatisticsVDS
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > VDS 목록
     *
     * @param statisticsGISSearchDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/vds")
    public @ResponseBody CommonResponse<?> getStatisticsVDS(StatisticsGISSearchDTO statisticsGISSearchDTO,
                                                            @RequestParam(value = "searchType", required = true) String searchType) {
        //resultType = trfvlm or avgspped
        List<StatisticsGISVDSDTO> gisDataList = gisDataList = gISService.getStatisticsVDSList(statisticsGISSearchDTO,searchType);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",gisDataList);
    }

    /**
     * methodName : getStatisticsVDSDetail
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > VDS 상세 정보
     *
     * @param instllcId
     * @param statisticsGISSearchDetailDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/vds/{resultType}/{instllcId}")
    public @ResponseBody CommonResponse<?> getStatisticsVDSDetail(@PathVariable("instllcId") String instllcId,
                                                                  @PathVariable("resultType") String resultType,
                                                                  StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO,
                                                                  @RequestParam(value = "searchType", required = true) String searchType) {
        //resultType = trfvlm or avgspeed
        StatisticsGISDetailDTO statisticsGISDetailDTO = null;
        //교통량
        if("trfvlm".equals(resultType)){
            statisticsGISDetailDTO =  gISService.getStatisticsVDSTrfvlmDetail(statisticsGISSearchDetailDTO,searchType);
        }else{
        // 평균속도
            statisticsGISDetailDTO =  gISService.getStatisticsVDSAvgSpeedDetail(statisticsGISSearchDetailDTO,searchType);
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",statisticsGISDetailDTO);
    }

    /**
     * methodName : getStatisticsFixedMetroCount
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > 고정형 메트로카운트 목록
     *
     * @param statisticsGISSearchDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/fixed/metrocount")
    public @ResponseBody CommonResponse<?> getStatisticsFixedMetroCount(StatisticsGISSearchDTO statisticsGISSearchDTO,
                                                            @RequestParam(value = "searchType", required = true) String searchType) {
        List<StatisticsGISFixedMetroCountDTO> gisDataList =  gISService.getStatisticsFixedMetroCountList(statisticsGISSearchDTO,searchType);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",gisDataList);
    }

    /**
     * methodName : getStatisticsFixedMetroCountDetail
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > 고정형 메트로카운트 상세 정보
     *
     * @param instllcId
     * @param statisticsGISSearchMetroCountDetailDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/fixed/metrocount/{resultType}/{instllcId}")
    public @ResponseBody CommonResponse<?> getStatisticsFixedMetroCountDetail(@PathVariable("instllcId") String instllcId,
                                                                              @PathVariable("resultType") String resultType,
                                                                              StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,
                                                                              @RequestParam(value = "searchType", required = true) String searchType) {
        //resultType = trfvlm or avgspeed
        StatisticsGISDetailDTO statisticsGISDetailDTO = null;
        //교통량
        if("trfvlm".equals(resultType)){
            statisticsGISDetailDTO =  gISService.getStatisticsFixedMetroCountTrfvlmDetail(statisticsGISSearchMetroCountDetailDTO,searchType);
        }else{
            // 평균속도
            statisticsGISDetailDTO =  gISService.getStatisticsFixedMetroCountAvgSpeedDetail(statisticsGISSearchMetroCountDetailDTO,searchType);
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",statisticsGISDetailDTO);
    }

    /**
     * methodName : getStatisticsMoveMetroCount
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > 이동형 메트로카운트 목록
     *
     * @param statisticsGISSearchDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/move/metrocount")
    public @ResponseBody CommonResponse<?> getStatisticsMoveMetroCount(StatisticsGISSearchDTO statisticsGISSearchDTO,
                                                            @RequestParam(value = "searchType", required = true) String searchType) {
        List<StatisticsGISMoveMetroCountDTO> gisDataList =  gISService.getStatisticsMoveMetroCountList(statisticsGISSearchDTO,searchType);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",gisDataList);
    }


    /**
     * methodName : getStatisticsMoveMetroCountDetail
     * author : Nk.KIM
     * date : 2024-08-08
     * description : GIS 시각화 통계/분석 > 이동형 메트로카운트 상세 정보
     *
     * @param instllcId
     * @param statisticsGISSearchMetroCountDetailDTO
     * @param searchType
     * @return common response
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/move/metrocount/{resultType}/{instllcId}")
    public @ResponseBody CommonResponse<?> getStatisticsMoveMetroCountDetail(@PathVariable("instllcId") String instllcId,
                                                                             @PathVariable("resultType") String resultType,
                                                                             StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,
                                                                             @RequestParam(value = "searchType", required = true) String searchType) {

        //resultType = trfvlm or avgspeed
        StatisticsGISDetailDTO statisticsGISDetailDTO = null;
        //교통량,
        if("trfvlm".equals(resultType)){
            statisticsGISDetailDTO =  gISService.getStatisticsMoveMetroCountTrfvlmDetail(statisticsGISSearchMetroCountDetailDTO,searchType);
        }else{
            // 평균속도
            statisticsGISDetailDTO =  gISService.getStatisticsMoveMetroCountAvgSpeedDetail(statisticsGISSearchMetroCountDetailDTO,searchType);
        }
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",statisticsGISDetailDTO);
    }


    /**
     * methodName : downloadTransCad
     * author : Nk.KIM
     * date : 2024-08-13
     * description : TransCAD 다운로드
     *
     * @param downloadType
     * @param searchYear
     * @param response
     * @return void
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/transcad/{downloadType}")
    public void downloadTransCad(@PathVariable("downloadType") String downloadType,
                                 @RequestParam("searchYear") String searchYear,
                                 HttpServletResponse response) {

        try {
            gISService.excelDownLoad(response,downloadType,searchYear);
        } catch (CommonException e) {
            throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    /**
     * methodName : getStatisticsMoveMetroCount
     * author : Nk.KIM
     * date : 2024-08-21
     * description : 트랜스케드 다운로드 장비별 년도
     *
     * @param searchType
     * @return CommonResponse<?>
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/transcad/search/years")
    public @ResponseBody CommonResponse<?> getTranscadSearchYears(@RequestParam(value = "searchType", required = false) String searchType) {
        List<String> years =  gISService.getSearchYearsForTranscad(searchType);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",years);
    }
}
