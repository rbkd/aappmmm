/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.web;


import javax.annotation.PostConstruct;

import apm.common.core.IdEntity;
import apm.common.service.BaseService;
import apm.common.utils.Reflections;
import apm.common.utils.SpringContextHolder;
import apm.common.utils.StringUtils;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 控制器支持类
 * <p>@author zhangzuoqiang
 * @version 2013-3-23
 * @param <T>
 * @update 2013-09-12
 */
public abstract class BaseController<S extends BaseService<?, T>, T extends IdEntity<String>> extends AbstractController {
	
	protected S service;
	
	private Class<S> serviceClass;
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		serviceClass = Reflections.getClassGenricType(getClass());
		entityClass = Reflections.getClassGenricType(getClass(), 1);
		service = (S) SpringContextHolder.getBean(serviceClass);
	}
	
	@ModelAttribute
	public T get(@RequestParam(required=false) String id) throws InstantiationException, IllegalAccessException {
		if (StringUtils.isNotEmpty(id)){
			return service.get(id);
		}else{
			return entityClass.newInstance();
		}
	}
}
