package com.sl.tdbms.web.admin.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sl.tdbms.web.admin.scheduler.job.InvstJobService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchedulerTask {

	@Autowired
	InvstJobService invstJobService;
	
	/**
	 * @brief : 설문조사 결과 상태 값 스케쥴러
	 * @details : 설문조사 결과 상태 값 스케쥴러
	 * @author : NK.KIM
	 * @date : 2024.02.23
	 */
//	@Scheduled(cron = "0 */1 * * * *") //테스트용 1분
	@Scheduled(cron = "0 1 0 * * *") // 매일 00:01분에 실행
    public void invstSurveySttsJobScheduler() throws InterruptedException {
		try {
			invstJobService.updateIvnstSurveyStts();
		}catch (Exception e) {
			log.debug("Invst Survey scheduler Status Update Failed.");
		}
    }

	/**
	 * @brief : 교통량 조사 결과 상태 값 스케쥴러
	 * @details : 교통량 조사 결과 상태 값 스케쥴러
	 * @author : NK.KIM
	 * @date : 2024.02.23
	 */
//	@Scheduled(cron = "0 */2 * * * *") //테스트용 2분
	@Scheduled(cron = "0 */15 * * * *") // 15분마다 실행
	public void invstTrafficSttsJobScheduler() throws InterruptedException {
		try {
			invstJobService.updateIvnstTrafficStts();
		}catch (Exception e) {
			log.debug("Invst Traffic scheduler Status Update Failed.");
		}
	}
}
