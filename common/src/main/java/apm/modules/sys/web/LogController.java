/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.web;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apm.common.core.Page;
import apm.common.web.BaseController;
import apm.modules.sys.entity.Log;
import apm.modules.sys.service.LogService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 日志Controller
 * @author resite
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = LogController.MODULE_PATH)
public class LogController extends BaseController<LogService, Log> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/log";

	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = service.find(new Page<Log>(request, response), paramMap); 
        model.addAttribute("page", page);
        model.addAllAttributes(paramMap);
		return "modules/sys/logList";
	}

}