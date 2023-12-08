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

/**
 * Pruebas de logica de la relacion Serie - Categoria
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(SerieCategoriaService.class)
public class SerieCategoriaServiceTest {
    
    @Autowired
	private SerieCategoriaService serieCategoriaService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private SerieEntity serie = new SerieEntity();

	private List<CategoriaEntity> categoriaList = new ArrayList<>();

	
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

		serie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(serie);

		for (int i = 0; i < 3; i++) {
			CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(entity);
			entity.getSeries().add(serie);
			categoriaList.add(entity);
			serie.getCategorias().add(entity);	
		}
	}

	/**
	 * Prueba para asociar un autor a un libro.
	 *
	 */
	@Test
	void testAddCategoria() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(newSerie);
		
		CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
		entityManager.persist(categoria);
		
		serieCategoriaService.addCategoria(newSerie.getId(), categoria.getId());
		
		CategoriaEntity lastCategoria = serieCategoriaService.getCategoria(newSerie.getId(), categoria.getId());
		assertEquals(categoria.getId(), lastCategoria.getId());
		assertEquals(categoria.getNombre(), lastCategoria.getNombre());
		assertEquals(categoria.getSeries(), lastCategoria.getSeries());
	}
	
	/**
	 * Prueba para asociar un autor que no existe a un libro.
	 *
	 */
	@Test
	void testAddInvalidCategoria() {
		assertThrows(EntityNotFoundException.class, ()->{
			SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(newSerie);
			serieCategoriaService.addCategoria(newSerie.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un autor a un libro que no existe.
	 *
	 */
	@Test
	void testAddCategoriaInvalidSerie() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(categoria);
			serieCategoriaService.addCategoria(0L, categoria.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de autores de un libro.
	 */
	@Test
	void testGetCategorias() throws EntityNotFoundException {
		List<CategoriaEntity>categoriaEntities = serieCategoriaService.getCategorias(serie.getId());

		assertEquals(categoriaList.size(), categoriaEntities.size());

		for (int i = 0; i < categoriaList.size(); i++) {
			assertTrue(categoriaEntities.contains(categoriaList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de autores de un libro que no existe.
	 */
	@Test
	void testGetCategoriasInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieCategoriaService.getCategorias(0L);
		});
	}

	/**
	 * Prueba para consultar un autor de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetCategoria() throws EntityNotFoundException, IllegalOperationException {
		CategoriaEntity categoriaEntity = categoriaList.get(0);
		CategoriaEntity categoria = serieCategoriaService.getCategoria(serie.getId(), categoriaEntity.getId());
		assertNotNull(categoria);

		assertEquals(categoriaEntity.getId(), categoria.getId());
		assertEquals(categoriaEntity.getSeries(), categoria.getSeries());
		assertEquals(categoriaEntity.getNombre(), categoria.getNombre());
	}
	
	/**
	 * Prueba para consultar un autor que no existe de un libro.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidCategoria()  {
		assertThrows(EntityNotFoundException.class, ()->{
			serieCategoriaService.getCategoria(serie.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un autor de un libro que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetCategoriaInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
			CategoriaEntity categoriaEntity = categoriaList.get(0);
			serieCategoriaService.getCategoria(0L, categoriaEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un autor no asociado a un libro.
	 *
	 */
	@Test
	void testGetNotAssociatedCategoria() {
		assertThrows(IllegalOperationException.class, ()->{
			SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(newSerie);
			CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(categoria);
			serieCategoriaService.getCategoria(newSerie.getId(), categoria.getId());
		});
	}

	/**
	 * Prueba para actualizar los autores de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceCategorias() throws EntityNotFoundException {
		List<CategoriaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(entity);
			serie.getCategorias().add(entity);
			nuevaLista.add(entity);
		}
		serieCategoriaService.replaceCategorias(serie.getId(), nuevaLista);
		
		List<CategoriaEntity> categoriaEntities = serieCategoriaService.getCategorias(serie.getId());
		for (CategoriaEntity aNuevaLista : nuevaLista) {
			assertTrue(categoriaEntities.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los autores de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceCategorias2() throws EntityNotFoundException {
		List<CategoriaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		serieCategoriaService.replaceCategorias(serie.getId(), nuevaLista);
		
		List<CategoriaEntity> categoriaEntities = serieCategoriaService.getCategorias(serie.getId());
		for (CategoriaEntity aNuevaLista : nuevaLista) {
			assertTrue(categoriaEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los autores de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceCategoriasInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<CategoriaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
				entity.getSeries().add(serie);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			serieCategoriaService.replaceCategorias(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los autores que no existen de un libro.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidCategorias() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<CategoriaEntity> nuevaLista = new ArrayList<>();
			CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			serieCategoriaService.replaceCategorias(serie.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un autor de un libro que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceCategoriasInvalidCategoria(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<CategoriaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				CategoriaEntity entity = factory.manufacturePojo(CategoriaEntity.class);
				entity.getSeries().add(serie);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			serieCategoriaService.replaceCategorias(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un autor con un libro.
	 *
	 */
	@Test
	void testRemoveCategoria() throws EntityNotFoundException {
		for (CategoriaEntity categoria : categoriaList) {
			serieCategoriaService.removeCategoria(serie.getId(), categoria.getId());
		}
		assertTrue(serieCategoriaService.getCategorias(serie.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un autor que no existe con un libro.
	 *
	 */
	@Test
	void testRemoveInvalidCategoria(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieCategoriaService.removeCategoria(serie.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un autor con un libro que no existe.
	 *
	 */
	@Test
	void testRemoveCategoriaInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieCategoriaService.removeCategoria(0L, categoriaList.get(0).getId());
		});
	}
}
