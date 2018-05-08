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

import cn.migu.newportal.util.constants.ParamConstants;

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class GetPersonalRecommendMethodImplTest
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
     * 私人推荐
     */
    @Test
    public void testView()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "personal_recommend");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("title", "专属推荐");
        params.put("titleDesc", "富文本");
        params.put("cornerShowType", "0,1,2,3,4,5,6,7,8");
        params.put("showType", "1");
        params.put("pageSize", "10");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPersonalRecommend")
                    .header(ParamConstants.X_Identity_ID, "31200868710")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    /**
     * 私人推荐
     */
    @Test
    public void testView_msisdnnull()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "personal_recommend");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("isMarginTop", "1");
        params.put("isMarginBottom", "1");
        params.put("title", "专属推荐");
        params.put("titleDesc", "富文本");
        params.put("cornerShowType", "0,1,2,3,4,5,6,7,8");
        params.put("showType", "1");
        params.put("pageSize", "10");
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getPersonalRecommend")
                    .header(ParamConstants.X_Identity_ID, "")
                    .header(ParamConstants.User_Agent, "1234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
