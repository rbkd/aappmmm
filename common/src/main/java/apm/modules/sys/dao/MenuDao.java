/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.dao;


import java.util.List;

import apm.common.core.Dao;
import apm.common.core.DaoImpl;
import apm.common.core.JpaDao;
import apm.modules.sys.entity.Menu;
import apm.modules.sys.entity.Role;
import apm.modules.sys.entity.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 菜单DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-05-15
 */
public interface MenuDao extends MenuDaoCustom, JpaDao<Menu> {

	@Modifying
	@Query("update Menu set delFlag='" + Menu.DEL_FLAG_DELETE + "' where id = ?1 or parentIds like ?2")
	public int deleteById(String id, String likeParentIds);
	
	public List<Menu> findByParentIdsLike(String parentIds);

	@Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' order by sort")
	public List<Menu> findAllList();
	
	/*@Query("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
			" and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL + 
			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1" + // or (m.user.id=?1  and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "')" + 
			" order by m.sort")*/
	@Query("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
			" and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL + 
			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (m.createBy.id=?1  and m.delFlag='" + Menu.DEL_FLAG_NORMAL + 
			"') order by m.sort")
	public List<Menu> findByUserId(String userId);
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface MenuDaoCustom extends Dao<Menu> {

}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class MenuDaoImpl extends DaoImpl<Menu> implements MenuDaoCustom {

}
