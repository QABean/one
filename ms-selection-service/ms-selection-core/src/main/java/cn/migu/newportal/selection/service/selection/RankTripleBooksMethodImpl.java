package cn.migu.newportal.selection.service.selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang3.math.NumberUtils;


import cn.migu.compositeservice.contservice.GetRank.GetRankRequest;
import cn.migu.newportal.cache.bean.RankInfo;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.BookRank;
import cn.migu.newportal.cache.cache.service.BookUVCacheService;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookRankCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalGetBookRankCacheService;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RankTripleBooksResponseOuterClass.NewRankList;
import cn.migu.selection.proto.base.RankTripleBooksResponseOuterClass.RankTripleBooksData;
import cn.migu.selection.proto.base.RankTripleBooksResponseOuterClass.RankTripleBooksResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 排行三封面（rank_triplebooks）
 *
 * @author yuhaihan
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
public class RankTripleBooksMethodImpl extends ServiceMethodImpl<RankTripleBooksResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(RankTripleBooksMethodImpl.class);
    
    private static final String METHOD_NAME = "getRankTriplebooks";
    
    // 排行榜类型，ues配置，1:评论榜;2:点击榜;3:订购榜;5:搜索榜;6:下载榜;10:月票榜;14:打赏榜;23:爱读榜;30:偷书榜;31:ugc热书榜;32:ugc新书榜;33:追更榜;34:完结榜;35:新书榜，默认1
    private static final String RANKTYPE_ENUM = "1|2|3|5|6|10|14|23|30|31|32|33|34|35";
    
    // 内容大类:0 全部，1 女频，2 出版，3 原创，默认0
    private static final String RANKRULE_ENUM = "[0-3]";
    
    // 内容类型:1图书类型，10全站类型 默认1
    private static final String BOOKITEMTYPE_ENUM = "1|10";
    
    // 排行类型 1:三图横排; 2:三图纵排，默认1
    private static final String RANKSHOWTYPE_ENUM = "[1-2]";
    
    // 展示类型 1:作家名; 2:点击量; 3:两行书名，默认1
    private static final String SHOWTYPE_ENUM = "[1-3]";
    
    // 点击数据来源 1:点击量; 2:UV, 默认1
    private static final String CLICKNUMTYPE_ENUM = "[1-2]";
    
    // 排列时间类型 1.日 2.周 3.月 4.总，默认2
    private static final String RANKDATETYPE_ENUM = "[1-4]";
    
    public RankTripleBooksMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 排行三封面业务处理
     *
     * @author yuhaihan
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<RankTripleBooksResponse> getRankTriplebooks(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter RankTripleBooksResponse.getRankTriplebooks ,identityId:{},ComponentRequest:{} ",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        Map<String, String> paramMap = request.getParamMapMap();
        RankTripleBooksResponse.Builder builder = RankTripleBooksResponse.newBuilder();
        builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));
        try
        {
            builder.setData(getRankTripleBooksData(paramMap, builder));
            // 设置组件可见
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        }
        catch (Exception e)
        {
            LOGGER.error(
                "Error  RankTripleBooksResponse.getRankTriplebooks() Error,identityId:{}, ComponentRequest:{},e:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request),
                e.getMessage());
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.OTHER_DEFAULT_FAIL, ParamConstants.FALSE);
            return new InvokeResult<RankTripleBooksResponse>(builder.build());
        }
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit RankTripleBooksResponse.getRankTriplebooks,identityId:{}, ComponentRequest: {}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        return new InvokeResult<RankTripleBooksResponse>(builder.build());
        
    }
    
    /**
     * 构造RankTripleBooksData数据
     *
     * @author yuhaihan
     * @param param
     * @param builder
     */
    private RankTripleBooksData getRankTripleBooksData(Map<String, String> param, RankTripleBooksResponse.Builder builder)
    {
        RankTripleBooksData.Builder dataBuilder = RankTripleBooksData.newBuilder();
        // 设置组件样式
        BeanMergeUtils.setComponentStyle(dataBuilder, param);
        // 排行类型 1:三图横排; 2:三图纵排，默认1
        dataBuilder.setRankShowType(
            RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.RANKSHOWTYPE), "1", RANKSHOWTYPE_ENUM));
        // 展示类型 1:作家名; 2:点击量; 3:两行书名，默认1
        dataBuilder
            .setShowType(RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.SHOWTYPE), "1", SHOWTYPE_ENUM));
        // 点击量文本
        dataBuilder
            .setClickValue(StringTools.nvl2(param.get(ParamConstants.CLICKVALUE), ParamConstants.DEF_CLICKVALUE));
        // 点击数据来源 1:点击量; 2:UV, 默认1
        String clickNumType =
            RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.CLICKNUMTYPE), "1", CLICKNUMTYPE_ENUM);
        dataBuilder.setClickNumType(clickNumType);
        // 排列时间类型 1.日 2.周 3.月 4.总，默认2
        String rankDateType =
            RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.RANKDATETYPE), "2", RANKDATETYPE_ENUM);
        // 图书UV放大倍数
        String multiple = param.get(ParamConstants.MULTIPLE);
        Map<String, String> bookDetailMap = new HashMap<>();
        // 排行列表
        List<RankInfo> ranks = getRanks(param, rankDateType);
        if (ranks != null && ranks.size() > 0)
        {
            int num = 0;
            for (RankInfo rankInfo : ranks)
            {
                if (num == 3)
                {
                    break;
                }
                if (Util.isEmpty(rankInfo))
                {
                    continue;
                }
                String bookId = rankInfo.getBookId();
                if (Util.isEmpty(bookId))
                {
                    continue;
                }
                num++;
                BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
                if (Util.isNotEmpty(bookItem) && Util.isNotEmpty(bookItem.getStatus()))
                {
                    NewRankList.Builder newRankList = NewRankList.newBuilder();
                    newRankList.setBookName(StringTools.nvl(bookItem.getBookName()));
                    newRankList.setBookCover(StringTools.nvl(BookUtils.getBookCover(bookItem)));
                    // 图书简介取得是长描述
                    newRankList.setBookBrief(StringTools.nvl(bookItem.getLongDescription()));
                    bookDetailMap.put(ParamConstants.BID, bookId);
                    newRankList.setBookUrl(StringTools.nvl(PresetPageUtils.getBookDetailPage(bookDetailMap)));
                    // 获取作者信息
                    AuthorInfo authorInfo =
                        PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                    if (Util.isNotEmpty(authorInfo))
                    {
                        LOGGER.debug("RankTripleBooksResponse.getRankTripleBooksData getAuthorInfo{}:"
                            ,authorInfo.getAuthorName());
                        newRankList.setAuthorName(StringTools.nvl(authorInfo.getAuthorPenName()));
                    }
                    else
                    {
                        newRankList.setAuthorName(ParamConstants.DEFAULTAUTHORNAME);
                    }
                    newRankList.setRankChange(getRankChange(rankInfo));
                    // 点击量 * 方法倍数
                    newRankList.setBookClickNum(
                        getBookClickNum(clickNumType, bookId, rankDateType) * getBookMultiple(multiple));
                    dataBuilder.addNewRankList(newRankList);
                }
            }
            
        }
        return dataBuilder.build();
    }
    
    /**
     * 获取图书放大倍数
     *
     * @author yuhaihan
     * @param multiple
     */
    private int getBookMultiple(String multiple)
    {
        int num = 1;
        int multipleNum = NumberUtils.toInt(multiple, 1);
        if (multipleNum > 0)
        {
            num = multipleNum;
        }
        return num;
    }
    
    /**
     * 获取点击量
     *
     * @author yuhaihan
     * @param clickNumType
     * @param bookId
     */
    private int getBookClickNum(String clickNumType, String bookId, String rankDateType)
    {
        int bookClickNum = ParamConstants.BOOKCLICKNUM_ZERO;
        if (clickNumType.equals(ParamConstants.CLICKNUMTYPE_ONE))
        {
            // 获取BOOKRANK
            BookRank bookRank = PortalGetBookRankCacheService.getInstance().getBookRankInfo(bookId, "", rankDateType);
            if (Util.isNotEmpty(bookRank))
            {
                bookClickNum = Integer.parseInt(bookRank.getClickValue());
            }
        }
        else
        {
            String bookUV = BookUVCacheService.getInstance().getBookUV(bookId);
            if (Util.isNotEmpty(bookUV))
            {
                bookClickNum = Integer.parseInt(bookUV);
            }
        }
        return bookClickNum;
    }
    
    /**
     * 排行变化,1:上升;0:不变;-1:下降s
     *
     * @author yuhaihan
     * @param rankInfo
     */
    private String getRankChange(RankInfo rankInfo)
    {
        String str = ParamConstants.UNCHANGED;
        int sort = StringTools.toInt(rankInfo.getSort(), 0);
        int lastSort = StringTools.toInt(rankInfo.getLastSort(), 0);
        if (sort < lastSort)
        {
            str = ParamConstants.RISE;
        }
        else if (sort > lastSort)
        {
            str = ParamConstants.DECLINE;
        }
        return str;
    }
    
    /**
     * 获取排行列列表
     *
     * @author yuhaihan
     * @param param
     */
    private List<RankInfo> getRanks(Map<String, String> param, String rankDateType)
    {
        // 排行榜类型:1:评论榜;2:点击榜;3:订购榜;5:搜索榜;6:下载榜;10:月票榜;14:打赏榜;23:爱读榜;30:偷书榜;31:ugc热书榜;32:ugc新书榜;33:追更榜;34:完结榜;35:新书榜，默认1
        String rankType = RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.RANKTYPE), "1", RANKTYPE_ENUM);
        // 内容大类:0 全部，1 女频，2 出版，3 原创，默认0
        String rankRule = RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.RANKRULE), "0", RANKRULE_ENUM);
        // 内容类型:1图书类型，10全站类型
        String bookItemType =
            RequestParamCheckUtils.checkEnumStr(param.get(ParamConstants.BOOKITEMTYPE), "1", BOOKITEMTYPE_ENUM);
        
        GetRankRequest.Builder request = GetRankRequest.newBuilder();
        request.setNodeId(StringTools.nvl(param.get(ParamConstants.NODEID)));
        request.setRankStart(ParamConstants.PAGE_NO);
        request.setRankCount(ParamConstants.COUNT_THREE);
        request.setRankType(rankType);
        request.setRankRule(rankRule);
        request.setBookItemType(bookItemType);
        request.setRankDateType(rankDateType);
        
        List<RankInfo> rankList = null;
        try
        {
            rankList = PortalBookRankCacheService.getInstance().getRankList(request.build());
        }
        catch (Exception e)
        {
            LOGGER.error("RankTripleBooksMethodImpl getRanks failed,identityId:{},request:{},e:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request),
                e);
        }
        
        return rankList;
    }
    
}
