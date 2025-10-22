package com.minitienda.minitienda;

import com.minitienda.modelo.Producto;
import com.minitienda.servicio.ServicioInventario;
import com.minitienda.util.CSVProductoProcessor;
import com.minitienda.util.CSVProductoExporter;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MiniTiendaApp extends Application {
    private ServicioInventario servicio;
    private int operacionesAlta = 0;
    private int operacionesBaja = 0;
    private int operacionesActualizacion = 0;
    private TextArea areaResultados;
    private ProgressBar progressBar;
    private Label lblProgreso;

    @Override
    public void start(Stage primaryStage) {
        try {
            servicio = new ServicioInventario();
            
            primaryStage.setTitle("Mini-Tienda - Gestión de Inventario");
            
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(15));
            
            // Título
            Label titulo = new Label("SISTEMA DE GESTIÓN DE INVENTARIO");
            titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            // Barra de progreso (inicialmente oculta)
            progressBar = new ProgressBar(0);
            progressBar.setPrefWidth(500);
            progressBar.setVisible(false);
            
            lblProgreso = new Label("");
            lblProgreso.setVisible(false);
            
            VBox topBox = new VBox(10, titulo, progressBar, lblProgreso);
            topBox.setAlignment(Pos.CENTER);
            topBox.setPadding(new Insets(0, 0, 15, 0));
            
            areaResultados = new TextArea();
            areaResultados.setEditable(false);
            areaResultados.setPrefHeight(250);
            areaResultados.setWrapText(true);
            areaResultados.setText("Bienvenido al Sistema de Gestión de Inventario\n" +
                                  "Seleccione una opción del menú\n" +
                                  "=".repeat(50));
            
            VBox menuBox = crearMenuBotones();
            
            root.setTop(topBox);
            root.setCenter(areaResultados);
            root.setRight(menuBox);
            
            Scene scene = new Scene(root, 950, 550);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            mostrarError("Error al inicializar la aplicación: " + e.getMessage());
        }
    }

    private VBox crearMenuBotones() {
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(0, 0, 0, 15));
        menuBox.setPrefWidth(220);
        
        // Botón Cargar CSV
        Button btnCargarCSV = crearBoton("📂 Cargar CSV");
        btnCargarCSV.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        // Botón Exportar CSV (NUEVO)
        Button btnExportarCSV = crearBoton("💾 Exportar CSV");
        btnExportarCSV.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnAgregar = crearBoton("➕ Agregar Producto");
        Button btnListar = crearBoton("📋 Listar Inventario");
        Button btnActualizarPrecio = crearBoton("💰 Actualizar Precio");
        Button btnActualizarStock = crearBoton("📦 Actualizar Stock");
        Button btnEliminar = crearBoton("🗑️ Eliminar Producto");
        Button btnBuscar = crearBoton("🔍 Buscar por Nombre");
        
        // Botón Eliminar Todos (NUEVO)
        Button btnEliminarTodos = crearBoton("🗑️ ELIMINAR TODO");
        btnEliminarTodos.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnSalir = crearBoton("🚪 Salir");
        btnSalir.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        
        btnCargarCSV.setOnAction(e -> cargarDesdeCSV());
        btnExportarCSV.setOnAction(e -> exportarACSV());
        btnAgregar.setOnAction(e -> agregarProducto());
        btnListar.setOnAction(e -> listarInventario());
        btnActualizarPrecio.setOnAction(e -> actualizarPrecio());
        btnActualizarStock.setOnAction(e -> actualizarStock());
        btnEliminar.setOnAction(e -> eliminarProducto());
        btnBuscar.setOnAction(e -> buscarProducto());
        btnEliminarTodos.setOnAction(e -> eliminarTodos());
        btnSalir.setOnAction(e -> salir());
        
        menuBox.getChildren().addAll(
            btnCargarCSV, btnExportarCSV, new Separator(),
            btnAgregar, btnListar, btnActualizarPrecio,
            btnActualizarStock, btnEliminar, btnBuscar,
            new Separator(), btnEliminarTodos, new Separator(), btnSalir
        );
        
        return menuBox;
    }
    
    private Button crearBoton(String texto) {
        Button btn = new Button(texto);
        btn.setPrefWidth(200);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-font-size: 12px;");
        return btn;
    }

    /**
     * MÉTODO ACTUALIZADO: Carga dinámica con Task para archivos grandes
     */
    private void cargarDesdeCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );
        
        File archivo = fileChooser.showOpenDialog(null);
        
        if (archivo == null) {
            return;
        }
        
        // Crear tarea en segundo plano
        Task<CSVProductoProcessor.ResultadoCarga> tarea = new Task<>() {
            @Override
            protected CSVProductoProcessor.ResultadoCarga call() throws Exception {
                updateMessage("Procesando archivo CSV...");
                return CSVProductoProcessor.procesarCSV(archivo);
            }
        };
        
        // Manejar progreso
        tarea.messageProperty().addListener((obs, oldMsg, newMsg) -> {
            lblProgreso.setText(newMsg);
        });
        
        // Cuando inicia
        tarea.setOnRunning(e -> {
            progressBar.setVisible(true);
            lblProgreso.setVisible(true);
            progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            mostrarMensaje("📂 Procesando archivo: " + archivo.getName() + "...\n" +
                          "Por favor espere...");
        });
        
        // Cuando termina exitosamente
        tarea.setOnSucceeded(e -> {
            CSVProductoProcessor.ResultadoCarga resultado = tarea.getValue();
            progressBar.setVisible(false);
            lblProgreso.setVisible(false);
            
            if (resultado.getProductosExitosos().isEmpty()) {
                mostrarError("❌ No se encontraron productos válidos en el archivo CSV.\n\n" +
                           "Errores encontrados:\n" + String.join("\n", resultado.getErrores()) +
                           "\n\nFormato esperado:\n" +
                           "nombre,precio,stock\n" +
                           "Laptop,1200.50,10\n" +
                           "Mouse,25.99,50");
                return;
            }
            
            // Confirmar carga
            confirmarYCargarProductos(archivo, resultado);
        });
        
        // Si hay error
        tarea.setOnFailed(e -> {
            progressBar.setVisible(false);
            lblProgreso.setVisible(false);
            Throwable ex = tarea.getException();
            mostrarError("❌ Error al procesar CSV:\n" + ex.getMessage());
        });
        
        // Ejecutar en hilo separado
        new Thread(tarea).start();
    }
    
    private void confirmarYCargarProductos(File archivo, CSVProductoProcessor.ResultadoCarga resultado) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Carga de Productos");
        confirmacion.setHeaderText("Se encontraron " + resultado.getExitosos() + " productos válidos");
        
        StringBuilder detalles = new StringBuilder();
        detalles.append("Archivo: ").append(archivo.getName()).append("\n");
        detalles.append("Total de líneas procesadas: ").append(resultado.getTotalProcesados()).append("\n");
        detalles.append("Productos válidos: ").append(resultado.getExitosos()).append("\n");
        detalles.append("Errores: ").append(resultado.getFallidos()).append("\n\n");
        
        if (resultado.tieneErrores()) {
            detalles.append("⚠️ Líneas con errores:\n");
            int maxErrores = Math.min(10, resultado.getErrores().size());
            for (int i = 0; i < maxErrores; i++) {
                detalles.append("  • ").append(resultado.getErrores().get(i)).append("\n");
            }
            if (resultado.getErrores().size() > 10) {
                detalles.append("  ... y ").append(resultado.getErrores().size() - 10)
                        .append(" errores más\n");
            }
            detalles.append("\n");
        }
        
        detalles.append("¿Desea cargar los ").append(resultado.getExitosos())
                .append(" productos válidos a la base de datos?");
        
        confirmacion.setContentText(detalles.toString());
        
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            cargarProductosEnBaseDatos(archivo, resultado);
        }
    }
    
    private void cargarProductosEnBaseDatos(File archivo, CSVProductoProcessor.ResultadoCarga resultado) {
        Task<Integer> tareaDB = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                updateMessage("Guardando productos en base de datos...");
                updateProgress(0, resultado.getExitosos());
                
                int cargados = servicio.cargarProductosDesdeCSV(resultado.getProductosExitosos());
                updateProgress(resultado.getExitosos(), resultado.getExitosos());
                
                return cargados;
            }
        };
        
        tareaDB.messageProperty().addListener((obs, oldMsg, newMsg) -> {
            lblProgreso.setText(newMsg);
        });
        
        tareaDB.progressProperty().addListener((obs, oldProg, newProg) -> {
            progressBar.setProgress(newProg.doubleValue());
        });
        
        tareaDB.setOnRunning(e -> {
            progressBar.setVisible(true);
            lblProgreso.setVisible(true);
            progressBar.setProgress(0);
        });
        
        tareaDB.setOnSucceeded(e -> {
            int cargados = tareaDB.getValue();
            operacionesAlta += cargados;
            
            progressBar.setVisible(false);
            lblProgreso.setVisible(false);
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✅ CARGA COMPLETADA EXITOSAMENTE\n");
            mensaje.append("=".repeat(50)).append("\n\n");
            mensaje.append("📊 Resumen de la carga:\n");
            mensaje.append("  • Archivo: ").append(archivo.getName()).append("\n");
            mensaje.append("  • Productos cargados: ").append(cargados).append("\n");
            mensaje.append("  • Total procesados: ").append(resultado.getTotalProcesados()).append("\n");
            
            if (resultado.tieneErrores()) {
                mensaje.append("  • Líneas con errores: ").append(resultado.getFallidos()).append("\n");
            }
            
            mostrarMensaje(mensaje.toString());
        });
        
        tareaDB.setOnFailed(e -> {
            progressBar.setVisible(false);
            lblProgreso.setVisible(false);
            
            Throwable ex = tareaDB.getException();
            if (ex.getMessage().contains("productos duplicados")) {
                mostrarError("⚠️ " + ex.getMessage());
            } else {
                mostrarError("❌ Error al guardar en base de datos:\n" + ex.getMessage());
            }
        });
        
        new Thread(tareaDB).start();
    }

    /**
     * MÉTODO NUEVO: Exportar a CSV
     */
    private void exportarACSV() {
        try {
            // Verificar si hay productos
            int totalProductos = servicio.contarProductos();
            if (totalProductos == 0) {
                mostrarError("ℹ️ No hay productos para exportar");
                return;
            }
            
            // Preguntar formato de exportación
            Alert formatoAlert = new Alert(Alert.AlertType.CONFIRMATION);
            formatoAlert.setTitle("Formato de Exportación");
            formatoAlert.setHeaderText("Seleccione el formato de exportación");
            
            ButtonType btnConId = new ButtonType("Con ID");
            ButtonType btnSinId = new ButtonType("Sin ID");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            formatoAlert.getButtonTypes().setAll(btnConId, btnSinId, btnCancelar);
            formatoAlert.setContentText("Total de productos a exportar: " + totalProductos + "\n\n" +
                                       "• Con ID: Incluye id,nombre,precio,stock\n" +
                                       "• Sin ID: Solo nombre,precio,stock\n" +
                                       "  (útil para importar en otros sistemas)");
            
            Optional<ButtonType> resultado = formatoAlert.showAndWait();
            if (!resultado.isPresent() || resultado.get() == btnCancelar) {
                return;
            }
            
            boolean incluirId = (resultado.get() == btnConId);
            
            // Seleccionar ubicación del archivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo CSV");
            fileChooser.setInitialFileName("productos_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
            );
            
            File archivo = fileChooser.showSaveDialog(null);
            if (archivo == null) {
                return;
            }
            
            // Exportar en segundo plano
            Task<CSVProductoExporter.ResultadoExportacion> tareaExport = new Task<>() {
                @Override
                protected CSVProductoExporter.ResultadoExportacion call() throws Exception {
                    updateMessage("Obteniendo productos de la base de datos...");
                    List<Producto> productos = servicio.listarTodos();
                    
                    updateMessage("Exportando " + productos.size() + " productos...");
                    
                    if (incluirId) {
                        return CSVProductoExporter.exportarACSV(productos, archivo);
                    } else {
                        return CSVProductoExporter.exportarSinId(productos, archivo);
                    }
                }
            };
            
            tareaExport.messageProperty().addListener((obs, oldMsg, newMsg) -> {
                lblProgreso.setText(newMsg);
            });
            
            tareaExport.setOnRunning(e -> {
                progressBar.setVisible(true);
                lblProgreso.setVisible(true);
                progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                mostrarMensaje("💾 Exportando datos...\nPor favor espere...");
            });
            
            tareaExport.setOnSucceeded(e -> {
                progressBar.setVisible(false);
                lblProgreso.setVisible(false);
                
                CSVProductoExporter.ResultadoExportacion res = tareaExport.getValue();
                
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("✅ EXPORTACIÓN COMPLETADA\n");
                mensaje.append("=".repeat(50)).append("\n\n");
                mensaje.append("📊 Resumen:\n");
                mensaje.append("  • Productos exportados: ").append(res.getRegistrosExportados()).append("\n");
                mensaje.append("  • Archivo: ").append(archivo.getName()).append("\n");
                mensaje.append("  • Ubicación: ").append(archivo.getAbsolutePath()).append("\n");
                mensaje.append("  • Formato: ").append(incluirId ? "Con ID" : "Sin ID").append("\n");
                
                mostrarMensaje(mensaje.toString());
            });
            
            tareaExport.setOnFailed(e -> {
                progressBar.setVisible(false);
                lblProgreso.setVisible(false);
                mostrarError("❌ Error al exportar:\n" + tareaExport.getException().getMessage());
            });
            
            new Thread(tareaExport).start();
            
        } catch (SQLException e) {
            mostrarError("❌ Error de base de datos:\n" + e.getMessage());
        } catch (Exception e) {
            mostrarError("❌ Error inesperado:\n" + e.getMessage());
        }
    }

    /**
     * MÉTODO NUEVO: Eliminar todos los productos
     */
    private void eliminarTodos() {
        try {
            int totalProductos = servicio.contarProductos();
            
            if (totalProductos == 0) {
                mostrarMensaje("ℹ️ No hay productos para eliminar");
                return;
            }
            
            // Confirmación con advertencia fuerte
            Alert confirmacion = new Alert(Alert.AlertType.WARNING);
            confirmacion.setTitle("⚠️ ADVERTENCIA - Eliminar Todos los Productos");
            confirmacion.setHeaderText("Esta acción NO se puede deshacer");
            
            ButtonType btnConfirmar = new ButtonType("Sí, eliminar todo", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmacion.getButtonTypes().setAll(btnConfirmar, btnCancelar);
            
            confirmacion.setContentText(
                "Total de productos en base de datos: " + totalProductos + "\n\n" +
                "⚠️ ATENCIÓN:\n" +
                "• Se eliminarán TODOS los productos\n" +
                "• Esta acción es IRREVERSIBLE\n" +
                "• Se recomienda exportar un respaldo antes\n\n" +
                "¿Está seguro de continuar?"
            );
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (!resultado.isPresent() || resultado.get() != btnConfirmar) {
                mostrarMensaje("ℹ️ Operación cancelada");
                return;
            }
            
            // Segunda confirmación
            TextInputDialog confirmacionFinal = new TextInputDialog();
            confirmacionFinal.setTitle("Confirmación Final");
            confirmacionFinal.setHeaderText("Para confirmar, escriba: ELIMINAR");
            confirmacionFinal.setContentText("Escriba ELIMINAR (en mayúsculas):");
            
            Optional<String> textoConfirmacion = confirmacionFinal.showAndWait();
            if (!textoConfirmacion.isPresent() || !textoConfirmacion.get().equals("ELIMINAR")) {
                mostrarMensaje("ℹ️ Operación cancelada - Confirmación incorrecta");
                return;
            }
            
            // Ejecutar eliminación
            Task<Integer> tareaEliminar = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    updateMessage("Eliminando todos los productos...");
                    return servicio.eliminarTodosLosProductos();
                }
            };
            
            tareaEliminar.messageProperty().addListener((obs, oldMsg, newMsg) -> {
                lblProgreso.setText(newMsg);
            });
            
            tareaEliminar.setOnRunning(e -> {
                progressBar.setVisible(true);
                lblProgreso.setVisible(true);
                progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            });
            
            tareaEliminar.setOnSucceeded(e -> {
                progressBar.setVisible(false);
                lblProgreso.setVisible(false);
                
                int eliminados = tareaEliminar.getValue();
                operacionesBaja += eliminados;
                
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("✅ ELIMINACIÓN MASIVA COMPLETADA\n");
                mensaje.append("=".repeat(50)).append("\n\n");
                mensaje.append("📊 Productos eliminados: ").append(eliminados).append("\n");
                mensaje.append("\nLa base de datos ha sido limpiada completamente.");
                
                mostrarMensaje(mensaje.toString());
            });
            
            tareaEliminar.setOnFailed(e -> {
                progressBar.setVisible(false);
                lblProgreso.setVisible(false);
                mostrarError("❌ Error al eliminar productos:\n" + 
                           tareaEliminar.getException().getMessage());
            });
            
            new Thread(tareaEliminar).start();
            
        } catch (SQLException e) {
            mostrarError("❌ Error de base de datos:\n" + e.getMessage());
        } catch (Exception e) {
            mostrarError("❌ Error inesperado:\n" + e.getMessage());
        }
    }

    private void agregarProducto() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del nuevo producto");
        
        ButtonType btnAgregar = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAgregar, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("0.00");
        TextField txtStock = new TextField();
        txtStock.setPromptText("0");
        
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Precio:"), 0, 1);
        grid.add(txtPrecio, 1, 1);
        grid.add(new Label("Stock:"), 0, 2);
        grid.add(txtStock, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAgregar) {
                try {
                    String nombre = txtNombre.getText().trim();
                    if (nombre.isEmpty()) {
                        throw new IllegalArgumentException("El nombre no puede estar vacío");
                    }
                    
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int stock = Integer.parseInt(txtStock.getText());
                    
                    if (precio < 0) {
                        throw new IllegalArgumentException("El precio no puede ser negativo");
                    }
                    if (stock < 0) {
                        throw new IllegalArgumentException("El stock no puede ser negativo");
                    }
                    
                    return new Producto(nombre, precio, stock);
                } catch (NumberFormatException e) {
                    mostrarError("Error: Precio y stock deben ser números válidos");
                    return null;
                } catch (IllegalArgumentException e) {
                    mostrarError("Error de validación: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });
        
        Optional<Producto> resultado = dialog.showAndWait();
        resultado.ifPresent(producto -> {
            try {
                servicio.agregarProducto(producto.getNombre(), producto.getPrecio(), producto.getStock());
                operacionesAlta++;
                mostrarMensaje("✅ Producto agregado exitosamente:\n" + 
                             "Nombre: " + producto.getNombre() + "\n" +
                             "Precio: $" + String.format("%.2f", producto.getPrecio()) + "\n" +
                             "Stock: " + producto.getStock());
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    mostrarError("❌ Error: Ya existe un producto con ese nombre");
                } else {
                    mostrarError("❌ Error de base de datos: " + e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                mostrarError("❌ Error de validación: " + e.getMessage());
            }
        });
    }

    private void listarInventario() {
        try {
            List<Producto> productos = servicio.listarTodos();
            
            if (productos.isEmpty()) {
                mostrarMensaje("ℹ️ No hay productos en el inventario");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("📋 INVENTARIO COMPLETO\n");
            sb.append("=".repeat(70)).append("\n\n");
            
            for (Producto p : productos) {
                sb.append(String.format("ID: %-5d | %-30s | Precio: $%8.2f | Stock: %5d\n",
                        p.getId(), p.getNombre(), p.getPrecio(), p.getStock()));
            }
            
            sb.append("\n").append("=".repeat(70));
            sb.append("\nTotal de productos: ").append(productos.size());
            
            mostrarMensaje(sb.toString());
            
        } catch (SQLException e) {
            mostrarError("❌ Error al listar productos: " + e.getMessage());
        }
    }

    private void actualizarPrecio() {
        TextInputDialog dialogId = new TextInputDialog();
        dialogId.setTitle("Actualizar Precio");
        dialogId.setHeaderText("Ingrese el ID del producto");
        dialogId.setContentText("ID:");
        
        Optional<String> resultId = dialogId.showAndWait();
        if (!resultId.isPresent() || resultId.get().trim().isEmpty()) {
            return;
        }
        
        try {
            int id = Integer.parseInt(resultId.get().trim());
            Producto producto = servicio.buscarPorId(id);
            
            if (producto == null) {
                mostrarError("❌ No se encontró un producto con ID: " + id);
                return;
            }
            
            TextInputDialog dialogPrecio = new TextInputDialog(String.valueOf(producto.getPrecio()));
            dialogPrecio.setTitle("Actualizar Precio");
            dialogPrecio.setHeaderText("Producto: " + producto.getNombre() + "\nPrecio actual: $" + 
                                      String.format("%.2f", producto.getPrecio()));
            dialogPrecio.setContentText("Nuevo precio:");
            
            Optional<String> resultPrecio = dialogPrecio.showAndWait();
            if (resultPrecio.isPresent()) {
                double nuevoPrecio = Double.parseDouble(resultPrecio.get());
                servicio.actualizarPrecio(id, nuevoPrecio);
                operacionesActualizacion++;
                mostrarMensaje("✅ Precio actualizado exitosamente\n" +
                             "Producto: " + producto.getNombre() + "\n" +
                             "Precio anterior: $" + String.format("%.2f", producto.getPrecio()) + "\n" +
                             "Precio nuevo: $" + String.format("%.2f", nuevoPrecio));
            }
            
        } catch (NumberFormatException e) {
            mostrarError("❌ Error: Debe ingresar números válidos");
        } catch (SQLException e) {
            mostrarError("❌ Error de base de datos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError("❌ Error de validación: " + e.getMessage());
        }
    }

    private void actualizarStock() {
        TextInputDialog dialogId = new TextInputDialog();
        dialogId.setTitle("Actualizar Stock");
        dialogId.setHeaderText("Ingrese el ID del producto");
        dialogId.setContentText("ID:");
        
        Optional<String> resultId = dialogId.showAndWait();
        if (!resultId.isPresent() || resultId.get().trim().isEmpty()) {
            return;
        }
        
        try {
            int id = Integer.parseInt(resultId.get().trim());
            Producto producto = servicio.buscarPorId(id);
            
            if (producto == null) {
                mostrarError("❌ No se encontró un producto con ID: " + id);
                return;
            }
            
            TextInputDialog dialogStock = new TextInputDialog(String.valueOf(producto.getStock()));
            dialogStock.setTitle("Actualizar Stock");
            dialogStock.setHeaderText("Producto: " + producto.getNombre() + "\nStock actual: " + 
                                     producto.getStock());
            dialogStock.setContentText("Nuevo stock:");
            
            Optional<String> resultStock = dialogStock.showAndWait();
            if (resultStock.isPresent()) {
                int nuevoStock = Integer.parseInt(resultStock.get());
                servicio.actualizarStock(id, nuevoStock);
                operacionesActualizacion++;
                mostrarMensaje("✅ Stock actualizado exitosamente\n" +
                             "Producto: " + producto.getNombre() + "\n" +
                             "Stock anterior: " + producto.getStock() + "\n" +
                             "Stock nuevo: " + nuevoStock);
            }
            
        } catch (NumberFormatException e) {
            mostrarError("❌ Error: Debe ingresar números válidos");
        } catch (SQLException e) {
            mostrarError("❌ Error de base de datos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError("❌ Error de validación: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Producto");
        dialog.setHeaderText("Ingrese el ID del producto a eliminar");
        dialog.setContentText("ID:");
        
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            return;
        }
        
        try {
            int id = Integer.parseInt(result.get().trim());
            Producto producto = servicio.buscarPorId(id);
            
            if (producto == null) {
                mostrarError("❌ No se encontró un producto con ID: " + id);
                return;
            }
            
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este producto?");
            confirmacion.setContentText("ID: " + producto.getId() + "\n" +
                                       "Nombre: " + producto.getNombre() + "\n" +
                                       "Precio: $" + String.format("%.2f", producto.getPrecio()) + "\n" +
                                       "Stock: " + producto.getStock());
            
            Optional<ButtonType> respuesta = confirmacion.showAndWait();
            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                servicio.eliminarProducto(id);
                operacionesBaja++;
                mostrarMensaje("✅ Producto eliminado exitosamente:\n" + producto.getNombre());
            }
            
        } catch (NumberFormatException e) {
            mostrarError("❌ Error: Debe ingresar un número válido");
        } catch (SQLException e) {
            mostrarError("❌ Error de base de datos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError("❌ Error: " + e.getMessage());
        }
    }

    private void buscarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Producto");
        dialog.setHeaderText("Ingrese el nombre o parte del nombre del producto");
        dialog.setContentText("Nombre:");
        
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            return;
        }
        
        try {
            String nombre = result.get().trim();
            List<Producto> productos = servicio.buscarPorNombre(nombre);
            
            if (productos.isEmpty()) {
                mostrarMensaje("ℹ️ No se encontraron productos con el nombre: \"" + nombre + "\"");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("🔍 RESULTADOS DE BÚSQUEDA: \"").append(nombre).append("\"\n");
            sb.append("=".repeat(70)).append("\n\n");
            
            for (Producto p : productos) {
                sb.append(String.format("ID: %-5d | %-30s | Precio: $%8.2f | Stock: %5d\n",
                        p.getId(), p.getNombre(), p.getPrecio(), p.getStock()));
            }
            
            sb.append("\n").append("=".repeat(70));
            sb.append("\nProductos encontrados: ").append(productos.size());
            
            mostrarMensaje(sb.toString());
            
        } catch (SQLException e) {
            mostrarError("❌ Error al buscar productos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError("❌ Error de validación: " + e.getMessage());
        }
    }

    private void salir() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resumen de Operaciones");
        alert.setHeaderText("RESUMEN DE LA SESIÓN");
        
        StringBuilder resumen = new StringBuilder();
        resumen.append("═".repeat(40)).append("\n\n");
        resumen.append("📈 Operaciones de Alta:          ").append(operacionesAlta).append("\n");
        resumen.append("📉 Operaciones de Baja:          ").append(operacionesBaja).append("\n");
        resumen.append("🔄 Operaciones de Actualización: ").append(operacionesActualizacion).append("\n\n");
        resumen.append("═".repeat(40)).append("\n");
        resumen.append("TOTAL DE OPERACIONES: ").append(operacionesAlta + operacionesBaja + operacionesActualizacion);
        resumen.append("\n\n¡Gracias por usar el Sistema de Gestión de Inventario!");
        
        alert.setContentText(resumen.toString());
        alert.showAndWait();
        
        System.exit(0);
    }

    private void mostrarMensaje(String mensaje) {
        areaResultados.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}