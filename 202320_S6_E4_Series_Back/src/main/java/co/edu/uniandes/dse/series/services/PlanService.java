package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.PlanEntity;
import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.PlanRepository;
import co.edu.uniandes.dse.series.repositories.PlataformaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlanService{
    
    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlataformaRepository plataformaRepository;


    //Servicio para crear un plan dentro de una plataforma
    @Transactional
    public PlanEntity createPlan(Long plataformaId, PlanEntity planEntity)
    throws IllegalOperationException, EntityNotFoundException{
        log.info("Inicia proceso de creacion de un plan");
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        planEntity.setPlataforma(plataformaEntity.get());
        if(planEntity.getNombre() == null || planEntity.getNombre().isEmpty()) throw new IllegalOperationException("nombre invalido");
        if(planEntity.getPrecio()==null || planEntity.getPrecio() < 0) throw new IllegalOperationException("Precio invalido");
        if(planEntity.getImagen()==null || planEntity.getImagen().isEmpty()) throw new IllegalOperationException("Imagen invalida");
        if(planEntity.getPuntaje()==null || planEntity.getPuntaje() < 0) throw new IllegalOperationException("Puntaje invalido");
        log.info("Termina proceso de creacion de un libro");
        return planRepository.save(planEntity);
    }

    //Servicio para consultar los planes de una plataforma
    @Transactional
    public List<PlanEntity> getPlanes(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes asociados a la plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        log.info("Termina proceso de consultar los planes asociados a la plataforma con id = {0}", plataformaId);
        return plataformaEntity.get().getPlanes();
    }

    //Servicio para ordenar planes por precio creciente
    @Transactional
    public List<PlanEntity> getPlanesOrdenadosPrecioCreciente(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes ordenados por precio de menor a mayor asociados a la plataforma con id ={0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        List<PlanEntity> planes = plataformaEntity.get().getPlanes();
        planes.sort((p1,p2) -> p1.getPrecio().compareTo(p2.getPrecio()));
        log.info("Termina proceso de consultar los planes por precio de menor a mayor asociados a la plataforma con id ={0}", plataformaId);
        return planes;
    }

    //Servicio para ordenar planes por precio decreciente
    @Transactional
    public List<PlanEntity> getPlanesOrdenadosPrecioDecreciente(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes ordenados por precio de mayor a menor asociados a la plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        List<PlanEntity> planes = plataformaEntity.get().getPlanes();
        planes.sort((p1,p2) -> p2.getPrecio().compareTo(p1.getPrecio()));
        log.info("Termina proceso de consultar los planes ordenados por precio de mayor a menor asociados a la plataforma con id = {0}", plataformaId);
        return planes;
    }

    //Servicio para ordenar planes por puntaje creciente
    @Transactional
    public List<PlanEntity> getPlanesOrdenadosPuntajeCreciente(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes ordenados por puntaje de menor a mayor asociados a la plataforma con id ={0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        List<PlanEntity> planes = plataformaEntity.get().getPlanes();
        planes.sort((p1,p2) -> p1.getPuntaje().compareTo(p2.getPuntaje()));
        log.info("Termina proceso de consultar los planes por puntaje de menor a mayor asociados a la plataforma con id = {0}", plataformaId);
        return planes;
    }

    //Servicio para ordenra planes por puntaje decreciente
    @Transactional
    public List<PlanEntity> getPlanesOrdenadosPuntajeDecreciente(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes ordenados por puntaje de mayor a menor asociados a la plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        List<PlanEntity> planes = plataformaEntity.get().getPlanes();
        planes.sort((p1,p2) -> p2.getPuntaje().compareTo(p1.getPuntaje()));
        log.info("Termina proceso de consultar los planes por puntaje de mayor a menor asociados a la plataforma con id ={0}", plataformaId);
        return planes;
    }

    //Servicio para ordenar planes alfabeticamente 
    @Transactional
    public List<PlanEntity> getPlanesOrdenadosAlfabeticamente(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar los planes ordenados alfabeticamente, asociados a la plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        List<PlanEntity> planes = plataformaEntity.get().getPlanes();
        planes.sort((p1,p2) -> p2.getNombre().compareTo(p1.getNombre()));
        log.info("Termina proceso de consultar los planes ordenados alfabeticamente asociados a la plataforma con id = {0}", plataformaId);
        return planes;
    }

    @Transactional
    public PlanEntity getPlan(Long plataformaId, Long planId) throws EntityNotFoundException { 
        log.info("Inicia proceso de consultar el plan con id = {0} de la plataforma con id = {1}", planId, plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        Optional<PlanEntity> planEntity = planRepository.findById(planId);
        if(planEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLAN_NOT_FOUND);
        log.info("Termina proceso de consultar el plan con id = {0} de la plataforma con id = {1}", planId, plataformaId);
        return planRepository.findByPlataformaIdAndId(plataformaId, planId);
    }

    @Transactional
    public PlanEntity updatePlanEntity(Long plataformaId, Long planId, PlanEntity plan) throws EntityNotFoundException, IllegalOperationException{
        log.info("Inicia proceso de actualizar el plan con id = {0} de la plataforma con id = {1}", planId, plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        Optional<PlanEntity> planEntity = planRepository.findById(planId);
        if(planEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLAN_NOT_FOUND);
        if(plan.getNombre() == null || plan.getNombre().isEmpty()) throw new IllegalOperationException("Nombre invalido");
        if(plan.getPrecio() == null || plan.getPrecio() < 0) throw new IllegalOperationException("Precio invalido");
        if(plan.getImagen() == null || plan.getImagen().isEmpty()) throw new IllegalOperationException("Imagen invalida");
        if(plan.getPuntaje() == null || plan.getPuntaje() < 0) throw new IllegalOperationException("Puntaje invalido");
        plan.setId(planId);
        plan.setPlataforma(plataformaEntity.get());
        log.info("Termina proceso de actualizar el plan con id = {0}", planId);
        return planRepository.save(plan);
    }

    @Transactional
    public void deletePlan(Long plataformaId, Long planId) throws EntityNotFoundException{
        log.info("Inicia proceso de borrar el plan con id = {0} de la plataforma con id = {1}", planId, plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        PlanEntity plan = getPlan(plataformaId, planId);
        if(plan == null) throw new EntityNotFoundException(ErrorMessage.PLAN_NOT_FOUND);
        planRepository.deleteById(planId);
        log.info("Termina el proceso de borrar el plan con id = {0} de la plataforma con id = {1}", planId, plataformaId);
    }
}