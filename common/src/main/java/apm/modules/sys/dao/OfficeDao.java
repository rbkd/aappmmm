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
import apm.modules.sys.entity.Office;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 机构DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-05-15
 */
public interface OfficeDao extends OfficeDaoCustom, JpaDao<Office> {

	@Modifying
	@Query("update Office set delFlag='" + Office.DEL_FLAG_DELETE + "' where id = ?1 or parentIds like ?2")
	public int deleteById(String id, String likeParentIds);
	
	public List<Office> findByParentIdsLike(String parentIds);

//	@Query("from Office where delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
//	public List<Office> findAllList();
//	
	@Query("from Office where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
	public List<Office> findAllChild(String parentId, String likeParentIds);
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface OfficeDaoCustom extends Dao<Office> {

}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class OfficeDaoImpl extends DaoImpl<Office> implements OfficeDaoCustom {

}
