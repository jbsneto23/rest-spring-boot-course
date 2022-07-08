package br.com.jbsneto.controllers;

import br.com.jbsneto.data.dto.v1.PersonDTO;
import br.com.jbsneto.data.dto.v2.PersonDTOV2;
import br.com.jbsneto.services.PersonServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonServices personService;

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO findById(@PathVariable("id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll() {
        return personService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PostMapping(value = "/v2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTOV2 createV2(@RequestBody PersonDTOV2 person) {
        return personService.createV2(person);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        personService.delete(id);
    }

}
