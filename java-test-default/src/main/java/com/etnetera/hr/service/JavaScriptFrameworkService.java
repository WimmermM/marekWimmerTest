package com.etnetera.hr.service;

import com.etnetera.hr.data.FrameworkVersion;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.in.JavaScriptFrameworkRequest;
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

    public JavaScriptFramework addFramework(JavaScriptFrameworkRequest request) {
        JavaScriptFramework framework = JavaScriptFramework.builder()
                .name(request.getName())
                .versions(new ArrayList<>(0))
                .build();

        return repository.save(framework);
    }

    public Optional<JavaScriptFramework> getFramework(Long id) {
        return repository.findById(id);
    }

    public void deleteFramework(JavaScriptFramework framework) {
        repository.delete(framework);
    }

    public JavaScriptFramework updateFramework(JavaScriptFramework framework, JavaScriptFrameworkRequest request) {
        if (!framework.getName().equals(request.getName())) {
            framework.setName(request.getName());
            return repository.save(framework);
        }
        return framework;
    }

    public Optional<JavaScriptFramework> addOrUpdateVersion(JavaScriptFramework framework, JavaScriptFrameworkRequest request) {
        Optional<FrameworkVersion> existingVersion = getExistingVersion(framework.getVersions(), request.getVersion());
        FrameworkVersion version;
        if (existingVersion.isEmpty()) {
            version = FrameworkVersion.builder()
                    .version(request.getVersion())
                    .deprecationDate(request.getDeprecationDate())
                    .hypeLevel(request.getHypeLevel())
                    .build();
            framework.addVersion(version);
        } else {
            existingVersion.get().setDeprecationDate(request.getDeprecationDate());
            existingVersion.get().setHypeLevel(request.getHypeLevel());
        }
        return Optional.of(repository.save(framework));
    }

    private Optional<FrameworkVersion> getExistingVersion (List<FrameworkVersion> versions, String version) {
        return versions.stream().filter(v -> v.getVersion().equals(version)).findAny();
    }
}