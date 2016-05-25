/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 基础DAO
 * 
 * @author Zaric
 * @version 2013-05-15
 * @param <T>
 */
public interface BaseDao<T extends IdEntity<String>> extends Dao<T>,
		JpaRepository<T, String>, JpaSpecificationExecutor<T> {
	
}