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
import co.edu.uniandes.dse.series.services.SerieActorService;







@RestController
@RequestMapping("/series")
public class SerieActorController {

    @Autowired
    private SerieActorService serieActorService;

    @Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/{serieId}/actores/{participanteId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ParticipanteDetailDTO getActor(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException, IllegalOperationException {
		ParticipanteEntity entity = serieActorService.getActor(serieId, participanteId);
		return modelMapper.map(entity, ParticipanteDetailDTO.class);
	}

    @GetMapping(value = "/{serieId}/actores")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ParticipanteDetailDTO> getActores(@PathVariable("serieId") Long serieId) throws EntityNotFoundException {
		List<ParticipanteEntity> entities = serieActorService.getActores(serieId);
		return modelMapper.map(entities, new TypeToken<List<ParticipanteDetailDTO>>() {
		}.getType());
	}

    @PostMapping(value = "/{serieId}/actores/{participanteId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ParticipanteDetailDTO addActor(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException {
		ParticipanteEntity entity = serieActorService.addActor(serieId, participanteId);
		return modelMapper.map(entity, ParticipanteDetailDTO.class);
	}

    @PutMapping(value = "/{serieId}/actores")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ParticipanteDetailDTO> replaceActores(@PathVariable("serieId") Long serieId, @RequestBody List<ParticipanteDTO> participantes) throws EntityNotFoundException {
		List<ParticipanteEntity> entities = modelMapper.map(participantes, new TypeToken<List<ParticipanteEntity>>() {
		}.getType());
		List<ParticipanteEntity> list = serieActorService.addActores(serieId, entities);
		return modelMapper.map(list, new TypeToken<List<ParticipanteDetailDTO>>() {
		}.getType());
	}

    @DeleteMapping(value = "/{serieId}/actores/{participanteId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable("serieId") Long serieId, @PathVariable("participanteId") Long participanteId) throws EntityNotFoundException {
		serieActorService.removeActor(serieId, participanteId);
	}
}
