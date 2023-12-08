package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.ParticipanteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParticipanteService {

        @Autowired
        ParticipanteRepository participanteRepository;


@Transactional
public ParticipanteEntity createParticipante(ParticipanteEntity participanteEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación del libro");

        if (participanteEntity.getDescripcion().length() > 150)
                throw new IllegalOperationException("Descripción no valida");

        if (participanteEntity.getNombre() == null || participanteEntity.getNombre().equals(""))
                throw new IllegalOperationException("Nombre vacio");

        if (participanteEntity.getFoto() == null)
                throw new IllegalOperationException("Foto vacia");


        log.info("Termina proceso de creación de participante");
        return participanteRepository.save(participanteEntity);
}

@Transactional
public List<ParticipanteEntity> getParticipantes() {
        log.info("Inicia proceso de consultar todos participantes");
        return participanteRepository.findAll();
}

@Transactional
public List<ParticipanteEntity> getParticipantesAlfabeticamente() {
        log.info("Inicia proceso de consultar todos participantes por orden alfabetico");
        List<ParticipanteEntity> participantes = participanteRepository.findAll();
        participantes.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
        return participantes;
}

@Transactional
public List<ParticipanteEntity> getParticipantesAntiAlfabeticamente() {
        log.info("Inicia proceso de consultar todos participantes por orden anti alfabetico");
        List<ParticipanteEntity> participantes = participanteRepository.findAll();
        participantes.sort((p1, p2) -> p2.getNombre().compareTo(p1.getNombre()));
        return participantes;
}

@Transactional
public ParticipanteEntity getParticipanteById(Long participanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar un participante con id  = {0}", participanteId);
        Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);
        if (participanteEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
        log.info("Termina proceso de consultar de participante con id = {0}", participanteId);
        return participanteEntity.get();
}

@Transactional
public ParticipanteEntity updateParticipante(Long participanteId, ParticipanteEntity participante)
                        throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el participante con id = {0}", participante);
        Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);
        if (participanteEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

        if (participante.getNombre() == null)
                throw new IllegalOperationException("Nombre no valido");

        participante.setId(participanteId);
        log.info("Termina proceso de actualizar el participante con id = {0}", participanteId);
        return participanteRepository.save(participante);
}

@Transactional
public void deleteParticipante(Long participanteId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el participante con id = {0}", participanteId);
        Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);
        if (participanteEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

        List<SerieEntity> seriesActuadas = participanteEntity.get().getSeriesActuadas();
        List<SerieEntity> seriesDirigidas = participanteEntity.get().getSeriesDirigidas();

        if (!seriesActuadas.isEmpty() || !seriesDirigidas.isEmpty())
                throw new IllegalOperationException("Imposible borrar participante porque tiene una serie asociada");

        participanteRepository.deleteById(participanteId);
        log.info("Termina proceso de borrar el participante con id = {0}", participanteId);
}

}