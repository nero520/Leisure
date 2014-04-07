package com.leisure.core.query;

import java.util.Map;

import com.leisure.core.dao.IGenericDAO;
import com.leisure.core.query.support.IQuery;
import com.leisure.core.query.support.IQueryObject;

public class GenericPageList extends PageList {
	
	private static final long serialVersionUID = 6730593239674387757L;

	protected String scope;

	protected Class cls;
	public GenericPageList(Class cls,IQueryObject queryObject,IGenericDAO dao)
	{
		this(cls,queryObject.getQuery(),queryObject.getParameters(),dao);
	}
	public GenericPageList(Class cls, String scope, Map paras,
			IGenericDAO dao) {
		this.cls = cls;
		this.scope = scope;
		IQuery query = new GenericQuery(dao);
		query.setParaValues(paras);
		this.setQuery(query);
	}

	/**
	 * 查询
	 * 
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            一页的查询个数
	 */
	public void doList(int currentPage, int pageSize) {
		String totalSql = "select COUNT(obj) from " + cls.getName() + " obj where "
				+ scope;
		super.doList(pageSize, currentPage, totalSql, scope);
	}
}
