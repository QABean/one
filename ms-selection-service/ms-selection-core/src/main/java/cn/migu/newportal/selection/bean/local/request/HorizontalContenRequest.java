package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;

/**
 * 横向内容请求参数处理
 *
 * @author yejiaxu
 * @version C10 2017年7月13日
 * @since SDP V300R003C10
 */
public class HorizontalContenRequest
{
    // 页面样式展示类型
    private String type;
    
    // 每行展示图书个数
    private String showNum;
    
    // 展示行数
    private String lineNum;
    
    // 是否展示下划线
    private String isShowLine;
    
    // 专区id列表
    private String nodeId_list;
    
    // 展示类型1、书名，2、书名+作者名，3、书名+点击量
    private String showType;
    
    // 展示腰封类型
    private String isShowYF;
    
    // 是否展示上边框
    private String isPaddingTop;
    
    // 角标展示
    private String cornerShowType;
    
    // 图书阅读人数放大倍数
    private String largeSize;
    
    // 图书名称显示类型
    private String nameShowType;
    
    // 展示图书数量（输入框）
    private String showBookNum;
    
    // 是否支持轮循：1轮循；0不轮循
    private String isCycle;
    
    // 排序
    private String sortType;
    
    // 专区展示个数
    private String showNodeNum;
    
    // 获取组件实例ID
    private String instanceId;
    
    // 是否显示上划线1：是；0否
    private String isMarginTop;
    
    // 是否显示底部划线1：是；0否
    private String isMarginBootom;
    
    private static HorizontalContenRequest instance = new HorizontalContenRequest();
    
    public HorizontalContenRequest()
    {
        super();
    }
    
    public HorizontalContenRequest(ComponentRequest request)
    {
        this.type = request.getParamMapMap().get(ParamConstants.TYPE);
        this.showNum = request.getParamMapMap().get(ParamConstants.SHOWLINE);
        this.lineNum = request.getParamMapMap().get(ParamConstants.LINENUMBER);
        this.isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        this.nodeId_list = request.getParamMapMap().get(ParamConstants.NODE_ID_LIST);
        this.showType = request.getParamMapMap().get(ParamConstants.SHOWTYPE);
        this.isShowYF = request.getParamMapMap().get(ParamConstants.ISSHOWYF);
        this.isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
        this.cornerShowType = request.getParamMapMap().get(ParamConstants.CORNERSHOWTYPE);
        this.largeSize = request.getParamMapMap().get(ParamConstants.LARGESIZE);
        this.nameShowType = request.getParamMapMap().get(ParamConstants.NAMESHOWTYPE);
        this.showBookNum = request.getParamMapMap().get(ParamConstants.SHOWNUM);
        this.isCycle = request.getParamMapMap().get(ParamConstants.ISCYCLE);
        this.sortType = request.getParamMapMap().get(ParamConstants.SORTTYPE);
        this.showNodeNum = request.getParamMapMap().get(ParamConstants.SHOWNODENUM);
        this.instanceId = request.getParamMapMap().get(ParamConstants.INSTANCEID);
        this.isMarginTop = ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISMARGINTOP), "0");
        this.isMarginBootom = ParamUtil.getIsShowLine(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM), "0");
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getShowNum()
    {
        return showNum;
    }
    
    public String getLineNum()
    {
        return lineNum;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public String getNodeId_list()
    {
        return nodeId_list;
    }
    
    public String getShowType()
    {
        return showType;
    }
    
    public String getIsShowYF()
    {
        return isShowYF;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public String getCornerShowType()
    {
        return cornerShowType;
    }
    
    public String getLargeSize()
    {
        return largeSize;
    }
    
    public String getNameShowType()
    {
        return nameShowType;
    }
    
    public String getShowBookNum()
    {
        return showBookNum;
    }
    
    public String getIsCycle()
    {
        return isCycle;
    }
    
    public String getSortType()
    {
        return sortType;
    }
    
    public String getShowNodeNum()
    {
        return showNodeNum;
    }
    
    public static HorizontalContenRequest getInstance()
    {
        return instance;
    }
    
    public String getInstanceId()
    {
        return instanceId;
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }

    public String getIsMarginBootom()
    {
        return isMarginBootom;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("HorizontalContenRequest [type=")
            .append(type)
            .append(", showNum=")
            .append(showNum)
            .append(", lineNum=")
            .append(lineNum)
            .append(", isShowLine=")
            .append(isShowLine)
            .append(", nodeId_list=")
            .append(nodeId_list)
            .append(", showType=")
            .append(showType)
            .append(", isShowYF=")
            .append(isShowYF)
            .append(", isPaddingTop=")
            .append(isPaddingTop)
            .append(", cornerShowType=")
            .append(cornerShowType)
            .append(", largeSize=")
            .append(largeSize)
            .append(", nameShowType=")
            .append(nameShowType)
            .append(", showBookNum=")
            .append(showBookNum)
            .append(", isCycle=")
            .append(isCycle)
            .append(", sortType=")
            .append(sortType)
            .append(", showNodeNum=")
            .append(showNodeNum)
            .append(", instanceId=")
            .append(instanceId)
            .append(", isMarginTop=")
            .append(isMarginTop)
            .append(", isMarginBootom=")
            .append(isMarginBootom)
            .append("]")
            .toString();
    }
    
}
