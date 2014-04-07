package com.leisure.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.leisure.core.constant.Globals;
import com.leisure.core.domain.IdEntity;

/**
 * @see 角色
 * 
 * @author xiaoxiong
 * 
 */
@SuppressWarnings("rawtypes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "role")
public class Role extends IdEntity implements java.lang.Comparable {
	private String roleName;
	private String roleCode;// To identify the role of the code
	private String type;
	private String info;
	@Column(columnDefinition = "bit default true")
	private boolean display;
	private int sequence;// order
	@ManyToOne(fetch = FetchType.LAZY)
	private RoleGroup rg;
	@ManyToMany(targetEntity = Res.class, fetch = FetchType.LAZY)
	@JoinTable(name = Globals.DEFAULT_TABLE_SUFFIX + "role_res", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "res_id"))
	private List<Res> reses = new ArrayList<Res>();

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<Res> getReses() {
		return reses;
	}

	public void setReses(List<Res> reses) {
		this.reses = reses;
	}

	@Override
	public int compareTo(Object obj) {
		Role role = (Role) obj;
		if (super.getId().equals(role.getId())) {
			return 0;
		}
		return 1;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public RoleGroup getRg() {
		return rg;
	}

	public void setRg(RoleGroup rg) {
		this.rg = rg;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
}
