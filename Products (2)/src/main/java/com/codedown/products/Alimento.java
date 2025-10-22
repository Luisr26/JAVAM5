package com.codedown.products;

/**
 * Subclase Alimento - Representa productos alimenticios
 * 
 * @author Luis Orozco
 * @version 1.0
 * @since 2024
 */
public class Alimento extends Producto {
    // Atributos específicos de Alimento
    private String fechaVencimiento;
    private String categoria; // lácteos, carnes, verduras, etc.
    
    // Constructor
    public Alimento(String nombre, double precio, String fechaVencimiento, String categoria) {
        super(nombre, precio); // Llamada al constructor de la clase padre
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
    }
    
    // Getters y Setters específicos
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    // Implementación del método abstracto (polimorfismo)
    @Override
    public String getDescripcion() {
        return "ALIMENTO: " + getNombre() + 
               " | Categoría: " + categoria + 
               " | Vence: " + fechaVencimiento + 
               " | Precio: $" + String.format("%.2f", getPrecio());
    }
}