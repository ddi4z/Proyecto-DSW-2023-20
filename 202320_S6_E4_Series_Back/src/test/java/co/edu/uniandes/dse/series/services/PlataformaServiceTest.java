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

import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(PlataformaService.class)
public class PlataformaServiceTest {
    @Autowired
    private PlataformaService plataformaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PlataformaEntity> plataformaList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PlataformaEntity");
    }

    private void insertData() {
        for(int i = 0; i < 4; i++){
            PlataformaEntity plataformaEntity = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(plataformaEntity);
            plataformaList.add(plataformaEntity);
        }
    }

    @Test
    void testDeleteInvalidPlataforma() {
        assertThrows(EntityNotFoundException.class, ()->{
            plataformaService.deletePlataforma(-1L);
    });
    }


    //Prueba para crear una plataforma
    @Test
    void testCreateBook() throws EntityNotFoundException, IllegalOperationException {
        PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
        //newEntity.setEditorial(editorialList.get(0));
        //newEntity.setIsbn("1-4028-9462-7");
        PlataformaEntity result = plataformaService.createPlataforma(newEntity);
        assertNotNull(result);
        PlataformaEntity entity = entityManager.find(PlataformaEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getSitioWeb(), entity.getSitioWeb());
        assertEquals(newEntity.getLogo(), entity.getLogo());
    }

    //Prueba para crear una plataforma que tiene un nombre que ya existe
    @Test
    void testCreatePlatformaWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setNombre(null);
                plataformaService.createPlataforma(newEntity);
        });
    }

    //Prueba para crear una plataforma que tiene un sitio web que ya existe
    @Test
    void testCreatePlatformaWithNoValidSitioWeb() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setSitioWeb(null);
                plataformaService.createPlataforma(newEntity);
        });
    }

    //Prueba para crear una plataforma que tiene un logo que ya existe
    @Test
    void testCreatePlatformaWithNoValidLogo() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setLogo("");
                plataformaService.createPlataforma(newEntity);
        });
    }
    //Prueba para crear una plataforma que tiene un nombre no nulo
    @Test
    void testCreatePlatformaWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setNombre(null);
                plataformaService.createPlataforma(newEntity);
        });
    }

    //Prueba para crear una plataforma que tiene un sitio web no nulo
    @Test
    void testCreatePlatformaWithNoValidSitioWeb2() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setSitioWeb(null);
                plataformaService.createPlataforma(newEntity);
        });
    }

    //Prueba para crear una plataforma que tiene un logo no nulo
    @Test
    void testCreatePlatformaWithNoValidLogo2() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity newEntity = factory.manufacturePojo(PlataformaEntity.class);
                newEntity.setLogo(null);
                plataformaService.createPlataforma(newEntity);
        });
    }

    //Prueba para obtener todas las plataformas
    @Test
    void testGetPlataformas() {
        List<PlataformaEntity> list = plataformaService.getPlataformas();
        assertEquals(plataformaList.size(), list.size());
        for (PlataformaEntity entity : list) {
                boolean found = false;
                for (PlataformaEntity storedEntity : plataformaList) {
                        if (entity.getId().equals(storedEntity.getId())) {
                                found = true;
                        }
                }
                assertTrue(found);
        }
    }


    //Prueba getPlataforma
    @Test
    void testGetPlataforma() throws EntityNotFoundException {
        PlataformaEntity entity = plataformaList.get(0);
        PlataformaEntity resultEntity = plataformaService.getPlataforma(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getSitioWeb(), resultEntity.getSitioWeb());
        assertEquals(entity.getLogo(), resultEntity.getLogo());
    }


    //Prueba getPlataforma plataforma no existente
    @Test
    void testGetInvalidPlatforma() {
        assertThrows(EntityNotFoundException.class,()->{
                plataformaService.getPlataforma(-1L);
        });
    }


    //Prubea actualizar plataforma
    @Test
    void testUpdatePlataforma() throws EntityNotFoundException, IllegalOperationException {
        PlataformaEntity entity = plataformaList.get(0);
        PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
        pojoEntity.setId(entity.getId());
        plataformaService.updatePlataforma(entity.getId(), pojoEntity);

        PlataformaEntity resp = entityManager.find(PlataformaEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getSitioWeb(), resp.getSitioWeb());
        assertEquals(pojoEntity.getLogo(), resp.getLogo());
    }

    //Prueba actualizar plataforma no existente
    @Test
    void testUpdatePlataformaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setId(0L);
                plataformaService.updatePlataforma(0L, pojoEntity);
        });
    }
    //Prueba actualizar plataforma sin sitio web
    @Test
    void testUpdatePlataformaInvalidWeb1() {
        assertThrows(IllegalOperationException.class, () -> {

                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setSitioWeb(null);
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

        //Prueba actualizar plataforma sin nombre
    @Test
    void testUpdatePlataformaInvalidNombre1() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setNombre(null);
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

        //Prueba actualizar plataforma sin logo
    @Test
    void testUpdatePlataformaInvalidLogo1() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setLogo(null);
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

    //Prueba actualizar plataforma con sitio web existente
    @Test
    void testUpdatePlataformaInvalidWeb2() {
        assertThrows(IllegalOperationException.class, () -> {
                
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setSitioWeb("");
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

        //Prueba actualizar plataforma con nombre existente
    @Test
    void testUpdatePlataformaInvalidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setNombre("");
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

        //Prueba actualizar plataforma con logo existente
    @Test
    void testUpdatePlataformaInvalidLogo2() {
        assertThrows(IllegalOperationException.class, () -> {
                PlataformaEntity pojoEntity = factory.manufacturePojo(PlataformaEntity.class);
                pojoEntity.setLogo(null);
                plataformaService.updatePlataforma(plataformaList.get(0).getId(), pojoEntity);
        });
    }

    //Prueba borrar plataforma
    @Test
    void testDeletePlataforma() throws EntityNotFoundException, IllegalOperationException {
        PlataformaEntity entity = plataformaList.get(1);
        plataformaService.deletePlataforma(entity.getId());
        PlataformaEntity deleted = entityManager.find(PlataformaEntity.class, entity.getId());
        assertNull(deleted);
    }






}