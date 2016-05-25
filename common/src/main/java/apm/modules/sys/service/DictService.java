/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.List;

import apm.common.core.Page;
import apm.common.service.BaseService;
import apm.common.utils.CacheUtils;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.DictDao;
import apm.modules.sys.entity.Dict;
import apm.modules.sys.support.Dicts;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 字典Service
 * @author resite
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService<DictDao, Dict> {

	public Page<Dict> find(Page<Dict> page, Dict dict) {
		DetachedCriteria dc = dao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(dict.getType())){
			dc.add(Restrictions.eq("type", dict.getType()));
		}
		if (StringUtils.isNotEmpty(dict.getDescription())){
			dc.add(Restrictions.like("description", "%"+dict.getDescription()+"%"));
		}
		dc.add(Restrictions.eq(Dict.DEL_FLAG, Dict.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
		return dao.find(page, dc);
	}
	
	public List<String> findTypeList(){
		return dao.findTypeList();
	}
	
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		dao.clear();
		dao.save(dict);
		CacheUtils.remove(Dicts.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dao.deleteById(id);
		CacheUtils.remove(Dicts.CACHE_DICT_MAP);
	}
	
}
