package cn.migu.newportal.selection.service.selection;

import cn.migu.compositeservice.campaignservice.GetBallotData.BallotOptionInfo;
import cn.migu.compositeservice.campaignservice.GetBallotData.GetBallotResponse;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.manager.ContentSortManager;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.bean.local.request.VoteToFreeInfoRequest;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.engine.UESServiceEngine;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.VoteToFreeResponseOuterClass.VoteBooks;
import cn.migu.selection.proto.base.VoteToFreeResponseOuterClass.VoteToFreeData;
import cn.migu.selection.proto.base.VoteToFreeResponseOuterClass.VoteToFreeResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

/**
 * 投票数据处理
 *
 * @author yejiaxu
 * @version C10 2017年7月3日
 * @since SDP V300R003C10
 */
public class VoteToFreeInfoImpl extends ServiceMethodImpl<VoteToFreeResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(VoteToFreeInfoImpl.class.getName());

    private static final String METHOD_NAME = "getVoteToFree";

    @Resource
    private UESServiceEngine uesServiceEngine;

    public VoteToFreeInfoImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 免费首页--投票
     *
     * @author yejiaxu
     * @param controller
     * @param request
     * @return
     * @throws PortalException
     */
    @ImplementMethod
    public InvokeResult<VoteToFreeResponse> getVoteToFreeInfo(ServiceController controller, ComponentRequest request)
        throws PortalException
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter VoteToFreeInfoImpl.getVoteToFreeInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        VoteToFreeResponse.Builder builder = VoteToFreeResponse.newBuilder();
        VoteToFreeData.Builder dataBuilder = VoteToFreeData.newBuilder();
        Map<String, String> map = request.getParamMapMap();
        VoteToFreeInfoRequest req = new VoteToFreeInfoRequest(map);

        builder.setPluginCode(req.getPluginCode());
        // 投票活动id
        if (StringTools.isEmpty(req.getBallotId()))
        {
            ProtoUtil.buildResultCode(builder,
                MSResultCode.ErrorCodeAndDesc.AUTHORBALLOT_IS_EMPTY,
                ParamConstants.NOT_VISABLE);
            LOG.error("VoteToFreeInfoImpl.getVoteToFreeInfo ballotId is empty,identityId:{}", CommonHttpUtil.getIdentity());
            return new InvokeResult<VoteToFreeResponse>(builder.build());
        }
        // 专区和内容
        if (StringTools.isEmpty(req.getNodeContent()))
        {
            ProtoUtil.buildResultCode(builder,
                MSResultCode.ErrorCodeAndDesc.NODE_OR_LINK_IS_EMPTY,
                ParamConstants.NOT_VISABLE);
            LOG.error("VoteToFreeInfoImpl.getVoteToFreeInfo book_id_list is emptyidentityId:{}", CommonHttpUtil.getIdentity());
            return new InvokeResult<VoteToFreeResponse>(builder.build());
        }
        GetBallotResponse ballotItem = null;
        List<BallotOptionInfo> ballotOptionInfo = null;
        try
        {
            // 获取投票信息
            ballotItem = ContentServiceEngine.getInstance().getBallotData(req.getBallotId());
        }
        catch (PortalException e)
        {
            LOG.error("VoteToFreeInfo buildResponse  error,e:{},ballotId:{}", e, req.getBallotId());
            ProtoUtil.buildResultCode(builder,
                MSResultCode.ErrorCodeAndDesc.AUTHORBALLOT_IS_EMPTY,
                ParamConstants.NOT_VISABLE);
            return new InvokeResult<VoteToFreeResponse>(builder.build());
        }
        if (Util.isNotEmpty(ballotItem) && Util.isNotEmpty(ballotItem.getBallotOptionListList()))
        {
            // 获取投票选项
            ballotOptionInfo = ballotItem.getBallotOptionListList();
        }
        // 调用uesService getNodeContent 接口获取专区和内容
        List<String> contents = new ArrayList<>();
        /**2017-11-14 09:18:32 接口专区和内容获取方式变更 hushanqing start*/
        Map<String, Set<String>> nodeAndContent =
            UesServerServiceUtils.getUesNodeAndContent(req.getInstanceId(), ParamConstants.BOOK_ID_LIST);
        if (Util.isNotEmpty(nodeAndContent))
        {
            for (Entry<String, Set<String>> entry : nodeAndContent.entrySet())
            {
                contents.addAll(entry.getValue());
            }
        }
        // 获取的排序后的图书id
        String[] tempBookId = ContentSortManager.getInstance()
            .sortContent(req.getSortType(), contents.toArray(new String[contents.size()]), req.getInstanceId());
        if (Util.isNotEmpty(tempBookId) && tempBookId.length > ballotOptionInfo.size())
        {
            tempBookId = Arrays.copyOfRange(tempBookId, 0, ballotOptionInfo.size());
        }
        // 获取投票的图书信息
        List<VoteBooks> voteBooks = getBookList(
            tempBookId,
            ballotOptionInfo,
            req.getLargeSize(),
            req.getTpbuttonDesc(),
            req.getNameShowType());
        dataBuilder.addAllVotes(voteBooks);
        ProtoUtil.buildCommonData(dataBuilder, request.getParamMapMap());
        builder.setData(dataBuilder);
        ProtoUtil.buildResultCode(builder, MSResultCode.ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("exit voteToFreeInfo.getVoteToFreeInfo,identityId:{} response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<VoteToFreeResponse>(builder.build());
    }

    /**
     * 获取参数投票活动的图书信息
     *
     * @author yejiaxu
     * @param ballotOptionInfo
     * @param multiple
     * @param tpbuttonDesc
     * @param nameShowType
     * @return
     */
    private List<VoteBooks> getBookList(String[] bookIdList,List<BallotOptionInfo> ballotOptionInfo, int multiple,
        String tpbuttonDesc, String nameShowType)
    {
        List<VoteBooks> voteBooks = new ArrayList<VoteBooks>();

        // 获取参与投票活动的图书相关信息
        if(Util.isNotEmpty(bookIdList))
        {
            for (int i = 0; i < bookIdList.length; i++)
            {
                // 获取图书信息
                BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookIdList[i]);
                if (Util.isEmpty(bookItem))
                {
                    LOG.debug("VoteToFreeInfoImpl.getBookList,bookItem is null; bookId :{} " + bookIdList[i]);
                    continue;
                }
                VoteBooks.Builder book = VoteBooks.newBuilder();
                // 获取作者信息
                AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                if (Util.isNotEmpty(authorInfo))
                {
                    book.setAuthorName(StringTools.nvl(authorInfo.getAuthorPenName()));
                }
                // 投票按钮描述
                book.setTpbuttonDesc(StringTools.nvl(tpbuttonDesc));
                // 图书书名
                book.setBookShowName(StringTools.nvl(BookDataUtil.getShowName(bookIdList[i], nameShowType)));
                book.setBookShortDesc(StringTools.nvl(bookItem.getShortDescription()));
                // 获取图书封面
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookItem,
                    PropertiesConfig.getProperty("cdnUrl"));
                book.setBookCoverLogo(StringTools.nvl(bookCover));
                // 图书详情页地址
                HashMap<String, String> map = new HashMap<>();
                map.put(ParamConstants.BID, bookIdList[i]);
                book.setBookDetailUrl(PresetPageUtils.getBookDetailPage(map));
                // 投票地址
                String voteUrl = ActionUrlUtils.getVoteActionUrlPrefix(String.valueOf(ballotOptionInfo.get(i).getBallotId())
                    , String.valueOf(ballotOptionInfo.get(i).getId()),null);
                book.setVoteUrl(StringTools.nvl(voteUrl));
                // 投票的数量
                long voteNum = ballotOptionInfo.get(i).getOptionTotal();
                voteNum = voteNum * multiple;
                book.setVoteNum(String.valueOf(voteNum));
                voteBooks.add(book.build());
            }
        }

        return voteBooks;
    }

}
