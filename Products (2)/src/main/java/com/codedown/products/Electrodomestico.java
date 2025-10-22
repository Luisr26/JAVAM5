package com.codedown.products;

/**
 * Subclase Electrodoméstico - Representa productos electrodomésticos
 * 
 * @author Luis Orozco
 * @version 1.0
 * @since 2024
 */
public class Electrodomestico extends Producto {
    // Atributos específicos de Electrodoméstico
    private String marca;
    private int garantiaMeses;
    private double consumoEnergetico; // en watts
    
    // Constructor
    public Electrodomestico(String nombre, double precio, String marca, int garantiaMeses, double consumoEnergetico) {
        super(nombre, precio); // Llamada al constructor de la clase padre
        this.marca = marca;
        this.garantiaMeses = garantiaMeses;
        this.consumoEnergetico = consumoEnergetico;
    }
    
    // Getters y Setters específicos
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public int getGarantiaMeses() {
        return garantiaMeses;
    }
    
    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }
    
    public double getConsumoEnergetico() {
        return consumoEnergetico;
    }
    
    public void setConsumoEnergetico(double consumoEnergetico) {
        this.consumoEnergetico = consumoEnergetico;
    }
    
    // Implementación del método abstracto (polimorfismo)
    @Override
    public String getDescripcion() {
        return "ELECTRODOMÉSTICO: " + getNombre() + 
               " | Marca: " + marca + 
               " | Garantía: " + garantiaMeses + " meses" +
               " | Consumo: " + consumoEnergetico + "W" +
               " | Precio: $" + String.format("%.2f", getPrecio());
    }
}