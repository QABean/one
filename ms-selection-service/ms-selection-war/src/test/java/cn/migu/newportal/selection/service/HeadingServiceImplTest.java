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
public class HeadingServiceImplTest
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
        params.put("pluginCode", "general_heading");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("suffix", "1");
        params.put("huiyuan", "1");
        params.put("label", "1");
        params.put("countType", "1");
        params.put("startTime", "2017-6-14 18:13:16");
        params.put("endTime", "2017-6-14 20:13:16");
        params.put("nid", "412269321");
        params.put("isNodeUrl", "1");
        params.put("alias", "男生专区");
        params.put("urlDesc", "链接后缀描述");
        params.put("url", "testUrl.html");
        params.put("title", "咪咕限免");
        params.put("content", "咪咕上新，千万别错过啦");
        params.put("textmin", "每晚20点~凌晨4点");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.headingService/getGeneralHeading")
                .header("X-Identity-ID", "13838380438")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
