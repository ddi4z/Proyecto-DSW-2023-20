package co.edu.uniandes.dse.series.dto;

import java.util.List;
import java.util.ArrayList;

import lombok.Data;

@Data
public class PlataformaDetailDTO extends PlataformaDTO {
    private List<SerieDTO> seriesPlataforma = new ArrayList<>();
    private List<PlanDTO> planes = new ArrayList<>();
}
