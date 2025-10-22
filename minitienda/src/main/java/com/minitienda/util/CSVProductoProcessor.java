/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.minitienda.util;

/**
 *
 * @author Coder
 */
import com.minitienda.modelo.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para procesar archivos CSV de productos.
 */
public class CSVProductoProcessor {
    
    /**
     * Resultado del procesamiento del CSV.
     */
    public static class ResultadoCarga {
        private final List<Producto> productosExitosos;
        private final List<String> errores;
        private final int totalProcesados;
        
        public ResultadoCarga(List<Producto> productosExitosos, List<String> errores, int totalProcesados) {
            this.productosExitosos = productosExitosos;
            this.errores = errores;
            this.totalProcesados = totalProcesados;
        }
        
        public List<Producto> getProductosExitosos() {
            return productosExitosos;
        }
        
        public List<String> getErrores() {
            return errores;
        }
        
        public int getTotalProcesados() {
            return totalProcesados;
        }
        
        public int getExitosos() {
            return productosExitosos.size();
        }
        
        public int getFallidos() {
            return errores.size();
        }
        
        public boolean tieneErrores() {
            return !errores.isEmpty();
        }
    }
    
   
    public static ResultadoCarga procesarCSV(File archivo) throws IOException {
        List<Producto> productosExitosos = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        int linea = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo));
             CSVParser csvParser = new CSVParser(reader, 
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreEmptyLines(true)
                             .setTrim(true)
                             .build())) {
            
            for (CSVRecord record : csvParser) {
                linea++;
                try {
                  
                    if (record.size() < 3) {
                        errores.add("Línea " + (linea + 1) + ": Faltan columnas (se esperan: nombre,precio,stock)");
                        continue;
                    }
                    
                    // Obtener valores
                    String nombre = record.get(0).trim();
                    String precioStr = record.get(1).trim();
                    String stockStr = record.get(2).trim();
                    
                    // Validar campos vacíos
                    if (nombre.isEmpty()) {
                        errores.add("Línea " + (linea + 1) + ": El nombre no puede estar vacío");
                        continue;
                    }
                    
                    if (precioStr.isEmpty()) {
                        errores.add("Línea " + (linea + 1) + ": El precio no puede estar vacío");
                        continue;
                    }
                    
                    if (stockStr.isEmpty()) {
                        errores.add("Línea " + (linea + 1) + ": El stock no puede estar vacío");
                        continue;
                    }
                    
                    // Convertir precio y stock
                    double precio;
                    int stock;
                    
                    try {
                        precio = Double.parseDouble(precioStr);
                    } catch (NumberFormatException e) {
                        errores.add("Línea " + (linea + 1) + ": Precio inválido '" + precioStr + "'");
                        continue;
                    }
                    
                    try {
                        stock = Integer.parseInt(stockStr);
                    } catch (NumberFormatException e) {
                        errores.add("Línea " + (linea + 1) + ": Stock inválido '" + stockStr + "'");
                        continue;
                    }
                    
                    // Validar valores
                    if (precio < 0) {
                        errores.add("Línea " + (linea + 1) + ": El precio no puede ser negativo");
                        continue;
                    }
                    
                    if (stock < 0) {
                        errores.add("Línea " + (linea + 1) + ": El stock no puede ser negativo");
                        continue;
                    }
                    
                    // Crear producto
                    Producto producto = new Producto(nombre, precio, stock);
                    productosExitosos.add(producto);
                    
                } catch (Exception e) {
                    errores.add("Línea " + (linea + 1) + ": Error inesperado - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo CSV: " + e.getMessage(), e);
        }
        
        return new ResultadoCarga(productosExitosos, errores, linea);
    }
    
   
   
}