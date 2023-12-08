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
import co.edu.uniandes.dse.series.services.CategoriaSerieService;


@RestController
@RequestMapping("/categorias")
public class CategoriaSerieController {

    @Autowired
	private CategoriaSerieService categoriaSerieService;

	@Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/{categoriaId}/series/{serieId}")
	@ResponseStatus(code = HttpStatus.OK)
	public SerieDetailDTO getSerie(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException, IllegalOperationException {
		SerieEntity serieEntity = categoriaSerieService.getSerie(categoriaId, serieId);
		return modelMapper.map(serieEntity, SerieDetailDTO.class);
	}

    @GetMapping(value = "/{categoriaId}/series")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> getSeries(@PathVariable("categoriaId") Long categoriaId) throws EntityNotFoundException {
		List<SerieEntity> serieEntity = categoriaSerieService.getSeries(categoriaId);
		return modelMapper.map(serieEntity, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());
	}
	@GetMapping(value = "/{categoriaId}/series/alfabeticamente")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> getSeriesAlfabeticamente(@PathVariable("categoriaId") Long categoriaId) throws EntityNotFoundException {
		List<SerieEntity> serieEntity = categoriaSerieService.getSeriesAlfabeticamente(categoriaId);
		return modelMapper.map(serieEntity, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());
	}

	@GetMapping(value = "/{categoriaId}/series/antiAlfabeticamente")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> getSeriesAntiAlfabeticamente(@PathVariable("categoriaId") Long categoriaId) throws EntityNotFoundException {
		List<SerieEntity> serieEntity = categoriaSerieService.getSeriesAntiAlfabeticamente(categoriaId);
		return modelMapper.map(serieEntity, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());
	}

    @PostMapping(value = "/{categoriaId}/series/{serieId}")
	@ResponseStatus(code = HttpStatus.OK)
	public SerieDetailDTO addSerie(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		SerieEntity serieEntity = categoriaSerieService.addSerie(categoriaId, serieId);
		return modelMapper.map(serieEntity, SerieDetailDTO.class);
	}

    @PutMapping(value = "/{categoriaId}/series")
	@ResponseStatus(code = HttpStatus.OK)
	public List<SerieDetailDTO> replaceSeries(@PathVariable("categoriaId") Long categoriaId, @RequestBody List<SerieDTO> series)
			throws EntityNotFoundException {
		List<SerieEntity> entities = modelMapper.map(series, new TypeToken<List<SerieEntity>>() {
		}.getType());
		List<SerieEntity> seriesList = categoriaSerieService.replaceSeries(categoriaId, entities);
		return modelMapper.map(seriesList, new TypeToken<List<SerieDetailDTO>>() {
		}.getType());

	}

    @DeleteMapping(value = "/{categoriaId}/series/{serieId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeSerie(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		categoriaSerieService.removeSerie(categoriaId, serieId);
	}
}
