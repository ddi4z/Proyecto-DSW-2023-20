package co.edu.uniandes.dse.series.services;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.EpisodioRepository;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SerieService {

    @Autowired
    SerieRepository serieRepository;

    @Autowired
    EpisodioRepository episodioRepository;


    @Transactional
    public SerieEntity createSerie(SerieEntity serieEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la serie");
        if ((serieEntity.getNombre() == null) || (serieEntity.getNombre().equals("")))
            throw new IllegalOperationException("El nombre de la serie no puede ser nulo o vacío");

        if ((serieEntity.getSinopsis() == null) || (serieEntity.getSinopsis().equals("")))
            throw new IllegalOperationException("La sinopsis de la serie no puede ser nula o vacía");

        if ((serieEntity.getImagen() == null) || (serieEntity.getImagen().equals("")))
            throw new IllegalOperationException("La imagen de la serie no puede ser nula o vacía");

        if ((serieEntity.getWallpaper() == null) || (serieEntity.getWallpaper().equals("")))
            throw new IllegalOperationException("El wallpaper de la serie no puede ser nulo o vacío");

        if ((serieEntity.getAnio() == null) || (serieEntity.getAnio().equals(0)))
            throw new IllegalOperationException("El anio de la serie no puede ser nulo o vacío");

        log.info("Termina proceso de creación de la serie");
        return serieRepository.save(serieEntity);
    }

    @Transactional
    public List<SerieEntity> getSeries(){
        log.info("Inicia proceso de consultar todas las series");
        return serieRepository.findAll();
    }

    @Transactional
    public List<SerieEntity> getSeriesAlfabetico(){
        log.info("Inicia proceso de consultar todas las series ordenadas alfabéticamente");
        List<SerieEntity> series = serieRepository.findAll();
        series.sort((SerieEntity s1, SerieEntity s2) -> s1.getNombre().compareTo(s2.getNombre()));
        return series;
    }


    @Transactional
    public List<SerieEntity> getSeriesAntiAlfabetico(){
        log.info("Inicia proceso de consultar todas las series ordenadas antialfabéticamente");
        List<SerieEntity> series = serieRepository.findAll();
        series.sort((SerieEntity s1, SerieEntity s2) -> s2.getNombre().compareTo(s1.getNombre()));
        return series;
    }


    @Transactional
    public SerieEntity getSerie(Long serieId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la serie con id = {0}", serieId);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        log.info("Termina proceso de consultar la serie con id = {0}", serieId);
        return serieEntity.get();
    }

    @Transactional
    public List<SerieEntity> getSerieByNombre(String nombre) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la serie con nombre ", nombre);
        List<SerieEntity> serieEntity = serieRepository.findByNombre(nombre);
        if (serieEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
        log.info("Termina proceso de consultar la serie con nombre", nombre);
        return serieEntity;
    }

    @Transactional
    public SerieEntity updateSerie(Long serieId, SerieEntity nuevaSerie)throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la serie con id = {0}", serieId);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        if ((nuevaSerie.getNombre() == null) || (nuevaSerie.getNombre().equals("")))
            throw new IllegalOperationException("El nombre de la serie no puede ser nulo o vacío");

        if ((nuevaSerie.getSinopsis() == null) || (nuevaSerie.getSinopsis().equals("")))
            throw new IllegalOperationException("La sinopsis de la serie no puede ser nula o vacía");

        if ((nuevaSerie.getImagen() == null) || (nuevaSerie.getImagen().equals("")))
            throw new IllegalOperationException("La imagen de la serie no puede ser nula o vacía");

       if ((nuevaSerie.getWallpaper() == null) || (nuevaSerie.getWallpaper().equals("")))
            throw new IllegalOperationException("El wallpaper de la serie no puede ser nulo o vacío");

        if ((nuevaSerie.getAnio() == null) || (nuevaSerie.getAnio().equals(0)))
            throw new IllegalOperationException("El anio de la serie no puede ser nulo o vacío");

        nuevaSerie.setId(serieId);
        log.info("Termina proceso de actualizar la serie con id = {0}", serieId);
        return serieRepository.save(nuevaSerie);
    }

    @Transactional
    public void deleteSerie(Long serieId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la serie con id = {0}", serieId);
        Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
        if (serieEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

        List<ParticipanteEntity> actores = serieEntity.get().getActores();
        if (!actores.isEmpty())
            throw new IllegalOperationException(ErrorMessage.DIRECTOR_ASOCIADO);

        List<ParticipanteEntity> directores = serieEntity.get().getDirectores();
        if (!directores.isEmpty())
            throw new IllegalOperationException(ErrorMessage.DIRECTOR_ASOCIADO);

        List<CategoriaEntity> categorias = serieEntity.get().getCategorias();
        if (!categorias.isEmpty())
            throw new IllegalOperationException(ErrorMessage.DIRECTOR_ASOCIADO);

        List<PlataformaEntity> plataformas = serieEntity.get().getPlataformas();
        if (!plataformas.isEmpty())
            throw new IllegalOperationException(ErrorMessage.DIRECTOR_ASOCIADO);

        serieRepository.deleteById(serieId);
        log.info("Termina proceso de borrar la serie con id = {0}", serieId);
    }

}








