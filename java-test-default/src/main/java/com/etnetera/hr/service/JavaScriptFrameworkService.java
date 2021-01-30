package com.etnetera.hr.service;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import com.etnetera.hr.out.FrameworkResponse;
import com.etnetera.hr.out.FrameworkWrapperObject;
import com.etnetera.hr.repository.FrameworkVersionRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;
    private final FrameworkVersionRepository frameworkVersionRepository;


    public FrameworkWrapperObject addFramework(JavaScriptFrameworkRequest request) {
        Optional<JavaScriptFramework> frameworkExists = repository.findByName(request.getName());
        if (frameworkExists.isPresent()) {
            JavaScriptFramework framework = frameworkExists.get();
            if (checkIfVersionExist(framework.getVersions(), request.getVersion())) {
                return createResponse("Framework with name" + request.getName() + "and Version " + request.getVersion() + " already exists!!!", framework);
            } else {
                saveVersion(framework,request);
                return createResponse("Added new version " + request.getVersion() + "to framework " + request.getName(), framework);
            }
        }
        JavaScriptFramework frameworkToSave = saveFramework(request);
        saveVersion(frameworkToSave, request);
        Optional<JavaScriptFramework> savedFramework = repository.findById(frameworkToSave.getId());
        return createResponse("Framework saved", savedFramework.get());
    }

    public FrameworkWrapperObject updateFramework (Long id, JavaScriptFrameworkRequest request) {
        Optional<JavaScriptFramework> frameworkToUpdate = repository.findById(id);
        if (frameworkToUpdate.isPresent()){
            JavaScriptFramework framework = frameworkToUpdate.get();
            Optional<FrameworkVersion> existingVersion = getExistingVersion(framework.getVersions(), request.getVersion());
            if (!frameworkToUpdate.get().getName().equals(request.getName())) {
                framework.setName(request.getName());
                repository.save(framework);
                return createResponse("Framework name changed", framework);
            }
            if (existingVersion.isPresent()) {
                FrameworkVersion version = existingVersion.get();
                version.setVersion(request.getVersion());
                version.setDeprecationDate(request.getDeprecationDate());
                version.setHypeLevel(request.getHypeLevel());
                frameworkVersionRepository.save(version);
                 Optional<JavaScriptFramework> updatedframework = repository.findById(framework.getId());
                return createResponse("Framework version changed", updatedframework.get());
            } else {
                saveVersion(framework, request);
                Optional<JavaScriptFramework> updatedframework = repository.findById(framework.getId());
                return createResponse("Added new version " + request.getVersion() + "to framework " + request.getName(), updatedframework.get());
            }
        }
        return createResponse("Framework with id: " + id + " not found in database", null);
    }


    public FrameworkWrapperObject deleteFramework(Long id) {
        Optional<JavaScriptFramework> framework = repository.findById(id);
        if (framework.isPresent()) {
            repository.deleteById(id);
            return createResponse("Framework Deleted", framework.get());
        }
        return createResponse("Framework with id: " + id + " not found in database", null);
    }


    public FrameworkWrapperObject findById(Long id){
        Optional<JavaScriptFramework> framework = repository.findById(id);
        if (framework.isPresent()){
            return createResponse("Framework found", framework.get());
        }
        return createResponse("Framework with id: " + id + " does not exists!", null);
    }

    private boolean checkIfVersionExist(List<FrameworkVersion> versionList, String version) {
        return versionList.stream().anyMatch(v -> v.getVersion().equals(version));
    }

    private JavaScriptFramework saveFramework(JavaScriptFrameworkRequest request) {
        JavaScriptFramework framework = JavaScriptFramework.builder()
                .name(request.getName())
                .build();
        return repository.save(framework);

    }

    private void saveVersion(JavaScriptFramework framework, JavaScriptFrameworkRequest request) {
        FrameworkVersion version = FrameworkVersion.builder()
                .hypeLevel(request.getHypeLevel())
                .version(request.getVersion())
                .deprecationDate(request.getDeprecationDate())
                .framework(framework)
                .build();
        frameworkVersionRepository.save(version);
    }

    private FrameworkWrapperObject createResponse(String message, JavaScriptFramework framework) {
        return FrameworkWrapperObject.builder()
                .message(message)
                .framework(framework)
                .build();
    }

    private Optional<FrameworkVersion> getExistingVersion (List<FrameworkVersion> versions, String version) {
        return versions.stream().filter(v -> v.getVersion().equals(version)).findAny();
    }
}
