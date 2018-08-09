package com.henrique.service.impl;

import com.google.gson.Gson;
import com.henrique.entity.Documentation;
import com.henrique.entity.Module;
import com.henrique.entity.Scope;
import com.henrique.exception.*;
import com.henrique.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.henrique.utils.PathHelper.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private Gson gson;

    @Override
    public Module getModule(Scope scope, String moduleName) throws Exception {
        String scopeName = scope.getName();
        Path modulePath = getModulePath(scopeName, moduleName);

        if (moduleExists(modulePath)) {

            Path metaFilePath = getModuleMetaFilePath(scopeName, moduleName);

            if (metaFileExists(metaFilePath)) {
                String content = new String(Files.readAllBytes(metaFilePath));
                Module module = gson.fromJson(content, Module.class);

                return module;
            }
        }

        return null;
    }

    @Override
    public Set<Module> getModules(Scope scope) throws Exception {
        Set<String> moduleNames = new HashSet<>();

        String scopeName = scope.getName();
        Path scopePath = getScopePath(scopeName);

        Files.list(scopePath)
                .filter(Files::isDirectory)
                .forEach(path -> moduleNames.add(path.getFileName().toString()));

        Set<Module> resultList = new HashSet<>();
        if (!moduleNames.isEmpty()) {
            for (String moduleName : moduleNames) {
                Module module = getModule(scope, moduleName);
                resultList.add(module);
            }
        }

        return resultList;
    }

    @Override
    public void createModule(Scope scope, Module module) throws Exception {
        String scopeName = scope.getName();
        String moduleName = module.getName();
        Path modulePath = getModulePath(scopeName, moduleName);

        Path metaFilePath = getModuleMetaFilePath(scopeName, moduleName);

        if (Files.notExists(modulePath)) {
            try {
                Files.createDirectory(modulePath);
                Files.createFile(metaFilePath);

                module.setCreationDate(new Date());
                module.setNextVersion(1);

                String jsonModule = gson.toJson(module);

                Files.write(metaFilePath, jsonModule.getBytes());
            } catch (IOException e) {
                String errorMessage = "Error to create the module: " + e.getMessage();
                throw new FileManipulationException(errorMessage);
            }
        } else {
            String errorMessage = "Module already exists: " + module.getName();
            throw new FileManipulationException(errorMessage);
        }
    }

    @Override
    public Module renameModule(Scope scope, Module module, String newName) throws Exception {
        try {
            String scopeName = scope.getName();
            String moduleName = module.getName();
            Path modulePath = getModulePath(scopeName, moduleName);

            if (moduleExists(modulePath)) {
                Path newModulePath = getModulePath(scopeName, newName);
                Files.move(modulePath, newModulePath);

                Path metaFileName = getModuleMetaFilePath(scopeName, newName, moduleName);
                Path newMetaFilePath = getModuleMetaFilePath(scopeName, newName, newName);

                if (metaFileExists(metaFileName)) {
                    Files.move(metaFileName, newMetaFilePath);
                    module.setName(newName);
                    String jsonModule = gson.toJson(module);

                    Files.write(newMetaFilePath, jsonModule.getBytes());
                }
            }
        } catch (FileAlreadyExistsException e) {
            String errorMessage = "Module already exists: " + newName;
            throw new FileAlreadyExistsException(errorMessage);
        } catch (IOException e) {
            String errorMessage = "Error to rename the module: " + e.getMessage();
            throw new FileManipulationException(errorMessage, e);
        }

        return module;
    }

    @Override
    public void updateProperties(Scope scope, Module module) throws Exception {
        try {
            String scopeName = scope.getName();
            String moduleName = module.getName();
            Path modulePath = getModulePath(scopeName, moduleName);

            if (moduleExists(modulePath)) {
                Path metaFilePath = getModuleMetaFilePath(scopeName, moduleName);
                if (metaFileExists(metaFilePath)) {
                    String jsonScope = gson.toJson(scope);
                    Files.write(metaFilePath, jsonScope.getBytes());
                }
            }
        } catch (IOException e) {
            String errorMessage = "Error to update the module's properties: " + e.getMessage();
            throw new FileManipulationException(errorMessage, e);
        }
    }

    @Override
    public void addDocumentation(Scope scope, Module module, String docDirectory) throws Exception {
        String scopeName = scope.getName();
        String moduleName = module.getName();
        Path modulePath = getModulePath(scopeName, moduleName);

        if (moduleExists(modulePath)) {

            Path metaFilePath = getModuleMetaFilePath(scopeName, moduleName);
            if (metaFileExists(metaFilePath)) {
                Integer nextVersion = module.getNextVersion();
                Path newDocPath = getModuleDocumentationPath(scopeName, moduleName, nextVersion);

                try {
                    Path docDirectoryPath = get(docDirectory);
                    FileUtils.copyDirectory(docDirectoryPath.toFile(), newDocPath.toFile());

                    Documentation doc = new Documentation();
                    doc.setCreationDate(new Date());
                    doc.setVersion(nextVersion);
                    doc.setDirectory(newDocPath.toString());

                    module.setNextVersion(nextVersion + 1);
                    module.add(doc);
                    String jsonModule = gson.toJson(module);

                    Files.write(metaFilePath, jsonModule.getBytes());
                } catch (IOException e) {
                    String errorMessage = "Error to add the documentation: " + e.getMessage();
                    throw new FileManipulationException(errorMessage, e);
                }
            }
        }
    }

    @Override
    public Module removeModule(Scope scope, Module module) throws Exception {
        String scopeName = scope.getName();
        String moduleName = module.getName();
        Path modulePath = getModulePath(scopeName, moduleName);

        if (moduleExists(modulePath)) {
            try {
                FileUtils.deleteDirectory(modulePath.toFile());
            } catch (IOException e) {
                String errorMessage = ("Error to remove the path: ").concat(modulePath.toString()).concat(". ").concat(e.getMessage());
                throw new FileManipulationException(errorMessage, e);
            }
        }

        return module;
    }

    private boolean moduleExists(Path path) throws NoSuchModuleException {
        if (Files.exists(path)) return true;
        String errorMessage = "Module doesn't exist: " + path.getFileName();
        throw new NoSuchModuleException(errorMessage);
    }

    private boolean metaFileExists(Path path) throws NoSuchMetaFileException {
        if (Files.exists(path)) return true;
        String errorMessage = "Meta file doesn't exist: " + path.getFileName();
        throw new NoSuchMetaFileException(errorMessage);
    }
}
