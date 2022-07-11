package br.com.jbsneto.repositories;

import br.com.jbsneto.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
