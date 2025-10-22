
package com.minitienda.config;


public class DatabaseConfig {
    
    // ========== CONFIGURACIÓN MYSQL ==========
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "minitienda_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Qwe.123*";  
    

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    

    private static final String DB_PARAMS = "useSSL=false&serverTimezone=UTC";
    

    public static String getUrl() {
        return String.format("jdbc:mysql://%s:%s/%s?%s", 
                DB_HOST, DB_PORT, DB_NAME, DB_PARAMS);
    }
    

    public static String getUser() {
        return DB_USER;
    }
    
 
    public static String getPassword() {
        return DB_PASSWORD;
    }
    

    public static String getDriver() {
        return DB_DRIVER;
    }
    
 
    public static String getDatabaseName() {
        return DB_NAME;
    }
    
  
    public static void printConfig() {
        System.out.println("========== CONFIGURACIÓN DE BASE DE DATOS ==========");
        System.out.println("URL:      " + getUrl());
        System.out.println("Usuario:  " + getUser());
        System.out.println("Password: " + maskPassword(getPassword()));
        System.out.println("Driver:   " + getDriver());
        System.out.println("===================================================");
    }
    
   
    private static String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "[VACÍA]";
        }
        if (password.length() <= 2) {
            return "**";
        }
        return password.charAt(0) + "*".repeat(password.length() - 2) + password.charAt(password.length() - 1);
    }
}