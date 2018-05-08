/*
 * 文 件 名:  BallotItem.java 
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  hKF68354 何继顺
 * 修改时间:  May 3, 2012
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package cn.migu.newportal.selection.bean;

import java.io.Serializable;
import java.util.List;

import com.huawei.iread.server.domain.BallotOptionInfo;

/**
 * 投票活动
 * 
 * @author  hKF68354 何继顺
 * @version  [版本号, May 3, 2012]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BallotItem implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BallotItem() {
        super();
    }

    /**
     * 总投票数
     */
    private String ballotCount;
    /**
     * 总参与人数
     */
    private String ballotorCount;
    /**开始时间*/
    
    private String startTime;
    /**结束时间*/
    
    private String endTime;
    /**状态*/
    
    private String status;
    /**活动标题*/
    
    private String title;
    /**
     * 选项列表
     */
    private List<BallotOptionInfo> ballotOptionList;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * 总投票数
     */
    public String getBallotCount()
    {
        return ballotCount;
    }

    public void setBallotCount(String ballotCount)
    {
        this.ballotCount = ballotCount;
    }

    /**
     * 总参与人数
     */
    public String getBallotorCount()
    {
        return ballotorCount;
    }

    public void setBallotorCount(String ballotorCount)
    {
        this.ballotorCount = ballotorCount;
    }

    public List<BallotOptionInfo> getBallotOptionList()
    {
        return ballotOptionList;
    }

    public void setBallotOptionList(List<BallotOptionInfo> ballotOptionList)
    {
        this.ballotOptionList = ballotOptionList;
    }

    
}
