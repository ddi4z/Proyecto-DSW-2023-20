package co.edu.uniandes.dse.series.dto;

import lombok.Data;

@Data
public class EpisodioDTO {
    private Long id;
    private Integer numeroEpisodio;
    private String nombre;
    private String resumen;
    private Integer duracionMinutos;
    private String imagen;
    private SerieDTO serie;
}
