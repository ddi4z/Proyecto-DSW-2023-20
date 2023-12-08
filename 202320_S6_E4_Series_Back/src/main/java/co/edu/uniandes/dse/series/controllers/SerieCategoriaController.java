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

import co.edu.uniandes.dse.series.dto.CategoriaDTO;
import co.edu.uniandes.dse.series.dto.CategoriaDetailDTO;
import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.SerieCategoriaService;

@RestController
@RequestMapping("/series")
public class SerieCategoriaController {

    @Autowired
	private SerieCategoriaService serieCategoriaService;

	@Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/{serieId}/categorias/{categoriaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CategoriaDetailDTO addCategoria(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		CategoriaEntity categoriaEntity = serieCategoriaService.addCategoria(serieId, categoriaId);
		return modelMapper.map(categoriaEntity, CategoriaDetailDTO.class);
	}

    @GetMapping(value = "/{serieId}/categorias/{categoriaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CategoriaDetailDTO getCategoria(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException, IllegalOperationException {
		CategoriaEntity categoriaEntity = serieCategoriaService.getCategoria(serieId, categoriaId);
		return modelMapper.map(categoriaEntity, CategoriaDetailDTO.class);
	}

    @PutMapping(value = "/{serieId}/categorias")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CategoriaDetailDTO> addCategorias(@PathVariable("serieId") Long serieId, @RequestBody List<CategoriaDTO> categorias)
			throws EntityNotFoundException {
		List<CategoriaEntity> entities = modelMapper.map(categorias, new TypeToken<List<CategoriaEntity>>() {
		}.getType());
		List<CategoriaEntity> categoriasList = serieCategoriaService.replaceCategorias(serieId, entities);
		return modelMapper.map(categoriasList, new TypeToken<List<CategoriaDetailDTO>>() {
		}.getType());
	}

    @GetMapping(value = "/{serieId}/categorias")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CategoriaDetailDTO> getCategorias(@PathVariable("serieId") Long serieId) throws EntityNotFoundException {
		List<CategoriaEntity> categoriaEntity = serieCategoriaService.getCategorias(serieId);
		return modelMapper.map(categoriaEntity, new TypeToken<List<CategoriaDetailDTO>>() {
		}.getType());
	}

    @DeleteMapping(value = "/{serieId}/categorias/{categoriaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeCategoria(@PathVariable("categoriaId") Long categoriaId, @PathVariable("serieId") Long serieId)
			throws EntityNotFoundException {
		serieCategoriaService.removeCategoria(serieId, categoriaId);
	}
}
