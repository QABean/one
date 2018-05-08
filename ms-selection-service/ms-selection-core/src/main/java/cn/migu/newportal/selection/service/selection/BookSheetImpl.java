package cn.migu.newportal.selection.service.selection;

import java.util.List;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.compositeservice.booksnsservice.GetBookSheetShareRankByBookId.ShareBookSheet;
import cn.migu.newportal.cache.bean.book.UserInfoSNS;
import cn.migu.newportal.cache.bean.booksheet.BookSheetInfo;
import cn.migu.newportal.cache.bean.booksheet.RankCacheBean;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookSheetInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookSheetRankCacheService;
import cn.migu.newportal.cache.cache.service.PortalUserInfoSNSCacheService;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.BookSheetInfoResponseOuterClass.BookSheetInfoData;
import cn.migu.selection.proto.base.BookSheetInfoResponseOuterClass.BookSheetInfoResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookSheetImpl extends ServiceMethodImpl<BookSheetInfoResponse, ComponentRequest>
{
    
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(BookSheetImpl.class.getName());
    
    private static final String METHOD_NAME = "getBookSheetInfo";
    
    public BookSheetImpl()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<BookSheetInfoResponse> v1(ServiceController controller, ComponentRequest req)
    {

        if (logger.isDebugEnabled())
        {
            logger.debug( "enter BookSheetImpl-v1，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(req));
        }
        
        BookSheetInfoResponse.Builder builder = BookSheetInfoResponse.newBuilder();
        builder.setPluginCode(req.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        try
        {
            BookSheetInfoData.Builder sheetInfoDataBuilder = BookSheetInfoData.newBuilder();
            
            String type = "1";
            String bookid = req.getParamMapMap().get(ParamConstants.BID);
            String isShowLine = req.getParamMapMap().get(ParamConstants.ISSHOWLINE);
            
            // 是否显示页面底部横线
            if (null != isShowLine)
            {
                sheetInfoDataBuilder.setIsShowLine(isShowLine);
            }
            
            sheetInfoDataBuilder
                .setIsMarginTop(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
            sheetInfoDataBuilder
                .setIsMarginBottom(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
            
            // RankCacheBean rank = PortalBookSheetRankCacheService.getInstance().getShareRankbyBookCacheBean(
            // StringUtils.isBlank(type) ? "1" : type,
            // bookid,
            // StringUtils.isBlank(pageNo) ? "" : pageNo,
            // StringUtils.isBlank(pageSize) ? "" : pageSize);
            
            // TODO CACHE中固定存储1-100的数据,请确认逻辑上对应的改动
            RankCacheBean rank = PortalBookSheetRankCacheService.getInstance()
                .getShareRankbyBookCacheBean(StringUtils.isBlank(type) ? "1" : type, bookid);
            
            sheetInfoDataBuilder.setTotalSheet(String.valueOf(rank.getTotalCount()));
            
            @SuppressWarnings("unchecked")
            List<ShareBookSheet> shareBookSheetList = (List<ShareBookSheet>)rank.getData();
            if (shareBookSheetList != null && shareBookSheetList.size() > 0)
            {
                String sheetId = shareBookSheetList.get(0).getSheetId();
                BookSheetInfo sheetInfo = PortalBookSheetInfoCacheService.getInstance().getBookSheetInfo(sheetId);
                if (null != sheetInfo)
                {
                    sheetInfoDataBuilder.setSheetName(StringUtils.defaultString(sheetInfo.getTitle()));
                    
                    String sheetCollectNum = StringUtils.isBlank(String.valueOf(sheetInfo.getCollectNum())) ? "0"
                        : String.valueOf(sheetInfo.getCollectNum());
                    if (Double.valueOf(sheetCollectNum) > 10000)
                    {
                        sheetCollectNum = String.valueOf(Math.round(Double.valueOf(sheetCollectNum) / 10000)) + "万";
                    }
                    sheetInfoDataBuilder.setSheetCollectNum(sheetCollectNum);
                    
                    sheetInfoDataBuilder.setSheetDesc(StringUtils.defaultString(sheetInfo.getSheetdesc()));
                    sheetInfoDataBuilder
                        .setSheetNum(StringUtils.isNotBlank(sheetInfo.getBooknum()) ? sheetInfo.getBooknum() : "0");
                    
                    // 获取书单作者
                    String creator = null;
                    String sheetMsisdn = sheetInfo.getMsisdn();
                    if (StringUtils.isNotEmpty(sheetMsisdn))
                    {
                        UserInfoSNS user = PortalUserInfoSNSCacheService.getInstance().getUserInfoSNS(sheetMsisdn, "");
                        if (null != user)
                        {
                            creator = user.getNickName();
                        }
                        else
                        {
                            creator =
                                sheetMsisdn.substring(0, 3) + "****" + sheetMsisdn.substring(sheetMsisdn.length() - 4);
                        }
                    }
                    sheetInfoDataBuilder.setSheetAuthor(StringUtils.isBlank(creator) ? "匿名" : creator);
                }
            }
            
            // 获取封面
            sheetInfoDataBuilder.setSheetCover(StringUtils.defaultString(PortalBookCoverCacheService.getInstance()
                .getBookCover(bookid, PropertiesConfig.getProperty("cdnUrl"))));
            
            // 获取配置项
            String title = PropertiesConfig.getProperty("sheetTitle");
            sheetInfoDataBuilder.setSheetTitle(title);
            String moreLink = PropertiesConfig.getProperty("moreLink");
            sheetInfoDataBuilder.setMoreLink(moreLink);
            String moreLinkDesc = PropertiesConfig.getProperty("moreLinkDesc");
            sheetInfoDataBuilder.setMoreLinkDesc(moreLinkDesc);
            
            // 判断是否ajax请求
            String isAjax = req.getParamMapMap().get(ParamConstants.ISAJAX);
            if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = req.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                sheetInfoDataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            
            builder.setData(sheetInfoDataBuilder.build());
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
        catch (Exception e)
        {
            logger.error("BookSheetServiceImpl-BookSheetInfo  error, identityId:{},e:{}",
                CommonHttpUtil.getIdentity(),
                e);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug( "Exit BookSheetImpl-v1，identityId:{} response :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<BookSheetInfoResponse>(builder.build());
    }
    
}
