package com.henrique.service.impl;

import com.google.gson.Gson;
import com.henrique.entity.Documentation;
import com.henrique.entity.Scope;
import com.henrique.exception.NoSuchMetaFileException;
import com.henrique.exception.NoSuchScopeException;
import com.henrique.exception.FileManipulationException;
import com.henrique.service.ScopeService;
import static com.henrique.utils.PathHelper.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ScopeServiceImpl implements ScopeService {

    @Autowired
    private Gson gson;

    @Override
    public Scope getScope(String scopeName) throws Exception {
        Path scopePath = getScopePath(scopeName);

        if (scopeExists(scopePath)) {
            Path metaFilePath = getScopeMetaFilePath(scopeName);

            if (metaFileExists(metaFilePath)) {
                String content = new String(Files.readAllBytes(metaFilePath));
                Scope scope = gson.fromJson(content, Scope.class);

                return scope;
            }
        }

        return null;
    }

    @Override
    public Set<Scope> getScopes() throws Exception {
        Set<String> scopeNames = new HashSet<>();

        Path rootPath = getRootPath();
        Files.list(rootPath)
                .filter(Files::isDirectory)
                .forEach(path -> scopeNames.add(path.getFileName().toString()));

        Set<Scope> resultList = new HashSet<>();
        if (!scopeNames.isEmpty()) {
            for (String scopeName : scopeNames) {
                Scope scope = getScope(scopeName);
                resultList.add(scope);
            }
        }

        return resultList;
    }

    @Override
    public void createScope(Scope scope) throws Exception {
        String scopeName = scope.getName();
        Path scopePath = getScopePath(scopeName);
        Path metaFilePath = getScopeMetaFilePath(scopeName);

        try {
            if(scopeExists(scopePath)) throw new FileAlreadyExistsException("Scope already exists: " + scopeName);
        } catch (NoSuchScopeException e) {
            try {
                Files.createDirectory(scopePath);
                Files.createFile(metaFilePath);

                scope.setCreationDate(new Date());
                scope.setNextVersion(1);

                String jsonScope = gson.toJson(scope);

                Files.write(metaFilePath, jsonScope.getBytes());
            } catch (IOException ioe) {
                String errorMessage = "Error to create the scope: " + ioe.getMessage();
                throw new FileManipulationException(errorMessage);
            }
        }
    }

    @Override
    public Scope renameScope(Scope scope, String newName) throws Exception {
        try {
            String scopeName = scope.getName();
            Path scopePath = getScopePath(scopeName);

            if (scopeExists(scopePath)) {
                Path newDirectoryPath = getScopePath(newName);
                Files.move(scopePath, newDirectoryPath);

                Path movedMetaFilePath = getScopeMetaFilePath(newName, scopeName);
                Path newMetaFilePath = getScopeMetaFilePath(newName, newName);

                if (metaFileExists(movedMetaFilePath)) {
                    Files.move(movedMetaFilePath, newMetaFilePath);
                    scope.setName(newName);
                    String jsonScope = gson.toJson(scope);

                    Files.write(newMetaFilePath, jsonScope.getBytes());
                }
            }
        } catch (FileAlreadyExistsException e) {
            String errorMessage = "Scope already exists: " + newName;
            throw new FileAlreadyExistsException(errorMessage);
        } catch (IOException e) {
            String errorMessage = "Error to rename the scope: " + e.getMessage();
            throw new FileManipulationException(errorMessage, e);
        }

        return scope;
    }

    @Override
    public void updateProperties(Scope scope) throws Exception {
        try {
            String scopeName = scope.getName();
            Path scopePath = getScopePath(scopeName);
            Path metaFilePath = getScopeMetaFilePath(scopeName);

            if (scopeExists(scopePath)) {
                if (metaFileExists(metaFilePath)) {
                    String jsonScope = gson.toJson(scope);
                    Files.write(metaFilePath, jsonScope.getBytes());
                }
            }
        } catch (IOException e) {
            String errorMessage = "Error to update the scope's properties: " + e.getMessage();
            throw new FileManipulationException(errorMessage, e);
        }
    }

    @Override
    public void addDocumentation(Scope scope, String docDirectory) throws Exception {
        String scopeName = scope.getName();
        Path scopePath = getScopePath(scopeName);
        Path metaFilePath = getScopeMetaFilePath(scopeName);

        if (scopeExists(scopePath)) {
            if (metaFileExists(metaFilePath)) {
                Integer nextVersion = scope.getNextVersion();
                Path newDocPath = getScopeDocumentationPath(scopeName, nextVersion);
                try {
                    Path docDirectoryPath = get(docDirectory);
                    FileUtils.copyDirectory(docDirectoryPath.toFile(), newDocPath.toFile());

                    Documentation doc = new Documentation();
                    doc.setCreationDate(new Date());
                    doc.setVersion(nextVersion);
                    doc.setDirectory(newDocPath.toString());

                    scope.setNextVersion(nextVersion + 1);
                    scope.add(doc);
                    String jsonScope = gson.toJson(scope);

                    Files.write(metaFilePath, jsonScope.getBytes());
                } catch (IOException e) {
                    String errorMessage = "Error to add the documentation: " + e.getMessage();
                    throw new FileManipulationException(errorMessage, e);
                }
            }
        }
    }

    @Override
    public Scope removeScope(Scope scope) throws Exception {
        String scopeName = scope.getName();
        Path scopePath = getScopePath(scopeName);
        if (scopeExists(scopePath)) {
            try {
                FileUtils.deleteDirectory(scopePath.toFile());
            } catch (IOException e) {
                String errorMessage = ("Error to remove the path: ").concat(scopePath.toString()).concat(". ").concat(e.getMessage());
                throw new FileManipulationException(errorMessage, e);
            }

            return scope;
        }

        return null;
    }

    private boolean scopeExists(Path path) throws NoSuchScopeException {
        if (Files.exists(path)) return true;
        String errorMessage = "Scope doesn't exist: " + path.getFileName();
        throw new NoSuchScopeException(errorMessage);
    }

    private boolean metaFileExists(Path path) throws NoSuchMetaFileException {
        if (Files.exists(path)) return true;
        String errorMessage = "Meta file doesn't exist: " + path.getFileName();
        throw new NoSuchMetaFileException(errorMessage);
    }
}
