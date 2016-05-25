/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

/**
 * 抽象实体基类
 * <p>@author Zaric
 * <p>@date 2013-9-18 下午1:48:38
 */
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>, Comparable<AbstractEntity<ID>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ID id;		// 编号

	public abstract ID getId();

    public abstract void setId(final ID id);
    
    public boolean isNew() {
        return null == getId();
    }

	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";
	
	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：删除；1：正常；2：审核；3：过期；）
	public static final String DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_DELETE = "0";
	public static final String DEL_FLAG_NORMAL = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	public static final String DEL_FLAG_EXPIRED = "3";
	
	@Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        AbstractEntity<?> that = (AbstractEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
	
    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    public int compareTo(AbstractEntity<ID> o) {
		if (o instanceof AbstractEntity) {
			AbstractEntity<ID> be = (AbstractEntity<ID>) o;
			if (this.id != null && be.getId() != null) {
				return this.compareTo(be);
			}
		}
		return 0;
	}
    
}