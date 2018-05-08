package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.NewCycleDataCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.manager.node.NodeManager;
import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.bean.local.request.BookNameClassificationRequest;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.BookNameClassificationResponseOuterClass.BookNameClassificationResponse;
import cn.migu.selection.proto.base.BookNameClassificationResponseOuterClass.CommonData;
import cn.migu.selection.proto.base.BookNameClassificationResponseOuterClass.CommonLink;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图书名分类
 * 
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since
 */
public class BookNameClassificationImpl extends ServiceMethodImpl<BookNameClassificationResponse, ComponentRequest>
{
    
    private static Logger logger = LoggerFactory.getLogger(BookNameClassificationImpl.class);
    
    private static final String METHOD_NAME = "BookNameClassificationForLink";
    
    /** 跳转类型 */
    private static final String JUMPTYPE_LINK = "1";
    
    private static final String CYCLE_KEY = "CYCLEDATA_";
    
    /** 显示个数 */
    private static int showNum = 5;
    
    public BookNameClassificationImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取图书名分类相关信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<BookNameClassificationResponse> getBookNameClassificationInfo(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter BookNameClassificationImpl-getBookNameClassificationInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        // 封装请求参数
        Map<String, String> params = request.getParamMapMap();
        BookNameClassificationRequest bkNameClass = new BookNameClassificationRequest(params);
        BookNameClassificationResponse.Builder builder = BookNameClassificationResponse.newBuilder();
        builder.setPluginCode(bkNameClass.getPluginCode());
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        CommonData.Builder dataBuilder = CommonData.newBuilder();
        String instanceId = bkNameClass.getInstanceId();
        if (StringTools.isEmpty(instanceId))
        {
            logger.error("component instanceId is empty,identityId:{} instanceId :{}",CommonHttpUtil.getIdentity(),instanceId);
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("instanceId :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<BookNameClassificationResponse>(builder.build());
        }
        
        CycleTypes cycle = bkNameClass.getCycle();
        cycle.setCycleNum(5);
        
        // 要显示的链接
        List<CommonLink> relShowList = null;
        // 链接
        if (JUMPTYPE_LINK.equals(bkNameClass.getJumpType()))
        {
            if (StringTools.isEmpty(bkNameClass.getLink_id_list()))
            {
                logger.error("link_id_list is empty,identityId:{} link_id_list:{}",
                    CommonHttpUtil.getIdentity(),
                    bkNameClass.getLink_id_list());
                builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
                builder.setMessageDesc("link_id_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
                return new InvokeResult<BookNameClassificationResponse>(builder.build());
            }
            relShowList = getLinkList(bkNameClass, cycle, controller);
            putEmptyLinkInfo(relShowList);
        }
        // 专区
        else
        {
            if (StringTools.isEmpty(bkNameClass.getNode_id_list()))
            {
                logger.error("node_id_list is empty,identityId:{} node_id_list:{}",
                    CommonHttpUtil.getIdentity(),
                    bkNameClass.getNode_id_list());
                builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
                builder.setMessageDesc("node_id_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
                return new InvokeResult<BookNameClassificationResponse>(builder.build());
            }
            relShowList = new ArrayList<CommonLink>();
            List<CommonLink> tempNodes = getNodeList(bkNameClass, cycle, controller);
            if (Util.isNotEmpty(tempNodes))
            {
                if (tempNodes.size() >= showNum)
                {
                    for (int i = 0; i < showNum; i++)
                    {
                        relShowList.add(tempNodes.get(i));
                    }
                }
                else
                {
                    relShowList.addAll(tempNodes);
                }
            }
            putEmptyLinkInfo(relShowList);
        }
        dataBuilder.addAllLinks(relShowList);
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
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        // 封装展示状态
        ProtoUtil.buildCommonData(dataBuilder, request.getParamMapMap());
        if (logger.isDebugEnabled())
        {
            logger.debug("exit BookNameClassificationInfo.getBookNameClassificationInfo(),identityId:{} response:{}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<BookNameClassificationResponse>(builder.build());
        
    }
    
    /**
     * 获取地域适配后的链接列表
     * 
     * @author hsq
     * @param bookNameClass
     * @param cycle
     * @param controller
     * @return
     */
    private List<CommonLink> getLinkList(BookNameClassificationRequest bookNameClass, CycleTypes cycle,
        ServiceController controller)
    {
        List<CommonLink> commonLinkList = new ArrayList<CommonLink>();
        // 获取配置链接列表
        List<LinkInfo> cofigLinks = UesServerServiceUtils.getLinkInfoList(bookNameClass.getLink_id_list());

        if (Util.isEmpty(cofigLinks))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("BookNameClassificationInfo.getLinkList failed,identityId:{} link_id_list:{}",
                    CommonHttpUtil.getIdentity(),
                    bookNameClass.getLink_id_list());
            }
            return commonLinkList;
        }
        
        String[] linkIds = bookNameClass.getLink_id_list().split(",");
        
        String key = CYCLE_KEY + "_linkids_" + bookNameClass.getInstanceId();
        linkIds = NewCycleDataCacheService.getInstance().getCycleData(key, cycle, linkIds);
        
        // 省ID
        String userProviceId = UserManager.getUserProvinceId();
        // 市ID
        String userCityId = UserManager.getUserCityId();
        
        // 过来适配省份的链接
        List<LinkInfo> linkInfoList = new ArrayList<LinkInfo>();
        
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
                linkInfoList.add(link);
            }
            // 链接省份匹配
            else if (StringTools.isNotEmpty(linkProvinceId) && StringTools.isNotEmpty(userProviceId)
                && linkProvinceId.contains(userProviceId))
            {
                linkInfoList.add(link);
            }
            // 链接地市匹配
            else if (StringTools.isNotEmpty(linkCityId) && StringTools.isNotEmpty(userCityId)
                && linkCityId.contains(userCityId))
            {
                linkInfoList.add(link);
            }
        }
        
        if (Util.isEmpty(linkInfoList))
        {
            return commonLinkList;
        }
        
        // 如果适配后只剩下1个，则直接返回这个链接
        int linkSize = linkInfoList.size();
        for (int i = 0; i < showNum; i++)
        {
            CommonLink.Builder commonLink = CommonLink.newBuilder();
            if (i < linkSize)
            {
                LinkInfo info = linkInfoList.get(i);
                commonLink.setLinkDesc(ParamUtil.checkResponse(info.getDescription()));
                
                String linkUrl = info.getLinkurl();
                if (StringTools.isNotEmpty(linkUrl))
                {
                    // 添加ln参数
                    Map<String, String> params =
                        UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(info.getSortvalue()));
                    linkUrl = UesServiceUtils.getUESUrl(linkUrl, params);
                }
                commonLink.setLinkUrl(ParamUtil.checkResponse(linkUrl));
            }
            else
            {
                commonLink.setLinkDesc(" ");
                commonLink.setLinkUrl("#");
            }
            
            commonLinkList.add(commonLink.build());
        }
        
        return commonLinkList;
    }
    
    /**
     * 获取配置链接列表
     * 
     * @author hsq
     * @param link_id_list
     * @return
     */
    private List<LinkInfo> getConfigLinks(String link_id_list)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter BookNameClassificationImpl-getConfigLinks identityId:{} link_id_list:{}",
                CommonHttpUtil.getIdentity(),
                link_id_list);
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
        if (Util.isEmpty(linkList) || StringTools.isEmpty(linkid))
        {
            logger.error("message,identityId:{} linkid:{} linkList:{}", CommonHttpUtil.getIdentity(), linkid, linkList);
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
     * 获取专区名称及专区URL
     * 
     * @author hushanqing
     * @param bookNameClass
     * @param cycle
     * @param controller
     * @return
     */
    public List<CommonLink> getNodeList(BookNameClassificationRequest bookNameClass, CycleTypes cycle,
        ServiceController controller)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter BookNameClassificationImpl-getNodeList identityId:{} cycle:{},bookNameClass:{}",
                CommonHttpUtil.getIdentity(),
                cycle,
                bookNameClass);
        }
        
        List<CommonLink> links = new ArrayList<>();
        
        String[] nodeIds = bookNameClass.getNode_id_list().split(",");
        
        String key = CYCLE_KEY + "_nodeIds_" + bookNameClass.getInstanceId();
        nodeIds = NewCycleDataCacheService.getInstance().getCycleData(key, cycle, nodeIds);
        
        for (String nid : nodeIds)
        {
            NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(nid);
            if (Util.isNotEmpty(nodeItem))
            {
                CommonLink.Builder link = CommonLink.newBuilder();
                // 专区名称
                String nodeName = nodeItem.getName();
                link.setLinkDesc(ParamUtil.checkResponse(nodeName));
                
                // 专区访问地址
                String nodeUrl = NodeManager.getNodeUrl(nid);
                link.setLinkUrl(ParamUtil.checkResponse(nodeUrl));
                links.add(link.build());
            }
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("eixt BookNameClassificationImpl.getNodeList(), list ={}",links);
        }
        return links;
    }
    
    /**
     * 插入空链接对象
     * 
     * @author hushanqing
     * @param linkList
     * @return
     */
    public void putEmptyLinkInfo(List<CommonLink> linkList)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("entry BookNameClassificationImpl.putEmptyLinkInfo linkList:{}" ,linkList);
        }
        
        CommonLink.Builder link = null;
        int num = showNum - linkList.size();
        for (int i = 0; i < num; i++)
        {
            link = CommonLink.newBuilder();
            link.setLinkUrl("#");
            linkList.add(link.build());
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("eixt BookNameClassificationImpl.putEmptyLinkInfo linkList ={}",linkList);
        }
        
    }
    
}
