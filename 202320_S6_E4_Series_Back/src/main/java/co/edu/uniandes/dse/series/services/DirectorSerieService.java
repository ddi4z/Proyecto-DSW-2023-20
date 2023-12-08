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
public class DirectorSerieService {

    @Autowired
    SerieRepository serieRepository;

    @Autowired
    ParticipanteRepository directorRepository;

    @Transactional
	public SerieEntity addSerie(Long serieId, Long directorId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle una serie a un director con id = {0}", serieId);
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		
		Optional<ParticipanteEntity> directorEntity = directorRepository.findById(directorId);
		if(directorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		
		serieEntity.get().getDirectores().add(directorEntity.get());
		log.info("Termina proceso de agregarle una serie a un director id = {0}", directorId);
		return serieEntity.get();

	}

	@Transactional
	public List<SerieEntity> getSeries(Long directorId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar las series asociados a un director con id = {0}", directorId);
		Optional<ParticipanteEntity> directorEntity = directorRepository.findById(directorId);
		if(directorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		
		return directorEntity.get().getSeriesDirigidas();
	}


	@Transactional
	public SerieEntity getSerie(Long directorId, Long serieId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar la serie con id = {0} del director con id = " + directorId, serieId);
		
		Optional<ParticipanteEntity> directorEntity = directorRepository.findById(directorId);
		if(directorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
				
		log.info("Termina proceso de consultar serie con id = {0} del director con id = " + directorId, serieId);
		
		if(!serieEntity.get().getDirectores().contains(directorEntity.get()))
			throw new IllegalOperationException("La serie no esta asociada con el director");
		
		return serieEntity.get();
	}


	@Transactional
	public List<SerieEntity> replaceSeries(Long directorId, List<SerieEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las series asociadas al director con id = {0}", directorId);
		Optional<ParticipanteEntity> directorEntity = directorRepository.findById(directorId);
		if (directorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		for (SerieEntity serie : list) {
			Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
			if (serieEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

			if (!directorEntity.get().getSeriesDirigidas().contains(serieEntity.get()))
				directorEntity.get().getSeriesDirigidas().add(serieEntity.get());
		}
		log.info("Termina proceso de reemplazar las series asociadas al director con id = {0}", directorId);
		return getSeries(directorId);
	
	}

	@Transactional
	public void removeSerie(Long directorId, Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una serie del director con id = {0}", directorId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		Optional<ParticipanteEntity> directorEntity = directorRepository.findById(directorId);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		if (directorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PARTICIPANTE_NOT_FOUND);

		directorEntity.get().getSeriesDirigidas().remove(serieEntity.get());

		log.info("Termina proceso de borrar una serie del director con id = {0}", directorId);
	}


}