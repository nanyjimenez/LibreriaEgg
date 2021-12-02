

package com.example.demo.Errores;



public class MisErrores extends Exception {
      
    /*Esta exception sirve para diferenciar los errores de nuestro código de los que ocurren en el sistema*/
    private static final long serialVersionUID = 7883636384872015753L;
    public MisErrores (String mensaje){
        super(mensaje);
    }
}

