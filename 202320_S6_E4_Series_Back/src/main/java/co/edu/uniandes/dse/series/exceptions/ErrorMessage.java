package co.edu.uniandes.dse.series.exceptions;

public final class ErrorMessage {
	public static final String SERIE_NOT_FOUND = "Serie no encontrada";
    public static final String EPISODIO_NOT_FOUND = "Episodio no encontrado";
    public static final String CATEGORIA_NOT_FOUND = "Categoria no encontrada";
    public static final String PARTICIPANTE_NOT_FOUND = "Participante no encontrado";
    public static final String PLAN_NOT_FOUND = "Plan no encontrado";
    public static final String PLATAFORMA_NOT_FOUND = "Plataforma no encontrada";
    public static final String SERIE_CATEGORIA = "Inicia proceso de consultar las series asociadas a la categoria con id = ";
    public static final String EPISODIO_SERIE_NOT_FOUND = "El episodio no pertenece a la serie";
    public static final String INVALID_EPISODIO_NOMBRE = "El nombre del episodio no puede ser nulo o vacío";
    public static final String DIRECTOR_ASOCIADO = "No se puede borrar la serie con id porque tiene directores asociados";
    


	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}