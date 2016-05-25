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
import apm.modules.sys.entity.Dict;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 字典DAO接口
 * <p>@author zhangzuoqiang
 * @version 2013-01-15
 */
public interface DictDao extends DictDaoCustom, JpaDao<Dict> {
	
	@Modifying
	@Query("update Dict set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(String id);

	@Query("from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' order by sort")
	public List<Dict> findAllList();

	@Query("select type from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' group by type")
	public List<String> findTypeList();
	
//	@Query("from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and type=?1 order by sort")
//	public List<Dict> findByType(String type);
//	
//	@Query("select label from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and value=?1 and type=?2")
//	public List<String> findValueByValueAndType(String value, String type);
	
}

/**
 * DAO自定义接口
 * <p>@author zhangzuoqiang
 */
interface DictDaoCustom extends Dao<Dict> {

	//public List<Dict> findAllList();

}

/**
 * DAO自定义接口实现
 * <p>@author zhangzuoqiang
 */
@Repository
class DictDaoImpl extends DaoImpl<Dict> implements DictDaoCustom {

	/*public List<Dict> findAllList() {
		return findBySql("from Dict where delFlag=? order by sort", Dict.DEL_FLAG_NORMAL);
	}*/

}
