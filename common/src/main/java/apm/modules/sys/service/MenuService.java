/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.List;

import apm.common.service.BaseService;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.MenuDao;
import apm.modules.sys.entity.Menu;
import apm.modules.sys.security.SystemAuthorizingRealm;
import apm.modules.sys.support.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author resite
 * @version 2013-4-19
 */
@Service
@Transactional(readOnly = true)
public class MenuService extends BaseService<MenuDao, Menu> {
	
	@Autowired
	private SystemAuthorizingRealm systemRealm;

	public List<Menu> findAllMenu(){
		return Users.getMenuList();
	}
	
	public Menu getMenu(String id){
		return dao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		if (StringUtils.isEmpty(menu.getParent().getId())) {
			menu.setParent(this.get("1"));
		}else {
			menu.setParent(this.getMenu(menu.getParent().getId()));
		}
		String oldParentIds = menu.getParentIds() ; // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		dao.clear();
		dao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = dao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		dao.clear();
		dao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		Users.removeCache(Users.CACHE_MENU_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		dao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		Users.removeCache(Users.CACHE_MENU_LIST);
	}
}
