package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.bean.local.request.ActivityBannerRequest;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.UrlUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ActivityBannerResponseOuterClass.ActivityBannerData;
import cn.migu.selection.proto.base.ActivityBannerResponseOuterClass.ActivityBannerResponse;
import cn.migu.selection.proto.base.ActivityBannerResponseOuterClass.ActivityBannerResponse.Builder;
import cn.migu.selection.proto.base.ActivityBannerResponseOuterClass.ActivityBanners;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 活动小标题
 * 
 * @author gongdaxin
 * @version 2017年7月27日
 * @since
 */
public class ActivityBannerImpl extends ServiceMethodImpl<ActivityBannerResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(ActivityBannerImpl.class);
    
    private static final String METHOD_NAME = "getActivityBannerInfo";
    
    public ActivityBannerImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 
     * 活动小标题
     *
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ActivityBannerResponse> getActivityBanners(ServiceController controller,
        ComponentRequest request)
    {
        ActivityBannerRequest bannerRequest = new ActivityBannerRequest(request);
        UesServiceUtils.setPublicParamToRequest(request.getParamMapMap());
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter ActivityBannerImpl.getActivityBanners,identityId:{}, ComponentRequest :{},ActivityBannerRequest:{}",
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request),bannerRequest);
        }
        
        ActivityBannerResponse.Builder builder = ActivityBannerResponse.newBuilder();
        String urlPrefix = UrlUtil.getUrlPrefix(controller);
        // 截取字符串：
        if (StringTools.isNotEmpty(urlPrefix) && urlPrefix.endsWith("/"))
        {
            urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
        }
        
        List<ActivityBanners> links = new ArrayList<ActivityBanners>();
        int linkNum = StringTools.toInt(bannerRequest.getLinkNum(), 1);
        // 当配置为无连接时，不获取连接信息，直接封装resp,返回数据
        if (linkNum <= 1)
        {
            ActivityBanners.Builder link = ActivityBanners.newBuilder();
            links.add(link.build());
            buildRespose(builder, bannerRequest, links, urlPrefix);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("exit ActivityBannerImpl.getActivityBanners,identityId:{} response:{} ",
                    CommonHttpUtil.getIdentity(),
                    JsonFormatUtil.printToString(builder));
            }
            return new InvokeResult<ActivityBannerResponse>(builder.build());
        }
        
        // 获取配置的所有连接的信息
        String linkIds = bannerRequest.getLinkIds();
        List<LinkInfo> linkInfoList = UesServerServiceUtils.getLinkInfoList( linkIds);
        
        // 遍历需要返回linkNum-1次（linkNum：1:无；2：单链接；3：双链接；:4三链接）
        for (int i = 0; i < linkNum - 1; i++)
        {
            ActivityBanners.Builder link = ActivityBanners.newBuilder();
            
            if (linkInfoList==null) 
            {
				break;
			}
            
            if (linkInfoList.size() - 1 >= i)
            {
                LinkInfo linkInfo = linkInfoList.get(i);
                if (linkInfo != null)
                {
                    String linkprefix = linkInfo.getLinkprefix();
                    // 如果连接前缀存在，则返回url为连接前缀配置的url,外站返回1
                    if (StringTools.isNotEmpty(linkprefix))
                    {
                        Map<String, String> params =
                            UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(linkInfo.getSortvalue()));
                        linkprefix = UesServiceUtils.getUESUrl(linkprefix, params);
                        
                        link.setLinkUrl(linkprefix);
                        link.setStartExPage(ParamConstants.STARTEXPAGE_ONE);
                    }
                    else
                    {
                        // 返回url为配置的连接url,外站返回0
                        String url = linkInfo.getLinkurl();
                        if (StringTools.isNotEmpty(url))
                        {
                            Map<String, String> params =
                                UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(linkInfo.getSortvalue()));
                            url = UesServiceUtils.getUESUrl(url, params);
                        }
                        link.setLinkUrl(ParamUtil.checkResponse(url));
                        link.setStartExPage(ParamConstants.STARTEXPAGE_ZERO);
                    }
                    links.add(link.build());
                    continue;
                }
                
            }
            
            // 获取不到连接信息，获取配置的连接信息不足显示个数时，返回空字段
            // link.setLinkUrl("");
            // link.setStartExPage(" ");
            
            links.add(link.build());
        }
        
        buildRespose(builder, bannerRequest, links, urlPrefix);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("exit ActivityBannerImpl.getActivityBanners,identityId:{} response :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ActivityBannerResponse>(builder.build());
    }
    
    /**
     * 
     * 构造返回参数
     *
     * @author gongdaxin
     * @param builder
     * @param bannerRequest
     * @param urlPrefix
     */
    private void buildRespose(Builder builder, ActivityBannerRequest bannerRequest, List<ActivityBanners> links,
        String urlPrefix)
    {
        ActivityBannerData.Builder builderData = ActivityBannerData.newBuilder();
        // 数据来源
        builderData.setLinkType(bannerRequest.getLinkNum());
        
        // 构造图片url
        String pictureUrl = "";
        if (StringTools.isNotEmpty(bannerRequest.getPictureUrl()))
        {
            pictureUrl = UrlTools.getRelativelyUrl(bannerRequest.getPictureUrl(), UesServiceUtils.DEFAULT_DOMAIN);
        }
        builderData.setPictureUrl(StringTools.nvl(pictureUrl));
        
        builderData.addAllLinks(links);
        
        // 构造基本信息
        builderData.setIsMarginBottom(bannerRequest.getIsMarginBootom());
        builderData.setIsMarginTop(bannerRequest.getIsMarginTop());
        builderData.setIsPaddingTop(bannerRequest.getIsPaddingTop());
        builderData.setIsShowLine(bannerRequest.getIsShowLine());
        builder.setPluginCode(bannerRequest.getPluginCode());
        builder.setData(builderData);
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        
    }
    
}
