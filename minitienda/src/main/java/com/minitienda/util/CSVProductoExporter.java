package com.minitienda.util;

import com.minitienda.modelo.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Clase para exportar productos a archivos CSV
 */
public class CSVProductoExporter {
    
    /**
     * Resultado de la exportación
     */
    public static class ResultadoExportacion {
        private final int registrosExportados;
        private final File archivoGenerado;
        private final boolean exitoso;
        private final String mensaje;
        
        public ResultadoExportacion(int registrosExportados, File archivoGenerado, 
                                   boolean exitoso, String mensaje) {
            this.registrosExportados = registrosExportados;
            this.archivoGenerado = archivoGenerado;
            this.exitoso = exitoso;
            this.mensaje = mensaje;
        }
        
        public int getRegistrosExportados() {
            return registrosExportados;
        }
        
        public File getArchivoGenerado() {
            return archivoGenerado;
        }
        
        public boolean isExitoso() {
            return exitoso;
        }
        
        public String getMensaje() {
            return mensaje;
        }
    }
    
    /**
     * Exporta una lista de productos a un archivo CSV con ID
     */
    public static ResultadoExportacion exportarACSV(List<Producto> productos, File archivo) 
            throws IOException {
        
        if (productos == null || productos.isEmpty()) {
            return new ResultadoExportacion(0, archivo, false, 
                "No hay productos para exportar");
        }
        
        try (FileWriter writer = new FileWriter(archivo);
             CSVPrinter csvPrinter = new CSVPrinter(writer, 
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader("id", "nombre", "precio", "stock")
                             .build())) {
            
            for (Producto producto : productos) {
                csvPrinter.printRecord(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock()
                );
            }
            
            csvPrinter.flush();
            
            return new ResultadoExportacion(
                productos.size(), 
                archivo, 
                true, 
                "Exportación exitosa de " + productos.size() + " productos"
            );
            
        } catch (IOException e) {
            throw new IOException("Error al exportar archivo CSV: " + e.getMessage(), e);
        }
    }
    
    /**
     * Exporta productos sin el ID (útil para importar en otros sistemas)
     */
    public static ResultadoExportacion exportarSinId(List<Producto> productos, File archivo) 
            throws IOException {
        
        if (productos == null || productos.isEmpty()) {
            return new ResultadoExportacion(0, archivo, false, 
                "No hay productos para exportar");
        }
        
        try (FileWriter writer = new FileWriter(archivo);
             CSVPrinter csvPrinter = new CSVPrinter(writer, 
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader("nombre", "precio", "stock")
                             .build())) {
            
            for (Producto producto : productos) {
                csvPrinter.printRecord(
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock()
                );
            }
            
            csvPrinter.flush();
            
            return new ResultadoExportacion(
                productos.size(), 
                archivo, 
                true, 
                "Exportación exitosa de " + productos.size() + " productos"
            );
            
        } catch (IOException e) {
            throw new IOException("Error al exportar archivo CSV: " + e.getMessage(), e);
        }
    }
}