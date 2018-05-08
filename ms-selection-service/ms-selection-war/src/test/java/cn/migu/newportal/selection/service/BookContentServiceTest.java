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
public class BookContentServiceTest
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
    public void testBook() // 图书
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookContentInfo")
                .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                .header("X-Real-IP", "127.0.0.1")
                .header("X-Identity-ID", "15168438375")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"453929257\",\"isShowLine\":\"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testBook2() // 听书(65149285)
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookContentInfo")
                .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                .header("X-Real-IP", "127.0.0.1")
                .header("X-Identity-ID", "15168438375")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"660300217\",\"isShowLine\":\"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testError1() // 图书ID不存在
        throws Exception
    {
        mockMvc
            .perform(
                post("/ms.selection.bookContentService/getBookContentInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"isShowLine\": \"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testError2() // 图书内容不存在或已下架
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookContentInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"1453929257\",\"isShowLine\": \"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testBookWithPresent() // 图书含赠书(bid=455296956)
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookContentInfo")
                .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                .header("X-Real-IP", "127.0.0.1")
                .header("X-Identity-ID", "15168438375")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"455296956\",\"isShowLine\":\"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    public void testUGCBook() // 用户自上传图书(bid=455352657)
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookContentService/getBookContentInfo")
                .header("User-Agent", "CMREADBC_Android_1440*2392_V6.80(1440*2392;Huawei;Nexus 6P;Android 7.0;cn;)")
                .header("X-Real-IP", "127.0.0.1")
                .header("X-Identity-ID", "15168438375")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    "{\"paramMap\":{\"pluginCode\": \"bookcontent\",\"bid\": \"455352657\",\"isShowLine\":\"1\",\"isMarginTop\": \"1\",\"isMarginBottom\": \"1\",\"isAjax\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

}
