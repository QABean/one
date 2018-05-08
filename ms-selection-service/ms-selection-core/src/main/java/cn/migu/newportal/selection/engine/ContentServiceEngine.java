package cn.migu.newportal.selection.engine;

import java.util.List;

import org.apache.commons.lang.StringUtils;


import com.huawei.iread.server.constant.States;
import com.huawei.iread.server.constant.Types;

import cn.migu.compositeservice.bookproduct.BookProductConsumer;
import cn.migu.compositeservice.bookproductservice.GetBookFeeDesc.GetBookFeeDescRequest;
import cn.migu.compositeservice.bookproductservice.GetBookFeeDesc.GetBookFeeDescResponse;
import cn.migu.compositeservice.booksns.BooksnsConsumer;
import cn.migu.compositeservice.booksnsservice.GetSingleNumAboutBook.GetSingleNumAboutBookRequest;
import cn.migu.compositeservice.booksnsservice.GetSingleNumAboutBook.GetSingleNumAboutBookResponse;
import cn.migu.compositeservice.campaign.CampaignConsumer;
import cn.migu.compositeservice.campaignservice.Balloted.BallotedRequest;
import cn.migu.compositeservice.campaignservice.Balloted.BallotedResponse;
import cn.migu.compositeservice.campaignservice.GetBallotData.GetBallotRequest;
import cn.migu.compositeservice.campaignservice.GetBallotData.GetBallotResponse;
import cn.migu.compositeservice.campaignservice.GetDrawNumber.DrawNumberRequest;
import cn.migu.compositeservice.campaignservice.GetDrawNumber.DrawNumberResponse;
import cn.migu.compositeservice.campaignservice.ProcessLottery.ProcessLotteryRequest;
import cn.migu.compositeservice.campaignservice.ProcessLottery.ProcessLotteryResponse;
import cn.migu.compositeservice.cont.ContConsumer;
import cn.migu.compositeservice.contservice.GetLimitBooksWithPart.GetBookListWithPartRequest;
import cn.migu.compositeservice.contservice.GetLimitBooksWithPart.GetLimitBooksWithPartResponse;
import cn.migu.compositeservice.contservice.GetPagedBooksWithPart.GetPagedBooksWithPartRequest;
import cn.migu.compositeservice.contservice.GetPagedBooksWithPart.GetPagedBooksWithPartResponse;
import cn.migu.compositeservice.contservice.GetRank.GetRankRequest;
import cn.migu.compositeservice.contservice.GetRank.GetRankResponse;
import cn.migu.compositeservice.other.OtherConsumer;
import cn.migu.compositeservice.otherservice.GetMyExchangeList.CdkInfo;
import cn.migu.compositeservice.otherservice.GetMyExchangeList.GetMyExchangeListRequest;
import cn.migu.compositeservice.otherservice.GetMyExchangeList.GetMyExchangeListResponse;
import cn.migu.compositeservice.otherservice.GetUserPreSubBookInfo.GetUserPreSubBookInfoRequest;
import cn.migu.compositeservice.otherservice.GetUserPreSubBookInfo.GetUserPreSubBookInfoResponse;
import cn.migu.compositeservice.otherservice.UserPreSubscription.UserPreSubscriptionRequest;
import cn.migu.compositeservice.otherservice.UserPreSubscription.UserPreSubscriptionResponse;
import cn.migu.compositeservice.user.UserConsumer;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.engine.MicroserviceTools;
import cn.migu.newportal.cache.util.GetSpringContext;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.userservice.GetCDKBlackUser.GetCDKBlackUserRequest;
import cn.migu.userservice.GetCDKBlackUser.GetCDKBlackUserResponse;
import cn.migu.userservice.GetExtendUserInfo.AddExtendUserInfoRequest;
import cn.migu.userservice.GetExtendUserInfo.GetExtendUserInfoResponse;
import cn.migu.userservice.GetMyBookScore.GetMyBookScoreReq;
import cn.migu.userservice.GetMyBookScore.GetMyBookScoreResponse;
import cn.migu.wheat.service.ApiResult;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组合服务消引擎,用于调用发布到etcd上的组合服务
 * 
 * @author wulinfeng
 * @version C10 2017年6月5日
 * @since SDP V300R003C10
 */
public class ContentServiceEngine
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(ContentServiceEngine.class);
    
    // 单例
    private static ContentServiceEngine engine = new ContentServiceEngine();
    
    public static ContentServiceEngine getInstance()
    {
        return engine;
    }
    
    public GetBookFeeDescResponse getBookFeeDesc(InvokeContext context, String bid, String mobile, String contentType,
        String isFinish, String hasActual, String isRecommond, String loginType)
        throws PortalException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "enter ContentServiceEngine.getBookFeeDesc, bid:{}, mobile:{}, contentType:{}, isFinish:{}, hasActual:{}," +
                    " isRecommond:{}, loginType:{}",
                bid,
                mobile,
                contentType,
                isFinish,
                hasActual,
                isRecommond,
                loginType);
        }
        BookProductConsumer bookProductConsumer = (BookProductConsumer)GetSpringContext.getCompositeEngine()
            .getConsumer("compositeservice.bookproduct.consumer");
        if (null == bookProductConsumer)
        {
            logger.error("ContentServiceEngine.getBookFeeDesc: compositeservice.bookproduct.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:contentConsumer is null");
        }
        GetBookFeeDescRequest.Builder requestBuilder = GetBookFeeDescRequest.newBuilder();
        requestBuilder.setBookId(bid);
        requestBuilder.setMobile(mobile);
        requestBuilder.setContentType(contentType);
        requestBuilder.setAccessType("1");
        requestBuilder.setIsFinished(isFinish);
        requestBuilder.setIsRecommend(isRecommond);
        // requestBuilder.setLoginType(loginType);
        requestBuilder.setHasActual(hasActual);
        
        GetBookFeeDescResponse protoType = GetBookFeeDescResponse.getDefaultInstance();
        
        ServiceResult<GetBookFeeDescResponse> sr = MicroserviceTools.getInstance().invoke(bookProductConsumer,
            context,
            "getBookFeeDesc",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("ContentServiceEngine.getBookFeeDesc: return null,request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "ContentServiceEngine.getBookFeeDesc: call microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("ContentServiceEngine.getBookFeeDesc failed,  errorCode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "ContentServiceEngine.getBookFeeDesc: call microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    public String getSingleNumAboutBookCounts(InvokeContext context, String bookId)
        throws PortalException
    {
        BooksnsConsumer booksnsConsumer =
            (BooksnsConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.booksns.consumer");
        if (null == booksnsConsumer)
        {
            logger.error("The Consumer:compositeservice.booksns.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:booksnsConsumer is null");
        }
        GetSingleNumAboutBookRequest.Builder request = GetSingleNumAboutBookRequest.newBuilder();
        request.setBookId(bookId);
        
        GetSingleNumAboutBookResponse protoType = GetSingleNumAboutBookResponse.getDefaultInstance();
        
        ServiceResult<GetSingleNumAboutBookResponse> sr = MicroserviceTools.getInstance().invoke(booksnsConsumer,
            context,
            "getSingleNumAboutBook",
            request.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call booksnsConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getbookFeeInfo microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call booksnsConsumer microservice error , errorcode:{} request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getbookFeeInfo microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return String.valueOf(sr.getData().getDownloadTotal());
        }
        
    }
    
    public String isBlackUser(InvokeContext context, String mobile)
        throws PortalException
    {
        UserConsumer userConsumer =
            (UserConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.user.consumer");
        if (null == userConsumer)
        {
            logger.error("The Consumer:compositeservice.user.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:userConsumer is null");
        }
        
        GetCDKBlackUserRequest.Builder request = GetCDKBlackUserRequest.newBuilder();
        request.setIdentifyID(mobile);
        
        ServiceResult<GetCDKBlackUserResponse> sr = MicroserviceTools.getInstance().invoke(userConsumer,
            context,
            "getCDKBlackUser",
            request.build(),
            GetCDKBlackUserResponse.getDefaultInstance());
        
        if (null == sr)
        {
            logger.error("Call userConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer isBlackUser microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call userConsumer microservice error , errorcode:{},request:{}" ,sr.getCode(),
               JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer isBlackUser microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            GetCDKBlackUserResponse response = sr.getData();
            if (response != null && StringUtils.isNotBlank(response.getIdentityId())) // 有响应说明是黑名单
            {
                return Types.TRUE;
            }
        }
        
        return Types.FALSE;
    }
    
    public String isBindCard(InvokeContext context, String mobile)
        throws PortalException
    {
        OtherConsumer otherConsumer =
            (OtherConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.other.consumer");
        if (null == otherConsumer)
        {
            logger.error("The Consumer:compositeservice.other.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:otherConsumer is null");
        }
        GetMyExchangeListRequest.Builder request = GetMyExchangeListRequest.newBuilder();
        request.setAccountName(mobile);
        request.setAccountType(SelectionConstants.ACCOUNTTYPE_MOBILE); // 类型3为手机号码
        request.setCdkPkgType(SelectionConstants.CDKPKGTYPE_NEWYEARCARD); // 类型3为新年卡
        request.setStart(1); // 分页起始页码为1
        request.setCount(1); // 只要拿第一条记录
        
        ServiceResult<GetMyExchangeListResponse> sr = MicroserviceTools.getInstance().invoke(otherConsumer,
            context,
            "getMyExchangeList",
            request.build(),
            GetMyExchangeListResponse.getDefaultInstance());
        
        if (null == sr)
        {
            logger.error("Call otherConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer isBindCard microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call otherConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer isBindCard microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            if (sr.getData() != null)
            {
                List<CdkInfo> cdkList = sr.getData().getCdkListList();
                
                if (cdkList != null && cdkList.size() > 0)
                {
                    CdkInfo cdk = cdkList.get(0);
                    if (cdk != null)
                    {
                        return "1"; // 存在新年卡则说明绑定，否则未绑定
                    }
                }
            }
        }
        
        return "0";
    }
    
/*    public List<LinkInfo> getLinkInfoList(InvokeContext context, LinkRequest request)
        throws PortalException
    {
        LinkServiceConsumer linkServiceConsumer = (LinkServiceConsumer)GetSpringContext.getCompositeEngine()
            .getConsumer("ues.interface.linkservice.consumer");
        if (null == linkServiceConsumer)
        {
            logger.error("The Consumer:linkServiceConsumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:linkServiceConsumer is null");
        }
        
        ServiceResult<LinkResponse> sr = MicroserviceTools.getInstance().invoke(linkServiceConsumer,
            context,
            "getLinkInfoList",
            request,
            LinkResponse.getDefaultInstance());
        if (null == sr)
        {
            logger.error("Call linkServiceConsumer microservice error ,return null");
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getLinkInfoList microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call linkServiceConsumer microservice error ,return errorcode:" + sr.getCode());
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getLinkInfoList microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData().getLinkInfoList();
        }
    }*/
    
/*    public String getConfig(InvokeContext context, ConfigRequest request)
        throws PortalException
    {
        UesConfigServiceConsumer pageConfigServiceConsumer = (UesConfigServiceConsumer)GetSpringContext
            .getCompositeEngine().getConsumer("ues.interface.configservice.consumer");
        if (null == pageConfigServiceConsumer)
        {
            logger.error("The Consumer:pageConfigServiceConsumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:pageConfigServiceConsumer is null");
        }
        
        ServiceResult<ConfigResponse> sr =
            pageConfigServiceConsumer.invoke(context, "getConfig", request, ConfigResponse.getDefaultInstance());
        if (null == sr)
        {
            logger.error("Call pageConfigServiceConsumer microservice error ,return null");
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getConfig microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call pageConfigServiceConsumer microservice error ,return errorcode:" + sr.getCode());
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getConfig microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData().getConfigVale();
        }
    }*/
    
/*    public String getParam(InvokeContext context, ParamRequest request)
        throws PortalException
    {
        PageConfigServiceConsumer pageParamServiceConsumer = (PageConfigServiceConsumer)GetSpringContext
            .getCompositeEngine().getConsumer("ues.interface.paramservice.consumer");
        if (null == pageParamServiceConsumer)
        {
            logger.error("The Consumer:pageParamServiceConsumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:pageParamServiceConsumer is null");
        }
        
        ServiceResult<ParamResponse> sr =
            pageParamServiceConsumer.invoke(context, "getParam", request, ParamResponse.getDefaultInstance());
        if (null == sr)
        {
            logger.error("Call pageParamServiceConsumer microservice error ,return null");
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getParam microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call pageParamServiceConsumer microservice error ,return errorcode:" + sr.getCode());
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getParam microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData().getParamVale();
        }
    }*/
    
    /**
     * 获取投票信息的组合服务
     *
     * @author yejiaxu
     * @param ballotId
     * @return
     * @throws PortalException
     */
    public GetBallotResponse getBallotData(String ballotId)
        throws PortalException
    {
        CampaignConsumer ballotDataConsumer =
            (CampaignConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.campaign.consumer");
        if (null == ballotDataConsumer)
        {
            logger.error("The Consumer:compositeservice.campaign.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:ballotDataConsumer is null");
        }
        GetBallotRequest.Builder requestBuilder = GetBallotRequest.newBuilder();
        requestBuilder.setBallotId(ballotId);
        
        InvokeContext ic = GetSpringContext.getInvokeContext();
        
        GetBallotResponse protoType = GetBallotResponse.getDefaultInstance();
        
        ServiceResult<GetBallotResponse> sr = MicroserviceTools.getInstance().invoke(ballotDataConsumer,
            ic,
            "getBallotData",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call ballotDataConsumer ballotDataConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getBallotData microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error(
                "Call ballotDataConsumer ballotDataConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call Compositeservice getBallotData microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 投票操作
     *
     * @author yejiaxu
     * @param msisdn
     * @return
     * @throws PortalException
     */
    public BallotedResponse balloted(String msisdn, String ballotId, String itemId)
        throws PortalException
    {
        CampaignConsumer ballotDataConsumer =
            (CampaignConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.campaign.consumer");
        if (null == ballotDataConsumer)
        {
            logger.error("The Consumer:compositeservice.campaign.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:ballotedConsumer is null");
        }
        BallotedRequest.Builder requestBuilder = BallotedRequest.newBuilder();
        requestBuilder.setBallotId(ballotId);
        requestBuilder.setItemId(itemId);
        requestBuilder.setMobile(msisdn);
        InvokeContext ic = GetSpringContext.getInvokeContext();
        
        BallotedResponse protoType = BallotedResponse.getDefaultInstance();
        
        ServiceResult<BallotedResponse> sr = MicroserviceTools.getInstance().invoke(ballotDataConsumer,
            ic,
            "balloted",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call ballotedConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer balloted microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger
                .error("Call ballotDataConsumer ballotedConsumer microservice error , errorcode:{}",
                    sr.getCode(),
                    JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer balloted microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 
     * 
     *
     * @param sortType
     * @param nodeId
     * @param provinceID
     * @param cityID
     * @return
     * @throws PortalException
     */
    public GetLimitBooksWithPartResponse getLimitBooksWithPart(String sortType, String nodeId, String provinceID,
        String cityID, String topLevel)
        throws PortalException
    {
        // 图书类型：书籍
        String bookTypes = Types.BOOK_TYPE + ";" + Types.VOICE_TYPE + ";" + Types.CARTOON_TYPE + ";"
            + Types.MAGAZINE_TYPE + ";" + Types.NEWSPAPER_TYPE;
        ContConsumer contentConsumer =
            (ContConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.cont.consumer");
        if (null == contentConsumer)
        {
            logger.error("The Consumer:compositeservice.cont.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:contentConsumer is null");
        }
        GetBookListWithPartRequest.Builder requestBuilder = GetBookListWithPartRequest.newBuilder();
        
        requestBuilder.setCatalogId(nodeId);
        
        // 图书类型：书籍
        requestBuilder.setBookItemType(bookTypes);
        
        // 书籍为上架状态
        requestBuilder.setStatus(States.CONTENT_ON_FIGHT);
        
        // 不递归查询子节点
        requestBuilder.setIsRecursive(Types.FALSE);
        
        requestBuilder.setSortType(sortType);
        
        // provinceId、cityId、topLevel只会有一个,哪个不为空就设置哪个
        if (StringTools.isNotEmpty(provinceID))
        {
            requestBuilder.setProvinceId(provinceID);
        }
        else if (StringTools.isNotEmpty(cityID))
        {
            requestBuilder.setCityId(cityID);
        }
        else if (StringTools.isNotEmpty(topLevel))
        {
            requestBuilder.setTopLevel(topLevel);
        }
        GetLimitBooksWithPartResponse protoType = GetLimitBooksWithPartResponse.getDefaultInstance();
        InvokeContext context = GetSpringContext.getInvokeContext();
        ServiceResult<GetLimitBooksWithPartResponse> sr = MicroserviceTools.getInstance().invoke(contentConsumer,
            context,
            "getLimitBooksWithPart",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call contentConsumer contentConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getbookFeeInfo microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call contentConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getbookFeeInfo microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 
     * 获取排行榜数据，门户不做缓存，后期由组合服务或者SMU做缓存
     *
     * @param pageNo
     * @param pageSize
     * @param nodeId
     * @return
     * @throws PortalException
     */
    public GetRankResponse getRank(String nodeId, String rankStandard, String rankType, int pageSize, int pageNo)
        throws PortalException
    {
        ContConsumer contentConsumer =
            (ContConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.cont.consumer");
        if (null == contentConsumer)
        {
            logger.error("The Consumer:compositeservice.cont.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:contentConsumer is null");
        }
        GetRankRequest.Builder requestBuilder = GetRankRequest.newBuilder();
        requestBuilder.setNodeId(nodeId);
        requestBuilder.setRankType(rankStandard);
        requestBuilder.setRankDateType(rankType);
        requestBuilder.setRankStart((pageNo - 1) * pageSize + 1);
        requestBuilder.setRankCount(pageSize);
        
        GetRankResponse protoType = GetRankResponse.getDefaultInstance();
        InvokeContext context = GetSpringContext.getInvokeContext();
        ServiceResult<GetRankResponse> sr = MicroserviceTools.getInstance().invoke(contentConsumer,
            context,
            "getRank",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call contentConsumer contentConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getbookFeeInfo microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call contentConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getbookFeeInfo microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 
     * 
     *
     * @param pageNo
     * @param pageSize
     * @param sortType
     * @param nodeId
     * @param provinceID
     * @param cityID
     * @return
     * @throws PortalException
     */
    public GetPagedBooksWithPartResponse getPagedBooksWithPart(int pageNo, int pageSize, String sortType, String nodeId,
        String provinceID, String cityID)
        throws PortalException
    {
        // 图书类型：书籍
        String bookTypes = Types.BOOK_TYPE + ";" + Types.VOICE_TYPE + ";" + Types.CARTOON_TYPE + ";"
            + Types.MAGAZINE_TYPE + ";" + Types.NEWSPAPER_TYPE;
        ContConsumer contentConsumer =
            (ContConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.cont.consumer");
        if (null == contentConsumer)
        {
            logger.error("The Consumer:compositeservice.cont.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:contentConsumer is null");
        }
        GetPagedBooksWithPartRequest.Builder requestBuilder = GetPagedBooksWithPartRequest.newBuilder();
        
        requestBuilder.setCatalogId(nodeId);
        
        // 图书类型：书籍
        requestBuilder.setBookItemType(bookTypes);
        
        // 书籍为上架状态
        requestBuilder.setStatus(States.CONTENT_ON_FIGHT);
        
        // 不递归查询子节点
        requestBuilder.setIsRecursive(Types.FALSE);
        
        requestBuilder.setSortType(sortType);
        
        // 省份ID
        requestBuilder.setProvinceId(provinceID);
        
        // 城市ID
        requestBuilder.setCityId(cityID);
        
        requestBuilder.setPageCount(String.valueOf(pageSize));
        requestBuilder.setPageNum(String.valueOf(pageNo));
        
        GetPagedBooksWithPartResponse protoType = GetPagedBooksWithPartResponse.getDefaultInstance();
        InvokeContext context = GetSpringContext.getInvokeContext();
        ServiceResult<GetPagedBooksWithPartResponse> sr = MicroserviceTools.getInstance().invoke(contentConsumer,
            context,
            "getPagedBooksWithPart",
            requestBuilder.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call contentConsumer contentConsumer microservice error ,return null request:{}",
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call ContentServer getbookFeeInfo microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call contentConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(requestBuilder));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "Call ContentServer getbookFeeInfo microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 抽奖次数查询
     * 
     * @author hushanqing
     * @param activeId
     * @param msisdn
     * @param portalType
     * @param loginType
     * @param channelCode
     * @param clientVersion
     * @param userAgent
     * @param missionId
     * @return
     * @throws PortalException
     */
    public DrawNumberResponse getDrawNumber(String activeId, String msisdn, String portalType, String loginType,
        String channelCode, String clientVersion, String userAgent, String missionId)
        throws PortalException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "enter ContentServiceEngine.getDrawNumber(),activeId:{},msisdn:{},portalType:{},loginType:{},channelCode:{},clientVersion:{},userAgent:{},missionId:{}",
                activeId,
                msisdn,
                portalType,
                loginType,
                channelCode,
                clientVersion,
                userAgent,
                missionId);
        }
        
        if (StringTools.isEmpty(activeId) || StringTools.isEmpty(msisdn) || StringTools.isEmpty(portalType))
        {
            return null;
        }
        
        CampaignConsumer campaignConsumer =
            (CampaignConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.campaign.consumer");
        if (null == campaignConsumer)
        {
            logger.error("ContentServiceEngine.getDrawNumber: compositeservice.campaign.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:campaignConsumer is null");
        }
        
        String channelCode_ = StringTools.nvl(channelCode);
        String clientVersion_ = StringTools.nvl(clientVersion);
        String userAgent_ = StringTools.nvl(userAgent);
        String loginType_ = StringTools.nvl(loginType);
        String missionId_ = StringTools.nvl(missionId);
        
        DrawNumberRequest.Builder request = DrawNumberRequest.newBuilder();
        // 必传字段
        request.setActiveId(activeId);
        request.setMsisdn(msisdn);
        request.setPortalType(portalType);
        // 非必传
        request.setChannelCode(channelCode_);
        request.setClientVersion(clientVersion_);
        request.setUserAgent(userAgent_);
        request.setLoginType(loginType_);
        request.setTaskId(missionId_);
        
        DrawNumberResponse protoType = DrawNumberResponse.getDefaultInstance();
        InvokeContext context = GetSpringContext.getInvokeContext();
        ServiceResult<DrawNumberResponse> sr = MicroserviceTools.getInstance().invoke(campaignConsumer,
            context,
            "getDrawNumber",
            request.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call campaignConsumer.getDrawNumber microservice error ,return null,request={}"
                ,JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call CampaignService getDrawNumber microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call campaignConsumer microservice error , errorcode:{},request:{}",
                sr.getCode(),
                JsonFormatUtil.printToString(request));
            throw new PortalException(sr.getCode(),
                "Call CampaignService getDrawNumber microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 调用抽奖逻辑接口
     * 
     * @param missionId 任务id
     * @return
     * @throws PortalException
     * @see [类、类#方法、类#成员]
     */
    public ProcessLotteryResponse processLottery(String raffleChance, String portalType, String msisdn,
        String lotteryId, String channelCode, String userAgent, String isLottery, String missionId)
        throws PortalException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "Enter ContentServiceEngine.processLottery(),lotteryId:{},portalType:{},msisdn:{},raffleChance:{}," +
                    "channelCode:{},userAgent:{},isLottery:{},missionId:{}",
                lotteryId,
                portalType,
                msisdn,
                raffleChance,
                channelCode,
                userAgent,
                isLottery,
                missionId);
        }
        
        // 判空验证
        if (StringTools.isEmpty(lotteryId) || StringTools.isEmpty(msisdn) || StringTools.isEmpty(portalType))
        {
            return null;
        }
        
        CampaignConsumer campaignConsumer =
            (CampaignConsumer)GetSpringContext.getCompositeEngine().getConsumer("compositeservice.campaign.consumer");
        if (null == campaignConsumer)
        {
            logger.error("ContentServiceEngine.getDrawNumber: compositeservice.campaign.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:campaignConsumer is null");
        }
        
        String channelCode_ = StringTools.nvl(channelCode);
        String raffleChance_ = StringTools.nvl(raffleChance);
        String userAgent_ = StringTools.nvl(userAgent);
        String missionId_ = StringTools.nvl(missionId);
        
        ProcessLotteryRequest.Builder request = ProcessLotteryRequest.newBuilder();
        request.setCID(lotteryId);
        request.setPortalType(portalType);
        request.setMsisdn(msisdn);
        
        request.setIsLotteryChance(raffleChance_);
        request.setUserAgent(userAgent_);
        request.setChannelCode(channelCode_);
        request.setTaskId(missionId_);
        
        if (!StringTools.isEq(SelectionConstants.SHAKE_LOTTERY, isLottery))
        {
            isLottery = SelectionConstants.DRAW_LOTTERY;
        }
        request.setTriggerScenario(isLottery);
        
        ProcessLotteryResponse protoType = ProcessLotteryResponse.getDefaultInstance();
        InvokeContext context = GetSpringContext.getInvokeContext();
        
        ServiceResult<ProcessLotteryResponse> sr = MicroserviceTools.getInstance().invoke(campaignConsumer,
            context,
            "processLottery",
            request.build(),
            protoType);
        
        if (null == sr)
        {
            logger.error("Call campaignConsumer.processLottery microservice error ,return null,request={}"
                ,JsonFormatUtil.printToString(request));
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "Call CampaignService processLottery microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("Call campaignConsumer microservice error , errorcode:{},request:{}", sr.getCode(),
                JsonFormatUtil.printToString(request));
            throw new PortalException(sr.getCode(),
                "Call CampaignService processLottery microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 是否预订了这本书
     * 
     * @author wanghao
     * @date 2017年10月31日
     * @return
     */
    public GetUserPreSubBookInfoResponse isReserve(GetUserPreSubBookInfoRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter ContentServiceEngine.isReserve(),request=" + JsonFormatUtil.printToString(request));
        }
        if (UserManager.isGuestUser(request.getMobile()) || Util.isEmpty(request.getBookId()))
        {
            logger.error("Error ContentServiceEngine.isReserve() Error! parameter is error,mobile:{},bookId:{}",
                request.getMobile(),
                request.getBookId());
            return null;
        }
        OtherConsumer otherConsumer =
            GetSpringContext.getCompositeEngine().getConsumer("compositeservice.other.consumer", OtherConsumer.class);
        if (null == otherConsumer)
        {
            logger.error("Error ContentServiceEngine.isReserve: compositeservice.other.consumer is null");
            return null;
        }
        ApiResult<GetUserPreSubBookInfoResponse> result =
            otherConsumer.getUserPreSubBookInfo(GetSpringContext.getInvokeContext(), request);
        if (Util.isEmpty(result))
        {
            logger.error("Error ContentServiceEngine.isReserve call compositeservice.other.consumer"
                + ".getUserPreSubBookInfo return data is null , the request :{} "
                , JsonFormatUtil.printToString(request));
            return null;
        }
        GetUserPreSubBookInfoResponse response = result.getData();
        if ((result.getCode() != 0 && result.getCode() != 200) || Util.isEmpty(response))
        {
            logger.error("Error ContentServiceEngine.isReserve compositeservice.other.consumer"
                    + ".getUserPreSubBookInfo ,the result code is {},reposne:{}",
                result.getCode(),
                JsonFormatUtil.printToString(response));
            return null;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit ContentServiceEngine.isReserve, response: " + JsonFormatUtil.printToString(response));
        }
        return response;
    }
    
    /**
     * 预订/取消预订图书
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @param request 操作类型（0添加或1删除）
     */
    public boolean reserveBook(UserPreSubscriptionRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter ContentServiceEngine.reserveBook(),request = " + JsonFormatUtil.printToString(request));
        }
        String operationType = request.getOperationType();
        if (UserManager.isGuestUser(request.getMobile()) || StringTools.isEmpty(request.getBookId())
            || StringTools.isEmpty(operationType) || (!ParamConstants.BOOK_RESERVE.equals(operationType)
                && !ParamConstants.BOOK_UNRESERVE.equals(operationType)))
        {
            logger.error(
                "Error ContentServiceEngine.reserveBook() Error! parameter is error,mobile:{},bookId:{},operationType:{}",
                request.getMobile(),
                request.getBookId(),
                operationType);
            return false;
        }
        OtherConsumer otherConsumer =
            GetSpringContext.getCompositeEngine().getConsumer("compositeservice.other.consumer", OtherConsumer.class);
        if (null == otherConsumer)
        {
            logger.error("Error ContentServiceEngine.reserveBook: compositeservice.other.consumer is null");
            return false;
        }
        ApiResult<UserPreSubscriptionResponse> result =
            otherConsumer.userPreSubscription(GetSpringContext.getInvokeContext(), request);
        if (Util.isEmpty(result))
        {
            logger.error("Error ContentServiceEngine.reserveBook call compositeservice.other.consumer"
                    + ".userPreSubscription return data is null , request :{} "
                , JsonFormatUtil.printToString(request));
            return false;
        }
        if (result.getCode() != 0 && result.getCode() != 200)
        {
            logger.error("Error ContentServiceEngine.reserveBook compositeservice.other.consumer"
                    + ".userPreSubscription ,the result code is {},request:{}",
                result.getCode(),
                JsonFormatUtil.printToString(request));
            return false;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit ContentServiceEngine.reserveBook");
        }
        return true;
    }
    
    /**
     * 获取用户扩展信息
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @param request
     * @return
     */
    public GetExtendUserInfoResponse getExtendUserInfo(AddExtendUserInfoRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter ContentServiceEngine.getExtendUserInfo(),request = " + request);
        }
        if (UserManager.isGuestUser(request.getMsisdn()))
        {
            logger.error("Error ContentServiceEngine.getExtendUserInfo() Error! msisdn is isGuestUser");
            return null;
        }
        UserConsumer userConsumer =
            GetSpringContext.getCompositeEngine().getConsumer("compositeservice.user.consumer", UserConsumer.class);
        if (null == userConsumer)
        {
            logger.error("Error ContentServiceEngine.getExtendUserInfo(): compositeservice.user.consumer is null");
            return null;
        }
        ApiResult<GetExtendUserInfoResponse> result =
            userConsumer.getExtendUserInfo(GetSpringContext.getInvokeContext(), request);
        if (Util.isEmpty(result))
        {
            logger.error("Error ContentServiceEngine.getExtendUserInfo call compositeservice.user.consumer"
                + ".getExtendUserInfo return data is null , request :{} " , JsonFormatUtil.printToString(request));
            return null;
        }
        GetExtendUserInfoResponse response = result.getData();
        
        if ((result.getCode() != 0 && result.getCode() != 200) || Util.isEmpty(response))
        {
            logger.error("Error ContentServiceEngine.getExtendUserInfo compositeservice.user.consumer"
                    + ".getExtendUserInfo ,the result code is {},reqeust:{}",
                result.getCode(),
                JsonFormatUtil.printToString(request));
            return null;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "Exit ContentServiceEngine.getExtendUserInfo response: " + JsonFormatUtil.printToString(response));
        }
        return response;
    }
    
    public GetBookFeeDescResponse getBookFeeDesc(String msisdn, BookItem bookItem, String isPresentQuery)
        throws PortalException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter ContentServiceEngine-getBookFeeDesc,msisdn:{},BookItem:{},isPresentQuery:{}",
                msisdn,
                bookItem,
                isPresentQuery);
        }
        BookProductConsumer bookProductConsumer = (BookProductConsumer)GetSpringContext.getCompositeEngine()
            .getConsumer("compositeservice.bookproduct.consumer");
        if (null == bookProductConsumer)
        {
            logger.error("ContentServiceEngine.getBookFeeDesc: compositeservice.bookproduct.consumer is null");
            throw new PortalException(MSResultCode.CONSUMER_IS_NULL, "The Consumer:contentConsumer is null");
        }
        GetBookFeeDescRequest.Builder requestBuilder = GetBookFeeDescRequest.newBuilder();
        // 图书id
        requestBuilder.setBookId(StringTools.nvl(bookItem.getBookId()));
        // 用户号码
        requestBuilder.setMobile(msisdn);
        // 图书类型
        requestBuilder.setContentType(StringTools.nvl(bookItem.getItemType()));
        // 门户类型
        requestBuilder.setAccessType(SystemConstants.PORTAL_TYPE);
        // 图书是否完本
        requestBuilder.setIsFinished(StringTools.nvl(bookItem.getIsFinish()));
        
        // 是否有实体
        String hasActual = "";
        if (StringTools.isEmpty(bookItem.getActualPrice())
            || StringTools.isEq(bookItem.getActualPrice(), SelectionConstants.ACTUALPRICE_0)
            || StringTools.isEq(bookItem.getActualPrice(), SelectionConstants.ACTUALPRICE_NONE))
        {
            hasActual = Types.FALSE;
        }
        else
        {
            hasActual = Types.TRUE;
        }
        requestBuilder.setHasActual(hasActual);
        
        // 是否支持关联推荐
        String isRecommond = "";
        if (StringUtils.isNotEmpty(bookItem.getIsRecommend())
            && StringTools.isEq(bookItem.getIsRecommend(), SelectionConstants.NOT_RECOMMOND))
        {
            isRecommond = SelectionConstants.NOT_RECOMMOND;
        }
        else
        {
            isRecommond = SelectionConstants.IS_RECOMMOND;
        }
        
        requestBuilder.setIsRecommend(isRecommond);
        // 是否为赠书鉴权查询
        if (StringTools.isNotEmpty(isPresentQuery))
        {
            requestBuilder.setIsPresentQuery(isPresentQuery);
        }
        
        InvokeContext context = GetSpringContext.getInvokeContext();
        ApiResult<GetBookFeeDescResponse> sr = bookProductConsumer.getBookFeeDesc(context, requestBuilder.build());
        
        if (null == sr)
        {
            logger.error("ContentServiceEngine.getBookFeeDesc: return null");
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_NULL,
                "ContentServiceEngine.getBookFeeDesc: call microservice error ,return null");
        }
        else if (0 != sr.getCode())
        {
            logger.error("ContentServiceEngine.getBookFeeDesc: return errorcode:" + sr.getCode());
            throw new PortalException(MSResultCode.MICROSERVICE_RETURN_ERROR,
                "ContentServiceEngine.getBookFeeDesc: call microservice error ,return errorcode:" + sr.getCode());
        }
        else
        {
            return sr.getData();
        }
    }
    
    /**
     * 获取我的评分，(针对当前书籍)
     * 
     * @author wanghao
     * @date 2017年11月3日
     * @return
     */
    public GetMyBookScoreResponse getMyBookScore(GetMyBookScoreReq request)
    {
        if (logger.isDebugEnabled())
        {
            logger
                .debug("Enter ContentServiceEngine.getMyBookScore(),request：{}" , JsonFormatUtil.printToString(request));
        }
        if (UserManager.isGuestUser(request.getMsisdn()) || Util.isEmpty(request.getBookId()))
        {
            logger.error("Error ContentServiceEngine.getMyBookScore() Error! parameter is error,msisdn:{},bookId:{}",
                request.getMsisdn(),
                request.getBookId());
            return null;
        }
        UserConsumer userConsumer =
            GetSpringContext.getCompositeEngine().getConsumer("compositeservice.user.consumer", UserConsumer.class);
        if (null == userConsumer)
        {
            logger.error("Error ContentServiceEngine.getMyBookScore(): compositeservice.user.consumer is null");
            return null;
        }
        ApiResult<GetMyBookScoreResponse> result =
            userConsumer.getMyBookScore(GetSpringContext.getInvokeContext(), request);
        if (Util.isEmpty(result))
        {
            logger.error("Error ContentServiceEngine.getMyBookScore call compositeservice.user.consumer"
                + ".getMyBookScore return data is null , request: {}" , JsonFormatUtil.printToString(request));
            return null;
        }
        GetMyBookScoreResponse response = result.getData();
        
        if ((result.getCode() != 0 && result.getCode() != 200) || Util.isEmpty(response))
        {
            logger.error("Error ContentServiceEngine.getMyBookScore compositeservice.user.consumer"
                    + ".getExtendUserInfo ,the result code is {},request:{}",
                result.getCode(),
                JsonFormatUtil.printToString(request));
            return null;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit ContentServiceEngine.getMyBookScore");
        }
        return response;
    }
}
