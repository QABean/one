package cn.migu.newportal.selection.bean;

import java.io.Serializable;



public class LimitNodeItem implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3670542422049875514L;

    //真正的图书个数
    private int totalCnt = 0;
    
    //从接口返回的消耗积分
    private String[] bookIds = null;
    
    private String adaptProvinceId = "000";
    

    private String adaptCityId = "000";
    
    

    public int getTotalCnt()
    {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt)
    {
        this.totalCnt = totalCnt;
    }

    public String[] getBookIds()
    {
        return bookIds;
    }

    public void setBookIds(String[] bookIds)
    {
        this.bookIds = bookIds;
    }

    public String getAdaptProvinceId()
    {
        return adaptProvinceId;
    }

    public void setAdaptProvinceId(String adaptProvinceId)
    {
        this.adaptProvinceId = adaptProvinceId;
    }

    public String getAdaptCityId()
    {
        return adaptCityId;
    }

    public void setAdaptCityId(String adaptCityId)
    {
        this.adaptCityId = adaptCityId;
    }
    
    
    
}

