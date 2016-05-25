/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
public abstract class IdEntity<ID extends Serializable> extends AbstractEntity<ID> {

	private static final long serialVersionUID = 1L;
	
	protected String remarks;	// 备注
	protected String delFlag; // 删除标记（0：删除；1：正常；2：审核）
	protected Date createDate;// 创建日期
	protected Date updateDate;// 更新日期
	
	protected String className;// 类名
	
	public IdEntity(){
		this.delFlag = DEL_FLAG_NORMAL;
		this.className = getClass().getSimpleName();
	}
	
	public IdEntity(ID id){
		this();
		this.id = id;
	}
	
	@PrePersist
	public void prePersist() {
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
	}

	@PreUpdate
	public void preUpdate() {
		this.setUpdateDate(new Date());
	}

	@DocumentId
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "apm.common.uuid.Base64UuidGenerator")
	@Column(length = 22)
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Length(min=1, max=1)
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

    public int compareTo(AbstractEntity<ID> o) {
		if (o instanceof IdEntity) {
			IdEntity<ID> qs = (IdEntity<ID>) o;
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