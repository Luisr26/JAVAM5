
package com.mycompany.minitienda.ui;

import com.mycompany.minitienda.domain.Producto;
import com.mycompany.minitienda.exceptions.DatoInvalidoException;
import com.mycompany.minitienda.exceptions.DuplicadoException;
import com.mycompany.minitienda.exceptions.PersistenciaException;
import com.mycompany.minitienda.service.InventarioServiceLocal;
import com.mycompany.minitienda.service.impl.InventarioServiceImpl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;


public class MainApp extends Application {
    
    private final InventarioServiceLocal service;
    private TextArea areaResultados;
    
    // Contadores de operaciones
    private int totalAltas = 0;
    private int totalBajas = 0;
    private int totalActualizaciones = 0;
    
    public MainApp() {
        this.service = InventarioServiceImpl.getInstancia();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mini-Tienda - Sistema de Inventario");
        
        // Layout principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        
        // Panel de botones (menú)
        VBox menuPanel = crearMenuPanel();
        root.setLeft(menuPanel);
        
        // Área de resultados
        areaResultados = new TextArea();
        areaResultados.setEditable(false);
        areaResultados.setPrefWidth(500);
        areaResultados.setPrefHeight(400);
        areaResultados.setWrapText(true);
        areaResultados.setText("=== BIENVENIDO AL SISTEMA DE INVENTARIO ===\nSeleccione una opción del menú.\n");
        
        VBox resultadosBox = new VBox(10);
        resultadosBox.getChildren().addAll(new Label("Resultados:"), areaResultados);
        root.setCenter(resultadosBox);
        
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox crearMenuPanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPrefWidth(250);
        
        Label titulo = new Label("MENÚ PRINCIPAL");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button btnAgregar = new Button("1. Agregar Producto");
        Button btnListar = new Button("2. Listar Inventario");
        Button btnActualizarPrecio = new Button("3. Actualizar Precio");
        Button btnActualizarStock = new Button("4. Actualizar Stock");
        Button btnEliminar = new Button("5. Eliminar Producto");
        Button btnBuscar = new Button("6. Buscar por Nombre");
        Button btnSalir = new Button("7. Salir");
        
        // Hacer los botones del mismo ancho
        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnListar.setMaxWidth(Double.MAX_VALUE);
        btnActualizarPrecio.setMaxWidth(Double.MAX_VALUE);
        btnActualizarStock.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnBuscar.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setMaxWidth(Double.MAX_VALUE);
        
        // Eventos de los botones
        btnAgregar.setOnAction(e -> agregarProducto());
        btnListar.setOnAction(e -> listarInventario());
        btnActualizarPrecio.setOnAction(e -> actualizarPrecio());
        btnActualizarStock.setOnAction(e -> actualizarStock());
        btnEliminar.setOnAction(e -> eliminarProducto());
        btnBuscar.setOnAction(e -> buscarProducto());
        btnSalir.setOnAction(e -> salirYMostrarResumen());
        
        vbox.getChildren().addAll(
            titulo,
            new Separator(),
            btnAgregar,
            btnListar,
            btnActualizarPrecio,
            btnActualizarStock,
            btnEliminar,
            btnBuscar,
            new Separator(),
            btnSalir
        );
        
        return vbox;
    }
    
    /**
     * Opción 1: Agregar producto
     */
    private void agregarProducto() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del nuevo producto");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtNombre = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtStock = new TextField();
        
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Precio:"), 0, 1);
        grid.add(txtPrecio, 1, 1);
        grid.add(new Label("Stock:"), 0, 2);
        grid.add(txtStock, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String nombre = txtNombre.getText();
                    Double precio = Double.parseDouble(txtPrecio.getText());
                    Integer stock = Integer.parseInt(txtStock.getText());
                    
                    service.agregarProducto(nombre, precio, stock);
                    totalAltas++;
                    
                    mostrarExito("Producto agregado exitosamente:\n" + 
                               "Nombre: " + nombre + "\n" +
                               "Precio: $" + precio + "\n" +
                               "Stock: " + stock);
                    
                } catch (NumberFormatException e) {
                    mostrarError("Error: Precio y Stock deben ser valores numéricos válidos.");
                } catch (DatoInvalidoException e) {
                    mostrarError("Error de Validación: " + e.getMessage());
                } catch (DuplicadoException e) {
                    mostrarError("Error: " + e.getMessage());
                } catch (PersistenciaException e) {
                    mostrarError("Error de Base de Datos: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Opción 2: Listar inventario
     */
    private void listarInventario() {
        try {
            List<Producto> productos = service.listarInventario();
            
            if (productos.isEmpty()) {
                areaResultados.setText("=== INVENTARIO VACÍO ===\n" +
                                      "No hay productos registrados en el sistema.");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("=== LISTADO DE PRODUCTOS ===\n");
                sb.append(String.format("%-5s %-30s %-15s %-10s\n", "ID", "NOMBRE", "PRECIO", "STOCK"));
                sb.append("-".repeat(65)).append("\n");
                
                for (Producto p : productos) {
                    sb.append(String.format("%-5d %-30s $%-14.2f %-10d\n",
                            p.getId(), p.getNombre(), p.getPrecio(), p.getStock()));
                }
                
                sb.append("-".repeat(65)).append("\n");
                sb.append("Total de productos: ").append(productos.size()).append("\n");
                
                areaResultados.setText(sb.toString());
            }
            
        } catch (PersistenciaException e) {
            mostrarError("Error al listar inventario: " + e.getMessage());
        }
    }
    
    /**
     * Opción 3: Actualizar precio
     */
    private void actualizarPrecio() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar Precio");
        dialog.setHeaderText("Ingrese el ID del producto y el nuevo precio");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtId = new TextField();
        TextField txtPrecio = new TextField();
        
        grid.add(new Label("ID del Producto:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nuevo Precio:"), 0, 1);
        grid.add(txtPrecio, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Integer id = Integer.parseInt(txtId.getText());
                    Double nuevoPrecio = Double.parseDouble(txtPrecio.getText());
                    
                    service.actualizarPrecio(id, nuevoPrecio);
                    totalActualizaciones++;
                    
                    mostrarExito("Precio actualizado exitosamente.\n" +
                               "Producto ID: " + id + "\n" +
                               "Nuevo precio: $" + nuevoPrecio);
                    
                } catch (NumberFormatException e) {
                    mostrarError("Error: ID y Precio deben ser valores numéricos válidos.");
                } catch (DatoInvalidoException e) {
                    mostrarError("Error de Validación: " + e.getMessage());
                } catch (PersistenciaException e) {
                    mostrarError("Error de Base de Datos: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Opción 4: Actualizar stock
     */
    private void actualizarStock() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Ingrese el ID del producto y el nuevo stock");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField txtId = new TextField();
        TextField txtStock = new TextField();
        
        grid.add(new Label("ID del Producto:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Nuevo Stock:"), 0, 1);
        grid.add(txtStock, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Integer id = Integer.parseInt(txtId.getText());
                    Integer nuevoStock = Integer.parseInt(txtStock.getText());
                    
                    service.actualizarStock(id, nuevoStock);
                    totalActualizaciones++;
                    
                    mostrarExito("Stock actualizado exitosamente.\n" +
                               "Producto ID: " + id + "\n" +
                               "Nuevo stock: " + nuevoStock);
                    
                } catch (NumberFormatException e) {
                    mostrarError("Error: ID y Stock deben ser valores numéricos válidos.");
                } catch (DatoInvalidoException e) {
                    mostrarError("Error de Validación: " + e.getMessage());
                } catch (PersistenciaException e) {
                    mostrarError("Error de Base de Datos: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Opción 5: Eliminar producto
     */
    private void eliminarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Producto");
        dialog.setHeaderText("Eliminar producto del inventario");
        dialog.setContentText("Ingrese el ID del producto:");
        
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                Integer id = Integer.parseInt(idStr);
                
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar Eliminación");
                confirmacion.setHeaderText("¿Está seguro de eliminar el producto?");
                confirmacion.setContentText("Esta acción no se puede deshacer.");
                
                confirmacion.showAndWait().ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        try {
                            service.eliminarProducto(id);
                            totalBajas++;
                            
                            mostrarExito("Producto eliminado exitosamente.\nID: " + id);
                            
                        } catch (PersistenciaException e) {
                            mostrarError("Error al eliminar: " + e.getMessage());
                        }
                    }
                });
                
            } catch (NumberFormatException e) {
                mostrarError("Error: El ID debe ser un valor numérico válido.");
            }
        });
    }
    
    /**
     * Opción 6: Buscar producto por nombre
     */
    private void buscarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Producto");
        dialog.setHeaderText("Buscar producto por nombre");
        dialog.setContentText("Ingrese el nombre del producto:");
        
        dialog.showAndWait().ifPresent(nombre -> {
            try {
                Producto producto = service.buscarProductoPorNombre(nombre);
                
                if (producto != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("=== PRODUCTO ENCONTRADO ===\n\n");
                    sb.append("ID:     ").append(producto.getId()).append("\n");
                    sb.append("Nombre: ").append(producto.getNombre()).append("\n");
                    sb.append("Precio: $").append(producto.getPrecio()).append("\n");
                    sb.append("Stock:  ").append(producto.getStock()).append(" unidades\n");
                    
                    areaResultados.setText(sb.toString());
                } else {
                    areaResultados.setText("=== BÚSQUEDA SIN RESULTADOS ===\n" +
                                          "No se encontró ningún producto con el nombre: " + nombre);
                }
                
            } catch (PersistenciaException e) {
                mostrarError("Error al buscar producto: " + e.getMessage());
            }
        });
    }
    
    /**
     * Opción 7: Salir y mostrar resumen
     */
    private void salirYMostrarResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE OPERACIONES ===\n\n");
        resumen.append("Productos agregados:     ").append(totalAltas).append("\n");
        resumen.append("Productos eliminados:    ").append(totalBajas).append("\n");
        resumen.append("Actualizaciones:         ").append(totalActualizaciones).append("\n");
        resumen.append("Total de operaciones:    ").append(totalAltas + totalBajas + totalActualizaciones).append("\n");
        resumen.append("\n¡Gracias por usar el sistema!");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resumen de Sesión");
        alert.setHeaderText("Finalizando sistema de inventario");
        alert.setContentText(resumen.toString());
        alert.showAndWait();
        
        System.exit(0);
    }
    
    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        areaResultados.setText("✓ OPERACIÓN EXITOSA\n\n" + mensaje);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        areaResultados.setText("✗ ERROR\n\n" + mensaje);
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}