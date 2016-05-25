/**
 * Copyright &copy; 2012-2013 <a href="http://sz-aistor.com/">Newsite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.modules.sys.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import apm.common.core.BaseEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import com.google.common.base.Objects;

/**
 * 菜单Entity
 * 
 * @author Zaric
 * @version 2013-01-15
 */
@Entity
@Table(name = "sys_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Log extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private User user;
	private String userName;
	private String loginName;
	private String uri;
	private String method;
	private String accessorIp;
	private String params;
	private String handlers;
	private String type; 		// 日志类型（1：接入日志；2：错误日志）
	private String exception; 	// 异常信息
	
	public static final String TYPE_ACCESS = "1";
	public static final String TYPE_EXCEPTION = "2";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAccessorIp() {
		return accessorIp;
	}

	public void setAccessorIp(String accessorIp) {
		this.accessorIp = accessorIp;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getHandlers() {
		return handlers;
	}

	public void setHandlers(String handlers) {
		this.handlers = handlers;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("userName", userName)
				.add("loginName", loginName)
				.add("uri", uri)
				.add("method", method)
				.add("accessorIp", accessorIp)
				.add("params", params)
				.add("handlers", handlers).toString();
	}

}