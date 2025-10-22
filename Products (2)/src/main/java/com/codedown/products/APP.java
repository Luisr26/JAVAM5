package com.codedown.products;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Aplicación principal del sistema de gestión de productos
 * 
 * @author Luis Orozco
 * @version 1.0
 * @since 2024
 */
public class APP {
    // Mismo código base que tu APP original
    private static ArrayList<Producto> inventario = new ArrayList<>();
    private static HashMap<String, Integer> stockProductos = new HashMap<>();
    private static double[] precios = new double[0];
    
    // MÉTODOS HELPER PARA DIÁLOGOS CUADRADOS
    
    // Método para mostrar mensajes en formato cuadrado
    private static void mostrarMensajeCuadrado(String mensaje, String titulo) {
        JTextArea textArea = new JTextArea(mensaje);
        textArea.setColumns(30); // Controla ancho
        textArea.setRows(12);    // Controla alto
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(null, textArea, titulo, 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Método para input cuadrado
    private static String mostrarInputCuadrado(String mensaje, String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 120));
        
        JLabel label = new JLabel("<html><div style='width:300px; padding:10px;'>" + 
            mensaje + "</div></html>");
        label.setHorizontalAlignment(JLabel.CENTER);
        
        JTextField textField = new JTextField(20);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20),
            textField.getBorder()));
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(textField, BorderLayout.SOUTH);
        
        int result = JOptionPane.showConfirmDialog(null, panel, titulo, 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        }
        return null;
    }
    
    // Método para opciones cuadradas
    private static int mostrarOpcionesCuadrado(String mensaje, String titulo, String[] opciones) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(380, 180));
        
        JLabel label = new JLabel("<html><div style='width:340px; text-align:center; padding:20px;'>" + 
            mensaje + "</div></html>");
        label.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(label, BorderLayout.CENTER);
        
        return JOptionPane.showOptionDialog(null, panel, titulo,
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opciones, opciones[0]);
    }
    
    public static void main(String[] args) {
        boolean continuar = true;
        while (continuar) {
            continuar = mostrarMenuPrincipal();
        }
        
        mostrarMensajeCuadrado("¡Gracias por usar el sistema de inventario!", "Despedida");
    }
    
    private static boolean mostrarMenuPrincipal() {
        String[] opciones = {
            "Ver Inventario",
            "Agregar Alimento",
            "Agregar Electrodoméstico", 
            "Modificar Producto",
            "Eliminar Producto",
            "Gestionar Stock",
            "Ver Stock",
            "Sincronizar Precios",
            "Reportes",
            "Salir"
        };
        
        int opcion = mostrarOpcionesCuadrado(
            "SISTEMA DE INVENTARIO AVANZADO\n\nSeleccione una opción:",
            "Gestión de Inventario",
            opciones
        );
        
        switch (opcion) {
            case 0: verInventarioCompleto(); break;
            case 1: agregarAlimento(); break;
            case 2: agregarElectrodomestico(); break;
            case 3: modificarProducto(); break;
            case 4: eliminarProducto(); break;
            case 5: gestionarStock(); break;
            case 6: verStockPorProducto(); break;
            case 7: sincronizarPrecios(); break;
            case 8: mostrarReportes(); break;
            case 9:
            case -1: return false;
        }
        
        return true;
    }
    
    private static void verInventarioCompleto() {
        if (inventario.isEmpty()) {
            mostrarMensajeCuadrado("El inventario está vacío.", "Inventario");
            return;
        }
        
        int itemsPorPagina = 5;
        int totalPaginas = (int) Math.ceil((double) inventario.size() / itemsPorPagina);
        int paginaActual = 0;
        
        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("INVENTARIO COMPLETO\n");
            sb.append("Página ").append(paginaActual + 1).append(" de ").append(totalPaginas).append("\n");
            sb.append("═══════════════════════\n\n");
            
            int inicio = paginaActual * itemsPorPagina;
            int fin = Math.min(inicio + itemsPorPagina, inventario.size());
            
            for (int i = inicio; i < fin; i++) {
                Producto producto = inventario.get(i);
                String nombreProducto = producto.getNombre();
                int stock = stockProductos.getOrDefault(nombreProducto, 0);
                
                // Truncar nombres largos para mantener formato cuadrado
                String nombreCorto = nombreProducto.length() > 20 ? 
                    nombreProducto.substring(0, 17) + "..." : nombreProducto;
                
                sb.append(String.format("%-2d. %-20s\n", i + 1, nombreCorto));
                sb.append(String.format("    $%-8.2f | Stock: %-3d\n", precios[i], stock));
                if (i < fin - 1) sb.append("\n");
            }
            
            String[] opciones;
            if (totalPaginas == 1) {
                opciones = new String[]{"Cerrar"};
            } else if (paginaActual == 0) {
                opciones = new String[]{"Siguiente →", "Cerrar"};
            } else if (paginaActual == totalPaginas - 1) {
                opciones = new String[]{"← Anterior", "Cerrar"};
            } else {
                opciones = new String[]{"← Anterior", "Siguiente →", "Cerrar"};
            }
            
            int opcion = mostrarOpcionesCuadrado(sb.toString(), "Inventario Completo", opciones);
            
            if (opcion == -1 || (totalPaginas == 1 && opcion == 0) || 
                (opciones.length == 2 && opcion == 1) ||
                (opciones.length == 3 && opcion == 2)) {
                break; // Cerrar
            } else if (opcion < opciones.length && opciones[opcion].contains("Siguiente")) {
                paginaActual++;
            } else if (opcion < opciones.length && opciones[opcion].contains("Anterior")) {
                paginaActual--;
            }
        }
    }
    
    private static void agregarAlimento() {
        try {
            String nombre = mostrarInputCuadrado("Ingrese el nombre del alimento:", "Nuevo Alimento");
            if (nombre == null || nombre.trim().isEmpty()) return;
            nombre = nombre.trim();
            
            String precioStr = mostrarInputCuadrado("Ingrese el precio del alimento:", "Precio");
            if (precioStr == null) return;
            double precio = Double.parseDouble(precioStr);
            
            String fechaVencimiento = mostrarInputCuadrado("Fecha de vencimiento (YYYY-MM-DD):", "Vencimiento");
            if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) return;
            
            String categoria = mostrarInputCuadrado("Categoría del alimento:", "Categoría");
            if (categoria == null || categoria.trim().isEmpty()) return;
            
            String stockStr = mostrarInputCuadrado("Stock inicial:", "Stock");
            if (stockStr == null) return;
            int stock = Integer.parseInt(stockStr);
            
            Alimento nuevoAlimento = new Alimento(nombre, precio, fechaVencimiento.trim(), categoria.trim());
            agregarProductoConStock(nuevoAlimento, stock);
            
            String mensaje = String.format("✓ ALIMENTO AGREGADO\n\n" +
                "Nombre: %s\n" +
                "Precio: $%.2f\n" +
                "Categoría: %s\n" +
                "Vencimiento: %s\n" +
                "Stock inicial: %d unidades", 
                nombre, precio, categoria, fechaVencimiento, stock);
            
            mostrarMensajeCuadrado(mensaje, "Éxito");
            
        } catch (NumberFormatException e) {
            mostrarMensajeCuadrado("Error: Ingrese valores numéricos válidos.", "Error");
        }
    }
    
    private static void agregarElectrodomestico() {
        try {
            String nombre = mostrarInputCuadrado("Nombre del electrodoméstico:", "Nuevo Electrodoméstico");
            if (nombre == null || nombre.trim().isEmpty()) return;
            nombre = nombre.trim();
            
            String precioStr = mostrarInputCuadrado("Precio del electrodoméstico:", "Precio");
            if (precioStr == null) return;
            double precio = Double.parseDouble(precioStr);
            
            String marca = mostrarInputCuadrado("Marca del electrodoméstico:", "Marca");
            if (marca == null || marca.trim().isEmpty()) return;
            
            String garantiaStr = mostrarInputCuadrado("Garantía en meses:", "Garantía");
            if (garantiaStr == null) return;
            int garantia = Integer.parseInt(garantiaStr);
            
            String consumoStr = mostrarInputCuadrado("Consumo energético (watts):", "Consumo");
            if (consumoStr == null) return;
            double consumo = Double.parseDouble(consumoStr);
            
            String stockStr = mostrarInputCuadrado("Stock inicial:", "Stock");
            if (stockStr == null) return;
            int stock = Integer.parseInt(stockStr);
            
            Electrodomestico nuevoElectro = new Electrodomestico(nombre, precio, marca.trim(), garantia, consumo);
            agregarProductoConStock(nuevoElectro, stock);
            
            String mensaje = String.format("✓ ELECTRODOMÉSTICO AGREGADO\n\n" +
                "Nombre: %s\n" +
                "Marca: %s\n" +
                "Precio: $%.2f\n" +
                "Garantía: %d meses\n" +
                "Consumo: %.1fW\n" +
                "Stock inicial: %d unidades", 
                nombre, marca, precio, garantia, consumo, stock);
            
            mostrarMensajeCuadrado(mensaje, "Éxito");
            
        } catch (NumberFormatException e) {
            mostrarMensajeCuadrado("Error: Ingrese valores numéricos válidos.", "Error");
        }
    }
    
    private static void agregarProductoConStock(Producto producto, int stock) {
        inventario.add(producto);
        stockProductos.put(producto.getNombre(), stock);
        
        // Expandir array de precios
        double[] nuevosPrecios = new double[inventario.size()];
        System.arraycopy(precios, 0, nuevosPrecios, 0, precios.length);
        nuevosPrecios[inventario.size() - 1] = producto.getPrecio();
        precios = nuevosPrecios;
    }
    
    private static void verStockPorProducto() {
        if (stockProductos.isEmpty()) {
            mostrarMensajeCuadrado("No hay productos en stock.", "Stock");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("STOCK POR PRODUCTO\n");
        sb.append("═══════════════════\n\n");
        
        int contador = 0;
        for (Map.Entry<String, Integer> entry : stockProductos.entrySet()) {
            if (contador >= 8) {
                sb.append(String.format("\n... y %d productos más", stockProductos.size() - contador));
                break;
            }
            
            String nombre = entry.getKey();
            Integer stock = entry.getValue();
            
            String nombreCorto = nombre.length() > 18 ? nombre.substring(0, 15) + "..." : nombre;
            
            String estado = "";
            if (stock == 0) {
                estado = " (Sin stock)";
            } else if (stock < 5) {
                estado = " (Bajo)";
            }
            
            sb.append(String.format("%-18s: %3d%s\n", nombreCorto, stock, estado));
            contador++;
        }
        
        mostrarMensajeCuadrado(sb.toString(), "Stock de Productos");
    }
    
    private static void sincronizarPrecios() {
        if (inventario.isEmpty()) {
            mostrarMensajeCuadrado("El inventario está vacío.", "Sincronización");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("SINCRONIZACIÓN DE PRECIOS\n");
        sb.append("═══════════════════════════\n\n");
        
        boolean hayDiferencias = false;
        int diferenciasEncontradas = 0;
        
        for (int i = 0; i < inventario.size(); i++) {
            Producto producto = inventario.get(i);
            double precioProducto = producto.getPrecio();
            double precioArray = precios[i];
            
            if (Math.abs(precioProducto - precioArray) > 0.001) {
                if (diferenciasEncontradas == 0) {
                    sb.append("Diferencias encontradas:\n\n");
                }
                if (diferenciasEncontradas < 3) {
                    String nombreCorto = producto.getNombre().length() > 15 ? 
                        producto.getNombre().substring(0, 12) + "..." : producto.getNombre();
                    sb.append(String.format("%-15s:\n", nombreCorto));
                    sb.append(String.format("  Objeto: $%.2f\n", precioProducto));
                    sb.append(String.format("  Array:  $%.2f\n\n", precioArray));
                }
                diferenciasEncontradas++;
                hayDiferencias = true;
                
                precios[i] = precioProducto;
            }
        }
        
        if (!hayDiferencias) {
            sb.append("✓ Precios sincronizados\n\n");
            sb.append("No se encontraron diferencias.");
        } else {
            if (diferenciasEncontradas > 3) {
                sb.append(String.format("... y %d diferencias más\n\n", diferenciasEncontradas - 3));
            }
            sb.append("✓ Sincronización completada\n\n");
            sb.append(String.format("Total corregidas: %d", diferenciasEncontradas));
        }
        
        mostrarMensajeCuadrado(sb.toString(), "Sincronización de Precios");
    }
    
    // Resto de métodos con el mismo patrón...
    private static void modificarProducto() {
        // Implementación similar usando mostrarMensajeCuadrado y mostrarInputCuadrado
    }
    
    private static void eliminarProducto() {
        // Implementación similar usando mostrarMensajeCuadrado y mostrarOpcionesCuadrado
    }
    
    private static void gestionarStock() {
        // Implementación similar usando los métodos cuadrados
    }
    
    private static void mostrarReportes() {
        // Implementación similar usando mostrarMensajeCuadrado
    }
}