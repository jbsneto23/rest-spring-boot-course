package br.com.jbsneto.services;

import br.com.jbsneto.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById(String id) {
        logger.info("Finding one person");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("João");
        person.setLastName("Souza");
        person.setAddress("Natal");
        person.setGender("Homem");
        return person;
    }

    public List<Person> findAll() {
        logger.info("Finding all people");
        List<Person> people = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            people.add(mockPerson(i));
        }
        return people;
    }

    public Person create(Person person) {
        logger.info("Creating onde person");
        person.setId(counter.incrementAndGet());
        return person;
    }

    public Person update(Person person) {
        logger.info("Updating onde person");
        return person;
    }

    public void delete(String id) {
        logger.info("Deleting one person");
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person name " + i);
        person.setLastName("Person last name " + i);
        person.setAddress("Address " + i);
        person.setGender("Male");
        return person;
    }

}
