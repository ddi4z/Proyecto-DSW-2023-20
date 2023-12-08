package co.edu.uniandes.dse.series.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({PlataformaSerieService.class, SerieService.class})

public class PlataformaSerieServiceTest {

    @Autowired
    private PlataformaSerieService plataformaSerieService;

    @Autowired
    private SerieService serieService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private PlataformaEntity plataforma = new PlataformaEntity();
    private List<SerieEntity> serieList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();

	}
    
	//Limpia las tablas que están implicadas en la prueba.
	 
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PlataformaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();

    }

	//Inserta los datos iniciales para el correcto funcionamiento de las pruebas.

    private void insertData() {
        
        plataforma = factory.manufacturePojo(PlataformaEntity.class);
		entityManager.persist(plataforma);

		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entity.getPlataformas().add(plataforma);
			entityManager.persist(entity);
			serieList.add(entity);
			plataforma.getSeriesPlataforma().add(entity);
		}
    }

    //Prueba testAddSerie

    @Test
    void testAddSerie() throws EntityNotFoundException, IllegalOperationException {

        SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
		serieService.createSerie(newSerie);

		SerieEntity SerieEntity = plataformaSerieService.addSerie(plataforma.getId(), newSerie.getId());
		assertNotNull(SerieEntity);

		assertEquals(SerieEntity.getId(), newSerie.getId());
		assertEquals(SerieEntity.getNombre(), newSerie.getNombre());
		assertEquals(SerieEntity.getImagen(), newSerie.getImagen());
		assertEquals(SerieEntity.getSinopsis(), newSerie.getSinopsis());
		assertEquals(SerieEntity.getAnio(), newSerie.getAnio());
		assertEquals(SerieEntity.getWallpaper(), newSerie.getWallpaper());

		SerieEntity lastSerie = plataformaSerieService.getSerie(plataforma.getId(), newSerie.getId());

		assertEquals(lastSerie.getId(), newSerie.getId());
		assertEquals(lastSerie.getNombre(), newSerie.getNombre());
		assertEquals(lastSerie.getImagen(), newSerie.getImagen());
		assertEquals(lastSerie.getSinopsis(), newSerie.getSinopsis());
		assertEquals(lastSerie.getAnio(), newSerie.getAnio());
		assertEquals(lastSerie.getWallpaper(), newSerie.getWallpaper());

    }

    //Prueba para asociar una serie a una plataforma inexistente

    @Test
	void testAddSerieInvalidPlataforma() {
		assertThrows(EntityNotFoundException.class, () -> {
			SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			serieService.createSerie(newSerie);
			plataformaSerieService.addSerie(0L, newSerie.getId());
		});
	}

    //Prueba para asociar una serie inexistente a una plataforma

    @Test
	void testAddInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			plataformaSerieService.addSerie(plataforma.getId(), 0L);
		});
	}

    //Prueba para consultar la lista de series de una plataforma

    @Test
	void testGetSeries() throws EntityNotFoundException {
		List<SerieEntity> serieEntities = plataformaSerieService.getSeries(plataforma.getId());

		assertEquals(serieList.size(), serieEntities.size());

		for (int i = 0; i < serieList.size(); i++) {
			assertTrue(serieEntities.contains(serieList.get(0)));
		}
	}

    //Prueba para consultar la lista de series de una plataforma inexistente

    @Test
	void testGetSeriesInvalidPlataforma() {
		assertThrows(EntityNotFoundException.class, () -> {
			plataformaSerieService.getSeries(0L);
		});
	}

    //Prueba para consultar una serie de una plataforma

    @Test
	void testGetSerie() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity serieEntity = serieList.get(0);
		SerieEntity serie = plataformaSerieService.getSerie(plataforma.getId(), serieEntity.getId());
		assertNotNull(serie);

		assertEquals(serieEntity.getId(), serie.getId());
		assertEquals(serieEntity.getNombre(), serie.getNombre());
		assertEquals(serieEntity.getImagen(), serie.getImagen());
		assertEquals(serieEntity.getSinopsis(), serie.getSinopsis());
		assertEquals(serieEntity.getAnio(), serie.getAnio());
		assertEquals(serieEntity.getWallpaper(), serie.getWallpaper());
	}

    //Prueba para consultar una serie de una plataforma inexistente

    @Test
	void testGetSerieInvalidPlataforma() {
		assertThrows(EntityNotFoundException.class, () -> {
			SerieEntity serieEntity = serieList.get(0);
			plataformaSerieService.getSerie(0L, serieEntity.getId());
		});
	}

    //Prueba para consultar una serie inexistente de una plataforma

    @Test
	void testGetInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			plataformaSerieService.getSerie(plataforma.getId(), 0L);
		});
	}

    //Prueba para consultar una serie que no está asociada a una plataforma

    @Test
	void testGetSerieNotAssociatedPlataforma() {
		assertThrows(IllegalOperationException.class, () -> {
			PlataformaEntity plataformaEntity = factory.manufacturePojo(PlataformaEntity.class);
			entityManager.persist(plataformaEntity);

			SerieEntity serieEntity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(serieEntity);

			plataformaSerieService.getSerie(plataformaEntity.getId(), serieEntity.getId());
		});
	}
    
    //Prueba para actualizar las series de una plataforma

    @Test
	void testReplaceSeries() throws EntityNotFoundException, IllegalOperationException {
		List<SerieEntity> nuevaLista = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		
		plataformaSerieService.addSeries(plataforma.getId(), nuevaLista);
		
		List<SerieEntity> serieEntities = entityManager.find(PlataformaEntity.class, plataforma.getId()).getSeriesPlataforma();
		for (SerieEntity item : nuevaLista) {
			assertTrue(serieEntities.contains(item));
		}
	}

    //Prueba para actualizar las series de una plataforma inexistente

    @Test
	void testReplaceSerieInvalidPlataforma() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<SerieEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
				serieService.createSerie(entity);
				nuevaLista.add(entity);
			}
			plataformaSerieService.addSeries(0L, nuevaLista);
		});
	}

    //Prueba para actualizar las series que no existen en una plataforma

    @Test
	void testReplaceInvalidBooks() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<SerieEntity> nuevaLista = new ArrayList<>();
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			plataformaSerieService.addSeries(plataforma.getId(), nuevaLista);
		});
	}

    //Prueba desasociar una serie de una plataforma 

    @Test
	void testRemoveSerie() throws EntityNotFoundException {
		for (SerieEntity serie : serieList) {
			plataformaSerieService.removeSerie(plataforma.getId(), serie.getId());
		}
		assertTrue(plataformaSerieService.getSeries(plataforma.getId()).isEmpty());
	}

    //Prueba desasociar una serie de una plataforma inexistente

    @Test
	void testRemoveSerieInvalidPlataforma() {
		assertThrows(EntityNotFoundException.class, () -> {
			for (SerieEntity serie : serieList) {
				plataformaSerieService.removeSerie(0L, serie.getId());
			}
		});
	}

    //Prueba desasociar una serie inexistente de una plataforma

    @Test
	void testRemoveInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			plataformaSerieService.removeSerie(plataforma.getId(), 0L);
		});
	}
}
