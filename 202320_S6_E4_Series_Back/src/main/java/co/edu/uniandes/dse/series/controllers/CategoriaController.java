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
import co.edu.uniandes.dse.series.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CategoriaDetailDTO> findAll() {
        List<CategoriaEntity> categorias = categoriaService.getCategorias();
        return modelMapper.map(categorias, new TypeToken<List<CategoriaDetailDTO>>(){}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoriaDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        CategoriaEntity categoriaEntity = categoriaService.getCategoria(id);
        return modelMapper.map(categoriaEntity, CategoriaDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoriaDTO create(@RequestBody CategoriaDTO categoriaDTO) throws IllegalOperationException, EntityNotFoundException {
        CategoriaEntity categoriaEntity = categoriaService.createCategoria(modelMapper.map(categoriaDTO, CategoriaEntity.class));
        return modelMapper.map(categoriaEntity, CategoriaDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoriaDTO update(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO)
                        throws EntityNotFoundException, IllegalOperationException {
        CategoriaEntity categoriaEntity = categoriaService.updateCategoria(id, modelMapper.map(categoriaDTO, CategoriaEntity.class));
        return modelMapper.map(categoriaEntity, CategoriaDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        categoriaService.deleteCategoria(id);
    }

}
