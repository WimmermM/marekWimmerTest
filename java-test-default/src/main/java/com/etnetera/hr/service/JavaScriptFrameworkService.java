package com.etnetera.hr.service;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.in.JavaScriptFrameworkRequest;
import com.etnetera.hr.out.FrameworkWrapperObject;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository repository;


    public FrameworkWrapperObject addFramework(JavaScriptFrameworkRequest request) {
        Optional<JavaScriptFramework> frameworkExists = repository.findByName(request.getName());
        JavaScriptFramework framework;
        if (frameworkExists.isPresent()) {
            framework = frameworkExists.get();
            if (checkIfVersionExist(framework.getVersions(), request.getVersion())) {
                return createResponse("Framework with name" + request.getName() + "and Version " + request.getVersion() + " already exists!!!", framework);
            } else {
                saveVersion(framework,request);
                return createResponse("Added new version " + request.getVersion() + "to framework " + request.getName(), framework);
            }
        }
        framework = saveFramework(request);
//        saveVersion(frameworkToSave, request);
//        Optional<JavaScriptFramework> savedFramework = repository.findById(frameworkToSave.getId());
        return createResponse("Framework saved", framework);
    }

    public FrameworkWrapperObject updateFramework (Long id, JavaScriptFrameworkRequest request) {
        Optional<JavaScriptFramework> frameworkToUpdate = repository.findById(id);
            JavaScriptFramework framework;
        if (frameworkToUpdate.isPresent()){
            framework = frameworkToUpdate.get();
            Optional<FrameworkVersion> existingVersion = getExistingVersion(framework.getVersions(), request.getVersion());
            if (!frameworkToUpdate.get().getName().equals(request.getName())) {
                framework.setName(request.getName());
                repository.save(framework);
            }
            if (existingVersion.isPresent()) {
                FrameworkVersion version = existingVersion.get();
                version.setVersion(request.getVersion());
                version.setDeprecationDate(request.getDeprecationDate());
                version.setHypeLevel(request.getHypeLevel());
                framework.getVersions().add(version);
                repository.save(framework);
                return createResponse("Framework changed", framework);
            } else {
                saveVersion(framework, request);
                return createResponse("Added new version " + request.getVersion() + "to framework " + request.getName(), framework);
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
                .versions(new ArrayList<>(0))
                .build();
        FrameworkVersion version = FrameworkVersion.builder()
                .version(request.getVersion())
                .hypeLevel(request.getHypeLevel())
                .deprecationDate(request.getDeprecationDate())
                .build();
        framework.getVersions().add(version);
        return repository.save(framework);

    }

    private void saveVersion(JavaScriptFramework framework, JavaScriptFrameworkRequest request) {
        FrameworkVersion version = FrameworkVersion.builder()
                .hypeLevel(request.getHypeLevel())
                .version(request.getVersion())
                .deprecationDate(request.getDeprecationDate())
                .framework(framework)
                .build();
        framework.getVersions().add(version);
        repository.save(framework);
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
