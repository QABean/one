package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang.StringUtils;


import com.huawei.iread.server.constant.States;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;

import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.manager.ContentSortManager;
import cn.migu.newportal.cache.cache.service.NewCycleDataCacheService;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoExCacheService;
import cn.migu.newportal.selection.bean.local.request.FreeCartoonRequest;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.selection.util.UrlUtil;
import cn.migu.newportal.selection.util.UserUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.NumberTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.FreeForCartoonFlowResponseOuterClass.FreeCartoon;
import cn.migu.selection.proto.base.FreeForCartoonFlowResponseOuterClass.FreeCartoonData;
import cn.migu.selection.proto.base.FreeForCartoonFlowResponseOuterClass.FreeForCartoonFlowResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 免费漫画瀑布流
 * 
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since
 */
public class FreeForCartoonFlowInfo extends ServiceMethodImpl<FreeForCartoonFlowResponse, ComponentRequest>
{
    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(FreeForCartoonFlowInfo.class);
    
    /** 接口名称 */
    private static final String METHOD_NAME = "getFreeCartoonFlowShowInfo";
    
    private static final String CYCLE_KEY = "CYCLEDATA_";
    
    public FreeForCartoonFlowInfo()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取免费漫画相关信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<FreeForCartoonFlowResponse> getFreeCartoonInfo(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter FreeForCartoonFlowInfo.getFreeCartoonInfo() ,identityId:{} request:{}" ,
                CommonHttpUtil.getIdentity(), request);
        }
        // 封装请求参数
        Map<String, String> params = request.getParamMapMap();
        FreeCartoonRequest freeCartoonBean = new FreeCartoonRequest(params);
        
        FreeForCartoonFlowResponse.Builder builder = FreeForCartoonFlowResponse.newBuilder();
        builder.setPluginCode(ParamUtil.checkResponse(freeCartoonBean.getPluginCode()));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        
        FreeCartoonData.Builder dataBuilder = FreeCartoonData.newBuilder();
        
        // 获取组件实例id
        if (StringTools.isEmpty(freeCartoonBean.getInstanceId()))
        {
            logger.error("component instanceId is empty,identityId:{} instanceId :{}",
                CommonHttpUtil.getIdentity(),
                freeCartoonBean.getInstanceId());
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("instanceId :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<FreeForCartoonFlowResponse>(builder.build());
        }
        
        // 页码
        int pageNo = NumberTools.toInt(freeCartoonBean.getPageNo(), 1);
        // 显示个数
        int showNum = ParamUtil.getShowNum(freeCartoonBean.getShowNum(), 2);
        // 调用uesServer获取图书id列表
        String[] book_id_list = this.getBookIds(freeCartoonBean.getInstanceId());
        // 轮询
        CycleTypes cycle = freeCartoonBean.getCycle();
        
        if (StringTools.isEmpty(freeCartoonBean.getLink_id_list()))
        {
            logger.error("link_id_list is empty,identityId:{} link_id_list :{}",
                CommonHttpUtil.getIdentity(),
                freeCartoonBean.getLink_id_list());
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("link_id_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<FreeForCartoonFlowResponse>(builder.build());
        }
        
        if (Util.isEmpty(book_id_list))
        {
            logger.error("book_id_list is empty,identityId:{} book_id_list:{}",CommonHttpUtil.getIdentity(),book_id_list);
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("book_id_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<FreeForCartoonFlowResponse>(builder.build());
        }
        
        String[] tempLinkIds = freeCartoonBean.getLink_id_list().split(",");
        String[] tempBookIds = book_id_list;
        // 存放链接和图书的对应关系
        Map<String, String> bookid_linkid = new HashMap<String, String>();
        int minSize = tempLinkIds.length > tempBookIds.length ? tempBookIds.length : tempLinkIds.length;
        
        String[] newBookIds = new String[minSize];
        for (int i = 0; i < minSize; i++)
        {
            bookid_linkid.put(tempBookIds[i] + "_" + i, tempLinkIds[i]);
            newBookIds[i] = tempBookIds[i] + "_" + i;
        }
        
        // 获取链接列表
        List<LinkInfo> linkList = getLinkList(freeCartoonBean, cycle, controller);
        if (Util.isEmpty(linkList))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("FreeForCartoonFlowInfo.getLinkList is empty,identityId:{},freeCartoonBean:{}",
                    CommonHttpUtil.getIdentity(),
                    freeCartoonBean);
            }
            
        }
        
        // 获取图书列表
        List<FreeCartoon> bookList = getBookList(freeCartoonBean, cycle, newBookIds, controller, pageNo, params);
        if (Util.isEmpty(bookList))
        {
            logger.error(
                "FreeForCartoonFlowInfo.getBookList is empty,identityId:{},freeCartoonBean:{} ,newBookIds:{},pageNo:{},params:{}",
                CommonHttpUtil.getIdentity(),
                freeCartoonBean,
                newBookIds,
                pageNo,
                params);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.NODEBOOK_NOT_EXIST.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.NODEBOOK_NOT_EXIST.getDesc());
            return new InvokeResult<FreeForCartoonFlowResponse>(builder.build());
        }
        
        List<FreeCartoon> showBookList = new ArrayList<>();
        // 需展示的图书列表
        List<FreeCartoon> relshowBookList = new ArrayList<>();

        for (int i = 0; i < bookList.size(); i++)
        {
            FreeCartoon freeCartoon = bookList.get(i);
            if (null == freeCartoon)
            {
                continue;
            }
            
            FreeCartoon.Builder cartoon = freeCartoon.toBuilder();
            cartoon.setBookId(cartoon.getBookId().split("_")[0]);
            // 获取图书匹配的链接
            LinkInfo linkInfo = getbookLinkInfo(bookid_linkid.get(freeCartoon.getBookId()), linkList);
            if (null != linkInfo)
            {
                String desc = linkInfo.getDescription();
                if (StringTools.isEmpty(desc))
                {
                    desc = " ";
                }
                cartoon.setLinkDesc(desc);
                String picUrl = UrlTools.getRelativelyUrl(linkInfo.getPicurl(), UesServiceUtils.DEFAULT_DOMAIN);
                cartoon.setPicUrl(picUrl);
                
                if (logger.isDebugEnabled())
                {
                    logger.debug("get success bookInfo; i:{}, bookid :{}, linkId:{}", i, freeCartoon.getBookId(),
                        linkInfo.getLinkId());
                }
            }
            else
            {
                cartoon.setLinkDesc(" ");
                cartoon.setPicUrl(" ");
            }
            showBookList.add(cartoon.build());
        }
        
        int bookSize = showBookList.size();
        
        if (bookSize < showNum)
        {
            if (bookSize > 1)
            {
                showNum = bookSize - bookSize % 2;
            }
            else
            {
                showNum = 1;
            }
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("get Show book number is {} " , showNum);
        }
        
        if (showNum == 1)
        {
            FreeCartoon cart = showBookList.get(0);
            if (cart != null)
            {
                relshowBookList.add(cart);
            }
        }
        else
        {
            
            for (int i = 0; i < showNum; i++)
            {
                FreeCartoon cart = showBookList.get(i);
                if (cart != null)
                {
                    relshowBookList.add(cart);
                }
            }
        }
        
        // 封装响应
        dataBuilder.setShowType(ParamUtil.checkResponse(freeCartoonBean.getShowType()));
        dataBuilder.addAllBooks(relshowBookList);
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        
        dataBuilder.setIsMarginTop(freeCartoonBean.getIsMarginTop());
        dataBuilder.setIsMarginBottom(freeCartoonBean.getIsMarginBottom());
        dataBuilder.setIsPaddingTop(freeCartoonBean.getIsPaddingTop());
        // 是否显示页面底部横线
        dataBuilder.setIsShowLine(freeCartoonBean.getIsShowLine());
        
        // 判断是否ajax请求
        String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
        if (StringUtils.isNotEmpty(isAjax) && ParamConstants.IS_AJAX.equals(isAjax))
        {
            ComponentRequest.Builder paramMapBuilder = request.toBuilder();
            paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
            dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
        }
        builder.setData(dataBuilder);
        
        // 设置成功返回码
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit FreeForCartoonFlowInfo.getFreeCartoonInfo(),identityId:{} response={}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<FreeForCartoonFlowResponse>(builder.build());
        
    }
    
    /**
     * 获取地域适配后的链接列表
     * 
     * @author hsq
     * @param freeCartoonBean
     * @param cycle
     * @param controller
     * @return
     */
    public List<LinkInfo> getLinkList(FreeCartoonRequest freeCartoonBean, CycleTypes cycle,
        ServiceController controller)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter FreeForCartoonFlowInfo getLinkList(), params={}" , freeCartoonBean);
        }
        
        String[] linkIds = freeCartoonBean.getLink_id_list().split(",");
        
        // 省ID
        String userProviceId = UserUtils.getProvinceID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 市ID
        String userCityId = UserUtils.getCityID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        
        // 过来适配省份的链接ID
        List<String> linkIdList = new ArrayList<String>();
        
        // 获取配置链接列表
        List<LinkInfo> cofigLinks = getConfigLinks(controller, freeCartoonBean.getLink_id_list());
        
        if (null == cofigLinks)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("FreeForCartoonFlowInfo.getConfigLinks is null,identityId:{} link_id_list:{}",
                    CommonHttpUtil.getIdentity(),
                    freeCartoonBean.getLink_id_list());
            }
            return null;
        }
        
        for (String linkId : linkIds)
        {
            LinkInfo link = this.getLinkInfo(linkId, cofigLinks);
            if (null == link)
            {
                continue;
            }
            
            String linkProvinceId = link.getProviceId();
            String linkCityId = link.getCityIds();
            
            // 链接地市和省份都为空
            if (StringTools.isEmpty(linkProvinceId) && StringTools.isEmpty(linkCityId))
            {
                linkIdList.add(linkId);
            }
            else if (StringTools.isNotEmpty(linkProvinceId) && linkProvinceId.contains(userProviceId))
            {
                linkIdList.add(linkId);
            }
            else if (StringTools.isNotEmpty(linkCityId) && linkCityId.contains(userCityId))
            {
                linkIdList.add(linkId);
            }
        }
        
        if (Util.isEmpty(linkIdList))
        {
            return null;
        }
        // 要显示的链接列表
        List<LinkInfo> showList = new ArrayList<LinkInfo>();
        
        // 如果适配后只剩下1个，则直接返回这个链接
        if (1 == linkIdList.size())
        {
            showList.add(getLinkInfo(linkIdList.get(0), cofigLinks));
        }
        else
        {
            // 适配的链接个数
            int length = linkIdList.size();
            
            for (int i = 0; i < length; i++)
            {
                showList.add(getLinkInfo(linkIdList.get(i), cofigLinks));
            }
        }
        
        return showList;
    }
    
    /**
     * 获取配置链接列表
     * 
     * @author hsq
     * @param controller
     * @param link_id_list
     * @return
     */
    public List<LinkInfo> getConfigLinks(ServiceController controller, String link_id_list)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("entry getConfigLinks ... link_id_list:{}" , link_id_list);
        }
        
        List<LinkInfo> list = null;
        // 调用ues微服务获取链接信息
        list = UesServerServiceUtils.getLinkInfoList(link_id_list);
        
        return list;
    }
    
    /**
     * 根据linkId从配置链接列表中获取匹配的链接
     * 
     * @author hsq
     * @param linkid
     * @param linkList
     * @return
     */
    public LinkInfo getLinkInfo(String linkid, List<LinkInfo> linkList)
    {
        if (null == linkList || StringTools.isEmpty(linkid))
        {
            logger.error("config linkList or linkid is empty,identityId:{} linkid:{} linkList:{}",
                CommonHttpUtil.getIdentity(),
                linkid,
                linkList);
            return null;
        }
        
        for (LinkInfo linkInfo : linkList)
        {
            if (linkid.equals(linkInfo.getLinkId()))
            {
                return linkInfo;
            }
        }
        return null;
    }
    
    /**
     * 获取图书列表
     * 
     * @author hsq
     * @param freeCartoonBean
     * @param cycle
     * @param book_id_list
     * @param pageNo
     * @param paramMap
     * @param controller
     * @return
     */
    public List<FreeCartoon> getBookList(FreeCartoonRequest freeCartoonBean, CycleTypes cycle, String[] book_id_list,
        ServiceController controller, int pageNo, Map<String, String> paramMap)
    {
        String[] bookCycleIds = null;
        // 不含下标的图书id列表
        String[] initBookdIds = new String[book_id_list.length];
        for (int i = 0; i < book_id_list.length; i++)
        {
            initBookdIds[i] = book_id_list[i].split("_")[0];
        }
        
        // 轮询
        if (cycle.getIsCycle().equals(SelectionConstants.IS_CYCLE))
        {
            String key = CYCLE_KEY + "_" + freeCartoonBean.getInstanceId();
            bookCycleIds = NewCycleDataCacheService.getInstance().getCycleData(key, cycle, book_id_list);
        }
        // 不轮询
        else
        {
            bookCycleIds = ContentSortManager.getInstance().sortContent(freeCartoonBean.getSortType(),
                initBookdIds,
                freeCartoonBean.getInstanceId());
            List<String> list = new ArrayList<>(Arrays.asList(book_id_list));
            // 获取排序后带下标的图书id
            for (int i = 0; i < bookCycleIds.length; i++)
            {
                for (String str : list)
                {
                    if (str.startsWith(bookCycleIds[i]))
                    {
                        bookCycleIds[i] = str;
                        list.remove(str);
                        break;
                    }
                }
            }
        }
        
        List<FreeCartoon> freeCartoonList = new ArrayList<FreeCartoon>();
        
        for (String bookId_index : bookCycleIds)
        {
            String bookId = bookId_index.split("_")[0];
            // 从缓存中获取图书信息
            BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
            if (null == bookItem)
            {
                continue;
            }
            FreeCartoon.Builder cartoon = FreeCartoon.newBuilder();
            // 获取显示名称
            String bookShowName = BookDataUtil.getShowName(bookId, freeCartoonBean.getNameShowType());
            cartoon.setBookShowName(ParamUtil.checkResponse(bookShowName));
            // 获取作者信息
            AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
            // 获取作者笔名
            if (null != authorInfo)
            {
                String authorPenName = authorInfo.getAuthorPenName();
                cartoon.setAuthorPenName(ParamUtil.checkResponse(authorPenName));
            }
            // 设置图书点击量
            String click =
                BookDataUtil.getInstance().getClickNumDesc(bookItem.getBookId(), freeCartoonBean.getLargeSize());
            cartoon.setBookClickDesc(ParamUtil.checkResponse(click));
            // 获取章节数
            // String[] chapterIds = bookItem.getChapterIDs();
            String[] chapterIds = (String[])PortalContentInfoExCacheService.getInstance().getChapterIds(bookId);
            int chapterSize = 0;
            if (null != chapterIds)
            {
                chapterSize = chapterIds.length;
            }
            cartoon.setChapterSize(String.valueOf(chapterSize));
            // 图书地址
            Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
            params.put(ParamConstants.BID, bookId);
            params.put(ParamConstants.NID, paramMap.get(ParamConstants.NID));
            
            String bookDetailUrl = PresetPageUtils.getBookDetailPage(params);
            cartoon.setBookDetailUrl(ParamUtil.checkResponse(bookDetailUrl));
            // 图书状态
            String bookStatue = bookItem.getIsFinish();
            cartoon.setBookStatus(ParamUtil.checkResponse(bookStatue));
            // 图书是否上架 13上架 16下架
            String status = bookItem.getStatus();
            
            // 获取角标
            String conce = BookDataUtil.getConer(bookItem, freeCartoonBean.getCornerShowTypes());
            cartoon.setCornerShowType(ParamUtil.checkResponse(conce));
            
            cartoon.setBookId(ParamUtil.checkResponse(bookId_index));
            // 如果上架则添加
            if (States.CONTENT_ON_FIGHT.equals(status))
            {
                freeCartoonList.add(cartoon.build());
            }
            
        }
        
        return freeCartoonList;
    }
    
    /**
     * 根据bookId找到对应的链接
     * 
     * @author hsq
     * @param bookId
     * @param linkList
     * @return
     */
    public LinkInfo getbookLinkInfo(String bookId, List<LinkInfo> linkList)
    {
        if (null == linkList || null == bookId)
        {
            logger.debug("linkList or bookid is null,identityId:{} bookId:{} linkList:{}",
                CommonHttpUtil.getIdentity(),
                bookId,
                linkList);
            return null;
        }
        for (LinkInfo link : linkList)
        {
            if (bookId.equals(link.getLinkId()))
            {
                return link;
            }
        }
        return null;
    }
    
    /**
     * 调用uesserver获取图书id列表
     * 
     * @author hushanqing
     * @param instanceId
     * @return
     */
    public String[] getBookIds(String instanceId)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter FreeForCartoonFlowInfo.getBookIds() parameter instanceId :{}" , instanceId);
        }
        String[] bookIds = null;
        Set<String> book_id_list = new HashSet<String>();
        Map<String, Set<String>> nodeAndContent = UesServerServiceUtils.getUesNodeAndContent(instanceId,ParamConstants.BOOK_ID_LIST);
        if (Util.isNotEmpty(nodeAndContent))
        {
            for (Entry<String, Set<String>> entry : nodeAndContent.entrySet())
            {
                book_id_list.addAll(entry.getValue());
            }
        }
        if (Util.isNotEmpty(book_id_list))
        {
            bookIds = book_id_list.toArray(new String[book_id_list.size()]);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("exit FreeForCartoonFlowInfo.getBookIds() bookIds :{}",bookIds);
        }
        return bookIds;
    }
}
