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

import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ CategoriaSerieService.class, SerieService.class })

public class CategoriaSerieServiceTest {

    @Autowired
	private CategoriaSerieService categoriaSerieService;

	@Autowired
	private SerieService serieService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private CategoriaEntity categoria = new CategoriaEntity();
	private List<SerieEntity> serieList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from CategoriaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		categoria = factory.manufacturePojo(CategoriaEntity.class);
		entityManager.persist(categoria);

		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entity.getCategorias().add(categoria);
			entityManager.persist(entity);
			serieList.add(entity);
			categoria.getSeries().add(entity);
		}
	}

	/**
	 * Prueba para asociar un libro a un author.
	 *
	 */
	@Test
	void testAddSerie() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
		serieService.createSerie(newSerie);

		SerieEntity serieEntity = categoriaSerieService.addSerie(newSerie.getId(),categoria.getId());
		assertNotNull(serieEntity);

		assertEquals(serieEntity.getId(), newSerie.getId());
		assertEquals(serieEntity.getNombre(), newSerie.getNombre());
        assertEquals(serieEntity.getCategorias(), newSerie.getCategorias());
        assertEquals(serieEntity.getImagen(), newSerie.getImagen());


		SerieEntity lastSerie = categoriaSerieService.getSerie(categoria.getId(), newSerie.getId());

		assertEquals(lastSerie.getId(), newSerie.getId());
		assertEquals(lastSerie.getNombre(), newSerie.getNombre());
        assertEquals(lastSerie.getCategorias(), newSerie.getCategorias());
        assertEquals(lastSerie.getImagen(), newSerie.getImagen());


	}
	

	/**
	 * Prueba para asociar un libro a un author que no existe.
	 *
	 */

	@Test
	void testAddSerieInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, () -> {
			SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			serieService.createSerie(newSerie);
			categoriaSerieService.addSerie(0L, newSerie.getId());
		});
	}

	/**
	 * Prueba para asociar un libro que no existe a un author.
	 *
	 */
	@Test
	void testAddInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			categoriaSerieService.addSerie(categoria.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar la lista de libros de un autor.
	 */
	@Test
	void testGetSeries() throws EntityNotFoundException {
		List<SerieEntity> serieEntities = categoriaSerieService.getSeries(categoria.getId());

		assertEquals(serieList.size(), serieEntities.size());

		for (int i = 0; i < serieList.size(); i++) {
			assertTrue(serieEntities.contains(serieList.get(0)));
		}
	}

	/**
	 * Prueba para consultar la lista de libros de un autor que no existe.
	 */
	@Test
	void testGetSeriesInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, () -> {
			categoriaSerieService.getSeries(0L);
		});
	}

	/**
	 * Prueba para consultar un libro de un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetSerie() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity serieEntity = serieList.get(0);
		SerieEntity serie = categoriaSerieService.getSerie(categoria.getId(), serieEntity.getId());
		assertNotNull(serie);

        assertEquals(serieEntity.getId(), serie.getId());
		assertEquals(serieEntity.getNombre(), serie.getNombre());
        assertEquals(serieEntity.getCategorias(), serie.getCategorias());
        assertEquals(serieEntity.getImagen(), serie.getImagen());

	}

	/**
	 * Prueba para consultar un libro de un autor que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetSerieInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, () -> {
			SerieEntity serieEntity = serieList.get(0);
			categoriaSerieService.getSerie(0L, serieEntity.getId());
		});
	}

	/**
	 * Prueba para consultar un libro que no existe de un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			categoriaSerieService.getSerie(categoria.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un libro que no está asociado a un autor.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetSerieNotAssociatedCategoria() {
		assertThrows(IllegalOperationException.class, () -> {
			CategoriaEntity categoriaEntity = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(categoriaEntity);

			SerieEntity serieEntity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(serieEntity);

			categoriaSerieService.getSerie(categoriaEntity.getId(), serieEntity.getId());
		});
	}

	/**
	 * Prueba para actualizar los libros de un autor.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceSeries() throws EntityNotFoundException, IllegalOperationException {
		List<SerieEntity> nuevaLista = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		
		categoriaSerieService.addSeries(categoria.getId(), nuevaLista);
		
		List<SerieEntity> serieEntities = entityManager.find(CategoriaEntity.class, categoria.getId()).getSeries();
		for (SerieEntity item : nuevaLista) {
			assertTrue(serieEntities.contains(item));
		}
	}
	
	/**
	 * Prueba para actualizar los libros de un autor que no existe.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceSeriesInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<SerieEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
				serieService.createSerie(entity);
				nuevaLista.add(entity);
			}
			categoriaSerieService.addSeries(0L, nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar los libros que no existen de un autor.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testReplaceInvalidSeries() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<SerieEntity> nuevaLista = new ArrayList<>();
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			categoriaSerieService.addSeries(categoria.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un libro con un autor.
	 *
	 */
	@Test
	void testRemoveSerie() throws EntityNotFoundException {
		for (SerieEntity serie : serieList) {
			categoriaSerieService.removeSerie(categoria.getId(), serie.getId());
		}
		assertTrue(categoriaSerieService.getSeries(categoria.getId()).isEmpty());
	}

	/**
	 * Prueba desasociar un libro con un autor que no existe.
	 *
	 */
	@Test
	void testRemoveSerieInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, () -> {
			for (SerieEntity serie : serieList) {
				categoriaSerieService.removeSerie(0L, serie.getId());
			}
		});
	}

	/**
	 * Prueba desasociar un libro que no existe con un autor.
	 *
	 */
	@Test
	void testRemoveInvalidSerie() {
		assertThrows(EntityNotFoundException.class, () -> {
			categoriaSerieService.removeSerie(categoria.getId(), 0L);
		});
	}
    
}
