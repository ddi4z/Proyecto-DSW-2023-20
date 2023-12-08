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

import co.edu.uniandes.dse.series.dto.PlataformaDTO;
import co.edu.uniandes.dse.series.dto.PlataformaDetailDTO;
import co.edu.uniandes.dse.series.dto.SerieDetailDTO;
import co.edu.uniandes.dse.series.entities.PlataformaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.services.PlataformaService;

@RestController
@RequestMapping("/plataformas")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PlataformaDetailDTO> findAll() {
        List<PlataformaEntity> plataformas = plataformaService.getPlataformas();
        return modelMapper.map(plataformas, new TypeToken<List<PlataformaDetailDTO>>(){}.getType());
    }

    @GetMapping(value = "/alfabeticamente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> getPlataformasAlfabetico() {
        List<PlataformaEntity> plataformas = plataformaService.getPlataformasAlfabeticamente();
        return modelMapper.map(plataformas, new TypeToken<List<PlataformaDetailDTO>>(){}.getType());
    }

    @GetMapping(value = "/antiAlfabeticamente")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SerieDetailDTO> getPlataformasAntiAlfabetico() {
        List<PlataformaEntity> plataformas = plataformaService.getPlataformasAntiAlfabeticamente();
        return modelMapper.map(plataformas, new TypeToken<List<PlataformaDetailDTO>>(){}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PlataformaDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        PlataformaEntity plataformaEntity = plataformaService.getPlataforma(id);
        return modelMapper.map(plataformaEntity, PlataformaDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PlataformaDTO create(@RequestBody PlataformaDTO plataformaDTO) throws IllegalOperationException, EntityNotFoundException {
        PlataformaEntity plataformaEntity = plataformaService.createPlataforma(modelMapper.map(plataformaDTO, PlataformaEntity.class));
        return modelMapper.map(plataformaEntity, PlataformaDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PlataformaDTO update(@PathVariable("id") Long id, @RequestBody PlataformaDTO plataformaDTO)
                        throws EntityNotFoundException, IllegalOperationException {
        PlataformaEntity plataformaEntity = plataformaService.updatePlataforma(id, modelMapper.map(plataformaDTO, PlataformaEntity.class));
        return modelMapper.map(plataformaEntity, PlataformaDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        plataformaService.deletePlataforma(id);
    }

}
