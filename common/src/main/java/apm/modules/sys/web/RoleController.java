/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.web;


import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import apm.common.config.Global;
import apm.common.utils.AM;
import apm.common.web.BaseController;
import apm.common.web.MediaTypes;
import apm.modules.sys.entity.Office;
import apm.modules.sys.entity.Role;
import apm.modules.sys.entity.User;
import apm.modules.sys.service.MenuService;
import apm.modules.sys.service.OfficeService;
import apm.modules.sys.service.RoleService;
import apm.modules.sys.service.UserService;
import apm.modules.sys.support.Users;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 角色Controller
 * <p>@author zhangzuoqiang
 * @version 2013-5-15 update 2013-06-08
 */
@Controller
@RequestMapping(value = RoleController.MODULE_PATH)
public class RoleController extends BaseController<RoleService, Role> {
	
	// 模块页面根路径
	public static final String MODULE_PATH = "${adminPath}/sys/role";

	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;

	@Autowired
	private OfficeService officeService;
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = service.findAllRole();
		model.addAttribute("list", list);
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
//		model.addAttribute("categoryList", categoryService.findByUser(false, null));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleForm";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(@Valid Role role, Model model, String oldName, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, role)){
			return form(role, model);
		}
		if (!"true".equals(checkName(oldName, role.getName()))){
			addMessage(model, AM.rs("sys_role_is_exists", role.getName()));
			return form(role, model);
		}
		service.saveRole(role);
		addMessage(redirectAttributes, AM.rs("sys_role_save_success", role.getName()));
		return "redirect:"+Global.getAdminPath()+"/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam String id, RedirectAttributes redirectAttributes) {
		if (Role.isAdmin(id)){
			addMessage(redirectAttributes, AM.rs("sys_role_delete_failure"));
//		}else if (Users.currentUser().getRoleIdList().contains(id)){
//			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
		}else{
			service.deleteRole(id);
			addMessage(redirectAttributes, AM.rs("sys_role_delete_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		Set<User> users = role.getUserList();
		model.addAttribute("users", users);
		return "modules/sys/roleAssign";
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("selectIds", role.getUserIds());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToRole";
	}
	
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		response.setContentType(MediaTypes.JSON_UTF_8);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Office office = officeService.get(officeId);
		List<User> userList = office.getUserList();
		for (User user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getId());
			map.put("pId", 0);
			map.put("name", user.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		Role role = service.getRole(roleId);
		User user = userService.getUser(userId);
		if (user.equals(Users.currentUser())) {
			addMessage(redirectAttributes, AM.rs("sys_role_remove_self", role.getName(), user.getName()));
		}else {
			Boolean flag = service.outUserInRole(role, userId);
			if (!flag) {
				addMessage(redirectAttributes, AM.rs("sys_role_remove_failure", user.getName(), role.getName()));
			}else {
				addMessage(redirectAttributes, AM.rs("sys_role_remove_success", user.getName(), role.getName()));
			}
		}
		return "redirect:"+Global.getAdminPath()+"/sys/role/assign?id="+role.getId();
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = service.assignUserToRole(role, idsArr[i]);
			if (null != user) {
				msg.append(AM.rs("sys_role_add_one_success", user.getName(), role.getName()));
				newNum++;
			}
		}
		addMessage(redirectAttributes, AM.rs("sys_role_add_success", newNum, msg));
		return "redirect:"+Global.getAdminPath()+"/sys/role/assign?id="+role.getId();
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && service.findRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

}
