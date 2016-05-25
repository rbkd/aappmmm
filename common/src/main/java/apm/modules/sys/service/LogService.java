/**
 * Copyright &copy; 2012-2013 <a href="http://sz-aistor.com/">Newsite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.service;


import java.util.Date;
import java.util.Map;

import apm.common.core.Page;
import apm.common.service.BaseService;
import apm.common.utils.DateUtils;
import apm.common.utils.StringUtils;
import apm.modules.sys.dao.LogDao;
import apm.modules.sys.entity.Log;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 日志Service
 * @author Zaric
 * @date  2013-5-31 上午12:56:06
 */
@Service
@Transactional(readOnly = true)
public class LogService extends BaseService<LogDao, Log> {

	public Page<Log> find(Page<Log> page, Map<String, Object> paramMap) {
		DetachedCriteria dc = dao.createDetachedCriteria();

		String createById = ObjectUtils.toString(paramMap.get("createById"));
		if (StringUtils.isNotEmpty(createById)){
			dc.add(Restrictions.eq("createBy.id", createById));
		}
		
		String uri = ObjectUtils.toString(paramMap.get("uri"));
		if (StringUtils.isNotBlank(uri)){
			dc.add(Restrictions.like("uri", "%"+uri+"%"));
		}

		String exception = ObjectUtils.toString(paramMap.get("exception"));
		if (StringUtils.isNotBlank(exception)){
			dc.add(Restrictions.eq("type", Log.TYPE_EXCEPTION));
		}
		
		Date beginDate = DateUtils.parseDate(paramMap.get("beginDate"));
		if (beginDate == null){
			beginDate = DateUtils.setDays(new Date(), 1);
			paramMap.put("beginDate", DateUtils.formatDate(beginDate, "yyyy-MM-dd"));
		}
		Date endDate = DateUtils.parseDate(paramMap.get("endDate"));
		if (endDate == null){
			endDate = DateUtils.addDays(DateUtils.addMonths(beginDate, 1), -1);
			paramMap.put("endDate", DateUtils.formatDate(endDate, "yyyy-MM-dd"));
		}
		dc.add(Restrictions.between("createDate", beginDate, endDate));
		
		dc.addOrder(Order.desc("createDate"));
		return dao.find(page, dc);
	}
	
}