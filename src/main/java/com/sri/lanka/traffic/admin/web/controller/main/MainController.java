package com.sri.lanka.traffic.admin.web.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
public class MainController {
	
	@Authority(authType = AuthType.READ)
    @GetMapping("/main")
    public String login(Model model){
        return "views/main/main";
    }
}
