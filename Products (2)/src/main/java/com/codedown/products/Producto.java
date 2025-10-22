package com.codedown.products;

/**
 * Clase abstracta Producto - Base para todos los productos del sistema
 * 
 * @author Luis Orozco
 * @version 1.0
 * @since 2024
 */
public abstract class Producto {
    // Atributos privados (encapsulamiento)
    private String nombre;
    private double precio;
    
    // Constructor
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    // Getters y Setters (encapsulamiento)
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    // Método abstracto que debe ser implementado por las subclases (abstracción)
    public abstract String getDescripcion();
    
    // Método toString para mostrar información básica
    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio);
    }
}