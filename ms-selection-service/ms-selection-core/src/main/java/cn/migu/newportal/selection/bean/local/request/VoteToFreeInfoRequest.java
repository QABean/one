package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.bean.common.BaseRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;

import java.util.Map;

/**
 * @author wangqiang
 * @version C10 2018年04月09日
 * @since SDP V300R003C10
 */
public class VoteToFreeInfoRequest extends BaseRequest
{
    // 投票按钮描述
    private String tpbuttonDesc;
    // 投票活动id
    private String ballotId;
    // 投票倍数：单选框，1:1;2:2;3:3;4:4;5:5
    private int largeSize;
    // 专区和内容
    private String nodeContent;
    // 显示类型:1：图书名称;3：图书封面;2：图书长推荐语;4：图书短推荐语;5:显示wap2.0推荐语;
    private String nameShowType;
    // 排序依据:0.图书更新时间倒序；1.图书上架时间倒序； 2.图书入库时间倒序；3.排序值升序
    private String sortType;

    public VoteToFreeInfoRequest(Map<String, String> paramMap)
    {
        super(paramMap);
        init(paramMap);
    }

    private void init(Map<String, String> paramMap)
    {
        if (Util.isNotEmpty(paramMap))
        {
            this.tpbuttonDesc = paramMap.get(ParamConstants.TPBUTTONDESC);
            this.ballotId = paramMap.get(ParamConstants.BALLOTID);
            this.largeSize = ParamUtil.getVoteLargize(paramMap.get(ParamConstants.LARGESIZE));
            this.nodeContent = paramMap.get(ParamConstants.TEXTWITHNODECONTENTBTN);
            this.sortType = ParamUtil.getSortType(paramMap.get(ParamConstants.SORTTYPE), "1");
        }
    }

    public String getTpbuttonDesc()
    {
        return tpbuttonDesc;
    }

    public void setTpbuttonDesc(String tpbuttonDesc)
    {
        this.tpbuttonDesc = tpbuttonDesc;
    }

    public String getBallotId()
    {
        return ballotId;
    }

    public void setBallotId(String ballotId)
    {
        this.ballotId = ballotId;
    }

    public int getLargeSize()
    {
        return largeSize;
    }

    public void setLargeSize(int largeSize)
    {
        this.largeSize = largeSize;
    }

    public String getNodeContent()
    {
        return nodeContent;
    }

    public void setNodeContent(String nodeContent)
    {
        this.nodeContent = nodeContent;
    }

    public String getNameShowType()
    {
        return nameShowType;
    }

    public void setNameShowType(String nameShowType)
    {
        this.nameShowType = nameShowType;
    }

    public String getSortType()
    {
        return sortType;
    }

    public void setSortType(String sortType)
    {
        this.sortType = sortType;
    }
}
