package cn.migu.newportal.selection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class AiRecommendListenMethodImplTest
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
        Map<String, Map> paramMap = new HashMap<String, Map>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", "123");
        params.put("pageSize", "10");


        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/aiRecommendListen").header("X-Identity-ID", "17646672223")
                .header("User-Agent", "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 请求头无阅读号
     * 
     * @author wulinfeng
     * @throws Exception
     */
    @Test
    public void testNoIdentity()
        throws Exception
    {
        Map<String, Map> paramMap = new HashMap<String, Map>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("std", "12345");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.booksheet.bookSheetService/colecteBookSheet").header("X-Identity-ID", "")
                .header("User-Agent", "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 书单Id为空
     * 
     * @author wulinfeng
     * @throws Exception
     */
    @Test
    public void testNoSheetIds()
        throws Exception
    {
        Map<String, Map> paramMap = new HashMap<String, Map>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("std", "");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.booksheet.bookSheetService/colecteBookSheet").header("X-Identity-ID", "17646672223")
                .header("User-Agent", "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
