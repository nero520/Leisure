package com.leisure.lucene;

import java.util.ArrayList;
import java.util.List;

public class LuceneResult {
	private List<LuceneVo> vo_list = new ArrayList<LuceneVo>();
	private int pages;
	private int rows;
	private int currentPage;
	private int pageSize;

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<LuceneVo> getVo_list() {
		return vo_list;
	}

	public void setVo_list(List<LuceneVo> vo_list) {
		this.vo_list = vo_list;
	}

}
