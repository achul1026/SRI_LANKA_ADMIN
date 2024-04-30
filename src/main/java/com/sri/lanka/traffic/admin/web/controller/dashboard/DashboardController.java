package com.sri.lanka.traffic.admin.web.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
public class DashboardController {

	@Authority(authType = AuthType.READ)
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		return "views/dashboard/dashboard";
	}
}
