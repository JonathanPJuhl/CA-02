
package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;


public class PersonDTO {
    
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private List<PhoneDTO> phonesDTO;
    private AddressDTO addressesDTO;
    private List<HobbyDTO> hobbiesDTO;


    public PersonDTO(Person person, AddressDTO addressesDTO, List<PhoneDTO> phonesDTO, List<HobbyDTO> hobbiesDTO) {
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phonesDTO = phonesDTO;
        this.addressesDTO = addressesDTO;
        this.hobbiesDTO = hobbiesDTO;
    }
    
        public PersonDTO(int id, Person person, AddressDTO addressesDTO, List<PhoneDTO> phonesDTO, List<HobbyDTO> hobbiesDTO) {
        this.id = id;
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phonesDTO = phonesDTO;
        this.addressesDTO = addressesDTO;
        this.hobbiesDTO = hobbiesDTO;
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

    public List<PhoneDTO> getPhones() {
        return phonesDTO;
    }

    public void setPhones(List<PhoneDTO> phonesDTO) {
        this.phonesDTO = phonesDTO;
    }

    public AddressDTO getAddress() {
        return addressesDTO;
    }

    public void setAddress(AddressDTO addressDTO) {
        this.addressesDTO = addressDTO;
    }

    public List<HobbyDTO> getHobbies() {
        return hobbiesDTO;
    }

    public void setHobbies(List<HobbyDTO> hobbiesDTO) {
        this.hobbiesDTO = hobbiesDTO;
    }

    public int getId() {
        return id;
    }
    
    
    
}
