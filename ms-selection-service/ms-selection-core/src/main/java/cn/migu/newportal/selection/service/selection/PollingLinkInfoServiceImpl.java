package cn.migu.newportal.selection.service.selection;

import java.util.*;

import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;

import cn.migu.newportal.selection.Exception.PortalException;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.PollingLinksResponseOuterClass.PollingLinkInfoList;
import cn.migu.selection.proto.base.PollingLinksResponseOuterClass.PollingLinksData;
import cn.migu.selection.proto.base.PollingLinksResponseOuterClass.PollingLinksResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 轮询链接列表
 * 
 *
 * @author zhangmm
 * @version C10 2017年7月25日
 * @since SDP V300R003C10
 */
public class PollingLinkInfoServiceImpl extends ServiceMethodImpl<PollingLinksResponse, ComponentRequest>
{
    
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(PollingLinkInfoServiceImpl.class.getName());
    
    // 参数封装
    private static Map<String, String> reqMap = new HashMap<>();
    
    private static final String METHOD_NAME = "getPollingLinkInfo";
    
    public PollingLinkInfoServiceImpl()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<PollingLinksResponse> getPollingLinks(ServiceController controller, ComponentRequest request)
        throws PortalException
    {
        // 入口日志
        if (logger.isDebugEnabled())
        {
            logger.debug( "Enter PollingLinkInfoServiceImpl-getPollingLinks()，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        Map<String,String> requestMap = request.getParamMapMap();
        PollingLinksResponse.Builder builder = PollingLinksResponse.newBuilder();
        PollingLinksData.Builder builderData = PollingLinksData.newBuilder();

        builder.setPluginCode(StringTools.nvl(requestMap.get(ParamConstants.PLUGINCODE)));

        String linkIds = requestMap.get(ParamConstants.LINKIDS);
        if (StringTools.isEmpty(linkIds))
        {
            logger.error("PollingLinkInfoServiceImpl-getPollingLinks(), linkIds is empty,identityId:{},linkIds:{}",
                CommonHttpUtil.getIdentity(),
                linkIds);
            ProtoUtil.buildResultCode(builder,ErrorCodeAndDesc.LINK_ID_IS_NULL,ParamConstants.NOT_VISABLE);
            return new InvokeResult<PollingLinksResponse>(builder.build());
        }

        String linkNumLimit = requestMap.get(ParamConstants.LINKNUMLIMIT);
        builderData.setLinkNumLimit(StringTools.nvl(linkNumLimit));
        builderData.setBackGroundType(requestMap.get(ParamConstants.BACKGTYPE));

        // 封装链接列表信息
        builderLinkInfoList(builderData, linkIds, StringTools.toInt(linkNumLimit, 1));

        // 设置成功返回码
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        // 封装展示状态
        ProtoUtil.buildCommonData(builderData, request.getParamMapMap());
        builder.setData(builderData);
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit PollingLinkInfoServiceImpl.getPollingLinks,identityId:{} ,response ={}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<PollingLinksResponse>(builder.build());
    }
    
    /**
     * 
     * 拼接响应链接
     *
     * @author zhangmm
     * @param linksList
     * @param list
     * @param backGroundType
     */
    /**
     * 封装链接列表信息
     * @param builderData
     * @param linkIds
     * @param linkNumLimit
     */
    private void builderLinkInfoList(PollingLinksData.Builder builderData, String linkIds,int linkNumLimit)
    {
        List<PollingLinkInfoList> linksList = new ArrayList<>();
        String[] linkIdArr = StringUtils.split(linkIds, ",");
        int linkIdLenght = linkIdArr.length > linkNumLimit ? linkNumLimit : linkIdArr.length;

        String[] linkIdArr2 = Arrays.copyOf(linkIdArr, linkIdLenght);
        linkIds = StringUtils.join(linkIdArr2,",");

        List<LinkInfo> linkInfoList = UesServerServiceUtils.getLinkInfoList(linkIds);
        if (Util.isEmpty(linkInfoList))
        {
            logger.error("PollingLinkInfoServiceImpl-getPollingLinks(), linkInfoList is empty,identityId:{},linkIds:{}",
                CommonHttpUtil.getIdentity(),
                linkIds);
            return;
        }

        for (LinkInfo linkInfo : linkInfoList)
        {
            if(Util.isEmpty(linkInfo))
            {
                continue;
            }

            String beginDate = linkInfo.getBegindate();
            String endDate = linkInfo.getEnddate();

            if(!DateTools.isEffectiveTime(beginDate,endDate,DateTools.DATE_FORMAT_24HOUR_19))
            {
                continue;
            }

            PollingLinkInfoList.Builder linkInfoBuilder = PollingLinkInfoList.newBuilder();
            linkInfoBuilder.setLinkId(StringTools.nvl(linkInfo.getLinkId()));
            linkInfoBuilder.setLinkName(StringTools.nvl(linkInfo.getLinkName()));
            linkInfoBuilder.setLinkPrefix(StringTools.nvl(linkInfo.getLinkprefix()));
            linkInfoBuilder.setLinkDesc(StringTools.nvl(linkInfo.getDescription()));
            linkInfoBuilder.setLinkSuffix(StringTools.nvl(linkInfo.getLinksuffix()));

            Map<String, String> param =
                UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(linkInfo.getSortvalue()));
            String linkUrl = UesServiceUtils.getUESUrl(linkInfo.getLinkurl(),param);
            linkInfoBuilder.setLinkUrl(StringTools.nvl(linkUrl));

            linkInfoBuilder.setLinkWebUrl(UesServiceUtils.getUESUrl(linkInfo.getLinkweburl(),param));

            String picUrl = UesServiceUtils.getUESUrl(linkInfo.getPicurl(),null);
            linkInfoBuilder.setPicUrl(StringTools.nvl(picUrl));

            linkInfoBuilder.setBeginTime(StringTools.nvl(beginDate));
            linkInfoBuilder.setEndTime(StringTools.nvl(endDate));

            linksList.add(linkInfoBuilder.build());
        }

        builderData.addAllLinkInfoList(linksList);

    }
    
}
