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
public class HorizontalContentServiceTest
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
     * 测试有轮询的横向列表情况
     *
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testHorizontalContentViewOne()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("pluginCode", "booklist_horizontal");
        params.put("node_id_list", "385333593");
        params.put("isShowLine", "1");
        params.put("showType", "1");
        params.put("isShowYF", "0");
        params.put("showNum", "3");
        params.put("cornerShowType", "1");
        params.put("isPaddingTop", "1");
        params.put("largeSize", "2");
        params.put("isCycle", "1");
        params.put("showNodeNum", "");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "2");
        params.put("cycleType", "1");
        params.put("beginDate", "2017/07/05 20:29:29");
        params.put("endDate", "2017/07/07 20:29:29");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.horizontalContentService/getHorizontalContentInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 
     *测试不轮循的横向列表情况
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testHorizontalContentViewTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("pluginCode", "booklist_horizontal");
        params.put("node_id_list", "385333593");
        params.put("isShowLine", "1");
        params.put("showType", "1");
        params.put("isShowYF", "0");
        params.put("showNum", "3");
        params.put("cornerShowType", "1");
        params.put("isPaddingTop", "1");
        params.put("largeSize", "2");
        params.put("isCycle", "0");
        params.put("showNodeNum", "");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "2");
        params.put("cycleType", "1");
        params.put("beginDate", "2017/07/05 20:29:29");
        params.put("endDate", "2017/07/07 20:29:29");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.horizontalContentService/getHorizontalContentInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 
     *专区Id为空的情况
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testHorizontalContentErrorOne()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("pluginCode", "booklist_horizontal");
        params.put("node_id_list", "");
        params.put("isShowLine", "1");
        params.put("showType", "1");
        params.put("isShowYF", "0");
        params.put("showNum", "3");
        params.put("cornerShowType", "1");
        params.put("isPaddingTop", "1");
        params.put("largeSize", "2");
        params.put("isCycle", "0");
        params.put("showNodeNum", "2");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "2");
        params.put("cycleType", "1");
        params.put("beginDate", "2017/07/05 20:29:29");
        params.put("endDate", "2017/07/07 20:29:29");
        params.put("instanceId", "222");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.horizontalContentService/getHorizontalContentInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * 
     *组件id为空的情况
     * @author yejiaxu
     * @throws Exception
     */
    @Test
    public void testHorizontalContentErrorTwo()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("pluginCode", "booklist_horizontal");
        params.put("node_id_list", "385333593");
        params.put("isShowLine", "1");
        params.put("showType", "1");
        params.put("isShowYF", "0");
        params.put("showNum", "3");
        params.put("cornerShowType", "1");
        params.put("isPaddingTop", "1");
        params.put("largeSize", "2");
        params.put("isCycle", "1");
        params.put("showNodeNum", "");
        params.put("nameShowType", "1");
        params.put("sortType", "1");
        params.put("cycleNum", "2");
        params.put("cyclePeriod", "2");
        params.put("cycleType", "1");
        params.put("beginDate", "2017/07/05 20:29:29");
        params.put("endDate", "2017/07/07 20:29:29");
        params.put("instanceId", "");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.horizontalContentService/getHorizontalContentInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
