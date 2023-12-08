package co.edu.uniandes.dse.series.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.series.dto.PlanDTO;
import co.edu.uniandes.dse.series.entities.PlanEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.PlanService;

@RestController
@RequestMapping("/plataforma")
public class PlanController{
    @Autowired private PlanService planService;
    @Autowired private ModelMapper modelMapper;

    //Metodo para devolver todos los planes de una plataforma
    @GetMapping(value = "/{plataforma_id}/planes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlanDTO> getPlanes(@PathVariable("plataforma_id") Long plataformaId) throws EntityNotFoundException{
        List<PlanEntity> planes = planService.getPlanes(plataformaId);
        return modelMapper.map(planes, new TypeToken<List<PlanDTO>>(){}.getType());
    }

    //Metodo para obtener los planes en orden de precio creciente
    @GetMapping(value = "/{plataforma_id}/planes/precioCreciente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlanDTO> getPlanesPrecioCreciente(@PathVariable("plataforma_id") Long plataformaId) throws EntityNotFoundException{
        List<PlanEntity> planes = planService.getPlanesOrdenadosPrecioCreciente(plataformaId);
        return modelMapper.map(planes, new TypeToken<List<PlanDTO>>(){}.getType());
    }

    //Metodo para obtener los planes en orden de precio decreciente
    @GetMapping(value = "/{plataforma_id}/planes/precioDecreciente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlanDTO> getPlanesPrecioDecreciente(@PathVariable("plataforma_id") Long plataformaId) throws EntityNotFoundException{
        List<PlanEntity> planes = planService.getPlanesOrdenadosPrecioDecreciente(plataformaId);
        return modelMapper.map(planes, new TypeToken<List<PlanDTO>>(){}.getType());
    }

    //Metodo para obtener los planes en orden de puntaje creciente
    @GetMapping(value = "/{plataforma_id}/planes/puntajeCreciente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlanDTO> getPlanesPuntajeCreciente(@PathVariable("plataforma_id") Long plataformaId) throws EntityNotFoundException{
        List<PlanEntity> planes = planService.getPlanesOrdenadosPuntajeCreciente(plataformaId);
        return modelMapper.map(planes, new TypeToken<List<PlanDTO>>(){}.getType());
    }

    //Metodo para obtener los planes en orden de puntaje decreciente
    @GetMapping(value = "/{plataforma_id}/planes/puntajeDecreciente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlanDTO> getPlanesPuntajeDecreciente(@PathVariable("plataforma_id") Long plataformaId) throws EntityNotFoundException{
        List<PlanEntity> planes = planService.getPlanesOrdenadosPuntajeDecreciente(plataformaId);
        return modelMapper.map(planes, new TypeToken<List<PlanDTO>>(){}.getType());
    }

    //Metodo para devolver un plan de una plataforma
    @GetMapping(value = "/{plataforma_id}/planes/{plan_id_1}")
    @ResponseStatus(code = HttpStatus.OK)
    public PlanDTO getPLan(@PathVariable("plataforma_id") Long plataformaId, @PathVariable("plan_id_1") Long planId) throws EntityNotFoundException{
        PlanEntity entity = planService.getPlan(plataformaId, planId);
        return modelMapper.map(entity, PlanDTO.class);
    }

    //Metodo para crear un plan en una plataforma
    @PostMapping(value = "/{plataforma_id}/planes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public PlanDTO createPlan(@PathVariable("plataforma_id") Long plataformaId, @RequestBody PlanDTO plan) throws IllegalOperationException, EntityNotFoundException{
        PlanEntity planEntity = modelMapper.map(plan, PlanEntity.class);
        PlanEntity newPlan = planService.createPlan(plataformaId, planEntity);
        return modelMapper.map(newPlan, PlanDTO.class);
    }

    //Metodo para actualizar un plan en una plataforma
    @PutMapping(value = "/{plataforma_id}/planes/{plan_id_1}")
    @ResponseStatus(code = HttpStatus.OK)
    public PlanDTO updatePlan(@PathVariable("plataforma_id") Long plataformaId, @PathVariable("plan_id_1") Long planId, @RequestBody PlanDTO plan) throws EntityNotFoundException, IllegalOperationException{
        PlanEntity planEntity = modelMapper.map(plan, PlanEntity.class);
        PlanEntity newPlanEntity = planService.updatePlanEntity(plataformaId, planId, planEntity);
        return modelMapper.map(newPlanEntity, PlanDTO.class);

    }

    //Metodo para borrar un plan en una plataforma
    @DeleteMapping(value = "/{plataforma_id}/planes/{plan_id_1}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable("plataforma_id") Long plataformaId, @PathVariable("plan_id_1") Long planId) throws EntityNotFoundException{
        planService.deletePlan(plataformaId, planId);
    }

}