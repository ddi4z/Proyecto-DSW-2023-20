package co.edu.uniandes.dse.series.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class PlanEntity extends BaseEntity{
    private String imagen;
    private String nombre;
    private Integer precio;
    private Integer puntaje;

    @PodamExclude
    @ManyToOne
    private PlataformaEntity plataforma;
}