package co.edu.uniandes.dse.series.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ParticipanteDetailDTO extends ParticipanteDTO {
    
    private List<SerieDTO> seriesActuadas = new ArrayList<>();
    private List<SerieDTO> seriesDirigidas = new ArrayList<>();

}
