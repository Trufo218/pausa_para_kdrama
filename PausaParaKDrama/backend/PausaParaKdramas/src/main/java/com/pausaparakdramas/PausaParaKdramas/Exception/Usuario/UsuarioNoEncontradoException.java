package com.pausaparakdramas.PausaParaKdramas.Exception.Usuario;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus( value =HttpStatus.NOT_FOUND, reason = "Usuario no encontrado mediante su ID.")
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String message) {
        super(message);
    }
}

