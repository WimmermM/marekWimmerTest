package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;


    public void addFramework(JavaScriptFramework framework){
        repository.save(framework);
    }

    public void updateFramework (Long id, JavaScriptFramework framework) {
        JavaScriptFramework frameworkToUpdate = repository.findById(id).get();
        if(repository.existsById(id)){
            framework.setName(frameworkToUpdate.getName());
            framework.setVersion(frameworkToUpdate.getVersion());
            framework.setDeprecationDate(frameworkToUpdate.getDeprecationDate());
            framework.setHypeLevel(frameworkToUpdate.getHypeLevel());
            repository.save(framework);
        } else {
            throw new NoSuchElementException("Framework with " + id + " not found");
        }
    }

    public void deleteFramework(Long id) {
        repository.deleteById(id);
    }


    public Optional<JavaScriptFramework> findById(Long id){
        return repository.findById(id);
    }
}
