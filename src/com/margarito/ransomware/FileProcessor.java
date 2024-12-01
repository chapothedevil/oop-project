package com.margarito.ransomware;

import java.io.File;
import java.util.List;

public abstract class FileProcessor {
    protected String rootDirectory;

    public FileProcessor(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    // Template method for processing files
    public abstract void processFiles(List<String> filePaths);

    // Utility method to check file extensions
    protected boolean hasExtension(String fileName, List<String> extensions) {
        return extensions.stream().anyMatch(fileName.toLowerCase()::endsWith);
    }
}
