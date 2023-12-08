package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.PlataformaRepository;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeriePlataformaService {

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private PlataformaRepository plataformaRepository;

    	/**
	 * Asocia un Plataforma existente a un Serie
	 *
	 * @param serieId   Identificador de la instancia de Serie
	 * @param plataformaId Identificador de la instancia de Plataforma
	 * @return Instancia de PlataformaEntity que fue asociada a Serie
	 */
	@Transactional
	public PlataformaEntity addPlataforma(Long serieId, Long plataformaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un plataforma al serie con id = {0}", serieId);
		Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
		if (plataformaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getPlataformas().add(plataformaEntity.get());
		log.info("Termina proceso de asociarle un plataforma al serie con id = {0}", serieId);
		return plataformaEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de PlataformaEntity asociadas a una instancia
	 * de Serie
	 *
	 * @param serieId Identificador de la instancia de Serie
	 * @return Colección de instancias de PlataformaEntity asociadas a la instancia de
	 *         Serie
	 */
	@Transactional
	public List<PlataformaEntity> getPlataformas(Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los plataformas del serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los plataformas del serie con id = {0}", serieId);
		return serieEntity.get().getPlataformas();
	}

	/**
	 * Obtiene una instancia de PlataformaEntity asociada a una instancia de Serie
	 *
	 * @param serieId   Identificador de la instancia de Serie
	 * @param plataformaId Identificador de la instancia de Plataforma
	 * @return La entidad del Plataforma asociada al serie
	 */
	@Transactional
	public PlataformaEntity getPlataforma(Long serieId, Long plataformaId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un plataforma del serie con id = {0}", serieId);
		Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (plataformaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		log.info("Termina proceso de consultar un plataforma del serie con id = {0}", serieId);
		if (!serieEntity.get().getPlataformas().contains(plataformaEntity.get()))
			throw new IllegalOperationException("The author is not associated to the book");
		
		return plataformaEntity.get();
	}

	@Transactional
	/**
	 * Remplaza las instancias de Plataforma asociadas a una instancia de Serie
	 *
	 * @param serieId Identificador de la instancia de Serie
	 * @param list    Colección de instancias de PlataformaEntity a asociar a instancia
	 *                de Serie
	 * @return Nueva colección de PlataformaEntity asociada a la instancia de Serie
	 */
	public List<PlataformaEntity> replacePlataformas(Long serieId, List<PlataformaEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los plataformas del serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		for (PlataformaEntity author : list) {
			Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(author.getId());
			if (plataformaEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

			if (!serieEntity.get().getPlataformas().contains(plataformaEntity.get()))
				serieEntity.get().getPlataformas().add(plataformaEntity.get());
		}
		log.info("Termina proceso de reemplazar los plataformas del serie con id = {0}", serieId);
		return getPlataformas(serieId);
	}

	@Transactional
	/**
	 * Desasocia un Plataforma existente de un Serie existente
	 *
	 * @param serieId   Identificador de la instancia de Serie
	 * @param plataformaId Identificador de la instancia de Plataforma
	 */
	public void removePlataforma(Long serieId, Long plataformaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un plataforma del serie con id = {0}", serieId);
		Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (plataformaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getPlataformas().remove(plataformaEntity.get());

		log.info("Termina proceso de borrar un plataforma del serie con id = {0}", serieId);
	}




}




