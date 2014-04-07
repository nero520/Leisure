package com.leisure.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.leisure.core.constant.Globals;
import com.leisure.core.tools.CommUtil;

/**
 * SpringSeurity验证切入点判断是否通过
 * @author xiaoxiong
 */
@Component
public class LoginUrlEntryPoint implements AuthenticationEntryPoint {


	public void commence(ServletRequest req, ServletResponse res,
			AuthenticationException authException) throws IOException,
			ServletException {
		String targetUrl = null;
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getRequestURI();
		if (request.getQueryString() != null
				&& !request.getQueryString().equals("")) {
			url = url + "?" + request.getQueryString();
		}
		request.getSession(false).setAttribute("refererUrl", url);
	    String refererUrl = request.getHeader("Referer");
	    targetUrl = refererUrl;
		if (url.indexOf("/admin/") >= 0) {
			targetUrl = request.getContextPath() + "/admin/login.htm";
		} else {
			targetUrl = request.getContextPath() + "/user/login.htm";
			if (CommUtil.null2String(
					request.getSession().getAttribute(Globals.DEFAULT_TABLE_SUFFIX+"view_type"))
					.equals("weixin")) {
				targetUrl = request.getContextPath() + "/weixin/login.htm";
			}
		}
		response.sendRedirect(targetUrl);
	}
}
