package com.leisure.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

import com.leisure.domain.UserConfig;
import com.leisure.core.annotation.Lock;
import com.leisure.core.constant.Globals;
import com.leisure.core.domain.IdEntity;

/**
 * @see 用户，所有用户都使用该类管理, 包括普通用户，管理员，操作员等
 * @author xiaoxiong
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "user")
public class User extends IdEntity implements UserDetails {
	private static final long serialVersionUID = 8026813053768023527L;
	private String userName;
	private String trueName;
	@Lock
	private String password;
	private String userRole;
	private Date birthday;
	private String telephone;
	private String QQ;
	private String WW;
	@Column(columnDefinition = "int default 0")
	private int years;
	private String MSN;
	private String address;
	private int sex;// 1 男、0 女、-1 保密
	private String email;
	private String mobile;
	@OneToOne
	private Accessory photo;
	@OneToOne
	private Area area;// 家乡
	private int status;
	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = Globals.DEFAULT_TABLE_SUFFIX + "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new TreeSet<Role>();
	@Transient
	private Map<String, List<Res>> roleResources;
	private Date lastLoginDate;
	private Date loginDate;
	private String lastLoginIp;
	private String loginIp;
	private int loginCount;
	private int report;// 是否允许举报,0为允许，-1为不允许
	@Lock
	@Column(precision = 12, scale = 2)
	private BigDecimal availableBalance;
	@Lock
	@Column(precision = 12, scale = 2)
	private BigDecimal freezeBlance;
	@Lock
	private int integral;
	@Lock
	private int gold;
	@OneToMany(mappedBy = "user")
	private List<Accessory> files = new ArrayList<Accessory>();
	@ManyToOne
	private User parent;
	@OneToMany(mappedBy = "parent")
	private List<User> childs = new ArrayList<User>();
	@Lock
	private int user_credit;
	@Transient
	private GrantedAuthority[] authorities = new GrantedAuthority[] {};
	private String qq_openid;
	private String sina_openid;
	@Column(columnDefinition = "LongText")
	private String store_quick_menu;// 用户快捷菜单，使用JSON
									// [{"menu_name":"",,"menu_url","xxxx.htm"},{"menu_name":"",,"menu_url","xxxx.htm"}]
	@OneToOne(mappedBy = "user")
	private UserConfig config;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Album> albums = new ArrayList<Album>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Accessory> accs = new ArrayList<Accessory>();

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Accessory> getAccs() {
		return accs;
	}

	public void setAccs(List<Accessory> accs) {
		this.accs = accs;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public String getSina_openid() {
		return sina_openid;
	}

	public void setSina_openid(String sina_openid) {
		this.sina_openid = sina_openid;
	}

	public String getQq_openid() {
		return qq_openid;
	}

	public void setQq_openid(String qq_openid) {
		this.qq_openid = qq_openid;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public GrantedAuthority[] get_all_Authorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(
				roles.size());
		for (Role role : roles) {
			grantedAuthorities
					.add(new GrantedAuthorityImpl(role.getRoleCode()));
		}
		return grantedAuthorities.toArray(new GrantedAuthority[roles.size()]);
	}

	public GrantedAuthority[] get_common_Authorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(
				roles.size());
		for (Role role : roles) {
			if (!role.getType().equals("ADMIN"))
				grantedAuthorities.add(new GrantedAuthorityImpl(role
						.getRoleCode()));
		}
		return grantedAuthorities
				.toArray(new GrantedAuthority[grantedAuthorities.size()]);
	}

	public String getAuthoritiesString() {
		List<String> authorities = new ArrayList<String>();
		for (GrantedAuthority authority : this.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}
		return StringUtils.join(authorities.toArray(), ",");
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public Map<String, List<Res>> getRoleResources() {
		if (this.roleResources == null) {
			this.roleResources = new HashMap<String, List<Res>>();
			for (Role role : this.roles) {
				String roleCode = role.getRoleCode();
				List<Res> ress = role.getReses();
				for (Res res : ress) {
					String key = roleCode + "_" + res.getType();
					if (!this.roleResources.containsKey(key)) {
						this.roleResources.put(key, new ArrayList<Res>());
					}
					this.roleResources.get(key).add(res);
				}
			}

		}
		return this.roleResources;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setRoleResources(Map<String, List<Res>> roleResources) {
		this.roleResources = roleResources;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qq) {
		QQ = qq;
	}

	public String getMSN() {
		return MSN;
	}

	public void setMSN(String msn) {
		MSN = msn;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Accessory getPhoto() {
		return photo;
	}

	public void setPhoto(Accessory photo) {
		this.photo = photo;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getFreezeBlance() {
		return freezeBlance;
	}

	public void setFreezeBlance(BigDecimal freezeBlance) {
		this.freezeBlance = freezeBlance;
	}

	public List<Accessory> getFiles() {
		return files;
	}

	public void setFiles(List<Accessory> files) {
		this.files = files;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public String getWW() {
		return WW;
	}

	public void setWW(String ww) {
		WW = ww;
	}

	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	public int getUser_credit() {
		return user_credit;
	}

	public void setUser_credit(int user_credit) {
		this.user_credit = user_credit;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

	public List<User> getChilds() {
		return childs;
	}

	public void setChilds(List<User> childs) {
		this.childs = childs;
	}

	public User getParent() {
		return parent;
	}

	public String getStore_quick_menu() {
		return store_quick_menu;
	}

	public void setStore_quick_menu(String store_quick_menu) {
		this.store_quick_menu = store_quick_menu;
	}

	public UserConfig getConfig() {
		return config;
	}

	public void setConfig(UserConfig config) {
		this.config = config;
	}
}
