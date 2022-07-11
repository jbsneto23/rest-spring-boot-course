package br.com.jbsneto.unittests.mapper;

import br.com.jbsneto.data.dto.v1.BookDTO;
import br.com.jbsneto.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> Books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            Books.add(mockEntity(i));
        }
        return Books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> Books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Books.add(mockDTO(i));
        }
        return Books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(number * 10D);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setKey(number.longValue());
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(number * 10D);
        return book;
    }
    
}
