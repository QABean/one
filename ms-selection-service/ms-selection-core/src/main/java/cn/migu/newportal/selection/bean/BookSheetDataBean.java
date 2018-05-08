package cn.migu.newportal.selection.bean;

import java.util.List;
import java.util.Map;

public class BookSheetDataBean
{

	private String sheetTitle;
    
    private String isShowLine;
    
    private String totalSheet;
    
    private String sheetCover;
    
    private String sheetName;
    
    private String sheetDesc;
    
    private String sheetAuthor;
    
    private String sheetNum;
    
    private String sheetCollectNum;
    
    private Map<String, String> ctag;
    
    private String moreLinkDesc;
    
    private String moreLink;
    
	private String sheetUrl;
	
    private List<Books> books;
    
    public List<Books> getBooks() {
		return books;
	}

	public void setBooks(List<Books> books) {
		this.books = books;
	}
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }
    
    public String getTotalSheet()
    {
        return totalSheet;
    }
    
    public void setTotalSheet(String totalSheet)
    {
        this.totalSheet = totalSheet;
    }
    
    public String getSheetCover()
    {
        return sheetCover;
    }
    
    public void setSheetCover(String sheetCover)
    {
        this.sheetCover = sheetCover;
    }
    
    public String getSheetName()
    {
        return sheetName;
    }
    
    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
    }
    
    public String getSheetDesc()
    {
        return sheetDesc;
    }
    
    public void setSheetDesc(String sheetDesc)
    {
        this.sheetDesc = sheetDesc;
    }
    
    public String getSheetAuthor()
    {
        return sheetAuthor;
    }
    
    public void setSheetAuthor(String sheetAuthor)
    {
        this.sheetAuthor = sheetAuthor;
    }
    
    public String getSheetTitle() {
		return sheetTitle;
	}

	public void setSheetTitle(String sheetTitle) {
		this.sheetTitle = sheetTitle;
	}

	public String getSheetCollectNum() {
		return sheetCollectNum;
	}

	public void setSheetCollectNum(String sheetCollectNum) {
		this.sheetCollectNum = sheetCollectNum;
	}

	public String getSheetUrl() {
		return sheetUrl;
	}

	public void setSheetUrl(String sheetUrl) {
		this.sheetUrl = sheetUrl;
	}

	public Map<String, String> getCtag()
    {
        return ctag;
    }
    
    public void setCtag(Map<String, String> ctag)
    {
        this.ctag = ctag;
    }
    
    public String getMoreLinkDesc()
    {
        return moreLinkDesc;
    }
    
    public void setMoreLinkDesc(String moreLinkDesc)
    {
        this.moreLinkDesc = moreLinkDesc;
    }
    
    public String getMoreLink()
    {
        return moreLink;
    }
    
    public void setMoreLink(String moreLink)
    {
        this.moreLink = moreLink;
    }
    
    public String getSheetNum()
    {
        return sheetNum;
    }
    
    public void setSheetNum(String sheetNum)
    {
        this.sheetNum = sheetNum;
    }
}
