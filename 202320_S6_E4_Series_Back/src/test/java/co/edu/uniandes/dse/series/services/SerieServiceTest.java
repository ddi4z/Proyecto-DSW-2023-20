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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(SerieService.class)
public class SerieServiceTest {

    @Autowired
    private SerieService serieService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<SerieEntity> serieList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SerieEntity");
    }

    private void insertData() {
        for(int i = 0; i < 3; i++){
            SerieEntity serieEntity = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(serieEntity);
            serieList.add(serieEntity);
        }
    }

    @Test
    void testCreateSerie() throws EntityNotFoundException, IllegalOperationException {
        SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
        SerieEntity result = serieService.createSerie(newEntity);
        assertNotNull(result);
        SerieEntity entity = entityManager.find(SerieEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getImagen(), entity.getImagen());
        assertEquals(newEntity.getSinopsis(), entity.getSinopsis());
        assertEquals(newEntity.getWallpaper(), entity.getWallpaper());
        assertEquals(newEntity.getAnio(), entity.getAnio());
    }

    @Test
    void testCreateSerieInvalidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setNombre("");
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testCreateSerieInvalidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setNombre(null);
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testCreateSerieInvalidSinopsis() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setSinopsis("");
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testCreateSerieInvalidSinopsis2() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setSinopsis(null);
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testCreateSerieInvalidImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setImagen("");
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testCreateSerieInvalidImagen2() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newEntity = factory.manufacturePojo(SerieEntity.class);
            newEntity.setImagen(null);
            serieService.createSerie(newEntity);
        });
    }

    @Test
    void testGetSeries() {
        List<SerieEntity> list = serieService.getSeries();
        assertEquals(serieList.size(), list.size());
        for (SerieEntity entity: list) {
            boolean found = false;
            for (SerieEntity storedEntity: serieList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetSerie() throws EntityNotFoundException {
        SerieEntity entity = serieList.get(0);
        SerieEntity resultEntity = serieService.getSerie(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(resultEntity.getId(), entity.getId());
        assertEquals(resultEntity.getNombre(), entity.getNombre());
        assertEquals(resultEntity.getImagen(), entity.getImagen());
        assertEquals(resultEntity.getSinopsis(), entity.getSinopsis());
        assertEquals(resultEntity.getWallpaper(), entity.getWallpaper());
        assertEquals(resultEntity.getAnio(), entity.getAnio());
    }

    @Test
    void testGetInvalidSerie() {
        assertThrows(EntityNotFoundException.class,()->{
            serieService.getSerie(0L);
        });  
    }

    @Test
    void testGetSerieByNombre() throws EntityNotFoundException {
        SerieEntity entity = serieList.get(0);
        List<SerieEntity> list = serieService.getSerieByNombre(entity.getNombre());
        assertNotNull(list);
         for (SerieEntity foundEntity: list) {
            boolean found = false;
            if (entity.getNombre().equals(foundEntity.getNombre())) {
                found = true;
                break;
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetInvalidSerieByNombre() {
        assertThrows(EntityNotFoundException.class,()->{
            serieService.getSerieByNombre("");
        });  
    }

    @Test
    void testGetInvalidSerieByNombre2() {
        assertThrows(EntityNotFoundException.class,()->{
            serieService.getSerieByNombre(null);
        });
    }

    @Test
    void testUpdateSerie() throws EntityNotFoundException, IllegalOperationException {
        SerieEntity entity = serieList.get(0);
        SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
        serieService.updateSerie(entity.getId(), pojoEntity);

        SerieEntity resultEntity = entityManager.find(SerieEntity.class, entity.getId());
        assertEquals(resultEntity.getId(), pojoEntity.getId());
        assertEquals(resultEntity.getNombre(), pojoEntity.getNombre());
        assertEquals(resultEntity.getImagen(), pojoEntity.getImagen());
        assertEquals(resultEntity.getSinopsis(), pojoEntity.getSinopsis());
        assertEquals(resultEntity.getWallpaper(), entity.getWallpaper());
        assertEquals(resultEntity.getAnio(), entity.getAnio());
    }

    @Test
    void testUpdateInvalidSerie() {
        assertThrows(EntityNotFoundException.class, () -> {
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setId(0L);
            serieService.updateSerie(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie2() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setNombre("");
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie3() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setNombre(null);
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie4() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setSinopsis("");
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie5() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setSinopsis(null);
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie6() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setImagen("");
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateInvalidSerie7() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity entity = serieList.get(0);
            SerieEntity pojoEntity = factory.manufacturePojo(SerieEntity.class);
            pojoEntity.setImagen(null);
            serieService.updateSerie(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteSerie() throws EntityNotFoundException, IllegalOperationException {
        SerieEntity entity = serieList.get(1);
        serieService.deleteSerie(entity.getId());
        SerieEntity deleted = entityManager.find(SerieEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidSerie() {
        assertThrows(EntityNotFoundException.class, ()->{
            serieService.deleteSerie(0L);
        });
    }
    
}