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
import co.edu.uniandes.dse.series.services.SerieService;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> findAll() {
            List<SerieEntity> series = serieService.getSeries();
            return modelMapper.map(series, new TypeToken<List<SerieDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/alfabeticamente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> getSeriesAlfabetico() {
            List<SerieEntity> series = serieService.getSeriesAlfabetico();
            return modelMapper.map(series, new TypeToken<List<SerieDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/antiAlfabeticamente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> getSeriesAntiAlfabetico() {
            List<SerieEntity> series = serieService.getSeriesAntiAlfabetico();
            return modelMapper.map(series, new TypeToken<List<SerieDetailDTO>>() {}.getType());
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public SerieDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            SerieEntity serieEntity = serieService.getSerie(id);
            return modelMapper.map(serieEntity, SerieDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SerieDTO create(@RequestBody SerieDTO serieDTO) throws IllegalOperationException, EntityNotFoundException {
            SerieEntity serieEntity = serieService.createSerie(modelMapper.map(serieDTO, SerieEntity.class));
            return modelMapper.map(serieEntity, SerieDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public SerieDTO update(@PathVariable("id") Long id, @RequestBody SerieDTO serieDTO) throws EntityNotFoundException, IllegalOperationException {
            SerieEntity serieEntity = serieService.updateSerie(id, modelMapper.map(serieDTO, SerieEntity.class));
            return modelMapper.map(serieEntity, SerieDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
            serieService.deleteSerie(id);
    }

}