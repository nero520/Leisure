package com.leisure.core.security.support;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.leisure.core.constant.Globals;
import com.leisure.core.tools.CommUtil;
import com.leisure.domain.User;

/**
 *  SpringSecurity用户获取工具类，该类的静态方法可以直接获取已经登录的用户信息
 * @author xiaoxiong
 * 
 */
public class SecurityUserHolder {

	/**
	 * Returns the current user
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() instanceof User) {

			return (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		} else {
			User user = null;
			if (RequestContextHolder.getRequestAttributes() != null) {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest();
				user = (request.getSession().getAttribute("user") != null ? (User) request
						.getSession().getAttribute("user") : null);
				if (Globals.SSO_SIGN) {
					Cookie[] cookies = request.getCookies();
					String id = "";
					if (cookies != null) {
						for (Cookie cookie : cookies) {
							if (cookie.getName()
									.equals(Globals.DEFAULT_TABLE_SUFFIX+"user_session")) {
								id = CommUtil.null2String(cookie.getValue());
							}
						}
					}
					if (id.equals("")) {
						user = null;
					}
				}
			}
			return user;
		}

	}
}
