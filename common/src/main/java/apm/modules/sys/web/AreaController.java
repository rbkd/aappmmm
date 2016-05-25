/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.web;


import java.util.List;

import javax.validation.Valid;

import apm.common.config.Global;
import apm.common.security.SecureModelAttribute;
import apm.common.utils.AM;
import apm.common.web.TreeController;
import apm.modules.sys.entity.Area;
import apm.modules.sys.service.AreaService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.google.common.collect.Lists;

/**
 * 区域Controller
 * <p>@author zhangzuoqiang
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = AreaController.MODULE_PATH)
public class AreaController extends TreeController<AreaService, Area> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/area";

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
//		User user = Users.currentUser();
//		if(user.isAdmin()){
			area.setId("1");
//		}else{
//			area.setId(user.getArea().getId());
//		}
		model.addAttribute("area", area);
		List<Area> list = Lists.newArrayList();
		List<Area> sourcelist = service.findAll();
		Area.sortList(list, sourcelist, area.getId());
        model.addAttribute("list", list);
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(@Valid @SecureModelAttribute(deniedField = {"parent.parent.*"}) Area area, 
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		service.save(area);
		addMessage(redirectAttributes, AM.rs("sys_area_save_success", area.getName()));
		return "redirect:"+Global.getAdminPath()+"/sys/area/";
	}
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if (Area.isAdmin(id)){
			addMessage(redirectAttributes, AM.rs("sys_area_delete_failure"));
		}else{
			service.delete(id);
			addMessage(redirectAttributes, AM.rs("sys_area_delete_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/area/";
	}
}
