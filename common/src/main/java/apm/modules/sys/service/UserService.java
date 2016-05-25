/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.Date;

import apm.common.core.Page;
import apm.common.service.BaseService;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.UserDao;
import apm.modules.sys.entity.User;
import apm.modules.sys.security.SystemAuthorizingRealm;
import apm.modules.sys.support.Users;

import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
public class UserService extends BaseService<UserDao, User> {
	
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		dao.clear();
		dao.save(user);
		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(String id) {
		dao.deleteById(id);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		DetachedCriteria dc = dao.createDetachedCriteria();
		User currentUser = Users.currentUser();
		dc.createAlias("company", "company");
		if (user.getCompany()!=null && StringUtils.isNotEmpty(user.getCompany().getId())){
			dc.add(Restrictions.or(
					Restrictions.eq("company.id", user.getCompany().getId()),
					Restrictions.like("company.parentIds", "%,"+user.getCompany().getId()+",%")
					));
		}
		dc.createAlias("office", "office");
		if (user.getOffice()!=null && StringUtils.isNotEmpty(user.getOffice().getId())){
			dc.add(Restrictions.or(
					Restrictions.eq("office.id", user.getOffice().getId()),
					Restrictions.like("office.parentIds", "%,"+user.getOffice().getId()+",%")
					));
		}
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()){
			dc.add(Restrictions.ne("id", "1"));
		}
		dc.add(dataScopeFilter(currentUser, "office", ""));
		//System.out.println(dataScopeFilterString(currentUser, "office", ""));
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%"+user.getLoginName()+"%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%"+user.getName()+"%"));
		}
		dc.add(Restrictions.eq(User.DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("company.code")).addOrder(Order.asc("office.code")).addOrder(Order.desc("id"));
		}
		return dao.find(page, dc);
	}
	
	public User getUser(String id) {
		return dao.findOne(id);
	}
	
	public User findByLoginName(String username) {
		return dao.findByLoginName(username);
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		dao.updatePasswordById(Users.entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(String id) {
		dao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
}