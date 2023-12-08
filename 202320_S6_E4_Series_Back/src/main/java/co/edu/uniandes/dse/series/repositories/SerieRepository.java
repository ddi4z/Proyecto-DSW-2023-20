package co.edu.uniandes.dse.series.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.series.entities.SerieEntity;

@Repository
public interface SerieRepository extends JpaRepository<SerieEntity, Long> {
    List<SerieEntity> findByNombre(String nombre);
}