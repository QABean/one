package cn.migu.newportal.selection.bean.local.request;

import java.util.Map;

import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.selection.bean.BaseResponseBean;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.NumberTools;

/**
 * 图书名分类请求参数封装类
 * 
 * @author hushanqing
 * @version C10 2017年7月26日
 * @since
 */
public class BookNameClassificationRequest extends BaseResponseBean
{
    private static final long serialVersionUID = 1L;

    /** 组件id */
    private String instanceId;
       
    /** 是否展示上边距 */
    private String isMarginTop;
    
    /** 是否显示底部划线 */
    private String isMarginBottom;
    
    /** 是否显示上边距 */
    private String isPaddingTop;
    
    /** 跳转类型 */
    private String jumpType;
    
    /** 专区id列表 */
    private String node_id_list;
    
    /** 链接id列表 */
    private String link_id_list;
    
    /** 轮询 */
    private CycleTypes cycle;

    private String isShowLine;
    
    public String getIsShowLine()
    {
        return isShowLine;
    }

    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }

    public String getInstanceId()
    {
        return instanceId;
    }
    
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public void setIsMarginTop(String isMarginTop)
    {
        this.isMarginTop = isMarginTop;
    }
    
    public String getIsMarginBottom()
    {
        return isMarginBottom;
    }
    
    public void setIsMarginBottom(String isMarginBottom)
    {
        this.isMarginBottom = isMarginBottom;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public void setIsPaddingTop(String isPaddingTop)
    {
        this.isPaddingTop = isPaddingTop;
    }
    
    public String getJumpType()
    {
        return jumpType;
    }
    
    public void setJumpType(String jumpType)
    {
        this.jumpType = jumpType;
    }
    
    public String getNode_id_list()
    {
        return node_id_list;
    }
    
    public void setNode_id_list(String node_id_list)
    {
        this.node_id_list = node_id_list;
    }
    
    public String getLink_id_list()
    {
        return link_id_list;
    }
    
    public void setLink_id_list(String link_id_list)
    {
        this.link_id_list = link_id_list;
    }
    
    public CycleTypes getCycle()
    {
        return cycle;
    }
    
    public void setCycle(CycleTypes cycle)
    {
        this.cycle = cycle;
    }
    
    public BookNameClassificationRequest()
    {
      
    }
    
    public BookNameClassificationRequest(Map<String, String> params)
    {
        initBookNameClassificationBean(params);
    }
    
    public void initBookNameClassificationBean(Map<String, String> params)
    {
        instanceId = String.valueOf(NumberTools.toInt(params.get(ParamConstants.INSTANCEID), 0));
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
        
        jumpType = params.get(ParamConstants.JUMPTYPE);
        jumpType = ParamUtil.getJumpType(jumpType, ParamUtil.TRUE);
        this.setJumpType(jumpType);
        
        node_id_list = params.get(ParamConstants.NODE_ID_LIST);
        this.setNode_id_list(node_id_list);
        
        link_id_list = params.get(ParamConstants.LINK_ID_LIST);
        this.setLink_id_list(link_id_list);
        
        cycle =  new CycleTypes(params);
        this.setCycle(cycle);
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("BookNameClassificationBean [instanceId=");
        builder.append(instanceId);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", jumpType=");
        builder.append(jumpType);
        builder.append(", node_id_list=");
        builder.append(node_id_list);
        builder.append(", link_id_list=");
        builder.append(link_id_list);
        builder.append(", cycle=");
        builder.append(cycle);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append("]");
        return builder.toString();
    }
}
