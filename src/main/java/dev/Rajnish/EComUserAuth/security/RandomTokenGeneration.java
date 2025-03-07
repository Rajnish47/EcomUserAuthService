package dev.Rajnish.EComUserAuth.security;

import java.util.Random;
import java.util.stream.Collectors;

public class RandomTokenGeneration {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public static String generateRandomString(int length) {
        return RANDOM.ints(length, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
