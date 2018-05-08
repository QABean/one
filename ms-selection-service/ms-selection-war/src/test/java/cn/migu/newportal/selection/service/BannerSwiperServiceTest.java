package cn.migu.newportal.selection.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
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
public class BannerSwiperServiceTest {
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Ignore
	@Test
	public void testView() throws Exception {
		mockMvc.perform(post("/ms.selection.bannerSwiperService/getBannerSwiperInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(
						"{\"paramMap\":{\"isAjax\":\"1\",\"linkids\":\"48088,48089,48090,48091\",\"pluginCode\":\"polling_link\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
	}

	@Ignore
	@Test
	public void testError() throws Exception {
		mockMvc.perform(
				post("/ms.selection.bannerSwiperService/getBannerSwiperInfo").contentType(MediaType.APPLICATION_JSON_UTF8)
						.content("{\"paramMap\":{\"pluginCode\":\"polling_link\"}}"))
				.andDo(print()).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

	}
}
