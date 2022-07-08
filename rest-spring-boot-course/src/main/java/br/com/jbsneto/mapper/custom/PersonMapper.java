package br.com.jbsneto.mapper.custom;

import br.com.jbsneto.data.dto.v2.PersonDTOV2;
import br.com.jbsneto.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        dto.setBirthDate(new Date());
        return dto;
    }

    public Person converDTOTotEntity(PersonDTOV2 dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAddress(dto.getAddress());
        person.setGender(dto.getGender());
        return person;
    }

}
