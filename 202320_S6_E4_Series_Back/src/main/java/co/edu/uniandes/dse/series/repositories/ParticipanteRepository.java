package co.edu.uniandes.dse.series.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.series.entities.ParticipanteEntity;

@Repository
public interface ParticipanteRepository extends JpaRepository<ParticipanteEntity, Long> {
        List<ParticipanteEntity> findByNombre(String nombre);
}