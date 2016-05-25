package apm.common.core;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import apm.common.utils.StringUtils;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;


import com.google.common.collect.Lists;

/**
 *  树状结构基类
 * <p>@author Zaric
 * <p>@date 2013-9-10 下午2:05:55
 */
@MappedSuperclass
public class TreeEntity<T extends TreeEntity<T>> extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected T parent;// 父级编号
	protected String parentIds; // 所有父级编号
	protected String code; 	// 编码
	protected String name; 	// 名称
	protected Integer sort; 	// 排序
	
	protected List<T> childList = Lists.newArrayList();// 拥有子列表
	
	public TreeEntity() {
		super();
		this.sort = 30;
	}

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code")
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<T> getChildList() {
		return childList;
	}

	public void setChildList(List<T> childList) {
		this.childList = childList;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}
	
	@Transient
	public static <T extends TreeEntity<T>> void sortList(List<T> list, List<T> sourcelist, String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			T e = sourcelist.get(i);
			if (e.getParent()!=null && StringUtils.isNotEmpty(e.getParent().getId()) && e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					T child = sourcelist.get(j);
					if (child.getParent()!=null && StringUtils.isNotEmpty(child.getParent().getId())
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}
	
	@Transient
	public static boolean isRoot(String id){
		return StringUtils.isNotEmpty(id) && id.equals("1");
	}
	
}