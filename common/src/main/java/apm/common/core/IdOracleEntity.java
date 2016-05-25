/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * <p> 抽象实体基类，提供统一的ID，和相关的基本功能方法
 * <p> 如果是如mysql这种自动生成主键的，请参考{@link BaseEntity}
 * <p/>
 * 子类只需要在类头上加 @SequenceGenerator(name="seq", sequenceName="你的sequence名字")
 * <p/>
 * <p/>
 * @author Zaric
 * @param <PK>
 */
@MappedSuperclass
public abstract class IdOracleEntity<PK extends Serializable> extends AbstractEntity<PK> {

	private static final long serialVersionUID = 1L;
	
	protected PK id;
	
	public IdOracleEntity(){
	}
	
	public IdOracleEntity(PK id){
		this();
		this.id = id;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    public PK getId() {
        return id;
    }

    @Override
    public void setId(PK id) {
        this.id = id;
    }
}
