/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;


import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import apm.modules.sys.entity.User;
import apm.modules.sys.support.Users;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity支持类
 * @author resite
 * @version 2013-01-15
 */
@MappedSuperclass
public abstract class BaseEntity extends IdEntity<String> {

	private static final long serialVersionUID = 1L;
	
	protected User createBy;	// 创建者
	protected User updateBy;	// 更新者
	
	public BaseEntity(){
		super();
	}
	
	public BaseEntity(String id){
		this();
		this.id = id;
	}

	@PrePersist
	public void prePersist() {
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		User user = Users.currentUser();
		if (user.getId() != null){
			this.setCreateBy(user);
			this.setUpdateBy(user);
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.setUpdateDate(new Date());
		User user = Users.currentUser();
		if (user.getId() != null){
			this.setUpdateBy(user);
		}
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public int compareTo(AbstractEntity<String> o) {
		if (o instanceof BaseEntity) {
			BaseEntity qs = (BaseEntity) o;
			if (this.id != null && qs.getId() != null) {
				int ret = this.compareTo(qs);
				if(ret != 0) {
					return ret;
				}
			}
		}
		return super.compareTo(o);
	}
	
}