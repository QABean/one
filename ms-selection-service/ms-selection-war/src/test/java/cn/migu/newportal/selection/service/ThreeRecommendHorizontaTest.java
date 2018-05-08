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
public class ThreeRecommendHorizontaTest
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
     * 正常
     * 
     */
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,48089,48090");
        
        params.put("bookId1", "362615352");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 链接不存在
     * 
     */
    @Test
    public void testNoLinks()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "");
        
        params.put("bookId1", "362615352");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 某个链接不存在
     * 
     */
    @Test
    public void testSomeLinkNotExist()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,,48090");
        
        params.put("bookId1", "362615352");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 某个链接id错误
     * 
     */
    @Test
    public void testSomeLinkError()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,48089,4809011111");
        
        params.put("bookId1", "362615352");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 某个图书id错误
     * 
     */
    @Test
    public void testSomeBookIdError()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,48089,48090");
        
        params.put("bookId1", "362615352");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534111");//error bookid
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 某个图书id为空
     * 
     */
    @Test
    public void testSomeBookIdEmpty()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,48089,48090");
        
        params.put("bookId1", "");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "376082534");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "352585218");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
     * 图书Id为空
     * 
     */
    @Test
    public void testNoBookIds()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "recommend_horizontal_bklist");
        params.put("link_id_list", "48088,48089,48090");
        
        params.put("bookId1", "");
        params.put("bookClass1", "人文社科");
        params.put("nameShowType1", "1");
        
        params.put("bookId2", "");
        params.put("bookClass2", "玄幻");
        params.put("nameShowType2", "1");
        
        params.put("bookId3", "");
        params.put("bookClass3", "科普");
        params.put("nameShowType3", "1");
        
        params.put("instanceId", "recommend_horizontal_bklist");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.threeRecommendHorizontalService/getThreeRecommendForHorizontal")
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
