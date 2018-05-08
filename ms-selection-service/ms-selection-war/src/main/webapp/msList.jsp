<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.ContextLoader"%>
<%@page import="cn.migu.wheat.client.ServerNode"%>
<%@page import="cn.migu.wheat.service.ServiceConsumer"%>
<%@page import="cn.migu.wheat.client.AbstractClient"%>
<%@page import="cn.migu.wheat.client.AbstractPool"%>
<%@page import="java.util.concurrent.ConcurrentMap"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="cn.migu.wheat.client.ConsistentHashPool"%>
<%@page import="java.util.concurrent.ConcurrentSkipListMap"%>
<%
	Map<String,ConcurrentMap<String, ServerNode>> map1 = new HashMap<String,ConcurrentMap<String, ServerNode>>();
	Map<String,ConcurrentSkipListMap<Integer, ServerNode>> map2 = new HashMap<String,ConcurrentSkipListMap<Integer, ServerNode>>();
	Map<String,String> map5 = new HashMap<String,String>();
	//用于重建buckets集合
	Map<String,ConsistentHashPool> map3 = new HashMap<String,ConsistentHashPool>();
    WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
    if (null != wac)
    {
        Map<String, ServiceConsumer> consumerMap = wac.getBeansOfType(ServiceConsumer.class);
        if (null != consumerMap && consumerMap.size() > 0)
        {
            int consumerIndex = 0;
            Set<String> keySet = consumerMap.keySet();
            for (String key : keySet)
            {
                consumerIndex++;
                ServiceConsumer sc = consumerMap.get(key);
                map5.put(key, sc.canonicalName());
                AbstractClient ac = sc.getClient();
                if (null != ac)
                {
                   /*  Class c2 = ac.getClass();
                    Field pool = c2.getSuperclass().getDeclaredField("pool");
                    pool.setAccessible(true);
                    Object o2 = pool.get(ac); */
                    AbstractPool o2 = ac.getPool();
                    if (o2 instanceof AbstractPool)
                    {
                        AbstractPool ap = (AbstractPool)o2;
                        if (null != ap)
                        {
                            Class c3 = ap.getClass();
                            
                            Field servers = c3.getSuperclass().getDeclaredField("servers");
                            servers.setAccessible(true);
                            Object o3 = servers.get(ap);
                            if (o3 instanceof ConcurrentMap)
                            {
                                ConcurrentMap<String, ServerNode> map = (ConcurrentMap<String, ServerNode>)o3;
                                if (null != map && map.size() > 0)
                                {
                                    map1.put(key, map);
                                }
                                else
                                {
                                    map1.put(key, map);
                                    //out.println("null");
                                }
                            }
                            if (ap instanceof ConsistentHashPool)
                            {
                                ConsistentHashPool chp = (ConsistentHashPool)ap;
                                map3.put(key, chp);
                                Class c4 = chp.getClass();
                                Field f4 = c4.getDeclaredField("buckets");
                                f4.setAccessible(true);
                                Object o4 = f4.get(chp);
                                if (o4 instanceof ConcurrentSkipListMap)
                                {
                                    ConcurrentSkipListMap<Integer, ServerNode> map4 =
                                        (ConcurrentSkipListMap<Integer, ServerNode>)o4;
                                    if (null != map4 && map4.size() > 0)
                                    {
                                        map2.put(key, map4);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
%>

 <html>
    <head>
    <title>微服务监控页面</title>
    <style type="text/css">
    <!--
    #ejiaA1 {
    width: 1000px;
    border-top: #E3E3E3 1px solid;
    border-left: #E3E3E3 1px solid;
    }
    #ejiaA1 td,#ejiaA1 th {
    padding: 5px;border-right: #E3E3E3 1px solid;
    border-bottom: #E3E3E3 1px solid;
    font-size:12px;
    height:16px; line-height:16px;
    }
    #ejiaA1 tr td span {color: #686868}
    #ejiaA1 tr td span.st1 {color: green}
    #ejiaA1 tr td span.st2 {color: red}
    #ejiaA1 tr td span.st3 {font-size:18 color:green}
    
    #ejiaA1 tr td span.st4 {
    padding: 5px;border-right: #E3E3E3 1px solid;
    border-bottom: #E3E3E3 1px solid;
    font-size:12px;
    height:16px; line-height:16px;
    }
    --></style>
    </head>
    <body>
    <h>本站点（ms-selection） Consumer 对应的微服务节点列</h>
    <br><br><br>
    <font color="red"><a href = "msList.jsp?rebuildAll=1">重建所有buckets</a>（慎用）</font>
     <br/><br/>
     
 <%
 //重建所有buckets
 String rebuildAll = request.getParameter("rebuildAll");
 if(null !=rebuildAll&&"1".equals(rebuildAll))
 {
     if(map3.size()>0)
     {
         Set<String> map3Keys = map3.keySet();
         for(String map3Key:map3Keys)
         {
             map3.get(map3Key).rebuildBuckets();
         }
         response.sendRedirect("msList.jsp");
     }
 }
 String showKey = request.getParameter("showkey");
 String isShow = request.getParameter("isShow");
if(null != map1&&!map1.isEmpty())
{
Set<String> keySet1 = map1.keySet();
int mainIndex = 0;
for(String key1:keySet1)
{
    mainIndex++;
    ConcurrentMap<String, ServerNode> consumerMap = map1.get(key1);
    Set<String> consumerKeys = consumerMap.keySet();
    int bucketsSize=0;
    if(null !=map2)
    {
        ConcurrentSkipListMap<Integer, ServerNode> bucketsMap_bak = map2.get(key1);
        if(null != bucketsMap_bak)
        {
            bucketsSize = bucketsMap_bak.size();
        }
    }
    %>
    
    ConsumerIndex<%=mainIndex %>:<%=map5.get(key1) %>,下面是可用节点列表
    <%
    for(String consumerKey:consumerKeys)
    {
        ServerNode consumerServerNode = consumerMap.get(consumerKey);
        %>
        <table width="500" border="0" cellspacing="0" cellpadding="0" id="ejiaA1">
    	<tr>
    		<td colspan="4"><span class="st3"><%=consumerKey %>:<%=consumerServerNode.toString() %>*********</span></td>
    	</tr>
 	</table>
        
        <%
    }
 %>
<br>
 <table width="500" border="0" cellspacing="0" cellpadding="0" id="ejiaA1">
 
    <tr>
    <td colspan="4"><span class="st3">下面是buckets集合中的节点信息   
    <a name=<%=mainIndex %> href="msList.jsp?showkey=<%=key1 %>&isShow=1#<%=mainIndex %>">点击这里查看(<%=bucketsSize %>)</a>&nbsp;&nbsp;&nbsp;
    <a href="msList.jsp?showkey=<%=key1 %>&isShow=0#<%=mainIndex %>">点击这里隐藏</a>&nbsp;&nbsp;&nbsp;
    <a href="msList.jsp?isShow=1&showkey=<%=key1 %>&isRebuild=1#<%=mainIndex %>">重建buckets（当出现服务不可用节点时使用）</a>
    </span></td>
    </tr>
    <%
    	//重建buckets
    	String isRebuild = request.getParameter("isRebuild");
    	if(null !=isRebuild&&"1".equals(isRebuild)&&showKey.equals(key1))
    	{
    	    ConsistentHashPool pool = map3.get(key1);
    	    if(null != pool)
    	    {
    	        pool.rebuildBuckets();
    	    }
    	    response.sendRedirect("msList.jsp?isShow=1&showkey="+key1+"#"+mainIndex);
    	}
    	
    	if(!map2.isEmpty())
    	{
    	    ConcurrentSkipListMap<Integer, ServerNode> bucketsMap = map2.get(key1);
    	    if(null!=bucketsMap&& !bucketsMap.isEmpty())
    	    {
    	        if(null!=showKey&&showKey.equals(key1)&&null !=isShow&&isShow.equals("1"))
    	        {
    	            NavigableSet<Integer> bucketsKeySet = bucketsMap.keySet();
    	            int index0 = 0;
    	            %>
    	            <tr>
    					<th>序号</th><th>节点key</th><th>节点信息</th><th>是否可用</th>
    				</tr>
    	            <%
        	        for(Integer bucketsKey:bucketsKeySet)
        	        {
        	            index0++;
        	            ServerNode serverNode = bucketsMap.get(bucketsKey);
        	            if(serverNode.available()){
        	                //节点可用
        	            %>
        	             <tr>
        	             	<td><span class="st4"><%=index0 %></span></td>
        					<td><span class="st4"><%=bucketsKey %></span></td>
        					<td><span class="st4"><%=serverNode.toString()%></span></td>
        					<td><span class="st4"><%=serverNode.available()%></span></td>
        				</tr>
        	            <%}else
        	            {
        	                //节点不可用
        	                %>
        	             <tr>
        	             	<td><span class="st1"><%=index0 %></span></td>
        					<td><span class="st2"><%=bucketsKey %></span></td>
        					<td><span class="st2"><%=serverNode.toString()%></span></td>
        					<td><span class="st1"><%=serverNode.available()%></span></td>
        				</tr>
        	                <%
        	            }
        	        }
    	        }
    	    }
    	}
    
    %>
    </table>
<br/>      
<%
}
}
 %>
    
    </body>
    </html>
