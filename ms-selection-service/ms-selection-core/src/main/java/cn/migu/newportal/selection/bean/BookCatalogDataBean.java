package cn.migu.newportal.selection.bean;

import java.util.Map;

public class BookCatalogDataBean
{
    private String bookStatus;
    
    private String chapterName;
    
    private String updateTime;
    
    private String contentType;
    
    private String bookId;
    
    private String bookName;
    
    private String bookCover;
    
    private String authorName;
    
    private String chargeMode;
    
    private String isShowLine;
    
    private Map<String,String> ctag;

    public String getBookStatus()
    {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus)
    {
        this.bookStatus = bookStatus;
    }

    public String getChapterName()
    {
        return chapterName;
    }

    public void setChapterName(String chapterName)
    {
        this.chapterName = chapterName;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getBookId()
    {
        return bookId;
    }

    public void setBookId(String bookId)
    {
        this.bookId = bookId;
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public String getBookCover()
    {
        return bookCover;
    }

    public void setBookCover(String bookCover)
    {
        this.bookCover = bookCover;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getChargeMode()
    {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode)
    {
        this.chargeMode = chargeMode;
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
