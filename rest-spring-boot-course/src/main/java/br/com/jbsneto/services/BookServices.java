package br.com.jbsneto.services;

import br.com.jbsneto.controllers.BookController;
import br.com.jbsneto.data.dto.v1.BookDTO;
import br.com.jbsneto.exceptions.RequiredObjectIsNullException;
import br.com.jbsneto.exceptions.ResourceNotFountException;
import br.com.jbsneto.mapper.DozerMapper;
import br.com.jbsneto.model.Book;
import br.com.jbsneto.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Log
@Service
public class BookServices {

    private final BookRepository repository;

    public BookDTO findById(Long id) {
        log.info("Finding one book");
        var entity =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        var dto = DozerMapper.parseObject(entity, BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return dto;
    }

    public List<BookDTO> findAll() {
        log.info("Finding all books");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookDTO.class);
        books.stream()
                .forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return books;
    }

    public BookDTO create(BookDTO book) {
        log.info("Creating one book");
        if(book == null) throw new RequiredObjectIsNullException();
        var entity = DozerMapper.parseObject(book, Book.class);
        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public BookDTO update(BookDTO book) {
        log.info("Updating one book");
        if(book == null) throw new RequiredObjectIsNullException();
        Book entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());
        var dto = DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        log.info("Deleting one person");
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        repository.delete(entity);
    }
}
