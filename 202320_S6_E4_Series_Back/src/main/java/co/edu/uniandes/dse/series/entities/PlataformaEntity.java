package co.edu.uniandes.dse.series.entities;

import java.util.ArrayList;
import javax.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.List;
@Data
@Entity
public class PlataformaEntity extends BaseEntity {
        
    private String nombre;
    private String sitioWeb;
    private String logo;

    @PodamExclude
    @ManyToMany(
        mappedBy = "plataformas", 
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY 
    )
    private List<SerieEntity> seriesPlataforma = new ArrayList<>();

    @PodamExclude
    @OneToMany(
        mappedBy = "plataforma", 
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private List<PlanEntity> planes = new ArrayList<>();
}
