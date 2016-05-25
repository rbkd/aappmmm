package apm.common.web;


import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import apm.common.core.TreeEntity;
import apm.common.service.BaseService;
import apm.common.utils.Reflections;
import apm.common.utils.SpringContextHolder;
import apm.common.utils.StringUtils;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * <p>@author Zaric
 * <p>@date 2013-9-12 下午1:27:52
 */
public abstract class TreeController<S extends BaseService<?, T>, T extends TreeEntity<?>> extends AbstractController {

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
	public T get(@RequestParam(required = false) String id) throws InstantiationException, IllegalAccessException {
		if (StringUtils.isNotEmpty(id)) {
			return service.get(id);
		} else {
			return entityClass.newInstance();
		}
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType(MediaTypes.JSON_UTF_8);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<T> list = service.findNodes();
		for (int i=0; i<list.size(); i++){
			T e = list.get(i);
			if (StringUtils.isEmpty(extId) || (StringUtils.isNotEmpty(extId) && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
