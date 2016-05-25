/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.List;

import apm.common.service.BaseService;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.AreaDao;
import apm.modules.sys.entity.Area;
import apm.modules.sys.support.Users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 区域Service
 * @author resite
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService<AreaDao, Area> {
	
	public List<Area> findAll(){
		return Users.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		if (StringUtils.isEmpty(area.getParent().getId())) {
			area.setParent(this.get("1"));
		}else {
			area.setParent(this.get(area.getParent().getId()));
		}
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		area.setParentIds(area.getParent().getParentIds()+area.getParent().getId()+",");
		dao.clear();
		dao.save(area);
		// 更新子节点 parentIds
		List<Area> list = dao.findByParentIdsLike("%,"+area.getId()+",%");
		for (Area e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
		}
		dao.clear();
		dao.save(list);
		Users.removeCache(Users.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dao.deleteById(id, "%,"+id+",%");
		Users.removeCache(Users.CACHE_AREA_LIST);
	}
	
}
