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

import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(ParticipanteService.class)
public class ParticipanteServiceTest {

    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ParticipanteEntity> participanteList = new ArrayList<>();
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
            entityManager.getEntityManager().createQuery("delete from ParticipanteEntity");
            entityManager.getEntityManager().createQuery("delete from SerieEntity");
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {

        for (int i = 0; i < 3; i++) {
                    ParticipanteEntity participanteEntity = factory.manufacturePojo(ParticipanteEntity.class);
                    entityManager.persist(participanteEntity);
                    participanteList.add(participanteEntity);
            }

        for (int i = 0; i < 3; i++) {
                SerieEntity serieEntity = factory.manufacturePojo(SerieEntity.class);
                entityManager.persist(serieEntity);
                serieList.add(serieEntity);
        }
    }

    @Test
        void testCreateParticipante() throws EntityNotFoundException, IllegalOperationException {
        ParticipanteEntity newEntity = factory.manufacturePojo(ParticipanteEntity.class);
        ParticipanteEntity result = participanteService.createParticipante(newEntity);
        assertNotNull(result);
        ParticipanteEntity entity = entityManager.find(ParticipanteEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
        assertEquals(newEntity.getFoto(), entity.getFoto());
}

        @Test
        void testCreateParticipanteWithNoValidNombre() {
                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity newEntity = factory.manufacturePojo(ParticipanteEntity.class);
                        newEntity.setNombre("");
                        participanteService.createParticipante(newEntity);
                });
}

        @Test
        void testCreateParticipanteWithNoValidNombre2() {
                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity newEntity = factory.manufacturePojo(ParticipanteEntity.class);
                        newEntity.setNombre(null);
                        participanteService.createParticipante(newEntity);
                });
}
        @Test
        void testCreateParticipanteWithNoValidDescripcion() {
                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity newEntity = factory.manufacturePojo(ParticipanteEntity.class);
                        newEntity.setDescripcion("a pellentesque sit amet porttitor eget dolor morbi non arcu risus quis varius quam quisque id diam vel quam elementum pulvinar etiam non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet consectetur adipiscing elit duis tristique sollicitudin nibh sit amet commodo nulla facilisi nullam vehicula ipsum a");
                        participanteService.createParticipante(newEntity);
                });
}

        @Test
        void testCreateParticipanteWithNoValidFoto() {
                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity newEntity = factory.manufacturePojo(ParticipanteEntity.class);
                        newEntity.setFoto(null);
                        participanteService.createParticipante(newEntity);
                });
}


        @Test
        void testGetParticipantes() {
                List<ParticipanteEntity> list = participanteService.getParticipantes();
                assertEquals(participanteList.size(), list.size());
                for (ParticipanteEntity entity : list) {
                        boolean found = false;
                        for (ParticipanteEntity storedEntity : participanteList) {
                                if (entity.getId().equals(storedEntity.getId())) {
                                        found = true;
                                }
                        }
                        assertTrue(found);
                }
}
        @Test
        void testGetParticipante() throws EntityNotFoundException {
                ParticipanteEntity entity = participanteList.get(0);
                ParticipanteEntity resultEntity = participanteService.getParticipanteById(entity.getId());
                assertNotNull(resultEntity);
                assertEquals(entity.getId(), resultEntity.getId());
                assertEquals(entity.getNombre(), resultEntity.getNombre());
                assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
                assertEquals(entity.getFoto(), resultEntity.getFoto());
}

        @Test
        void testGetInvalidParticipante() {
                assertThrows(EntityNotFoundException.class,()->{
                        participanteService.getParticipanteById(0L);
                });
}

        @Test
        void testUpdateParticipante() throws EntityNotFoundException, IllegalOperationException {
                ParticipanteEntity entity = participanteList.get(0);
                ParticipanteEntity pojoEntity = factory.manufacturePojo(ParticipanteEntity.class);
                pojoEntity.setId(entity.getId());
                participanteService.updateParticipante(entity.getId(), pojoEntity);

                ParticipanteEntity resp = entityManager.find(ParticipanteEntity.class, entity.getId());
                assertEquals(pojoEntity.getId(), resp.getId());
                assertEquals(pojoEntity.getNombre(), resp.getNombre());
                assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
                assertEquals(pojoEntity.getFoto(), resp.getFoto());
}

        @Test
        void testUpdateParticipanteInvalidId() {
                assertThrows(EntityNotFoundException.class, () -> {
                ParticipanteEntity pojoEntity = factory.manufacturePojo(ParticipanteEntity.class);
                pojoEntity.setId(0L);
                participanteService.updateParticipante(0L, pojoEntity);
        });
        }
        @Test
        void testUpdateParticipanteInvalidNombre() {
                assertThrows(IllegalOperationException.class, () -> {
                ParticipanteEntity pojoEntity = factory.manufacturePojo(ParticipanteEntity.class);
                pojoEntity.setNombre(null);
                participanteService.updateParticipante(participanteList.get(0).getId(), pojoEntity);
        });
}


        @Test
        void testDeleteParticipante() throws EntityNotFoundException, IllegalOperationException {
                ParticipanteEntity entity = participanteList.get(1);
                List<SerieEntity> actuadas = entity.getSeriesActuadas();
                List<SerieEntity> dirigidas = entity.getSeriesDirigidas();
                if (actuadas.isEmpty() && dirigidas.isEmpty())
                        participanteService.deleteParticipante(entity.getId());
                        ParticipanteEntity deleted = entityManager.find(ParticipanteEntity.class, entity.getId());
                        assertNull(deleted);
}

        @Test
        void testDeleteInvalidParticipante() {
                assertThrows(EntityNotFoundException.class, ()->{
                        participanteService.deleteParticipante(0L);
                });
}

        @Test
        void testDeleteParticipanteWithSerieActuada() {


                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity entity =  participanteList.get(0);
                        SerieEntity fakeSerie = factory.manufacturePojo(SerieEntity.class);
                        List<SerieEntity> seriesActuadasFake = new ArrayList<>();
                        seriesActuadasFake.add(fakeSerie);
                        entity.setSeriesActuadas(seriesActuadasFake);
                        participanteService.deleteParticipante(entity.getId());
                });

}
        @Test
        void testDeleteParticipanteWithSerieDirigida() {


                assertThrows(IllegalOperationException.class, () -> {
                        ParticipanteEntity entity =  participanteList.get(0);
                        SerieEntity fakeSerie = factory.manufacturePojo(SerieEntity.class);
                        List<SerieEntity> seriesDirigidasFake = new ArrayList<>();
                        seriesDirigidasFake.add(fakeSerie);
                        entity.setSeriesDirigidas(seriesDirigidasFake);
                        participanteService.deleteParticipante(entity.getId());
                });

}
}