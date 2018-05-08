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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

import cn.migu.wheat.XHeaders;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-mvc.xml", "classpath:spring-core.xml"})
public class FreeForCartoonFlowServiceTest
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
     * 轮询处理
     * 
     */
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("showType", "1");
        params.put("pluginCode", "cartoon_list");
        params.put("nameShowType", "1");
        params.put("cornerShowType", "1");
        params.put("sortType", "1");
        params.put("isCycle", "1");
        params.put("cycleNum", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("beginDate", "");
        params.put("endDate", "");
        params.put("showNum", "1");
        params.put("pageNo", "1");
        params.put("book_id_list", "362615352,385539499,352585218,660145759");
        params.put("link_id_list", "48088,48089,48090,48091");
        params.put("isShowLine", "1");
        params.put("largeSize", "1");
        params.put("instanceId", "12345");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.freeForCartoonFlowService/getFreeCartoonFlowShowInfo")
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
     * 排序处理 sortType=1
     * 
     */
    @Test
    public void testViewForSort()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("showType", "1");
        params.put("pluginCode", "cartoon_list");
        params.put("nameShowType", "1");
        params.put("cornerShowType", "1");
        params.put("sortType", "1");
        params.put("isCycle", "0");
        params.put("cycleNum", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("beginDate", "");
        params.put("endDate", "");
        params.put("showNum", "1");
        params.put("pageNo", "1");
        params.put("book_id_list", "362615352,385539499,352585218,660145759");
        params.put("link_id_list", "48088,48089,48090,48091");
        params.put("isShowLine", "1");
        params.put("largeSize", "1");
        params.put("instanceId", "12345");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.freeForCartoonFlowService/getFreeCartoonFlowShowInfo")
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
     * 排序处理 sortType=2
     * 
     */
    @Test
    public void testViewForSort2()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("showType", "1");
        params.put("pluginCode", "cartoon_list");
        params.put("nameShowType", "1");
        params.put("cornerShowType", "1");
        params.put("sortType", "2");
        params.put("isCycle", "0");
        params.put("cycleNum", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("beginDate", "");
        params.put("endDate", "");
        params.put("showNum", "1");
        params.put("pageNo", "1");
        params.put("book_id_list", "362615352,385539499,352585218,660145759");
        params.put("link_id_list", "48088,48089,48090,48091");
        params.put("isShowLine", "1");
        params.put("largeSize", "1");
        params.put("instanceId", "12345");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.freeForCartoonFlowService/getFreeCartoonFlowShowInfo")
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
     * 排序处理 sortType=3
     * 
     */
    @Test
    public void testViewForSort3()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("showType", "1");
        params.put("pluginCode", "cartoon_list");
        params.put("nameShowType", "1");
        params.put("cornerShowType", "1");
        params.put("sortType", "3");
        params.put("isCycle", "0");
        params.put("cycleNum", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("beginDate", "");
        params.put("endDate", "");
        params.put("showNum", "1");
        params.put("pageNo", "1");
        params.put("book_id_list", "362615352,385539499,352585218,660145759");
        params.put("link_id_list", "48088,48089,48090,48091");
        params.put("isShowLine", "1");
        params.put("largeSize", "1");
        params.put("instanceId", "12345");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.freeForCartoonFlowService/getFreeCartoonFlowShowInfo")
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
     * 排序处理 sortType=6
     * 
     */
    @Test
    public void testViewForSort6()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("showType", "1");
        params.put("pluginCode", "cartoon_list");
        params.put("nameShowType", "1");
        params.put("cornerShowType", "1");
        params.put("sortType", "6");
        params.put("isCycle", "0");
        params.put("cycleNum", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "1");
        params.put("beginDate", "");
        params.put("endDate", "");
        params.put("showNum", "1");
        params.put("pageNo", "1");
        params.put("book_id_list", "362615352,385539499,352585218,660145759");
        params.put("link_id_list", "48088,48089,48090,48091");
        params.put("isShowLine", "1");
        params.put("largeSize", "1");
        params.put("instanceId", "12345");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.freeForCartoonFlowService/getFreeCartoonFlowShowInfo")
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
