package br.com.jbsneto.services;

import br.com.jbsneto.data.dto.v1.PersonDTO;
import br.com.jbsneto.exceptions.ResourceNotFountException;
import br.com.jbsneto.mapper.DozerMapper;
import br.com.jbsneto.model.Person;
import br.com.jbsneto.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return DozerMapper.parseObject(entity, PersonDTO.class);
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people");
        return DozerMapper.parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating onde person");
        var entity = DozerMapper.parseObject(person, Person.class);
        return DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFountException("No records found for this ID"));
        repository.delete(entity);
    }


}
