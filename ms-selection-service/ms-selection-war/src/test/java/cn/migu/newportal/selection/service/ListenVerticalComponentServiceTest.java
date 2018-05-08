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
public class ListenVerticalComponentServiceTest
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
     * 正常返回
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testListenVerticalViewOne()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical_listen");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("pageNo", "1");
        params.put("showNum", "5");
        params.put("nid", "385333593");
        params.put("isShowLine", "0");
        params.put("isPlayNew", "1");
        params.put("cornerShowType", "1");
        params.put("instanceId", "222");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.listenVerticalComponentService /getListenVerticalComponentShowInfo").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 配置专区没有对应专区信息情况
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testListenVerticalViewTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical_listen");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("pageNo", "1");
        params.put("showNum", "5");
        params.put("nid", "12554");
        params.put("isShowLine", "0");
        params.put("isPlayNew", "1");
        params.put("cornerShowType", "1");
        params.put("instanceId", "222");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.listenVerticalComponentService /getListenVerticalComponentShowInfo").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 配置专区信息下没有图书情况
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testListenVerticalViewThree()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical_listen");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("pageNo", "1");
        params.put("showNum", "5");
        params.put("nid", "385333593");
        params.put("isShowLine", "0");
        params.put("isPlayNew", "1");
        params.put("cornerShowType", "1");
        params.put("instanceId", "222");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.listenVerticalComponentService /getListenVerticalComponentShowInfo").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 没有配置专区情况
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testListenVerticalErrorOne()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical_listen");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("pageNo", "1");
        params.put("showNum", "5");
        params.put("nid", " ");
        params.put("isShowLine", "0");
        params.put("isPlayNew", "1");
        params.put("cornerShowType", "1");
        params.put("instanceId", "222");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.listenVerticalComponentService /getListenVerticalComponentShowInfo").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 
     *没有组件实例Id情况
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testListenVerticalErrorTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical_listen");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("pageNo", "1");
        params.put("showNum", "5");
        params.put("nid", "385333593");
        params.put("isShowLine", "0");
        params.put("isPlayNew", "1");
        params.put("cornerShowType", "1");
        params.put("instanceId", " ");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.listenVerticalComponentService /getListenVerticalComponentShowInfo").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
