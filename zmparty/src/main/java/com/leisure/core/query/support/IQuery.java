package com.leisure.core.query.support;

import java.util.List;
import java.util.Map;
/**
 * 分页查询算法接口
 * 
 * @author xiaoxiong
 *
 */
public interface IQuery {
	/**
	 * 根据查询条件返回记录总数
	 * @param conditing
	 * @return 查询记录结果总数
	 */
	int getRows(String conditing);

	/**
	 * 根据查询条件返回符合条件的结果数
	 * @param condition
	 * @return 根据条件获得查询结果集
	 */
	List getResult(String condition);

	/**
	 * 设置有效结果记录的开始位置
	 * @param begin
	 */
	void setFirstResult(int begin);

	/**
	 * 最大返回记录数
	 * @param max
	 */
	void setMaxResults(int max);

	/**
	 * 设置查询参数
	 * @param params
	 */
	void setParaValues(Map params);

	/**
	 * 根据查询条件，记录开始位置及最大记录数返回有效查询结果
	 * @param conditing
	 * @param begin
	 * @param max
	 * @return 指定范围内的查询结果记录集
	 */
	List getResult(String conditing, int begin, int max);
}
