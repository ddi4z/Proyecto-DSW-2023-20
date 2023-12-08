package co.edu.uniandes.dse.series.services;

import static org.junit.jupiter.api.Assertions.*;


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
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de la logica de la relacion Serie - Plataforma
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(SeriePlataformaService.class)
public class SeriePlataformaServiceTest {

    @Autowired
    private SeriePlataformaService seriePlataformaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private SerieEntity serie = new SerieEntity();
    private List<PlataformaEntity> plataformaList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que estan implicadas en la prueba
     */
    private void clearData(){
        entityManager.getEntityManager().createQuery("delete from PlataformaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();
    }

    /*
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas
     */
    private void insertData(){
        serie = factory.manufacturePojo(SerieEntity.class);
        entityManager.persist(serie);

        for(int i=0; i<3; i++){
            PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(entity);
            entity.getSeriesPlataforma().add(serie);
            plataformaList.add(entity);
            serie.getPlataformas().add(entity);
        }
    }

	
    /*
     * Prueba para asociar una plataforma a una serie
     */
    @Test
    void testAddPlataforma() throws EntityNotFoundException, IllegalOperationException{
        SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
        entityManager.persist(newSerie);

        PlataformaEntity plataforma = factory.manufacturePojo(PlataformaEntity.class);
        entityManager.persist(plataforma);

        seriePlataformaService.addPlataforma(newSerie.getId(), plataforma.getId());

        PlataformaEntity lastPlataforma = seriePlataformaService.getPlataforma(newSerie.getId(), plataforma.getId());
        assertEquals(plataforma.getId(), lastPlataforma.getId());
        assertEquals(plataforma.getNombre(), lastPlataforma.getNombre());
        assertEquals(plataforma.getSitioWeb(), lastPlataforma.getSitioWeb());
        assertEquals(plataforma.getLogo(), lastPlataforma.getLogo());
    }


    /*
     * Prueba para asociar una plataforma que no existe a un libro
     */
    @Test
    void testAddInvalidPlataforma(){
        assertThrows(EntityNotFoundException.class, ()->{
            SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            seriePlataformaService.addPlataforma(newSerie.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar una plataforma a un libro que no existe
     */
    @Test
    void testAddPlataformaInvalidSerie(){
        assertThrows(EntityNotFoundException.class, ()->{
            PlataformaEntity plataforma = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(plataforma);
            seriePlataformaService.addPlataforma(0L, plataforma.getId());
        });
    }

    /*
     * Prueba para consultar la lista de plataformas de un libro
     */
    @Test
    void testGetPlataformas() throws EntityNotFoundException{
        List<PlataformaEntity> plataformaEntities = seriePlataformaService.getPlataformas(serie.getId());
        assertEquals(plataformaList.size(), plataformaEntities.size());

        for(int i=0; i<plataformaList.size(); i++){
            assertTrue(plataformaEntities.contains(plataformaList.get(i)));
        }
    }

    /*
     * Prueba para consultar la lista de plataformas de un libro que no existe
     */
    @Test
    void testGetPlataformasInvalidSerie(){
        assertThrows(EntityNotFoundException.class, ()->{
            seriePlataformaService.getPlataformas(0L);
        });
    }

    /*
     * Prueba para consultar una plataforma de una serie
     */
    @Test
    void testGetPlataforma() throws EntityNotFoundException, IllegalOperationException{
        PlataformaEntity plataformaEntity = plataformaList.get(0);
        PlataformaEntity plataforma = seriePlataformaService.getPlataforma(serie.getId(), plataformaEntity.getId());
        assertNotNull(plataforma);

        assertEquals(plataformaEntity.getId(), plataforma.getId());
        assertEquals(plataformaEntity.getNombre(), plataforma.getNombre());
        assertEquals(plataformaEntity.getSitioWeb(), plataforma.getSitioWeb());
        assertEquals(plataformaEntity.getLogo(), plataforma.getLogo());
    }

    /*
     * Prueba para consultar una plataforma que no existe de una serie
     */
    @Test
    void testGetInvalidPlataforma(){
        assertThrows(EntityNotFoundException.class, ()->{
            seriePlataformaService.getPlataforma(serie.getId(), 0L);
        });
    }

    /*
     * Prueba para consultar una plataforma de una serie que no existe
     */
    @Test
    void testGetPlataformaInvalidSerie(){
        assertThrows(EntityNotFoundException.class, ()->{
            PlataformaEntity plataformaEntity = plataformaList.get(0);
            seriePlataformaService.getPlataforma(0L, plataformaEntity.getId());
        });
    }

    /*
     * Prueba para obtener una plataforma no asociada a una serie
     */
    @Test
    void testGetNotAssociatedPlataforma(){
        assertThrows(IllegalOperationException.class, ()->{
            SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            PlataformaEntity plataforma = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(plataforma);
            seriePlataformaService.getPlataforma(newSerie.getId(), plataforma.getId());
        });
    }

    /*
     * Prueba para actualizar las plataformas de una serie
     */
    @Test
    void testReplacePlataformas() throws EntityNotFoundException{
        List<PlataformaEntity> nuevaLista = new ArrayList<>();
        for(int i=0; i<3; i++){
            PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
            entityManager.persist(entity);
            serie.getPlataformas().add(entity);
            nuevaLista.add(entity);
        }
        seriePlataformaService.replacePlataformas(serie.getId(), nuevaLista);
        List<PlataformaEntity> plataformaEntities = seriePlataformaService.getPlataformas(serie.getId());
        for(PlataformaEntity aNuevaLista : nuevaLista){
            assertTrue(plataformaEntities.contains(aNuevaLista));
        }
    }

    /**
	 * Prueba para actualizar las plataformas de una serie.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePlataformas2() throws EntityNotFoundException {
		List<PlataformaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		seriePlataformaService.replacePlataformas(serie.getId(), nuevaLista);
		
		List<PlataformaEntity> plataformaEntities = seriePlataformaService.getPlataformas(serie.getId());
		for (PlataformaEntity aNuevaLista : nuevaLista) {
			assertTrue(plataformaEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar las plataformas de una serie que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePlataformasInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<PlataformaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
				entity.getSeriesPlataforma().add(serie);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			seriePlataformaService.replacePlataformas(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar las plataformas que no existen de una serie.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidPlataformas() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<PlataformaEntity> nuevaLista = new ArrayList<>();
			PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			seriePlataformaService.replacePlataformas(serie.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar una plataforma de una serie que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePlataformasInvalidPlataforma(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<PlataformaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				PlataformaEntity entity = factory.manufacturePojo(PlataformaEntity.class);
				entity.getSeriesPlataforma().add(serie);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			seriePlataformaService.replacePlataformas(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar una plataforma con una serie.
	 *
	 */
	@Test
	void testRemovePlataforma() throws EntityNotFoundException {
		for (PlataformaEntity plataforma : plataformaList) {
			seriePlataformaService.removePlataforma(serie.getId(), plataforma.getId());
		}
		assertTrue(seriePlataformaService.getPlataformas(serie.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar una plataforma que no existe con una serie.
	 *
	 */
	@Test
	void testRemoveInvalidPlataforma(){
		assertThrows(EntityNotFoundException.class, ()->{
			seriePlataformaService.removePlataforma(serie.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar una plataforma con una serie que no existe.
	 *
	 */
	@Test
	void testRemovePlataformaInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			seriePlataformaService.removePlataforma(0L, plataformaList.get(0).getId());
		});
	}

}
