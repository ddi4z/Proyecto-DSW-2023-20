package co.edu.uniandes.dse.series.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.series.entities.SerieEntity;
import co.edu.uniandes.dse.series.entities.CategoriaEntity;
import co.edu.uniandes.dse.series.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.series.exceptions.ErrorMessage;
import co.edu.uniandes.dse.series.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.series.repositories.SerieRepository;
import co.edu.uniandes.dse.series.repositories.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SerieCategoriaService {
    
    @Autowired
	private SerieRepository serieRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * Asocia un Author existente a un Book
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 * @return Instancia de AuthorEntity que fue asociada a Book
	 */
	@Transactional
	public CategoriaEntity addCategoria(Long serieId, Long categoriaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle una categoria a la serie con id = {0}", serieId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getCategorias().add(categoriaEntity.get());
		log.info("Termina proceso de asociarle una categoria a la serie con id = {0}", serieId);
		return categoriaEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de AuthorEntity asociadas a una instancia
	 * de Book
	 *
	 * @param bookId Identificador de la instancia de Book
	 * @return Colección de instancias de AuthorEntity asociadas a la instancia de
	 *         Book
	 */
	@Transactional
	public List<CategoriaEntity> getCategorias(Long serieId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todas las categorias de la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		log.info("Finaliza proceso de consultar todas las categorias de la serie con id = {0}", serieId);
		return serieEntity.get().getCategorias();
	}

	/**
	 * Obtiene una instancia de AuthorEntity asociada a una instancia de Book
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 * @return La entidad del Autor asociada al libro
	 */
	@Transactional
	public CategoriaEntity getCategoria(Long serieId, Long categoriaId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar una categoria de la serie con id = {0}", serieId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);
		log.info("Termina proceso de consultar una categoria de la serie con id = {0}", serieId);
		if (!serieEntity.get().getCategorias().contains(categoriaEntity.get()))
			throw new IllegalOperationException("The categoria is not associated to the serie");
		
		return categoriaEntity.get();
	}

	@Transactional
	/**
	 * Remplaza las instancias de Author asociadas a una instancia de Book
	 *
	 * @param bookId Identificador de la instancia de Book
	 * @param list    Colección de instancias de AuthorEntity a asociar a instancia
	 *                de Book
	 * @return Nueva colección de AuthorEntity asociada a la instancia de Book
	 */
	public List<CategoriaEntity> replaceCategorias(Long serieId, List<CategoriaEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las categorias de la serie con id = {0}", serieId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);
		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		for (CategoriaEntity categoria : list) {
			Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoria.getId());
			if (categoriaEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

            if (!serieEntity.get().getCategorias().contains(categoriaEntity.get()))
                    serieEntity.get().getCategorias().add(categoriaEntity.get());
		}
		log.info("Termina proceso de reemplazar las categorias de la serie con id = {0}", serieId);
		return getCategorias(serieId);
	}

	@Transactional
	/**
	 * Desasocia un Author existente de un Book existente
	 *
	 * @param bookId   Identificador de la instancia de Book
	 * @param authorId Identificador de la instancia de Author
	 */
	public void removeCategoria(Long serieId, Long categoriaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una categoria de la serie con id = {0}", serieId);
		Optional<CategoriaEntity> categoriaEntity = categoriaRepository.findById(categoriaId);
		Optional<SerieEntity> serieEntity = serieRepository.findById(serieId);

		if (categoriaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CATEGORIA_NOT_FOUND);

		if (serieEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SERIE_NOT_FOUND);

		serieEntity.get().getCategorias().remove(categoriaEntity.get());

		log.info("Termina proceso de borrar una categoria de la serie con id = {0}", serieId);
	}
}
