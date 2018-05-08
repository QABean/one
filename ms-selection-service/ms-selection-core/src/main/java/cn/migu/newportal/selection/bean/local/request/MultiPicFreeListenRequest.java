package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.bean.common.BaseRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;

import java.util.Map;

/**
 * @author wangqiang
 * @version C10 2018年03月15日
 * @since SDP V300R003C10
 */
public class MultiPicFreeListenRequest extends BaseRequest
{
    private String pageNo;
    private String zqSize;
    private String isShowLine;
    private String dataSrc;
    private String startFrom;
    private String cornerShowType;
    private String showType;
    private String largeSize;
    private String showNum;
    private String node_alias;
    private String nameShowType;
    private String nid;

    /**
     * 封装参数的构造函数
     *
     * @param paramMap 参数
     * @author wangqiang
     */
    public MultiPicFreeListenRequest(Map<String, String> paramMap)
    {
        super(paramMap);
        init(paramMap);
    }

    /**
     * 对参数进行初始化,并进行一些校验
     *
     * @param paramMap
     * @author wangqiang
     */
    private void init(Map<String, String> paramMap)
    {
        if (Util.isNotEmpty(paramMap))
        {
            // 数据来源，（1.自配，2.专区）
            this.dataSrc =ParamUtil.checkDataSrc(paramMap.get(ParamConstants.DATASRC));
            // 是否播放最新章节（1.播放第一章节，2.播放最新章节,）
            this.startFrom = ParamUtil.checkShowType(paramMap.get(ParamConstants.STARTFROM));
            // 角标展示（1:免费;2:限免;3:会员;4:完本;5:名家;6:上传）,可多选
            this.cornerShowType =paramMap.get(ParamConstants.CORNERSHOWTYPE);
            // 展示样式（1:书名+点击量;2:书名+主播，默认1）
            this.showType =ParamUtil.checkShowType(paramMap.get(ParamConstants.SHOWTYPE));
            // 图书UV放大倍数
            this.largeSize = paramMap.get(ParamConstants.LARGESIZE);
            // 专区下图书列表显示数
            this.showNum = paramMap.get(ParamConstants.SHOWNUM);
            // 图书名称显示类型:1：图书书名,2：长推荐语,4：短推荐语,5：WAP2.0推荐语,6：适配推荐语
            this.nameShowType = paramMap.get(ParamConstants.NAMESHOWTYPE);
            // 专区id
            this.nid = paramMap.get(ParamConstants.NID);
        }
    }

    public String getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(String pageNo)
    {
        this.pageNo = pageNo;
    }

    public String getZqSize()
    {
        return zqSize;
    }

    public void setZqSize(String zqSize)
    {
        this.zqSize = zqSize;
    }

    @Override
    public String getIsShowLine()
    {
        return isShowLine;
    }

    @Override
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }

    public String getDataSrc()
    {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc)
    {
        this.dataSrc = dataSrc;
    }

    public String getStartFrom()
    {
        return startFrom;
    }

    public void setStartFrom(String startFrom)
    {
        this.startFrom = startFrom;
    }

    public String getCornerShowType()
    {
        return cornerShowType;
    }

    public void setCornerShowType(String cornerShowType)
    {
        this.cornerShowType = cornerShowType;
    }

    public String getShowType()
    {
        return showType;
    }

    public void setShowType(String showType)
    {
        this.showType = showType;
    }

    public String getLargeSize()
    {
        return largeSize;
    }

    public void setLargeSize(String largeSize)
    {
        this.largeSize = largeSize;
    }

    public String getShowNum()
    {
        return showNum;
    }

    public void setShowNum(String showNum)
    {
        this.showNum = showNum;
    }

    public String getNode_alias()
    {
        return node_alias;
    }

    public void setNode_alias(String node_alias)
    {
        this.node_alias = node_alias;
    }

    public String getNameShowType()
    {
        return nameShowType;
    }

    public void setNameShowType(String nameShowType)
    {
        this.nameShowType = nameShowType;
    }

    public String getNid()
    {
        return nid;
    }

    public void setNid(String nid)
    {
        this.nid = nid;
    }
}
