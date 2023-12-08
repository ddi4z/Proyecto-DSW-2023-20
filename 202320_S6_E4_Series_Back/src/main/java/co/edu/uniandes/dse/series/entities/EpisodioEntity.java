package co.edu.uniandes.dse.series.entities;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@Entity
public class EpisodioEntity extends BaseEntity {
    private Integer numeroEpisodio;
    private String nombre;
    @Column(length = 1000)
    private String resumen;
    private Integer duracionMinutos;
    private String imagen;

    @PodamExclude
    @ManyToOne(
        cascade = CascadeType.PERSIST,
        fetch = FetchType.LAZY
    )
    SerieEntity serie;
}
