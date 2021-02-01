package com.etnetera.hr;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	private FrameworkVersionRepository versionRepository;


	public static JavaScriptFramework framework;
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

		FrameworkVersion version = new FrameworkVersion("1","2",1,react);

		versionRepository.save(version);
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
				.andExpect(jsonPath("message", is("Framework saved")))
				.andExpect(jsonPath("data.id", is(1)))
				.andExpect(jsonPath("data.name", is("prepare")));

	}

	@Test
	public void findFramework() throws Exception {
		prepareData();
		mockMvc.perform(get("/frameworks/find/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("message", is("Framework found")))
				.andExpect(jsonPath("data.id", is(1)))
				.andExpect(jsonPath("data.name", is("ReactJS")))
				.andExpect(jsonPath("data.versions", hasSize(1)))
				.andExpect(jsonPath("data.versions[0].id", is(1)))
				.andExpect(jsonPath("data.versions[0].version", is("1")))
				.andExpect(jsonPath("data.versions[0].deprecationDate", is("2")))
				.andExpect(jsonPath("data.versions[0].hypeLevel", is(1)));
	}

	@Test
	public void updateFramework() throws Exception {
		framework.setName("testUpdate");
		mockMvc.perform(put("/frameworks/delete/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("message", is("Framework name changed")))
				.andExpect(jsonPath("data.id", is(1)))
				.andExpect(jsonPath("data.name", is("ReactJS")))
				.andExpect(jsonPath("data.versions", hasSize(1)))
				.andExpect(jsonPath("data.versions[0].id", is(1)))
				.andExpect(jsonPath("data.versions[0].version", is("1")))
				.andExpect(jsonPath("data.versions[0].deprecationDate", is("2")))
				.andExpect(jsonPath("data.versions[0].hypeLevel", is(1)));
	}

	@Test
	public void deleteFramework() throws Exception {
		prepareData();
		mockMvc.perform(delete("/frameworks/delete/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath ("message", is("Framework Deleted")));

		mockMvc.perform(delete("/frameworks/delete/4").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath ("message", is("Framework with id: 4 not found in database")));
	}

}
