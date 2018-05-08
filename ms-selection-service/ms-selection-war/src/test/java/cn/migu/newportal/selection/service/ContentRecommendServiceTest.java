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
@ContextConfiguration(locations={"classpath:spring-mvc.xml", "classpath:spring-core.xml"})
public class ContentRecommendServiceTest
{
    private MockMvc mockMvc;
    
    @Autowired
    WebApplicationContext wac;
    
    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    //同类推荐BI调用
    @Test
    public void testView() //图书阅读还阅读关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "booklist_same");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("type", "1");
        params.put("pageSize", "10");
        params.put("dataFrom", "5");
        params.put("title", "同类推荐");
        params.put("rankDateType", "1");
        params.put("rankStandard", "1");
        params.put("recommendLevel", "1");
        params.put("bid", "355727697");
        params.put("pageNo", "1");

        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    //同类推荐非BI调用
    @Test
    public void testViewNoBi() //图书阅读还阅读关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "booklist_same");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("type", "1");
        params.put("pageSize", "10");
        params.put("dataFrom", "1");
        params.put("title", "同类推荐");
        params.put("rankDateType", "1");
        params.put("rankStandard", "1");
        params.put("recommendLevel", "1");
        params.put("bid", "355727697");
        params.put("pageNo", "1");

        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    //dataFrom=4
    @Test
    public void testViewDataFrom_4() //图书阅读还阅读关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "booklist_same");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("type", "1");
        params.put("type", "1");
        params.put("pageSize", "10");
        params.put("dataFrom", "4");
        params.put("title", "同类推荐");
        params.put("rankDateType", "1");
        params.put("rankStandard", "1");
        params.put("recommendLevel", "1");
        params.put("bid", "355727697");
        params.put("pageNo", "1");

        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    //dataFrom为空
    @Test
    public void testViewDataFrom_Empty() //图书阅读还阅读关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "booklist_same");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("type", "1");
        params.put("pageSize", "10");
        params.put("dataFrom", "");
        params.put("title", "同类推荐");
        params.put("rankDateType", "1");
        params.put("rankStandard", "1");
        params.put("recommendLevel", "1");
        params.put("bid", "355727697");
        params.put("pageNo", "1");

        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testView1()//图书订购还订购关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "6");
        params.put("pluginCode", "booklist_same");
        params.put("bid", "408330146");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    contentParam ))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    @Test
    public void testView2()//图书浏览还浏览关联推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "7");
        
        params.put("pluginCode", "booklist_same");
        params.put("bid", "408330146");
        params.put("isShowLine", "1");
        
        params.put("isAjax", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo").header("X-Identity-ID", "31200868710")
                .header("User-Agent", "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    //同类推荐调用缓存
    @Test
    public void testView_Mem()//UES读过还读
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
       
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "1");
        params.put("pluginCode", "booklist_same");
        params.put("bid", "453929257");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        params.put("channelId", "1000");
        params.put("ranDateType", "3");
        params.put("recommendType", "1");
        params.put("rankType", "3");
        params.put("recommendGrade", "4");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").header("User-Agent", "123").content(contentParam
                    ))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
     @Test
    public void testView_Mem1()//同系列推荐
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
       
        Map<String,String> params=new HashMap<String,String>();
        params.put("dataFrom", "2");
        params.put("pluginCode", "booklist_same");
        params.put("bid", "453566089");
        params.put("isShowLine", "1");
        params.put("isAjax", "1");
        params.put("recommendType", "4");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo").header("x-real-ip", "127.0.0.1")
                .header("User-Agent", "123").contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
     
     @Test
     public void testView_Mem2()//UES买过还买过
         throws Exception
     {
         Map<String,Map> paramMap=new HashMap<String,Map>();
        
         Map<String,String> params=new HashMap<String,String>();
         params.put("dataFrom", "3");
         params.put("pluginCode", "booklist_same");
         params.put("bid", "453566089");
         params.put("isShowLine", "1");
         params.put("isAjax", "1");
         params.put("recommendType", "2");
         
         paramMap.put("paramMap", params);
         ObjectMapper mapper=new ObjectMapper();
         String contentParam=mapper.writeValueAsString(paramMap);
         mockMvc
             .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                 .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1").header("User-Agent", "123").content(
                     contentParam))
             .andDo(print())
             .andExpect(status().is(200))
             .andReturn()
             .getResponse()
             .getContentAsString();
     }
     @Test
     public void testView_Mem3()//表示UES同作者
         throws Exception
     {
         Map<String,Map> paramMap=new HashMap<String,Map>();
        
         Map<String,String> params=new HashMap<String,String>();
         params.put("dataFrom", "4");
         params.put("pluginCode", "booklist_same");
         params.put("isShowLine", "1");
         params.put("isAjax", "1");
         params.put("authorId", "1000162132");
         
         paramMap.put("paramMap", params);
         ObjectMapper mapper=new ObjectMapper();
         String contentParam=mapper.writeValueAsString(paramMap);
         mockMvc
             .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                 .contentType(MediaType.APPLICATION_JSON_UTF8).header("x-real-ip", "127.0.0.1")
                 .header("User-Agent", "123").content(
                     contentParam))
             .andDo(print())
             .andExpect(status().is(200))
             .andReturn()
             .getResponse()
             .getContentAsString();
     }
     @Test
     public void testView_Mem4()//听书详情页同类推荐
         throws Exception
     {
         Map<String,Map> paramMap=new HashMap<String,Map>();
        
         Map<String,String> params=new HashMap<String,String>();
         params.put("dataFrom", "8");
         params.put("pluginCode", "booklist_same");
         params.put("isShowLine", "1");
         params.put("isAjax", "1");
         params.put("bid", "453566089");
         
         paramMap.put("paramMap", params);
         ObjectMapper mapper=new ObjectMapper();
         String contentParam=mapper.writeValueAsString(paramMap);
         mockMvc
             .perform(post("/ms.selection.selectionService/getContentRecommendInfo").header("x-real-ip", "127.0.0.1")
                 .header("User-Agent", "123").contentType(MediaType.APPLICATION_JSON_UTF8).content(
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
        mockMvc
            .perform(post("/ms.selection.selectionService/getContentRecommendInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"pluginCode\": \"booklist_same\",\"bid\": \"\",\"isShowLine\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
