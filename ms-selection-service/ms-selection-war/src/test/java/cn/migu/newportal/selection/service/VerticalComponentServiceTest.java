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
public class VerticalComponentServiceTest
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
     * 测试有纵向图书列表情况
     *
     * @author wangwenjuan
     * @throws Exception
     */
    @Test
    public void testVerticalComponentViewOne()
        throws Exception
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "booklist_vertical");
        params.put("isMarginTop", "0");
        params.put("isLaLoding", "1");
        params.put("endLogo", "2");
        params.put("showPage", "3");
        params.put("isShowLine", "1");
        params.put("cornerShowType", "2");
        params.put("pageNo", "2");
        params.put("largeSize", "2");
        params.put("nid", "6891829");
        params.put("nameShowType", "1");
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(params);
        mockMvc.perform(post("/ms.selection.verticalComponentService/getVerticalComponentInfo")
                .header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
}
