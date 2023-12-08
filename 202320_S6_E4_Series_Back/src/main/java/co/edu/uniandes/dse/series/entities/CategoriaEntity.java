package co.edu.uniandes.dse.series.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
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

public class CategoriaEntity extends BaseEntity {

    private String nombre;
    private String imagen;

    @PodamExclude
    @ManyToMany(
        mappedBy = "categorias", 
        cascade = CascadeType.ALL, 
        fetch = FetchType.LAZY
    )
    private List<SerieEntity> series = new ArrayList<>();
    
}
