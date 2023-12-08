package co.edu.uniandes.dse.series.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.series.entities.EpisodioEntity;

@Repository
public interface EpisodioRepository extends JpaRepository<EpisodioEntity, Long>{
    Optional<EpisodioEntity> findByNombre(String nombre);
    Optional<EpisodioEntity> findByNumeroEpisodio(int numero);
    
}
