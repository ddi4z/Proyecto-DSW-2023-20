package co.edu.uniandes.dse.series.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.series.entities.PlataformaEntity;

@Repository
public interface PlataformaRepository extends JpaRepository<PlataformaEntity, Long> {
    List<PlataformaEntity> findByNombre(String nombre);
    List<PlataformaEntity> findBySitioWeb(String sitioWeb);
    List<PlataformaEntity> findByLogo(String logo);
}