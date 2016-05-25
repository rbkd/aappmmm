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
import apm.modules.sys.entity.Area;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 区域DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-01-15
 */
public interface AreaDao extends AreaDaoCustom, JpaDao<Area> {

	@Modifying
	@Query("update Area set delFlag='" + Area.DEL_FLAG_DELETE + "' where id = ?1 or parentIds like ?2")
	public int deleteById(String id, String likeParentIds);
	
	public List<Area> findByParentIdsLike(String parentIds);

	@Query("from Area where delFlag='" + Area.DEL_FLAG_NORMAL + "' order by code")
	public List<Area> findAllList();
	
	@Query("from Area where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Area.DEL_FLAG_NORMAL + "' order by code")
	public List<Area> findAllChild(String parentId, String likeParentIds);
	
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface AreaDaoCustom extends Dao<Area> {

}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class AreaDaoImpl extends DaoImpl<Area> implements AreaDaoCustom {

}
