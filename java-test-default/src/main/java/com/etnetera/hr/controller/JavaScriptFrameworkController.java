package com.etnetera.hr.controller;

import com.etnetera.hr.out.FrameworkResponse;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.time.LocalDate;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;
	private final JavaScriptFrameworkService service;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository, JavaScriptFrameworkService service) {
		this.repository = repository;
		this.service = service;
	}

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping(path = "/frameworks/add")
	public FrameworkResponse addFramework (@RequestBody String name, String version, String deprecationDate, BigDecimal hypeLevel) {
		service.addFramework(name, version, deprecationDate, hypeLevel);
		return new FrameworkResponse(HttpStatus.CREATED, "Framework Saved");
	}

	@PostMapping(path = "/frameworks/find/{id}")
	public FrameworkResponse findFrameworkById(@PathVariable Long id){
		return new FrameworkResponse(HttpStatus.OK,service.findById(id).toString());}


	@PostMapping(path = "/frameworks/delete/{id}")
	public FrameworkResponse DeleteFramework(@PathVariable Long id) {
		service.deleteFramework(id);
		return new FrameworkResponse(HttpStatus.OK,"Framework Deleted");
	}
}
