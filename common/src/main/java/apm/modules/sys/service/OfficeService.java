/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.List;

import apm.common.service.BaseService;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.OfficeDao;
import apm.modules.sys.entity.Office;
import apm.modules.sys.support.Users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 机构Service
 * @author resite
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService<OfficeDao, Office> {

	public List<Office> findAll(){
		return Users.getOfficeList();
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		if (StringUtils.isEmpty(office.getParent().getId())) {
			office.setParent(this.get("1"));
		}else {
			office.setParent(this.get(office.getParent().getId()));
		}
		
		if (StringUtils.isEmpty(office.getArea().getId())) {
			office.getArea().setId("1");
		}
		String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		office.setParentIds(office.getParent().getParentIds()+office.getParent().getId()+",");
		dao.clear();
		dao.save(office);
		// 更新子节点 parentIds
		List<Office> list = dao.findByParentIdsLike("%,"+office.getId()+",%");
		for (Office e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
		}
		dao.clear();
		dao.save(list);
		Users.removeCache(Users.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dao.deleteById(id, "%,"+id+",%");
		Users.removeCache(Users.CACHE_OFFICE_LIST);
	}
	
}
