package cn.migu.newportal.selection.bean.local.request;

import cn.migu.newportal.util.bean.common.BaseRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;

import java.util.Map;

/**
 * 智能广告封装请求体
 * @author wangqiang
 * @version C10 2018年03月29日
 * @since SDP V300R003C10
 */
public class IntelliAdRequest extends BaseRequest
{
    // 推荐位编号
   private String recNum;
   //外部广告id
   private String wzid;
   //图片尺寸枚举，1 大，2 小
   private String picSize;

    public IntelliAdRequest(Map<String, String> paramMap)
    {
        super(paramMap);
        init(paramMap);
    }

    /**
     * 对参数进行初始化,并进行一些校验
     *
     * @param paramMap
     * @author wangqiang
     */
    private void init(Map<String, String> paramMap)
    {
        if (Util.isNotEmpty(paramMap))
        {
            this.recNum = StringTools.nvl(paramMap.get(ParamConstants.RECNUM));
            this.wzid = StringTools.nvl(paramMap.get(ParamConstants.WZID));
            this.picSize = StringTools.isEq(paramMap.get(ParamConstants.PICSIZE),ParamConstants.PICSIZE_SMALL)?
                ParamConstants.PICSIZE_SMALL: ParamConstants.PICSIZE_BIG;
        }
    }

    public String getRecNum()
    {
        return recNum;
    }

    public void setRecNum(String recNum)
    {
        this.recNum = recNum;
    }

    public String getWzid()
    {
        return wzid;
    }

    public void setWzid(String wzid)
    {
        this.wzid = wzid;
    }

    public String getPicSize()
    {
        return picSize;
    }

    public void setPicSize(String picSize)
    {
        this.picSize = picSize;
    }
}
