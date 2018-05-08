package cn.migu.newportal.selection.bean;

import java.util.List;

public class PersonalRecommendBean {
	
	private int prtype; // 智能推荐应用场景标识
	
	private String msisdn; // 推荐内容的用户标识
	
	private int page_id; // 当前页码
	
	private int page_booknum; // 每页展示条数
	
	private int page_num; // 总页数
	
	private int book_num; // 图书数
	
	private String bid; // 源图书ID
	
	private String error_code; // 错误码
	
	private List<Books> books; // 私人推荐图书列表

	public int getPrtype() {
		return prtype;
	}

	public void setPrtype(int prtype) {
		this.prtype = prtype;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getPage_id() {
		return page_id;
	}

	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}

	public int getPage_booknum() {
		return page_booknum;
	}

	public void setPage_booknum(int page_booknum) {
		this.page_booknum = page_booknum;
	}

	public int getPage_num() {
		return page_num;
	}

	public void setPage_num(int page_num) {
		this.page_num = page_num;
	}

	public int getBook_num() {
		return book_num;
	}

	public void setBook_num(int book_num) {
		this.book_num = book_num;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public List<Books> getBooks() {
		return books;
	}

	public void setBooks(List<Books> books) {
		this.books = books;
	}
}
