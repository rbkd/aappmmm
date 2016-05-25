/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * @author 薛定谔的猫
 * @date 2013-5-28 下午9:56:38
 */
public interface JpaDao<T extends IdEntity<String>> extends BaseDao<T>,
		JpaRepository<T, String>, JpaSpecificationExecutor<T> {
	
}
