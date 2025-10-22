package com.codeup.academico.UI;

import com.codeup.academico.Domain.Estudiante;
import com.codeup.academico.Services.RegistroEstudianteService;
import com.codeup.academico.Services.CalculoService;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario para el registro de estudiantes
 * @author Coder
 */
public class RegistroEstudianteFrame {
    private final RegistroEstudianteService registroService;
    private final CalculoService calculoService;
    private final Scanner scanner;
    
    // Constructor que recibe los servicios (inyección de dependencias)
    public RegistroEstudianteFrame(RegistroEstudianteService registroService, CalculoService calculoService) {
        this.registroService = registroService;
        this.calculoService = calculoService;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Inicia la interfaz de usuario
     */
    public void iniciar() {
        System.out.println("=== SISTEMA DE REGISTRO ACADÉMICO ===");
        mostrarMenu();
    }
    
    /**
     * Muestra el menú principal y maneja las opciones
     */
    private void mostrarMenu() {
        int opcion;
        
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Listar estudiantes");
            System.out.println("3. Buscar estudiante por ID");
            System.out.println("4. Calcular promedio de estudiante");
            System.out.println("5. Mostrar estadísticas generales");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        registrarEstudiante();
                        break;
                    case 2:
                        listarEstudiantes();
                        break;
                    case 3:
                        buscarEstudiante();
                        break;
                    case 4:
                        calcularPromedioEstudiante();
                        break;
                    case 5:
                        mostrarEstadisticas();
                        break;
                    case 6:
                        System.out.println("¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                opcion = 0;
            }
        } while (opcion != 6);
    }
    
    /**
     * Registra un nuevo estudiante
     */
    private void registrarEstudiante() {
        System.out.println("\n--- REGISTRAR ESTUDIANTE ---");
        
        try {
            System.out.print("ID del estudiante: ");
            String id = scanner.nextLine();
            
            System.out.print("Nombre del estudiante: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Edad del estudiante: ");
            int edad = Integer.parseInt(scanner.nextLine());
            
            System.out.println("Ingrese las 3 notas (0.0 - 5.0):");
            List<com.codeup.academico.Domain.Nota> notas = new java.util.ArrayList<>();
            
            for (int i = 1; i <= 3; i++) {
                System.out.print("Nota " + i + ": ");
                double valorNota = Double.parseDouble(scanner.nextLine());
                notas.add(new com.codeup.academico.Domain.Nota(valorNota));
            }
            
            Estudiante estudiante = new Estudiante(id, nombre, edad, notas);
            
            if (registroService.agregarEstudiante(estudiante)) {
                System.out.println("✅ Estudiante registrado exitosamente!");
                double promedio = calculoService.calcularPromedioEstudiante(estudiante);
                System.out.printf("Promedio del estudiante: %.2f\n", promedio);
                System.out.println("Estado: " + calculoService.obtenerEstadoAcademico(estudiante));
            } else {
                System.out.println("❌ Error: Ya existe un estudiante con ese ID.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos los estudiantes registrados
     */
    private void listarEstudiantes() {
        System.out.println("\n--- LISTA DE ESTUDIANTES ---");
        
        List<Estudiante> estudiantes = registroService.listarEstudiantes();
        
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-8s %-12s %-12s %-10s\n", 
                         "ID", "Nombre", "Edad", "Nota 1", "Nota 2", "Nota 3");
        System.out.println("-".repeat(80));
        
        for (Estudiante estudiante : estudiantes) {
            List<com.codeup.academico.Domain.Nota> notas = estudiante.getNotas();
            System.out.printf("%-10s %-20s %-8d %-12.2f %-12.2f %-10.2f\n",
                             estudiante.getId(),
                             estudiante.getNombre(),
                             estudiante.getEdad(),
                             notas.get(0).getValor(),
                             notas.get(1).getValor(),
                             notas.get(2).getValor());
        }
    }
    
    /**
     * Busca un estudiante por ID
     */
    private void buscarEstudiante() {
        System.out.println("\n--- BUSCAR ESTUDIANTE ---");
        System.out.print("Ingrese el ID del estudiante: ");
        String id = scanner.nextLine();
        
        Estudiante estudiante = registroService.buscarEstudiantePorId(id);
        
        if (estudiante != null) {
            System.out.println("\n✅ Estudiante encontrado:");
            System.out.println("ID: " + estudiante.getId());
            System.out.println("Nombre: " + estudiante.getNombre());
            System.out.println("Edad: " + estudiante.getEdad());
            
            List<com.codeup.academico.Domain.Nota> notas = estudiante.getNotas();
            System.out.println("Notas: " + notas.get(0).getValor() + ", " + 
                             notas.get(1).getValor() + ", " + notas.get(2).getValor());
            
            double promedio = calculoService.calcularPromedioEstudiante(estudiante);
            System.out.printf("Promedio: %.2f\n", promedio);
            System.out.println("Estado: " + calculoService.obtenerEstadoAcademico(estudiante));
        } else {
            System.out.println("❌ No se encontró un estudiante con ese ID.");
        }
    }
    
    /**
     * Calcula el promedio de un estudiante específico
     */
    private void calcularPromedioEstudiante() {
        System.out.println("\n--- CALCULAR PROMEDIO DE ESTUDIANTE ---");
        System.out.print("Ingrese el ID del estudiante: ");
        String id = scanner.nextLine();
        
        Estudiante estudiante = registroService.buscarEstudiantePorId(id);
        
        if (estudiante != null) {
            double promedio = calculoService.calcularPromedioEstudiante(estudiante);
            System.out.printf("Promedio del estudiante %s: %.2f\n", 
                             estudiante.getNombre(), promedio);
            System.out.println("Estado: " + calculoService.obtenerEstadoAcademico(estudiante));
        } else {
            System.out.println("❌ No se encontró un estudiante con ese ID.");
        }
    }
    
    /**
     * Muestra estadísticas generales
     */
    private void mostrarEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS GENERALES ---");
        System.out.println(registroService.obtenerEstadisticas());
    }
}
