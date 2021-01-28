package com.etnetera.hr.controller;

import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import com.etnetera.hr.out.FrameworkResponse;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;
	private final JavaScriptFrameworkService service;


	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping(path = "/frameworks/add")
	public FrameworkResponse addFramework (@RequestBody JavaScriptFramework request) {
		service.addFramework(request);
		return new FrameworkResponse(HttpStatus.CREATED, "Framework " + request.toString() + " Saved");
	}

	@PostMapping(path = "/frameworks/find/{id}")
	public FrameworkResponse findFrameworkById(@PathVariable Long id){
		return new FrameworkResponse(HttpStatus.OK,service.findById(id).toString());}


	@DeleteMapping(path = "/frameworks/delete/{id}")
	public FrameworkResponse DeleteFramework(@PathVariable Long id) {
		service.deleteFramework(id);
		return new FrameworkResponse(HttpStatus.OK,"Framework Deleted " + service.findById(id).toString());
	}

	@PutMapping(path = "/frameworks/delete/{id}")
	public FrameworkResponse updateFramework(@PathVariable Long id, @RequestBody JavaScriptFramework framework) {
		service.updateFramework(id, framework);
		return new FrameworkResponse(HttpStatus.OK,"Framework Updated " + service.findById(id).toString());
	}
}
