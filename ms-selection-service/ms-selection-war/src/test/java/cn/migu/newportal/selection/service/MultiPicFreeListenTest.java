package cn.migu.newportal.selection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author wangqiang
 * @version C10 2018年03月19日
 * @since SDP V300R003C10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
public class MultiPicFreeListenTest
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
     * @author wangqiang
     * @throws Exception
     */
    @Test
    public void testVoiceBook_one()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "1");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
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
     * @author wangqiang
     * @throws Exception
     */
    @Test
    public void testVoiceBook_two()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "1");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }


    @Test
    public void testVoiceBook_2()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_3()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "3");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_4()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "2");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_5()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "3");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_6()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "2");
        params.put("showNum", "4");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("nid", "590010005");
        params.put("nameShowType", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    @Test
    public void testVoiceBook_7()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "3");
        params.put("cornerShowType", "1");
        params.put("showType", "2");
        params.put("largeSize", "2");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("nid", "590010005");
        params.put("nameShowType", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_9()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "1");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("bookId4", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("coverlogo4", "coverlogo4.html");
        params.put("nid", "590010005");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_12()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "1");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("bookId4", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("coverlogo4", "coverlogo4.html");
        params.put("nid", "590010007");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
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
     * @author wangqiang
     * @throws Exception
     */
    @Test
    public void testVoiceBook_two1()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "1");
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
        params.put("nid", "41047813");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
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
     * @author wangqiang
     * @throws Exception
     */
    @Test
    public void testVoiceBook_10()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");

        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "2");
        params.put("showNum", "1");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("nid", "590010005");
        params.put("nameShowType", "1");
        params.put("X-Identity-ID", "31200732278");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_14()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "3");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("bookId4", "660411000");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("X-Identity-ID", "31200732278");
        params.put("coverlogo4", "coverlogo4.html");
        params.put("nid", "590010005");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_15()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("bookId4", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("coverlogo4", "coverlogo4.html");
        params.put("nid", "590010005");
        paramMap.put("paramMap", params);
        params.put("X-Identity-ID", "31200732278");
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_16()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "3");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "442576726");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "coverlogo4.html");
        params.put("nid", "590010005");
        params.put("nameShowType", "1");
        params.put("X-Identity-ID", "31200732278");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_17()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "2");
        params.put("cornerShowType", "2");
        params.put("showType", "1");
        params.put("largeSize", "3");
        params.put("showNum", "3");
        params.put("node_alias", "1");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "coverlogo4.html");
        params.put("nid", "590010005");
        params.put("X-Identity-ID", "31200732278");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_18()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "1");
        params.put("startFrom", "2");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "3");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "442576726");
        params.put("bookId3", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "coverlogo4.html");
        params.put("nid", "590010007");
        params.put("nameShowType", "1");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    public void testVoiceBook_11()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "multiPic_FreeListen");
        params.put("pageNo", "1");
        params.put("isShowLine", "1");
        params.put("dataSrc", "2");
        params.put("startFrom", "3");
        params.put("cornerShowType", "1");
        params.put("showType", "1");
        params.put("largeSize", "4");
        params.put("showNum", "3");
        params.put("node_alias", "专区名称");
        params.put("bookId1", "395765385");
        params.put("bookId2", "404006540");
        params.put("bookId3", "442576726");
        params.put("bookId4", "660411000");
        params.put("coverlogo1", "1");
        params.put("coverlogo2", "1");
        params.put("coverlogo3", "1");
        params.put("coverlogo4", "coverlogo4.html");
        params.put("nid", "590010005");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/multiPicFreeListen").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
}
