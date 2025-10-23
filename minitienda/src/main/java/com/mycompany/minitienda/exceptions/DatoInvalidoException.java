
package com.mycompany.minitienda.exceptions;


public class DatoInvalidoException extends Exception {
    
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
    
    public DatoInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
