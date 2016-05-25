/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.service;


import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import apm.common.core.DynamicSpecifications;
import apm.common.core.IdEntity;
import apm.common.core.JpaDao;
import apm.common.core.SearchFilter;
import apm.common.utils.Reflections;
import apm.common.utils.SpringContextHolder;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service基类
 * @author resite
 * @version 2013-05-15
 */
@Transactional(readOnly = true)
public abstract class BaseService<S extends JpaDao<T>, T extends IdEntity<String>> extends DataService {
	
	protected S dao;
	
	private Class<S> daoClass;
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		daoClass = Reflections.getClassGenricType(getClass());
		entityClass = Reflections.getClassGenricType(getClass(), 1);
		dao = (S) SpringContextHolder.getBean(daoClass);
	}

	protected BaseService() {
	}
	
	public boolean exists(String id) {
		return dao.exists(id);
	}
	
	public long count() {
		return dao.count();
	}
	
	public T get(String id) {
		if (null ==id) {
			return null;
		}
		return dao.findOne(id);
	}

	public T load(String id) {
		T t = dao.findOne(id);
		if (t == null) {
			throw new ServiceException("id:" + entityClass.getSimpleName() + id + "在数据库里没有匹配的对象");
		}
		return t;
	}
	
	public List<T> findNodes() {
		return dao.findNodes();
	}
	
	public List<T> findAll() {
		return dao.findAll();
	}
	
	public List<T> findAll(Iterable<String> ids) {
		return (List<T>) dao.findAll(ids);
	}
	
	public List<T> findAll(Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), entityClass);
		
		return dao.findAll(spec);
	}
	
	/*public List<T> list(FilterContext filterContext) {
		Specification<T> specification = filterContext.getSpecification();
		Pageable pageable = filterContext.getPageable();
		Page<T> page = dao.findAll(specification, pageable);
		filterContext.setTotal(page.getTotalElements());
		return page.getContent();
	}*/

	@Transactional(readOnly = false)
	public void save(T t) {
		dao.clear();
		dao.save(t);
	}
	
	@Transactional(readOnly = false)
	public Iterable<T> save(Iterable<T> entities) {
		dao.clear();
		return dao.save(entities);
	}
	
	/**
	 * 逻辑还原
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void revert(String id) {
		dao.update("update " + entityClass.getSimpleName() + " set delFlag=" + T.DEL_FLAG_NORMAL + " where id = ?", id);
	}

	/**
	 * 逻辑还原
	 * @param t
	 */
	@Transactional(readOnly = false)
	public void revert(T t) {
		dao.update("update " + entityClass.getSimpleName() + " set delFlag=" + T.DEL_FLAG_NORMAL + " where id = ?", t.getId());
	}

	/**
	 * 逻辑删除
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delete(String id) {
		dao.update("update " + entityClass.getSimpleName() + " set delFlag=" + T.DEL_FLAG_DELETE + " where id = ?", id);
	}
	
	/**
	 * 逻辑删除
	 * @param t
	 */
	@Transactional(readOnly = false)
	public void delete(T t) {
		dao.update("update " + entityClass.getSimpleName() + " set delFlag=" + T.DEL_FLAG_DELETE + " where id = ?", t.getId());
	}

	/**
	 * 物理删除
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void remove(String id) {
		dao.delete(id);
	}

	/**
	 * 物理删除
	 * @param t
	 */
	@Transactional(readOnly = false)
	public void remove(T t) {
		dao.delete(t);
	}
	
	/**
	 * 物理删除
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public void remove(Iterable<String> ids) {
		for(String id : ids) {
			dao.delete(id);
		}
	}
	
}
