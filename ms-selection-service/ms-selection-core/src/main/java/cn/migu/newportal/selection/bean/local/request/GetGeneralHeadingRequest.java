package cn.migu.newportal.selection.bean.local.request;

import java.io.Serializable;
import java.util.Map;

import cn.migu.newportal.util.bean.common.BaseRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.RequestCheckUtil;

/**
 * 通用标题请求封装
 * @author hushanqing
 * @version C10 2017年8月30日
 * @since SDP V300R003C10
 */
public class GetGeneralHeadingRequest extends BaseRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 组件编号 */
    private String pluginCode;
    
    /** 组件实例id */
    private String instanceId;
    
    /** 是否显示上划线 0:不显示;1:显示 */
    private String isMarginTop;
    
    /** 是否显示底部划线划线 0:不显示;1:显示 */
    private String isMarginBottom;
    
    /** 是否显示上边框 0:不显示；1:显示 */
    private String isPaddingTop;
    
    /** 是否展示下划线：1:展示;0:不展示,默认0 */
    private String isShowLine;
    
    /** 标题后缀类型 1：文字 ,2：时间,0:无 */
    private String suffix;
    
    /** 会员（1:是;0:否） */
    private String huiyuan;
    
    /** 标题标签（1：无；2：黄色框，；3：自签书特有） */
    private String label;
    
    /** 倒计时类型（1：当天；2：手动） */
    private String countType;
    
    /** 手动开始时间 */
    private String startTime;
    
    /** 手动结束时间 */
    private String endTime;
    
    /** 专区id */
    private String nid;
    
    /** 是否跳转专区（1:是;0:否，默认0） */
    private String isNodeUrl;
    
    /** 专区显示名称 */
    private String alias;
    
    /** 后缀连接描述 */
    private String urlDesc;
    
    /** 后缀链接地址 */
    private String url;
    
    /** 标题内容 */
    private String title;
    
    /** 标题标签文本（富文本） */
    private String content;
    
    /** 小文本内容（富文本） */
    private String textmin;
    
    /**倒计时未开始时是否展示 ：0 不提示 1 提示*/
    private String isShowReminder;
    
    public String getPluginCode()
    {
        return pluginCode;
    }
    
    public void setPluginCode(String pluginCode)
    {
        this.pluginCode = pluginCode;
    }
    
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
    
    public String getSuffix()
    {
        return suffix;
    }
    
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }
    
    public String getHuiyuan()
    {
        return huiyuan;
    }
    
    public void setHuiyuan(String huiyuan)
    {
        this.huiyuan = huiyuan;
    }
    
    public String getLabel()
    {
        return label;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }
    
    public String getCountType()
    {
        return countType;
    }
    
    public void setCountType(String countType)
    {
        this.countType = countType;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public String getNid()
    {
        return nid;
    }
    
    public void setNid(String nid)
    {
        this.nid = nid;
    }
    
    public String getIsNodeUrl()
    {
        return isNodeUrl;
    }
    
    public void setIsNodeUrl(String isNodeUrl)
    {
        this.isNodeUrl = isNodeUrl;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public String getUrlDesc()
    {
        return urlDesc;
    }
    
    public void setUrlDesc(String urlDesc)
    {
        this.urlDesc = urlDesc;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getTextmin()
    {
        return textmin;
    }
    
    public void setTextmin(String textmin)
    {
        this.textmin = textmin;
    }
    
    public GetGeneralHeadingRequest()
    {
        
    }
       
    
    public String getIsShowReminder()
    {
        return isShowReminder;
    }

    public void setIsShowReminder(String isShowReminder)
    {
        this.isShowReminder = isShowReminder;
    }

    public GetGeneralHeadingRequest(Map<String, String> params)
    {
        super(params);
        initGetGeneralHeadingRequest(params);
    }
    
    private void initGetGeneralHeadingRequest(Map<String, String> params)
    {
        instanceId = params.get(ParamConstants.INSTANCEID);
        this.setInstanceId(instanceId);
        
        pluginCode = params.get(ParamConstants.PLUGINCODE);
        pluginCode = RequestCheckUtil.getPluginCode(pluginCode);
        this.setPluginCode(pluginCode);
        
        isMarginTop = params.get(ParamConstants.ISMARGINTOP);
        isMarginTop = RequestCheckUtil.getCSSType(isMarginTop);
        this.setIsMarginTop(isMarginTop);
        
        isMarginBottom = params.get(ParamConstants.ISMARGINBOTTOM);
        isMarginBottom = RequestCheckUtil.getCSSType(isMarginBottom);
        this.setIsMarginBottom(isMarginBottom);
        
        isPaddingTop = params.get(ParamConstants.ISPADDINGTOP);
        isPaddingTop = RequestCheckUtil.getCSSType(isPaddingTop);
        this.setIsPaddingTop(isPaddingTop);
        
        isShowLine = params.get(ParamConstants.ISSHOWLINE);
        isShowLine = RequestCheckUtil.getCSSType(isShowLine);
        this.setIsShowLine(isShowLine);
        
        suffix = params.get(ParamConstants.SUFFIX);
        suffix = RequestCheckUtil.getSuffix(suffix, Types.SUFFIX_NONE);
        this.setSuffix(suffix);
        
        huiyuan = params.get(ParamConstants.HUIYUAN);
        huiyuan = RequestCheckUtil.getOneOrTwo(huiyuan,Types.NOT_HUIYUAN);
        this.setHuiyuan(huiyuan);
        
        label = params.get(ParamConstants.LABEL);
        label = RequestCheckUtil.getLabel(label, Types.LABEL_NONE);
        this.setLabel(label);
        
        countType = params.get(ParamConstants.COUNTTYPE);
        countType = RequestCheckUtil.getOneOrTwo(countType, Types.COUNTTYPE_CURRENT);
        this.setCountType(countType);
        
        startTime = params.get(ParamConstants.STARTTIME);
        this.setStartTime(StringTools.nvl(startTime));
        
        endTime = params.get(ParamConstants.ENDTIME);
        this.setEndTime(StringTools.nvl(endTime));
        
        nid = params.get(ParamConstants.NID);
        this.setNid(StringTools.nvl(nid));
        
        isNodeUrl = params.get(ParamConstants.ISNODEURL);
        isNodeUrl= RequestCheckUtil.getCSSType(isNodeUrl);
        this.setIsNodeUrl(isNodeUrl);
        
        alias = params.get(ParamConstants.ALIAS);
        this.setAlias(StringTools.nvl(alias));
        
        urlDesc = params.get(ParamConstants.URLDESC);
        this.setUrlDesc(StringTools.nvl(urlDesc));
        
        url = params.get(ParamConstants.URL);
        this.setUrl(StringTools.nvl(url));
        
        title = params.get(ParamConstants.TITLE);
        this.setTitle(StringTools.nvl(title));
        
        content = params.get(ParamConstants.CONTENT);
        this.setContent(StringTools.nvl(content));
        
        textmin = params.get(ParamConstants.TEXTMIN);
        this.setTextmin(StringTools.nvl(textmin));
        
        isShowReminder = params.get(ParamConstants.ISSHOWREMINDER);
        isShowReminder = RequestCheckUtil.getCSSType(isShowReminder);
        this.setIsShowReminder(isShowReminder);
        
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GetGeneralHeadingRequest [pluginCode=");
        builder.append(pluginCode);
        builder.append(", instanceId=");
        builder.append(instanceId);
        builder.append(", isMarginTop=");
        builder.append(isMarginTop);
        builder.append(", isMarginBottom=");
        builder.append(isMarginBottom);
        builder.append(", isPaddingTop=");
        builder.append(isPaddingTop);
        builder.append(", isShowLine=");
        builder.append(isShowLine);
        builder.append(", suffix=");
        builder.append(suffix);
        builder.append(", huiyuan=");
        builder.append(huiyuan);
        builder.append(", label=");
        builder.append(label);
        builder.append(", countType=");
        builder.append(countType);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", nid=");
        builder.append(nid);
        builder.append(", isNodeUrl=");
        builder.append(isNodeUrl);
        builder.append(", alias=");
        builder.append(alias);
        builder.append(", urlDesc=");
        builder.append(urlDesc);
        builder.append(", url=");
        builder.append(url);
        builder.append(", title=");
        builder.append(title);
        builder.append(", content=");
        builder.append(content);
        builder.append(", textmin=");
        builder.append(textmin);
        builder.append(", isShowReminder=");
        builder.append(isShowReminder);
        builder.append("]");
        return builder.toString();
    }
    
    
}
