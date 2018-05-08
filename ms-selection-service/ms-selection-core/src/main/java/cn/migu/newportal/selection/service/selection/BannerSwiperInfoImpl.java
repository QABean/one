package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.uesserver.api.Struct.ConfigRequest;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.service.BannerSwiperServiceImpl;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.BannerSwiperResponseOuterClass.BannerSwiperBanner;
import cn.migu.selection.proto.base.BannerSwiperResponseOuterClass.BannerSwiperData;
import cn.migu.selection.proto.base.BannerSwiperResponseOuterClass.BannerSwiperResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BannerSwiperInfoImpl extends ServiceMethodImpl<BannerSwiperResponse, ComponentRequest>
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerSwiperInfoImpl.class);
    
    private static final String METHOD_NAME = "getBannerSwiperInfo";
    
    private BannerSwiperServiceImpl bannerSwiperServiceImpl;
    
    public BannerSwiperServiceImpl getBannerSwiperServiceImpl()
    {
        return bannerSwiperServiceImpl;
    }
    
    public BannerSwiperInfoImpl()
    {
        super(METHOD_NAME);
    }
    
    
    @ImplementMethod
    public InvokeResult<BannerSwiperResponse> bannerSwiperInfo(ServiceController controller, ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug( "enter BannerSwiperInfoImpl-bannerSwiperInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        BannerSwiperResponse.Builder builder = BannerSwiperResponse.newBuilder();
        InvokeContext context = controller.getContext();
        String domainUrl = StringUtils.EMPTY;
        Map<String, String> map = request.getParamMapMap();
        UesServiceUtils.setPublicParamToRequest(map);
        builder.setPluginCode(map.get(ParamConstants.PLUGINCODE));
        
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        
        ConfigRequest.Builder configBuilder = ConfigRequest.newBuilder();
        configBuilder.setSiteId(SelectionConstants.DEFAULT_SITEID);
        configBuilder.setConfigkey(SelectionConstants.DOMAIN_PREFIX);
        
        List<LinkInfo> list = null;
        list = UesServerServiceUtils.getLinkInfoList(map.get(ParamConstants.LINKIDS));
        domainUrl = UesServerServiceUtils.getUesParamConfig(SelectionConstants.DOMAIN_PREFIX, "");
        if (list != null && list.size() > 0)
        {
            BannerSwiperData.Builder bannerSwiperData = BannerSwiperData.newBuilder();
            
            bannerSwiperData.setIsShowLine(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISSHOWLINE)));
            bannerSwiperData.setIsMarginTop(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
            bannerSwiperData.setIsMarginBottom(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
            
            List<BannerSwiperBanner> bannerSwiperBanners = new ArrayList<BannerSwiperBanner>();
            BannerSwiperBanner.Builder bannerSwiperBanner = BannerSwiperBanner.newBuilder();
            for (LinkInfo info : list)
            {
                bannerSwiperBanner.setPicUrl(domainUrl + SelectionConstants.ENV_P + info.getPicurl());
                
                bannerSwiperBanner.setUrl(UesServiceUtils.getUESUrl(info.getLinkurl(),
                    UesServiceUtils.buildPublicParaMap(null, Util.nvl(info.getSortvalue()))));
                bannerSwiperBanners.add(bannerSwiperBanner.build());
            }
            bannerSwiperData.addAllBanner(bannerSwiperBanners);
            bannerSwiperData.setTotalNum(String.valueOf(list.size()));
            if (list.size() > 1)
            {
                bannerSwiperData.setIsShowLine(ParamConstants.IS_ISSHOWLINE);
            }
            else
            {
                bannerSwiperData.setIsShowLine(ParamConstants.NOT_ISSHOWLINE);
            }
            
            String isAjax = map.get(ParamConstants.ISAJAX);
            if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                bannerSwiperData.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            
            builder.setData(bannerSwiperData);
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
            
        }
        else
        {
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
            
        }
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug( "Exit BannerSwiperInfoImpl-bannerSwiperInfo，identityId:{} response :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<BannerSwiperResponse>(builder.build());
    }
    
}
