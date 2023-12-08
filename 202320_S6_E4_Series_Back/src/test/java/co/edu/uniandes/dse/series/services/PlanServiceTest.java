package co.edu.uniandes.dse.series.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.series.entities.PlanEntity;
import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(PlanService.class)

public class PlanServiceTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PlanEntity> planList = new ArrayList<>();
    private PlataformaEntity plataformaEntity;

    //Configuración inicial de la prueba.
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    
    }

    //Limpia las tablas que estan implicadas en la prueba.
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PlanEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PlataformaEntity").executeUpdate();
    
    }

    //Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
    private void insertData() {

        plataformaEntity = factory.manufacturePojo(PlataformaEntity.class);
        entityManager.persist(plataformaEntity);

        for (int i = 0; i < 3; i++) {
            PlanEntity planEntity = factory.manufacturePojo(PlanEntity.class);
            planEntity.setPlataforma(plataformaEntity);
            entityManager.persist(planEntity);
            planList.add(planEntity);
        }

        plataformaEntity.setPlanes(planList);

    }

    //Prueba createPlan
    @Test
    void testCreatePlan() throws IllegalOperationException, EntityNotFoundException {
        PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        PlanEntity result = planService.createPlan(plataformaEntity.getId(), newEntity);
        assertNotNull(result);
        PlanEntity entity = entityManager.find(PlanEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getImagen(), entity.getImagen());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getPrecio(), entity.getPrecio());
        assertEquals(newEntity.getPuntaje(), entity.getPuntaje());
    }

    //Prueba createPlan con plataforma inexistente
    @Test
    void testCreatePlanInvalidPlataforma() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
            planService.createPlan(0L, newEntity);
        });
    }

    //Prueba createPlan con plataforma nula
    @Test
    void testCreatePlanInvalidPlataforma2() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
            newEntity.setPlataforma(null);
            planService.createPlan(0L, newEntity);
        });
    }

    //Prueba createPlan con Imagen inválido (cadena vacia)
    @Test
    void testCreatePlanNoValidImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setImagen("");
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con Imagen inválido (nulo)
    @Test
    void testCreatePlanNoValidImagen2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setImagen(null);
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con Nombre inválido (cadena vacia)
    @Test
    void testCreatePlanNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setNombre("");
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con Nombre inválido (nulo)
    @Test
    void testCreatePlanNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setNombre(null);
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con precio inválido (menor a cero)
    @Test
    void testCreatePlanNoValidPrecio() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setPrecio(-9500);
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con precio inválido (nulo)
    @Test
    void testCreatePlanNoValidPrecio2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
        newEntity.setPlataforma(plataformaEntity);
        newEntity.setPrecio(null);
        planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con puntaje invalido (menor a cero)
    @Test
    void testCreatePlanNoValidPuntaje(){
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
            newEntity.setPlataforma(plataformaEntity);
            newEntity.setPuntaje(-1);
            planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba createPlan con puntaje invalido (nulo)
    @Test
    void testCreatePLanNoValidPuntaje2(){
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity newEntity = factory.manufacturePojo(PlanEntity.class);
            newEntity.setPlataforma(plataformaEntity);
            newEntity.setPuntaje(null);
            planService.createPlan(plataformaEntity.getId(), newEntity);
        });
    }

    //Prueba getPlanes
    @Test
    void testGetPlanes() throws EntityNotFoundException {
        List<PlanEntity> list = planService.getPlanes(plataformaEntity.getId());
        assertEquals(planList.size(), list.size());
        for (PlanEntity entity : list) {
            boolean found = false;
            for (PlanEntity storedEntity : planList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    //Prueba para consultar la lista de planes de una plataforma inexistente
    @Test
    void testGetPlanesInvalidPlataforma() {
        assertThrows(EntityNotFoundException.class, () -> {
            planService.getPlanes(0L);      
        });
    }

    //Prueba getPlan
    @Test
    void testGetPlan() throws EntityNotFoundException {
        PlanEntity entity = planList.get(0);
        PlanEntity resulEntity = planService.getPlan(plataformaEntity.getId(), entity.getId());
        assertNotNull(resulEntity);
        assertEquals(entity.getId(), resulEntity.getId());
        assertEquals(entity.getImagen(), resulEntity.getImagen());
        assertEquals(entity.getNombre(), resulEntity.getNombre());
        assertEquals(entity.getPrecio(), resulEntity.getPrecio());
        assertEquals(entity.getPuntaje(), resulEntity.getPuntaje());
    }

    //Prueba getPlan de una plataforma inexistente
    @Test
    void testGetInvalidPlataforma() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity entity = planList.get(0);
            planService.getPlan(0L, entity.getId());
        });
    }

    //Prueba getPlan de un plan que no existe en una plataforma
    @Test
    void testGetInvalidPlan() {
        assertThrows(EntityNotFoundException.class, () -> {
            planService.getPlan(plataformaEntity.getId(), 0L);
        });
    }

    //Prueba updatePlan
    @Test
    void testUpadtePlan() throws EntityNotFoundException, IllegalOperationException  {
        PlanEntity entity = planList.get(0);
        PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
        pojoEntity.setId(entity.getId());
        planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);

        PlanEntity resp = entityManager.find(PlanEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(entity.getImagen(), resp.getImagen());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getPrecio(), resp.getPrecio());
        assertEquals(pojoEntity.getPuntaje(), resp.getPuntaje());
    }

    //Prueba updatePlanEntity de una plataforma inexistente
    @Test
    void testUpadtePlanInvalidPlataforma() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(0L, entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity de un plan inexistente de una plataforma
    @Test
    void testUpadteInvalidPlan() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            planService.updatePlanEntity(plataformaEntity.getId(), 0L, pojoEntity);
        });
    }

    //Prueba updatePlanEntity (imagen invalida -> cadena vacia)
    @Test
    void testUpadtePlanNoValidImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setImagen("");
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (imagen invalida -> nulo)
    @Test
    void testUpadtePlanNoValidImagen2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setImagen("");
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (nombre invalido -> cadena vacia)
    @Test
    void testUpadtePlanNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setNombre("");
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (nombre invalido -> nulo)
    @Test
    void testUpadtePlanNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setNombre(null);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (precio invalido -> menor a cero)
    @Test
    void testUpadtePlanNoValidPrecio() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setPrecio(-5300);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (precio invalido -> nulo)
    @Test
    void testUpadtePlanNoValidPrecio2() {
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setPrecio(null);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (puntaje invalido -> menor a cero)
    @Test
    void testUpdatePlanNoValidPuntaje(){
        assertThrows(IllegalOperationException.class, () -> {
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setPuntaje(-1);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba updatePlanEntity (puntaje invalido -> nulo)
    @Test
    void testUpdatePlanNoValidPuntaje2(){
        assertThrows(IllegalOperationException.class, ()->{
            PlanEntity entity = planList.get(0);
            PlanEntity pojoEntity = factory.manufacturePojo(PlanEntity.class);
            pojoEntity.setPuntaje(null);
            pojoEntity.setId(entity.getId());
            planService.updatePlanEntity(plataformaEntity.getId(), entity.getId(), pojoEntity);
        });
    }

    //Prueba deletePlan
    @Test
    void testDeletePlan() throws EntityNotFoundException {
        PlanEntity entity = planList.get(0);
        planService.deletePlan(plataformaEntity.getId(), entity.getId());
        PlanEntity deleted = entityManager.find(PlanEntity.class, entity.getId());
        assertNull(deleted);
    }

    //Prueba deletePlan de una plataforma inexistente
    @Test
    void testDeletePlanInvalidPlantaform() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlanEntity entity = planList.get(0);
            planService.deletePlan(0L, entity.getId());
        });
    }

    //Prueba deletePlan de una plataforma a la cual no pertenece
    @Test
    void testDeletePlanWithNoAssociatedPlataform() {
        assertThrows(EntityNotFoundException.class, () -> {
            PlataformaEntity newPlataforma = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(newPlataforma);
            PlanEntity entity = planList.get(0);
            planService.deletePlan(newPlataforma.getId(), entity.getId());
        });
    }
}