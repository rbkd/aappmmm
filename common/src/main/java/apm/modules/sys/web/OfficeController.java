/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.web;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import apm.common.config.Global;
import apm.common.security.SecureModelAttribute;
import apm.common.utils.AM;
import apm.common.utils.StringUtils;
import apm.common.web.BaseController;
import apm.common.web.MediaTypes;
import apm.modules.sys.entity.Office;
import apm.modules.sys.service.OfficeService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 机构Controller
 * <p>@author zhangzuoqiang
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = OfficeController.MODULE_PATH)
public class OfficeController extends BaseController<OfficeService, Office> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/office";
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = {"list", ""})
	public String list(Office office, Model model) {
//		User user = Users.currentUser();
//		if(user.isAdmin()){
			office.setId("1");
//		}else{
//			office.setId(user.getOffice().getId());
//		}
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = service.findAll();
		Office.sortList(list, sourcelist, office.getId());
        model.addAttribute("list", list);
		return "modules/sys/officeList";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(@Valid @SecureModelAttribute(deniedField = {"parent.parent.*"}) Office office, 
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		service.save(office);
		addMessage(redirectAttributes, AM.rs("sys_office_save_success", office.getName()));
		return "redirect:"+Global.getAdminPath()+"/sys/office/";
	}
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if (Office.isRoot(id)){
			addMessage(redirectAttributes, AM.rs("sys_office_delete_failure"));
		}else{
			service.delete(id);
			addMessage(redirectAttributes, AM.rs("sys_office_delete_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/office/";
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) Long type,
			@RequestParam(required=false) Long grade, HttpServletResponse response) {
		response.setContentType(MediaTypes.JSON_UTF_8);
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = Users.currentUser();
		List<Office> list = service.findAll();
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			if ((StringUtils.isEmpty(extId) || (StringUtils.isNotEmpty(extId) && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue()))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
