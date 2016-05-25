/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.dao;

import apm.common.core.Dao;
import apm.common.core.DaoImpl;
import apm.common.core.JpaDao;
import apm.modules.sys.entity.Role;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 角色DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-05-15
 */
public interface RoleDao extends RoleDaoCustom, JpaDao<Role> {
	
	@Query("from Role where name = ?1 and delFlag = '" + Role.DEL_FLAG_NORMAL + "'")
	public Role findByName(String name);

	@Modifying
	@Query("update Role set delFlag='" + Role.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(String id);

//	@Query("from Role where delFlag='" + Role.DEL_FLAG_NORMAL + "' order by name")
//	public List<Role> findAllList();
//
//	@Query("select distinct r from Role r, User u where r in elements (u.roleList) and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (r.user.id=?1 and r.delFlag='" + Role.DEL_FLAG_NORMAL +
//			"') order by r.name")
//	public List<Role> findByUserId(Long userId);
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface RoleDaoCustom extends Dao<Role> {
}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class RoleDaoImpl extends DaoImpl<Role> implements RoleDaoCustom {

}
