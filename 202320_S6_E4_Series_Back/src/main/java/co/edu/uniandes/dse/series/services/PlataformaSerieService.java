package co.edu.uniandes.dse.series.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class PlataformaSerieService {

    @Autowired
    private SerieRepository serieRepository;
    
    @Autowired
    private PlataformaRepository plataformaRepository;

    /*
     * Asocia una Serie existente a un Autor
     * @param serieId Identificador de la instancia de Serie
     * @param plataformaId Identificador de la instancia de Plataforma
     * @return Instancia de SerieEntity que fue asociada a la plataforma
     */
     @Transactional
     public SerieEntity addSerie(Long plataformaId, Long serieId) throws EntityNotFoundException {
        log.info("Inicia el proceso de creacion de asociarle una Serie a la Plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        if(serieEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        serieEntity.get().getPlataformas().add(plataformaEntity.get());
        log.info("Termina el proceso de asociarle una Serie a la plataforma con id = {0}", plataformaId);
        return serieEntity.get();
    }

    

    /*
     * Obtiene una coleccion de instancias de SerieEntity asociadas a una instancia de Plataforma 
     * @param plataformaId Identificador de la instancia de Plataforma
     * @return Coleccion de instancias de SerieEntity 
     */
    @Transactional
    public List<SerieEntity> getSeries(Long plataformaId) throws EntityNotFoundException{
        log.info("Inicia el proceso de consultar todas las series de la plataforma con id={0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

        log.info("Termina el proceso de consultar todas las series de la plataforma con id={0}", plataformaId);
        return plataformaEntity.get().getSeriesPlataforma();
    }

    /*
     * Obtiene una instancia de SerieEntity asociada a una instancia de Plataforma
     * 
     * @param plataformaId Identificador de la instancia de Plataforma
     * @param serieId Identificador de la instancia de Serie
     * @return La entidad de la Serie de la Plataforma 
     */
    @Transactional
    public SerieEntity getSerie(Long plataformaId, Long serieId) throws EntityNotFoundException, IllegalOperationException{
        log.info("Inicia el proceso de consultar la serie con id={0} de la plataforma con id="+plataformaId, serieId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        Optional<SerieEntity> serieEntity  = serieRepository.findById(serieId);

        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        if(serieEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        log.info("Termina el proceso de consultar la serie con id={0} de la plataforma con id="+plataformaId, serieId);
        if(!serieEntity.get().getPlataformas().contains(plataformaEntity.get())){
            throw new IllegalOperationException("La serie no esta asociada con la plataforma");
        }
        return serieEntity.get();
    }

    /*
     * Remplaza las instancias de Serie asociadas a una instancia de Plataforma
     * @param plataformaId Identificador de la instancia de Plataforma
     * @param series Coleccion de instancias de SerieEntity a asociar a una instancia de Plataforma
     * @return Nueva coleccion de SerieEntity asociada a una instancia de Plataforma
     */
    @Transactional
    public List<SerieEntity> addSeries(Long plataformaId, List<SerieEntity> series) throws EntityNotFoundException {
        log.info("Inicia el proceso de remplazar las series asociadas a la plataforma con id={0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if(plataformaEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        
        for(SerieEntity serie : series){
            Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
            if(serieEntity.isEmpty()) throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
            if(!serieEntity.get().getPlataformas().contains(plataformaEntity.get())){
                serieEntity.get().getPlataformas().add(plataformaEntity.get());
            }
        }

        log.info("Termina el proceso de remplazar las series asociadas a la plataforma con id={0}", plataformaId);
        plataformaEntity.get().setSeriesPlataforma(series);
        return plataformaEntity.get().getSeriesPlataforma();
    }

   
    @Transactional
	public List<SerieEntity> replaceSeries(Long plataformaId, List<SerieEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las series asociadas al plataforma con id = {0}", plataformaId);
		Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
		if (plataformaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

		for (SerieEntity serie : list) {
			Optional<SerieEntity> serieEntity = serieRepository.findById(serie.getId());
			if (serieEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

			if (!plataformaEntity.get().getSeriesPlataforma().contains(serieEntity.get()))
				plataformaEntity.get().getSeriesPlataforma().add(serieEntity.get());
		}
		log.info("Termina proceso de reemplazar las series asociadas al plataforma con id = {0}", plataformaId);
		return getSeries(plataformaId);
	
	}



    /**
	 * Desasocia una Serie existente de una Plataforma existente
	 *
	 * @param plataformaId Identificador de la instancia de Plataforma
	 * @param serieId   Identificador de la instancia de Serie
	 */
	@Transactional
	public void removeSerie(Long plataformaId, Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una Serie de la Plataforma con id = {0}", plataformaId);
		Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
		if (plataformaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);

		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getPlataformas().remove(plataformaEntity.get());
		plataformaEntity.get().getSeriesPlataforma().remove(serieEntity.get());
		log.info("Finaliza proceso de borrar una Serie de la Plataforma con id = {0}", plataformaId);
	}
}
