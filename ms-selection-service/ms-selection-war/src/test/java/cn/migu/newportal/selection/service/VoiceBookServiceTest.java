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
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
public class VoiceBookServiceTest
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
     * 用户自配情况
     * 
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
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("isplayNew", "");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "1");
        params.put("node_alias", "1");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("nodeId", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.voiceBookService/VoiceBookService").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 专区
     * 
     *
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testVoiceBook_two()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "voiceBook");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("isplayNew", "");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "1");
        params.put("node_alias", "1");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("nodeId", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.voiceBookService/VoiceBookService").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
