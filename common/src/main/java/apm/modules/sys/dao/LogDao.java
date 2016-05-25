/**
 * Copyright &copy; 2012-2013 <a href="http://sz-aistor.com/">Newsite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.dao;


import java.util.List;

import apm.common.core.Dao;
import apm.common.core.DaoImpl;
import apm.common.core.JpaDao;
import apm.modules.sys.entity.Log;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * 字典DAO接口
 * @author Zaric
 * @version 2013-01-15
 */
public interface LogDao extends LogDaoCustom, JpaDao<Log> {
	
	@Modifying
	@Query("update Log set delFlag='" + Log.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(String id);

	@Query("from Log where delFlag='" + Log.DEL_FLAG_NORMAL + "' order by createDate")
	public List<Log> findAllList();
}

/**
 * DAO自定义接口
 * @author Zaric
 */
interface LogDaoCustom extends Dao<Log> {

}

/**
 * DAO自定义接口实现
 * @author Zaric
 */
@Repository
class LogDaoImpl extends DaoImpl<Log> implements LogDaoCustom {

}
