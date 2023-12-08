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

import co.edu.uniandes.dse.series.dto.SerieDTO;
import co.edu.uniandes.dse.series.dto.SerieDetailDTO;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.PlataformaSerieService;

@RestController
@RequestMapping("/plataformas")
public class PlataformaSerieController{
    @Autowired
    private PlataformaSerieService plataformaSerieService;

    @Autowired
    private ModelMapper modelMapper;

    //getSerie
    @GetMapping(value = "/{plataformaId}/series/{serieId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SerieDetailDTO getSerie(@PathVariable("plataformaId") Long plataformaId, @PathVariable("serieId") Long serieId)
        throws EntityNotFoundException, IllegalOperationException {
            SerieEntity serieEntity = plataformaSerieService.getSerie(plataformaId, serieId);
            return modelMapper.map(serieEntity, SerieDetailDTO.class);
        }
    //getSeries
    @GetMapping(value = "/{plataformaId}/series")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> getSeries(@PathVariable("plataformaId") Long plataformaId)
        throws EntityNotFoundException {
            List<SerieEntity> serieEntity = plataformaSerieService.getSeries(plataformaId);
            return modelMapper.map(serieEntity, new TypeToken<List<SerieDetailDTO>>(){}.getType());
        }

    //addSerie
    @PostMapping(value = "/{plataformaId}/series/{serieId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SerieDetailDTO addSerie(@PathVariable("plataformaId") Long plataformaId, @PathVariable("serieId") Long serieId)
        throws EntityNotFoundException {
            SerieEntity serieEntity = plataformaSerieService.addSerie(plataformaId, serieId);
            return modelMapper.map(serieEntity, SerieDetailDTO.class);
        }

    //replaceSerie
    @PutMapping(value = "/{plataformaId}/series")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> replaceSeries(@PathVariable("plataformaId") Long plataformaId, @RequestBody List<SerieDTO> series)
        throws EntityNotFoundException{
            List<SerieEntity> entities = modelMapper.map(series, new TypeToken<List<SerieEntity>>(){}.getType());
            List<SerieEntity> seriesList = plataformaSerieService.replaceSeries(plataformaId, entities);
            return modelMapper.map(seriesList, new TypeToken<List<SerieDetailDTO>>(){}.getType());
        }

    //removeSerie
    @DeleteMapping(value = "/{plataformaId}/series/{serieId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeSerie(@PathVariable("plataformaId") Long directorId, @PathVariable("serieId") Long serieId)
        throws EntityNotFoundException{
            plataformaSerieService.removeSerie(directorId, serieId);
        }


}
