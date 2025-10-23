🏪 Sistema de Inventario Mini-Tienda
📋 Descripción
Sistema de gestión de inventario desarrollado en Java 17 con interfaz gráfica JavaFX, implementando arquitectura por capas y el patrón Singleton. Permite la administración completa de productos mediante operaciones CRUD con validaciones robustas y manejo de excepciones personalizadas.

🎯 Características Principales
✨ Funcionalidades

✅ Alta de productos - Agregar nuevos productos al inventario
📋 Listado completo - Visualizar todos los productos registrados
💰 Actualizar precios - Modificar el precio de productos existentes
📦 Actualizar stock - Ajustar las cantidades disponibles
🗑️ Eliminar productos - Dar de baja productos con confirmación
🔍 Búsqueda - Localizar productos por nombre exacto
📊 Resumen de sesión - Contador de operaciones realizadas

🚀 Características Técnicas

🏗️ Arquitectura por capas (Domain, DAO, Service, UI)
🎯 Patrón Singleton en Service y Conexión DB
✔️ Validaciones robustas con excepciones personalizadas
🛡️ Prepared Statements para prevenir SQL Injection
🎨 Interfaz moderna con JavaFX
💾 Persistencia en MySQL con JDBC


🏗️ Arquitectura del Proyecto
Patrón: Arquitectura por Capas
com.mycompany.minitienda
│
├─ domain/                              # 📦 Capa de Dominio
│  └─ Producto.java                     # POJO del producto
│
├─ exceptions/                          # ⚠️ Excepciones Personalizadas
│  ├─ DatoInvalidoException.java        # Validación de datos
│  ├─ DuplicadoException.java           # Productos duplicados
│  └─ PersistenciaException.java        # Errores de BD
│
├─ DAO/                                 # 🗄️ Capa de Acceso a Datos
│  ├─ jdbc/
│  │  └─ ProductoDAOImpl.java           # Implementación JDBC
│  └─ ProductoDAO.java                  # Interfaz DAO
│
├─ infra/                               # 🔧 Infraestructura
│  └─ config/
│     └─ ConexionDB.java                # Singleton - Conexión BD
│
├─ service/                             # 💼 Capa de Negocio
│  ├─ impl/
│  │  └─ InventarioServiceImpl.java     # Implementación Singleton
│  └─ InventarioServiceLocal.java       # Interfaz de servicios
│
└─ ui/                                  # 🖥️ Capa de Presentación
   └─ MainApp.java                      # Aplicación JavaFX
Patrón de Diseño: Singleton
Implementado en:

InventarioServiceImpl - Única instancia del servicio
ConexionDB - Única conexión a base de datos


🛠️ Tecnologías Utilizadas
TecnologíaVersiónPropósitoJava17Lenguaje de programaciónJavaFX17+Interfaz gráfica de usuarioMySQL8.0+Base de datos relacionalJDBCNativoConectividad con BDNetBeans27IDE de desarrolloMaven3.8+Gestor de dependencias

📦 Requisitos Previos
Software Necesario
bash☑️ JDK 17 o superior
☑️ MySQL Server 8.0+
☑️ NetBeans IDE 27
☑️ Maven 3.8+ (incluido en NetBeans)
Dependencias Maven (pom.xml)
xml<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.mycompany</groupId>
    <artifactId>minitienda</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>
    
    <dependencies>
        <!-- JavaFX Controls -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>17.0.2</version>
        </dependency>
        
        <!-- JavaFX FXML -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>17.0.2</version>
        </dependency>
        
        <!-- MySQL Connector -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.mycompany.minitienda.ui.MainApp</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

⚙️ Configuración de Base de Datos
1. Crear Base de Datos
sqlCREATE DATABASE IF NOT EXISTS minitienda_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE minitienda_db;
2. Crear Tabla de Productos
sqlCREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL CHECK (stock >= 0),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB;
3. Datos de Prueba (Opcional)
sqlINSERT INTO productos (nombre, precio, stock) VALUES
('Laptop HP', 1200.00, 10),
('Mouse Logitech', 25.99, 50),
('Teclado Mecánico', 89.99, 30),
('Monitor Samsung 24"', 299.99, 15),
('Webcam HD', 79.99, 25);
4. Configurar Conexión en ConexionDB.java
javapublic class ConexionDB {
    private static ConexionDB instancia;
    private Connection conexion;
    
    private static final String URL = "jdbc:mysql://localhost:3306/minitienda_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Qwe.123*"; // ⚠️ Cambiar según tu configuración
    
    // ... resto del código
}

🚀 Instalación y Ejecución
Método 1: NetBeans IDE
bash1. Abrir NetBeans 27
2. File → Open Project
3. Seleccionar la carpeta del proyecto "minitienda"
4. Configurar la base de datos (ver sección anterior)
5. Click derecho en el proyecto → Clean and Build
6. Click derecho → Run
Método 2: Línea de Comandos
bash# Navegar a la carpeta del proyecto
cd minitienda

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run

# Generar JAR ejecutable
mvn clean package
Método 3: JAR Ejecutable
bash# Después de generar el JAR
cd target
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -jar minitienda-1.0-SNAPSHOT.jar

📖 Manual de Usuario
🖥️ Interfaz Principal
┌─────────────────────────────────────────────────────────────┐
│  Mini-Tienda - Sistema de Inventario                        │
├──────────────────┬──────────────────────────────────────────┤
│  MENÚ PRINCIPAL  │          Resultados:                     │
│  ──────────────  │  ════════════════════════════════════    │
│                  │                                           │
│  1. Agregar      │  === BIENVENIDO AL SISTEMA ===          │
│     Producto     │  Seleccione una opción del menú         │
│                  │                                           │
│  2. Listar       │                                           │
│     Inventario   │                                           │
│                  │                                           │
│  3. Actualizar   │                                           │
│     Precio       │                                           │
│                  │                                           │
│  4. Actualizar   │                                           │
│     Stock        │                                           │
│                  │                                           │
│  5. Eliminar     │                                           │
│     Producto     │                                           │
│                  │                                           │
│  6. Buscar por   │                                           │
│     Nombre       │                                           │
│  ──────────────  │                                           │
│  7. Salir        │                                           │
└──────────────────┴──────────────────────────────────────────┘

1️⃣ Agregar Producto
Pasos:

Click en "1. Agregar Producto"
Completar el formulario:

Nombre: Texto único (ej. "Laptop Dell XPS")
Precio: Número decimal positivo (ej. 1299.99)
Stock: Número entero positivo (ej. 10)


Click en "OK"

Validaciones:

✅ Nombre no puede estar vacío
✅ Nombre debe ser único
✅ Precio debe ser >= 0
✅ Stock debe ser >= 0

Ejemplo:
Nombre: Laptop Gaming ASUS
Precio: 1899.99
Stock: 15

✅ Producto agregado exitosamente

2️⃣ Listar Inventario
Pasos:

Click en "2. Listar Inventario"
Ver listado completo en el área de resultados

Salida:
=== LISTADO DE PRODUCTOS ===
ID    NOMBRE                         PRECIO          STOCK     
-----------------------------------------------------------------
1     Laptop HP                      $1200.00        10        
2     Mouse Logitech                 $25.99          50        
3     Teclado Mecánico               $89.99          30        
-----------------------------------------------------------------
Total de productos: 3

3️⃣ Actualizar Precio
Pasos:

Click en "3. Actualizar Precio"
Ingresar ID del Producto (ej. 1)
Ingresar Nuevo Precio (ej. 1150.00)
Click en "OK"

Validaciones:

✅ ID debe existir en la base de datos
✅ Precio debe ser >= 0

Ejemplo:
ID del Producto: 1
Nuevo Precio: 1150.00

✅ Precio actualizado exitosamente
Producto ID: 1
Nuevo precio: $1150.00

4️⃣ Actualizar Stock
Pasos:

Click en "4. Actualizar Stock"
Ingresar ID del Producto (ej. 2)
Ingresar Nuevo Stock (ej. 75)
Click en "OK"

Validaciones:

✅ ID debe existir en la base de datos
✅ Stock debe ser >= 0

Ejemplo:
ID del Producto: 2
Nuevo Stock: 75

✅ Stock actualizado exitosamente
Producto ID: 2
Nuevo stock: 75

5️⃣ Eliminar Producto
Pasos:

Click en "5. Eliminar Producto"
Ingresar ID del Producto a eliminar
Confirmar la eliminación en el diálogo
Click en "OK"

Advertencia:
⚠️ Esta acción NO se puede deshacer
Ejemplo:
ID del Producto: 3

¿Está seguro de eliminar este producto?
Esta acción no se puede deshacer.

[OK] [Cancelar]

✅ Producto eliminado exitosamente
ID: 3

6️⃣ Buscar por Nombre
Pasos:

Click en "6. Buscar por Nombre"
Ingresar el nombre exacto del producto
Click en "OK"

Nota: La búsqueda es exacta (case-sensitive)
Ejemplo:
Ingrese el nombre del producto: Mouse Logitech

=== PRODUCTO ENCONTRADO ===

ID:     2
Nombre: Mouse Logitech
Precio: $25.99
Stock:  75 unidades
Si no se encuentra:
=== BÚSQUEDA SIN RESULTADOS ===
No se encontró ningún producto con el nombre: Mouse Inalámbrico

7️⃣ Salir
Pasos:

Click en "7. Salir"
Ver resumen de operaciones
Click en "OK" para cerrar

Resumen de sesión:
=== RESUMEN DE OPERACIONES ===

Productos agregados:     5
Productos eliminados:    1
Actualizaciones:         8
Total de operaciones:    14

¡Gracias por usar el sistema!

🔒 Validaciones Implementadas
Validaciones de Dominio
CampoReglaExcepciónNombreNo vacío, únicoDatoInvalidoException / DuplicadoExceptionPrecio>= 0, numéricoDatoInvalidoExceptionStock>= 0, enteroDatoInvalidoExceptionIDExistente en BDPersistenciaException
Ejemplo de Validación en Producto.java
javapublic Producto(String nombre, Double precio, Integer stock) {
    if (nombre == null || nombre.trim().isEmpty()) {
        throw new DatoInvalidoException("El nombre no puede estar vacío");
    }
    if (precio == null || precio < 0) {
        throw new DatoInvalidoException("El precio debe ser mayor o igual a 0");
    }
    if (stock == null || stock < 0) {
        throw new DatoInvalidoException("El stock debe ser mayor o igual a 0");
    }
    
    this.nombre = nombre.trim();
    this.precio = precio;
    this.stock = stock;
}

🛡️ Manejo de Excepciones
Jerarquía de Excepciones Personalizadas
Exception
    │
    └─ RuntimeException
           │
           ├─ DatoInvalidoException      # Validación de datos
           ├─ DuplicadoException          # Nombres duplicados
           └─ PersistenciaException       # Errores de BD
Descripción de Excepciones
DatoInvalidoException
java// Se lanza cuando:
- Nombre vacío o null
- Precio negativo o null
- Stock negativo o null
- ID inválido (0 o negativo)

// Ejemplo:
throw new DatoInvalidoException("El precio debe ser mayor o igual a 0");
DuplicadoException
java// Se lanza cuando:
- Producto con el mismo nombre ya existe

// Ejemplo:
throw new DuplicadoException("Ya existe un producto con el nombre: " + nombre);
PersistenciaException
java// Se lanza cuando:
- Error de conexión a BD
- Error en consultas SQL
- Producto no encontrado por ID

// Ejemplo:
throw new PersistenciaException("No se encontró producto con ID: " + id);

💾 Capa de Persistencia (DAO)
Operaciones Implementadas
ProductoDAO.java (Interfaz)
javapublic interface ProductoDAO {
    void insertar(Producto producto) throws PersistenciaException;
    List<Producto> listarTodos() throws PersistenciaException;
    Producto buscarPorId(Integer id) throws PersistenciaException;
    Producto buscarPorNombre(String nombre) throws PersistenciaException;
    void actualizarPrecio(Integer id, Double precio) throws PersistenciaException;
    void actualizarStock(Integer id, Integer stock) throws PersistenciaException;
    void eliminar(Integer id) throws PersistenciaException;
}
Ejemplo: insertar() con Prepared Statement
java@Override
public void insertar(Producto producto) throws PersistenciaException {
    String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
    
    try (Connection conn = ConexionDB.getInstancia().getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, producto.getNombre());
        ps.setDouble(2, producto.getPrecio());
        ps.setInt(3, producto.getStock());
        
        ps.executeUpdate();
        
    } catch (SQLException e) {
        if (e.getErrorCode() == 1062) { // Duplicate entry
            throw new DuplicadoException("Ya existe un producto con el nombre: " + 
                                        producto.getNombre());
        }
        throw new PersistenciaException("Error al insertar producto", e);
    }
}

💼 Capa de Servicio (Business Logic)
Patrón Singleton en InventarioServiceImpl
javapublic class InventarioServiceImpl implements InventarioServiceLocal {
    
    private static InventarioServiceImpl instancia;
    private final ProductoDAO productoDAO;
    
    // Constructor privado
    private InventarioServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }
    
    // Método estático para obtener instancia única
    public static synchronized InventarioServiceImpl getInstancia() {
        if (instancia == null) {
            instancia = new InventarioServiceImpl();
        }
        return instancia;
    }
    
    // Métodos de negocio...
}
Métodos del Servicio
java// Alta de producto
void agregarProducto(String nombre, Double precio, Integer stock);

// Listado completo
List<Producto> listarInventario();

// Actualización de precio
void actualizarPrecio(Integer id, Double precio);

// Actualización de stock
void actualizarStock(Integer id, Integer stock);

// Baja de producto
void eliminarProducto(Integer id);

// Búsqueda
Producto buscarProductoPorNombre(String nombre);

🔧 Configuración de Conexión (Singleton)
ConexionDB.java
javapublic class ConexionDB {
    
    private static ConexionDB instancia;
    private Connection conexion;
    
    private static final String URL = "jdbc:mysql://localhost:3306/minitienda_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Qwe.123*";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Constructor privado
    private ConexionDB() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
    
    // Obtener instancia única
    public static synchronized ConexionDB getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new ConexionDB();
        }
        return instancia;
    }
    
    // Obtener conexión
    public Connection getConexion() {
        return conexion;
    }
    
    // Cerrar conexión
    public void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
}

🐛 Solución de Problemas
Error: "Could not connect to database"
Causa: MySQL no está ejecutándose o credenciales incorrectas
Solución:
bash# Verificar estado de MySQL
sudo systemctl status mysql  # Linux
net start MySQL              # Windows

# Verificar credenciales en ConexionDB.java
URL = "jdbc:mysql://localhost:3306/minitienda_db"
USER = "root"
PASSWORD = "tu_password_aqui"

Error: "Driver MySQL no encontrado"
Causa: Dependencia MySQL no está en el classpath
Solución:

Verificar pom.xml:

xml<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

Ejecutar:

bashmvn clean install

Error: "JavaFX runtime components are missing"
Causa: JavaFX no está configurado correctamente
Solución:
Opción 1 - Maven:
bashmvn javafx:run
Opción 2 - VM Arguments:
bash--module-path /path/to/javafx-sdk-17/lib 
--add-modules javafx.controls,javafx.fxml

Error: "Duplicate entry for key 'nombre'"
Causa: Intento de agregar producto con nombre existente
Solución:

El sistema lanza DuplicadoException automáticamente
Cambiar el nombre del producto o eliminar el duplicado


Error: "Producto no encontrado con ID: X"
Causa: ID no existe en la base de datos
Solución:

Listar inventario para ver IDs válidos
Verificar que el producto no haya sido eliminado


📊 Diagrama de Flujo - Agregar Producto
┌─────────────────────────┐
│  Usuario click         │
│  "Agregar Producto"    │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Mostrar formulario     │
│  (Nombre, Precio,Stock) │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Usuario ingresa datos  │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Validar datos          │
│  (Producto.java)        │
└──────┬──────────────────┘
       │
       ├─────❌ DatoInvalidoException
       │         └─> Mostrar error
       │
       ▼
┌─────────────────────────┐
│  Service.agregarProducto│
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  DAO.insertar()         │
└──────┬──────────────────┘
       │
       ├─────❌ DuplicadoException
       │         └─> Mostrar error
       │
       ├─────❌ PersistenciaException
       │         └─> Mostrar error
       │
       ▼
┌─────────────────────────┐
│  ✅ Producto guardado   │
│  Incrementar totalAltas │
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  Mostrar mensaje éxito  │
└─────────────────────────┘

📈 Contadores de Operaciones
El sistema rastrea automáticamente:
javaprivate int totalAltas = 0;          // Productos agregados
private int totalBajas = 0;          // Productos eliminados
private int totalActualizaciones = 0; // Precios/Stocks actualizados
Se muestra al salir:
=== RESUMEN DE OPERACIONES ===

Productos agregados:     12
Productos eliminados:    3
Actualizaciones:         25
Total de operaciones:    40

🎨 Personalización de la UI
Estilos CSS Inline
java// Título del menú
titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

// Botones
boton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

// Área de resultados
areaResultados.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
Modificar Colores
java// En MainApp.java, agregar:
btnAgregar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
btnSalir.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");

🔐 Seguridad
Prácticas Implementadas

Prepared Statements ✅

Prevención de SQL Injection
Escape automático de caracteres especiales


Validación de Entrada ✅

Validación en capa de dominio
Validación en capa de servicio


Manejo de Excepciones ✅

No se exponen stack traces completos al usuario
Mensajes de error amigables


Conexión Singleton ✅

Control centralizado de conexiones
Prevención de conexiones múltiples



⚠️ Recomendaciones Adicionales
java// NO HACER (contraseña hardcodeada):
private static final String PASSWORD = "Qwe.123*";

// MEJOR (usar variables de entorno):
private static final String PASSWORD = System.getenv("DB_PASSWORD");

// O usar archivo de configuración:
Properties props = new Properties();
props.load(new FileInputStream("config.properties"));
String password = props.getProperty("db.password");

📚 Estructura de Clases Principales
Producto.java (Domain)
javapublic class Producto {
    private Integer id;
    private String nombre;
    private Double precio;
    private Integer stock;
    
    // Constructor con validaciones
    public Producto(String nombre, Double precio, Integer stock) {
        // Validaciones...
    }
    
    // Getters y Setters con validaciones
}
ProductoDAOImpl.java (DAO)
javapublic class ProductoDAOImpl implements ProductoDAO {
    
    @Override
    public void insertar(Producto producto) {
        // Prepared Statement
    }
    
    @Override
    public List<Producto> listarTodos() {
        // SELECT * FROM productos
    }
    
    // Otros métodos CRUD...
}
InventarioServiceImpl.java (Service)
javapublic class InventarioServiceImpl implements InventarioServiceLocal {
    
    private static InventarioServiceImpl instancia; // Singleton
    private final ProductoDAO productoDAO;
    
    private InventarioServiceImpl() {
        this.productoDAO = new ProductoDAOImpl();
    }
    
    public static synchronized InventarioServiceImpl getInstancia() {
        // Singleton implementation
    }
    
    // Métodos de negocio...
}
MainApp.java (UI)
javapublic class MainApp extends Application {
    
    private final InventarioServiceLocal service;
    private TextArea areaResultados;
    private int totalAltas, totalBajas, totalActualizaciones;
    
    public MainApp() {
        this.service = InventarioServiceImpl.getInstancia();
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Crear interfaz gráfica
    }
    
    // Métodos de eventos...
}

---

## 👨‍💻 Autor

**Desarrollador:** Luis Orozco  
**Versión:** 1.0.0  
**Fecha:** Octubre 2024  
**IDE:** NetBeans 27  
**Java:** 17

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
