package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
/**
 * 获取名家推荐请求类
 * 
 *
 * @author zhaoxinwei
 * @version C10 2017年7月27日
 * @since SDP V300R003C10
 */
public class GetRecommendAuthorRequest
{
    // 组件名字
    private String pluginCode;
    
    // 展示类型
    private String showType;
    
    // 是否显示上划线1：是；0否
    private String isMarginTop;
    
    // 是否显示底部划线1：是；0否
    private String isMarginBottom;
    
    // 展示上边距1：是；0：否
    private String isPaddingTop;
    
    // 展示下划线1：是；0：否
    private String isShowLine;
    
    // 页码数
    private String linkIdList;
    
    // 组件实例Id
    private String instanceId;
    
    public String getPluginCode()
    {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode)
    {
        this.pluginCode = pluginCode;
    }

    public String getShowType()
    {
        return showType;
    }


    public void setShowType(String showType)
    {
        this.showType = showType;
    }


    public void setIsMarginTop(String isMarginTop)
    {
        this.isMarginTop = isMarginTop;
    }


    public void setIsMarginBottom(String isMarginBottom)
    {
        this.isMarginBottom = isMarginBottom;
    }


    public void setIsPaddingTop(String isPaddingTop)
    {
        this.isPaddingTop = isPaddingTop;
    }


    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }


    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }


    public GetRecommendAuthorRequest(ComponentRequest request)
    {
        if (Util.isNotEmpty(request))
        {
            this.pluginCode = StringTools.nvl(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
            
            String tempShowType = StringTools.nvl(request.getParamMapMap().get(ParamConstants.SHOWTYPE_MODEL));
            this.showType = StringTools.isEq(tempShowType, ParamConstants.SHOWTYPE_MODELTWO)
                ? ParamConstants.SHOWTYPE_MODELTWO : ParamConstants.SHOWTYPE_MODELONE;
            
            String tempIsMarginBottom = StringTools.nvl(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM));
            this.isMarginBottom = StringTools.isEq(tempIsMarginBottom, ParamConstants.IS_MARGINBOTTOM)
                ? ParamConstants.IS_MARGINBOTTOM : ParamConstants.NOT_MARGINBOTTOM;
            
            String  tempIsMarginTop= StringTools.nvl(request.getParamMapMap().get(ParamConstants.ISMARGINTOP));
            this.isMarginTop = StringTools.isEq(tempIsMarginTop, ParamConstants.IS_MARGINTOP)
                ? ParamConstants.IS_MARGINTOP : ParamConstants.NOT_MARGINTOP;
            
            String tempIsPaddingTop = StringTools.nvl(request.getParamMapMap().get(ParamConstants.ISPADDINGTOP));
            this.isPaddingTop = StringTools.isEq(tempIsPaddingTop, ParamConstants.IS_PADDINGTOP)
                ? ParamConstants.IS_PADDINGTOP : ParamConstants.NOT_PADDINGTOP;
            
            String tempIsShowLine = StringTools.nvl(request.getParamMapMap().get(ParamConstants.ISSHOWLINE));
            this.isShowLine = StringTools.isEq(tempIsShowLine, ParamConstants.IS_ISSHOWLINE)
                ? ParamConstants.IS_ISSHOWLINE : ParamConstants.NOT_ISSHOWLINE;
            
            this.linkIdList = StringTools.nvl(request.getParamMapMap().get(ParamConstants.LINK_ID_LIST));
            
            this.instanceId = StringTools.nvl(request.getParamMapMap().get(ParamConstants.INSTANCEID));
        }
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public String getIsMarginBottom()
    {
        return isMarginBottom;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    
    public String getInstanceId()
    {
        return instanceId;
    }

    public String getLinkIdList()
    {
        return linkIdList;
    }

    public void setLinkIdList(String linkIdList)
    {
        this.linkIdList = linkIdList;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GetRecommendAuthorRequest [pluginCode=");
        builder.append(pluginCode);
        builder.append(", showType=");
        builder.append(showType);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", linkIdList=");
        builder.append(linkIdList);
        builder.append(", instanceId=");
        builder.append(instanceId);
        builder.append("]");
        return builder.toString();
    }

    

}
