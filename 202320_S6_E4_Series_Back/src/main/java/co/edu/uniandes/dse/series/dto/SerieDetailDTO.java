package co.edu.uniandes.dse.series.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class SerieDetailDTO extends SerieDTO{
    private List<CategoriaDTO> categorias = new ArrayList<>();
    private List<ParticipanteDTO> directores = new ArrayList<>();
    private List<ParticipanteDTO> actores = new ArrayList<>();
    private List<EpisodioDTO> episodios = new ArrayList<>();
    private List<PlataformaDTO> plataformas = new ArrayList<>();
}
