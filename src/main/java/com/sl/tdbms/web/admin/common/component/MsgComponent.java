package com.sl.tdbms.web.admin.common.component;

import java.math.BigDecimal;
import java.util.Map;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.entity.MsgQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sl.tdbms.web.admin.common.enums.MsgQueueStatus;
import com.sl.tdbms.web.admin.common.enums.MsgTemplateType;
import com.sl.tdbms.web.admin.common.enums.MsgType;
import com.sl.tdbms.web.admin.common.repository.MsgQueueRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

@Component
public class MsgComponent {
	
	@Value("${srilanka.main.url}")
	public String MAIN_URL;
    
    @Value("${srilanka.mail.sender}")
    public String MAIL_SENDER;
    
    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Autowired
    MsgQueueRepository msgQueueRepository;
    
    
	/**
	  * @Method Name : saveMsgQueue
	  * @작성일 : 2024. 5. 22.
	  * @작성자 : NK.KIM
	  * @Method 설명 : message queue 저장
	  * @param msgQueue
	  * @param msgTemplateType
	  * @param htmlValues
	  */
    @Transactional
	public void saveMsgQueue(MsgQueue msgQueue, MsgTemplateType msgTemplateType, Map<String,Object> htmlValues) {
    	
		Context context = new Context();
		htmlValues.put("url", MAIN_URL);
		htmlValues.put("lang", CommonUtils.getLanguageCode());
		if(!CommonUtils.isNull(htmlValues)) {
			htmlValues.forEach((key, value)->{
	            context.setVariable(key, value);
	        });
		}
        String htmlBody = templateEngine.process(msgTemplateType.getPath(), context);
        
        msgQueue.setMsgType(MsgType.EMAIL);
        msgQueue.setStatus(MsgQueueStatus.WAITING);
        msgQueue.setSender(MAIL_SENDER);
        msgQueue.setRetry(BigDecimal.ZERO);
        msgQueue.setTitle(context.getVariable("emailTitle").toString());
        msgQueue.setContent(htmlBody);
        
        msgQueueRepository.save(msgQueue);
	}
	
}
