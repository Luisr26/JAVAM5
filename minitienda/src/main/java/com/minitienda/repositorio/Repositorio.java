
package com.minitienda.repositorio;


import java.sql.SQLException;
import java.util.List;

public interface Repositorio<T> {
    void crear(T entidad) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> buscarTodos() throws SQLException;
    void actualizar(T entidad) throws SQLException;
    void eliminar(int id) throws SQLException;
}