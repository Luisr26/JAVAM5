
package com.minitienda.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
    
    static {
        try {
            // Cargar el driver de MySQL
            Class.forName(DatabaseConfig.getDriver());
            System.out.println("✓ Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se pudo cargar el driver de MySQL.\n" +
                    "Asegúrate de tener la dependencia mysql-connector-j en el pom.xml", e);
        }
    }
    
  
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                    DatabaseConfig.getUrl(),
                    DatabaseConfig.getUser(),
                    DatabaseConfig.getPassword()
            );
            System.out.println("✓ Conexión establecida con la base de datos: " + DatabaseConfig.getDatabaseName());
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar a la base de datos");
            System.err.println("  URL: " + DatabaseConfig.getUrl());
            System.err.println("  Usuario: " + DatabaseConfig.getUser());
            System.err.println("  Mensaje: " + e.getMessage());
            throw e;
        }
    }
    
  
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✓ Prueba de conexión exitosa");
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Prueba de conexión fallida: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("\n========== PRUEBA DE CONEXIÓN A BASE DE DATOS ==========\n");
        
        DatabaseConfig.printConfig();
        
        System.out.println("\nIntentando conectar...\n");
        
        if (testConnection()) {
            System.out.println("\n✅ ¡Conexión exitosa! La base de datos está configurada correctamente.");
        } else {
            System.out.println("\n❌ Error en la conexión. Verifica:");
            System.out.println("   1. Que MySQL esté ejecutándose");
            System.out.println("   2. Que la base de datos '" + DatabaseConfig.getDatabaseName() + "' exista");
            System.out.println("   3. Que el usuario y contraseña sean correctos");
            System.out.println("   4. Que el puerto " + DatabaseConfig.getUrl().split(":")[2].split("/")[0] + " esté disponible");
        }
        
        System.out.println("\n======================================================\n");
    }
}