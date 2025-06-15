package com.loriscatiz.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private final static Dotenv dotenv = Dotenv.load();

    public static String get(String key){
        return dotenv.get(key);
    }


}
