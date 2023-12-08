package co.edu.uniandes.dse.series.services;

import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


import co.edu.uniandes.dse.series.entities.EpisodioEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.EpisodioRepository;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EpisodioService {
    @Autowired
    EpisodioRepository episodioRepository;

    @Autowired
    SerieRepository serieRepository;

    @Transactional
    public EpisodioEntity createEpisodio(Long serieId, EpisodioEntity episodioEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación del episodio");

        if ((episodioEntity.getNumeroEpisodio() <= 0))
                throw new IllegalOperationException("El número del episodio no puede ser negativo");
        if (episodioEntity.getNombre() == null || episodioEntity.getNombre().equals(""))
                throw new IllegalOperationException(ErrorMessage.INVALID_EPISODIO_NOMBRE);
        if (episodioEntity.getDuracionMinutos() <= 0)
                throw new IllegalOperationException("La duración del episodio no puede ser negativa o nula");
        if ((episodioEntity.getResumen() == null) || (episodioEntity.getResumen().equals("")))
                throw new IllegalOperationException("La descripción del episodio no puede ser nula o vacía");
        if ((episodioEntity.getImagen() == null) || (episodioEntity.getImagen().equals("")))
                throw new IllegalOperationException("La imagen del episodio no puede ser nula o vacía");

        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		episodioEntity.setSerie(serieEntity.get());

        log.info("Termina proceso de creación del episodio");
        return episodioRepository.save(episodioEntity);
    }


    @Transactional
    public List<EpisodioEntity> getEpisodios(Long serieId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar todas los episodios");
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        log.info("Termina proceso de consultar todas los episodios");
        return serieEntity.get().getEpisodios();
    }

    @Transactional
    public List<EpisodioEntity> getEpisodiosAlfabetico(Long serieId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar todas los episodios ordenados alfabéticamente");
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        log.info("Termina proceso de consultar todas los episodios ordenados alfabéticamente");
        List<EpisodioEntity> episodios = serieEntity.get().getEpisodios();
        episodios.sort((EpisodioEntity e1, EpisodioEntity e2) -> e1.getNombre().compareTo(e2.getNombre()));
        return episodios;
    }

    @Transactional
    public List<EpisodioEntity> getEpisodiosAntiAlfabetico(Long serieId) throws EntityNotFoundException{
        log.info("Inicia proceso de consultar todas los episodios ordenados antialfabéticamente");
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        log.info("Termina proceso de consultar todas los episodios ordenados antialfabéticamente");
        List<EpisodioEntity> episodios = serieEntity.get().getEpisodios();
        episodios.sort((EpisodioEntity e1, EpisodioEntity e2) -> e2.getNombre().compareTo(e1.getNombre()));
        return episodios;
    }

    @Transactional
    public EpisodioEntity getEpisodioById(Long serieId,Long episodioId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el episodio con id = {0} de la serie con id: "+serieId, episodioId);
        Optional<EpisodioEntity> episodioEntity = episodioRepository.findById(episodioId);
        if (episodioEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.EPISODIO_NOT_FOUND);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        if (!serieEntity.get().getEpisodios().contains(episodioEntity.get()))
            throw new IllegalOperationException(ErrorMessage.EPISODIO_SERIE_NOT_FOUND);

        log.info("Termina proceso de consultar el episodio con id = {0}", episodioId);
        return episodioEntity.get();
    }
    @Transactional
    public EpisodioEntity getEpisodioByNombre(Long serieId, String nombre) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el episodio con nombre ", nombre);
        Optional<EpisodioEntity> episodioEntity = episodioRepository.findByNombre(nombre);
        if (episodioEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.EPISODIO_NOT_FOUND);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        if (!serieEntity.get().getEpisodios().contains(episodioEntity.get()))
            throw new IllegalOperationException(ErrorMessage.EPISODIO_SERIE_NOT_FOUND);

        log.info("Termina proceso de consultar el episodio con nombre", nombre);
        return episodioEntity.get();
    }



    @Transactional
    public EpisodioEntity updateEpisodio(Long serieId, Long episodioId, EpisodioEntity nuevoEpisodio)throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el episodio con id = {0} de la serie con id: "+serieId, episodioId);
        Optional<EpisodioEntity> episodioEntity = episodioRepository.findById(episodioId);
        if (episodioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.EPISODIO_NOT_FOUND);

        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        if ((nuevoEpisodio.getNombre() == null) || (nuevoEpisodio.getNombre().equals("")))
            throw new IllegalOperationException(ErrorMessage.INVALID_EPISODIO_NOMBRE);

        if ((nuevoEpisodio.getResumen() == null) || (nuevoEpisodio.getResumen().equals("")))
            throw new IllegalOperationException("La sinopsis del episodio no puede ser nula o vacía");

        if ((nuevoEpisodio.getImagen() == null) || (nuevoEpisodio.getImagen().equals("")))
            throw new IllegalOperationException("La imagen del episodio no puede ser nula o vacía");

        if ((nuevoEpisodio.getNumeroEpisodio() <= 0))
                throw new IllegalOperationException("El número del episodio no puede ser negativo");


        if (nuevoEpisodio.getDuracionMinutos() <= 0)
                throw new IllegalOperationException("La duración del episodio no puede ser negativa o nula");
        
        nuevoEpisodio.setId(episodioId);
        nuevoEpisodio.setSerie(serieEntity.get());
        log.info("Termina proceso de actualizar el episodio con id = {0}", episodioId);
        return episodioRepository.save(nuevoEpisodio);
    }

    @Transactional
    public void deleteEpisodio(Long serieId, Long episodioId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el episodio con id = {0} de la serie con id = " + serieId,
        episodioId);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        Optional<EpisodioEntity> episodioEntity = episodioRepository.findById(episodioId);
        if (episodioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.EPISODIO_NOT_FOUND);

        if (!serieEntity.get().getEpisodios().contains(episodioEntity.get()))
            throw new IllegalOperationException(ErrorMessage.EPISODIO_SERIE_NOT_FOUND);
        

        episodioRepository.deleteById(episodioId);
        log.info("Termina proceso de borrar el episodio con id = {0} de la serie con id = "  + serieId,
        episodioId);
    }

}