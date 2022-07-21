package br.com.jbsneto.repositories;

import br.com.jbsneto.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Modifying
    @Query("update Person p set p.enabled = false where p.id = :id")
    void disablePerson(@Param("id") Long id);
}
