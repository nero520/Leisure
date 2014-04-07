package com.leisure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.leisure.core.constant.Globals;
import com.leisure.core.domain.IdEntity;

/**
 * @see 系统日志，默认关闭
 * @author xiaoxiong
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "syslog")
public class SysLog extends IdEntity {
	private String title;
	private int type;//0作为通用日志，1为异常日志
	@Lob
	@Column(columnDefinition = "LongText")
	private String content;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	private String ip;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
