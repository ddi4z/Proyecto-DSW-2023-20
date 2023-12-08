package co.edu.uniandes.dse.series.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.List;

@Data
@Entity
public class SerieEntity extends BaseEntity {
    private String nombre;
    private String imagen;
    @Column(length = 1000)
    private String wallpaper;
    @Column(length = 1000)
    private String sinopsis;
    private Integer anio;

    @PodamExclude
    @ManyToMany(
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY
    )
    private List<CategoriaEntity> categorias = new ArrayList<>();

    @PodamExclude
    @ManyToMany(
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY
    )
    private List<ParticipanteEntity> directores = new ArrayList<>();

    @PodamExclude
    @ManyToMany(
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY
    )
    private List<ParticipanteEntity> actores = new ArrayList<>();

    @PodamExclude
    @OneToMany(
        mappedBy = "serie", 
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private List<EpisodioEntity> episodios = new ArrayList<>();

    @PodamExclude
    @ManyToMany(
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY
    )
    private List<PlataformaEntity> plataformas = new ArrayList<>();

}



