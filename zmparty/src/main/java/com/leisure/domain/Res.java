package com.leisure.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.leisure.core.constant.Globals;
import com.leisure.core.domain.IdEntity;

/**
 * @see 系统资源
 * @author xiaoxiong
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "res")
public class Res extends IdEntity {
	private String resName;
	private String type;//url
	private String value;
	@ManyToMany(mappedBy = "reses", targetEntity = Role.class, fetch = FetchType.EAGER)
	private List<Role> roles = new ArrayList<Role>();
	private int sequence;
	private String info;

	@Transient
	public String getRoleAuthorities() {
		List<String> roleAuthorities = new ArrayList<String>();
		for (Role role : roles) {
			roleAuthorities.add(role.getRoleCode());
		}
		return StringUtils.join(roleAuthorities.toArray(), ",");
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
