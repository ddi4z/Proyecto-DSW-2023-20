package co.edu.uniandes.dse.series.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CategoriaDetailDTO extends CategoriaDTO {
    
    private List<SerieDTO> series = new ArrayList<>();

}
