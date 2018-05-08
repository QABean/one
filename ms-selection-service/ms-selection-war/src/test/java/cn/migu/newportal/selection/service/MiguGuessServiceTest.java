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
public class MiguGuessServiceTest {

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testView() throws Exception {
		mockMvc.perform(post("/ms.selection.selectionService/getMiguGuessInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(
						"{\"paramMap\":{\"pluginCode\": \"booklist_recommend\",\"isShowLine\": \"1\",\"prtype\": \"20\",\"pageSize\": \"10\",\"editionid\": \"7\",\"isAjax\":\"1\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testError() throws Exception {
		mockMvc.perform(post("/ms.selection.selectionService/getMiguGuessInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(
						"{\"paramMap\":{\"pluginCode\": \"booklist_recommend\",\"isShowLine\": \"1\",\"pageSize\": \"10\",\"editionid\": \"7\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
	}
	/**
	 * prtype=65
	 * 
	 *
	 * @author zhangmm
	 * @throws Exception
	 */
    @Test
    public void testPrtypeIsSixtyFive()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "miguguess_list");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("cornerShowType", "1");
        params.put("dataFrom", "65");
        params.put("pageSize", "10");
        params.put("sheetCount", "10");
        params.put("pageNo", "1");
        params.put("title", "咪咕猜");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getMiguGuessInfo").contentType(MediaType.APPLICATION_JSON_UTF8).header("X-Identity-ID", "15057121414")
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }
    
    /**
     * prtype！=65
     * 
     *
     * @author zhangmm
     * @throws Exception
     */
    @Test
    public void testPrtypeIsNotSixtyFive()
        throws Exception
    {
        Map<String,Map> paramMap=new HashMap<String,Map>();
        Map<String,String> params=new HashMap<String,String>();
        params.put("pluginCode", "miguguess_list");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");
        params.put("isMarginTop", "0");
        params.put("isMarginBottom", "0");

        params.put("dataFrom", "63");
        params.put("pageSize", "10");
        params.put("sheetCount", "10");
        params.put("pageNo", "1");
        params.put("title", "咪咕猜");
        paramMap.put("paramMap", params);
        ObjectMapper mapper=new ObjectMapper();
        String contentParam=mapper.writeValueAsString(paramMap);
        mockMvc
            .perform(post("/ms.selection.selectionService/getMiguGuessInfo").contentType(MediaType.APPLICATION_JSON_UTF8).header("X-Identity-ID", "15057121414")
                .content(
                    contentParam))
            .andDo(print())
            .andExpect(status().is(200))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

}
