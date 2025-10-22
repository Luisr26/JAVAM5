package com.codeup.academico.Services;

import com.codeup.academico.Domain.Estudiante;
import com.codeup.academico.Domain.Nota;
import java.util.List;

/**
 * Servicio encargado de realizar cálculos académicos
 * @author Coder
 */
public class CalculoService {
    
    /**
     * Calcula el promedio de un estudiante basado en sus 3 notas
     * @param estudiante El estudiante del cual calcular el promedio
     * @return El promedio del estudiante
     */
    public double calcularPromedioEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getNotas() == null) {
            throw new IllegalArgumentException("El estudiante y sus notas no pueden ser nulos");
        }
        
        List<Nota> notas = estudiante.getNotas();
        if (notas.size() != 3) {
            throw new IllegalArgumentException("El estudiante debe tener exactamente 3 notas");
        }
        
        double suma = 0.0;
        for (Nota nota : notas) {
            suma += nota.getValor();
        }
        
        return suma / notas.size();
    }
    
    /**
     * Calcula el promedio general de todos los estudiantes
     * @param estudiantes Lista de estudiantes
     * @return El promedio general de todos los estudiantes
     */
    public double calcularPromedioGeneral(List<Estudiante> estudiantes) {
        if (estudiantes == null || estudiantes.isEmpty()) {
            return 0.0;
        }
        
        double sumaTotal = 0.0;
        int totalNotas = 0;
        
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNotas() != null && estudiante.getNotas().size() == 3) {
                for (Nota nota : estudiante.getNotas()) {
                    sumaTotal += nota.getValor();
                    totalNotas++;
                }
            }
        }
        
        return totalNotas > 0 ? sumaTotal / totalNotas : 0.0;
    }
    
    public boolean estudianteAprobo(Estudiante estudiante) {
        double promedio = calcularPromedioEstudiante(estudiante);
        return promedio >= 3.0;
    }
    
    public String obtenerEstadoAcademico(Estudiante estudiante) {
        return estudianteAprobo(estudiante) ? "Aprobado" : "Reprobado";
    }
}
