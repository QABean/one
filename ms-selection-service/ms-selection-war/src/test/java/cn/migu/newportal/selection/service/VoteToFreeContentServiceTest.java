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
public class VoteToFreeContentServiceTest
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
     * 成功返回投票信息：配置图书大于等于活动数
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "111");
        params.put("largeSize", "2");
        params.put("book_id_list", "800103130,601788603,602442229,800292254");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 成功返回投票信息:配置的图书小与活动数
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeViewTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "111");
        params.put("largeSize", "2");
        params.put("book_id_list", "800103130,601788603,602442229");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    
    /**
     * 没有配置图书情况
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeErrorOne()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "111");
        params.put("largeSize", "2");
        params.put("book_id_list", " ");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 活动Id为空
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeErrorTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "");
        params.put("largeSize", "2");
        params.put("book_id_list", "800103130,601788603,602442229,800292254");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 组件实例Id为空
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeErrorThree()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "111");
        params.put("largeSize", "2");
        params.put("book_id_list", "800103130,601788603,602442229,800292254");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "15943285238")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 
     *请求头用户为空
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testVoteToFreeErrorFour()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "ballot_list");
        params.put("tpbuttonDesc", "今日限免");
        params.put("ballotId", "111");
        params.put("largeSize", "2");
        params.put("book_id_list", "800103130,601788603,602442229,800292254");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("instanceId", "111");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getVoteToFree").header("X-Identity-ID", "")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
