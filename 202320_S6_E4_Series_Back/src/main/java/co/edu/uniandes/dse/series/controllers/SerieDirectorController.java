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

import co.edu.uniandes.dse.series.dto.ParticipanteDTO;
import co.edu.uniandes.dse.series.dto.ParticipanteDetailDTO;
import co.edu.uniandes.dse.series.entities.ParticipanteEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.SerieDirectorService;

@RestController
@RequestMapping("/series")
public class SerieDirectorController {

    @Autowired
    private SerieDirectorService serieDirectorService;

    @Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/{serieId}/directores/{participanteId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ParticipanteDetailDTO getDirector(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException, IllegalOperationException {
		ParticipanteEntity entity = serieDirectorService.getDirector(serieId, participanteId);
		return modelMapper.map(entity, ParticipanteDetailDTO.class);
	}

    @GetMapping(value = "/{serieId}/directores")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ParticipanteDetailDTO> getDirectores(@PathVariable("serieId") Long serieId) throws EntityNotFoundException {
		List<ParticipanteEntity> entities = serieDirectorService.getDirectores(serieId);
		return modelMapper.map(entities, new TypeToken<List<ParticipanteDetailDTO>>() {
		}.getType());
	}

    @PostMapping(value = "/{serieId}/directores/{participanteId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ParticipanteDetailDTO addDirector(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException {
		ParticipanteEntity entity = serieDirectorService.addDirector(serieId, participanteId);
		return modelMapper.map(entity, ParticipanteDetailDTO.class);
	}

    @PutMapping(value = "/{serieId}/directores")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ParticipanteDetailDTO> replaceDirectores(@PathVariable("serieId") Long serieId, @RequestBody List<ParticipanteDTO> participantes) throws EntityNotFoundException {
		List<ParticipanteEntity> entities = modelMapper.map(participantes, new TypeToken<List<ParticipanteEntity>>() {
		}.getType());
		List<ParticipanteEntity> list = serieDirectorService.addDirectores(serieId, entities);
		return modelMapper.map(list, new TypeToken<List<ParticipanteDetailDTO>>() {
		}.getType());
	}

    @DeleteMapping(value = "/{serieId}/directores/{participanteId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeDirector(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException {
		serieDirectorService.removeDirector(serieId, participanteId);
	}
}
