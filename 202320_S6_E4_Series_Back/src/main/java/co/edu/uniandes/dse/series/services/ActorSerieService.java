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
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActorSerieService {

    @Autowired
    SerieRepository serieRepository;

    @Autowired
    ParticipanteRepository participanteRepository;

    @Transactional
	public SerieEntity addSerie(Long serieId, Long actorId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle una serie a un actor con id = {0}", serieId);
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		
		Optional<ParticipanteEntity> actorEntity = participanteRepository.findById(actorId);
		if(actorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		
		serieEntity.get().getActores().add(actorEntity.get());
		log.info("Termina proceso de agregarle una serie a un actor id = {0}", actorId);
		return serieEntity.get();
	}

	@Transactional
	public List<SerieEntity> getSeries(Long actorId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar las series asociados a un actor con id = {0}", actorId);
		Optional<ParticipanteEntity> actorEntity = participanteRepository.findById(actorId);
		if(actorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos las series del actor con id = {0}", actorId);
		return actorEntity.get().getSeriesActuadas();
	}


	@Transactional
	public SerieEntity getSerie(Long actorId, Long serieId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar la serie con id = {0} del actor con id = " + actorId, serieId);
		
		Optional<ParticipanteEntity> actorEntity = participanteRepository.findById(actorId);
		if(actorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
				
		log.info("Termina proceso de consultar serie con id = {0} del actor con id = " + actorId, serieId);

		if(!serieEntity.get().getActores().contains(actorEntity.get()))
			throw new IllegalOperationException("La serie no esta asociada con el actor");
		
		return serieEntity.get();
	}


	@Transactional
	public List<SerieEntity> replaceSeries(Long actorId, List<SerieEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las series asociadas al actor con id = {0}", actorId);
		Optional<ParticipanteEntity> actorEntity = participanteRepository.findById(actorId);
		if (actorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		for (SerieEntity serie : list) {
			Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
			if (serieEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

			if (!actorEntity.get().getSeriesActuadas().contains(serieEntity.get()))
				actorEntity.get().getSeriesActuadas().add(serieEntity.get());
		}
		log.info("Termina proceso de reemplazar las series asociadas al actor con id = {0}", actorId);
		return getSeries(actorId);
	
	}

	@Transactional
	public void removeSerie(Long actorId, Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una serie del actor con id = {0}", actorId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		Optional<ParticipanteEntity> participanteEntity = participanteRepository.findById(actorId);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		if (participanteEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		participanteEntity.get().getSeriesActuadas().remove(serieEntity.get());

		log.info("Termina proceso de borrar una serie del actor con id = {0}", actorId);
	}

}