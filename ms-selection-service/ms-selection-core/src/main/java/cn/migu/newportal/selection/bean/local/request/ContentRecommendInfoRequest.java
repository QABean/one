package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.util.bean.common.BaseRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;

import java.util.Map;

/**
 *
 *
 * @author zhangzhen
 * @version C10 2018年02月11日 
 * @since SDP V300R003C10
 */
public class ContentRecommendInfoRequest extends BaseRequest
{

    private static final long serialVersionUID = -928464121512529775L;

    //展示样式: 默认:3;枚举值:1:一行书名+读过人数;2:一行书名+作者;3:两行书名
    private String type;
    //每页显示条数
    private String pageSize;
    //数据来源: 1:UES读过还读;2:同系列推荐;3:UES买过还买过;4:ues同作者作品;5:BI读过还读;6:BI买过还买过;7:BI浏览还浏览; 默认1
    private String dataFrom;
    //标题内容，默认：同类推荐
    private String title;
    //排行榜日期类型：1:日榜;2:周榜;3:月榜;4:总榜;5:年榜
    private String rankDateType;
    //排行榜依据：排行榜依据，1:评论榜;2:点击榜;3:销量榜;4:推荐榜;5:搜索榜;6:下载榜;7:献花榜;8:收藏榜;9:上升榜;10:月票榜
    private String rankStandard;
    //排行榜推荐级别：1:全站;2:一级分类;3:二级分类
    private String recommendLevel;
    //图书id
    private String bid;
    //页码
    private String pageNo;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(String pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getDataFrom()
    {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom)
    {
        this.dataFrom = dataFrom;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getRankDateType()
    {
        return rankDateType;
    }

    public void setRankDateType(String rankDateType)
    {
        this.rankDateType = rankDateType;
    }

    public String getRankStandard()
    {
        return rankStandard;
    }

    public void setRankStandard(String rankStandard)
    {
        this.rankStandard = rankStandard;
    }

    public String getRecommendLevel()
    {
        return recommendLevel;
    }

    public void setRecommendLevel(String recommendLevel)
    {
        this.recommendLevel = recommendLevel;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(String pageNo)
    {
        this.pageNo = pageNo;
    }

    public ContentRecommendInfoRequest(Map<String, String> paramMap)
    {
        super(paramMap);
        init(paramMap);
    }

    private void init(Map<String, String> paramMap)
    {
        if (Util.isNotEmpty(paramMap))
        {

            //展示样式: 默认:3;枚举值:1:一行书名+读过人数;2:一行书名+作者;3:两行书名
            type = paramMap.get(ParamConstants.TYPE);
            if (!StringTools.isEq("1", type) && !StringTools.isEq("2", type))
            {
                type = "3";
            }

            //每页显示条数
            pageSize = paramMap.get(ParamConstants.PAGESIZE);

            //数据来源: 1:UES读过还读;2:同系列推荐;3:UES买过还买过;4:ues同作者作品;5:BI读过还读;6:BI买过还买过;7:BI浏览还浏览; 默认1
            dataFrom = paramMap.get(ParamConstants.DATAFROM);

            //标题内容，默认：同类推荐
            title = paramMap.get(ParamConstants.TITLE);

            //排行榜日期类型：1:日榜;2:周榜;3:月榜;4:总榜;5:年榜
            rankDateType = paramMap.get(ParamConstants.RANKDATETYPE);

            //排行榜依据：排行榜依据，1:评论榜;2:点击榜;3:销量榜;4:推荐榜;5:搜索榜;6:下载榜;7:献花榜;8:收藏榜;9:上升榜;10:月票榜
            rankStandard = paramMap.get(ParamConstants.RANKSTANDARD);

            //图书id
            bid = paramMap.get(ParamConstants.BID);

            pageNo = paramMap.get(ParamConstants.PAGENO);
        }
    }

    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer("ContentRecommendInfoRequest{");
        sb.append("type='").append(type).append('\'');
        sb.append(", pageSize='").append(pageSize).append('\'');
        sb.append(", dataFrom='").append(dataFrom).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", rankDateType='").append(rankDateType).append('\'');
        sb.append(", rankStandard='").append(rankStandard).append('\'');
        sb.append(", recommendLevel='").append(recommendLevel).append('\'');
        sb.append(", bid='").append(bid).append('\'');
        sb.append(", pageNo='").append(pageNo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
