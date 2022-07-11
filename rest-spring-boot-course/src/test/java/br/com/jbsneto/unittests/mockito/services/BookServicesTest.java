package br.com.jbsneto.unittests.mockito.services;

import br.com.jbsneto.data.dto.v1.BookDTO;
import br.com.jbsneto.exceptions.RequiredObjectIsNullException;
import br.com.jbsneto.model.Book;
import br.com.jbsneto.repositories.BookRepository;
import br.com.jbsneto.services.BookServices;
import br.com.jbsneto.unittests.mapper.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

    private MockBook input;

    private BookServices service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        input = new MockBook();
        service = new BookServices(repository);
    }

    @Test
    void testFindById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));
        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(10D, result.getPrice());
        assertEquals(entity.getLaunchDate(), result.getLaunchDate());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));
    }

    @Test
    void testFindAll() {
        List<Book> entities = input.mockEntityList();
        when(repository.findAll())
                .thenReturn(entities);
        var books = service.findAll();

        assertNotNull(books);
        assertTrue(books.size() == 14);

        var book1 = books.get(1);

        assertNotNull(book1);
        assertNotNull(book1.getKey());
        assertEquals(1L, book1.getKey());
        assertEquals("Author Test1", book1.getAuthor());
        assertEquals("Title Test1", book1.getTitle());
        assertEquals(10D, book1.getPrice());
        assertEquals(entities.get(1).getLaunchDate(), book1.getLaunchDate());
        assertTrue(book1.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));

        var book5 = books.get(5);

        assertNotNull(book5);
        assertNotNull(book5.getKey());
        assertEquals(5L, book5.getKey());
        assertEquals("Author Test5", book5.getAuthor());
        assertEquals("Title Test5", book5.getTitle());
        assertEquals(50D, book5.getPrice());
        assertEquals(entities.get(5).getLaunchDate(), book5.getLaunchDate());
        assertTrue(book5.getLinks().toString().contains("</api/book/v1/5>;rel=\"self\""));

        var book12 = books.get(12);

        assertNotNull(book12);
        assertNotNull(book12.getKey());
        assertEquals(12L, book12.getKey());
        assertEquals("Author Test12", book12.getAuthor());
        assertEquals("Title Test12", book12.getTitle());
        assertEquals(120D, book12.getPrice());
        assertEquals(entities.get(12).getLaunchDate(), book12.getLaunchDate());
        assertTrue(book12.getLinks().toString().contains("</api/book/v1/12>;rel=\"self\""));
    }

    @Test
    void testCreate() {
        Book entity = input.mockEntity(1);

        Book persisted = input.mockEntity(1);
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(entity))
                .thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(10D, result.getPrice());
        assertEquals(entity.getLaunchDate(), result.getLaunchDate());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testUpdate() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = input.mockEntity(1);
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);
        dto.setKey(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(repository.save(entity))
                .thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(10D, result.getPrice());
        assertEquals(entity.getLaunchDate(), result.getLaunchDate());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/book/v1/1>;rel=\"self\""));
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testDelete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}
