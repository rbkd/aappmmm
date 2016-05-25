/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.dao;


import java.util.Date;

import apm.common.core.Dao;
import apm.common.core.DaoImpl;
import apm.common.core.JpaDao;
import apm.modules.sys.entity.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 用户DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-01-15
 */
public interface UserDao extends UserDaoCustom, JpaDao<User> {
	
	@Query("from User where loginName = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public User findByLoginName(String loginName);

	@Modifying
	@Query("update User set delFlag='" + User.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(String id);
	
	@Modifying
	@Query("update User set password=?1 where id = ?2")
	public int updatePasswordById(String newPassword, String id);
	
	@Modifying
	@Query("update User set loginIp=?1, loginDate=?2 where id = ?3")
	public int updateLoginInfo(String loginIp, Date loginDate, String id);
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface UserDaoCustom extends Dao<User> {

}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class UserDaoImpl extends DaoImpl<User> implements UserDaoCustom {

}
