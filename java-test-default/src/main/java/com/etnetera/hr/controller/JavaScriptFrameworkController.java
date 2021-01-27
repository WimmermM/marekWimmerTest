package com.etnetera.hr.controller;

import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

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
	public void addFramework(@RequestBody String name, String version, String deprecationDate, String hypeLevel) {
		service.addFramework(name,version,deprecationDate,hypeLevel);
	}

	@PostMapping(path = "/frameworks/find/{id}")
	public void findFrameworkById(@PathVariable Long id){service.findById(id);}


	@PostMapping(path = "/frameworks/delete/{id}")
	public void DeleteFramework(@PathVariable Long id) {
		service.deleteFramework(id);
	}
}
