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
public class RecommendContentRequest  extends BaseRequest
{
    private static final long serialVersionUID = 6027788907958199643L;

    ///图书id
    private String bid;
    //分享描述
    private String shareDesc;
    //分享标题
    private String shareTitle;
    //评论页跳转地址（配置老门户地址）
    private String goCommentUrl;
    //打赏页跳转地址（配置老门户地址）
    private String goRewardUrl;
    //分享类型： 1:和飞信;2:微信好友;3:朋友圈;4:微博;5:二维码;6:QQ;7:QQ空间;8:其他;  默认：1,2,3,4,5,6,7,8
    private String shareChange;
    //分享成功跳转地址
    private String successLink;
    //分享成功是否跳转页面 1:是;2:否; 默认2
    private String shareSuccessLink;
    //跳转周期设置 : 1:总共一次;2:一天一次;3:不限次数; 默认1
    private String linkCycle;


    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getShareDesc()
    {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc)
    {
        this.shareDesc = shareDesc;
    }

    public String getShareTitle()
    {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle)
    {
        this.shareTitle = shareTitle;
    }

    public String getShareChange()
    {
        return shareChange;
    }

    public void setShareChange(String shareChange)
    {
        this.shareChange = shareChange;
    }

    public String getSuccessLink()
    {
        return successLink;
    }

    public void setSuccessLink(String successLink)
    {
        this.successLink = successLink;
    }

    public String getShareSuccessLink()
    {
        return shareSuccessLink;
    }

    public void setShareSuccessLink(String shareSuccessLink)
    {
        this.shareSuccessLink = shareSuccessLink;
    }

    public String getLinkCycle()
    {
        return linkCycle;
    }

    public void setLinkCycle(String linkCycle)
    {
        this.linkCycle = linkCycle;
    }

    public String getGoCommentUrl()
    {
        return goCommentUrl;
    }

    public void setGoCommentUrl(String goCommentUrl)
    {
        this.goCommentUrl = goCommentUrl;
    }

    public String getGoRewardUrl()
    {
        return goRewardUrl;
    }

    public void setGoRewardUrl(String goRewardUrl)
    {
        this.goRewardUrl = goRewardUrl;
    }

    public RecommendContentRequest(Map<String, String> paramMap)
    {
        super(paramMap);
        init(paramMap);
    }

    private void init(Map<String, String> paramMap)
    {
        if (Util.isNotEmpty(paramMap))
        {
            bid = paramMap.get(ParamConstants.BID);

            shareDesc = paramMap.get(ParamConstants.SHAREDESC);

            shareTitle = paramMap.get(ParamConstants.SHARETITLE);

            //分享类型： 1:和飞信;2:微信好友;3:朋友圈;4:微博;5:二维码;6:QQ;7:QQ空间;8:其他; 默认：1,2,3,4,5,6,7,8
            shareChange = paramMap.get(ParamConstants.SHARECHANGE);
            boolean flag = false;
            if(StringTools.isNotEmpty(shareChange))
            {
                flag = shareChange.matches("^[1-8,()]+$");
            }
            shareChange = flag ? shareChange : ParamConstants.SHARECHANGE_DEFAULT;

            successLink = paramMap.get(ParamConstants.SUCCESSLINK);

            //分享成功是否跳转页面 1:是;2:否; 默认2
            shareSuccessLink = paramMap.get(ParamConstants.SHARESUCCESSLINK);
            shareSuccessLink = StringTools.isEq("1", shareSuccessLink) ? shareSuccessLink : "2";

            //跳转周期设置 : 1:总共一次;2:一天一次;3:不限次数; 默认1
            linkCycle = paramMap.get(ParamConstants.LINKCYCLE);
            if (!StringTools.isEq("2", linkCycle) && !StringTools.isEq("3", linkCycle))
            {
                linkCycle = "1";
            }

            goCommentUrl = paramMap.get(ParamConstants.GOCOMMENTURL);

            goRewardUrl = paramMap.get(ParamConstants.GOREWARDURL);
        }
    }
}
