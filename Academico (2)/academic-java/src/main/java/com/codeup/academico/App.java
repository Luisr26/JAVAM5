package com.codeup.academico;

import com.codeup.academico.Services.CalculoService;
import com.codeup.academico.Services.RegistroEstudianteService;
import com.codeup.academico.UI.AcademicSystemFrame;
import javax.swing.SwingUtilities;

/**
 * Clase principal que conecta todas las capas de la aplicación
 * Demuestra el uso de la arquitectura por capas con inyección de dependencias
 * 
 * @author Coder
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema Académico...");
        
        // === CONFIGURACIÓN DE LA ARQUITECTURA POR CAPAS ===
        
        // 1. Crear instancias de los servicios (Capa Services)
        CalculoService calculoService = new CalculoService();
        RegistroEstudianteService registroService = new RegistroEstudianteService(calculoService);
        
        // 2. Crear la interfaz gráfica inyectando las dependencias (Capa UI)
        AcademicSystemFrame ui = new AcademicSystemFrame(registroService, calculoService);
        
        // 3. Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                ui.iniciar();
                System.out.println("✅ Sistema Académico iniciado correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
