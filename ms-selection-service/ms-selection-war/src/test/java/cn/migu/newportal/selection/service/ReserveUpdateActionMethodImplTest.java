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

import cn.migu.newportal.util.constants.ParamConstants;

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ReserveUpdateActionMethodImplTest 
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
     * 预订更新action正确测试
     * 
     */
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "452196429");
        params.put("reserveType", "0");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "31200868710")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 取消预订更新action正确测试
     * 
     */
    @Test
    public void test2View()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "452196429");
        params.put("reserveType", "1");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "31200868710")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 图书id不存在
     * 
     */
    @Test
    public void errorView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "40");
        params.put("reserveType", "1");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "31200868710")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 图书id为null 参数错误
     * 
     */
    @Test
    public void error2View()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "");
        params.put("reserveType", "1");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "90")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    
    /**
     * 选择类型不匹配错误
     * 
     */
    @Test
    public void error3View()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "452196429");
        params.put("reserveType", "3");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "90")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 用户为游客
     * 
     */
    @Test
    public void error4View()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("bid", "40");
        params.put("reserveType", "1");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.recommendContentService/reserveUpdateAction")
            		.header(ParamConstants.X_Identity_ID, "90")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
