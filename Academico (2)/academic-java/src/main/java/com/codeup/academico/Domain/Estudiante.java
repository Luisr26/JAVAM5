package com.codeup.academico.Domain;

import java.util.List;

public class Estudiante {
    private final String id;
    private String nombre;
    private int edad;
    private String curso;
    private List<Nota> notas;

    // Constructor con parámetros para inicializar todos los atributos
    public Estudiante(String id, String curso, int edad, List<Nota> notas) {
        if (notas == null || notas.size() != 3) {
            throw new IllegalArgumentException("Debe proporcionar exactamente 3 notas.");
        }
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.curso = curso;
        this.notas = notas;
    }
    // Getters y setters

    public String getId() {
        return id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<Nota> getNotas() {
        return notas;
    }
    
    public void setNotas(List<Nota> notas) {
        if (notas == null || notas.size() != 3) {
            throw new IllegalArgumentException("Debe proporcionar exactamente 3 notas.");
        }
        this.notas = notas;
    }
}
