package cn.migu.newportal.selection.bean.local.request;

import java.util.Arrays;

import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;

/**
 * 活动页小标题
 *
 * @author gongdaxin
 * @version C10 2017年7月27日
 * @since
 */
public class ActivityBannerRequest
{
    //组件名称
    private String pluginCode;
    
    // 是否显示上划线1：是；0否
    private String isMarginTop=ParamConstants.NOT_MARGINTOP;
    
    // 是否显示底部划线1：是；0否
    private String isMarginBootom=ParamConstants.NOT_MARGINBOTTOM;
    
    // 展示上边距1：是；0：否
    private String isPaddingTop=ParamConstants.NOT_PADDINGTOP;
    
    // 展示下划线1：是；0：否
    private String isShowLine=ParamConstants.NOT_ISSHOWLINE;
    
    // 连接个数,默认无连接（1）
    private String linkNum ="1";
    
    // 图片地址
    private String pictureUrl;
    
    // 连接集合
    private String linkIds;
    
    public ActivityBannerRequest()
    {
        super();
    }
    
    public ActivityBannerRequest(ComponentRequest request)
    {
        super();
        if (Util.isNotEmpty(request))
        {
            pluginCode = request.getParamMapMap().get(ParamConstants.PLUGINCODE);
            
            isMarginTop =
                StringTools.isEq(ParamConstants.IS_MARGINTOP, request.getParamMapMap().get(ParamConstants.ISMARGINTOP))
                    ? ParamConstants.IS_MARGINTOP : ParamConstants.NOT_MARGINTOP;
            
            isMarginBootom = StringTools.isEq(ParamConstants.IS_MARGINBOTTOM,
                request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)) ? ParamConstants.IS_MARGINBOTTOM
                    : ParamConstants.NOT_MARGINBOTTOM;
            
            isPaddingTop = StringTools.isEq(ParamConstants.IS_PADDINGTOP,
                request.getParamMapMap().get(ParamConstants.ISPADDINGTOP)) ? ParamConstants.IS_PADDINGTOP
                    : ParamConstants.NOT_PADDINGTOP;
            
            isShowLine =
                StringTools.isEq(ParamConstants.IS_SHOWLINE, request.getParamMapMap().get(ParamConstants.ISSHOWLINE))
                    ? ParamConstants.IS_SHOWLINE : ParamConstants.IS_NOT_SHOWLINE;
            
            // 获取数据来源
            String gType = request.getParamMapMap().get(ParamConstants.GTYPE);
            String[] gTypeValue = new String[] {ParamConstants.GTYPE_ONE, ParamConstants.GTYPE_TWO,
                ParamConstants.GTYPE_THREE, ParamConstants.GTYPE_FOUR};
            linkNum = ParamUtil.checkParamter(gTypeValue, gType, ParamConstants.GTYPE_ONE);
            
            // 获取图片地址
            pictureUrl = request.getParamMapMap().get(ParamConstants.PICTURE_URL);
            
            // 获取配置链接ID集合
            linkIds = request.getParamMapMap().get(ParamConstants.LINK_ID_LIST);
        }
    }
    
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public void setIsMarginTop(String isMarginTop)
    {
        this.isMarginTop = isMarginTop;
    }
    
    public String getIsMarginBootom()
    {
        return isMarginBootom;
    }
    
    public void setIsMarginBootom(String isMarginBootom)
    {
        this.isMarginBootom = isMarginBootom;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public void setIsPaddingTop(String isPaddingTop)
    {
        this.isPaddingTop = isPaddingTop;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }
    
    public String getLinkNum()
    {
        return linkNum;
    }
    
    public void setLinkNum(String linkNum)
    {
        this.linkNum = linkNum;
    }
    
    public String getPictureUrl()
    {
        return pictureUrl;
    }
    
    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }
    
    public String getLinkIds()
    {
        return linkIds;
    }
    
    public void setLinkIds(String linkIds)
    {
        this.linkIds = linkIds;
    }
    
    public String getPluginCode()
    {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode)
    {
        this.pluginCode = pluginCode;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ActivityBannerRequest [pluginCode=");
        builder.append(pluginCode);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBootom=");
        builder.append(isMarginBootom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", linkNum=");
        builder.append(linkNum);
        builder.append(", pictureUrl=");
        builder.append(pictureUrl);
        builder.append(", linkIds=");
        builder.append(linkIds);
        builder.append("]");
        return builder.toString();
    }
    
}
