package com.etnetera.hr.controller;


import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import com.etnetera.hr.out.FrameworkResponse;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import java.util.Optional;

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
	public FrameworkResponse<JavaScriptFramework> addFramework (@RequestBody JavaScriptFrameworkRequest request) {
		JavaScriptFramework framework = service.addFramework(request);
		Optional<JavaScriptFramework> toReturn = service.addOrUpdateVersion(framework, request);
		return new FrameworkResponse<JavaScriptFramework>(HttpStatus.CREATED.value(), createMessage(framework.getId(), "created"), toReturn.get());
	}

	@GetMapping(path = "/frameworks/find/{id}")
	public FrameworkResponse<JavaScriptFramework> findFrameworkById(@PathVariable Long id){
		Optional<JavaScriptFramework> framework = service.getFramework(id);
		if (framework.isEmpty()) {
			return returnNotFound(id);
		}
		return new FrameworkResponse<JavaScriptFramework>(HttpStatus.OK.value(), createMessage(id, "found"), framework.get());
	}

	@DeleteMapping(path = "/frameworks/delete/{id}")
	public FrameworkResponse<JavaScriptFramework> deleteFramework(@PathVariable Long id) {
		Optional<JavaScriptFramework> framework = service.getFramework(id);

		if (framework.isEmpty()) {
			return returnNotFound(id);
		}

		service.deleteFramework(framework.get());
		return new FrameworkResponse<JavaScriptFramework>(HttpStatus.NO_CONTENT.value(), createMessage(id, "deleted"));
	}

	@PutMapping(path = "/frameworks/update/{id}")
	public FrameworkResponse<JavaScriptFramework> updateFramework(@PathVariable Long id, @RequestBody JavaScriptFrameworkRequest request) {
		Optional<JavaScriptFramework> framework = service.getFramework(id);

		if (framework.isEmpty()) {
			return returnNotFound(id);
		}

		JavaScriptFramework updatedFrameWork = service.updateFramework(framework.get(), request);
		Optional<JavaScriptFramework> frameworkOptional = service.addOrUpdateVersion(updatedFrameWork, request);
		return new FrameworkResponse<JavaScriptFramework>(HttpStatus.OK.value(), createMessage(id, "updated"), frameworkOptional.get());
	}

	private FrameworkResponse<JavaScriptFramework> returnNotFound(Long id) {
		return new FrameworkResponse<JavaScriptFramework>(HttpStatus.OK.value(), createNotFoundMessage(id));
	}

	private String createMessage(Long id, String action) {
		return "Framework id: " + id + " " +action;
	}

	private String createNotFoundMessage(Long id) {
		return createMessage(id, "not found");
	}
}