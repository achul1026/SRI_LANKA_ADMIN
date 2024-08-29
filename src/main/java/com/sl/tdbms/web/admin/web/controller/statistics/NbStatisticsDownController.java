package com.sl.tdbms.web.admin.web.controller.statistics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.dto.neighbor.StatisticsSummaryDTO;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.util.NbParserUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.web.service.statistics.NbStatisticsDownService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * packageName    : com.sl.tdbms.web.admin.web.controller.statistics
 * fileName       : NbStatisticsDownController.java
 * author         : kjg08
 * date           : 24. 7. 29.
 * description    : MCC, TM, METRO 통계 데이터 다운로드를 위한 컨트롤러 클래스.
 * 통계 데이터 조회 및 엑셀 다운로드 기능을 제공합니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 29.        kjg08           최초 생성
 */
@Controller
@RequestMapping("/statistics/dashboard/download")
@RequiredArgsConstructor
public class NbStatisticsDownController {
    private static final Logger logger = LoggerFactory.getLogger(NbStatisticsDownController.class);

    private final NbStatisticsDownService nbStatisticsDownService;

    /**
     * 다운로드 페이지를 표시하는 메소드.
     *
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 다운로드 페이지 템플릿 이름
     */
    @GetMapping
    @Authority(authType = AuthType.READ)
    public String showDownloadPage(Model model) {
        //logger.info("통계 다운로드 페이지 요청");
        try {
            List<String> years = nbStatisticsDownService.getAvailableYears();
            model.addAttribute("years", years);
            return "tags/statistics/nbDownloadStatistics";
        } catch (Exception e) {
            logger.error("사용 가능한 연도 가져오기 중 오류 발생", e);
            model.addAttribute("error", "사용 가능한 연도를 로드하지 못했습니다");
            return "error";
        }
    }




    /**
     * 통계 데이터를 JSON 형식으로 스트리밍하는 메소드.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @param year 조회 연도 (선택적)
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return JSON 형식의 통계 데이터 스트림
     */
    @GetMapping("/data")
    @ResponseBody
    @Authority(authType = AuthType.READ)
    public ResponseEntity<StreamingResponseBody> getStatisticsData(
            @RequestParam String item,
            @RequestParam(required = false) String year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        //logger.info("통계 데이터 요청 - 항목: {}, 연도: {}, 페이지: {}, 크기: {}", item, year, page, size);

        ObjectMapper objectMapper = new ObjectMapper();
        StreamingResponseBody responseBody = outputStream -> {
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);
            jsonGenerator.setCodec(objectMapper);
            jsonGenerator.writeStartObject();

            try {
                StatisticsSummaryDTO summary = nbStatisticsDownService.getStatisticsSummary(item, year, page, size);
                jsonGenerator.writeNumberField("totalElements", summary.getTotalCount());
                jsonGenerator.writeNumberField("totalPages", summary.getTotalPages());
                jsonGenerator.writeNumberField("currentPage", summary.getCurrentPage());
                jsonGenerator.writeFieldName("content");
                jsonGenerator.writeStartArray();

                for (Object dto : summary.getContent()) {
                    jsonGenerator.writeObject(dto);
                }

                jsonGenerator.writeEndArray();
                jsonGenerator.writeEndObject();
            } catch (Exception e) {
                logger.error("통계 데이터 생성 중 오류 발생", e);
                jsonGenerator.writeStringField("error", "데이터 처리 중 오류가 발생했습니다");
            } finally {
                jsonGenerator.close();
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }


    /**
     * 엑셀 형식으로 통계 데이터를 다운로드하는 메소드.
     *
     * @param item 통계 항목 (METRO 또는 MCC,TM)
     * @param year 조회 연도 (선택적)
     * @return 엑셀 파일 다운로드 응답
     * @throws IOException 파일 생성 중 IO 예외 발생 시
     */
    @GetMapping("/excel")
    @Authority(authType = AuthType.READ)
    public ResponseEntity<Resource> downloadExcel(
            @RequestParam String item,
            @RequestParam(required = false) String year) throws IOException {

        //logger.info("엑셀 다운로드 요청 - 항목: {}, 연도: {}", item, year);

        String fileName = String.format("statistics_data_%s_%s.xlsx", item, year != null ? year : "all_years");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            NbParserUtils.generateExcelReport(baos, item,
                    consumer -> nbStatisticsDownService.streamAllStatisticsData(item, year, consumer));

            //logger.debug("엑셀 리포트 생성 완료. 크기: {} 바이트", baos.size());

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("엑셀 리포트 생성 중 오류 발생", e);
            // 스택 트레이스를 로그에 기록
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error(sw.toString());

            throw new IOException("엑셀 리포트 생성에 실패했습니다", e);
        } finally {
            baos.close();
        }
    }
}
