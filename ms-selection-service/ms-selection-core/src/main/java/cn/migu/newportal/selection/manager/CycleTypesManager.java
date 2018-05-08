package cn.migu.newportal.selection.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huawei.iread.server.constant.Types;

import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.manager.ContentSortManager;
import cn.migu.newportal.cache.cache.service.NewCycleDataCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.util.other.Util;

public class CycleTypesManager
{
    /** 空专区数据源 */
    private static List<String> EMPTY_LIST = new ArrayList<String>(0);
    
    /**
     * 获取轮循专区列表
     * 
     * @param nodes
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String[] getCyceNodeList(String key,String[] nodeids,CycleTypes cycleType)
    {
        return NewCycleDataCacheService.getInstance().getCycleData(key, cycleType, nodeids);
    }
    
    /**
     * 获取专区数据源，支持轮循
     * 
     * @param cycleTy
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getBookListByNodeid(CycleTypes cycleTy,String[] nodeIds,String sortType,String instanceId)
    {
        if (null == nodeIds || 0 == nodeIds.length)
        {
            return EMPTY_LIST;
        }
        List<String> bookIdResult = new ArrayList<String>();
        if (!Util.isEmpty(cycleTy))
        {
            // 如果碎片支持轮循且在轮循时间范围内，则要取轮循的数据
            if (cycleTy.getIsCycle().equals(Types.TRUE))
            {
                nodeIds = getCyceNodeList(instanceId, nodeIds, cycleTy);
            }
            int nodeIdNum = nodeIds.length;
            String[] contentIds = null;
            NodeItem node = null;
            for (int i = 0; i < nodeIdNum; i++)
            {
                node = PortalNodeInfoCacheService.getInstance().getNodeItem(nodeIds[i]);
                if (node != null)
                {
                    contentIds = node.getBookIDs();
                }
                if (Util.isNotEmpty(contentIds))
                {
                    // 获取排序或轮循后的全部书籍ID，不分页
                    String[] bookIDs = ContentSortManager.getInstance().getBookListForNodes(cycleTy,
                        nodeIds[i],
                        sortType,
                        cycleTy.getIsCycle(),
                        instanceId);
                    if (Util.isEmpty(bookIDs))
                    {
                        continue;
                    }
                    bookIdResult.addAll(Arrays.asList(bookIDs));
                }
            }
        }
        return bookIdResult;
    }
}
