package br.com.jbsneto.services;

import br.com.jbsneto.exceptions.ResourceNotFountException;
import br.com.jbsneto.model.Person;
import br.com.jbsneto.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    private final PersonRepository repository;

    public Person findById(Long id) {
        logger.info("Finding one person");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
    }

    public List<Person> findAll() {
        logger.info("Finding all people");
        return repository.findAll();
    }

    public Person create(Person person) {
        logger.info("Creating onde person");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating onde person");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return repository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        repository.delete(entity);
    }


}
