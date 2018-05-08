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

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-core.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonalRecommendServiceTest {
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testView() throws Exception {
		mockMvc.perform(post("/ms.selection.personalRecommendService/getPersonalRecommendInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(
						"{\"paramMap\":{\"pluginCode\": \"recommend_person\",\"pageSize\": \"10\",\"prtype\": \"9\",\"isAjax\":\"1\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testError() throws Exception {
		mockMvc.perform(post("/ms.selection.personalRecommendService/getPersonalRecommendInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(
						"{\"paramMap\":{\"pluginCode\": \"recommend_person\",\"pageSize\": \"10\",\"prtype\": \"19\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

	}
}
