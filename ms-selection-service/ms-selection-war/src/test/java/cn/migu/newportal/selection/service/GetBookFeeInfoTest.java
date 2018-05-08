package cn.migu.newportal.selection.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-mvc.xml", "classpath:spring-core.xml"})
public class GetBookFeeInfoTest
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
    public void testFee() // 资费
        throws Exception
    {
        mockMvc
            .perform(
                post("/ms.selection.bookContentService/getBookFeeInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                    .header("X-Real-IP", "127.0.0.1")
                    .content(
                        "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"453929257\",\"isShowLine\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testFee2() // 另一本书的资费
        throws Exception
    {
        mockMvc
            .perform(
                post("/ms.selection.bookContentService/getBookFeeInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                    .header("X-Real-IP", "127.0.0.1")
                    .header("X-Identity-ID", "13824384869")
                    .content(
                        "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"409345450\",\"isShowLine\": \"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testFee3() // 另一种ua的资费
        throws Exception
    {
        mockMvc
            .perform(
                post("/ms.selection.bookContentService/getBookFeeInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .header("User-Agent", "CMREADBC_Android_1440*2392_V4.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                    .header("X-Real-IP", "127.0.0.1")
                    .content(
                        "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"409345450\",\"isShowLine\": \"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testFeeError1() // 图书ID不存在
        throws Exception
    {
        mockMvc
            .perform(
                post("/ms.selection.bookContentService/getBookFeeInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"isShowLine\": \"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testFeeError2() // 图书内容不存在或已下架
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookFeeInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"1453929257\",\"isShowLine\": \"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
}
