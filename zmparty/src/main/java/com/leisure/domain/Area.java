package com.leisure.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.leisure.core.constant.Globals;
import com.leisure.core.domain.IdEntity;

/**
 * @see 地区
 * 
 * @author xiaoxiong
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "area")
public class Area extends IdEntity {
	private String areaName;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<Area> childs = new ArrayList<Area>();
	@ManyToOne(fetch = FetchType.LAZY)
	private Area parent;
	private int sequence;
	private int level;
	@Column(columnDefinition = "bit default false")
	private boolean common;// common area

	public boolean isCommon() {
		return common;
	}

	public void setCommon(boolean common) {
		this.common = common;
	}

	public List<Area> getChilds() {
		return childs;
	}

	public void setChilds(List<Area> childs) {
		this.childs = childs;
	}

	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
