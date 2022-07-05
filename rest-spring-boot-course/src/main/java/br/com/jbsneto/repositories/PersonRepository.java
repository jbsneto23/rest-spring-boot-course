package br.com.jbsneto.repositories;

import br.com.jbsneto.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
