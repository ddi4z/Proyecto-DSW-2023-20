package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.CategoriaRepository;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriaSerieService {

    @Autowired
	private SerieRepository serieRepository;

    @Autowired
	private CategoriaRepository categoriaRepository;

    /**
	 * Agregar una serie a una categoría
	 *
	 * @param serieId      El id de la serie a guardar
	 * @param categoriaId El id de la categoria en la cual se va a guardar la serie.
	 * @return La serie creada.
	 * @throws EntityNotFoundException 
	 */


     @Transactional
	public SerieEntity addSerie(Long serieId, Long categoriaId) throws EntityNotFoundException {
		log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if(categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
		
        serieEntity.get().getCategorias().add(categoriaEntity.get());
        log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
        return serieEntity.get();
	}

    	/**
	 * Retorna todas las series asociadas a una categoria
	 *
	 * @param categoriaId El ID de la categoria buscada
	 * @return La lista de series de la categoria
	 * @throws EntityNotFoundException si la categoria no existe
	 */
	@Transactional
	public List<SerieEntity> getSeries(Long categoriaId) throws EntityNotFoundException {
		log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if(categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
		return categoriaEntity.get().getSeries();
	}

	@Transactional
	public List<SerieEntity> getSeriesAlfabeticamente(Long categoriaId) throws EntityNotFoundException {
		log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

		if(categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        
        log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId);
		List<SerieEntity> series = categoriaEntity.get().getSeries();
		series.sort((SerieEntity s1, SerieEntity s2) -> s1.getNombre().compareTo(s2.getNombre()));
		return series;
	}

	@Transactional
	public List<SerieEntity> getSeriesAntiAlfabeticamente(Long categoriaId) throws EntityNotFoundException {
		log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);

		if(categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
        
        log.info("Termina proceso de consultar todas las series de la categoria con id = {0}", categoriaId);
		List<SerieEntity> series = categoriaEntity.get().getSeries();
		series.sort((SerieEntity s1, SerieEntity s2) -> s2.getNombre().compareTo(s1.getNombre()));
		return series;

	}

	/**
	 * Retorna una serie asociada a una categoria
	 *
	 * @param categoriaId El id de la categoria a buscar.
	 * @param serieId      El id de la serie a buscar
	 * @return La serie encontrada dentro de la categoria.
	 * @throws EntityNotFoundException Si la serie no se encuentra en la categoria
	 * @throws IllegalOperationException Si la serie no está asociada a la categoria
	 */
	@Transactional
	public SerieEntity getSerie(Long categoriaId, Long serieId) throws EntityNotFoundException, IllegalOperationException {
		log.info(ErrorMessage.SERIE_CATEGORIA+categoriaId );
		
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if(categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
		
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if(serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
				
		log.info("Termina proceso de consultar la serie con id = {0} de la categoria con id = " + categoriaId, serieId);
		
		if(!serieEntity.get().getCategorias().contains(categoriaEntity.get()))
			throw new IllegalOperationException("The serie is not associated to the categoria");
		
		return serieEntity.get();
	}

	/**
	 * Remplazar series de una categoria
	 *
	 * @param series        Lista de series que serán los de la categoria.
	 * @param categoriaId El id de la categoria que se quiere actualizar.
	 * @return La lista de series actualizada.
	 * @throws EntityNotFoundException Si la categoria o una serie de la lista no se encuentran
	 */
	@Transactional
	public List<SerieEntity> addSeries(Long categoriaId, List<SerieEntity> series) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las series asociadas a la categoria con id = {0}", categoriaId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		for (SerieEntity serie : series) {
			Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
			if (serieEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

			if (!serieEntity.get().getCategorias().contains(categoriaEntity.get()))
				serieEntity.get().getCategorias().add(categoriaEntity.get());
		}
		log.info("Finaliza proceso de reemplazar las series asociados a la categoria con id = {0}", categoriaId);
		categoriaEntity.get().setSeries(series);
		return categoriaEntity.get().getSeries();
	}

	@Transactional
	public List<SerieEntity> replaceSeries(Long categoriaId, List<SerieEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las series asociadas a la categoria con id = {0}", categoriaId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		for (SerieEntity serie : list) {
			Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
			if (serieEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

			if (!categoriaEntity.get().getSeries().contains(serieEntity.get()))
				categoriaEntity.get().getSeries().add(serieEntity.get());
		}
		log.info("Termina proceso de reemplazar las series asociadas a la categoria con id = {0}", categoriaId);
		return getSeries(categoriaId);
	
	}

    @Transactional
	public void removeSerie(Long categoriaId, Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una serie de la categoria con id = {0}", categoriaId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getCategorias().remove(categoriaEntity.get());
		categoriaEntity.get().getSeries().remove(serieEntity.get());
		log.info("Finaliza proceso de borrar una serie de la categoria con id = {0}", categoriaId);
	}


}