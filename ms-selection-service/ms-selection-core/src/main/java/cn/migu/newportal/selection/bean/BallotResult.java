/*
 * 文 件 名:  BallotResult.java 
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  hKF68354 何继顺
 * 修改时间:  May 4, 2012
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package cn.migu.newportal.selection.bean;

/**
 * 投票结果信息
 * 
 * @author  hKF68354 何继顺
 * @version  [版本号, May 4, 2012]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BallotResult
{
    /**
     * 结果码
     */
    private int resultCode;

    /**
     * 投票时达到选项限制数时返回限制次数
     */
    private String limit;

    /**
     * 投票未开始时，返回开始时间
     */
    private String beginTime;
    
    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getLimit()
    {
        return limit;
    }

    public void setLimit(String limit)
    {
        this.limit = limit;
    }

    public int getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }
}
