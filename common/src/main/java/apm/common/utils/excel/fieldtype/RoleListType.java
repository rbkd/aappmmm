/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.utils.excel.fieldtype;


import java.util.List;
import java.util.Set;

import apm.common.utils.Collections3;
import apm.common.utils.SpringContextHolder;
import apm.common.utils.StringUtils;
import apm.modules.sys.entity.Role;
import apm.modules.sys.service.RoleService;



import com.google.common.collect.Sets;

/**
 * 字段类型转换
 * <p>@author zhangzuoqiang
 * @version 2013-5-29
 */
public class RoleListType {

	private static RoleService roleService = SpringContextHolder.getBean(RoleService.class);
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		Set<Role> roleList = Sets.newHashSet(); 
		List<Role> allRoleList = roleService.findAllRole();
		for (String s : StringUtils.split(val, ",")){
			for (Role e : allRoleList){
				if (e.getName().equals(s)){
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			Set<Role> roleList = (Set<Role>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}
	
}
