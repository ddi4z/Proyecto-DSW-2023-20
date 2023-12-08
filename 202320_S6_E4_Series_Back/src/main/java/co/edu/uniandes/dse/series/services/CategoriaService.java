package co.edu.uniandes.dse.series.services;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CategoriaService {
        @Autowired
        CategoriaRepository categoriaRepository;

        @Transactional
        public CategoriaEntity createCategoria(CategoriaEntity categoriaEntity) throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de creación de la categoría");

                if (categoriaEntity.getNombre() == null)
                        throw new IllegalOperationException("Categoría is not valid");

                log.info("Termina proceso de creación de la categoría");
                return categoriaRepository.save(categoriaEntity);
        }
        @Transactional
        public List<CategoriaEntity> getCategorias() {
                log.info("Inicia proceso de consultar todas las categorías");
                return categoriaRepository.findAll();

        }


        @Transactional
        public CategoriaEntity getCategoria(Long categoriaId) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar la categoría con Id = {0}", categoriaId);
                Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
                if (categoriaEntity.isEmpty())
                        throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);
                log.info("Termina proceso de consultar la categoría con Id = {0}", categoriaId);
                return categoriaEntity.get();
        }


        @Transactional
        public CategoriaEntity updateCategoria(Long categoriaId, CategoriaEntity categoria)
                        throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la categoria con Id = {0}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        categoria.setId(categoriaId);
        log.info("Termina proceso de actualizar la categoria con id = {0}", categoriaId);
        return categoriaRepository.save(categoria);
        }


        @Transactional
        public void deleteCategoria(Long categoriaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la categoria con id = {0}", categoriaId);
        Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
        if (categoriaEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

        List<SerieEntity> series = categoriaEntity.get().getSeries();

        if (!series.isEmpty())
                throw new IllegalOperationException("No se puede eliminar la categoria porque tiene series asociadas");

        categoriaRepository.deleteById(categoriaId);
        log.info("Termina proceso de borrar la categoria con id = {0}", categoriaId);
        }




}
