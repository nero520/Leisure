package com.leisure.core.mv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.leisure.core.constant.Globals;
import com.leisure.core.security.support.SecurityUserHolder;
import com.leisure.core.tools.CommUtil;
import com.leisure.core.tools.HttpInclude;
import com.leisure.domain.SysConfig;
import com.leisure.domain.UserConfig;

/**
 *  视图管理，封装ModelAndView并进行系统扩展
 * @author xiaoxiong
 * 
 */
public class JModelAndView extends ModelAndView {
	/**
	 * 普通视图，根据velocity配置文件的路径直接加载视图
	 * 
	 * @param viewName
	 *            视图名称
	 */
	public JModelAndView(String viewName) {
		super.setViewName(viewName);
	}

	/**
	 * 
	 * @param viewName
	 *            用户自定义的视图，可以添加任意路径
	 * @param request
	 */
	public JModelAndView(String viewName, SysConfig config, UserConfig uconfig,
			HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath().equals("/") ? ""
				: request.getContextPath();
		String webPath = CommUtil.getURL(request);
		String port = request.getServerPort() == 80 ? "" : ":"
				+ CommUtil.null2Int(request.getServerPort());
		if (Globals.SSO_SIGN && config.isSecond_domain_open()
				&& !CommUtil.generic_domain(request).equals("localhost")) {
			webPath = "http://www." + CommUtil.generic_domain(request) + port
					+ contextPath;
		}
		super.setViewName(viewName);
		super.addObject("domainPath", CommUtil.generic_domain(request));
		if (config.getImageWebServer() != null
				&& !config.getImageWebServer().equals("")) {
			super.addObject("imageWebServer", config.getImageWebServer());
		} else {
			super.addObject("imageWebServer", webPath);
		}
		super.addObject("webPath", webPath);
		super.addObject("config", config);
		super.addObject("uconfig", uconfig);
		super.addObject("user", SecurityUserHolder.getCurrentUser());
		super.addObject("httpInclude", new HttpInclude(request, response));
		String query_url = "";
		if (request.getQueryString() != null
				&& !request.getQueryString().equals("")) {
			query_url = "?" + request.getQueryString();
		}
		super.addObject("current_url", request.getRequestURI() + query_url);
		boolean second_domain_view = false;
		String serverName = request.getServerName().toLowerCase();
		if (serverName.indexOf("www.") < 0 && serverName.indexOf(".") >= 0
				&& serverName.indexOf(".") != serverName.lastIndexOf(".")
				&& config.isSecond_domain_open()) {
			String secondDomain = serverName.substring(0,
					serverName.indexOf("."));
			second_domain_view = true;// 使用二级域名访问，相关js url需要处理，避免跨域
			super.addObject("secondDomain", secondDomain);
		}
		super.addObject("second_domain_view", second_domain_view);
	}

	/**
	 * 按指定路径加载视图，如不指定则系统默认路径加载
	 * 
	 * @param viewName
	 *            视图名称
	 * @param config
	 *            商城配置
	 * @param userPath
	 *            自定义路径，和type配合使用
	 * @param type
	 *            视图类型 0为后台，1为前台 大于1为自定义路径
	 */
	public JModelAndView(String viewName, SysConfig config, UserConfig uconfig,
			int type, HttpServletRequest request, HttpServletResponse response) {
		if (config.getSysLanguage() != null) {
			if (config.getSysLanguage().equals(Globals.DEFAULT_SYSTEM_LANGUAGE)) {
				if (type == 0) {
					super.setViewName(Globals.SYSTEM_MANAGE_PAGE_PATH
							+ viewName);
				}
				if (type == 1) {
					super.setViewName(Globals.SYSTEM_FORNT_PAGE_PATH + viewName);
				}
				if (type > 1) {
					super.setViewName(viewName);
				}

			} else {
				if (type == 0) {
					super.setViewName(Globals.DEFAULT_SYSTEM_PAGE_ROOT
							+ config.getSysLanguage() + "/admin/" + viewName);
				}
				if (type == 1) {
					super.setViewName(Globals.DEFAULT_SYSTEM_PAGE_ROOT
							+ config.getSysLanguage() + "/user/" + viewName);
				}
				if (type > 1) {
					super.setViewName(viewName);
				}
			}
		} else {
			super.setViewName(viewName);
		}
		super.addObject("CommUtil", new CommUtil());
		String contextPath = request.getContextPath().equals("/") ? ""
				: request.getContextPath();
		String webPath = CommUtil.getURL(request);
		String port = request.getServerPort() == 80 ? "" : ":"
				+ CommUtil.null2Int(request.getServerPort());
		if (Globals.SSO_SIGN && config.isSecond_domain_open()
				&& !CommUtil.generic_domain(request).equals("localhost")) {
			webPath = "http://www." + CommUtil.generic_domain(request) + port
					+ contextPath;
		}
		super.addObject("domainPath", CommUtil.generic_domain(request));
		super.addObject("webPath", webPath);
		if (config.getImageWebServer() != null
				&& !config.getImageWebServer().equals("")) {
			super.addObject("imageWebServer", config.getImageWebServer());
		} else {
			super.addObject("imageWebServer", webPath);
		}
		super.addObject("config", config);
		super.addObject("uconfig", uconfig);
		super.addObject("user", SecurityUserHolder.getCurrentUser());
		super.addObject("httpInclude", new HttpInclude(request, response));
		String query_url = "";
		if (request.getQueryString() != null
				&& !request.getQueryString().equals("")) {
			query_url = "?" + request.getQueryString();
		}
		super.addObject("current_url", request.getRequestURI() + query_url);
		boolean second_domain_view = false;
		String serverName = request.getServerName().toLowerCase();
		if (serverName.indexOf("www.") < 0 && serverName.indexOf(".") >= 0
				&& serverName.indexOf(".") != serverName.lastIndexOf(".")
				&& config.isSecond_domain_open()) {
			String secondDomain = serverName.substring(0,
					serverName.indexOf("."));
			second_domain_view = true;// 使用二级域名访问，相关js url需要处理，避免跨域
			super.addObject("secondDomain", secondDomain);
		}
		super.addObject("second_domain_view", second_domain_view);
	}
}
