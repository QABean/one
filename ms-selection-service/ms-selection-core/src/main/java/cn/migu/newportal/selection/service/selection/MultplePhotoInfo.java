package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.service.MultiplePhotoServiceImpl;
import cn.migu.newportal.selection.util.ContentUtil;
import cn.migu.newportal.selection.util.UserUtils;
import cn.migu.newportal.util.bean.user.PortalUserInfo;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.MultiplePhotoResponseOuterClass.Books;
import cn.migu.selection.proto.base.MultiplePhotoResponseOuterClass.Data;
import cn.migu.selection.proto.base.MultiplePhotoResponseOuterClass.MultiplePhotoResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

public class MultplePhotoInfo extends ServiceMethodImpl<MultiplePhotoResponse, ComponentRequest>
{
    
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(MultplePhotoInfo.class.getName());
    
    private static final String METHOD_NAME = "getComponentShowInfo";
    
    private MultiplePhotoServiceImpl serviceImpl;
    
    public MultplePhotoInfo()
    {
        super(METHOD_NAME);
    }
    
    
    @ImplementMethod
    public InvokeResult<MultiplePhotoResponse> v1(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter MultplePhotoInfo-v1，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        MultiplePhotoResponse.Builder builder = MultiplePhotoResponse.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable("0");
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        Data.Builder dataBuilder = Data.newBuilder();
        
        // 获取请求参数
        String showNum = request.getParamMapMap().get("showLine");
        String lineNum = request.getParamMapMap().get("lineNumber");
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        String nid = request.getParamMapMap().get(ParamConstants.NID);
        // 计算总推荐图书数目
        Integer totalNum = Integer.valueOf(showNum) * Integer.valueOf(lineNum) + 1;
        dataBuilder.setTotalNum(totalNum);
        
        // 专区获取书籍地域信息
        try
        {
            NodeItem nodeItems = PortalNodeInfoCacheService.getInstance().getNodeItem(nid);
            if (nodeItems == null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug(" MultplePhotoInfo nodeItems is null ,nid:{}",nid);
                }
            }
            else
            {
                Map<String, List<String>> map = nodeItems.getBookMap();
                Map<String, List<String>> proMap = nodeItems.getBookProvinceMap();
                Map<String, List<String>> cityMap = nodeItems.getBookCityMap();
                
                PortalUserInfo userInfo = UserUtils.getUserInfo();// TODO暂时先取测试数据
                String provinceId = userInfo.getUserInfo().getProvinceId();
                String cityId = userInfo.getUserInfo().getCityId();
                List<String> bookIds = new ArrayList<>();
                if (cityMap != null && cityMap.containsKey(cityId)) // 1、市存在,优先去市下面拿书
                {
                    List<String> cityBookIds = cityMap.get(cityId);
                    
                    if (cityBookIds != null)
                    {
                        bookIds = cityBookIds;
                    }
                }
                else // 2、市不存在,则去省下面拿,省也不存在,去全国取
                {
                    if (proMap != null && proMap.containsKey(provinceId))
                    {
                        List<String> provinBookIds = proMap.get(provinceId);
                        bookIds = provinBookIds;
                        
                    }
                    else
                    {
                        if (map != null)
                        {
                            for (Entry<String, List<String>> key : map.entrySet())
                            {
                                List<String> mapBookIds = key.getValue();
                                bookIds.addAll(mapBookIds);
                            }
                        }
                    }
                }
                
                // 判断总数是否大于页面所需要的总数
                if (bookIds != null && bookIds.size() > totalNum)
                {
                    bookIds = bookIds.subList(0, totalNum);
                }
                
                if (bookIds != null && bookIds.size() > 0)
                {
                    List<Books> booksList = new ArrayList<Books>();
                    String bookUrlPrefix = ContentUtil.getBookUrlPrefix(controller);
                    for (String bookId : bookIds)
                    {
                        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
                        // 获取作者信息
                        AuthorInfo authorInfo =
                            PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                        Books.Builder books = Books.newBuilder();
                        books.setAuthorName(StringUtils.defaultString(authorInfo.getAuthorName()));
                        books.setBookDesc(StringUtils.defaultString(bookItem.getLongDescription()));
                        books.setBookName(StringUtils.defaultString(bookItem.getBookName()));
                        // 获取封面信息
                        String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookId,
                            PropertiesConfig.getProperty("cdnUrl"));
                        books.setBookCoverLogo(bookCover);
                        
                        
                        Map<String, String>  param = UesServiceUtils.buildPublicParaMap(null, null);
                        param.put("bid",bookId);
                        param.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));
                        
                        
                        books.setBookUrl(UrlTools.processForView(bookUrlPrefix, param));
                        books.setStatus(StringUtils.defaultString(bookItem.getStatus()));
                        booksList.add(books.build());
                    }
                    dataBuilder.addAllBooks(booksList);
                    builder.setIsvisable("1");
                }
                
                // 是否显示页面底部横线
                if (null != isShowLine)
                {
                    dataBuilder.setIsShowLine(isShowLine);
                }
                
                dataBuilder.setIsMarginTop(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
                dataBuilder.setIsMarginBottom(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
                
                // 判断是否ajax请求
                String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
                if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
                {
                    ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                    paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                    dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
                }
                builder.setData(dataBuilder);
                
                // 设置成功返回码
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
                builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
            }
        }
        catch (Exception e)
        {
            logger.error("MultplePhotoInfoImpl-MultplePhotoInfo error", e);
        }
        
        return new InvokeResult<MultiplePhotoResponse>(builder.build());
    }
    
    public MultiplePhotoServiceImpl getMultiplePhotoServiceImpl()
    {
        return serviceImpl;
    }
}
