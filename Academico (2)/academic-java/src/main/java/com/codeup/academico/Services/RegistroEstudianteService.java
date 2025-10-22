
package com.codeup.academico.Services;

import com.codeup.academico.Domain.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado del registro y gestión de estudiantes
 * @author Coder
 */
public class RegistroEstudianteService {
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final CalculoService calculoService;
    
    // Constructor que recibe el servicio de cálculos (inyección de dependencias)
    public RegistroEstudianteService(CalculoService calculoService) {
        this.calculoService = calculoService;
    }
    
    /**
     * Agrega un estudiante a la lista
     * @param estudiante El estudiante a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     */
    public boolean agregarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        
        try {
            // Verificar que no exista un estudiante con el mismo ID
            if (buscarEstudiantePorId(estudiante.getId()) != null) {
                return false; // Ya existe un estudiante con ese ID
            }
            
            estudiantes.add(estudiante);
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar estudiante: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene la lista de todos los estudiantes
     * @return Lista de estudiantes (copia para evitar modificaciones externas)
     */
    public List<Estudiante> listarEstudiantes() {
        return new ArrayList<>(estudiantes);
    }
    
    /**
     * Busca un estudiante por su ID
     * @param id El ID del estudiante a buscar
     * @return El estudiante encontrado o null si no existe
     */
    public Estudiante buscarEstudiantePorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        for (Estudiante estudiante : estudiantes) {
            if (id.equals(estudiante.getId())) {
                return estudiante;
            }
        }
        return null;
    }
    
    /**
     * Calcula el promedio general de todos los estudiantes
     * @return El promedio general
     */
    public double calcularPromedioGeneral() {
        return calculoService.calcularPromedioGeneral(estudiantes);
    }
    
    /**
     * Obtiene estadísticas de los estudiantes
     * @return String con estadísticas detalladas
     */
    public String obtenerEstadisticas() {
        if (estudiantes.isEmpty()) {
            return "No hay estudiantes registrados.";
        }
        
        int totalEstudiantes = estudiantes.size();
        int aprobados = 0;
        int reprobados = 0;
        double promedioGeneral = calcularPromedioGeneral();
        
        for (Estudiante estudiante : estudiantes) {
            if (calculoService.estudianteAprobo(estudiante)) {
                aprobados++;
            } else {
                reprobados++;
            }
        }
        
        return String.format(
            "=== ESTADÍSTICAS ACADÉMICAS ===\n" +
            "Total de estudiantes: %d\n" +
            "Estudiantes aprobados: %d\n" +
            "Estudiantes reprobados: %d\n" +
            "Promedio general: %.2f\n" +
            "Porcentaje de aprobación: %.1f%%",
            totalEstudiantes, aprobados, reprobados, promedioGeneral,
            (double) aprobados / totalEstudiantes * 100
        );
    }
    
    /**
     * Obtiene el número total de estudiantes registrados
     * @return Número de estudiantes
     */
    public int getTotalEstudiantes() {
        return estudiantes.size();
    }
}
