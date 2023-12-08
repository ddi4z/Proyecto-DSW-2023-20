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

import co.edu.uniandes.dse.series.entities.EpisodioEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(EpisodioService.class)
public class EpisodioServiceTest {

    
    @Autowired
    private EpisodioService episodioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EpisodioEntity> episodioList = new ArrayList<>();

    private SerieEntity serieEntity;
    @BeforeEach
    private void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EpisodioEntity").executeUpdate();;
        entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();;
    }

    private void insertData() {
        serieEntity = factory.manufacturePojo(SerieEntity.class);
        entityManager.persist(serieEntity);

        for(int i = 0; i < 4; i++){
            EpisodioEntity episodioEntity = factory.manufacturePojo(EpisodioEntity.class);
            episodioEntity.setSerie(serieEntity);
            entityManager.persist(episodioEntity);
            episodioList.add(episodioEntity);
        }

        serieEntity.setEpisodios(episodioList);
    }

    @Test
    void testCreateEpisodio() throws EntityNotFoundException, IllegalOperationException {
        EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
        EpisodioEntity result = episodioService.createEpisodio(serieEntity.getId(),newEntity);
        assertNotNull(result);
        EpisodioEntity entity = entityManager.find(EpisodioEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getResumen(), entity.getResumen());
        assertEquals(newEntity.getImagen(), entity.getImagen());
        assertEquals(newEntity.getNumeroEpisodio(), entity.getNumeroEpisodio());
        assertEquals(newEntity.getDuracionMinutos(), entity.getDuracionMinutos());
    }

	@Test
	void testCreateEpisodioInvalidSerie() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
			episodioService.createEpisodio(0L, newEntity);
		});
	}


    @Test
    void testCreateEpisodioInvalidNumeroEpisodio1() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(-1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
        
    }

    @Test
    void testCreateEpisodioInvalidNumeroEpisodio2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(0);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
        
    }

    @Test
    void testCreateEpisodioInvalidDuracion() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(-5);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidDuracion2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(0);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre(null);
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidResumen() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("");
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidResumen2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen(null);
            newEntity.setImagen("Imagen del episodio 1");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen("");
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testCreateEpisodioInvalidImagen2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity newEntity = factory.manufacturePojo(EpisodioEntity.class);
            newEntity.setNombre("Episodio 1");
            newEntity.setNumeroEpisodio(1);
            newEntity.setDuracionMinutos(60);
            newEntity.setResumen("Resumen del episodio 1");
            newEntity.setImagen(null);
            episodioService.createEpisodio(serieEntity.getId(),newEntity);
        });
    }

    @Test
    void testGetEpisodios() throws EntityNotFoundException {
        List<EpisodioEntity> list = episodioService.getEpisodios(serieEntity.getId());
        assertEquals(episodioList.size(), list.size());
        for (EpisodioEntity entity : list) {
                boolean found = false;
                for (EpisodioEntity storedEntity : episodioList) {
                        if (entity.getId().equals(storedEntity.getId())) {
                                found = true;
                        }
                }
                assertTrue(found);
        }
    }

    @Test
    void testGetEpisodioById() throws EntityNotFoundException, IllegalOperationException {
        EpisodioEntity entity = episodioList.get(0);
        EpisodioEntity resultEntity = episodioService.getEpisodioById(serieEntity.getId(),entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getResumen(), resultEntity.getResumen());
        assertEquals(entity.getImagen(), resultEntity.getImagen());
        assertEquals(entity.getNumeroEpisodio(), resultEntity.getNumeroEpisodio());
        assertEquals(entity.getDuracionMinutos(), resultEntity.getDuracionMinutos());
    }

    @Test
    void testGetEpisodioByNombre() throws EntityNotFoundException, IllegalOperationException {
        EpisodioEntity entity = episodioList.get(0);
        EpisodioEntity resultEntity = episodioService.getEpisodioByNombre(serieEntity.getId(),entity.getNombre());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getResumen(), resultEntity.getResumen());
        assertEquals(entity.getImagen(), resultEntity.getImagen());
        assertEquals(entity.getNumeroEpisodio(), resultEntity.getNumeroEpisodio());
        assertEquals(entity.getDuracionMinutos(), resultEntity.getDuracionMinutos());
    }

    @Test
    void testGetInvalidEpisodioById() {
        assertThrows(EntityNotFoundException.class,()->{
            episodioService.getEpisodioById(serieEntity.getId(),0L);
        });
    }
    @Test
    void testGetInvalidEpisodioById2() {
        assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newSerie =  factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            EpisodioEntity entity = episodioList.get(0);
            episodioService.getEpisodioById(newSerie.getId(),entity.getId());
        });
    }
    @Test
    void testGetInvalidEpisodioByNombre1() {
        assertThrows(EntityNotFoundException.class,()->{
            episodioService.getEpisodioByNombre(serieEntity.getId(),"");
        });
    }

    @Test
    void testGetInvalidEpisodioByNombre2() {
        assertThrows(EntityNotFoundException.class,()->{
            episodioService.getEpisodioByNombre(serieEntity.getId(),null);
        });
    }
	@Test
	void testGetEpisodioIdInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			EpisodioEntity entity = episodioList.get(0);
			episodioService.getEpisodioById(0L, entity.getId());
		});
	}
    @Test
	void testGetEpisodioNombreInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			EpisodioEntity entity = episodioList.get(0);
			episodioService.getEpisodioByNombre(0L, entity.getNombre());
		});
	}
    @Test
	void testGetEpisodioNombreInvalidSerie2() {
		assertThrows(IllegalOperationException.class, () -> {
            SerieEntity newSerie =  factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            EpisodioEntity entity = episodioList.get(0);
            episodioService.getEpisodioByNombre(newSerie.getId(), entity.getNombre());
		});
	}
	@Test
	void testGetEpisodiosInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			episodioService.getEpisodios(0L);
		});
	}
    @Test
    void testUpdateEpisodio() throws EntityNotFoundException, IllegalOperationException {
        EpisodioEntity entity = episodioList.get(0);
        EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
        pojoEntity.setId(entity.getId());

        episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);

        EpisodioEntity resp = entityManager.find(EpisodioEntity.class, entity.getId());

        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getResumen(), resp.getResumen());
        assertEquals(pojoEntity.getImagen(), resp.getImagen());
        assertEquals(pojoEntity.getNumeroEpisodio(), resp.getNumeroEpisodio());
        assertEquals(pojoEntity.getDuracionMinutos(), resp.getDuracionMinutos());
    }

    @Test
    void testUpdateInvalidEpisodio() {
        assertThrows(EntityNotFoundException.class, () -> {
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setId(0L);
            episodioService.updateEpisodio(serieEntity.getId(),0L, pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioNombre1() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setNombre(null);
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }
    @Test
    void testUpdateInvalidEpisodioNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setNombre("");
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setImagen(null);
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioImagen2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setImagen("");
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioResumen1() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setResumen(null);
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioResumen2() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setResumen("");
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }
    @Test
    void testUpdateInvalidEpisodioNumero() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setNumeroEpisodio(0);
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
    void testUpdateInvalidEpisodioDuracion() {
        assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
            pojoEntity.setDuracionMinutos(0);
            pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(serieEntity.getId(),entity.getId(), pojoEntity);
    });
    }

    @Test
	void testUpdateEpisodioInvalidSerie() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			EpisodioEntity entity = episodioList.get(0);
			EpisodioEntity pojoEntity = factory.manufacturePojo(EpisodioEntity.class);
			pojoEntity.setId(entity.getId());
            episodioService.updateEpisodio(0L, entity.getId(), pojoEntity);
		});
		
	}

    @Test
    void testDeleteEpisodio() throws EntityNotFoundException, IllegalOperationException {
        EpisodioEntity entity = episodioList.get(1);
        episodioService.deleteEpisodio(serieEntity.getId(),entity.getId());
        EpisodioEntity deleted = entityManager.find(EpisodioEntity.class, entity.getId());
        assertNull(deleted);

    }

    @Test
    void testDeleteInvalidEpisodio() {
        assertThrows(EntityNotFoundException.class, ()->{
            episodioService.deleteEpisodio(-1L, episodioList.get(0).getId());
    });
    }

    @Test
    void testDeleteInvalidEpisodio2() {
        assertThrows(EntityNotFoundException.class, ()->{
            episodioService.deleteEpisodio(serieEntity.getId(),-28L);


    });
    }

	@Test
	void testDeleteEpisodioInvalidSerie()  {
		assertThrows(EntityNotFoundException.class, ()->{
			EpisodioEntity entity = episodioList.get(0);
			episodioService.deleteEpisodio(0L, entity.getId());
		});
	}

    @Test
	void testDeleteEpisodioWithNoAssociatedSerie() {
		assertThrows(IllegalOperationException.class, () -> {
            EpisodioEntity entity = episodioList.get(0);
            SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            episodioService.deleteEpisodio(newSerie.getId(), entity.getId());
		});
	}
}

