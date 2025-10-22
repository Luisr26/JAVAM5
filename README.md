# 🏪 Sistema de Gestión de Inventario - Mini-Tienda

## 📋 Descripción

Sistema de gestión de inventario desarrollado en **Java 17** con interfaz gráfica **JavaFX**, que permite la administración completa de productos mediante operaciones CRUD, importación/exportación de archivos CSV y procesamiento asíncrono para archivos grandes.

---

## 🎯 Características Principales

### ✨ Funcionalidades Core
- ✅ **CRUD Completo** de productos (Alta, Baja, Modificación, Consulta)
- 📂 **Importación CSV** con validación y procesamiento asíncrono
- 💾 **Exportación CSV** con/sin ID para compatibilidad con otros sistemas
- 🔍 **Búsqueda** de productos por nombre (coincidencia parcial)
- 📊 **Listado completo** del inventario
- 🗑️ **Eliminación masiva** con doble confirmación
- 📈 **Resumen de operaciones** por sesión

### 🚀 Características Técnicas
- ⚡ **Procesamiento asíncrono** con `javafx.concurrent.Task`
- 📊 **Barra de progreso** para operaciones pesadas
- ✔️ **Validaciones robustas** de datos
- 🛡️ **Manejo de excepciones** personalizado
- 🎨 **Interfaz moderna** con JavaFX
- 🔄 **Actualizaciones en tiempo real** del estado

---

## 🏗️ Arquitectura del Proyecto

### Patrón: **Arquitectura por Capas**

```
com.minitienda.minitienda
├─ modelo/                          # Capa de Dominio
│  └─ Producto.java                 # POJO del producto
│
├─ dao/                             # Capa de Acceso a Datos (Interfaces)
│  └─ ProductoDAO.java              
│
├─ dao/impl/                        # Implementación DAO
│  └─ ProductoDAOImpl.java          # Implementación JDBC
│
├─ servicio/                        # Capa de Negocio (Interfaces)
│  └─ IInventarioServicio.java      
│
├─ servicio/impl/                   # Implementación de Servicios
│  └─ ServicioInventario.java       # Lógica de negocio
│
├─ util/                            # Utilidades
│  ├─ ConexionBD.java               # Singleton para BD
│  ├─ CSVProductoProcessor.java     # Procesador CSV (importar)
│  └─ CSVProductoExporter.java      # Exportador CSV
│
├─ excepciones/                     # Excepciones Personalizadas
│  ├─ ProductoNoEncontradoException.java
│  ├─ ProductoDuplicadoException.java
│  └─ DatosInvalidosException.java
│
└─ MiniTiendaApp.java              # Aplicación JavaFX (UI)
```

### Patrón de Diseño: **Singleton**
- Aplicado en `ConexionBD.java` para garantizar una única instancia de conexión

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 17 | Lenguaje base |
| JavaFX | Incluido en JDK | Interfaz gráfica |
| MySQL | 8.0+ | Base de datos |
| JDBC | Nativo Java | Conectividad BD |
| NetBeans | 27 | IDE de desarrollo |

---

## 📦 Requisitos Previos

### Software Necesario
```bash
☑️ JDK 17 o superior
☑️ MySQL Server 8.0+
☑️ NetBeans IDE 27 (recomendado)
☑️ Maven (incluido en NetBeans)
```

### Dependencias Maven

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

---

## ⚙️ Configuración de Base de Datos

### 1. Crear Base de Datos

```sql
CREATE DATABASE IF NOT EXISTS minitienda_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE minitienda_db;
```

### 2. Crear Tabla de Productos

```sql
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL CHECK (stock >= 0),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Índices para optimización
CREATE INDEX idx_nombre ON productos(nombre);
CREATE INDEX idx_precio ON productos(precio);
```

### 3. Configurar Conexión

Editar `ConexionBD.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/minitienda_db";
private static final String USER = "root";
private static final String PASSWORD = "tu_password"; // Cambiar según tu configuración
```

---

## 🚀 Instalación y Ejecución

### Método 1: Con NetBeans

```bash
1. Clonar o descargar el proyecto
2. Abrir NetBeans 27
3. File → Open Project → Seleccionar carpeta del proyecto
4. Configurar la base de datos (ver sección anterior)
5. Click derecho en el proyecto → Build
6. Click derecho → Run
```

### Método 2: Línea de Comandos

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn javafx:run

# Generar JAR ejecutable
mvn clean package
java -jar target/minitienda-1.0.jar
```

---

## 📖 Manual de Usuario

### 🔵 Importar Productos desde CSV

**Formato del archivo CSV:**
```csv
nombre,precio,stock
Laptop Dell XPS,1299.99,15
Mouse Logitech,29.99,50
Teclado Mecánico,89.99,30
```

**Pasos:**
1. Click en **"📂 Cargar CSV"**
2. Seleccionar archivo `.csv`
3. Revisar resumen de validación
4. Confirmar carga
5. Esperar barra de progreso

**Validaciones automáticas:**
- ✅ Formato correcto de columnas
- ✅ Precios y stocks numéricos positivos
- ✅ Nombres únicos (duplicados)
- ⚠️ Líneas con errores se reportan pero no detienen la carga

---

### 💾 Exportar Productos a CSV

**Opciones de exportación:**

1. **Con ID** (formato completo):
```csv
id,nombre,precio,stock
1,Laptop Dell XPS,1299.99,15
2,Mouse Logitech,29.99,50
```

2. **Sin ID** (para importar en otros sistemas):
```csv
nombre,precio,stock
Laptop Dell XPS,1299.99,15
Mouse Logitech,29.99,50
```

**Pasos:**
1. Click en **"💾 Exportar CSV"**
2. Elegir formato (Con/Sin ID)
3. Seleccionar ubicación y nombre de archivo
4. Esperar confirmación

---

### ➕ Agregar Producto Manualmente

1. Click en **"➕ Agregar Producto"**
2. Ingresar datos:
   - **Nombre**: Único, no vacío
   - **Precio**: Número decimal positivo
   - **Stock**: Número entero positivo
3. Click en **"Agregar"**

---

### 📋 Listar Inventario

- Click en **"📋 Listar Inventario"**
- Muestra todos los productos con formato tabular
- Incluye total de productos al final

---

### 💰 Actualizar Precio

1. Click en **"💰 Actualizar Precio"**
2. Ingresar **ID** del producto
3. Ver precio actual
4. Ingresar nuevo precio
5. Confirmar cambio

---

### 📦 Actualizar Stock

1. Click en **"📦 Actualizar Stock"**
2. Ingresar **ID** del producto
3. Ver stock actual
4. Ingresar nuevo stock
5. Confirmar cambio

---

### 🗑️ Eliminar Producto Individual

1. Click en **"🗑️ Eliminar Producto"**
2. Ingresar **ID** del producto
3. Revisar datos del producto
4. Confirmar eliminación

---

### 🗑️ Eliminar Todos los Productos

⚠️ **OPERACIÓN IRREVERSIBLE**

1. Click en **"🗑️ ELIMINAR TODO"**
2. Leer advertencia
3. Primera confirmación
4. Segunda confirmación (escribir **"ELIMINAR"**)
5. Procesamiento automático

💡 **Recomendación:** Exportar CSV antes de eliminar todo

---

### 🔍 Buscar por Nombre

1. Click en **"🔍 Buscar por Nombre"**
2. Ingresar nombre o fragmento
3. Ver resultados (búsqueda parcial habilitada)

**Ejemplo:**
- Buscar: "mouse" → Encuentra "Mouse Logitech", "Mouse Inalámbrico", etc.

---

## 📊 Resumen de Sesión

Al hacer click en **"🚪 Salir"**, se muestra:

```
══════════════════════════════════════
📈 Operaciones de Alta:          25
📉 Operaciones de Baja:          3
🔄 Operaciones de Actualización: 12
══════════════════════════════════════
TOTAL DE OPERACIONES: 40
```

---

## 🔒 Validaciones Implementadas

### Validaciones de Datos
| Campo | Validación |
|-------|-----------|
| Nombre | No vacío, único en BD |
| Precio | Numérico, >= 0 |
| Stock | Entero, >= 0 |
| ID | Existente en BD |

### Validaciones de CSV
- ✅ Formato de 3 columnas (nombre,precio,stock)
- ✅ Encabezados correctos
- ✅ Datos numéricos válidos
- ✅ Detección de duplicados
- ⚠️ Reporte de líneas con errores

---

## 🛡️ Manejo de Excepciones

### Excepciones Personalizadas

```java
ProductoNoEncontradoException    // Producto no existe
ProductoDuplicadoException       // Nombre duplicado
DatosInvalidosException          // Validación fallida
ConexionBDException              // Error de conexión
```

### Excepciones Nativas Java
- `SQLException` - Errores de base de datos
- `NumberFormatException` - Conversión numérica fallida
- `IOException` - Errores de lectura/escritura CSV
- `IllegalArgumentException` - Argumentos inválidos

---

## 🎨 Interfaz Gráfica

### Componentes Principales
- **BorderPane** como contenedor principal
- **TextArea** para resultados
- **ProgressBar** para operaciones largas
- **Buttons** con estilo personalizado
- **Dialogs** modales para entrada de datos
- **FileChooser** para selección de archivos

### Colores y Estilo
```
🟢 Verde (#4caf50)   - Cargar CSV
🔵 Azul (#2196f3)    - Exportar CSV
🔴 Rojo (#d32f2f)    - Salir
🟠 Naranja (#ff5722) - Eliminar Todo
```

---

## 📁 Estructura de Archivos CSV

### Archivo de Importación Válido

```csv
nombre,precio,stock
Laptop Gaming ASUS ROG,1899.99,8
Monitor LG 27",349.99,20
Teclado Mecánico RGB,129.99,35
Mouse Gamer Razer,79.99,42
Auriculares HyperX,99.99,28
```

### Errores Comunes en CSV

❌ **Incorrecto:**
```csv
producto,valor,cantidad  # Encabezados incorrectos
Laptop,mil doscientos,10  # Precio no numérico
,500.00,20                # Nombre vacío
Mouse Gamer,-30.00,15     # Precio negativo
```

✅ **Correcto:**
```csv
nombre,precio,stock
Laptop,1200.00,10
Mouse Gamer,30.00,15
```

---

## 🐛 Solución de Problemas

### Error: "Could not connect to database"

**Solución:**
1. Verificar que MySQL esté ejecutándose
2. Validar credenciales en `ConexionBD.java`
3. Confirmar que existe la base de datos `minitienda_db`

```bash
# Verificar MySQL
sudo systemctl status mysql  # Linux
net start MySQL              # Windows
```

---

### Error: "No suitable driver found"

**Solución:**
1. Agregar dependencia MySQL en `pom.xml`
2. Ejecutar `mvn clean install`
3. Verificar que el JAR de MySQL esté en el classpath

---

### Error: "JavaFX runtime components are missing"

**Solución:**
```bash
# Ejecutar con argumentos VM
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

O usar Maven:
```bash
mvn javafx:run
```

---

### Archivo CSV no carga

**Verificar:**
- ✅ Codificación UTF-8
- ✅ Separador de comas (`,`)
- ✅ Formato: nombre,precio,stock
- ✅ Sin líneas vacías al inicio
- ✅ Encabezados exactos

---

## 📈 Rendimiento

### Optimizaciones Implementadas

| Operación | Estrategia | Beneficio |
|-----------|-----------|-----------|
| Carga CSV | `Task` asíncrono | No bloquea UI |
| Exportación | Streaming | Bajo uso de memoria |
| Consultas | Índices BD | Búsquedas rápidas |
| Validación | Lotes | Procesa múltiples registros |

### Capacidades
- ✅ Importa **10,000+** productos sin problemas
- ✅ Exporta archivos de **varios MB** sin congelar UI
- ✅ Búsquedas instantáneas con índices

---

## 🔐 Seguridad

### Buenas Prácticas Implementadas

1. **Prepared Statements** - Prevención SQL Injection
2. **Validación de entrada** - Sanitización de datos
3. **Transacciones** - Consistencia de datos
4. **Conexión Singleton** - Control de recursos
5. **Manejo de excepciones** - Sin exposición de stack traces

---

## 📝 Ejemplo de Uso Completo

```java
// 1. Importar productos masivamente
Click "📂 Cargar CSV" → productos_inicial.csv → ✅ 150 productos cargados

// 2. Agregar producto individual
Click "➕ Agregar Producto"
  Nombre: "SSD Samsung 1TB"
  Precio: 189.99
  Stock: 25

// 3. Buscar productos
Click "🔍 Buscar por Nombre" → "samsung" → 🔍 5 resultados

// 4. Actualizar precio
Click "💰 Actualizar Precio" → ID: 15 → Precio: 179.99

// 5. Exportar respaldo
Click "💾 Exportar CSV" → productos_backup_20241010.csv

// 6. Ver inventario
Click "📋 Listar Inventario" → 151 productos

// 7. Salir con resumen
Click "🚪 Salir" → Ver estadísticas de sesión
```

---

## 🤝 Contribuciones

Para contribuir al proyecto:

1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/NuevaFuncionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/NuevaFuncionalidad`)
5. Abrir Pull Request

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 👨‍💻 Autor

**Desarrollador:** Luis Orozco  
**Versión:** 1.0.0  
**Fecha:** Octubre 2024  
**IDE:** NetBeans 27  
**Java:** 17  

---

## 📞 Soporte y Contacto

Para reportar bugs, solicitar características o consultas:

- 📧 **Email:** luisoro009@gmail.com
- 📱 **Teléfono/WhatsApp:** +57 304 523 3125
- 🐛 **Issues:** GitHub Issues
- 📚 **Documentación:** Wiki del proyecto

---

## 🔄 Changelog

### v1.0.0 (2024-10-10)
- ✨ Release inicial
- ✅ CRUD completo de productos
- ✅ Importación/Exportación CSV
- ✅ Procesamiento asíncrono
- ✅ Validaciones robustas
- ✅ Interfaz gráfica JavaFX
- ✅ Manejo de excepciones personalizado

---

## 🎯 Roadmap Futuro

- [ ] Reportes PDF
- [ ] Gráficas de inventario
- [ ] Categorías de productos
- [ ] Sistema de usuarios y permisos
- [ ] Historial de cambios (auditoría)
- [ ] Integración con código de barras
- [ ] API REST
- [ ] Notificaciones de stock bajo

---

**¡Gracias por usar Mini-Tienda! 🏪✨**

**Desarrollado con ❤️ por Luis Orozco**