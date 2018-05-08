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

/**
 * 
 * 偏好排序展示test
 *
 * @author liuchuanzhuang
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-core.xml", "classpath:spring-mvc.xml" })
public class PreferenceLinksMethodImplMethodTest
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
    public void PreferenceLinksMethodImplMethodTest1()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "preference_ranklist");
        params.put("rule", "1");
        params.put("nanUrl", "nanUrl.html");
        params.put("nvUrl", "nvUrl.html");
        params.put("cbUrl", "cbUrl.html");
        params.put("cyUrl", "cyUrl.html");
        params.put("isAjax", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/preferenceLinks").header("x-real-ip", "127.0.0.1")
                .header("terminalUniqueId", "32432")
                .header("X-Identity-ID", "42000038003")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void PreferenceLinksMethodImplMethodTest2()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "preference_ranklist");
        params.put("rule", "1");
        params.put("nanUrl", "nanUrl.html");
        params.put("nvUrl", "nvUrl.html");
        params.put("cbUrl", "cbUrl.html");
        params.put("cyUrl", "cyUrl.html");
        params.put("isAjax", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/preferenceLinks").header("x-real-ip", "127.0.0.1")
                .header("terminalUniqueId", "32432")
                .header("X-Identity-ID", "")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void PreferenceLinksMethodImplMethodTest3()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "preference_ranklist");
        params.put("rule", "1");
        params.put("nanUrl", "nanUrl.html");
        params.put("nvUrl", "nvUrl.html");
        params.put("cbUrl", "cbUrl.html");
        params.put("cyUrl", "cyUrl.html");
        params.put("isAjax", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/preferenceLinks").header("x-real-ip", "127.0.0.1")
                .header("terminalUniqueId", "")
                .header("X-Identity-ID", "")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
   
}
