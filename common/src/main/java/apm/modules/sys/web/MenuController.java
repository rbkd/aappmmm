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
import apm.modules.sys.entity.Menu;
import apm.modules.sys.service.MenuService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.google.common.collect.Lists;

/**
 * 菜单Controller
 * <p>@author zhangzuoqiang
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = MenuController.MODULE_PATH)
public class MenuController extends TreeController<MenuService, Menu> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/menu";

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = service.findAllMenu();
		Menu.sortList(list, sourcelist, "1");
        model.addAttribute("list", list);
		return "modules/sys/menuList";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		model.addAttribute("menu", menu);
		return "modules/sys/menuForm";
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	public String save(@Valid @SecureModelAttribute(deniedField = {"parent.parent.*"}) Menu menu, 
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		service.saveMenu(menu);
		addMessage(redirectAttributes, AM.rs("sys_menu_save_success", menu.getName()));
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if (Menu.isRoot(id)){
			addMessage(redirectAttributes, AM.rs("sys_menu_delete_failure"));
		}else{
			service.deleteMenu(id);
			addMessage(redirectAttributes, AM.rs("sys_menu_delete_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}

	@RequiresUser
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menuTree";
	}
	
	/**
	 * 批量修改菜单排序
	 * @param ids
	 * @param sorts
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, Model model, RedirectAttributes redirectAttributes) {
		if (ids == null || ids.length == 0) {
	    	addMessage(redirectAttributes, AM.rs("sys_menu_no_data"));
		}else {
        	int len = ids.length;
        	Menu[] menus = new Menu[len];
        	for (int i = 0; i < len; i++) {
        		menus[i] = service.getMenu(ids[i]);
        		menus[i].setSort(sorts[i]);
        		service.saveMenu(menus[i]);
        	}
        	addMessage(redirectAttributes, AM.rs("sys_menu_sort_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
}
