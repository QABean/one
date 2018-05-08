package cn.migu.newportal.selection.bean;

import cn.migu.newportal.util.bean.page.PaginationInfo;

public class PagedContent<T>
{
    /**当前分页的内容*/
    private T content;
    /**每页显示数*/
    private int pageSize;
    /**本页起始序号 从1开始*/
    private int startIndex;
    
    /**当前页面*/
    private int pageNo;
    /**总页数*/
    private int totalPage;
    
    /**总记录数*/
    private int totalSize;
    
    public PagedContent(PaginationInfo pgInf)
    {
        // 默认使用当前页面
        this.init(pgInf);
    }
    
    public PagedContent(PaginationInfo pgInf, String pagedUrl)
    {
        this.init(pgInf);
    }
    
    public PagedContent(int pageSize, int pageNo, int totalSize)
    {
        PaginationInfo pgInf = new PaginationInfo(pageSize, pageNo, totalSize);
        // 默认使用当前页面
        this.init(pgInf);
    }
    
    public PagedContent(int pageSize, int pageNo, int totalSize, String pagedUrl)
    {
        PaginationInfo pgInf = new PaginationInfo(pageSize, pageNo, totalSize);
        
        this.init(pgInf);
    }
    
    public PagedContent()
    {
    }
    
    
    private void init(PaginationInfo pgInf)
    {
        this.pageNo = pgInf.getPageNo();
        this.totalPage = pgInf.getTotalPage();
        this.totalSize = pgInf.getTotalSize();
        this.pageSize = pgInf.getReqPageSize();
        this.startIndex = pgInf.getStart() + 1;
    }

    
    /**
     * 当前分页的内容
     */
    public T getContent()
    {
        return content;
    }
    
    /**
     * 当前页面
     * @return [参数说明]
     * 
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getPageNo()
    {
        return pageNo;
    }
    
    /**
     * 总页数
     * @return [参数说明]
     * 
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getTotalPage()
    {
        return totalPage;
    }
    
    /**
     * 总记录数
     * @return [参数说明]
     * 
     * @return int [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getTotalSize()
    {
        return totalSize;
    }

    /**
     * @param 对content进行赋值
     */
    public void setContent(T content)
    {
        this.content = content;
    }
  

    /**
     * @return 返回 pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * @return 返回 startIndex
     */
    public int getStartIndex()
    {
        return startIndex;
    }
    
}
