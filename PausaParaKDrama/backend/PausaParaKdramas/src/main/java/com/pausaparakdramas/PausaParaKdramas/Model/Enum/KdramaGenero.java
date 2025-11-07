package com.pausaparakdramas.PausaParaKdramas.Model.Enum;

public enum KdramaGenero {
    ROMANCE("Romance"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    MISTERIO("Misterio"),
    ACCION("Acción"),
    FANTASIA("Fantasía"),
    HISTORICO("Histórico"),
    SUSPENSE("Suspense"),
    AVENTURA("Aventura"),
    THRILLER("Thriller");

    private final String generoNombre;

    KdramaGenero(String generoNombre) {
        this.generoNombre = generoNombre;
    }

    public String getGeneroNombre() {
        return generoNombre;
    }

    @Override
    public String toString() {
        return generoNombre;
    }
}
