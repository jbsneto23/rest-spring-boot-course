package br.com.jbsneto.services;

import br.com.jbsneto.controllers.PersonController;
import br.com.jbsneto.data.dto.v1.PersonDTO;
import br.com.jbsneto.exceptions.RequiredObjectIsNullException;
import br.com.jbsneto.exceptions.ResourceNotFountException;
import br.com.jbsneto.mapper.DozerMapper;
import br.com.jbsneto.model.Person;
import br.com.jbsneto.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    private final PersonRepository repository;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person");
        var entity =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        var dto = DozerMapper.parseObject(entity, PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people");
        var people = DozerMapper.parseListObjects(repository.findAll(), PersonDTO.class);
        people.stream()
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return people;
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating onde person");
        if(person == null) throw new RequiredObjectIsNullException();
        var entity = DozerMapper.parseObject(person, Person.class);
        var dto = DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person");
        if(person == null) throw new RequiredObjectIsNullException();
        Person entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var dto = DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        repository.delete(entity);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling a person");
        repository.disablePerson(id);
        var entity =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        var dto = DozerMapper.parseObject(entity, PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }



}
