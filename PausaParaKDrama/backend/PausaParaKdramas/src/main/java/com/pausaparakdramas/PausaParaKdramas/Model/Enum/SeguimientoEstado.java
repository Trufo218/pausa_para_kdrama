package com.pausaparakdramas.PausaParaKdramas.Model.Enum;

public enum SeguimientoEstado {
    VIENDO("Viendo"),
    FINALIZADO("Finalizado"),
    PENDIENTE("Pendiente");

    private final String nombre;

    SeguimientoEstado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
