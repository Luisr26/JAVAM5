
package com.mycompany.minitienda.service.impl;



import com.mycompany.minitienda.domain.Producto;
import com.mycompany.minitienda.exceptions.DatoInvalidoException;
import com.mycompany.minitienda.exceptions.DuplicadoException;
import com.mycompany.minitienda.exceptions.PersistenciaException;
import com.mycompany.minitienda.DAO.ProductoDAO;
import com.mycompany.minitienda.DAO.jdbc.ProductoDAOImpl;
import com.mycompany.minitienda.service.InventarioServiceLocal;
import java.util.List;

/**
 * Implementación del servicio de inventario.
 * Contiene la lógica de negocio y validaciones antes de delegar al DAO.
 * Propaga excepciones personalizadas hacia la capa de presentación.
 */
public class InventarioServiceImpl implements InventarioServiceLocal {
    
    private static InventarioServiceImpl instancia;
    private final ProductoDAO productoDAO;
    
    // Constructor privado para Singleton
    private InventarioServiceImpl() {
        this.productoDAO = ProductoDAOImpl.getInstancia();
    }
    
    public static InventarioServiceImpl getInstancia() {
        if (instancia == null) {
            synchronized (InventarioServiceImpl.class) {
                if (instancia == null) {
                    instancia = new InventarioServiceImpl();
                }
            }
        }
        return instancia;
    }

    @Override
    public void agregarProducto(String nombre, Double precio, Integer stock) 
            throws DatoInvalidoException, DuplicadoException, PersistenciaException {
        
        // Validaciones de negocio
        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);
        
        // Verificar si ya existe un producto con ese nombre
        if (productoDAO.existePorNombre(nombre)) {
            throw new DuplicadoException("Ya existe un producto con el nombre: " + nombre);
        }
        
        // Crear y persistir el producto
        Producto producto = new Producto(nombre, precio, stock);
        productoDAO.insertar(producto);
    }

    @Override
    public List<Producto> listarInventario() throws PersistenciaException {
        return productoDAO.listarTodos();
    }

    @Override
    public void actualizarPrecio(Integer id, Double nuevoPrecio) 
            throws DatoInvalidoException, PersistenciaException {
        
        validarPrecio(nuevoPrecio);
        productoDAO.actualizarPrecio(id, nuevoPrecio);
    }

    @Override
    public void actualizarStock(Integer id, Integer nuevoStock) 
            throws DatoInvalidoException, PersistenciaException {
        
        validarStock(nuevoStock);
        productoDAO.actualizarStock(id, nuevoStock);
    }

    @Override
    public void eliminarProducto(Integer id) throws PersistenciaException {
        productoDAO.eliminar(id);
    }

    @Override
    public Producto buscarProductoPorNombre(String nombre) throws PersistenciaException {
        return productoDAO.buscarPorNombre(nombre);
    }
    
    // Métodos privados de validación
    
    /**
     * Valida que el nombre no sea nulo ni vacío
     */
    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del producto no puede estar vacío");
        }
    }
    
    /**
     * Valida que el precio sea mayor a cero
     */
    private void validarPrecio(Double precio) throws DatoInvalidoException {
        if (precio == null || precio <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor a cero");
        }
    }
    
    /**
     * Valida que el stock no sea negativo
     */
    private void validarStock(Integer stock) throws DatoInvalidoException {
        if (stock == null || stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo");
        }
    }
}