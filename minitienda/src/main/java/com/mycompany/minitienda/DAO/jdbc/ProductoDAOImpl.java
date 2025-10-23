
package com.mycompany.minitienda.DAO.jdbc;



import com.mycompany.minitienda.domain.Producto;
import com.mycompany.minitienda.exceptions.PersistenciaException;
import com.mycompany.minitienda.infra.config.ConexionDB;
import com.mycompany.minitienda.DAO.ProductoDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAOImpl implements ProductoDAO {
    
    private static ProductoDAOImpl instancia;
    
    // Constructor privado para Singleton
    private ProductoDAOImpl() {
    }
    
    public static ProductoDAOImpl getInstancia() {
        if (instancia == null) {
            synchronized (ProductoDAOImpl.class) {
                if (instancia == null) {
                    instancia = new ProductoDAOImpl();
                }
            }
        }
        return instancia;
    }

    @Override
    public void insertar(Producto producto) throws PersistenciaException {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar el producto en la base de datos", e);
        }
    }

    @Override
    public List<Producto> listarTodos() throws PersistenciaException {
        String sql = "SELECT id, nombre, precio, stock FROM productos";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los productos de la base de datos", e);
        }
        
        return productos;
    }

    @Override
    public void actualizarPrecio(Integer id, Double nuevoPrecio) throws PersistenciaException {
        String sql = "UPDATE productos SET precio = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, nuevoPrecio);
            stmt.setInt(2, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el precio del producto", e);
        }
    }

    @Override
    public void actualizarStock(Integer id, Integer nuevoStock) throws PersistenciaException {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el stock del producto", e);
        }
    }

    @Override
    public void eliminar(Integer id) throws PersistenciaException {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new PersistenciaException("No se encontró el producto con ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el producto de la base de datos", e);
        }
    }

    @Override
    public Producto buscarPorNombre(String nombre) throws PersistenciaException {
        String sql = "SELECT id, nombre, precio, stock FROM productos WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getDouble("precio"));
                    producto.setStock(rs.getInt("stock"));
                    return producto;
                }
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el producto por nombre", e);
        }
        
        return null;
    }

    @Override
    public boolean existePorNombre(String nombre) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Error al verificar existencia del producto", e);
        }
        
        return false;
    }

    public void insertar(Object producto) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
