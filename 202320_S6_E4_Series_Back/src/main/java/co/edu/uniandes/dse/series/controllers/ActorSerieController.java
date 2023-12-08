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
import co.edu.uniandes.dse.series.services.ActorSerieService;

@RestController
@RequestMapping("/actores")
public class ActorSerieController {

    @Autowired
    private ActorSerieService actorSerieService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(value = "/{actorId}/series/{serieId}")
    @ResponseStatus(code = HttpStatus.OK)
	public SerieDetailDTO getSerie(@PathVariable("actorId") Long actorId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException, IllegalOperationException {
		SerieEntity serieEntity = actorSerieService.getSerie(actorId, serieId);
		return modelMapper.map(serieEntity, SerieDetailDTO.class);
	}

    @GetMapping(value = "/{actorId}/series")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> getSeries(@PathVariable("actorId") Long actorId) throws EntityNotFoundException {
		List<SerieEntity> serieEntity = actorSerieService.getSeries(actorId);
		return modelMapper.map(serieEntity, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());
	}

    @PostMapping(value = "/{actorId}/series/{serieId}")
	@ResponseStatus(code = HttpStatus.OK)
	public SerieDetailDTO addSerie(@PathVariable("actorId") Long actorId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		SerieEntity serieEntity = actorSerieService.addSerie(actorId, serieId);
		return modelMapper.map(serieEntity, SerieDetailDTO.class);
	}

    @PutMapping(value = "/{actorId}/series")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> replaceSeries(@PathVariable("actorId") Long actorId, @RequestBody List<SerieDTO> series)
			throws EntityNotFoundException {
		List<SerieEntity> entities = modelMapper.map(series, new TypeToken<List<SerieEntity>>() {
		}.getType());
		List<SerieEntity> seriesList = actorSerieService.replaceSeries(actorId, entities);
		return modelMapper.map(seriesList, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());

	}

    @DeleteMapping(value = "/{actorId}/series/{serieId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeSerie(@PathVariable("actorId") Long actorId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		actorSerieService.removeSerie(actorId, serieId);
	}

}
