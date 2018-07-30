package com.henrique.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathHelper {

    private final static  String rootPathURL = "/Users/henrique/temp/jp";
    private final static  String metaFileSuffix = "-meta.txt";
    private final static  String documentationPrefix = "v";
    private final static Path rootPath = Paths.get(rootPathURL);


    public static Path getRootPath() {
        return rootPath;
    }

    public static Path get(String directory) {
        return Paths.get(directory);
    }


    // SCOPE
    public static Path getScopePath(String scopeName) {
        return rootPath.resolve(scopeName);
    }

    public static Path getScopeMetaFilePath(String scopeName) {
        return getScopePath(scopeName).resolve(scopeName.concat(metaFileSuffix));
    }

    public static Path getScopeMetaFilePath(String scopeName, String metaFileName) {
        return rootPath.resolve(scopeName).resolve(metaFileName.concat(metaFileSuffix));
    }

    public static Path getScopeDocumentationPath(String scopeName, Integer version) {
        return rootPath.resolve(scopeName).resolve(documentationPrefix + version);
    }


    // MODULE
    public static Path getModulePath(String scopeName, String moduleName) {
        return rootPath.resolve(scopeName).resolve(moduleName);
    }

    public static Path getModuleMetaFilePath(String scopeName, String moduleName) {
        return getModulePath(scopeName, moduleName).resolve(moduleName.concat(metaFileSuffix));
    }

    public static Path getModuleMetaFilePath(String scopeName, String moduleName, String metaFileName) {
        return getModulePath(scopeName, moduleName).resolve(metaFileName.concat(metaFileSuffix));
    }

    public static Path getModuleDocumentationPath(String scopeName, String moduleName, Integer version) {
        return getModulePath(scopeName, moduleName).resolve(documentationPrefix + version);
    }

}
