package com.leisure.core.security;

import java.util.Map;

/**
 * 权限管理接口
 * @author xiaoxiong
 */
public interface SecurityManager {

	public Map<String, String> loadUrlAuthorities();

}
