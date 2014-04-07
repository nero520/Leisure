package com.leisure.service;

import com.leisure.domain.SysConfig;

public interface ISysConfigService {
	/**
	 * 
	 * @param shopConfig
	 * @return
	 */
	boolean save(SysConfig shopConfig);

	/**
	 * 
	 * @param shopConfig
	 * @return
	 */
	boolean delete(SysConfig shopConfig);

	/**
	 * 
	 * @param shopConfig
	 * @return
	 */
	boolean update(SysConfig shopConfig);

	/**
	 * 
	 * @return
	 */
	SysConfig getSysConfig();
}
