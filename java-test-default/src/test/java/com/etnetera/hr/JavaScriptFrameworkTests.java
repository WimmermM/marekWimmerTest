package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import com.etnetera.hr.repository.FrameworkVersionRepository;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.BeforeClass;
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
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
	private FrameworkVersionRepository versionRepository;


	public static JavaScriptFramework framework;
	public static JavaScriptFramework frameworkInvalid;
	public static FrameworkVersion frameworkVersion;
	public static JavaScriptFrameworkRequest request;



	@BeforeClass
	public static void prepare() {
		 request = JavaScriptFrameworkRequest.builder()
				 .name("prepare")
				 .deprecationDate("2")
				 .hypeLevel(1)
				 .version("2")
				 .build();
	}

	private void prepareData() throws Exception {
		JavaScriptFramework react = new JavaScriptFramework("ReactJS");
		JavaScriptFramework vue = new JavaScriptFramework("Vue.js");
		
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
		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));
		
		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors", hasSize(1)))
			.andExpect(jsonPath("$.errors[0].field", is("name")))
			.andExpect(jsonPath("$.errors[0].message", is("Size")));
		
	}


	@Test
	public void addFramework() throws Exception {
		mockMvc.perform(post("/frameworks/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("Framework saved")))
				.andExpect(jsonPath("$.data[0].id", is(1)))
				.andExpect(jsonPath("$.data[1].name", is("prepare")));

	}

	@Test(expected = NestedServletException.class)
	public void addFrameworkMissingValue() throws Exception {
		mockMvc.perform(post("/frameworks/add").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(frameworkInvalid)));
	}

	@Test
	public void findFramework() throws Exception {
		prepareData();
		mockMvc.perform(post("/frameworks/find/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[1].message", is("Framework found")))
				.andExpect(jsonPath("$[2].data[0].id", is(1)))
				.andExpect(jsonPath("$[2].data[1].name", is("Mareksdsa")))
				.andExpect(jsonPath("$[3].data[2].versions", hasSize(4)))
				.andExpect(jsonPath("$[3].data[2].versions[0].id", is(1)))
				.andExpect(jsonPath("$[3].data[2].versions[1].version", is("1")))
				.andExpect(jsonPath("$[3].data[2].versions[2].deprecationDate", is("2")))
				.andExpect(jsonPath("$[3].data[2].versions[3].hypeLevel", is(1)));
	}

	@Test
	public void updateFramework() throws Exception {
		prepareData();
		framework.setName("testUpdate");
		mockMvc.perform(put("/frameworks/delete/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteFramework() throws Exception {
		prepareData();
		mockMvc.perform(delete("/frameworks/delete/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

}
