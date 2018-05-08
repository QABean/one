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
 * 偏好修改入口_V7preference_modify
 * 
 * @author hanyafei
 * @version C10 2018年1月2日
 * @since SDP V300R003C10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class ModifyPreferenceEntranceMethodImplTest
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
    public void test()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "preference_modifyentrance");
        params.put("picUrl", "xiaoming.jpg");
        params.put("isAjax", "1");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("isPaddingTop", "1");
        params.put("isShowLine", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/modifyPreferenceEntrance")
                .header("Content-Type", "application/json")
                .header("User-Agent", "andriod")
                .header("X-Identity-ID", "31201128015")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
        
    }
    
    /**
     * 参数校验
     * 
     * @author hanyafei
     * @throws Exception
     */
    @Test
    public void testParam()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "preference_modifyentrance");
        params.put("picUrl", "");
        params.put("isAjax", "1");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("isPaddingTop", "1");
        params.put("isShowLine", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/modifyPreferenceEntrance")
                .header("Content-Type", "application/json")
                .header("User-Agent", "andriod")
                .header("X-Identity-ID", "31201128015")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
        
    }
    
}
