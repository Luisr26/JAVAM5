package com.codeup.academico.UI;

import com.codeup.academico.Domain.Estudiante;
import com.codeup.academico.Domain.Nota;
import com.codeup.academico.Services.RegistroEstudianteService;
import com.codeup.academico.Services.CalculoService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Interfaz gráfica principal del Sistema Académico
 * @author Coder
 */
public class AcademicSystemFrame extends JFrame {
    private final RegistroEstudianteService registroService;
    private final CalculoService calculoService;
    
    // Componentes principales
    private JTabbedPane tabbedPane;
    private JTable estudiantesTable;
    private DefaultTableModel tableModel;
    
    // Panel de registro
    private JTextField txtId, txtCurso, txtNombre, txtEdad;
    private JTextField txtNota1, txtNota2, txtNota3;
    private JButton btnRegistrar, btnLimpiar;
    
    // Panel de búsqueda
    private JTextField txtBuscarId;
    private JButton btnBuscar;
    private JTextArea txtResultado;
    
    // Panel de estadísticas
    private JTextArea txtEstadisticas;
    private JButton btnActualizarEstadisticas;
    
    public AcademicSystemFrame(RegistroEstudianteService registroService, CalculoService calculoService) {
        this.registroService = registroService;
        this.calculoService = calculoService;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadInitialData();
    }
    
    private void initializeComponents() {
        // Configuración de la ventana principal
        setTitle("Sistema Académico - Gestión de Estudiantes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Inicializar componentes
        tabbedPane = new JTabbedPane();
        estudiantesTable = new JTable();
        tableModel = new DefaultTableModel();
        
        // Configurar tabla
        String[] columnNames = {"ID", "Curso","Nombre", "Edad", "Nota 1", "Nota 2", "Nota 3", "Promedio", "Estado"};
        tableModel.setColumnIdentifiers(columnNames);
        estudiantesTable.setModel(tableModel);
        estudiantesTable.setRowHeight(25);
        estudiantesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        estudiantesTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Hacer la tabla no editable
        estudiantesTable.setDefaultEditor(Object.class, null);
        
        // Inicializar campos de texto
        txtId = new JTextField(15);
        txtCurso = new JTextField(15);
        txtNombre = new JTextField(15);
        txtEdad = new JTextField(15);
        txtNota1 = new JTextField(15);
        txtNota2 = new JTextField(15);
        txtNota3 = new JTextField(15);
        txtBuscarId = new JTextField(15);
        
        // Inicializar botones
        btnRegistrar = new JButton("Registrar Estudiante");
        btnLimpiar = new JButton("Limpiar Formulario");
        btnBuscar = new JButton("Buscar");
        btnActualizarEstadisticas = new JButton("Actualizar Estadísticas");
        
        // Inicializar áreas de texto
        txtResultado = new JTextArea(10, 50);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        txtEstadisticas = new JTextArea(15, 50);
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Crear paneles
        JPanel panelRegistro = createRegistroPanel();
        JPanel panelLista = createListaPanel();
        JPanel panelBusqueda = createBusquedaPanel();
        JPanel panelEstadisticas = createEstadisticasPanel();
        
        // Agregar pestañas
        tabbedPane.addTab("Registrar", panelRegistro);
        tabbedPane.addTab("Lista de Estudiantes", panelLista);
        tabbedPane.addTab("Buscar Estudiante", panelBusqueda);
        tabbedPane.addTab("Estadísticas", panelEstadisticas);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.add(new JLabel("Sistema Académico - Gestión de Estudiantes"));
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createRegistroPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Nuevo Estudiante"));
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID del Estudiante:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);
        
        // Curso
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCurso, gbc);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNombre, gbc);
        
        // Edad
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEdad, gbc);
        
        // Notas
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Nota 1 (0.0-5.0):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNota1, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Nota 2 (0.0-5.0):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNota2, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Nota 3 (0.0-5.0):"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNota3, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnLimpiar);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createListaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Estudiantes Registrados"));
        
        JScrollPane scrollPane = new JScrollPane(estudiantesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBusquedaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Estudiante por ID"));
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("ID del Estudiante:"));
        searchPanel.add(txtBuscarId);
        searchPanel.add(btnBuscar);
        
        // Panel de resultados
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultado de la Búsqueda"));
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEstadisticasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Estadísticas Académicas"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnActualizarEstadisticas);
        
        JScrollPane scrollPane = new JScrollPane(txtEstadisticas);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventListeners() {
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEstudiante();
            }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEstudiante();
            }
        });
        
        btnActualizarEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEstadisticas();
            }
        });
    }
    
    private void registrarEstudiante() {
        try {
            // Validar campos
            if (txtId.getText().trim().isEmpty() || 
                txtNombre.getText().trim().isEmpty() || 
                txtEdad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Obtener datos
            String id = txtId.getText().trim();
            String curso = txtCurso.getText().trim();
            String nombre = txtNombre.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            
            // Validar y obtener notas
            double nota1 = Double.parseDouble(txtNota1.getText().trim());
            double nota2 = Double.parseDouble(txtNota2.getText().trim());
            double nota3 = Double.parseDouble(txtNota3.getText().trim());
            
            // Crear notas
            List<Nota> notas = List.of(
                new Nota(nota1),
                new Nota(nota2),
                new Nota(nota3)
            );
            
            // Crear estudiante
            Estudiante estudiante = new Estudiante(id, curso, edad, notas);
            
            // Registrar estudiante
            if (registroService.agregarEstudiante(estudiante)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Estudiante registrado exitosamente!\n" +
                    "Promedio: " + String.format("%.2f", calculoService.calcularPromedioEstudiante(estudiante)) + "\n" +
                    "Estado: " + calculoService.obtenerEstadoAcademico(estudiante),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                limpiarFormulario();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: Ya existe un estudiante con ese ID.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: Ingrese valores numéricos válidos para edad y notas.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
        txtNota1.setText("");
        txtNota2.setText("");
        txtNota3.setText("");
    }
    
    private void buscarEstudiante() {
        String id = txtBuscarId.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese un ID para buscar.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Estudiante estudiante = registroService.buscarEstudiantePorId(id);
        
        if (estudiante != null) {
            List<Nota> notas = estudiante.getNotas();
            double promedio = calculoService.calcularPromedioEstudiante(estudiante);
            String estado = calculoService.obtenerEstadoAcademico(estudiante);
            
            String resultado = String.format(
                "=== ESTUDIANTE ENCONTRADO ===\n" +
                "ID: %s\n" +
                "Nombre: %s\n" +
                "Edad: %d\n" +
                "Nota 1: %.2f\n" +
                "Nota 2: %.2f\n" +
                "Nota 3: %.2f\n" +
                "Promedio: %.2f\n" +
                "Estado: %s\n",
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getEdad(),
                notas.get(0).getValor(),
                notas.get(1).getValor(),
                notas.get(2).getValor(),
                promedio,
                estado
            );
            
            txtResultado.setText(resultado);
        } else {
            txtResultado.setText("❌ No se encontró un estudiante con el ID: " + id);
        }
    }
    
    private void actualizarEstadisticas() {
        String estadisticas = registroService.obtenerEstadisticas();
        txtEstadisticas.setText(estadisticas);
    }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0); // Limpiar tabla
        
        List<Estudiante> estudiantes = registroService.listarEstudiantes();
        
        for (Estudiante estudiante : estudiantes) {
            List<Nota> notas = estudiante.getNotas();
            double promedio = calculoService.calcularPromedioEstudiante(estudiante);
            String estado = calculoService.obtenerEstadoAcademico(estudiante);
            
            Object[] row = {
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getEdad(),
                String.format("%.2f", notas.get(0).getValor()),
                String.format("%.2f", notas.get(1).getValor()),
                String.format("%.2f", notas.get(2).getValor()),
                String.format("%.2f", promedio),
                estado
            };
            
            tableModel.addRow(row);
        }
    }
    
    private void loadInitialData() {
        actualizarTabla();
        actualizarEstadisticas();
    }
    
    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
