
package com.mycompany.minitienda.service;



import com.mycompany.minitienda.domain.Producto;
import com.mycompany.minitienda.exceptions.DatoInvalidoException;
import com.mycompany.minitienda.exceptions.DuplicadoException;
import com.mycompany.minitienda.exceptions.PersistenciaException;
import java.util.List;


public interface InventarioServiceLocal {
    
  
    void agregarProducto(String nombre, Double precio, Integer stock) 
            throws DatoInvalidoException, DuplicadoException, PersistenciaException;
    
   
    List<Producto> listarInventario() throws PersistenciaException;
 
    void actualizarPrecio(Integer id, Double nuevoPrecio) 
            throws DatoInvalidoException, PersistenciaException;
    
   
    void actualizarStock(Integer id, Integer nuevoStock) 
            throws DatoInvalidoException, PersistenciaException;
    
 
    void eliminarProducto(Integer id) throws PersistenciaException;
    
   
    Producto buscarProductoPorNombre(String nombre) throws PersistenciaException;
}