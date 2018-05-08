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

/**
 * 智能广告
 *
 * @author wangqiang
 * @return
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class IntelliAdMethodImplTest
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
     * 智能广告单元测试
     *
     * @author wangqiang
     * @throws Exception
     */
    @Test
    public void testView() throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "intelligent_advertisement");
        params.put("recNum", "1");
        params.put("wzid", "1");
        params.put("picSize", "1");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/intelliAd").header("X-Identity-ID", "31201083244")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testView_1() throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "intelligent_advertisement");
        params.put("recNum", "REC33");
        params.put("wzid", "1");
        params.put("picSize", "");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("isPaddingTop", "1");
        params.put("isShowLine", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/intelliAd").header("X-Identity-ID", "31201083244")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
