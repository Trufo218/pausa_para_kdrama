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
    THRILLER("Thriller"),
    TERROR("Terror"),
    CRIMEN("Crimen"),
    CIENCIA_FICCION("Ciencia Ficción"),
    FAMILIAR("Familiar"),
    ANIMACION("Animación"),
    REALITY("Reality show"),
    DOCUMENTAL("Documental"),
    MUSICAL("Musical"),
    MELODRAMA("Melodrama"),
    GUERRA("Guerra"),
    DEPORTES("Deportes"),
    LGTBI("LGTBI+"),
    POLITICA("Política"),
    MEDICINA("Medicina"),
    ESCOLAR("Escolar"),
    LEGAL("Legal / Jurídico"),
    SUPERNATURAL("Supernatural"),
    PSICOLOGICO("Psicológico"),
    DESCONOCIDO("Desconocido");

    private final String generoNombre;

    KdramaGenero(String generoNombre) {
        this.generoNombre = generoNombre;
    }

    public String getGeneroNombre() {
        return generoNombre;
    }

    public static KdramaGenero fromTmdb(String tmdbGenre) {
        if (tmdbGenre == null) return DESCONOCIDO;

        tmdbGenre = tmdbGenre.trim().toLowerCase();

        switch (tmdbGenre) {

            case "romance": return ROMANCE;

            case "comedy":
            case "comedia": return COMEDIA;

            case "drama": return DRAMA;

            case "mystery":
            case "misterio": return MISTERIO;

            case "action":
            case "acción": return ACCION;

            case "fantasy":
            case "fantasía": return FANTASIA;

            case "history":
            case "historia":
            case "histórico": return HISTORICO;

            case "thriller": return THRILLER;

            case "adventure":
            case "aventura": return AVENTURA;

            case "horror":
            case "terror": return TERROR;

            case "crime":
            case "crimen": return CRIMEN;

            case "science fiction":
            case "ciencia ficción":
            case "sci-fi": return CIENCIA_FICCION;

            case "family":
            case "familiar": return FAMILIAR;

            case "animation":
            case "animación": return ANIMACION;

            case "reality":
            case "reality show":
            case "reality-tv": return REALITY;

            case "documentary":
            case "documental": return DOCUMENTAL;

            case "music":
            case "musical": return MUSICAL;

            case "melodrama": return MELODRAMA;

            case "war":
            case "guerra": return GUERRA;

            case "sports":
            case "deportes": return DEPORTES;

            case "lgbt":
            case "lgtbi":
            case "lgtbi+": return LGTBI;

            case "politics":
            case "política": return POLITICA;

            case "medical":
            case "medicina": return MEDICINA;

            case "school":
            case "escolar": return ESCOLAR;

            case "legal":
            case "law":
            case "jurídico": return LEGAL;

            case "supernatural":
            case "sobrenatural": return SUPERNATURAL;

            case "psychological":
            case "psicológico": return PSICOLOGICO;

            case "suspense": return SUSPENSE;
        }

        return DESCONOCIDO;
    }

    @Override
    public String toString() {
        return generoNombre;
    }
}
