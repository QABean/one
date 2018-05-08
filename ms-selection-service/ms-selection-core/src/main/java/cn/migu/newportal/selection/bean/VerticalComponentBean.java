package cn.migu.newportal.selection.bean;

import java.util.List;

public class VerticalComponentBean
{
    private int prtype;
    
    private String msisdn;
    
    private int page_id;
    
    private int page_booknum;
    
    private int page_num;
    
    private int book_num;
    
    private String error_code;
    
    private List<VerticalComponentDataBean> books;
    
    public int getPrtype()
    {
        return prtype;
    }
    
    public void setPrtype(int prtype)
    {
        this.prtype = prtype;
    }
    
    public String getMsisdn()
    {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }
    
    public int getPage_id()
    {
        return page_id;
    }
    
    public void setPage_id(int page_id)
    {
        this.page_id = page_id;
    }
    
    public int getPage_booknum()
    {
        return page_booknum;
    }
    
    public void setPage_booknum(int page_booknum)
    {
        this.page_booknum = page_booknum;
    }
    
    public int getPage_num()
    {
        return page_num;
    }
    
    public void setPage_num(int page_num)
    {
        this.page_num = page_num;
    }
    
    public int getBook_num()
    {
        return book_num;
    }
    
    public void setBook_num(int book_num)
    {
        this.book_num = book_num;
    }
    
    public String getError_code()
    {
        return error_code;
    }
    
    public void setError_code(String error_code)
    {
        this.error_code = error_code;
    }
    
    public List<VerticalComponentDataBean> getBooks()
    {
        return books;
    }
    
    public void setBooks(List<VerticalComponentDataBean> books)
    {
        this.books = books;
    }
    
}
