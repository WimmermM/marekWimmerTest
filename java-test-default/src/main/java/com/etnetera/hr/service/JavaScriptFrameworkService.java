package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JavaScriptFrameworkService {

    @Autowired
    private final JavaScriptFrameworkRepository repository;

    public JavaScriptFrameworkService(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }


    public JavaScriptFramework addFramework(String name, String version, String deprecationDate, String hypeLevel ){
        return new JavaScriptFramework(name, version, deprecationDate, hypeLevel);
    }

    public void updateFramework (Long id) {
        JavaScriptFramework framework = repository.findById(id).get();
        repository.save(framework);
    }

    public void deleteFramework(Long id) {
        repository.deleteById(id);
    }


    public Optional<JavaScriptFramework> findById(Long id){
        return repository.findById(id);
    }




}
