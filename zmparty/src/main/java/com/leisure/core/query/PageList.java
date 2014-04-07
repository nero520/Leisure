package com.leisure.core.query;

import java.util.List;
import java.util.Map;

import com.leisure.core.query.support.IPageList;
import com.leisure.core.query.support.IQuery;

/**
 * 实现通过调用IQuery实现分页处理，其它特殊形式的分页查询需求只需要继承该类即可
 * 
 * @author xiaoxiong
 * 
 */
public class PageList implements IPageList {
	private int rowCount;// 记录数

	private int pages;// 总页数

	private int currentPage;// 实际页数

	private int pageSize;

	private List result;// 查询结果集

	private IQuery query;// 查询器

	public PageList() {

	}

	/**
	 * 根据查询器q构造一个分页处理对象
	 * 
	 * @param q
	 */
	public PageList(IQuery q) {
		this.query = q;
	}

	/**
	 * 设置查询器
	 */
	public void setQuery(IQuery q) {
		query = q;
	}

	/**
	 * 返回查询结果集，只有在执行doList方法后才能取得正确的查询结果
	 */
	public List getResult() {
		return result;
	}

	/**
	 * 根据每页记录数，页码，统计sql及实际查询sql执行查询操作
	 */
	public void doList(int pageSize, int pageNo, String totalSQL,
			String queryHQL) {
		doList(pageSize,pageNo,totalSQL,queryHQL,null);
	}

	/**
	 * 根据每页记录数，页码，统计sql及实际查询sql及参数执行查询操作
	 */
	public void doList(int pageSize, int pageNo, String totalSQL,
			String queryHQL, Map params) {
		List rs = null;
		this.pageSize=pageSize;
		if(params!=null)query.setParaValues(params);
		int total = query.getRows(totalSQL);
		if (total > 0) {
			this.rowCount = total;
			this.pages = (this.rowCount + pageSize - 1) / pageSize; // 记算总页数
			int intPageNo = (pageNo > this.pages ? this.pages : pageNo);
			if (intPageNo < 1)
				intPageNo = 1;
			this.currentPage = intPageNo;
			if (pageSize > 0) {
				query.setFirstResult((intPageNo - 1) * pageSize);
				query.setMaxResults(pageSize);
			}
			rs = query.getResult(queryHQL);
		}
		result = rs;
	}

	/**
	 * 返回总页数
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * 返回总记录数
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * 返回当前页
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 返回每页大小
	 */
	public int getPageSize() {
		return pageSize;
	}

	public int getNextPage()
	{
		int p=this.currentPage+1;
		if(p>this.pages)p=this.pages;
		return p;
	}
	public int getPreviousPage()
	{
		int p=this.currentPage-1;
		if(p<1)p=1;
		return p;
	}
}
