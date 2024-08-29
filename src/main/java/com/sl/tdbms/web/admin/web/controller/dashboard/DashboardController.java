package com.sl.tdbms.web.admin.web.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.config.authentication.Authority;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
public class DashboardController {

	@Authority(authType = AuthType.READ)
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		return "views/dashboard/dashboard";
	}
}
