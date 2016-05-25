package apm.common.core;


import java.util.Date;

import javax.persistence.MappedSuperclass;

import apm.common.utils.excel.annotation.ExcelField;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 账户
 * <p>@author Zaric
 * <p>@date 2013-9-10 上午9:16:42
 */
@MappedSuperclass
public class AccountEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String icoUrl; // 头像url
	private String loginName;// 登录名
	private String password;// 密码
	private String name; // 姓名
	private String email; // 邮箱
	private String loginIp; // 最后登陆IP
	private Date loginDate; // 最后登陆日期

	@Length(min = 1, max = 100)
	@ExcelField(title = "登录名", align = 2, sort = 30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min = 1, max = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min = 1, max = 100)
	@ExcelField(title = "姓名", align = 2, sort = 40)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email
	@Length(min = 0, max = 200)
	@ExcelField(title = "邮箱", align = 1, sort = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ExcelField(title = "最后登录IP", type = 1, align = 1, sort = 100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "最后登录日期", type = 1, align = 1, sort = 110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getIcoUrl() {
		return icoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}

	@Override
	public int compareTo(AbstractEntity<String> o) {
		if (o instanceof AccountEntity) {
			AccountEntity qs = (AccountEntity) o;
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