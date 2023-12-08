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

import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(CategoriaService.class)
public class CategoriaServiceTest {
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CategoriaEntity> categoriaList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
            clearData();
            insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
            entityManager.getEntityManager().createQuery("delete from CategoriaEntity");
            entityManager.getEntityManager().createQuery("delete from SerieEntity");
    }

    /**
    * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
    */
    private void insertData() {

            for (int i = 0; i < 3; i++) {
                    CategoriaEntity categoriaEntity = factory.manufacturePojo(CategoriaEntity.class);
                    entityManager.persist(categoriaEntity);
                    categoriaList.add(categoriaEntity);
            }

            SerieEntity serieEntity = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(serieEntity);
            serieEntity.getCategorias().add(categoriaList.get(0));
            categoriaList.get(0).getSeries().add(serieEntity);
    }
    

    @Test
    void testCreateCategoria() throws EntityNotFoundException, IllegalOperationException {
        CategoriaEntity newEntity = factory.manufacturePojo(CategoriaEntity.class);
        CategoriaEntity result = categoriaService.createCategoria(newEntity);
        assertNotNull(result);
        CategoriaEntity entity = entityManager.find(CategoriaEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
    }
    @Test
    void testCreateCategoriaInvalidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
                CategoriaEntity newEntity = factory.manufacturePojo(CategoriaEntity.class);
                newEntity.setNombre(null);
                categoriaService.createCategoria(newEntity);
        });
    }       




    @Test
    void testGetCategorias() {
        List<CategoriaEntity> list = categoriaService.getCategorias();
        assertEquals(categoriaList.size(), list.size());
        for (CategoriaEntity entity : list) {
                boolean found = false;
                for (CategoriaEntity storedEntity : categoriaList) {
                        if (entity.getId().equals(storedEntity.getId())) {
                                found = true;
                        }
                }
                assertTrue(found);
        }
}

    @Test
    void testGetCategoria() throws EntityNotFoundException {
        CategoriaEntity entity = categoriaList.get(0);
        CategoriaEntity resultEntity = categoriaService.getCategoria(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getSeries(), resultEntity.getSeries());
        assertEquals(entity.getImagen(), resultEntity.getImagen());
}

    @Test
    void testGetInvalidCategoria() {
        assertThrows(EntityNotFoundException.class,()->{
                categoriaService.getCategoria(0L);
        });
}

    @Test
    void testUpdateCategoria() throws EntityNotFoundException, IllegalOperationException {
        CategoriaEntity entity = categoriaList.get(0);
        CategoriaEntity pojoEntity = factory.manufacturePojo(CategoriaEntity.class);
        pojoEntity.setId(entity.getId());
        categoriaService.updateCategoria(entity.getId(), pojoEntity);

        CategoriaEntity resp = entityManager.find(CategoriaEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getSeries(), resp.getSeries());
        assertEquals(pojoEntity.getImagen(), resp.getImagen());
}

    @Test
    void testUpdateBookInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
                CategoriaEntity pojoEntity = factory.manufacturePojo(CategoriaEntity.class);
                pojoEntity.setId(0L);
                categoriaService.updateCategoria(0L, pojoEntity);
        });
}
    
    @Test
    void testDeleteCategoria() throws EntityNotFoundException, IllegalOperationException {
        CategoriaEntity entity = categoriaList.get(1);
        categoriaService.deleteCategoria(entity.getId());
        CategoriaEntity deleted = entityManager.find(CategoriaEntity.class, entity.getId());
        assertNull(deleted);
}

    @Test
    void testDeleteInvalidCategoria() {
        assertThrows(EntityNotFoundException.class, ()->{
                categoriaService.deleteCategoria(0L);
        });
}

    @Test
    void testDeleteBookWithAuthor() {
        assertThrows(IllegalOperationException.class, () -> {
                CategoriaEntity entity = categoriaList.get(0);
                categoriaService.deleteCategoria(entity.getId());
        });
}

}