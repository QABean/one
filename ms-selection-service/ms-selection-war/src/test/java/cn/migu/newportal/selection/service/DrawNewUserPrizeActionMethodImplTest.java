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
 * 新用户领奖action————————测试类
 *
 * @author Li Bin
 * @version C10 2017年12月14日
 * @since SDP V300R003C10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-core.xml", "classpath:spring-mvc.xml"})
public class DrawNewUserPrizeActionMethodImplTest
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
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(
                post("/ms.selection.selectionService/drawNewUserPrize").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .header("x-real-ip", "127.0.0.1")
                    .header("User-Agent", "123")
                    .header("X-Identity-ID", "31201128015")
                    .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 校验游客
     * 
     * @author Li Bin
     * @throws Exception
     */
    @Test
    public void testUser()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(
                post("/ms.selection.selectionService/drawNewUserPrize").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .header("x-real-ip", "127.0.0.1")
                    .header("User-Agent", "123")
                    .header("X-Identity-ID", "")
                    .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
