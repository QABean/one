package cn.migu.newportal.selection.bean.local.request;

import org.apache.commons.lang.math.NumberUtils;

import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;

/**
 * 听书纵向请求参数bean
 *
 * @author yejiaxu
 * @version C10 2017年7月18日
 * @since SDP V300R003C10
 */
public class ListenVerticalRequest
{
    // 是否显示上划线1：是；0否
    private String isMarginTop;
    
    // 是否显示底部划线1：是；0否
    private String isMarginBootom;
    
    // 展示上边距1：是；0：否
    private String isPaddingTop;
    
    // 展示下划线1：是；0：否
    private String isShowLine;
    
    // 页码数
    private int pageNo;
    
    // 图书展示个数
    private int showNum;
    
    // 专区Id
    private String nid;
    
    // 是否播放最新一章1：是；2：否
    private String isPlayNew;
    
    // 角标展示（0:无;1:免费;2:限免;3:会员;4:完本;5:名家;6:上传）
    private String cornerShowType;
    
    // 组件实例Id
    private String instanceId;
    
    public ListenVerticalRequest()
    {
        super();
    }
    
    public ListenVerticalRequest(ComponentRequest request)
    {
        super();
        if (Util.isNotEmpty(request))
        {
            
            // 页码
            String page = request.getParamMapMap().get(ParamConstants.PAGENO);
            if (NumberUtils.toInt(page, 1) < 1)
            {
                page = PropertiesConfig.getProperty("detailPage_bookVertical_default_pageNo", "1");
            }
            int pageNumber = NumberUtils.toInt(page);
            
            // 每页展示数量
            String showPg = request.getParamMapMap().get(ParamConstants.SHOWNUM);// 获取展示条数
            if (NumberUtils.toInt(showPg, -1) < 1)
            {
                showPg = PropertiesConfig.getProperty("detailPage_verticalComponentInfo_default_showPage", "5");
            }
            int showPage = NumberUtils.toInt(showPg);
            this.isMarginTop = ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISMARGINTOP), "0");
            this.isMarginBootom =
                ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM), "0");
            this.isPaddingTop = ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISPADDINGTOP), "0");
            this.isShowLine = ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISSHOWLINE), "0");
            this.pageNo = pageNumber;
            this.showNum = showPage;
            this.nid = request.getParamMapMap().get(ParamConstants.NID);
            this.isPlayNew = ParamUtil.checkIsplayNew(request.getParamMapMap().get(ParamConstants.ISPLAYNEW));
            this.cornerShowType = request.getParamMapMap().get(ParamConstants.CORNERSHOWTYPE);
            this.instanceId = request.getParamMapMap().get(ParamConstants.INSTANCEID);
        }
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public String getIsMarginBootom()
    {
        return isMarginBootom;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public int getPageNo()
    {
        return pageNo;
    }
    
    public int getShowNum()
    {
        return showNum;
    }
    
    public String getNid()
    {
        return nid;
    }
    
    public String getIsPlayNew()
    {
        return isPlayNew;
    }
    
    public String getCornerShowType()
    {
        return cornerShowType;
    }
    
    public String getInstanceId()
    {
        return instanceId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("ListenVerticalRequest [isMarginTop=")
            .append(isMarginTop)
            .append(", isMarginBootom=")
            .append(isMarginBootom)
            .append(", isPaddingTop=")
            .append(isPaddingTop)
            .append(", isShowLine=")
            .append(isShowLine)
            .append(", pageNo=")
            .append(pageNo)
            .append(", showNum=")
            .append(showNum)
            .append(", nid=")
            .append(nid)
            .append(", isPlayNew=")
            .append(isPlayNew)
            .append(", cornerShowType=")
            .append(cornerShowType)
            .append(", instanceId=")
            .append(instanceId)
            .append("]")
            .toString();
    }
}
