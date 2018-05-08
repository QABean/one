package cn.migu.newportal.selection.service.selection;

import cn.migu.compositeservice.contservice.Common.RankInfo;
import cn.migu.compositeservice.contservice.GetBIRank.GetBIRankResponse;
import cn.migu.compositeservice.contservice.GetBIRecommendBooks.BIRecommendItem;
import cn.migu.compositeservice.contservice.GetBIRecommendBooks.GetBIRecommendBooksResponse;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.UserInfoSNS;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.PortalBIRankRespCacheService;
import cn.migu.newportal.cache.cache.service.PortalBIRecommendCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalGeneralInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalUserInfoSNSCacheService;
import cn.migu.newportal.cache.manager.book.BookManager;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PresetPageUtils;
import cn.migu.newportal.cache.util.RequestParamCheckUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.GuessLikeListResponseOuterClass.GuessLikeListBook;
import cn.migu.selection.proto.base.GuessLikeListResponseOuterClass.GuessLikeListData;
import cn.migu.selection.proto.base.GuessLikeListResponseOuterClass.GuessLikeListResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 猜你喜欢组件（guesslike_list）
 *
 * @author fengjiangtao
 * @date 2018/5/2 下午 11:46
 */
public class GuessLikeListMethodImpl extends ServiceMethodImpl<GuessLikeListResponse, ComponentRequest>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuessLikeListMethodImpl.class.getName());

    private static final String METHOD_NAME = "guessLikeList";

    /**
     * 展示样式正则校验，1:两行书名;2:一行书名+读过人数;3:一行书名+作者
     */
    private static final String SHOW_TYPE_REGEX = "[1-3]";

    /**
     * 展示样式默认值，1:两行书名;
     */
    private static final String SHOW_TYPE_DEFAULT = "1";

    /**
     * 数据来源正则校验，1:按理由;2:按名家;3:BI实时接口
     */
    private static final String DATA_FROM_REGEX = "[1-3]";

    /**
     * 数据来源默认值，1:按理由;
     */
    private static final String DATA_FROM_DEFAULT = "1";

    /**
     * 数据来源默认值，2:按名家;
     */
    private static final String DATA_FROM_FAMOUS = "2";

    /**
     * 加密阅读号原文
     */
    private static final char[] CHAR_ARR =
        "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public GuessLikeListMethodImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 获取组件响应数据
     *
     * @param controller 服务控制器
     * @param request 封装请求入参的对象
     * @return cn.migu.wheat.service.InvokeResult<cn.migu.selection.proto.base.ContentRecommendResponseOuterClass
     * .ContentRecommendResponse>
     * @author fengjiangtao
     */
    @ImplementMethod
    public InvokeResult<GuessLikeListResponse> guessLikeList(ServiceController controller, ComponentRequest request)
    {

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter GuessLikeListMethodImpl.guessLikeList，identityId:{}, request:{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        GuessLikeListResponse.Builder builder = GuessLikeListResponse.newBuilder();
        Map<String, String> paramMap = request.getParamMapMap();
        String msisdn = CommonHttpUtil.getIdentity();

        try
        {
            builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));
            GuessLikeListData data = createGuessLikeListData(paramMap, msisdn);
            builder.setData(data);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        }
        catch (Throwable e)
        {
            LOGGER.error("GuessLikeListMethodImpl.guessLikeList has error, identityId:{}, request:{}, e:{}",
                msisdn,
                JsonFormatUtil.printToString(request),
                e);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.OTHER_DEFAULT_FAIL, ParamConstants.FALSE);
            return new InvokeResult<>(builder.build());
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("exit GuessLikeListMethodImpl.guessLikeList，identityId:{}, request:{}, response:{}"
                ,
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request),
                JsonFormatUtil.printToString(builder));
        }

        return new InvokeResult<>(builder.build());

    }

    /**
     * 构建返回的data数据
     *
     * @param paramMap 封装请求入参的map
     * @param msisdn 阅读号
     * @return cn.migu.selection.proto.base.GuessLikeListResponseOuterClass.GuessLikeListData
     * @author fengjiangtao
     */
    private GuessLikeListData createGuessLikeListData(Map<String, String> paramMap, String msisdn)
    {
        int pageSize = RequestParamCheckUtils.paginationParamCheck(paramMap.get(ParamConstants.PAGESIZE), 10);

        GuessLikeListData.Builder dataBuilder = GuessLikeListData.newBuilder();
        dataBuilder.setTitle(StringTools.nvl(paramMap.get(ParamConstants.TITLE)));
        String showType = paramMap.get(ParamConstants.SHOWTYPE);
        if (Util.isEmpty(showType) || !showType.matches(SHOW_TYPE_REGEX))
        {
            showType = SHOW_TYPE_DEFAULT;
        }
        dataBuilder.setShowType(showType);
        dataBuilder.setPageSize(pageSize);
        dataBuilder.setMsisdn(StringTools.nvl(encryptMobile(msisdn)));
        UserInfoSNS userInfoSNS = PortalUserInfoSNSCacheService.getInstance().getUserInfoSNS(msisdn, null);
        if (Util.isNotEmpty(userInfoSNS) && Util.isNotEmpty(userInfoSNS.getUserRelatedMobile()))
        {
            dataBuilder.setRelatedMobile(encryptMobile(userInfoSNS.getUserRelatedMobile()));
        }
        String dataFrom = paramMap.get(ParamConstants.DATAFROM);
        if (Util.isEmpty(dataFrom) || !dataFrom.matches(DATA_FROM_REGEX))
        {
            dataFrom = DATA_FROM_DEFAULT;
        }
        dataBuilder.setDataFrom(dataFrom);
        List<BookItem> books = null;
        //dataFrom为3时，不处理
        if (dataFrom.equals(DATA_FROM_DEFAULT) || dataFrom.equals(DATA_FROM_FAMOUS))
        {
            if (!UserManager.isGuestUser(msisdn))
            {
                books = getUserLikeBooksByReason(msisdn, pageSize, dataFrom);
            }
            if (Util.isEmpty(books))
            {
                String rankDateType = paramMap.get(ParamConstants.RANKDATETYPE);
                String rankRule = paramMap.get(ParamConstants.RANKRULE);
                String rankType = paramMap.get(ParamConstants.RANKTYPE);
                String nid = paramMap.get(ParamConstants.NID);
                books = getUserLikeBooksByBIRank(rankDateType, rankRule, rankType, nid, pageSize);
            }
        }
        List<GuessLikeListBook> bookList = new ArrayList<>();
        if (Util.isNotEmpty(books))
        {
            HashMap<String, String> tmpMap = new HashMap<>();
            for (BookItem book : books)
            {
                GuessLikeListBook.Builder guessLikeListBook = GuessLikeListBook.newBuilder();
                guessLikeListBook.setBookName(StringTools.nvl(book.getBookName()));
                guessLikeListBook.setAuthorName(StringTools.nvl(BookManager.getPenName(book.getAuthorId())));
                tmpMap.put(ParamConstants.BID, book.getBookId());
                guessLikeListBook.setBookUrl(StringTools.nvl(PresetPageUtils.getBookDetailPage(tmpMap)));
                guessLikeListBook.setBookCover(StringTools.nvl(BookManager.getBookCoverLogo(book)));
                bookList.add(guessLikeListBook.build());
            }
        }
        dataBuilder.addAllBooks(bookList);
        BeanMergeUtils.setComponentStyle(dataBuilder, paramMap);
        return dataBuilder.build();
    }

    /**
     * 加密手机号
     *
     * @param userRelatedMobile 手机号
     * @return java.lang.String
     * @author fengjiangtao
     */
    private String encryptMobile(String userRelatedMobile)
    {
        if (Util.isEmpty(userRelatedMobile))
        {
            return userRelatedMobile;
        }
        //字符串倒序
        userRelatedMobile = StringUtils.reverse(userRelatedMobile);
        int length = userRelatedMobile.length();
        //5个数字一组分组
        int[] divide = new int[(length + 4) / 5];
        int divideLength = divide.length;
        for (int i = 0; i < divideLength; i++)
        {
            int temp = i * 5 + 5;
            if (temp > length)
            {
                divide[i] = NumberUtils.toInt(userRelatedMobile.substring(i * 5, length));
            }
            else
            {
                divide[i] = NumberUtils.toInt(userRelatedMobile.substring(i * 5, temp));
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        //除最后一组均转换为62进制, 最后一组包含一位数字，前面拼上"|", 各组数字按字符串拼接
        for (int i = 0; i < divideLength - 1; i++)
        {
            stringBuffer.append(string10to62(divide[i]));
        }
        stringBuffer.append("|").append(divide[divideLength - 1]);
        return stringBuffer.toString();
    }

    /**
     * 将10进制转换为62进制
     *
     * @param number 要转换的数字
     * @return java.lang.String
     * @author fengjiangtao
     */
    private String string10to62(int number)
    {
        int radix = CHAR_ARR.length;
        int qutient = number;
        StringBuffer stringBuffer = new StringBuffer();
        do
        {
            int mod = qutient % radix;
            qutient = (qutient - mod) / radix;
            stringBuffer.insert(0, CHAR_ARR[mod]);
        } while (qutient > 0);
        if (stringBuffer.length() < 3)
        {
            int dis = 3 - stringBuffer.length();
            for (int i = 0; i < dis; i++)
            {
                stringBuffer.insert(0, 0);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取用户喜爱图书列表
     *
     * @param msisdn 阅读号
     * @param pageSize 分页大小
     * @return java.util.List<cn.migu.newportal.cache.bean.book.BookItem>
     * @author fengjiangtao
     */
    private List<BookItem> getUserLikeBooksByReason(String msisdn, int pageSize, String dataFrom)
    {
        String recType = SystemConstants.RECOMMEND_BOOK_AND_REASON;
        if (dataFrom.equals(DATA_FROM_FAMOUS))
        {
            recType = SystemConstants.RECOMMEND_BOOK_AND_AUTHOR;
        }
        GetBIRecommendBooksResponse biRecommendBooks =
            PortalBIRecommendCacheService.getInstance().getBIRecommendBooks(msisdn, recType);
        if (Util.isNotEmpty(biRecommendBooks) && Util.isNotEmpty(biRecommendBooks.getListList()))
        {
            List<BIRecommendItem> listList = biRecommendBooks.getListList();
            List<BookItem> books = new ArrayList<>();
            int count = 1;
            for (BIRecommendItem biRecommendItem : listList)
            {
                if (count > pageSize)
                {
                    break;
                }
                if (Util.isEmpty(biRecommendItem) || Util.isEmpty(biRecommendItem.getBookId()))
                {
                    continue;
                }
                BookItem bookItem =
                    PortalContentInfoCacheService.getInstance().getBookItem(biRecommendItem.getBookId());
                if (Util.isEmpty(bookItem) || ParamConstants.BOOK_SHELF_STATUS.equals(bookItem.getStatus()))
                {
                    continue;
                }
                books.add(bookItem);
                count++;
            }
            return books;
        }
        return null;
    }

    /**
     * 获取bi排行榜
     *
     * @param rankDateType
     * @param rankRule
     * @param rankType
     * @param nid
     * @param pageSize
     * @return java.util.List<cn.migu.newportal.cache.bean.book.BookItem>
     * @author fengjiangtao
     */
    private List<BookItem> getUserLikeBooksByBIRank(String rankDateType, String rankRule,
        String rankType, String nid, int pageSize)
    {
        int showAllNum = NumberUtils.toInt(PortalGeneralInfoCacheService.getInstance()
            .getConfigValue("portal_rank_num"), 1000);
        GetBIRankResponse biRankResponse = PortalBIRankRespCacheService.getInstance()
            .getBIRankResponse(nid, rankType, rankDateType, rankRule, showAllNum);
        if (Util.isNotEmpty(biRankResponse) && Util.isNotEmpty(biRankResponse.getRankListList()))
        {
            List<RankInfo> rankList = biRankResponse.getRankListList();
            List<BookItem> books = new ArrayList<>();
            int count = 1;
            for (RankInfo rankInfo : rankList)
            {
                if (count > pageSize)
                {
                    break;
                }
                if (Util.isEmpty(rankInfo) || Util.isEmpty(rankInfo.getBookId()))
                {
                    continue;
                }
                BookItem bookItem =
                    PortalContentInfoCacheService.getInstance().getBookItem(rankInfo.getBookId());
                if (Util.isEmpty(bookItem) || ParamConstants.BOOK_SHELF_STATUS.equals(bookItem.getStatus()))
                {
                    continue;
                }
                books.add(bookItem);
                count++;
            }
            return books;
        }
        return null;
    }

}
