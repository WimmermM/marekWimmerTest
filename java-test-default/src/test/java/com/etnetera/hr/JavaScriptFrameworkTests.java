package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.etnetera.hr.service.JavaScriptFrameworkService;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private JavaScriptFrameworkRepository repository;

	@Autowired
	JavaScriptFrameworkService service;

	private void prepareData() throws Exception {
		JavaScriptFramework react = new JavaScriptFramework("ReactJS", "1.0.1", "cas", new BigDecimal(20));
		JavaScriptFramework vue = new JavaScriptFramework("Vue.js","1.0.0", "cas", new BigDecimal(20));
		
		repository.save(react);
		repository.save(vue);
	}

	@Test
	public void frameworksTest() throws Exception {
		prepareData();

		mockMvc.perform(get("/frameworks")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Vue.js")));
	}
	
	@Test
	public void addFrameworkInvalid() throws JsonProcessingException, Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		mockMvc.perform(get("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));
		
		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(get("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors", hasSize(1)))
			.andExpect(jsonPath("$.errors[0].field", is("name")))
			.andExpect(jsonPath("$.errors[0].message", is("Size")));
		
	}

	@Test
	public void deleteFramework() throws Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setId(1L);
		framework.setName("test");
		framework.setDeprecationDate("String");
		framework.setVersion("1.0.0");
		framework.setHypeLevel(new BigDecimal(20));
		repository.save(framework);

		mockMvc.perform(post("/frameworks/delete/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void  addFramework() throws Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setName("test");
		framework.setDeprecationDate("casd");
		framework.setVersion("1.0.0");
		framework.setHypeLevel(new BigDecimal(20));

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(framework);
		mockMvc.perform(post("/frameworks/add").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	@Test
	public void findFramework() throws Exception {
		JavaScriptFramework framework = new JavaScriptFramework();
		framework.setId(1L);
		framework.setName("test");
		framework.setDeprecationDate("cas");
		framework.setVersion("1.0.0");
		framework.setHypeLevel(new BigDecimal(20));
		repository.save(framework);

		mockMvc.perform(post("/frameworks/find/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}
}
