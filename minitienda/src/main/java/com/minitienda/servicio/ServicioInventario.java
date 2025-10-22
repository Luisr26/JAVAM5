package com.minitienda.servicio;

import com.minitienda.modelo.Producto;
import com.minitienda.repositorio.ProductoRepositorio;
import com.minitienda.repositorio.Repositorio;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestión de inventario de productos
 * @author Coder
 */
public class ServicioInventario {
    private final ProductoRepositorio repositorio;
    
    public ServicioInventario() {
        this.repositorio = new ProductoRepositorio();
    }
    
    public ServicioInventario(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void agregarProducto(String nombre, double precio, int stock) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        Producto producto = new Producto(nombre.trim(), precio, stock);
        repositorio.crear(producto);
    }

    public void actualizarPrecio(int id, double nuevoPrecio) throws SQLException {
        if (nuevoPrecio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        
        Producto producto = repositorio.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto con ID " + id + " no encontrado");
        }
        
        producto.setPrecio(nuevoPrecio);
        repositorio.actualizar(producto);
    }

    public void actualizarStock(int id, int nuevoStock) throws SQLException {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        Producto producto = repositorio.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto con ID " + id + " no encontrado");
        }
        
        producto.setStock(nuevoStock);
        repositorio.actualizar(producto);
    }

    public void eliminarProducto(int id) throws SQLException {
        Producto producto = repositorio.buscarPorId(id);
        if (producto == null) {
            throw new IllegalArgumentException("Producto con ID " + id + " no encontrado");
        }
        
        repositorio.eliminar(id);
    }

    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        
        return repositorio.buscarPorNombre(nombre.trim());
    }

    public List<Producto> listarTodos() throws SQLException {
        return repositorio.buscarTodos();
    }

    public Producto buscarPorId(int id) throws SQLException {
        return repositorio.buscarPorId(id);
    }
    
    /**
     * Carga productos desde CSV usando inserción por lotes (optimizado para archivos grandes)
     * @param productos Lista de productos a cargar
     * @return Número de productos cargados exitosamente
     * @throws SQLException Si hay error en la base de datos
     */
    public int cargarProductosDesdeCSV(List<Producto> productos) throws SQLException {
        if (productos == null || productos.isEmpty()) {
            throw new IllegalArgumentException("La lista de productos no puede estar vacía");
        }
        
        // Usar inserción en lote para mejor rendimiento con archivos grandes
        return repositorio.crearEnLote(productos);
    }
    
    /**
     * Elimina todos los productos de la base de datos
     * @return Número de productos eliminados
     * @throws SQLException Si hay error en la base de datos
     */
    public int eliminarTodosLosProductos() throws SQLException {
        return repositorio.eliminarTodos();
    }
    
    /**
     * Obtiene el conteo total de productos en la base de datos
     * @return Número total de productos
     * @throws SQLException Si hay error en la base de datos
     */
    public int contarProductos() throws SQLException {
        return repositorio.contarProductos();
    }
}