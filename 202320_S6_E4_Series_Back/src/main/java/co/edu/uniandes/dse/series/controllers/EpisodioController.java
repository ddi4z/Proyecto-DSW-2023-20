package co.edu.uniandes.dse.series.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.series.dto.EpisodioDTO;
import co.edu.uniandes.dse.series.entities.EpisodioEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.EpisodioService;

@RestController
@RequestMapping("/serie")
public class EpisodioController {
        @Autowired
        private EpisodioService episodioService;

        @Autowired
        private ModelMapper modelMapper;

    //Metodo para devolver todos los episodios de una serie

        @GetMapping (value = "/{serie}/episodios")
        @ResponseStatus(code = HttpStatus.OK)
        public List<EpisodioDTO> getepisodios(@PathVariable("serie") Long serieId) throws EntityNotFoundException {
            List<EpisodioEntity> episodios = episodioService.getEpisodios(serieId);
            return modelMapper.map(episodios, new TypeToken<List<EpisodioDTO>>() {
            }.getType());
        }

        @GetMapping (value = "/{serie}/episodios/alfabeticamente")
        @ResponseStatus(code = HttpStatus.OK)
        public List<EpisodioDTO> getEpisodiosAlfabetico(@PathVariable("serie") Long serieId) throws EntityNotFoundException {
            List<EpisodioEntity> episodios = episodioService.getEpisodiosAlfabetico(serieId);
            return modelMapper.map(episodios, new TypeToken<List<EpisodioDTO>>() {
            }.getType());
        }
        @GetMapping (value = "/{serie}/episodios/antiAlfabeticamente")
        @ResponseStatus(code = HttpStatus.OK)
        public List<EpisodioDTO> getEpisodiosAntiAlfabetico(@PathVariable("serie") Long serieId) throws EntityNotFoundException {
            List<EpisodioEntity> episodios = episodioService.getEpisodiosAntiAlfabetico(serieId);
            return modelMapper.map(episodios, new TypeToken<List<EpisodioDTO>>() {
            }.getType());
        }
        //Metodo para devolver un episodio de una serie

        @GetMapping(value = "/{serie}/episodios/{episodio_id_1}")
        @ResponseStatus(code = HttpStatus.OK)
        public EpisodioDTO getEpisodio(@PathVariable("serie") Long serieId, @PathVariable("episodio_id_1") Long episodioId) throws EntityNotFoundException, IllegalOperationException {
            EpisodioEntity entity = episodioService.getEpisodioById(serieId, episodioId);
            return modelMapper.map(entity, EpisodioDTO.class);
        }

        //Metodo para crear un episodio en una serie

        @PostMapping(value = "/{serie}/episodios")
        @ResponseStatus(code = HttpStatus.CREATED)
        public EpisodioDTO createEpisodio(@PathVariable("serie") Long serieId, @RequestBody EpisodioDTO episodio) throws IllegalOperationException, EntityNotFoundException {
            EpisodioEntity episodioEntity = modelMapper.map(episodio, EpisodioEntity.class);
            EpisodioEntity entity = episodioService.createEpisodio(serieId, episodioEntity);
            return modelMapper.map(entity, EpisodioDTO.class);
        }

        //Metodo para actualizar un episodio de una serie

        @PutMapping(value = "/{serie}/episodios/{episodio_id_1}")
        @ResponseStatus(code = HttpStatus.OK)
        public EpisodioDTO updateEpisodio(@PathVariable("serie") Long serieId, @PathVariable("episodio_id_1") Long episodioId, @RequestBody EpisodioDTO episodio) throws EntityNotFoundException, IllegalOperationException {
            EpisodioEntity episodioEntity = modelMapper.map(episodio, EpisodioEntity.class);
            EpisodioEntity entity = episodioService.updateEpisodio(serieId, episodioId, episodioEntity);
            return modelMapper.map(entity, EpisodioDTO.class);
        }

        //Metodo para borrar un episodio de una serie

        @DeleteMapping(value = "/{serie}/episodios/{episodio_id_1}")
        @ResponseStatus(code = HttpStatus.NO_CONTENT)
        public void deleteEpisodio(@PathVariable("serie") Long serieId, @PathVariable("episodio_id_1") Long episodioId) throws EntityNotFoundException, IllegalOperationException {
            episodioService.deleteEpisodio(serieId, episodioId);
        }

}







