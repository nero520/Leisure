package com.leisure.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.leisure.core.query.support.IPageList;
import com.leisure.core.query.support.IQueryObject;
import com.leisure.domain.SysLog;

public interface ISysLogService {
	/**
	 * @see 保存日志
	 * 
	 * @param instance
	 * @return true as success,false as wrong
	 */
	boolean save(SysLog instance);

	/**
	 * @see 获取日志
	 * 
	 * @param id
	 * @return
	 */
	SysLog getObjById(Long id);

	/**
	 * @see 删除日志
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(Long id);

	/**
	 * @see 批量删除日志
	 * 
	 * @param ids
	 * @return
	 */
	boolean batchDelete(List<Serializable> ids);

	/**
	 * @see 查询出日志列表
	 * 
	 * @param properties
	 * @return
	 */
	IPageList list(IQueryObject properties);

	/**
	 * @param id
	 * @param dir
	 */
	boolean update(SysLog instance);

	/**
	 * 
	 * @param query
	 * @param params
	 * @param begin
	 * @param max
	 * @return
	 */
	List<SysLog> query(String query, Map params, int begin, int max);
}
