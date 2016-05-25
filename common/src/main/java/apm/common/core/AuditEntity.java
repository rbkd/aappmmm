/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;


import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import apm.modules.sys.entity.User;
import apm.modules.sys.support.Users;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity支持类
 * @author Zaric
 * @version 2013-01-15
 */
@MappedSuperclass
public abstract class AuditEntity extends IdEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private User auditUser;
	private Date auditDate;
	private String auditContent;

	
	@PrePersist
	public void prePersist() {
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
	}

	@PreUpdate
	public void preUpdate() {
		this.setUpdateDate(new Date());
		User user = Users.currentUser();
		if (user.getId() != null){
			this.setAuditUser(user);
		}
		this.setAuditDate(new Date());
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="audit_user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

}
