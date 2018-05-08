package cn.migu.newportal.selection.util;

import cn.migu.newportal.util.constants.BookContants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.string.StringTools;

public class ParamUtil
{
    private static final String JUMPTYPE_LINK = "1";
    
    private static final String JUMPTYPE_NODE = "2";
    
    public static final String TRUE = "1";
    
    public static final String FALSE = "0";
    
    private static final String BACKGROUNDTY = "2";
    
    /**
     * 获取显示个数，最后显示个数为默认值的倍数。
     * 
     * @author hsq
     * @param showNum
     * @param def
     * @return
     */
    public static int getShowNum(String showNum, int def)
    {
        int num = 0;
        if (StringTools.isEmpty(showNum) || StringTools.isEq(showNum,"0"))
        {
            num = 0;
        }
        else
        {
            try
            {
                // num = Double.valueOf(showNum).intValue();
                num = StringTools.toInt(showNum, def);
                if (num <= def)
                {
                    num = def;
                }
                else
                {
                    if (def != 0)
                    {
                        num = num - num % def;
                    }
                }
            }
            catch (Exception e)
            {
                num = def;
            }
            
        }
        return num;
    }
    
    /**
     * 获取点击倍数
     * 
     * @author hsq
     * @param largeSize 倍数
     * @param def 默认值 1
     * @return
     */
    public static int getLargeSize(String largeSize, int def)
    {
        int num = 1;
        if (StringTools.isEmpty(largeSize) || "0".equals(largeSize))
        {
            return def;
        }
        else
        {
            try
            {
                num = Double.valueOf(largeSize).intValue();
                if(num<=0) num =def;
            }
            catch (Exception e)
            {
                return def;
            }
        }
        return num;
    }
    
    /**
     * 获取显示类型
     * 
     * @author hsq
     * @param showType 展示种类1:书名;2:书名+作者名;3:书名+点击量;4:书名+章节数;5:书名+文案; 默认1
     * @param def 默认值
     * @return
     */
    public static String getShowType(String showType, String def)
    {
        String type = def;
        switch (showType)
        {
            // 书名
            case SelectionConstants.SHOW_TYPE_BOOK_NAME:
                // 书名+作者名
            case SelectionConstants.SHOW_TYPE_AUTHORNAME:
                // 书名+点击量
            case SelectionConstants.SHOW_TYPE_CLICK:
                // 书名+章节数
            case SelectionConstants.SHOW_TYPE_CHAPTERSIZE:
                // 书名+文案
            case SelectionConstants.SHOW_TYPE_LINKDESC:
                type = showType;
                break;
            
            default:
                type = def;
                break;
        }
        return type;
    }
    
    /**
     * 获取是否轮询
     * 
     * @author hsq
     * @param isCycle 是否支持轮训 1：轮询 0：不轮询
     * @param def
     * @return
     */
    public static String getIsCycle(String isCycle, String def)
    {
        if (SelectionConstants.IS_CYCLE.equals(isCycle))
        {
            return isCycle;
        }
        return def;
    }
    
    /**
     * 获取是否展示下划线
     * 
     * @author hsq
     * @param isShowLine 是否展示下划线 1：展示 0：不展示
     * @param def
     * @return
     */
    public static String getIsShowLine(String isShowLine, String def)
    {
        if (SelectionConstants.IS_SHOWLINE.equals(isShowLine))
        {
            return isShowLine;
        }
        return def;
    }
    
    /**
     * 获取排序类型
     * 
     * @author hsq
     * @param sortType 排序1:更新时间;2:上架时间;3:入库时间；6:排序值升序，非必填，默认1
     * @param def 默认值
     * @return
     */
    public static String getSortType(String sortType, String def)
    {
        String type = def;
        if (StringTools.isEmpty(sortType))
        {
            return def;
        }
        
        switch (sortType)
        {
            // 更新时间
            case BookContants.BOOK_UPDATE:
                // 上架时间
            case BookContants.BOOK_ON_NODE:
                // 入库时间
            case BookContants.BOOK_IN_STACK:
                // 省份
            case BookContants.BOOK_IN_PROVINCE:
                // 城市
            case BookContants.BOOK_IN_CITY:
                /** 不轮循的时候，按照排序值排序 */
            case BookContants.BOOK_IN_CHOOSE:
                type = sortType;
                break;
            
            default:
                type = def;
                break;
        }
        return type;
    }
    
    /**
     * 获取腰封展示类型
     *
     * @author yejiaxu
     * @param isShowYF 腰封：1：不展示；2:蓝色；3：红色；4:绿色
     * @param def
     * @return
     */
    public static String getIsShowYF(String isShowYF, String def)
    {
        String type = def;
        if (StringTools.isEmpty(isShowYF))
        {
            return def;
        }
        switch (isShowYF)
        {
            // 1:不展示腰封
            case SelectionConstants.IS_SHOWYF_NULL:
                // 2:展示蓝色
            case SelectionConstants.IS_SHOWYF_BLUE:
                // 3：展示红色
            case SelectionConstants.IS_SHOWYF_RED:
                // 4:展示绿色
            case SelectionConstants.IS_SHOWYF_GREEN:
                type = isShowYF;
                break;
            default:
                type = def;
                break;
            
        }
        return type;
    }
    
    public static String getPluginCode(String pluginCode)
    {
        if (StringTools.isEmpty(pluginCode))
        {
            return "";
        }
        return pluginCode;
    }
    
    public static String checkParamter(String[] ableValue, String param, String defualt)
    {
        
        boolean flage = false;
        for (int i = 0; i < ableValue.length; i++)
        {
            if (StringTools.isEq(ableValue[i], param))
            {
                flage = true;
                break;
            }
        }
        
        if (!flage)
        {
            return defualt;
        }
        
        return param;
    }
    
    /**
     * 检查返回参数中为空，报文输出空值
     *
     * @author yejiaxu
     * @param param
     * @return
     */
    public static String checkResponse(String param)
    {
        if (StringTools.isEmpty(param))
        {
            return " ";
        }
        return param;
    }
    
    /**
     * 投票倍数处理，1:1;2:2;3:3;4:4;5:5
     *
     * @author yejiaxu
     * @param largize
     * @return
     */
    public static int getVoteLargize(String largize)
    {
        int temp = 1;
        if (StringTools.isEmpty(largize))
        {
            return temp;
        }
        switch (largize)
        {
            // 1:1倍
            case SelectionConstants.VOTE_LARGIZE_ONE:
                temp = 1;
                break;
            // 2:2倍
            case SelectionConstants.VOTE_LARGIZE_TWO:
                temp = 2;
                break;
            // 3:3倍
            case SelectionConstants.VOTE_LARGIZE_THREE:
                temp = 3;
                break;
            // 4:4倍
            case SelectionConstants.VOTE_LARGIZE_FOUR:
                temp = 4;
                break;
            // 5:5倍
            case SelectionConstants.VOTE_LARGIZE_FIVE:
                temp = 5;
                break;
            
            default:
                temp = 1;
                break;
        }

        return temp;
    }

    public static String checkShowType(String showType)
    {
        if (StringTools.isEq(showType, ParamConstants.NUMONE) || StringTools.isEq(showType, ParamConstants.NUMTWO) ||
            StringTools.isEq(showType, ParamConstants.NUMTHREE))
        {
            return showType;
        }
        return ParamConstants.NUMONE;
    }
    
    public static String checkIsplayNew(String isplayNew)
    {
        if (StringTools.isEq(isplayNew, ParamConstants.NUMONE) || StringTools.isEq(isplayNew, ParamConstants.NUMTWO))
        {
            return isplayNew;
        }
        return ParamConstants.NUMONE;
    }
    
    public static String checkDataSrc(String dataSrc)
    {
        if (StringTools.isEq(dataSrc, ParamConstants.NUMONE) || StringTools.isEq(dataSrc, ParamConstants.NUMTWO))
        {
            return dataSrc;
        }
        return ParamConstants.NUMONE;
    }
    
    /**
     * 获取跳转类型
     * 
     * @author hsq
     * @param jumpType
     * @param def
     * @return
     */
    public static String getJumpType(String jumpType, String def)
    {
        if (StringTools.isEmpty(jumpType))
        {
            return def;
        }
        if (ParamUtil.JUMPTYPE_LINK.equals(jumpType) || ParamUtil.JUMPTYPE_NODE.equals(jumpType))
        {
            return jumpType;
        }
        return def;
    }
    
    /**
     * 获取默认值
     * 
     * @author
     * @param jumpType
     * @param def
     * @return
     */
    public static String getMarginValue(String jumpType, String def)
    {
        if (StringTools.isEmpty(jumpType))
        {
            return def;
        }
        return jumpType;
    }
    
    /**
     * 验证获取isMarginTop
     * @author hushanqing
     * @param isMarginTop
     * @return
     */
    public static String getIsMarginTop(String isMarginTop)
    {
        if (ParamUtil.TRUE.equals(isMarginTop))
        {
            return isMarginTop;
        }
        return ParamUtil.FALSE;
    }
    
    /**
     * 验证获取isMarginBottom
     * @author hushanqing
     * @param
     * @return
     */
    public static String getIsMarginBottom(String isMarginBottom)
    {
        if (ParamUtil.TRUE.equals(isMarginBottom))
        {
            return isMarginBottom;
        }
        return ParamUtil.FALSE;
    }
    
    /**
     * 验证获取isPaddingTop
     * @author hushanqing
     * @param
     * @return
     */
    public static String getisPaddingTop(String isPaddingTop)
    {
        if (ParamUtil.TRUE.equals(isPaddingTop))
        {
            return isPaddingTop;
        }
        return ParamUtil.FALSE;
    }
    
    /**
     * 验证获取backGroundType
     * @author zhangmm
     * @param backGroundType
     * @return
     */
    public static String getbackGroundType(String backGroundType)
    {
        if (ParamUtil.BACKGROUNDTY.equals(backGroundType))
        {
            return backGroundType;
        }
        return ParamUtil.TRUE;
    }
}
