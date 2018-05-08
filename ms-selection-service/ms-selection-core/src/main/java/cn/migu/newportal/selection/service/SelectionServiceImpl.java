package cn.migu.newportal.selection.service;

import cn.migu.newportal.selection.service.selection.*;
import cn.migu.selection.proto.base.AiRecommendListenResponseOuterClass.AiRecommendListenResponse;
import cn.migu.selection.proto.base.ApiLevelOneAdResponseOuterClass.ApiLevelOneAdResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ContentRecommendResponseOuterClass.ContentRecommendResponse;
import cn.migu.selection.proto.base.GetPersonalRecommendResponseOuterClass.GetPersonalRecommendResponse;
import cn.migu.selection.proto.base.GuessLikeListResponseOuterClass.GuessLikeListResponse;
import cn.migu.selection.proto.base.IntelliAdResponseOuterClass.IntelliAdResponse;
import cn.migu.selection.proto.base.MiguGuessResponseOuterClass.MiguGuessResponse;
import cn.migu.selection.proto.base.ModifyPreferenceEntranceResponseOuterClass.ModifyPreferenceEntranceResponse;
import cn.migu.selection.proto.base.NewUserLoginPrizeResponseOuterClass.NewUserLoginPrizeResponse;
import cn.migu.selection.proto.base.NewUserPrizeActionResponseOuterClass.NewUserPrizeActionResponse;
import cn.migu.selection.proto.base.OrientationFreeBookResponseOuterClass.OrientationFreeBookResponse;
import cn.migu.selection.proto.base.PollingLinksResponseOuterClass.PollingLinksResponse;
import cn.migu.selection.proto.base.PreferenceLinksResponseOuterClass.PreferenceLinksResponse;
import cn.migu.selection.proto.base.RankTripleBooksResponseOuterClass.RankTripleBooksResponse;
import cn.migu.selection.proto.base.RecommendContentResponseOuterClass.RecommendContentResponse;
import cn.migu.selection.proto.base.ReserveUpdateActionResponseOuterClass.ReserveUpdateActionResponse;
import cn.migu.selection.proto.base.VoiceBookResponseOuterClass.VoiceBookResponse;
import cn.migu.selection.proto.base.VoteActionResponseOuterClass.VoteActionResponse;
import cn.migu.selection.proto.base.VoteToFreeResponseOuterClass.VoteToFreeResponse;
import cn.migu.selection.proto.selectionService._SelectionService.SelectionService;
import cn.migu.sns.proto.base.NewAjaxResponseOuterClass.NewAjaxResponse;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import javax.annotation.Resource;

public class SelectionServiceImpl extends SelectionService implements SelectionService.Interface, ServiceImpl
{

    @Resource(name = "apiLevelOneAd")
    private ApiLevelOneAdMethodImpl apiLevelOneAd;

    @Resource(name = "advertRecord")
    private AdvertRecordMethodImpl advertRecord;

    @Resource(name = "orientationFreeBookMethodImpl")
    OrientationFreeBookMethodImpl orientationFreeBookMethodImpl;

    @Resource(name = "preferenceLinksMethodImpl")
    PreferenceLinksMethodImpl preferenceLinksMethodImpl;

    @Resource(name = "getNewUserLoginPrize")
    GetNewUserLoginPrizeMethodImpl getNewUserLoginPrize;

    @Resource(name = "drawNewUserPrizeAction")
    DrawNewUserPrizeActionMethodImpl drawNewUserPrizeAction;

    @Resource(name = "rankTripleBooksMethodImpl")
    RankTripleBooksMethodImpl rankTripleBooksMethodImpl;

    @Resource(name = "intelliAdMethodImpl")
    IntelliAdMethodImpl intelliAdMethodImpl;

    @Resource(name = "getPersonalRecommendMethodImpl")
    GetPersonalRecommendMethodImpl getPersonalRecommendMethodImpl;

    @Resource(name = "modifyPreferenceEntranceMethodImpl")
    ModifyPreferenceEntranceMethodImpl modifyPreferenceEntranceMethodImpl;

    @Resource(name = "getRecommendContentMethodImpl")
    private GetRecommendContentMethodImpl getRecommendContentMethodImpl;

    @Resource(name = "reserveUpdateActionImpl")
    private ReserveUpdateActionMethodImpl reserveUpdateActionImpl;

    @Resource(name = "contentRecommendInfo")
    ContentRecommendInfo contentRecommendInfo;

    @Resource(name = "getMiguGuessInfoMethodImpl")
    private GetMiguGuessInfoMethodImpl getMiguGuessInfoMethodImpl;

    @Resource(name = "aiRecommendListenMethodImpl")
    AiRecommendListenMethodImpl aiRecommendListenMethodImpl;

    @Resource(name = "multiPicFreeListenImpl")
    MultiPicFreeListenImpl multiPicFreeListenImpl;

    @Resource(name = "pollingLinkInfoServiceImpl")
    private PollingLinkInfoServiceImpl pollingLinkInfoServiceImpl;

    @Resource(name = "voteToFreeInfo")
    private VoteToFreeInfoImpl voteToFreeInfo;

    @Resource(name = "voteActionInfo")
    private VoteActionInfoImpl voteActionInfo;

    @Resource(name = "guessLikeListMethodImpl")
    private GuessLikeListMethodImpl guessLikeListMethodImpl;

    private String serviceName;

    private String version;

    private ServiceMethod[] methods = null;

    public SelectionServiceImpl()
    {

        super();

    }

    public void init()
    {
        methods = new ServiceMethod[] {orientationFreeBookMethodImpl, preferenceLinksMethodImpl,
            rankTripleBooksMethodImpl, intelliAdMethodImpl, getNewUserLoginPrize, drawNewUserPrizeAction,
            getPersonalRecommendMethodImpl, modifyPreferenceEntranceMethodImpl, getRecommendContentMethodImpl,
            reserveUpdateActionImpl, contentRecommendInfo, getMiguGuessInfoMethodImpl, aiRecommendListenMethodImpl,
            multiPicFreeListenImpl, pollingLinkInfoServiceImpl, voteToFreeInfo, voteActionInfo, apiLevelOneAd,
            advertRecord, guessLikeListMethodImpl};
    }

    @Override
    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    @Override
    public ServiceMethod[] getMethods()
    {
        return methods;
    }

    public void setMethods(ServiceMethod[] methods)
    {
        this.methods = methods;
    }

    @Override
    public void orientationFreeBook(RpcController controller, ComponentRequest request,
        RpcCallback<OrientationFreeBookResponse> response)
    {
        if (controller instanceof ServiceController)
        {

            orientationFreeBookMethodImpl.call((ServiceController)controller, request, response);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void preferenceLinks(RpcController controller, ComponentRequest request,
        RpcCallback<PreferenceLinksResponse> response)
    {
        if (controller instanceof ServiceController)
        {

            preferenceLinksMethodImpl.call((ServiceController)controller, request, response);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void newUserLoginPrize(RpcController controller, ComponentRequest request,
        RpcCallback<NewUserLoginPrizeResponse> response)
    {
        if (controller instanceof ServiceController)
        {

            getNewUserLoginPrize.call((ServiceController)controller, request, response);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void drawNewUserPrize(RpcController controller, ComponentRequest request,
        RpcCallback<NewUserPrizeActionResponse> response)
    {
        if (controller instanceof ServiceController)
        {

            drawNewUserPrizeAction.call((ServiceController)controller, request, response);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void getRankTriplebooks(RpcController controller, ComponentRequest request,
        RpcCallback<RankTripleBooksResponse> done)
    {
        if (controller instanceof ServiceController)
        {

            rankTripleBooksMethodImpl.call((ServiceController)controller, request, done);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void intelliAd(RpcController controller, ComponentRequest request, RpcCallback<IntelliAdResponse> done)
    {
        if (controller instanceof ServiceController)
        {

            intelliAdMethodImpl.call((ServiceController)controller, request, done);

        }
        else
        {

            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void getPersonalRecommend(RpcController controller, ComponentRequest request,
        RpcCallback<GetPersonalRecommendResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            getPersonalRecommendMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void modifyPreferenceEntrance(RpcController controller, ComponentRequest request,
        RpcCallback<ModifyPreferenceEntranceResponse> done)
    {

        if (controller instanceof ServiceController)
        {
            modifyPreferenceEntranceMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getRecommendContent(RpcController controller, ComponentRequest request,
        RpcCallback<RecommendContentResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            getRecommendContentMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void reserveUpdateAction(RpcController controller, ComponentRequest request,
        RpcCallback<ReserveUpdateActionResponse> done)
    {
        if (controller instanceof ServiceController)
        {

            reserveUpdateActionImpl.call((ServiceController)controller, request, done);

        }
        else
        {

            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getContentRecommendInfo(RpcController controller, ComponentRequest request,
        RpcCallback<ContentRecommendResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            contentRecommendInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getMiguGuessInfo(RpcController controller, ComponentRequest request,
        RpcCallback<MiguGuessResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            getMiguGuessInfoMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void aiRecommendListen(RpcController controller, ComponentRequest request,
        RpcCallback<AiRecommendListenResponse> done)
    {
        aiRecommendListenMethodImpl.call(controller, request, done);
    }

    @Override
    public void multiPicFreeListen(RpcController controller, ComponentRequest request,
        RpcCallback<VoiceBookResponse> done)
    {

        if (controller instanceof ServiceController)
        {
            multiPicFreeListenImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getPollingLinkInfo(RpcController controller, ComponentRequest request,
        RpcCallback<PollingLinksResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            pollingLinkInfoServiceImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getVoteToFree(RpcController controller, ComponentRequest request, RpcCallback<VoteToFreeResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            voteToFreeInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getVoteAction(RpcController controller, ComponentRequest request, RpcCallback<VoteActionResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            voteActionInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    @Override
    public void getApiLevelOneAd(RpcController controller, ComponentRequest request,
        RpcCallback<ApiLevelOneAdResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            apiLevelOneAd.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void getAdvertRecord(RpcController controller, ComponentRequest request, RpcCallback<NewAjaxResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            advertRecord.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }

    }

    @Override
    public void guessLikeList(RpcController controller, ComponentRequest request,
        RpcCallback<GuessLikeListResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            guessLikeListMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

}
