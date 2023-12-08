package co.edu.uniandes.dse.series.dto;

import lombok.Data;
@Data
public class PlanDTO {
    private Long id;
    private String imagen;
    private String nombre;
    private Integer precio;
    private Integer puntaje;
    private PlataformaDTO plataforma;
}
