package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.PlataformaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlataformaService {

    @Autowired
    PlataformaRepository plataformaRepository;


    @Transactional
    public PlataformaEntity createPlataforma(PlataformaEntity plataformaEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la plataforma");

        if(plataformaEntity.getNombre() == null || plataformaEntity.getNombre().equals("")){
            throw new IllegalOperationException("El nombre de la plataforma no es valido");
        }

        if(plataformaEntity.getSitioWeb() == null || plataformaEntity.getSitioWeb().equals("")){
            throw new IllegalOperationException("El sitio web de la plataforma no es valido");
        }

        if(plataformaEntity.getLogo() == null || plataformaEntity.getLogo().equals("")){
            throw new IllegalOperationException("El logo de la plataforma no es valido");
        }

        log.info("Termina proceso de creación de plataforma");
        return plataformaRepository.save(plataformaEntity);
    }

        @Transactional
        public List<PlataformaEntity> getPlataformas() {
                log.info("Inicia proceso de consultar todas las plataformas");
                return plataformaRepository.findAll();

        }

        @Transactional
        public List<PlataformaEntity> getPlataformasAlfabeticamente() {
                log.info("Inicia proceso de consultar todas las plataformas por orden alfabetico");
                List<PlataformaEntity> plataformas = plataformaRepository.findAll();
                plataformas.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
                return plataformas;

        }

        @Transactional
        public List<PlataformaEntity> getPlataformasAntiAlfabeticamente() {
                log.info("Inicia proceso de consultar todas las plataformas anti alfabetico");
                List<PlataformaEntity> plataformas = plataformaRepository.findAll();
                plataformas.sort((p1, p2) -> p2.getNombre().compareTo(p1.getNombre()));
                return plataformas;

        }

    @Transactional
    public PlataformaEntity getPlataforma(Long plataformaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar una plataforma con id  = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if (plataformaEntity.isEmpty()){
            throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        }
                
        log.info("Termina proceso de consultar plataforma con id = {0}", plataformaId);
        return plataformaEntity.get();
    }

    @Transactional
    public PlataformaEntity updatePlataforma(Long plataformaId, PlataformaEntity plataforma) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la plataforma con id = {0}", plataforma);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if (plataformaEntity.isEmpty()){
            throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        }

        if(plataforma.getNombre() == null || plataforma.getNombre().equals("")){
            throw new IllegalOperationException("El nombre de la plataforma no es valido");
        }

        if(plataforma.getSitioWeb() == null || plataforma.getSitioWeb().equals("")){
            throw new IllegalOperationException("El sitio web de la plataforma no es valido");
        }

        if(plataforma.getLogo() == null || plataforma.getLogo().equals("")){
            throw new IllegalOperationException("El logo de la plataforma no es valido");
        }

        plataforma.setId(plataformaId);
        log.info("Termina proceso de actualizar la plataforma con id = {0}", plataformaId);
        return plataformaRepository.save(plataforma);
    }

    @Transactional
    public void deletePlataforma(Long plataformaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la plataforma con id = {0}", plataformaId);
        Optional<PlataformaEntity> plataformaEntity = plataformaRepository.findById(plataformaId);
        if (plataformaEntity.isEmpty()){
            throw new EntityNotFoundException(ErrorMessage.PLATAFORMA_NOT_FOUND);
        }

        List<SerieEntity> series = plataformaEntity.get().getSeriesPlataforma();
        if (!series.isEmpty()){
            throw new IllegalOperationException("Imposible borrar plataforma porque tiene una serie asociada");
        }
            
        plataformaRepository.deleteById(plataformaId);
        log.info("Termina proceso de borrar la plataforma con id = {0}", plataformaId);
    }

}