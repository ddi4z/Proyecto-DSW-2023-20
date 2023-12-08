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

import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(SerieDirectorService.class)
public class SerieDirectorTest {
    
    @Autowired
    private SerieDirectorService serieDirectorService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private SerieEntity serie;

    private List<ParticipanteEntity> participanteList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SerieEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ParticipanteEntity").executeUpdate();
    }

    private void insertData() {
		serie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(serie);

		for (int i = 0; i < 3; i++) {
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(entity);
			entity.getSeriesDirigidas().add(serie);
			participanteList.add(entity);
			serie.getDirectores().add(entity);	
		}
	}

    @Test
	void testAddDirector() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(newSerie);
		
		ParticipanteEntity participante = factory.manufacturePojo(ParticipanteEntity.class);
		entityManager.persist(participante);
		
		serieDirectorService.addDirector(newSerie.getId(), participante.getId());
		
		ParticipanteEntity lastParticipante = serieDirectorService.getDirector(newSerie.getId(), participante.getId());
		assertEquals(participante.getId(), lastParticipante.getId());
        assertEquals(participante.getNombre(), lastParticipante.getNombre());
        assertEquals(participante.getFoto(), lastParticipante.getFoto());
        assertEquals(participante.getDescripcion(), lastParticipante.getDescripcion());
	}

    @Test
	void testAddDirectorInvalidDirector() {
		assertThrows(EntityNotFoundException.class, ()->{
		    SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(newSerie);
			serieDirectorService.addDirector(newSerie.getId(), 0L);
		});
	}

    @Test
	void testAddDirectorInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
		    ParticipanteEntity participante = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(participante);
			serieDirectorService.addDirector(0L, participante.getId());
		});
	}

    @Test
	void testAddDirectores() throws EntityNotFoundException {
		List<ParticipanteEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		serieDirectorService.addDirectores(serie.getId(), nuevaLista);
		
		List<ParticipanteEntity> participanteEntities = serieDirectorService.getDirectores(serie.getId());
        assertEquals(participanteEntities.size(), nuevaLista.size() + participanteList.size());

		for (int i = 0; i < participanteList.size(); i++) {
			assertTrue(participanteEntities.contains(participanteList.get(i)));
		}

		for (int i = 0; i < nuevaLista.size(); i++) {
			assertTrue(participanteEntities.contains(nuevaLista.get(i)));
		}
	}

    @Test
	void testAddDirectoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            List<ParticipanteEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            serieDirectorService.addDirectores(0L, nuevaLista);
		});
	}

    @Test
	void testAddDirectoresInvalidDirector() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ParticipanteEntity> nuevaLista = new ArrayList<>();
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			serieDirectorService.addDirectores(serie.getId(), nuevaLista);
		});
	}

	@Test
	void testGetDirectores() throws EntityNotFoundException {
		List<ParticipanteEntity> participanteEntities = serieDirectorService.getDirectores(serie.getId());
		assertEquals(participanteList.size(), participanteEntities.size());

		for (int i = 0; i < participanteList.size(); i++) {
			assertTrue(participanteEntities.contains(participanteList.get(i)));
		}
	}

    @Test
	void testGetDirectoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
			serieDirectorService.getDirectores(0L);
		});
	}

	@Test
	void testGetDirector() throws EntityNotFoundException, IllegalOperationException {
		ParticipanteEntity participanteEntity = participanteList.get(0);
		ParticipanteEntity participante = serieDirectorService.getDirector(serie.getId(), participanteEntity.getId());
		assertNotNull(participante);

		assertEquals(participante.getId(), participanteEntity.getId());
        assertEquals(participante.getNombre(), participanteEntity.getNombre());
        assertEquals(participante.getFoto(), participanteEntity.getFoto());
        assertEquals(participante.getDescripcion(), participanteEntity.getDescripcion());
    }

    @Test
	void testGetDirectoresInvalidDirector() {
		assertThrows(EntityNotFoundException.class, ()->{
			serieDirectorService.getDirector(serie.getId(), 0L);
		});
	}

    @Test
	void testGetDirectorInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            ParticipanteEntity participante = participanteList.get(0);
			serieDirectorService.getDirector(0L, participante.getId());
		});
	}

    @Test
	void testGetDirectorNotAssociated() {
        assertThrows(IllegalOperationException.class, ()->{
            SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            ParticipanteEntity newParticipante = factory.manufacturePojo(ParticipanteEntity.class);
            entityManager.persist(newParticipante);
            serieDirectorService.getDirector(newSerie.getId(), newParticipante.getId());
        });
    }

    @Test
	void testReplaceDirectores() throws EntityNotFoundException {
		List<ParticipanteEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		serieDirectorService.replaceDirectores(serie.getId(), nuevaLista);
		
		List<ParticipanteEntity> participanteEntities = serieDirectorService.getDirectores(serie.getId());
        assertEquals(nuevaLista.size(), participanteEntities.size());

		for (int i = 0; i < nuevaLista.size(); i++) {
			assertTrue(participanteEntities.contains(nuevaLista.get(i)));
		}
	}

    @Test
	void testReplaceDirectoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            List<ParticipanteEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            serieDirectorService.replaceDirectores(0L, nuevaLista);
		});
	}

    @Test
	void testReplaceDirectoresInvalidDirector() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ParticipanteEntity> nuevaLista = new ArrayList<>();
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			serieDirectorService.replaceDirectores(serie.getId(), nuevaLista);
		});
	}

	@Test
	void testRemoveDirector() throws EntityNotFoundException {
		for (ParticipanteEntity participante : participanteList) {
			serieDirectorService.removeDirector(serie.getId(), participante.getId());
		}
		assertTrue(serieDirectorService.getDirectores(serie.getId()).isEmpty());
	}

	@Test
	void testRemoveDirectorInvalidDirector(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieDirectorService.removeDirector(serie.getId(), 0L);
		});
	}

	@Test
	void testRemoveDirectorInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieDirectorService.removeDirector(0L, participanteList.get(0).getId());
		});
	}

}
