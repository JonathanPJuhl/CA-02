
package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;


public class PersonDTO {
    
    private String email;
    private String firstName;
    private String lastName;
    private List<Phone> phones;
    private Address address;
    private List<Hobby> hobbies;


    public PersonDTO(Person person, Address address, List<Phone> phones, List<Hobby> hobby) {
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phones = phones;
        this.address = address;
        this.hobbies = hobby;
    }

    public PersonDTO(Person pers) {
        this.email = pers.getEmail();
        this.firstName = pers.getFirstName();
        this.lastName = pers.getLastName();
    }

    public static List<PersonDTO> getDtos(List<Person> persons){
        List<PersonDTO> pdtos = new ArrayList();
        persons.forEach(pers->pdtos.add(new PersonDTO(pers)));
        return pdtos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
    
    
    
}
