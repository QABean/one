package cn.migu.newportal.selection.service.selection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


import com.googlecode.protobuf.format.JsonFormat;

import cn.migu.compositeservice.forumsnsservice.Getmypostlist.GetMyPostListResponse;
import cn.migu.compositeservice.forumsnsservice.Getmypostlist.MSG_Post;
import cn.migu.newportal.cache.bean.book.GetUserPortraitResponse;
import cn.migu.newportal.cache.bean.book.UserInfoSNS;
import cn.migu.newportal.cache.cache.service.PortalPostInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalUserInfoSNSCacheService;
import cn.migu.newportal.cache.cache.service.PortalUserPortraitCacheService;
import cn.migu.newportal.cache.util.GetSpringContext;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.cache.util.PortalConfig;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.ParamKeys;
import cn.migu.newportal.util.constants.SnsConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.BookCommentResponseOuterClass.BookCommentData;
import cn.migu.selection.proto.base.BookCommentResponseOuterClass.BookCommentResponse;
import cn.migu.selection.proto.base.BookCommentResponseOuterClass.Comment;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图书详情页消费者
 * 
 * @author wulinfeng
 * @version C10 2017年5月31日
 * @since SDP V300R003C10
 */
public class BookCommentInfo extends ServiceMethodImpl<BookCommentResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(BookCommentInfo.class.getName());
    
    private static final String METHOD_NAME = "getBookCommentInfo";
    
    public BookCommentInfo()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<BookCommentResponse> v1(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter BookCommentInfo.v1 ,identityId:{} ComponentRequest :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormat.printToString(request));
        }
        BookCommentResponse.Builder builder = BookCommentResponse.newBuilder();
        String pluginCode = request.getParamMapMap().get(ParamConstants.PLUGINCODE);
        builder.setPluginCode(pluginCode);
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        BookCommentData.Builder dataBuilder = BookCommentData.newBuilder();
        
        // 获取请求参数
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        String listType = request.getParamMapMap().get(ParamConstants.LISTTYPE);
        if (StringTools.isEmpty(listType))
            listType = "0";
        String pageNo = request.getParamMapMap().get(ParamConstants.PAGENO);
        if (NumberUtils.toInt(pageNo, -1) < 1)
            pageNo = PropertiesConfig.getProperty("detailPage_bookComment_default_pageNo", "1");
        String pageSize = request.getParamMapMap().get(ParamConstants.PAGESIZE);
        if (NumberUtils.toInt(pageSize, -1) < 1)
            pageSize = PropertiesConfig.getProperty("detailPage_bookComment_default_pageSize", "5");
        
        // 是否显示页面底部横线
        if (null != isShowLine)
        {
            dataBuilder.setIsShowLine(isShowLine);
        }
        
        dataBuilder.setIsMarginTop(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
        dataBuilder
            .setIsMarginBottom(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
        
        try
        {
            String forumId = PortalPostInfoCacheService.getInstance().getBarForumId("1", bid);
            // GetMyPostListRequest.Builder postListBuilder = GetMyPostListRequest.newBuilder();
            String writeCommentUrl = PropertiesConfig.getProperty("bookcomment.writeCommentUrl");
            String bookCommentUrl = PropertiesConfig.getProperty("bookcomment.bookCommentUrl");
            // 从请求消息头里取出用户手机号
            String viewMsisdn = HttpUtil.getHeader(ParamConstants.MSISDN);
            Map<String, String> params = new HashMap<String, String>();
            params.put(PropertiesConfig.getProperty(ParamKeys.forumId), forumId);
            params.put(PropertiesConfig.getProperty(ParamKeys.msisdn), viewMsisdn);
            params.put(PropertiesConfig.getProperty(ParamKeys.PURL), HttpUtil.getRequest().getRequestURL().toString());
            writeCommentUrl = UrlTools.appendParams(writeCommentUrl, params);
            bookCommentUrl = UrlTools.appendParams(bookCommentUrl, params);
            dataBuilder.setWriteCommentUrl(StringUtils.defaultString(writeCommentUrl));
            dataBuilder.setBookCommentUrl(StringUtils.defaultString(bookCommentUrl));
            // postListBuilder.setMsisdn(StringUtils.defaultString(viewMsisdn));
            // postListBuilder.setMyType("0");
            // postListBuilder.setStart();
            // postListBuilder.setCount(NumberUtils.toInt(pageSize));
            dataBuilder.setTotalNum("0");
            GetMyPostListResponse postListResponse = GetSpringContext.getCompositeEngine().getMyPostList(viewMsisdn,
                "0",
                (NumberUtils.toInt(pageNo) - 1) * NumberUtils.toInt(pageSize),
                NumberUtils.toInt(pageSize));
            if (null != postListResponse && postListResponse.getTotalCnt() > 0
                && null != postListResponse.getPostListList() && postListResponse.getPostListList().size() > 0)
            {
                dataBuilder.setTotalNum(StringTools.getString(postListResponse.getTotalCnt() + "", "0"));
                List<Comment> commentList = new ArrayList<>();
                Comment.Builder comment = Comment.newBuilder();
                for (MSG_Post post : postListResponse.getPostListList())
                {
                    String s = post.getAuthorId();
                    UserInfoSNS userInfoSNS = PortalUserInfoSNSCacheService.getInstance().getUserInfoSNS(s, null);
                    GetUserPortraitResponse userPortrait =
                        PortalUserPortraitCacheService.getInstance().getUserPortrait(s);
                    comment.setPosterPic(StringUtils.defaultString(getUserNewHeadImgUrl(userInfoSNS, userPortrait)));
                    if (null != userInfoSNS)
                        comment.setPostId(StringUtils.defaultString(userInfoSNS.getMsisdn()));
                    comment.setPosterName(StringTools.getString(post.getAuthorNickName(), "匿名"));
                    comment.setPosterIdentity(StringTools.getString(post.getAuthorDegree() + "", "0"));
                    comment.setPosterTime(StringUtils.defaultString(getFormatTime(post.getPublishTime())));
                    comment.setPosterContent(StringUtils.defaultString(post.getPCont()));
                    comment.setUpDownCount(StringTools.getString(post.getAbetNum() + "", "0"));
                    comment.setUpDownUrl(StringUtils.defaultString(
                        getUpDownUrl(post.getPid() + "", HttpUtil.getRequest().getRequestURL().toString())));
                    comment.setReplyNum(StringTools.getString(post.getReplyCnt() + "", "0"));
                    comment.setReplyUrl(StringUtils.defaultString(
                        getReplyUrl(post.getPid() + "", forumId, HttpUtil.getRequest().getRequestURL().toString())));
                    comment.setIsApprovedByMe(StringTools.getString(post.getIsAbeted() + "", "0"));
                    commentList.add(comment.build());
                }
                
                dataBuilder.addAllCommentList(commentList);
            }
            
            // 判断是否ajax请求
            String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
            if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            builder.setData(dataBuilder);
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            // 设置成功返回码
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
        catch (Exception e)
        {
            logger.error("BookCommentServiceImpl-BookCommentInfo  error identityId:{},e:{}",
                CommonHttpUtil.getIdentity(), e);
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("BookCommentInfo.v1 ,identityId:{} BookCommentResponse :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormat.printToString(builder.build()));
        }
        return new InvokeResult<BookCommentResponse>(builder.build());
    }
    
    /**
     * 获取指定用户头像
     *
     * @author zhudinglei
     * @version [UES121, 2016年12月8日]
     * @since [BR002109]
     * @return
     */
    private String getUserNewHeadImgUrl(UserInfoSNS userInfoSNS, GetUserPortraitResponse response)
    {
        // 当前服务器根路径
        String basePath = PropertiesConfig.getProperty("group.userhead.url.prefix", "http://10.211.95.78:8081/")
            + SnsConstants.HEAD_IMAGE_PARENT_PATH + "/";
        
        // 默认图片
        String imageName = "defaultmigu.jpg";
        
        // 响应为空，或者响应中resultCode为24063，判断性别可见及性别返回系统图片(未进入判断分支全部走默认图片逻辑)
        if (Util.isEmpty(response) || StringTools.isEq("24063", response.getResultCode()))
        {
            // 父字段不存在(默认性别可见) or 父字段存在,性别策略不存在(默认为1 性别可见)
            // or 性别为可见 or 用户为当前用户
            if (null != userInfoSNS)
            {
                if (Util.isEmpty(userInfoSNS.getPrivacyInfo())
                    || StringTools.isEmpty(userInfoSNS.getPrivacyInfo().getSexVisible())
                    || StringTools.isEq(SystemConstants.SEX_VISIBLE_SET, userInfoSNS.getPrivacyInfo().getSexVisible())
                    || StringTools.isEq(userInfoSNS.getMsisdn(), HttpUtil.getHeader("moblie")))
                {
                    String sex = userInfoSNS.getSex();
                    
                    if (StringTools.isEq(SnsConstants.SEX_TYPE_MALE, sex))
                    {
                        imageName = "male1.jpg";
                    }
                    else if (StringTools.isEq(SnsConstants.SEX_TYPE_FEMALE, sex))
                    {
                        imageName = "female1.jpg";
                    }
                }
            }
        }
        else
        {
            // 自定义头像
            if (StringTools.isEq(SystemConstants.USER_DEFINED_IMGTYPE, response.getHeadIdType()))
            {
                // 审核中，或审核通过
                if (StringTools.isEq(SystemConstants.IMG_UNDER_REVIEW, response.getAuditStatus())
                    || StringTools.isEq(SystemConstants.IMG_PASS, response.getAuditStatus()))
                {
                    // 获取小图，走action流输出
                    imageName = response.getSmallAvatarId();
                    String url = UrlTools.appendParam(PropertiesConfig.getProperty(ParamKeys.GET_HEAD_URL),
                        "headIconId",
                        imageName);
                    return url;
                }
            }
            // 非自定义头像
            else if (StringTools.isEq(SystemConstants.SYSTEM_IMGTYPE, response.getHeadIdType()))
            {
                // 父字段不存在(默认性别可见) or 父字段存在,性别策略不存在(默认为1 性别可见)
                // or 性别为可见 or 用户为当前用户
                if (null != userInfoSNS)
                {
                    if (Util.isEmpty(userInfoSNS.getPrivacyInfo())
                        || StringTools.isEmpty(userInfoSNS.getPrivacyInfo().getSexVisible())
                        || StringTools.isEq(SystemConstants.SEX_VISIBLE_SET,
                            userInfoSNS.getPrivacyInfo().getSexVisible())
                        || StringTools.isEq(userInfoSNS.getMsisdn(), HttpUtil.getHeader("moblie")))
                    {
                        // 系统头像存在
                        if (StringTools.isNotEmpty(response.getHeadId()))
                        {
                            imageName = response.getHeadId();
                        }
                        else
                        {
                            String sex = userInfoSNS.getSex();
                            
                            if (StringTools.isEq(SnsConstants.SEX_TYPE_MALE, sex))
                            {
                                imageName = "male1.jpg";
                            }
                            else if (StringTools.isEq(SnsConstants.SEX_TYPE_FEMALE, sex))
                            {
                                imageName = "female1.jpg";
                            }
                        }
                    }
                }
            }
        }
        
        return basePath + imageName;
    }
    
    /**
     * 获取顶url
     * 
     * @param postId:0驳,1顶
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getUpDownUrl(String postId, String purl)
    {
        
        Map<String, String> params = new HashMap<String, String>();
        params.put(PropertiesConfig.getProperty(ParamKeys.postId), postId);
        params.put(PropertiesConfig.getProperty(ParamKeys.PURL), purl);
        params.put(PropertiesConfig.getProperty(ParamKeys.abetOrOpposeType), "1");
        String url = UrlTools.appendParams(PropertiesConfig.getProperty(ParamKeys.abetOrOpposePostUrl), params);
        
        return url;
    }
    
    /**
     * 获取回帖地址
     */
    private String getReplyUrl(String postId, String forumId, String purl)
    {
        // 配置帖子列表地址，当没有配置时跑新互动
        // BR002690 考虑https
        String url = UrlTools.replaceUrlHttpToEmptyWhenSchemeIsHttps(
            PortalConfig.get("url/bar_list_title_sns_url", PropertiesConfig.getProperty(ParamKeys.postUrl)));
        Map<String, String> params = new HashMap<String, String>();
        params.put(PropertiesConfig.getProperty(ParamKeys.postId), postId);
        params.put(PropertiesConfig.getProperty(ParamKeys.forumId), forumId);
        params.put(PropertiesConfig.getProperty(ParamKeys.PURL), purl);
        url = UrlTools.appendParams(url, params);
        
        return url;
    }
    
    private String getFormatTime(String publishTime)
    {
        Date d = new Date();
        if (new SimpleDateFormat(DateTools.DATE_FORMAT2_8).format(d)
            .equals(DateTools.timeTransform(publishTime, DateTools.DATE_FORMAT_14, DateTools.DATE_FORMAT2_8)))
        {
            return "今天 " + DateTools.timeTransform(publishTime, DateTools.DATE_FORMAT_14, DateTools.DATE_FORMAT_19);
        }
        else if (new SimpleDateFormat(DateTools.DATE_FORMAT_YEAR).format(d)
            .equals(DateTools.timeTransform(publishTime, DateTools.DATE_FORMAT_14, DateTools.DATE_FORMAT_YEAR)))
        {
            return DateTools.timeTransform(publishTime, DateTools.DATE_FORMAT_14, DateTools.DATE_PATTERN_10);
        }
        else
        {
            return DateTools.timeTransform(publishTime, DateTools.DATE_FORMAT_14, DateTools.DATE_FORMAT_10);
        }
        
    }
    
}
