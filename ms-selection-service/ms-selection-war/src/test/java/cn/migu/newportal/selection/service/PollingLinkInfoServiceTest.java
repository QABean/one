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

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PollingLinkInfoServiceTest {
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
    /**
     * 
     * 
     *
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testPolling_one()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "polling_link");
        params.put("backGroundType", "2");
        params.put("isShowLine", "1");
        params.put("instanceId", "100");
        params.put("linkids", "48088,48089,48090,48091,48111");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPollingLinkInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
	
    
    /**
     * 
     * 
     *
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testPolling_two()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "polling_link");
        params.put("backGroundType", "1");
        params.put("isShowLine", "1");
        params.put("instanceId", "100");
        params.put("linkids", "48088,48089,48090,48091,48111");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPollingLinkInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
	
    /**
     * 
     * 
     *
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testPolling_three()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "polling_link");
        params.put("backGroundType", "1");
        params.put("isShowLine", "1");
        params.put("instanceId", "");
        params.put("linkids", "48088,48089,48090,48091,48111");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPollingLinkInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testPolling_four()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "polling_link");
        params.put("backGroundType", "1");
        params.put("isShowLine", "1");
        params.put("instanceId", "");
        params.put("linkids", "481233123213");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPollingLinkInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testPolling_five()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "polling_link");
        params.put("backGroundType", "1");
        params.put("isShowLine", "1");
        params.put("instanceId", "");
        params.put("linkids", "");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPollingLinkInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
	
}
