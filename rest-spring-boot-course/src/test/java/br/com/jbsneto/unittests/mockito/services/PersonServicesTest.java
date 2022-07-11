package br.com.jbsneto.unittests.mockito.services;

import br.com.jbsneto.data.dto.v1.PersonDTO;
import br.com.jbsneto.exceptions.RequiredObjectIsNullException;
import br.com.jbsneto.model.Person;
import br.com.jbsneto.repositories.PersonRepository;
import br.com.jbsneto.services.PersonServices;
import br.com.jbsneto.unittests.mapper.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    private MockPerson input;

    private PersonServices service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        input = new MockPerson();
        service = new PersonServices(repository);
    }

    @Test
    void testFindById() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));
        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));
    }

    @Test
    void testFindAll() {
        List<Person> entities = input.mockEntityList();
        when(repository.findAll())
                .thenReturn(entities);
        var people = service.findAll();

        assertNotNull(people);
        assertTrue(people.size() == 14);

        var person1 = people.get(1);

        assertNotNull(person1.getKey());
        assertEquals(1L, person1.getKey());
        assertEquals("First Name Test1", person1.getFirstName());
        assertEquals("Last Name Test1", person1.getLastName());
        assertEquals("Addres Test1", person1.getAddress());
        assertEquals("Female", person1.getGender());
        assertNotNull(person1.getLinks());
        assertTrue(person1.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));

        var person5 = people.get(5);

        assertNotNull(person1.getKey());
        assertEquals(5L, person5.getKey());
        assertEquals("First Name Test5", person5.getFirstName());
        assertEquals("Last Name Test5", person5.getLastName());
        assertEquals("Addres Test5", person5.getAddress());
        assertEquals("Female", person5.getGender());
        assertNotNull(person5.getLinks());
        assertTrue(person5.getLinks().toString().contains("</api/person/v1/5>;rel=\"self\""));

        var person12 = people.get(12);

        assertNotNull(person1.getKey());
        assertEquals(12L, person12.getKey());
        assertEquals("First Name Test12", person12.getFirstName());
        assertEquals("Last Name Test12", person12.getLastName());
        assertEquals("Addres Test12", person12.getAddress());
        assertEquals("Male", person12.getGender());
        assertNotNull(person12.getLinks());
        assertTrue(person12.getLinks().toString().contains("</api/person/v1/12>;rel=\"self\""));
    }

    @Test
    void testCreate() {
        Person entity = input.mockEntity(1);

        Person persisted = input.mockEntity(1);
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.save(entity))
                .thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
           service.create(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        Person persisted = input.mockEntity(1);
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        dto.setKey(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(repository.save(entity))
                .thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals(1L, result.getKey());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());

        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testDelete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        service.delete(1L);
    }

}
