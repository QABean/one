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
public class BookCatalogServiceTest
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
    // 电子书 完本
    public void testView()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"pluginCode\": \"book_catalog\",\"bid\":\"453929257\",\"isShowLine\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 电子书 连载
    public void testView_1()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"pluginCode\": \"book_catalog\",\"bid\":\"404942517\",\"isShowLine\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 电子书 测试桩测试
    public void testView_2()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content("{\"paramMap\":{\"isAjax\":\"1\",\"pluginCode\":\"book_catalog\",\"isShowLine\":\"1\",\"bid\":\"404942517\",\"isGetDataFromPilling\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 听书 完本倒序
    public void testView1()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getListenBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"isAjax\":\"1\",\"pluginCode\":\"listen_book_catalog\",\"isShowLine\":\"1\",\"bid\":\"453929257\",\"sortType\":\"2\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 听书 完本 正序
    public void testView2()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getListenBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"isAjax\":\"1\",\"pluginCode\":\"listen_book_catalog\",\"isShowLine\":\"1\",\"bid\":\"453929257\",\"sortType\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 听书 连载
    public void testView3()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getListenBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(
                    "{\"paramMap\":{\"isAjax\":\"1\",\"pluginCode\":\"listen_book_catalog\",\"isShowLine\":\"1\",\"bid\":\"404942517\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    @Test
    // 听书 测试桩测试
    public void testView4()
        throws Exception
    {
        mockMvc
            .perform(post("/ms.selection.bookCatalogService/getListenBookCatalogInfo")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content("{\"paramMap\":{\"isAjax\":\"1\",\"pluginCode\":\"listen_book_catalog\",\"isShowLine\":\"1\",\"bid\":\"404942517\",\"isGetDataFromPilling\":\"1\"}}"))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
   
}
