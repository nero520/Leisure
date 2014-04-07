package com.leisure.core.security.interceptor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ConfigAttributeEditor;
import org.springframework.security.intercept.web.FilterInvocation;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.util.AntUrlPathMatcher;
import org.springframework.security.util.RegexUrlPathMatcher;
import org.springframework.security.util.UrlMatcher;

import com.leisure.core.constant.Globals;
import com.leisure.core.tools.CommUtil;

/**
 * 系统的核心管理，使用SpringSecurity完成角色和权限来拦截
 * @author xiaoxiong
 */
public class SecureResourceFilterInvocationDefinitionSource implements
		FilterInvocationDefinitionSource, InitializingBean {

	private UrlMatcher urlMatcher;

	private boolean useAntPath = true;

	private boolean lowercaseComparisons = true;

	/**
	 * @param useAntPath
	 *            the useAntPath to set
	 */
	public void setUseAntPath(boolean useAntPath) {
		this.useAntPath = useAntPath;
	}

	/**
	 * @param lowercaseComparisons
	 */
	public void setLowercaseComparisons(boolean lowercaseComparisons) {
		this.lowercaseComparisons = lowercaseComparisons;
	}

	public void afterPropertiesSet() throws Exception {

		// default url matcher will be RegexUrlPathMatcher
		this.urlMatcher = new RegexUrlPathMatcher();

		if (useAntPath) { // change the implementation if required
			this.urlMatcher = new AntUrlPathMatcher();
		}

		// Only change from the defaults if the attribute has been set
		if ("true".equals(lowercaseComparisons)) {
			if (!this.useAntPath) {
				((RegexUrlPathMatcher) this.urlMatcher)
						.setRequiresLowerCaseUrl(true);
			}
		} else if ("false".equals(lowercaseComparisons)) {
			if (this.useAntPath) {
				((AntUrlPathMatcher) this.urlMatcher)
						.setRequiresLowerCaseUrl(false);
			}
		}

	}

	public ConfigAttributeDefinition getAttributes(Object filter)
			throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) filter;
		String requestURI = filterInvocation.getRequestUrl();
		boolean verify = true;
		// 这里绑定域名
		String serverName = filterInvocation.getHttpRequest().getServerName();
		if (CommUtil.isIp(serverName)) {
			verify = true;
		} else {
			if (!serverName.trim().equals("localhost")) {
				if (!(serverName.indexOf(".") == serverName.lastIndexOf("."))) {
					serverName = serverName
							.substring(serverName.indexOf(".") + 1);
				}
				verify = CommUtil.cal_domain(serverName).equals(
						Globals.DEFAULT_BIND_DOMAIN_CODE);
			}
		}
		if (verify && requestURI.indexOf("login.htm") < 0) {
			Map<String, String> urlAuthorities = this
					.getUrlAuthorities(filterInvocation);
			String grantedAuthorities = null;

			for (Iterator<Map.Entry<String, String>> iter = urlAuthorities
					.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, String> entry = iter.next();
				String url = entry.getKey();
				if (!CommUtil.null2String(url).equals("")
						&& urlMatcher.pathMatchesUrl(url, requestURI)) {
					grantedAuthorities = entry.getValue();
					break;
				}

			}
			if (grantedAuthorities != null) {
				ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();
				configAttrEditor.setAsText(grantedAuthorities);
				return (ConfigAttributeDefinition) configAttrEditor.getValue();
			}
		} else {
			if (requestURI.indexOf("login.htm") < 0) {
				ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();
				configAttrEditor.setAsText("domain_error");
				filterInvocation.getHttpRequest().getSession()
						.setAttribute("domain_error", true);
				return (ConfigAttributeDefinition) configAttrEditor.getValue();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.intercept.ObjectDefinitionSource#
	 * getConfigAttributeDefinitions()
	 */
	@SuppressWarnings("unchecked")
	public Collection getConfigAttributeDefinitions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.intercept.ObjectDefinitionSource#supports
	 * (java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return true;
	}

	/**
	 * 
	 * @param filterInvocation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getUrlAuthorities(
			FilterInvocation filterInvocation) {
		ServletContext servletContext = filterInvocation.getHttpRequest()
				.getSession().getServletContext();
		return (Map<String, String>) servletContext
				.getAttribute("urlAuthorities");
	}

}
