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

import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(DirectorSerieService.class)


public class DirectorSerieServiceTest {
    
    @Autowired
	private DirectorSerieService directorSerieService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private ParticipanteEntity participante = new ParticipanteEntity();
	private SerieEntity serie = new SerieEntity();
	private List<SerieEntity> serieList = new ArrayList<>();

	
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
	
	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ParticipanteEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		serie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(serie);

		participante = factory.manufacturePojo(ParticipanteEntity.class);
		participante.setSeriesDirigidas(serieList);
		entityManager.persist(participante);

		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(entity);
			entity.getDirectores().add(participante);
			serieList.add(entity);
			participante.getSeriesDirigidas().add(entity);	
		}
	}

	/**
	 * Prueba para asociar un autor a un libro.
	 *
	 */
	@Test
	void testAddSerie() throws EntityNotFoundException, IllegalOperationException {
		ParticipanteEntity newParticipante = factory.manufacturePojo(ParticipanteEntity.class);
		newParticipante.setSeriesDirigidas(serieList);
		entityManager.persist(newParticipante);
		
		SerieEntity serie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(serie);
		
		directorSerieService.addSerie(serie.getId(),newParticipante.getId());
		
		SerieEntity lastSerie = directorSerieService.getSerie(newParticipante.getId(), serie.getId());
		assertEquals(serie.getId(), lastSerie.getId());
		assertEquals(serie.getSinopsis(), lastSerie.getSinopsis());
		assertEquals(serie.getImagen(), lastSerie.getImagen());
	}
	
	/**
	 * Prueba para asociar un autor que no existe a un libro.
	 *
	 */
	@Test
	void testAddInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
			ParticipanteEntity newParticipante = factory.manufacturePojo(ParticipanteEntity.class);
			newParticipante.setSeriesDirigidas(serieList);
			entityManager.persist(newParticipante);
			directorSerieService.addSerie(newParticipante.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un autor a un libro que no existe.
	 *
	 */
	@Test
	void testAddSerieInvalidParticipante() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			SerieEntity serie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(serie);
			directorSerieService.addSerie(0L, serie.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de autores de un libro.
	 */
	@Test
	void testGetSerie() throws EntityNotFoundException {
		List<SerieEntity> serieEntities = directorSerieService.getSeries(participante.getId());

		assertEquals(serieList.size(), serieEntities.size());

		for (int i = 0; i < serieList.size(); i++) {
			assertTrue(serieEntities.contains(serieList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de autores de un libro que no existe.
	 */
	@Test
	void testGetSeriesInvalidParticipante(){
		assertThrows(EntityNotFoundException.class, ()->{
			directorSerieService.getSeries(0L);
		});
	}

	/**
	 * Prueba para consultar un autor de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetSeries() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity serieEntity = serieList.get(0);
		SerieEntity serie = directorSerieService.getSerie(participante.getId(), serieEntity.getId());
		assertNotNull(serie);

		assertEquals(serieEntity.getId(), serie.getId());
		assertEquals(serieEntity.getSinopsis(), serie.getSinopsis());
		assertEquals(serieEntity.getImagen(), serie.getImagen());
	}
	
	/**
	 * Prueba para consultar un autor que no existe de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidSerie()  {
		assertThrows(EntityNotFoundException.class, ()->{
			directorSerieService.getSerie(participante.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un autor de un libro que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetSerieInvalidParticipante() {
		assertThrows(EntityNotFoundException.class, ()->{
			SerieEntity serieEntity = serieList.get(0);
			directorSerieService.getSerie(0L, serieEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un autor no asociado a un libro.
	 *
	 */
	@Test
	void testGetNotAssociatedSerie() {
		assertThrows(IllegalOperationException.class, ()->{
			ParticipanteEntity newParticipante = factory.manufacturePojo(ParticipanteEntity.class);
			newParticipante.setSeriesDirigidas(serieList);
			entityManager.persist(newParticipante);
			SerieEntity serie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(serie);
			directorSerieService.getSerie(newParticipante.getId(), serie.getId());
		});
	}

	/**
	 * Prueba para actualizar los autores de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceSeries() throws EntityNotFoundException {
		List<SerieEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(entity);
			participante.getSeriesDirigidas().add(entity);
			nuevaLista.add(entity);
		}
		directorSerieService.replaceSeries(participante.getId(), nuevaLista);
		
		List<SerieEntity> serieEntities = directorSerieService.getSeries(participante.getId());
		for (SerieEntity aNuevaLista : nuevaLista) {
			assertTrue(serieEntities.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los autores de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceAuthors() throws EntityNotFoundException {
		List<SerieEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		directorSerieService.replaceSeries(participante.getId(), nuevaLista);
		
		List<SerieEntity> serieEntities = directorSerieService.getSeries(participante.getId());
		for (SerieEntity aNuevaLista : nuevaLista) {
			assertTrue(serieEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los autores de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceSeriesInvalidParticipante(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<SerieEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
				entity.getDirectores().add(participante);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			directorSerieService.replaceSeries(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los autores que no existen de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidSeries() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<SerieEntity> nuevaLista = new ArrayList<>();
			SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			directorSerieService.replaceSeries(participante.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un autor de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceSeriesInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<SerieEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				SerieEntity entity = factory.manufacturePojo(SerieEntity.class);
				entity.getDirectores().add(participante);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			directorSerieService.replaceSeries(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un autor con un libro.
	 *
	 */
	@Test
	void testRemoveSerie() throws EntityNotFoundException {

        for (int i = 0; i < serieList.size(); i++) {

            directorSerieService.removeSerie(participante.getId(), serieList.get(i).getId());

			i -= 1;
        }
		


        assertTrue(directorSerieService.getSeries(participante.getId()).isEmpty());

        /** 
		for (SerieEntity serie : serieList) {
			actorSerieService.removeSerie(participante.getId(), serie.getId());
		}
		assertTrue(actorSerieService.getSeries(participante.getId()).isEmpty());
        */
	}
	
	/**
	 * Prueba desasociar un autor que no existe con un libro.
	 *
	 */
	@Test
	void testRemoveInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			directorSerieService.removeSerie(participante.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un autor con un libro que no existe.
	 *
	 */
	@Test
	void testRemoveSerieInvalidParticipante(){
		assertThrows(EntityNotFoundException.class, ()->{
			directorSerieService.removeSerie(0L, serieList.get(0).getId());
		});
	}


}
