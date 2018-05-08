package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.NumberTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.AiRecommendListenResponseOuterClass.AiRecommendListenData;
import cn.migu.selection.proto.base.AiRecommendListenResponseOuterClass.AiRecommendListenResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 智能推荐听书
 *
 * @author xufan
 * @version C10 2018年3月16日
 * @since SDP V300R003C10
 */
public class AiRecommendListenMethodImpl extends ServiceMethodImpl<AiRecommendListenResponse, ComponentRequest>
{

    private static final ExecutorService exec = Executors.newCachedThreadPool();

    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(AiRecommendListenMethodImpl.class);

    private static final String METHOD_NAME = "aiRecommendListen";

    public AiRecommendListenMethodImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 智能推荐听书
     *
     * @author xufan
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<AiRecommendListenResponse> aiRecommendListen(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter AiRecommendListenMethodImpl.aiRecommendListen(),identityId:{},request:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }

        Map<String, String> paramMap = request.getParamMapMap();
        // 标题
        String title = paramMap.get(ParamConstants.TITLE);
        // pageSize
        int pageSize = NumberTools.toPositiveInt(paramMap.get(ParamConstants.PAGESIZE), ParamConstants.PAGE_SIZE_TEN);

        String msisdn = CommonHttpUtil.getIdentity();

        AiRecommendListenResponse.Builder builder = AiRecommendListenResponse.newBuilder();

        AiRecommendListenData.Builder dataBuilder = AiRecommendListenData.newBuilder();
        // 通用参数
        ProtoUtil.buildCommonData(dataBuilder, paramMap);

        dataBuilder.setMsisdn(Util.nvl(msisdn));

        builder.setPluginCode(Util.nvl(paramMap.get(ParamConstants.PLUGINCODE)));

        dataBuilder.setTitle(Util.nvl(title));

        dataBuilder.setPageSize(pageSize);

        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);

        builder.setData(dataBuilder);

        if (logger.isDebugEnabled())
        {
            logger.debug("Exit AiRecommendListenMethodImpl.aiRecommendListen(),response:{} ,identityId = {}",
                JsonFormatUtil.printToString(builder.build()), CommonHttpUtil.getIdentity());
        }
        return new InvokeResult<AiRecommendListenResponse>(builder.build());
    }

}
