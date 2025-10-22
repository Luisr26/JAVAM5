package com.minitienda.repositorio;

import com.minitienda.config.ConnectionFactory;
import com.minitienda.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorio implements Repositorio<Producto> {

    @Override
    public void crear(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos(nombre, precio, stock) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }
        }
    }
    
   
    public int crearEnLote(List<Producto> productos) throws SQLException {
        String sql = "INSERT INTO productos(nombre, precio, stock) VALUES (?, ?, ?)";
        int insertados = 0;
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false); // Desactivar autocommit para transacción
            
            try {
                for (Producto producto : productos) {
                    stmt.setString(1, producto.getNombre());
                    stmt.setDouble(2, producto.getPrecio());
                    stmt.setInt(3, producto.getStock());
                    stmt.addBatch();
                    
                  
                    if (++insertados % 1000 == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                }
                
              
                stmt.executeBatch();
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        
        return insertados;
    }

    @Override
    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, precio, stock FROM productos WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerProducto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscarTodos() throws SQLException {
        String sql = "SELECT id, nombre, precio, stock FROM productos ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productos.add(extraerProducto(rs));
            }
        }
        return productos;
    }

    @Override
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    
    public int eliminarTodos() throws SQLException {
        String sql = "DELETE FROM productos";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return stmt.executeUpdate();
        }
    }
    
   
    public int contarProductos() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM productos";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre, precio, stock FROM productos WHERE nombre LIKE ?";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extraerProducto(rs));
                }
            }
        }
        return productos;
    }
    
    private Producto extraerProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getDouble("precio"),
            rs.getInt("stock")
        );
    }
}