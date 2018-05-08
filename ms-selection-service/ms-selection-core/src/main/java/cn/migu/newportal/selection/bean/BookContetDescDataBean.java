package cn.migu.newportal.selection.bean;

import java.util.Map;

public class BookContetDescDataBean
{
    private String bookRecommend;
    
    private String bookDesc;
    
    private String isShowLine;
    
    private Map<String,String> ctag;

    public String getBookRecommend()
    {
        return bookRecommend;
    }

    public void setBookRecommend(String bookRecommend)
    {
        this.bookRecommend = bookRecommend;
    }

    public String getBookDesc()
    {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc)
    {
        this.bookDesc = bookDesc;
    }

    public String getIsShowLine()
    {
        return isShowLine;
    }

    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }

    public Map<String, String> getCtag()
    {
        return ctag;
    }

    public void setCtag(Map<String, String> ctag)
    {
        this.ctag = ctag;
    }
}
