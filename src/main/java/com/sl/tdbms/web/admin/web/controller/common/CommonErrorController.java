package com.sl.tdbms.web.admin.web.controller.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonErrorController implements ErrorController {

	@RequestMapping("/error")
    public String errorPage(){
        return "views/common/errorPage";
    }
}
 