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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-mvc.xml", "classpath:spring-core.xml"})
public class PortalBookLinkServiceImplTest
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
        params.put("pluginCode", "bookcontent_link");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "453929257");
        params.put("msisdn", "31200787212");
        params.put("isAjax", "1");
        params.put("lotteryid", "12049");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.portalBookLinkService/getPortalBookLink")
                .header("X-Identity-ID", "31200787212")
                .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
                .header("X-Auth-Token", "fUigqawxif8WKRlx")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testIdentityIsEmpty()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent_link");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "453929257");
        params.put("msisdn", "31200787212");
        params.put("isAjax", "1");
        params.put("lotteryid", "14563");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.portalBookLinkService/getPortalBookLink")
                .header("X-Identity-ID", "")
                .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
                .header("X-Auth-Token", "fUigqawxif8WKRlx")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testBookIdNotIsExist()
        throws Exception
    {
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent_link");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "123444");
        params.put("msisdn", "31200787212");
        params.put("isAjax", "1");
        params.put("lotteryid", "14563");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.portalBookLinkService/getPortalBookLink")
                .header("X-Identity-ID", "")
                .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
                .header("X-Auth-Token", "fUigqawxif8WKRlx")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testError()
        throws Exception
    {
        
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent_link");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "453929257");
        params.put("msisdn", "31200872076");
        params.put("isAjax", "1");
        params.put("lotteryid", "14563");
        
        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
            .perform(post("/ms.selection.portalBookLinkService/getPortalBookLink")
                .header("X-Identity-ID", "31200787212")
                .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
                .header("X-Auth-Token", "yNQ134KTw8FKnucB")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    //抽奖
    @Test
    public void testExchangeLottery()  throws Exception{
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "455303056");
        params.put("msisdn", "31200872076");
        params.put("isAjax", "1");
        params.put("slk", "2");
        params.put("lotteryid", "14563");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
        .perform(post("/ms.selection.raffleService/exchangeLottery")
            .header("X-Identity-ID", "31200872076")
            .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
    
    @Test
    public void testLotteryIdIsNotExist()  throws Exception{
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "455303056");
        params.put("msisdn", "31200872076");
        params.put("isAjax", "1");
        params.put("slk", "2");
        params.put("lotteryid", "123456");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
        .perform(post("/ms.selection.raffleService/exchangeLottery")
            .header("X-Identity-ID", "31200872076")
            .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
    
    @Test
    public void testWinning()  throws Exception{
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "455303056");
        params.put("msisdn", "31200787231");
        params.put("isAjax", "1");
        params.put("slk", "2");
        params.put("lotteryid", "14563");

        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
        .perform(post("/ms.selection.raffleService/exchangeLottery")
            .header("X-Identity-ID", "31200787231")
            .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
    
    @Test
    public void testException()  throws Exception{
        Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pluginCode", "bookcontent");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isPaddingTop", "0");
        params.put("isShowLine", "0");
        
        params.put("bid", "455303056");
        params.put("msisdn", "31200787219");
        params.put("isAjax", "1");
        params.put("slk", "1");


        paramMap.put("paramMap", params);
        ObjectMapper mapper = new ObjectMapper();
        String contentParam = mapper.writeValueAsString(paramMap);
        
        mockMvc
        .perform(post("/ms.selection.raffleService/exchangeLottery")
            .header("X-Identity-ID", "31200787219")
            .header("User-Agent", "CMREADBC_Android_1152*1920_V6.84(1152*1920;Meizu;MX4;Android 5.1;cn;)")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(contentParam))
        .andDo(print())
        .andExpect(status().is(200))
        .andReturn()
        .getResponse()
        .getContentAsString();
    }
}
