package co.edu.uniandes.dse.series.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.series.entities.PlanEntity;



@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long>{
    List<PlanEntity> findByNombre(String nombre);
    PlanEntity findByPlataformaIdAndId(Long plataformaId, Long id);
}