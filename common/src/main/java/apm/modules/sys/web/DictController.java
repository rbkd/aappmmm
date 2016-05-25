/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import apm.common.config.Global;
import apm.common.core.Page;
import apm.common.utils.AM;
import apm.common.web.BaseController;
import apm.modules.sys.entity.Dict;
import apm.modules.sys.service.DictService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * 字典Controller
 * <p>@author zhangzuoqiang
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = DictController.MODULE_PATH)
public class DictController extends BaseController<DictService, Dict> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/dict";
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = service.findTypeList();
		model.addAttribute("typeList", typeList);
        Page<Dict> page = service.find(new Page<Dict>(request, response), dict); 
        model.addAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")
	public String save(@Valid Dict dict, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dict)){
			return form(dict, model);
		}
		service.save(dict);
		addMessage(redirectAttributes, AM.rs("sys_dict_save_success", dict.getLabel()));
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage&type="+dict.getType();
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		service.delete(id);
		addMessage(redirectAttributes, AM.rs("sys_dict_delete_success"));
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
	}

}
