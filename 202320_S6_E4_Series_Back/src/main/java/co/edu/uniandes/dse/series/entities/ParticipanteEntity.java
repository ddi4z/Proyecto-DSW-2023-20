package co.edu.uniandes.dse.series.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.List;
/**
 * Clase que representa un libro en la persistencia
 *
 * @author ISIS2603
 */

@Data
@Entity
public class ParticipanteEntity extends BaseEntity {

        private String nombre;
        @Column(length = 1000)
        private String foto;
        @Column(length = 1000)
        private String descripcion;

        @PodamExclude
        @ManyToMany(
                mappedBy = "actores", 
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY
        )
        private List<SerieEntity> seriesActuadas = new ArrayList<>();

        @PodamExclude
        @ManyToMany(
                mappedBy = "directores", 
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY
        )
        private List<SerieEntity> seriesDirigidas = new ArrayList<>();

}