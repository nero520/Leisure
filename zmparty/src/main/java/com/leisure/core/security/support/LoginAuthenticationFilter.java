package com.leisure.core.security.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.security.util.TextUtils;

import com.leisure.core.tools.CommUtil;
import com.leisure.service.ISysConfigService;

/**
 * 重写SpringSecurity登录验证过滤器,验证器重新封装封装用户登录信息， 可以任意控制用户与外部程序的接口
 * @author xiaoxiong
 * 
 */
public class LoginAuthenticationFilter extends AuthenticationProcessingFilter {
	@Autowired
	private ISysConfigService configService;

	public Authentication attemptAuthentication(HttpServletRequest request)
			throws AuthenticationException {
		String login_role = request.getParameter("login_role");
		if (login_role == null || login_role.equals(""))
			login_role = "user";
		HttpSession session = request.getSession();
		session.setAttribute("login_role", login_role);
		session.setAttribute("ajax_login",
				CommUtil.null2Boolean(request.getParameter("ajax_login")));
		boolean flag = true;
		if (session.getAttribute("verify_code") != null) {
			String code = request.getParameter("code") != null ? request
					.getParameter("code").toUpperCase() : "";
			if (!session.getAttribute("verify_code").equals(code)) {
				flag = false;
			}
		}
		if (!flag) {
			String username = obtainUsername(request);
			String password = "";
			username = username.trim();
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
			if ((session != null) || (getAllowSessionCreation())) {
				request.getSession().setAttribute(
						"SPRING_SECURITY_LAST_USERNAME",
						TextUtils.escapeEntities(username));
			}
			setDetails(request, authRequest);
			return getAuthenticationManager().authenticate(authRequest);
		} else {
			String username = "";
			if (CommUtil.null2Boolean(request.getParameter("encode"))) {
				username = CommUtil.decode(obtainUsername(request)) + ","
						+ login_role;
			} else
				username = obtainUsername(request) + "," + login_role;
			String password = obtainPassword(request);
			username = username.trim();
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
			if ((session != null) || (getAllowSessionCreation())) {
				request.getSession().setAttribute(
						"SPRING_SECURITY_LAST_USERNAME",
						TextUtils.escapeEntities(username));
			}
			setDetails(request, authRequest);
			return getAuthenticationManager().authenticate(authRequest);
		}
	}

	protected void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException {
		request.getSession(false).removeAttribute("verify_code");
		super.onSuccessfulAuthentication(request, response, authResult);
	}

	protected void onUnsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException {
		String uri = request.getRequestURI();
		super.onUnsuccessfulAuthentication(request, response, failed);
	}
}
