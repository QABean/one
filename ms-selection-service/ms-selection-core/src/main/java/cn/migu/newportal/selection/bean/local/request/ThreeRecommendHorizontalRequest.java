package cn.migu.newportal.selection.bean.local.request;

import java.util.Map;

import cn.migu.newportal.selection.bean.BaseResponseBean;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;

/**
 * 推荐三封面横向列表请求参数封装
 * 
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since
 */
public class ThreeRecommendHorizontalRequest extends BaseResponseBean
{
    
    private static final long serialVersionUID = 1L;
    
    /** 组件实例id */
    private String instanceId;
    
    /** 是否展示上边距 */
    private String isMarginTop;
    
    /** 是否显示底部划线 */
    private String isMarginBottom;
    
    /** 是否显示上边距 */
    private String isPaddingTop;
    
    /** 是否展示下划线 */
    private String isShowLine;
    
    /** 图书id1 */
    private String bookid1;
    
    /** 图书id2 */
    private String bookid2;
    
    /** 图书id2 */
    private String bookid3;
    
    /** 图书分类1 */
    private String bookClass1;
    
    /** 图书分类2 */
    private String bookClass2;
    
    /** 图书分类3 */
    private String bookClass3;
    
    /** 图书名称显示类型1 */
    private String nameShowType1;
    
    /** 图书名称显示类型2 */
    private String nameShowType2;
    
    /** 图书名称显示类型3 */
    private String nameShowType3;
    
    /** 链接id列表 */
    private String link_id_list;
    
    public String getInstanceId()
    {
        return instanceId;
    }
    
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }
    
    public String getIsMarginTop()
    {
        return isMarginTop;
    }
    
    public void setIsMarginTop(String isMarginTop)
    {
        this.isMarginTop = isMarginTop;
    }
    
    public String getIsMarginBottom()
    {
        return isMarginBottom;
    }
    
    public void setIsMarginBottom(String isMarginBottom)
    {
        this.isMarginBottom = isMarginBottom;
    }
    
    public String getIsPaddingTop()
    {
        return isPaddingTop;
    }
    
    public void setIsPaddingTop(String isPaddingTop)
    {
        this.isPaddingTop = isPaddingTop;
    }
    
    public String getIsShowLine()
    {
        return isShowLine;
    }
    
    public void setIsShowLine(String isShowLine)
    {
        this.isShowLine = isShowLine;
    }
    
    public String getBookid1()
    {
        return bookid1;
    }
    
    public void setBookid1(String bookid1)
    {
        this.bookid1 = bookid1;
    }
    
    public String getBookid2()
    {
        return bookid2;
    }
    
    public void setBookid2(String bookid2)
    {
        this.bookid2 = bookid2;
    }
    
    public String getBookid3()
    {
        return bookid3;
    }
    
    public void setBookid3(String bookid3)
    {
        this.bookid3 = bookid3;
    }
    
    public String getBookClass1()
    {
        return bookClass1;
    }
    
    public void setBookClass1(String bookClass1)
    {
        this.bookClass1 = bookClass1;
    }
    
    public String getBookClass2()
    {
        return bookClass2;
    }
    
    public void setBookClass2(String bookClass2)
    {
        this.bookClass2 = bookClass2;
    }
    
    public String getBookClass3()
    {
        return bookClass3;
    }
    
    public void setBookClass3(String bookClass3)
    {
        this.bookClass3 = bookClass3;
    }
    
    public String getNameShowType1()
    {
        return nameShowType1;
    }
    
    public void setNameShowType1(String nameShowType1)
    {
        this.nameShowType1 = nameShowType1;
    }
    
    public String getNameShowType2()
    {
        return nameShowType2;
    }
    
    public void setNameShowType2(String nameShowType2)
    {
        this.nameShowType2 = nameShowType2;
    }
    
    public String getNameShowType3()
    {
        return nameShowType3;
    }
    
    public void setNameShowType3(String nameShowType3)
    {
        this.nameShowType3 = nameShowType3;
    }
    
    public String getLink_id_list()
    {
        return link_id_list;
    }
    
    public void setLink_id_list(String link_id_list)
    {
        this.link_id_list = link_id_list;
    }
    
    public ThreeRecommendHorizontalRequest()
    {
    }
    
    public ThreeRecommendHorizontalRequest(Map<String, String>  params)
    {
        initThreeRecommendHorizontal(params);
    }
    public void initThreeRecommendHorizontal(Map<String, String>  params)
    {
        instanceId = params.get(ParamConstants.INSTANCEID);
        this.setInstanceId(instanceId);
        
        pluginCode = params.get(ParamConstants.PLUGINCODE);
        pluginCode = ParamUtil.getPluginCode(pluginCode);
        this.setPluginCode(pluginCode);
        
        isMarginTop = params.get(ParamConstants.ISMARGINTOP);
        isMarginTop = ParamUtil.getIsMarginTop(isMarginTop);
        this.setIsMarginTop(isMarginTop);
        
        isMarginBottom = params.get(ParamConstants.ISMARGINBOTTOM);
        isMarginBottom = ParamUtil.getIsMarginBottom(isMarginBottom);
        this.setIsMarginBottom(isMarginBottom);
        
        isPaddingTop = params.get(ParamConstants.ISPADDINGTOP);
        isPaddingTop = ParamUtil.getisPaddingTop(isPaddingTop);
        this.setIsPaddingTop(isPaddingTop);
        
        isShowLine = params.get(ParamConstants.ISSHOWLINE);
        isShowLine = ParamUtil.getIsShowLine(isShowLine, ParamUtil.FALSE);
        this.setIsShowLine(isShowLine);
        
        bookid1 = params.get(ParamConstants.BOOKID1);
        this.setBookid1(bookid1);
        
        bookid2 = params.get(ParamConstants.BOOKID2);
        this.setBookid2(bookid2);
        
        bookid3 = params.get(ParamConstants.BOOKID3);
        this.setBookid3(bookid3);
        
        bookClass1 = params.get(ParamConstants.BOOKCLASS1);
        this.setBookClass1(bookClass1);
        
        bookClass2 = params.get(ParamConstants.BOOKCLASS2);
        this.setBookClass2(bookClass2);
        
        bookClass3 = params.get(ParamConstants.BOOKCLASS3);
        this.setBookClass3(bookClass3);
        
        nameShowType1 = params.get(ParamConstants.NAMESHOWTYPE1);
        this.setNameShowType1(nameShowType1);
        
        nameShowType2 = params.get(ParamConstants.NAMESHOWTYPE2);
        this.setNameShowType2(nameShowType2);
        
        nameShowType3 = params.get(ParamConstants.NAMESHOWTYPE3);
        this.setNameShowType3(nameShowType3);
        
        link_id_list = params.get(ParamConstants.LINK_ID_LIST);
        this.setLink_id_list(link_id_list);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ThreeRecommendHorizontalBean [instanceId=");
        builder.append(instanceId);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", bookid1=");
        builder.append(bookid1);
        builder.append(", bookid2=");
        builder.append(bookid2);
        builder.append(", bookid3=");
        builder.append(bookid3);
        builder.append(", bookClass1=");
        builder.append(bookClass1);
        builder.append(", bookClass2=");
        builder.append(bookClass2);
        builder.append(", bookClass3=");
        builder.append(bookClass3);
        builder.append(", nameShowType1=");
        builder.append(nameShowType1);
        builder.append(", nameShowType2=");
        builder.append(nameShowType2);
        builder.append(", nameShowType3=");
        builder.append(nameShowType3);
        builder.append(", link_id_list=");
        builder.append(link_id_list);
        builder.append("]");
        return builder.toString();
    }
}
