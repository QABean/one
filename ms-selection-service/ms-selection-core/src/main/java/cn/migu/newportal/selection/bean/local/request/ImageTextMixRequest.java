package cn.migu.newportal.selection.bean.local.request;

import java.util.Map;

import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.ParamUtil;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.TypesNum;

/**
 * 封装请求入参
 * 
 * @author zhangmm
 * @version C10 2017年7月20日
 * @since SDP V300R003C10
 */
public class ImageTextMixRequest
{
    
    /** 组件名称 */
    private String pluginCode;
    
    /** 是否展示上边距（单选框）1:展示;2:不展示; 默认2 */
    private String ismarginTop;
    
    /** 获取尾标（单选框）1:点击量;2:图书评分;3:作者名 */
    private String endLogo;
    
    /** 名称显示类型(下拉列表) 1：图书书名,2：长推荐语,4：短推荐语,5：WAP2.0推荐语,6：适配推荐语 */
    private String nameShowType;
    
    /** 组件实例id */
    private String instanceId;
    
    /** 节点id */
    private String node_id_list;
    
    /** 排序类型 1:更新时间;2:上架时间;3:入库时间；6:排序值升序，非必填，默认2 */
    private String sortType;
    
    /** 图书UA放大倍数 */
    private String largeSize;
    
    /** 底部划线 0:不显示;1:显示 */
    private String isMarginBottom;
    
    /** 上边距 0:不显示；1:显示 */
    private String isPaddingTop;
    
    /** 下划线 1：展示 0：不展示 */
    private String isShowLine;
    
    public String getPluginCode()
    {
        return pluginCode;
    }
    
    public void setPluginCode(String pluginCode)
    {
        this.pluginCode = pluginCode;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
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
    
    public String getLargeSize()
    {
        return largeSize;
    }
    
    public void setLargeSize(String largeSize)
    {
        this.largeSize = largeSize;
    }
    
    public String getSortType()
    {
        return sortType;
    }
    
    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }
    
    public String getNode_id_list()
    {
        return node_id_list;
    }
    
    public void setNode_id_list(String node_id_list)
    {
        this.node_id_list = node_id_list;
    }
    
    public String getInstanceId()
    {
        return instanceId;
    }
    
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }
    
    public ImageTextMixRequest(Map<String, String> params)
    {
        init(params);
    }
    
    public String getIsmarginTop()
    {
        return ismarginTop;
    }
    
    public void setIsmarginTop(String ismarginTop)
    {
        this.ismarginTop = ismarginTop;
    }
    
    public String getEndLogo()
    {
        return endLogo;
    }
    
    public void setEndLogo(String endLogo)
    {
        this.endLogo = endLogo;
    }
    
    public String getNameShowType()
    {
        return nameShowType;
    }
    
    public void setNameShowType(String nameShowType)
    {
        this.nameShowType = nameShowType;
    }
    
    public void init(Map<String, String> params)
    {
        // 组件名称
        String pluginCode = StringTools.nvl(params.get(ParamConstants.PLUGINCODE));
        this.setPluginCode(pluginCode);
        
        // 组件实例id
        String instanceId = StringTools.nvl(params.get(ParamConstants.INSTANCEID));
        this.setInstanceId(instanceId);
        // 节点id
        String node_id_list = StringTools.nvl(params.get(ParamConstants.NODE_ID_LIST));
        this.setNode_id_list(node_id_list);
        
        // 排序类型 1:更新时间;2:上架时间;3:入库时间；6:排序值升序，非必填，默认1
        String sortType = StringTools.nvl(params.get(ParamConstants.SORTTYPE));
        String[] sortTypeValue = new String[] {ParamConstants.SORTTYPE_ONE, ParamConstants.SORTTYPE_TWO,
            ParamConstants.SORTTYPE_THREE, ParamConstants.SORTTYPE_SIX};
        sortType = ParamUtil.checkParamter(sortTypeValue, sortType, ParamConstants.SORTTYPE_ONE);
        this.setSortType(sortType);
        
        // 获取图书ua放大倍数
        String largeSize = ParamUtil.responseOut(params.get(ParamConstants.LARGESIZE));
        this.setLargeSize(largeSize);
        
        // 获取上边距
        String ismarginTop = StringTools.nvl(params.get(ParamConstants.ISMARGINTOP));
        ismarginTop = (StringTools.isEq(ismarginTop, ParamConstants.IS_MARGINTOP)) ? ParamConstants.IS_MARGINTOP
            : ParamConstants.NOT_MARGINTOP;
        this.setIsmarginTop(ismarginTop);
        
        // 获取尾标
        String endLogo = StringTools.nvl(params.get(ParamConstants.ENDLOG));
        String[] endLogoValue =
            new String[] {ParamConstants.ISONE_ENDLOGO, ParamConstants.ISTWO_ENDLOGO, ParamConstants.ISTHREE_ENDLOGO};
        endLogo = ParamUtil.checkParamter(endLogoValue, endLogo, ParamConstants.ISONE_ENDLOGO);
        this.setEndLogo(endLogo);
        
        // 获取名称显示类型
        String nameShowType = StringTools.nvl(params.get(ParamConstants.NAMESHOWTYPE));
        String[] nameShowTypeValue = new String[] {ParamConstants.NAMESHOWTYPE_1, ParamConstants.NAMESHOWTYPE_2,
            ParamConstants.NAMESHOWTYPE_4, ParamConstants.NAMESHOWTYPE_5, ParamConstants.NAMESHOWTYPE_6};
        nameShowType = ParamUtil.checkParamter(nameShowTypeValue, nameShowType, ParamConstants.NAMESHOWTYPE_1);
        this.setNameShowType(nameShowType);
        
        // 获取底部划线
        String isMarginBottom = StringTools.nvl(params.get(ParamConstants.ISMARGINBOTTOM));
        isMarginBottom = (StringTools.isEq(isMarginBottom, TypesNum.ONE)) ? TypesNum.ONE : TypesNum.ZERO;
        this.setIsMarginBottom(isMarginBottom);
        
        // 显示上边框 0:不显示；1:显示
        String isPaddingTop = StringTools.nvl(params.get(ParamConstants.ISPADDINGTOP));
        isPaddingTop = (StringTools.isEq(isPaddingTop, TypesNum.ONE)) ? TypesNum.ONE : TypesNum.ZERO;
        this.setIsPaddingTop(isPaddingTop);
        
        // 下划线 1：展示 0：不展示
        String isShowLine = StringTools.nvl(params.get(ParamConstants.ISSHOWLINE));
        isShowLine = (StringTools.isEq(isShowLine, TypesNum.ONE)) ? TypesNum.ONE : TypesNum.ZERO;
        this.setIsShowLine(isShowLine);
    }
    
    @Override
    public String toString()
    {
        
        StringBuilder builder = new StringBuilder();
        builder.append("GetRecommendAuthorRequest [pluginCode=");
        builder.append(pluginCode);
        builder.append(", isMarginTop=");
        builder.append(ismarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", endLogo=");
        builder.append(endLogo);
        builder.append(", nameShowType=");
        builder.append(nameShowType);
        builder.append(", node_id_list=");
        builder.append(node_id_list);
        builder.append(", sortType=");
        builder.append(sortType);
        builder.append(", largeSize=");
        builder.append(largeSize);
        builder.append(", instanceId=");
        builder.append(instanceId);
        builder.append("]");
        return builder.toString();
    }
}
