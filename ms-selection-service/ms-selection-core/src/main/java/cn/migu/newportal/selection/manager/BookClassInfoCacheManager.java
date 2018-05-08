package cn.migu.newportal.selection.manager;

import org.apache.commons.lang.StringUtils;

import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.service.NewBookClassInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;

public class BookClassInfoCacheManager
{
    
    /** 单实例方法 */
    public static BookClassInfoCacheManager getInstance()
    {
        return instance;
    }
    
    /** 单实例对象 */
    private static BookClassInfoCacheManager instance = new BookClassInfoCacheManager();
    
    /**
     * 根据图书标识查询分类信息
     * 
     * @param bookId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public cn.migu.newportal.cache.bean.book.BookClassInfo getCategory(String bookId)
    {
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        if (null == bookItem)
        {
            return null;
        }
        
        String classId = bookItem.getBookClassId();
        if (StringUtils.isEmpty(classId))
        {
            return null;
        }
        
        return this.getBookClassInfo(classId);
    }
    
    /**
     * 查询图书类型信息,如果缓存中不存在,则调用接口查询，并更新缓存
     * 
     * @param bookClassID
     * @return BookClassInfo
     * @see [类、类#方法、类#成员]
     */
    public cn.migu.newportal.cache.bean.book.BookClassInfo getBookClassInfo(String bookClassID)
    {
        return NewBookClassInfoCacheService.getInstance().getBookClassInfo(bookClassID);
    }
}
