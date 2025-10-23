
package com.mycompany.minitienda.DAO;


import com.mycompany.minitienda.domain.Producto;
import com.mycompany.minitienda.exceptions.PersistenciaException;
import java.util.List;


public interface ProductoDAO {
    
   
    void insertar(Producto producto) throws PersistenciaException;
    
    /**
     * Obtiene todos los productos del inventario
     * @return Lista de productos
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    List<Producto> listarTodos() throws PersistenciaException;
    
    /**
     * Actualiza el precio de un producto
     * @param id ID del producto
     * @param nuevoPrecio Nuevo precio
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    void actualizarPrecio(Integer id, Double nuevoPrecio) throws PersistenciaException;
    
    /**
     * Actualiza el stock de un producto
     * @param id ID del producto
     * @param nuevoStock Nuevo stock
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    void actualizarStock(Integer id, Integer nuevoStock) throws PersistenciaException;
    
    /**
     * Elimina un producto de la base de datos
     * @param id ID del producto a eliminar
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    void eliminar(Integer id) throws PersistenciaException;
    
    /**
     * Busca un producto por nombre
     * @param nombre Nombre del producto
     * @return Producto encontrado o null
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    Producto buscarPorNombre(String nombre) throws PersistenciaException;
    
    /**
     * Verifica si existe un producto con el nombre dado
     * @param nombre Nombre a verificar
     * @return true si existe, false en caso contrario
     * @throws PersistenciaException si ocurre un error en la base de datos
     */
    boolean existePorNombre(String nombre) throws PersistenciaException;
}
