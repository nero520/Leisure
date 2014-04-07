package com.leisure.core.loader;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.leisure.core.security.SecurityManager;

/**
 * 系统初始化资源文件
 * 
 * @author xiaoxiong
 */
public class ServletContextLoaderListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext = servletContextEvent.getServletContext();
		SecurityManager securityManager = this
				.getSecurityManager(servletContext);
		Map<String, String> urlAuthorities = securityManager
				.loadUrlAuthorities();
		servletContext.setAttribute("urlAuthorities", urlAuthorities);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		servletContextEvent.getServletContext().removeAttribute(
				"urlAuthorities");
	}

	protected SecurityManager getSecurityManager(ServletContext servletContext) {
		return (SecurityManager) WebApplicationContextUtils
				.getWebApplicationContext(servletContext).getBean(
						"securityManager");
	}
}
