package com.sl.tdbms.web.admin.web.controller.datamng;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.datamng.NbMetroCountMngService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sl.tdbms.web.admin.web.controller.datamng
 * fileName       : NbMetroCountMngController.java
 * author         : kjg08
 * date           : 24. 7. 22.
 * description    : 메트로 카운트 파일 업로드를 관리하는 컨트롤러 클래스.
 *                  파일 업로드 페이지 렌더링 및 파일 업로드 처리 기능을 제공합니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 7. 22.        kjg08           최초 생성
 */
@Controller
@RequestMapping("/datamng/metroupload")
public class NbMetroCountMngController {

    private static final Logger logger = LoggerFactory.getLogger(NbMetroCountMngController.class);

    @Autowired
    private NbMetroCountMngService nbMetroCountMngService;

    /**
     * 파일 업로드 페이지를 렌더링하는 메소드.
     *
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 업로드 페이지 템플릿 이름
     */
    @Authority(authType = AuthType.READ)
    @GetMapping
    public String index(Model model) {
        //logger.info("파일 업로드 페이지 요청");
        return "views/datamng/nbMetroCountUpload";
    }

    /**
     * 파일 업로드를 처리하는 메소드.
     *
     * @param files         업로드된 파일 배열
     * @param equipmentType 장비 유형 ("fixed" 또는 "mobile")
     * @return 업로드 처리 결과를 포함한 공통 응답 객체
     */
    @PostMapping("/upload")
    public @ResponseBody CommonResponse<?> uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam("equipmentType") String equipmentType) {
        //logger.info("파일 업로드 요청 - 파일 수: {}, 장비 유형: {}", files.length, equipmentType);

        try {
            for (MultipartFile file : files) {
                try {
                    nbMetroCountMngService.processFile(file, equipmentType);
                } catch (Exception e) {
                    logger.error("파일 처리 중 오류 발생: " + file.getOriginalFilename(), e);
                    // 개별 파일 처리 실패를 기록하고 계속 진행
                }
            }

            Map<String, Object> finalResult = nbMetroCountMngService.getFinalResult();
            String resMsg = buildResponseMessage(finalResult);

            //logger.info("파일 업로드 완료 - 결과: {}", resMsg);
            return CommonResponse.successToData(finalResult, resMsg);
        } catch (Exception e) {
            logger.error("파일 업로드 중 예기치 못한 오류 발생", e);
            return CommonResponse.ResponseCodeAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 예기치 못한 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 업로드 처리 결과 메시지를 생성하는 메소드.
     *
     * @param result 업로드 처리 결과 맵
     * @return 응답 메시지 문자열
     */
    private String buildResponseMessage(Map<String, Object> result) {
        StringBuilder msg = new StringBuilder();
        List<String> successfulFiles = (List<String>) result.get("successfulFiles");
        List<String> failedFiles = (List<String>) result.get("failedFiles");
        Map<String, List<String>> fileErrors = (Map<String, List<String>>) result.get("fileErrors");
        int totalInserted = (int) result.get("totalInserted");

        if (!successfulFiles.isEmpty()) {
            msg.append("성공적으로 처리된 파일: ").append(String.join(", ", successfulFiles)).append(". ");
        }
        if (!fileErrors.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : fileErrors.entrySet()) {
                String fileName = entry.getKey();
                List<String> errors = entry.getValue();
                msg.append(fileName).append(": ");
                msg.append(String.join(", ", errors)).append("; ");
            }
            msg.setLength(msg.length() - 2); // 마지막 "; " 제거
            msg.append(". ");
        }
        if (!failedFiles.isEmpty()) {
            msg.append("처리 실패한 파일: ").append(String.join(", ", failedFiles)).append(". ");
        }
        msg.append("총 삽입된 레코드 수: ").append(totalInserted);

        return msg.toString();
    }
}