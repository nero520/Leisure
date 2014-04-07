package com.leisure.core.security.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.leisure.core.security.SecurityManager;
import com.leisure.domain.Res;
import com.leisure.domain.Role;
import com.leisure.domain.User;
import com.leisure.service.IResService;
import com.leisure.service.IUserService;

/**
 *  用户登录管理器，用户名、密码验证完成后进入该验证器，该验证器用来获取用户权限获取
 *  及外部系统同步登录
 * @author xiaoxiong
 */
public class SecurityManagerSupport implements UserDetailsService,
		SecurityManager {
	@Autowired
	private IUserService userService;
	@Autowired
	private IResService resService;

	public UserDetails loadUserByUsername(String data)
			throws UsernameNotFoundException, DataAccessException {
		String[] list = data.split(",");
		String userName = list[0];
		String loginRole = "user";
		if (list.length == 2) {
			loginRole = list[1];
		}
		Map params = new HashMap();
		params.put("userName", userName);
		List<User> users = this.userService.query(
				"select obj from User obj where obj.userName =:userName ",
				params, -1, -1);
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("User " + userName
					+ " has no GrantedAuthority");
		}
		User user = users.get(0);
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (!user.getRoles().isEmpty() && user.getRoles() != null) {
			Iterator<Role> roleIterator = user.getRoles().iterator();
			while (roleIterator.hasNext()) {
				Role role = roleIterator.next();
				if (loginRole.equalsIgnoreCase("ADMIN")) {
					GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(
							role.getRoleCode().toUpperCase());
					authorities.add(grantedAuthority);
				} else {
					if (!role.getType().equals("ADMIN")) {
						GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(
								role.getRoleCode().toUpperCase());
						authorities.add(grantedAuthority);
					}
				}
			}
		}
		GrantedAuthority[] auths = new GrantedAuthority[authorities.size()];
		user.setAuthorities(authorities.toArray(auths));
		return user;

	}

	public Map<String, String> loadUrlAuthorities() {
		Map<String, String> urlAuthorities = new HashMap<String, String>();
		Map params = new HashMap();
		params.put("type", "URL");
		List<Res> urlResources = this.resService.query(
				"select obj from Res obj where obj.type = :type", params, -1,
				-1);
		for (Res res : urlResources) {
			urlAuthorities.put(res.getValue(), res.getRoleAuthorities());
		}
		return urlAuthorities;
	}

}
