package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.bean.CornerParam;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.corner.CornerBusiness;
import cn.migu.newportal.cache.cache.service.CurrentUserInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalCapacitySupportCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.RequestParamCheckUtils;
import cn.migu.newportal.selection.manager.UserPreferenceManager;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.bean.user.PortalUserInfo;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.other.NumberTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.PortalUtils;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.MiguGuessResponseOuterClass.MiguGuessData;
import cn.migu.selection.proto.base.MiguGuessResponseOuterClass.MiguGuessResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 咪咕猜_V7
 */
public class GetMiguGuessInfoMethodImpl extends ServiceMethodImpl<MiguGuessResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(GetMiguGuessInfoMethodImpl.class);
    
    private static final String METHOD_NAME = "getMiguGuessInfo";

    private static final String[] prtypeValue = new String[] {ParamConstants.PRTYPE_15, ParamConstants.PRTYPE_14,
        ParamConstants.PRTYPE_16, ParamConstants.PRTYPE_17, ParamConstants.PRTYPE_63, ParamConstants.PRTYPE_65};
    
    public GetMiguGuessInfoMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 咪咕猜_V7
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<MiguGuessResponse> getMiguGuessInfo(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter GetMiguGuessInfoMethodImpl-getMiguGuessInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        MiguGuessResponse.Builder builder = MiguGuessResponse.newBuilder();
        MiguGuessData.Builder builderData = MiguGuessData.newBuilder();

        Map<String, String> paramMap = request.getParamMapMap();
        builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));

        // 当前页码
        String pageNo = paramMap.get(ParamConstants.PAGENO);
        int pageNoTemp = RequestParamCheckUtils.paginationParamCheck(pageNo, 1);
        builderData.setPageNo(StringTools.nvl(pageNoTemp));

        // 展示数量
        String pageSize = paramMap.get(ParamConstants.PAGESIZE);
        int pageSizeTemp = RequestParamCheckUtils.paginationParamCheck(pageSize, 10);
        builderData.setPageSize(StringTools.nvl(pageSizeTemp));

        //标题
        String title = paramMap.get(ParamConstants.TITLE);
        builderData.setTitle(StringTools.nvl(title));

        // 数据来源： 15:新书;14:热书;16:标签; 17:普通;65:综合;63:关联推荐图书level=1的图书;默认:65
        String dataFrom = paramMap.get(ParamConstants.DATAFROM);
        dataFrom = ParamUtil.checkParamter(prtypeValue, dataFrom, ParamConstants.PRTYPE_65);
        builderData.setDataFrom(dataFrom);

        // 角标展示（复选框）：1:免费;2:限免;3:会员;4:完本;5:名家;6:上传;
        String cornerShowType = paramMap.get(ParamConstants.CORNERSHOWTYPE);
        builderData.setCornerShowType(getCornerShowType(cornerShowType));

        // 间隔图书数量
        String sheetCount = paramMap.get(ParamConstants.SHEET_COUNT);
        int sheetCountTemp = StringTools.toInt(sheetCount, 10);
        sheetCountTemp = sheetCountTemp <= 0 ? 10 : sheetCountTemp;
        builderData.setSheetCount(StringTools.nvl(sheetCountTemp));

        //关联的手机账号
        String relatedAccount = "";
        PortalUserInfo portalUserInfo = CurrentUserInfoCacheService.getInstance().getCurrentUserInfo(CommonHttpUtil.getIdentity());
        if (Util.isNotEmpty(portalUserInfo) && Util.isNotEmpty(portalUserInfo.getUserInfo()))
        {
            relatedAccount = portalUserInfo.getUserInfo().getUserRelatedMobile();
        }
        builderData.setRelatedAccount(StringTools.nvl(relatedAccount));

        //客户端版本号，默认"android"
        String clientVersion = PortalUtils.getUAVersion();
        //客户端版本号;请求头client_version中获取，默认"android"
        clientVersion = StringTools.isEmpty(clientVersion) ? "android" : clientVersion;
        builderData.setClientVersion(clientVersion);

        builderData.setMsisdn(StringTools.nvl(CommonHttpUtil.getIdentity()));

        String edition_id = "";
        //是否支持从平台获取用户偏好 1:是 0否
        String and_newpictureShare = isSupportAbility(ParamConstants.AND_ISSUPPORTABILITYREFFLE);
        String ios_newpictureShare = isSupportAbility(ParamConstants.IOS_ISSUPPORTABILITYREFFLE);
        if (StringTools.isEq(Types.ZERO, and_newpictureShare) && StringTools.isEq(Types.ZERO, ios_newpictureShare))
        {
            edition_id = paramMap.get(ParamConstants.FTLEDITIONID);
            // 用户偏好值，默认返回7
            edition_id = StringTools.isEmpty(edition_id) ? ParamConstants.DEFAULT_EDITIONID : edition_id;
        }
        else
        {
            List<String> edition_id_list = UserPreferenceManager.getUserSelectedPreference();
            if(Util.isNotEmpty(edition_id_list))
            {
                for (String editionId : edition_id_list)
                {
                    if (StringTools.isEmpty(editionId))
                    {
                        continue;
                    }
                    edition_id = edition_id + editionId;
                }
            }
        }
        builderData.setEditionId(edition_id);
        //组件id
        String instanceId = StringTools.nvl(NumberTools.toInt(paramMap.get(ParamConstants.INSTANCEID), 0));
        builderData.setInstanceId(instanceId);

        //封装通用响应数据(isMarginBottom,isMarginTop,isPaddingTop,isShowLine)
        ProtoUtil.buildCommonData(builderData, request.getParamMapMap());
        // 设置返回数据是否可见、状态码、描述
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        builder.setData(builderData);

        if (logger.isDebugEnabled())
        {
            logger.debug( "Exit GetMiguGuessInfoMethodImpl-getMiguGuessInfo，identityId:{} response :{}"
                , CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<MiguGuessResponse>(builder.build());
    }

    /**
     * 角标展示（复选框）：1:免费;2:限免;3:会员;4:完本;5:名家;6:上传;
     * @param cornerShowType
     * @return
     */
    private String getCornerShowType(String cornerShowType)
    {
        boolean flag = false;
        if(StringTools.isNotEmpty(cornerShowType))
        {
            flag = cornerShowType.matches("^[1-6,()]+$");
        }
        cornerShowType = flag ? cornerShowType : "";
        return cornerShowType;
    }

    /**
     * 根据能力关键字判断当前版本能力
     * @param capacityKey
     * @return
     */
    public String isSupportAbility(String capacityKey)
    {
        if (StringTools.isEmpty(capacityKey))
        {
            return null;
        }
        return PortalCapacitySupportCacheService.getInstance().isSupportAbility(capacityKey);
    }

}