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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class ApiLevelOneAdMethodTest
{
    private MockMvc mockMvc;
    
    @Autowired
    WebApplicationContext wac;
    
    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "api_levelone_ad");
        params.put("appid", "001");
        params.put("authid", "12456");
        params.put("id", "11111");
        params.put("actiontype", "0");
        params.put("token", "migushipin-token");
        params.put("isbaoNoShow", "0");
        params.put("isClickBaoguang", "0");
        params.put("bgbfb", "0");
        params.put("pnumberha", "0");
      
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
        .perform(post("/ms.selection.selectionService/getApiLevelOneAd")//.header("x-real-ip", "127.0.0.1")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
    
    @Test
    public void testViewTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "api_levelone_ad");
        params.put("appid", "001");
        params.put("authid", "12456");
        params.put("id", "11111");
        params.put("actiontype", "0");
        params.put("token", "migushipin-token");
        params.put("isbaoNoShow", "1");
        params.put("isClickBaoguang", "0");
        params.put("bgbfb", "0");
        params.put("pnumberha", "1");
      
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
        .perform(post("/ms.selection.selectionService/getApiLevelOneAd")//.header("x-real-ip", "127.0.0.1")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
    
}
