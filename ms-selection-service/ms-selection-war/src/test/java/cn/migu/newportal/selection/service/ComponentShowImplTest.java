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
public class ComponentShowImplTest
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
     * 获取专区下的图书信息
     * 
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testVoiceBook_one()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "voiceBook");
        params.put("isMarginTop", "2");
        params.put("isCycle", "1");
        params.put("cyclePeriod", "1");
        params.put("cycleType", "");
        params.put("cycleNum", "6");
        params.put("beginDate", "2017-07-03 14:54:31");
        params.put("endDate", "2017-07-04 14:54:31");
        params.put("endLogo", "1");
        params.put("largeSize", "1");
        params.put("node_id_list", "41047813,700000004346,6891829,590001917");
        params.put("nameShowType", "1");
        params.put("sortType", "2");
        params.put("instanceId", "100");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("ms.selection.ImageTextMix/getComponentShow").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
