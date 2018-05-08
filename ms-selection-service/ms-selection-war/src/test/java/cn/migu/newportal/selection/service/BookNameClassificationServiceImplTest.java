package cn.migu.newportal.selection.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import cn.migu.wheat.XHeaders;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-mvc.xml", "classpath:spring-core.xml"})

public class BookNameClassificationServiceImplTest
{
    private MockMvc mockMvc;
    
    @Autowired
    WebApplicationContext wac;
    
    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    /**
     * 链接轮询处理
     * 
     */
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "1");
        params.put("isCycle", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-03 14:54:31");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917,590001079,399785410");
        params.put("link_id_list", "48087,48088,48089,48090,48091");
        params.put("instanceId", "category_list");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 链接不轮询处理
     * 
     */
    @Test
    public void testLinkNoCycle()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "1");
        params.put("isCycle", "0");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-03 14:54:31");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917,590001079,399785410");
        params.put("link_id_list", "48087,48088,48089,48090");
        params.put("instanceId", "category_list");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 专区不轮询
     * 
     */
    @Test
    public void testNodeNoCycle()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "2");
        params.put("isCycle", "0");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-03 14:54:31");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917,590001079");
        params.put("link_id_list", "48087,48088,48089,48090,48091");
        params.put("instanceId", "category_list");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 无连接列表
     * 
     */
    @Test
    public void testNoLinks()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "1");
        params.put("isCycle", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-03 14:54:31");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917,590001079,399785410");
        params.put("link_id_list", "");
        params.put("instanceId", "category_list");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 无专区列表
     * 
     */
    @Test
    public void testNoNodes()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "2");
        params.put("isCycle", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-03 14:54:31");
        params.put("node_id_list", "");
        params.put("link_id_list", "48087,48088,48089,48090,48091");
        params.put("instanceId", "category_list");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 无组件id
     * 
     */
    @Test
    public void testNoInstanceId()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "category_list");
        params.put("jumpType", "1");
        params.put("isCycle", "0");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("cycleBeginDate", "2017-07-03 14:54:31");
        params.put("cycleEndDate", "2017-07-20 14:54:31");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917,590001079,399785410");
        params.put("link_id_list", "48087,48088,48089,48090,48091");
        params.put("instanceId", "");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.bookNameClassificationService/BookNameClassificationForLink")
                .header(HttpHeaders.USER_AGENT, "andriod")
                .header(XHeaders.CLIENT_ID, "222222")
                .header(XHeaders.REQUEST_ID, "11111").header("X-Real-IP", "10.211.111.10").header("X-Identity-ID", "1222222")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
