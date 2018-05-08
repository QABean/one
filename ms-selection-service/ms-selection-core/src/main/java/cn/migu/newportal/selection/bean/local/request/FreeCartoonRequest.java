package cn.migu.newportal.selection.bean.local.request;

import java.util.Map;

import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.selection.bean.BaseResponseBean;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;

/**
 * 免费漫画瀑布流请求参数封装类
 * 
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since
 */
public class FreeCartoonRequest extends BaseResponseBean
{
    private static final long serialVersionUID = 1L;
    
    /** 角标 */
    private String cornerShowTypes;
    
    /**轮询*/
    private CycleTypes cycle;
    
    /** 组件实例id */
    private String instanceId;
    
    /** 是否显示底部划线 */
    private String isMarginBottom;
    
    /** 是否展示上边距 */
    private String isMarginTop;
    
    /** 是否显示上边距 */
    private String isPaddingTop;
    
    /** 是否展示下划线 */
    private String isShowLine;
    
    /** 图书uv放大倍数 */
    private String largeSize;
    
    /** 链接id列表 */
    private String link_id_list;

    /** 名称显示类型 */
    private String nameShowType;

    /** 页码 */
    private String pageNo;
    
    /** 显示个数 */
    private String showNum;
    
    /** 显示类型 */
    private String showType;
    
    /**排序类型*/
    private String sortType;
    
    public FreeCartoonRequest()
    {
        
    }
    
    public FreeCartoonRequest(Map<String, String> params)
    {
        initFreeCartoonBean(params);
    }
    
    public String getCornerShowTypes()
    {
        return cornerShowTypes;
    }
    
    public CycleTypes getCycle()
    {
        return cycle;
    }
    
    public String getInstanceId()
    {
        return instanceId;
    }
    
    public String getIsMarginBottom()
    {
        return isMarginBottom;
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public String getLargeSize()
    {
        return largeSize;
    }
    
    public String getLink_id_list()
    {
        return link_id_list;
    }
    
    public String getNameShowType()
    {
        return nameShowType;
    }
    
    public String getPageNo()
    {
        return pageNo;
    }
    
    public String getShowNum()
    {
        return showNum;
    }
    
    public String getShowType()
    {
        return showType;
    }
    
    public String getSortType()
    {
        return sortType;
    }
    
    public void initFreeCartoonBean(Map<String, String> params)
    {
        instanceId = params.get(ParamConstants.INSTANCEID);
        this.setInstanceId(instanceId);
        
        pluginCode = params.get(ParamConstants.PLUGINCODE);
        pluginCode = ParamUtil.getPluginCode(pluginCode);
        this.setPluginCode(pluginCode);
        
        isMarginTop = params.get(ParamConstants.ISMARGINTOP);
        isMarginTop = ParamUtil.getIsMarginTop(isMarginTop);
        this.setIsMarginTop(isMarginTop);
        
        isMarginBottom = params.get(ParamConstants.ISMARGINBOTTOM);
        isMarginBottom = ParamUtil.getIsMarginBottom(isMarginBottom);
        this.setIsMarginBottom(isMarginBottom);
        
        isPaddingTop = params.get(ParamConstants.ISPADDINGTOP);
        isPaddingTop = ParamUtil.getisPaddingTop(isPaddingTop);
        this.setIsPaddingTop(isPaddingTop);
        
        isShowLine = params.get(ParamConstants.ISSHOWLINE);
        isShowLine = ParamUtil.getIsShowLine(isShowLine, ParamUtil.FALSE);
        this.setIsShowLine(isShowLine);
        
        showType = params.get(ParamConstants.SHOWTYPE);
        showType = ParamUtil.getShowType(showType, ParamConstants.SHOWTYPE_BOOKNAME);
        this.setShowType(showType);
        
        sortType = params.get(ParamConstants.SORTTYPE);
        sortType = ParamUtil.getSortType(sortType, "1");
        
        nameShowType = params.get(ParamConstants.NAMESHOWTYPE);
        this.setNameShowType(nameShowType);
        
        cornerShowTypes = params.get(ParamConstants.CORNERSHOWTYPE);
        this.setCornerShowTypes(cornerShowTypes);
        
        pageNo = params.get(ParamConstants.PAGENO);
        this.setPageNo(pageNo);
        
        showNum = params.get(ParamConstants.SHOWNUM);
        this.setShowNum(showNum);
        
        link_id_list = params.get(ParamConstants.LINK_ID_LIST);
        this.setLink_id_list(link_id_list);
        
        largeSize = params.get(ParamConstants.LARGESIZE);
        largeSize = String.valueOf(ParamUtil.getLargeSize(largeSize, 1));
        this.setLargeSize(largeSize);
        
        cycle = new CycleTypes(params);
        this.setCycle(cycle);
    }
    
    public void setCornerShowTypes(String cornerShowTypes)
    {
        this.cornerShowTypes = cornerShowTypes;
    }
    
    public void setCycle(CycleTypes cycle)
    {
        this.cycle = cycle;
    }
    
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }
    
    public void setIsMarginBottom(String isMarginBottom)
    {
        this.isMarginBottom = isMarginBottom;
    }
    
    public void setIsMarginTop(String isMarginTop)
    {
        this.isMarginTop = isMarginTop;
    }
    
    public void setIsPaddingTop(String isPaddingTop)
    {
        this.isPaddingTop = isPaddingTop;
    }
    
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }
    
    public void setLargeSize(String largeSize)
    {
        this.largeSize = largeSize;
    }
    
    public void setLink_id_list(String link_id_list)
    {
        this.link_id_list = link_id_list;
    }
    
    public void setNameShowType(String nameShowType)
    {
        this.nameShowType = nameShowType;
    }
    
    public void setPageNo(String pageNo)
    {
        this.pageNo = pageNo;
    }
    
    public void setShowNum(String showNum)
    {
        this.showNum = showNum;
    }
    
    public void setShowType(String showType)
    {
        this.showType = showType;
    }
    
    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("FreeCartoonBean [instanceId=");
        builder.append(instanceId);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", showType=");
        builder.append(showType);
        builder.append(", nameShowType=");
        builder.append(nameShowType);
        builder.append(", cornerShowTypes=");
        builder.append(cornerShowTypes);
        builder.append(", pageNo=");
        builder.append(pageNo);
        builder.append(", showNum=");
        builder.append(showNum);
        builder.append(", largeSize=");
        builder.append(largeSize);
        builder.append(", link_id_list=");
        builder.append(link_id_list);
        builder.append(", cycle=");
        builder.append(cycle);
        builder.append("]");
        return builder.toString();
    }
    
}
