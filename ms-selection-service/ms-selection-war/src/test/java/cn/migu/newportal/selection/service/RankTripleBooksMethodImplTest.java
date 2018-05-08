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
 * 排行三封面
 *
 * @author yuhaihan
 * @version C10 2017年12月18日
 * @since SDP V300R003C10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class RankTripleBooksMethodImplTest
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
    public void successView() throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "rank_triplebooks");
        params.put("isMarginTop", "");
        params.put("isMarginBottom", "");
        params.put("isPaddingTop", "");
        params.put("isShowLine", "");
        params.put("isAjax", "");
        params.put("rankDateType", "4");
        params.put("nodeId", "");
        params.put("rankShowType", "1");
        params.put("showType", "1");
        params.put("clickValue", "$num点击");
        params.put("clickNumType", "0");
        params.put("multiple", "1");
        params.put("rankType", "2");
        params.put("rankRule", "");
        params.put("bookItemType", "1");
        
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getRankTriplebooks").header("X-Identity-ID", "31201083244")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    
    /**
     * 排行三封面单元测试
     *
     * @author yuhaihan
     * @throws Exception
     */
    
    @Test
    public void errorView() throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "rank_triplebooks");
        params.put("isMarginTop", "");
        params.put("isMarginBottom", "");
        params.put("isPaddingTop", "");
        params.put("isShowLine", "");
        params.put("isAjax", "");
        params.put("rankDateType", "4");
        params.put("nodeId", "");
        params.put("rankShowType", "1");
        params.put("showType", "1");
        params.put("clickValue", "$num点击");
        params.put("clickNumType", "0");
        params.put("multiple", "1");
        params.put("rankType", "2");
        params.put("rankRule", "");       
        params.put("bookItemType", "1");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getRankTriplebooks").header("X-Identity-ID", "31201083244")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
