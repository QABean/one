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
public class ActivityBannerServiceTest
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
     * 活动小标题
     *gtype 数据来源为1的情况：无连接
     * @author wangwenjuan
     * @throws Exception
     */
    @Test
    public void testActivityBanner()throws
        Exception
        {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode","recommend_list");
        params.put("gType","2");
        params.put("picture_url","content/repository/ues/image/s109/20170411160021435.jpg");
        params.put("link_id_list","48111");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.ActivityBannerService/getActivityBannerInfo")
                .header("Content-Type", "application/json")
                .header("User-Agent", "andriod")
                .header("X-Request-ID", "111111111")
                .header("X-Client-ID", "2222222")
                .header("X-Real-IP", "10.211.111.10")
                .header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    
        }
    
}
