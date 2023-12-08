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
@Import(SerieActorService.class)
public class SerieActorTest {
    
    @Autowired
    private SerieActorService serieActorService;

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
			entity.getSeriesActuadas().add(serie);
			participanteList.add(entity);
			serie.getActores().add(entity);	
		}
	}

    @Test
	void testAddActor() throws EntityNotFoundException, IllegalOperationException {
		SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
		entityManager.persist(newSerie);
		
		ParticipanteEntity participante = factory.manufacturePojo(ParticipanteEntity.class);
		entityManager.persist(participante);
		
		serieActorService.addActor(newSerie.getId(), participante.getId());
		
		ParticipanteEntity lastParticipante = serieActorService.getActor(newSerie.getId(), participante.getId());
		assertEquals(participante.getId(), lastParticipante.getId());
        assertEquals(participante.getNombre(), lastParticipante.getNombre());
        assertEquals(participante.getFoto(), lastParticipante.getFoto());
        assertEquals(participante.getDescripcion(), lastParticipante.getDescripcion());
	}

    @Test
	void testAddActorInvalidActor() {
		assertThrows(EntityNotFoundException.class, ()->{
		    SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
			entityManager.persist(newSerie);
			serieActorService.addActor(newSerie.getId(), 0L);
		});
	}

    @Test
	void testAddActorInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
		    ParticipanteEntity participante = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(participante);
			serieActorService.addActor(0L, participante.getId());
		});
	}

    @Test
	void testAddActores() throws EntityNotFoundException {
		List<ParticipanteEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		serieActorService.addActores(serie.getId(), nuevaLista);
		
		List<ParticipanteEntity> participanteEntities = serieActorService.getActores(serie.getId());
        assertEquals(participanteEntities.size(), nuevaLista.size() + participanteList.size());

		for (int i = 0; i < participanteList.size(); i++) {
			assertTrue(participanteEntities.contains(participanteList.get(i)));
		}

		for (int i = 0; i < nuevaLista.size(); i++) {
			assertTrue(participanteEntities.contains(nuevaLista.get(i)));
		}
	}

    @Test
	void testAddActoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            List<ParticipanteEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            serieActorService.addActores(0L, nuevaLista);
		});
	}

    @Test
	void testAddActoresInvalidActor() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ParticipanteEntity> nuevaLista = new ArrayList<>();
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			serieActorService.addActores(serie.getId(), nuevaLista);
		});
	}

	@Test
	void testGetActores() throws EntityNotFoundException {
		List<ParticipanteEntity> participanteEntities = serieActorService.getActores(serie.getId());
		assertEquals(participanteList.size(), participanteEntities.size());

		for (int i = 0; i < participanteList.size(); i++) {
			assertTrue(participanteEntities.contains(participanteList.get(i)));
		}
	}

    @Test
	void testGetActoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
			serieActorService.getActores(0L);
		});
	}

	@Test
	void testGetActor() throws EntityNotFoundException, IllegalOperationException {
		ParticipanteEntity participanteEntity = participanteList.get(0);
		ParticipanteEntity participante = serieActorService.getActor(serie.getId(), participanteEntity.getId());
		assertNotNull(participante);

		assertEquals(participante.getId(), participanteEntity.getId());
        assertEquals(participante.getNombre(), participanteEntity.getNombre());
        assertEquals(participante.getFoto(), participanteEntity.getFoto());
        assertEquals(participante.getDescripcion(), participanteEntity.getDescripcion());
    }

    @Test
	void testGetActoresInvalidActor() {
		assertThrows(EntityNotFoundException.class, ()->{
			serieActorService.getActor(serie.getId(), 0L);
		});
	}

    @Test
	void testGetActorInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            ParticipanteEntity participante = participanteList.get(0);
			serieActorService.getActor(0L, participante.getId());
		});
	}

    @Test
	void testGetActorNotAssociated() {
        assertThrows(IllegalOperationException.class, ()->{
            SerieEntity newSerie = factory.manufacturePojo(SerieEntity.class);
            entityManager.persist(newSerie);
            ParticipanteEntity newParticipante = factory.manufacturePojo(ParticipanteEntity.class);
            entityManager.persist(newParticipante);
            serieActorService.getActor(newSerie.getId(), newParticipante.getId());
        });
    }

    @Test
	void testReplaceActores() throws EntityNotFoundException {
		List<ParticipanteEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		serieActorService.replaceActores(serie.getId(), nuevaLista);
		
		List<ParticipanteEntity> participanteEntities = serieActorService.getActores(serie.getId());
        assertEquals(nuevaLista.size(), participanteEntities.size());

		for (int i = 0; i < nuevaLista.size(); i++) {
			assertTrue(participanteEntities.contains(nuevaLista.get(i)));
		}
	}

    @Test
	void testReplaceActoresInvalidSerie() {
		assertThrows(EntityNotFoundException.class, ()->{
            List<ParticipanteEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            serieActorService.replaceActores(0L, nuevaLista);
		});
	}

    @Test
	void testReplaceActoresInvalidActor() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ParticipanteEntity> nuevaLista = new ArrayList<>();
			ParticipanteEntity entity = factory.manufacturePojo(ParticipanteEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			serieActorService.replaceActores(serie.getId(), nuevaLista);
		});
	}

	@Test
	void testRemoveActor() throws EntityNotFoundException {
		for (ParticipanteEntity participante : participanteList) {
			serieActorService.removeActor(serie.getId(), participante.getId());
		}
		assertTrue(serieActorService.getActores(serie.getId()).isEmpty());
	}

	@Test
	void testRemoveActorInvalidActor(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieActorService.removeActor(serie.getId(), 0L);
		});
	}

	@Test
	void testRemoveActorInvalidSerie(){
		assertThrows(EntityNotFoundException.class, ()->{
			serieActorService.removeActor(0L, participanteList.get(0).getId());
		});
	}

}
