# 📚 Sistema Académico de Gestión de Estudiantes

Sistema de gestión académica desarrollado en Java que permite registrar estudiantes, calcular promedios y generar estadísticas utilizando una arquitectura por capas con interfaz gráfica Swing.

## 🎯 Características

- ✅ Registro de estudiantes con información personal y académica
- 📊 Cálculo automático de promedios individuales y generales
- 🔍 Búsqueda de estudiantes por ID
- 📈 Generación de estadísticas académicas (aprobados, reprobados, porcentajes)
- 🖥️ Interfaz gráfica intuitiva con pestañas organizadas
- ✨ Validación de datos y manejo de errores

## 🏗️ Arquitectura

El proyecto utiliza una **arquitectura por capas** con **inyección de dependencias**:

```
com.codeup.academico/
├── Domain/              # Capa de dominio (entidades)
│   ├── Estudiante.java
│   └── Nota.java
├── Services/            # Capa de servicios (lógica de negocio)
│   ├── CalculoService.java
│   └── RegistroEstudianteService.java
├── UI/                  # Capa de presentación
│   ├── AcademicSystemFrame.java
│   └── RegistroEstudianteFrame.java
└── App.java            # Punto de entrada
```

### Capas del Sistema

#### 1. **Capa de Dominio (Domain)**
Define las entidades del negocio:
- **Estudiante**: Representa a un estudiante con ID, nombre, edad, curso y notas
- **Nota**: Representa una calificación (0.0 - 5.0) con validación

#### 2. **Capa de Servicios (Services)**
Implementa la lógica de negocio:
- **CalculoService**: Maneja todos los cálculos académicos
  - Cálculo de promedios individuales
  - Cálculo de promedio general
  - Determinación de estado académico (Aprobado/Reprobado)
  
- **RegistroEstudianteService**: Gestiona el registro de estudiantes
  - Agregar estudiantes (con validación de duplicados)
  - Listar estudiantes
  - Buscar por ID
  - Generar estadísticas

#### 3. **Capa de Presentación (UI)**
Gestiona la interacción con el usuario:
- **AcademicSystemFrame**: Interfaz gráfica principal con Swing
- **RegistroEstudianteFrame**: Interfaz de consola (alternativa)

## 🚀 Requisitos

- **Java**: JDK 11 o superior
- **IDE recomendado**: NetBeans, IntelliJ IDEA o Eclipse
- **Sistema operativo**: Windows, Linux o macOS

## 📦 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/sistema-academico.git
cd sistema-academico
```

2. Compila el proyecto:
```bash
javac -d bin src/com/codeup/academico/**/*.java
```

3. Ejecuta la aplicación:
```bash
java -cp bin com.codeup.academico.App
```

### Usando un IDE

1. Abre el proyecto en tu IDE favorito
2. Asegúrate de que todas las dependencias estén configuradas
3. Ejecuta la clase `App.java`

## 💻 Uso

### Interfaz Gráfica

Al iniciar la aplicación, verás una interfaz con 4 pestañas:

#### 1. **Registrar**
Formulario para agregar nuevos estudiantes:
- ID del estudiante (único)
- Curso
- Nombre completo
- Edad
- Tres notas (0.0 - 5.0)

#### 2. **Lista de Estudiantes**
Tabla que muestra todos los estudiantes registrados con:
- Información personal
- Notas individuales
- Promedio calculado
- Estado académico

#### 3. **Buscar Estudiante**
Búsqueda por ID con visualización detallada del estudiante.

#### 4. **Estadísticas**
Panel que muestra:
- Total de estudiantes
- Estudiantes aprobados/reprobados
- Promedio general
- Porcentaje de aprobación

## 📋 Reglas de Negocio

- **Nota aprobatoria**: 3.0 o superior
- **Rango de notas**: 0.0 - 5.0
- **Cantidad de notas**: Exactamente 3 por estudiante
- **ID único**: No se permiten estudiantes con IDs duplicados
- **Cálculo de promedio**: Suma de notas / 3

## 🎨 Ejemplo de Uso

```java
// Crear servicios
CalculoService calculoService = new CalculoService();
RegistroEstudianteService registroService = 
    new RegistroEstudianteService(calculoService);

// Crear estudiante
List<Nota> notas = List.of(
    new Nota(4.5),
    new Nota(3.8),
    new Nota(4.2)
);
Estudiante estudiante = new Estudiante("001", "Java Avanzado", 20, notas);

// Registrar
registroService.agregarEstudiante(estudiante);

// Calcular promedio
double promedio = calculoService.calcularPromedioEstudiante(estudiante);
System.out.println("Promedio: " + promedio); // 4.17

// Obtener estado
String estado = calculoService.obtenerEstadoAcademico(estudiante);
System.out.println("Estado: " + estado); // Aprobado
```

## 🧪 Validaciones

El sistema incluye validaciones en múltiples niveles:

- ✅ Notas entre 0.0 y 5.0
- ✅ Exactamente 3 notas por estudiante
- ✅ IDs únicos (sin duplicados)
- ✅ Campos obligatorios no vacíos
- ✅ Valores numéricos válidos para edad y notas

## 🛠️ Tecnologías Utilizadas

- **Java SE**: Lenguaje principal
- **Swing**: Framework para interfaz gráfica
- **Collections Framework**: Gestión de datos en memoria
- **GridBagLayout**: Diseño de formularios

## 🔄 Arquitectura por Capas - Beneficios

1. **Separación de responsabilidades**: Cada capa tiene un propósito específico
2. **Mantenibilidad**: Facilita cambios sin afectar otras capas
3. **Testabilidad**: Servicios independientes fáciles de probar
4. **Escalabilidad**: Permite agregar funcionalidades sin reestructurar
5. **Inyección de dependencias**: Bajo acoplamiento entre componentes

## 📝 Estructura de Datos

### Estudiante
```java
{
    id: "001",
    nombre: "Juan Pérez",
    edad: 20,
    curso: "Java Avanzado",
    notas: [
        {valor: 4.5},
        {valor: 3.8},
        {valor: 4.2}
    ]
}
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Fork el proyecto
2. Crea una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver