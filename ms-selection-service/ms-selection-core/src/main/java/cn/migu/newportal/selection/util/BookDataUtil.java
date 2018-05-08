package cn.migu.newportal.selection.util;

import cn.migu.newportal.cache.bean.Category;
import cn.migu.newportal.cache.bean.CornerParam;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.corner.CornerBusiness;
import cn.migu.newportal.cache.cache.service.BookScoreDetailListCacheService;
import cn.migu.newportal.cache.cache.service.BookUVCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.util.UesParamKeyConstants;
import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.selection.manager.BookClassInfoCacheManager;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.userservice.GetAvgScoreAndTotalPersons.BookMarkInfoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookDataUtil
{
    /**
     * 单实例方法
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static BookDataUtil getInstance()
    {
        return instance;
    }
    
    /** 单实例对象 */
    private static BookDataUtil instance = new BookDataUtil();
    
    private static Logger logger = LogManager.getLogger(BookDataUtil.class);
    
    /**
     * 获取图书显示名称
     * 
     * @author hsq
     * @param bookid
     * @param showType
     * @return
     */
    public static String getShowName(String bookid, String showType)
    {
        // 从缓存中获取图书信息
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookid);
        String bookShowName = "";
        if (null == bookItem)
        {
            logger.error("BookDataUtil.getShowName get bookItem from PortalContentInfoCacheService is null;bookid = "+bookid);
            return bookShowName;
        }

        // 显示长推荐语
         if (SelectionConstants.SHOW_TYPE_LONG_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getRecommendReason();
        }
        // 显示短推荐语
        else if (SelectionConstants.SHOW_TYPE_SHORT_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getShortRecommendReason();
        }
        // 显示wap2.0推荐语
        else if (SelectionConstants.SHOW_TYPE_WAP2_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getLongRecommendReason();
        }
        
        // 默认显示图书名称
        if (StringTools.isEmpty(bookShowName))
        {
            bookShowName = bookItem.getBookName();
        }
        if(StringTools.isEmpty(bookShowName))
        {
            return " ";
        }
        return bookShowName;
    }

    /**
     * 获取图书显示名称
     *
     * @author hsq
     * @param bookItem
     * @param showType
     * @return
     */
    public static String getShowName(BookItem bookItem , String showType)
    {
        // 从缓存中获取图书信息
        //BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookid);
        String bookShowName = "";
        if (null == bookItem)
        {
            logger.error("BookDataUtil.getShowName get bookItem from PortalContentInfoCacheService is null;bookItem = "+bookItem);
            return bookShowName;
        }

        // 显示长推荐语
        if (SelectionConstants.SHOW_TYPE_LONG_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getRecommendReason();
        }
        // 显示短推荐语
        else if (SelectionConstants.SHOW_TYPE_SHORT_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getShortRecommendReason();
        }
        // 显示wap2.0推荐语
        else if (SelectionConstants.SHOW_TYPE_WAP2_RECOMMEND_WORDS.equals(showType))
        {
            bookShowName = bookItem.getLongRecommendReason();
        }

        // 默认显示图书名称
        if (StringTools.isEmpty(bookShowName))
        {
            bookShowName = bookItem.getBookName();
        }
        if(StringTools.isEmpty(bookShowName))
        {
            return " ";
        }
        return bookShowName;
    }
    
    /**
     * 图书点击量       
     * 
     *
     * @author zhangmm
     * @param bookId
     * @param largeSize
     * @return
     */
    public String getClickNumDesc(String bookId, String largeSize)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("BookDataUtil.getClickNumDesc  bookId=:" + bookId + ",largeSize=" + largeSize);
        }
        int larSize = ParamUtil.getLargeSize(largeSize, 1);
        // 图书阅读人数后端取数据开关, "0":关闭 ,"1":开启 ,默认开启
        String bookUVswitcher = UesServerServiceUtils.getUesParamConfig(UesParamKeyConstants.GET_BOOKUV_SWITCH, "1");
        String clickNum =
            (StringTools.isEq("1", bookUVswitcher)) ? BookUVCacheService.getInstance().getBookUV(bookId) : "0";
        return Util.isEmpty(clickNum) ? "0" : Long.parseLong(clickNum) * larSize + "";
    }
    
    
    /**
     * 获取角标
     * 
     * @param bookItem
     * @param showType
     * @return
     */
    public static String getConer(BookItem bookItem, String showType)
    {
        CornerParam cp = new CornerParam();
        cp.setBookId(bookItem.getBookId());
        cp.setBookItem(bookItem);
        cp.setCornerShowTypes(showType);

        return CornerBusiness.getInstance().getCorner(cp);
    }
    
    /**
     * 图书评分数
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getScore(String bookId)
    {
        BookMarkInfoResponse bookMarkInfoResponse =
            BookScoreDetailListCacheService.getInstance().getBookScoreDetailList(bookId);
        String avgScore = "";
        if (Util.isNotEmpty(bookMarkInfoResponse))
        {
            avgScore = bookMarkInfoResponse.getAvgScore();
        }
        return avgScore;
    }
    
    /**
     * 图书分类
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNodeName(String bookId,BookItem bookItem)
    {
        cn.migu.newportal.cache.bean.book.BookClassInfo categoryInfo = BookClassInfoCacheManager.getInstance().getCategory(bookId);
        Category category = null;
        if (null != categoryInfo)
        {
            category = Category.getInstance(categoryInfo.getBookClassId(), bookItem.getItemType());
        }
        if (category != null && category.getUniteNode() != null)
        {
            return category.getUniteNode().getName();
        }
        return null;
    }
    
}
