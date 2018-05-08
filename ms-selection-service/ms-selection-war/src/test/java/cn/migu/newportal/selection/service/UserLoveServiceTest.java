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
@ContextConfiguration({"classpath:spring-mvc.xml", "classpath:spring-core.xml"})
public class UserLoveServiceTest
{
    private MockMvc mockMvc;
    
    @Autowired
    WebApplicationContext wac;
    
    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    //调用BI的接口
    @Test
    public void testView()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "3");
        params.put("pluginCode", "booklist_like");
        params.put("bid", "453566089");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.contentRecommendService/getUserLoveInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    //调用缓存服务
    @Test
    public void testView_cahe()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "1");
        params.put("pluginCode", "booklist_like");
        params.put("bid", "453566089");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        params.put("nodeId", "");
        params.put("rankType", "3");
        params.put("rankRule", "4");
        params.put("rankDateType", "4");
        params.put("recommendType", "3");
        params.put("recType", "4");
        params.put("title", "猜你喜欢");
        params.put("nodeId", "41047813");
        params.put("msisdn", "186681355555");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.contentRecommendService/getUserLoveInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    public void testView_cahe1()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "2");
        params.put("pluginCode", "booklist_like");
        params.put("bid", "453566089");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        params.put("nodeId", "");
        params.put("rankType", "3");
        params.put("rankRule", "4");
        params.put("rankDateType", "4");
        params.put("recommendType", "3");
        params.put("recType", "4");
        params.put("title", "猜你喜欢");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.contentRecommendService/getUserLoveInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    @Test
    public void testError()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "3");
        params.put("pluginCode", "booklist_like");
        params.put("bid", "");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.userLoveService/getUserLoveInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
