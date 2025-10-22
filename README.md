# 🏪 Sistema de Gestión de Inventario

Sistema avanzado de gestión de inventario de productos desarrollado en Java que implementa los 4 pilares de la Programación Orientada a Objetos (POO) con una interfaz gráfica Swing intuitiva y moderna.

## 🎯 Características Principales

- ✅ Gestión de múltiples tipos de productos (Alimentos y Electrodomésticos)
- 📊 Control de inventario con seguimiento de stock
- 💰 Sincronización automática de precios
- 📈 Generación de reportes detallados
- 🖥️ Interfaz gráfica con diálogos optimizados
- 🔄 Sistema de paginación para grandes inventarios
- ⚠️ Alertas de stock bajo
- ✏️ Modificación y eliminación de productos

## 🏗️ Arquitectura POO

El proyecto demuestra los **4 pilares de la Programación Orientada a Objetos**:

### 1. **Abstracción** 🎭
```
Producto (Clase Abstracta)
├── Alimento
└── Electrodoméstico
```

La clase abstracta `Producto` define el contrato base que todas las subclases deben cumplir, ocultando detalles de implementación específicos.

### 2. **Encapsulamiento** 🔒
Todos los atributos son privados con acceso controlado mediante getters y setters:
```java
private String nombre;
private double precio;

public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }
```

### 3. **Herencia** 👨‍👦
Las subclases heredan comportamiento y estado de la clase padre:
- `Alimento extends Producto` → Hereda nombre y precio, agrega fecha de vencimiento y categoría
- `Electrodomestico extends Producto` → Hereda nombre y precio, agrega marca, garantía y consumo

### 4. **Polimorfismo** 🦎
Mismo método, diferentes implementaciones:
```java
// En Alimento:
@Override
public String getDescripcion() {
    return "ALIMENTO: " + getNombre() + " | Categoría: " + categoria...;
}

// En Electrodoméstico:
@Override
public String getDescripcion() {
    return "ELECTRODOMÉSTICO: " + getNombre() + " | Marca: " + marca...;
}
```

## 📦 Estructura del Proyecto

```
com.codedown.products/
├── Producto.java              # Clase abstracta base
├── Alimento.java             # Subclase para alimentos
├── Electrodomestico.java     # Subclase para electrodomésticos
└── APP.java                  # Aplicación principal con GUI
```

### Clases del Sistema

#### **Producto** (Abstracta)
Clase base para todos los productos del sistema.

**Atributos:**
- `nombre`: String
- `precio`: double

**Métodos:**
- `getDescripcion()`: Método abstracto (polimorfismo)
- `toString()`: Representación en texto

#### **Alimento** (Concreta)
Representa productos alimenticios.

**Atributos adicionales:**
- `fechaVencimiento`: String (formato YYYY-MM-DD)
- `categoria`: String (lácteos, carnes, verduras, etc.)

#### **Electrodoméstico** (Concreto)
Representa productos electrodomésticos.

**Atributos adicionales:**
- `marca`: String
- `garantiaMeses`: int
- `consumoEnergetico`: double (en watts)

## 🚀 Requisitos

- **Java**: JDK 11 o superior
- **IDE recomendado**: NetBeans, IntelliJ IDEA o Eclipse
- **Sistema operativo**: Windows, Linux o macOS
- **Swing**: Incluido en JDK

## 📦 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/sistema-inventario.git
cd sistema-inventario
```

2. Compila el proyecto:
```bash
javac -d bin src/com/codedown/products/*.java
```

3. Ejecuta la aplicación:
```bash
java -cp bin com.codedown.products.APP
```

### Usando un IDE

1. Abre el proyecto en tu IDE
2. Asegúrate de que el paquete `com.codedown.products` esté configurado
3. Ejecuta la clase `APP.java`

## 💻 Uso del Sistema

### Menú Principal

Al iniciar la aplicación, verás un menú con las siguientes opciones:

#### 1. **Ver Inventario** 📋
- Visualiza todos los productos registrados
- Sistema de paginación (5 productos por página)
- Muestra: nombre, precio y stock disponible
- Navegación entre páginas

#### 2. **Agregar Alimento** 🥗
Registra un nuevo alimento con:
- Nombre del producto
- Precio
- Fecha de vencimiento (YYYY-MM-DD)
- Categoría (lácteos, carnes, verduras, etc.)
- Stock inicial

#### 3. **Agregar Electrodoméstico** 🔌
Registra un nuevo electrodoméstico con:
- Nombre del producto
- Precio
- Marca
- Garantía (en meses)
- Consumo energético (watts)
- Stock inicial

#### 4. **Modificar Producto** ✏️
Permite actualizar la información de productos existentes.

#### 5. **Eliminar Producto** 🗑️
Elimina productos del inventario.

#### 6. **Gestionar Stock** 📦
Administra las cantidades disponibles:
- Agregar stock
- Reducir stock
- Consultar niveles

#### 7. **Ver Stock** 📊
Visualiza el stock de todos los productos con:
- Alertas de stock bajo (< 5 unidades)
- Productos sin stock
- Resumen general

#### 8. **Sincronizar Precios** 🔄
Sincroniza los precios entre objetos y arrays:
- Detecta diferencias automáticamente
- Muestra cambios realizados
- Actualiza valores inconsistentes

#### 9. **Reportes** 📈
Genera estadísticas del inventario.

## 🎨 Características de la Interfaz

### Diálogos Cuadrados Optimizados
El sistema utiliza diálogos con formato consistente:
- **Tamaño fijo**: 350x120 px para inputs
- **Área de texto**: 30 columnas x 12 filas para mensajes
- **Padding uniforme**: 10px en todos los bordes
- **Paginación inteligente**: 5 items por página

### Sistema de Alertas
- ⚠️ **Stock Bajo**: < 5 unidades
- 🔴 **Sin Stock**: 0 unidades
- ✅ **Stock Normal**: ≥ 5 unidades

## 📋 Ejemplos de Uso

### Crear un Alimento
```java
Alimento leche = new Alimento(
    "Leche Entera",           // nombre
    3500.0,                   // precio
    "2025-01-15",            // fecha vencimiento
    "Lácteos"                // categoría
);
```

### Crear un Electrodoméstico
```java
Electrodomestico nevera = new Electrodomestico(
    "Nevera LG",             // nombre
    1500000.0,               // precio
    "LG",                    // marca
    24,                      // garantía (meses)
    150.0                    // consumo (watts)
);
```

### Polimorfismo en Acción
```java
// ArrayList puede contener diferentes tipos de productos
ArrayList<Producto> inventario = new ArrayList<>();
inventario.add(leche);          // Alimento
inventario.add(nevera);         // Electrodoméstico

// Mismo método, diferentes resultados
for (Producto p : inventario) {
    System.out.println(p.getDescripcion()); // Polimorfismo
}
```

## 🔄 Estructuras de Datos

### 1. ArrayList<Producto>
Almacena todos los productos del inventario:
```java
private static ArrayList<Producto> inventario = new ArrayList<>();
```

### 2. HashMap<String, Integer>
Asocia cada producto con su cantidad en stock:
```java
private static HashMap<String, Integer> stockProductos = new HashMap<>();
```

### 3. Array de Precios
Mantiene un registro paralelo de precios:
```java
private static double[] precios = new double[0];
```

## 🛠️ Tecnologías Utilizadas

- **Java SE**: Lenguaje principal
- **Swing**: Framework de interfaz gráfica
  - JOptionPane: Diálogos modales
  - JPanel: Contenedores
  - JTextArea: Áreas de texto
  - JTextField: Campos de entrada
- **Collections Framework**: 
  - ArrayList: Lista dinámica
  - HashMap: Mapeo clave-valor
- **BorderLayout & GridBagLayout**: Gestores de diseño

## 📊 Validaciones del Sistema

- ✅ Campos obligatorios no vacíos
- ✅ Valores numéricos válidos (precio, stock, garantía, consumo)
- ✅ Formato de fecha (YYYY-MM-DD)
- ✅ Nombres de productos únicos (prevención de duplicados)
- ✅ Stock no negativo
- ✅ Precios mayores a cero

## 🎯 Conceptos POO Demostrados

| Concepto | Implementación |
|----------|----------------|
| **Abstracción** | Clase `Producto` con método abstracto `getDescripcion()` |
| **Encapsulamiento** | Atributos privados con getters/setters públicos |
| **Herencia** | `Alimento` y `Electrodomestico` extienden `Producto` |
| **Polimorfismo** | Implementaciones diferentes de `getDescripcion()` |
| **Sobrescritura** | `@Override` en métodos de subclases |
| **Constructor de superclase** | `super(nombre, precio)` |

## 📝 Reglas de Negocio

- **Stock mínimo recomendado**: 5 unidades
- **Formato de fecha**: YYYY-MM-DD para alimentos
- **Garantía**: Expresada en meses para electrodomésticos
- **Consumo energético**: Expresado en watts (W)
- **Categorías de alimentos**: Libre definición (lácteos, carnes, verduras, etc.)

## 🔧 Personalización

### Agregar Nuevos Tipos de Productos

Para agregar un nuevo tipo de producto:

1. Crea una nueva clase que extienda `Producto`:
```java
public class Ropa extends Producto {
    private String talla;
    private String color;
    
    public Ropa(String nombre, double precio, String talla, String color) {
        super(nombre, precio);
        this.talla = talla;
        this.color = color;
    }
    
    @Override
    public String getDescripcion() {
        return "ROPA: " + getNombre() + " | Talla: " + talla + 
               " | Color: " + color + " | Precio: $" + getPrecio();
    }
}
```

2. Agrega la opción en el menú principal de `APP.java`
3. Crea el método `agregarRopa()` siguiendo el patrón existente

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

## 👨‍💻 Autor

**Luis Orozco** - Sistema de Gestión de Inventario CodeDown

## 🎓 Propósito Educativo

Este proyecto fue diseñado para demostrar:
- ✅ Los 4 pilares de la POO
- ✅ Uso de clases abstractas
- ✅ Herencia y polimorfismo
- ✅ Estructuras de datos en Java
- ✅ Desarrollo de interfaces gráficas con Swing
- ✅ Buenas prácticas de programación

## 📞 Soporte

Si encuentras problemas o tienes sugerencias:
- Abre un issue en GitHub
- Contacta al equipo de desarrollo

---

⭐ **Si este proyecto te ayudó a entender POO, ¡dale una estrella en GitHub!**

**Versión**: 1.0  
**Última actualización**: Octubre 2025  
**Java Version**: JDK 11+