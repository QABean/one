package cn.migu.newportal.selection.manager;

import cn.migu.compositeservice.contservice.Common.RankInfo;
import cn.migu.compositeservice.contservice.GetLimitBooksWithPart.GetLimitBooksWithPartResponse;
import cn.migu.compositeservice.contservice.GetLimitBooksWithPart.GetLimitBooksWithPartResponse.EntryItem;
import cn.migu.compositeservice.contservice.GetPagedBooksWithPart.GetPagedBooksWithPartResponse;
import cn.migu.compositeservice.contservice.GetRank.GetRankResponse;
import cn.migu.newportal.cache.bean.bookContent.Book;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.selection.bean.LimitNodeItem;
import cn.migu.newportal.selection.bean.PagedContent;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.RankType;
import cn.migu.newportal.selection.util.UserUtils;
import cn.migu.newportal.util.bean.page.PaginTools;
import cn.migu.newportal.util.constants.BookContants;
import cn.migu.newportal.util.constants.DatasrcSourceScene;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import com.huawei.iread.server.constant.ResultCode;
import com.huawei.iread.server.constant.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager
{
    
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(BookManager.class.getName());
    /** 点击榜排行类型 */
    protected final static String CLICKRANKTYPE = "2";
    
    /** 上架时间排行 */
    protected final static String ONSHELFRANKTYPE = "1";
    
    /**
     * 进行包月专区下图书列表地域适配
     * 
     * @param nodeItem
     * @param cityId
     * @param provinceId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static LimitNodeItem filterCityAndProvinceBookIds(NodeItem nodeItem, String cityId, String provinceId)
    {
        LimitNodeItem books = new LimitNodeItem();
        // 如果市ID不为空.且不为全国
        if (StringTools.isNotEmpty(cityId) && !cityId.equals(Types.COUNTRY))
        {
            Map<String, List<String>> cityBookMap = nodeItem.getCityBooksMap();
            
            // 先获取该专区下有图书的市列表
            List<String> cityList = nodeItem.getCityList();
            // 如果该cityList中有指定的市Id,但是该市Id下没有对应的图书,调用接口查询一次并放入Map中
            if (Util.isNotEmpty(cityList)&&cityList.contains(cityId))
            {
                if (cityBookMap != null&&!cityBookMap.containsKey(cityId))
                {
                    GetLimitBooksWithPartResponse cityBookResponse = null;
                    try
                    {
                        cityBookResponse = ContentServiceEngine.getInstance()
                            .getLimitBooksWithPart(BookContants.BOOK_ON_NODE, nodeItem.getId(), null, cityId, null);
                    }
                    catch (PortalException e)
                    {
                        logger.error("filterCityAndProvinceBookIds id error,errorCode:{},e:{}", e.getErrorCode(), e);
                    }

                    if (cityBookResponse != null)
                    {
                        EntryItem cityBookMapEntryItem = cityBookResponse.getBookMapMap().get(cityId);
                        if (cityBookMapEntryItem != null)
                        {
                            // 向nodeItem添加该市图书信息
                            cityBookMap.put(cityId, cityBookMapEntryItem.getListList());

                            // 向nodeItem添加该市图书数量信息
                            Map<String, Integer> cityBookCountMap = nodeItem.getCityBooksCountMap();
                            cityBookCountMap.put(cityId, cityBookMapEntryItem.getCount());
                        }
                    }
                }
            }
            
            if (null != cityBookMap && cityBookMap.size() > 0)
            {
                // 获取该市下上架的书籍列表
                List<String> bookIDs = cityBookMap.get(cityId);
                if (null != bookIDs && 0 < bookIDs.size())
                {
                    String[] allBookIds = new String[bookIDs.size()];
                    books.setBookIds(bookIDs.toArray(allBookIds));
                    if (SystemConstants.NODE_QUERY_SWITCH)
                    {
                        if (nodeItem.getCityBooksCountMap() != null)
                        {
                            Integer totalCnt = nodeItem.getCityBooksCountMap().get(cityId);
                            books.setTotalCnt(totalCnt != null ? totalCnt : 0);
                        }
                    }
                    else
                    {
                        books.setTotalCnt(bookIDs.size());
                    }
                    books.setAdaptCityId(cityId);
                    return books;
                }
            }
        }
        
        Map<String, List<String>> provinceBooksMap = nodeItem.getProvinceBooksMap();
        
        if (null != provinceBooksMap && provinceBooksMap.size() > 0)
        {
            if (StringTools.isNotEmpty(provinceId) && !provinceId.equals(SystemConstants.CHINA_ID))
            {
                // 先获取该专区下有图书的省列表
                List<String> provinceList = nodeItem.getProvinceList();
                // 如果该专区下的省列表中包含该省Id,但是该省下没有对应的图书,则调用接口查询一次
                if (provinceList.contains(provinceId))
                {
                    if (!provinceBooksMap.containsKey(provinceId))
                    {
                        GetLimitBooksWithPartResponse provinceBookResponse = null;
                        try
                        {
                            try
                            {
                                provinceBookResponse = ContentServiceEngine.getInstance().getLimitBooksWithPart(
                                    BookContants.BOOK_ON_NODE, nodeItem.getId(), provinceId, null, null);
                            }
                            catch (PortalException e)
                            {
                                if (logger.isDebugEnabled())
                                {
                                    logger.error("filterCityAndProvinceBookIds is error,errorCode:{},e:{}", e.getErrorCode(),e);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            // 此处不处理，继续走下面流程
                        }
                        
                        if (provinceBookResponse != null)
                        {
                            EntryItem provinceBookMapEntryItem = provinceBookResponse.getBookMapMap().get(provinceId);
                            if (provinceBookMapEntryItem != null)
                            {
                                // 向nodeItem添加该市图书信息
                                provinceBooksMap.put(provinceId, provinceBookMapEntryItem.getListList());
                                
                                // 向nodeItem添加该市图书数量信息
                                Map<String, Integer> provinceBookCountMap = nodeItem.getProvinceBooksCountMap();
                                provinceBookCountMap.put(provinceId, provinceBookMapEntryItem.getCount());
                            }
                        }
                    }
                }
                
                List<String> bookIDs = provinceBooksMap.get(provinceId);
                
                if (null != bookIDs && 0 < bookIDs.size())
                {
                    String[] allBookIds = new String[bookIDs.size()];
                    books.setBookIds(bookIDs.toArray(allBookIds));
                    if (SystemConstants.NODE_QUERY_SWITCH)
                    {
                        if (nodeItem.getProvinceBooksCountMap() != null)
                        {
                            Integer cnt = nodeItem.getProvinceBooksCountMap().get(provinceId);
                            books.setTotalCnt(cnt == null ? 0 : cnt.intValue());
                        }
                    }
                    else
                    {
                        books.setTotalCnt(bookIDs.size());
                    }
                    books.setAdaptProvinceId(provinceId);
                    return books;
                }
            }
            
            List<String> chinaBooks = provinceBooksMap.get(SystemConstants.CHINA_ID);
            if (null != chinaBooks && chinaBooks.size() > 0)
            {
                String[] allBookIds = new String[chinaBooks.size()];
                
                books.setBookIds(chinaBooks.toArray(allBookIds));
                if (SystemConstants.NODE_QUERY_SWITCH)
                {
                    if (nodeItem.getProvinceBooksCountMap() != null)
                    {
                        Integer totalCnt = nodeItem.getProvinceBooksCountMap().get(SystemConstants.CHINA_ID);
                        books.setTotalCnt(totalCnt != null ? totalCnt : 0);
                    }
                }
                else
                {
                    books.setTotalCnt(chinaBooks.size());
                }
                return books;
            }
        }
        return books;
    }
    
    /**
     * 判断用户请求的数据是否溢出了缓存的数据。 如果溢出， 那么直接调用server的分页接口。
     * 
     *
     * @author
     * @param pageNo
     * @param pageSize
     * @param nodeId
     * @param cityId
     * @param provinceId
     * @return
     */
    public static String[] overflowProcess(int pageNo, int pageSize, String nodeId, String cityId, String provinceId)
    {
        GetPagedBooksWithPartResponse response = null;
        try
        {
            response = ContentServiceEngine.getInstance().getPagedBooksWithPart(pageNo,
                pageSize,
                BookContants.BOOK_ON_NODE,
                nodeId,
                provinceId,
                cityId);
        }
        catch (PortalException e)
        {
            // 非内容为空，记录错误日志
            if (!String.valueOf(ResultCode.ID_HAVE_NO_BOOK).equals(e.getExceptionCode()))
            {
                if(logger.isDebugEnabled()){
                    logger.error("get GetBookListWithPart is wrong, bookType = (1;2;3;5),nodeId = {}, errorCode = {},e:{}"
                        ,nodeId,e.getExceptionCode(), e);
                }
            }
        }
        if (null == response)
        {
            return null;
        }
        // 如果市ID不为空.且不为全国
        if (StringTools.isNotEmpty(cityId) && !cityId.equals(Types.COUNTRY))
        {
            Map<String, GetPagedBooksWithPartResponse.MSG_EntryItem> cityBookMap = response.getBookCityMapMap();
            
            if (null != cityBookMap && cityBookMap.size() > 0)
            {
                // 获取该市下上架的书籍列表
                List<String> bookIDs = cityBookMap.get(cityId).getListList();
                if (null != bookIDs && 0 < bookIDs.size())
                {
                    String[] allBookIds = new String[bookIDs.size()];
                    return bookIDs.toArray(allBookIds);
                }
            }
        }
        
        Map<String, GetPagedBooksWithPartResponse.MSG_EntryItem> provinceBooksMap = response.getBookProvinceMapMap();
        
        if (null != provinceBooksMap && provinceBooksMap.size() > 0)
        {
            if (StringTools.isNotEmpty(provinceId) && !provinceId.equals(SystemConstants.CHINA_ID))
            {
                List<String> bookIDs = provinceBooksMap.get(provinceId).getListList();
                
                if (null != bookIDs && 0 < bookIDs.size())
                {
                    String[] allBookIds = new String[bookIDs.size()];
                    return bookIDs.toArray(allBookIds);
                }
            }
            
            List<String> chinaBooks = provinceBooksMap.get(SystemConstants.CHINA_ID).getListList();
            if (null != chinaBooks && chinaBooks.size() > 0)
            {
                String[] allBookIds = new String[chinaBooks.size()];
                
                return chinaBooks.toArray(allBookIds);
            }
        }
        
        return null;
    }
    
    /**
     * 获取专区下指定排序规则的分页图书
     *
     * @author 
     * @param rankType
     * @param nid
     * @param pageNo
     * @param showPage
     * @param showType
     * @return
     * @throws PortalException
     */
    public static PagedContent<List<Book>> getPagedContent(String rankType, String nid, int pageNo,
        int showPage,String showType)
            throws PortalException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("BookManager getPagedContent ,rankType = {},nid = {},pageNo = {},showPage = {},showType = {}",
                rankType,
                nid,
                pageNo,
                showPage,
                showType);
        }
        // 省ID
        String provinceId = UserUtils.getProvinceID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 市ID
        String cityId = UserUtils.getCityID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 排行榜类型，请求URL参数传到PG，然后由PG转发过来
        String currRankType = getCurrRankType(rankType);
        //图书显示类型
        if (StringTools.isEmpty(showType))
        {
            showType = String.valueOf(SystemConstants.SHOW_TYPE_BOOK_NAME);
        }
        // 如果是点击榜的则获取专区点击榜排序数据
        if (CLICKRANKTYPE.equals(currRankType))
        {
            return getClickBookList(nid, showType, pageNo, showPage);
        }
        else
        {
            // 按上架时间排序
            return getPagedBooks(nid, showType, cityId, provinceId, pageNo, showPage);
        }
        
    }
    
    /**
     * 获取当前排行榜类型
     * 
     * @return [参数说明]
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getCurrRankType(String rankType)
    {
        if (CLICKRANKTYPE.equals(rankType))
        {
            return rankType;
        }
        else
        {
            return ONSHELFRANKTYPE;
        }
    }
    
    /**
     * 获取图书点击榜数据
     * 
     * @param nodeId 专区Id
     * @param showType 显示方式
     * @param pageNo 当前页码
     * @param pageSize 每页显示记录数
     * @return [参数说明]
     * @return PagedContent<List<Book>> [返回类型说明]
     * @throws PortalException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static PagedContent<List<Book>> getClickBookList(String nodeId, String showType, int pageNo, int pageSize)
        throws PortalException
    {
        // 分页调用getRank接口
        List<String> bookList = new ArrayList<String>();
        GetRankResponse rankResponse = ContentServiceEngine.getInstance().getRank(nodeId,
            RankType.RANK_STANDARD_CLICK,
            RankType.RANK_TYPE_TOTAL,
            pageSize,
            pageNo);
        if (Util.isEmpty(bookList))
        {
            return null;
        }
        
        List<RankInfo> infoList = rankResponse.getRankListList();
        if (Util.isEmpty(infoList))
        {
            return null;
        }
        for (int i = 0, len = infoList.size(); i < len; i++)
        {
            if (Util.isEmpty(infoList.get(i)))
            {
                continue;
            }
            bookList.add(infoList.get(i).getBookId());
        }
        
        String[] bookIds = bookList.toArray(new String[bookList.size()]);
        
        String[] pagedBookIds = PaginTools.getPagedList(bookIds, pageNo, pageSize);
        
        List<Book> bookInfoList = new ArrayList<Book>();
        for (String bid : pagedBookIds)
        {
            Book book = new Book();
            book.setBookId(bid);
            bookInfoList.add(book);
        }
        
        PagedContent<List<Book>> pagedBooks = new PagedContent<List<Book>>(pageSize, pageNo, bookIds.length);
        pagedBooks.setContent(bookInfoList);
        return pagedBooks;
    }
    
    /**
     * 分页查询专区下的内容列表
     * 
     * @param nodeId
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static PagedContent<List<Book>> getPagedBooks(String nodeId, String showType, String cityId, String provinceId,
        int pageNo, int pageSize)
    {
        // 从缓存中获取专区信息
        NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(nodeId);
        if (null == nodeItem)
        {
            return null;
        }
        
        String[] bookIds = nodeItem.getBookIDs();
        if (null == bookIds || 0 == bookIds.length)
        {
            return null;
        }
        int totalCnt = bookIds.length;
        int requestCnt = pageNo * pageSize;
        LimitNodeItem books = new LimitNodeItem();
        
        cityId = StringTools.isEmpty(cityId) ? Types.COUNTRY : cityId;
        provinceId = StringTools.isEmpty(provinceId) ? SystemConstants.CHINA_ID : provinceId;
        
        // 包月收费模式
        if (null != nodeItem.getProductInfo()
            && StringTools.isEq(SystemConstants.CHARGE_MODE_MONTHLY, nodeItem.getProductInfo().getChargeMode()))
        {
            books = BookManager.filterCityAndProvinceBookIds(nodeItem, cityId, provinceId);
            bookIds = books.getBookIds();
            totalCnt = books.getTotalCnt();
            if (null == bookIds || 0 == bookIds.length)
            {
                return null;
            }
        }
        boolean hasPaged = false;
        // 如果用户请求的个数没有超过缓存的图书个数, 直接返回原图书列表
        if (requestCnt >= bookIds.length)
        {
            bookIds = BookManager.overflowProcess(pageNo, pageSize, nodeId, books.getAdaptCityId(), books.getAdaptProvinceId());
            hasPaged = true;
        }
        return getPageBookList(nodeId,
            bookIds,
            showType,
            pageNo,
            pageSize,
            DatasrcSourceScene.fromNode,
            totalCnt,
            hasPaged);
    }
    
    /**
     * 分页图书数据源
     * 
     * @param bookIds
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static PagedContent<List<Book>> getPageBookList(String nodeId, String[] bookIds, String showType, int pageNo,
        int pageSize, String DSCreateSource, int totalCnt, boolean hasPaged)
    {
        
        String[] pagedBookIds = null;
        // 是否已经分过页了。
        if (hasPaged)
        {
            pagedBookIds = bookIds;
        }
        else
        {
            pagedBookIds = PaginTools.getPagedList(bookIds, pageNo, pageSize);
        }
        if (null == pagedBookIds)
        {
            pagedBookIds = new String[] {};
        }
        List<Book> bookList = new ArrayList<Book>();
        for (String bid : pagedBookIds)
        {
            Book book = new Book();
            book.setBookId(bid);
            bookList.add(book);
        }
        
        PagedContent<List<Book>> pagedBooks = new PagedContent<List<Book>>(pageSize, pageNo, totalCnt);
        pagedBooks.setContent(bookList);
        
        return pagedBooks;
    }
}
