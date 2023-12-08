package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.repositories.ParticipanteRepository;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SerieDirectorService {
        
    @Autowired
    SerieRepository serieRepository;

    @Autowired
    ParticipanteRepository participanteRepository;

    @Transactional
    public ParticipanteEntity addDirector(Long serieId, Long participanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un director a la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		if (participanteEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		serieEntity.get().getDirectores().add(participanteEntity.get());
		log.info("Termina proceso de asociarle un director a la serie con id = {0}", serieId);
		return participanteEntity.get();
    }

	@Transactional
	public List<ParticipanteEntity> addDirectores(Long serieId, List<ParticipanteEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de agregar los directores de la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		for (ParticipanteEntity participante : list) {
			Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participante.getId());
			if (participanteEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

			if (!serieEntity.get().getDirectores().contains(participanteEntity.get()))
				serieEntity.get().getDirectores().add(participanteEntity.get());
		}

		log.info("Termina proceso de agregar los directores de la serie con id = {0}", serieId);
		return getDirectores(serieId);
	}

	@Transactional
	public List<ParticipanteEntity> getDirectores(Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los directores de la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los directores de la serie con id = {0}", serieId);
		return serieEntity.get().getDirectores();
	}

	@Transactional
	public ParticipanteEntity getDirector(Long serieId, Long participanteId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un director de la serie con id = {0}", serieId);
		Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (participanteEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		
		if (!serieEntity.get().getDirectores().contains(participanteEntity.get()))
			throw new IllegalOperationException("El director no se encuentra asociado a la serie");
		
		log.info("Termina proceso de consultar un director de la serie con id = {0}", serieId);
		return participanteEntity.get();
	}

	@Transactional
	public List<ParticipanteEntity> replaceDirectores(Long serieId, List<ParticipanteEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los directores de la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		for (ParticipanteEntity participante : list) {
			Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participante.getId());
			if (participanteEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		}
		
		serieEntity.get().setDirectores(list);
		log.info("Termina proceso de reemplazar los directores de la serie con id = {0}", serieId);
		return getDirectores(serieId);
	}

	@Transactional
	public void removeDirector(Long serieId, Long participanteId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un director de la serie con id = {0}", serieId);
		Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(participanteId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (participanteEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getDirectores().remove(participanteEntity.get());
		log.info("Termina proceso de borrar un director de la serie con id = {0}", serieId);
	}
}
